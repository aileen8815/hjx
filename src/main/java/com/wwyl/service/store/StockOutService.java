package com.wwyl.service.store;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Enums.StockOutStatus;
import com.wwyl.Enums.StockRelocationStatus;
import com.wwyl.Enums.StoreLocationStatus;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.StoreLocationDao;
import com.wwyl.dao.store.BookInventoryDao;
import com.wwyl.dao.store.BookInventoryHisDao;
import com.wwyl.dao.store.OutboundRegisterDao;
import com.wwyl.dao.store.StockOutDao;
 
import com.wwyl.dao.store.StockRelocationDao;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.store.OutboundRegister;
import com.wwyl.entity.store.StockOut;
import com.wwyl.entity.store.StockRelocation;
import com.wwyl.service.settings.TaskService;

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class StockOutService {

	@Resource
	private StockOutDao stockOutDao;
	@Resource
	private OutboundRegisterDao outboundRegisterDao;
	@Resource
	private BookInventoryDao bookInventoryDao;
	@Resource
	private StoreLocationDao storeLocationDao;
	@Resource
	private BookInventoryHisDao bookInventoryHisDao;
	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private TaskService taskService;
	@Resource
	private StockRelocationDao stockRelocationDao;
    @Resource
    private SqlSessionTemplate sqlSessionTemplate;
	
	public List<StockOut> findAll() {
		return stockOutDao.findAll();
	}
	
	public StockOut findOne(Long id) {
		return stockOutDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(StockOut stockOut) {
		stockOutDao.save(stockOut);
	}
	/**
	 * ??????
	 * @param stockOut
	 */
	@Transactional(readOnly = false)
	public void cancellation(Long stockOutId) {
		StockOut stockout = stockOutDao.findOne(stockOutId);
		stockout.setStockOutStatus(StockOutStatus.?????????);
		stockOutDao.save(stockout);
	}

	public Page<StockOut> findStockOutItems(int page, int rows,final String serialNo,
			final StockOutStatus stockOutStatus,final Long customerId,final Long storeArea,final Long product) {
		Specification<StockOut> specification = new Specification<StockOut>() {
			@Override
			public Predicate toPredicate(Root<StockOut> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				Path SerialNoPath=	root.get("outboundRegister").get("serialNo");
				Path customerPath=	root.get("outboundRegister").get("customer").get("id");
				Path productPath=	root.get("product").get("id");
				Path storeAreaPath=	root.get("storeLocation").get("storeArea").get("id");
				 
				Path stockOutStatusPath=	root.get("stockOutStatus");
				if (product != null) {
					Predicate p1 = criteriaBuilder.equal(productPath, product);
					list.add(p1);
				}
				if (storeArea != null) {
					Predicate p1 = criteriaBuilder.equal(storeAreaPath, storeArea);
					list.add(p1);
				}
				if (customerId != null) {
					Predicate p1 = criteriaBuilder.equal(customerPath, customerId);
					list.add(p1);
				}
				if (stockOutStatus != null) {
					Predicate p1 = criteriaBuilder.equal(stockOutStatusPath, stockOutStatus);
					list.add(p1);
				}
				if (serialNo != null && serialNo.trim().length() > 0) {
					Predicate p2 = criteriaBuilder.like(SerialNoPath, "%" + serialNo + "%");
					list.add(p2);
				}
			 
				Predicate[] p = new Predicate[list.size()];
				criteriaQuery.orderBy((criteriaBuilder.desc(root.get("serialNo"))));
				return criteriaBuilder.and(list.toArray(p));
				
				 
			}
		};
		return stockOutDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}

	public List<StockOut> findStockOutByInboundReceiptId(Long outboundRegisterId) {
		return stockOutDao.findStockOutByOutboundRegisterId(outboundRegisterId);
	}
	public List<StockOut>  findBystoreContainer(Long container){
		return stockOutDao.findBystoreContainer(container);
	}
	/**
	 * ?????????????????????
	 * 
	 * @param id
	 *            ?????????id
	 * @param outboundRegisterId
	 *            ?????????id
	 */
	@Transactional(readOnly = false)
	public StockOut completePicking(Long id) {
		// ?????????????????????
		StockOut stockOut = findOne(id);
		if (stockOut == null) {
			throw new RuntimeException("???????????????");
		}

		if (StockOutStatus.?????????.compareTo(stockOut.getStockOutStatus()) <= 0) {
			throw new RuntimeException("?????????????????????????????????????????????");
		}

		OutboundRegister outboundRegister = stockOut.getOutboundRegister();
		stockOut.setStockOutStatus(StockOutStatus.?????????);
		stockOut.setStockOutEndTime(new Date());
		stockOut.setStockOutOperator(ThreadLocalHolder.getCurrentOperator());

		// ?????????????????????
		// FIXME ????????????
		List<StockOut> stockOutList = findStockOutByInboundReceiptId(outboundRegister.getId());
		boolean result = true;
		for (StockOut stockOut2 : stockOutList) {
			if (stockOut2.getStockOutStatus() != StockOutStatus.?????????&&stockOut2.getStockOutStatus() != StockOutStatus.?????????) {
				result = false;
				break;
			}

		}

		stockOutDao.save(stockOut);// ??????????????????
		if (result) {
			outboundRegister.setStockOutStatus(StockOutStatus.?????????);
			outboundRegister.setCompleteTime(new Date());
			taskService.assignStockOutEndNotice(outboundRegister);
		} else {
			outboundRegister.setStockOutStatus(StockOutStatus.?????????);
		}
		outboundRegisterDao.save(outboundRegister);// ?????????????????????????????????
		
		//??????????????????????????????
		bookInventoryService.updatebookInventoryHis(stockOut.getStoreContainer().getId());
		 
		
		// ??????????????????
		bookInventoryDao.deleteByProductAndStoreContainer(stockOut.getProduct().getId(), stockOut.getStoreContainer().getId());
		//??????????????????????????????
		storeLocationDao.updateStoreLocationStatus(stockOut.getStoreLocation().getId(), StoreLocationStatus.?????????);
		
		//?????????????????????????????????????????????
		StockRelocation   stockRelocation=stockRelocationDao.findByStoreContainer(stockOut.getStoreContainer().getId(), StockRelocationStatus.?????????);
		if(stockRelocation!=null){
			stockRelocation.setStockRelocationStatus(StockRelocationStatus.??????);
			stockRelocationDao.save(stockRelocation);
		}
		return stockOut;
	}

	public List<StockOut> findUnclaimStockOut(Long storeAreaID) {
		return stockOutDao.findUnclaimStockOut(new StockOutStatus[]{StockOutStatus.?????????, StockOutStatus.?????????}, storeAreaID);
	}
	public List<Long> findUseingStockOut(Object[] productids,Long customerid) {
		return stockOutDao.findUseingStockOut(new StockOutStatus[]{StockOutStatus.?????????, StockOutStatus.?????????},productids,customerid);
	}
	public List<StockOut> findDailyStockOutByCustomerId(Long customerid, Date startDate, Date endDate) {
		return stockOutDao.findDailyStockOutByCustomerId(customerid, startDate, endDate, new StockOutStatus[]{StockOutStatus.?????????});
	}

	//??????????????????????????????
	public List<Map> findStockOutTotalByOutboundRegisterId(Long outboundRegisterId, final StockOutStatus stockOutStatus) {
    	Map<String, Object> params = null;
    	params = new HashMap<String, Object>();
        params.put("outboundRegisterId", outboundRegisterId);
        params.put("stockOutStatus", stockOutStatus.ordinal());
        return sqlSessionTemplate.selectList("findStockOutTotalByOutboundRegisterId", params);
    }
}
