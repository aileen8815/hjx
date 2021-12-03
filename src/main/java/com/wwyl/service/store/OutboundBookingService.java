package com.wwyl.service.store;

import java.math.BigDecimal;
import java.util.*;


import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Constants;
import com.wwyl.Enums.BookingStatus;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.BookingMethodDao;
import com.wwyl.dao.settings.CommentDao;
import com.wwyl.dao.store.BookInventoryDao;
import com.wwyl.dao.store.OutboundBookingDao;
import com.wwyl.dao.store.OutboundBookingItemDao;
import com.wwyl.entity.settings.BookingMethod;
import com.wwyl.entity.settings.Comment;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.InboundBookingItem;
import com.wwyl.entity.store.OutboundBooking;
import com.wwyl.entity.store.OutboundBookingItem;
import com.wwyl.service.settings.TaskService;

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class OutboundBookingService {
	@Resource
	private OutboundBookingDao outboundBookingDao;
	@Resource
	private OutboundBookingItemDao outboundBookingItemDao;
	@Resource
	private BookInventoryDao bookInventoryDao;
	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private CommentDao commentDao;
	@Resource
	private BookingMethodDao bookingMethodDao;
	@Resource
	private TaskService taskService;
	
	
	public List<OutboundBooking> findAll() {
		return outboundBookingDao.findAll();
	}

	public OutboundBooking findOne(Long id) {
		return outboundBookingDao.findOne(id);
	}

	public List<OutboundBooking> findOutBySerialNo(String serialNo) {
		return outboundBookingDao.findBySerialNo(serialNo);
	}

	@Transactional(readOnly = false)
	public Long save(OutboundBooking outboundBooking,String[] productCheck) {
		//车型为空时候
		if(outboundBooking!=null&&outboundBooking.getVehicleType().getId()==null){
			outboundBooking.setVehicleType(null);
		}
	
		
		OutboundBookingItem item=null;
		if(outboundBooking.getId()!=null){
			List<OutboundBookingItem> outboundBookingItems=outboundBookingItemDao.findByOutboundBookingItems(outboundBooking.getId());
			for (OutboundBookingItem outboundBookingItem : outboundBookingItems) {
				outboundBookingItemDao.delete(outboundBookingItem.getId());
			}
			
		}
		outboundBookingDao.save(outboundBooking);
		//productCheckstr   批次_商品ID_数量_预计托盘数
		for (String productCheckstr : productCheck) {
			String[] productamount= productCheckstr.split("_");//[批次,商品ID,数量,预计托盘数]
			List<BookInventory> bookInventoryList = bookInventoryService.findBookInventorySpecification(outboundBooking.getCustomer().getId(),null,null,productamount[1],null,null,null);
			Map<String,BookInventory> bookInventoryMap=bookInventoryService.mergeBookInventorysBybatchs(bookInventoryList);
			BookInventory bookInventory	=bookInventoryMap.get(productamount[0]+"_"+productamount[1]);
			double weight=weightBudget(bookInventory.getAmount(),bookInventory.getWeight(),productamount[2]);
			item = new OutboundBookingItem();
			item.setAmount(Double.valueOf(productamount[2]));
			item.setStoreContainerCount(Integer.valueOf(productamount[3]));
			item.setAmountMeasureUnit(bookInventory.getAmountMeasureUnit());
			item.setOutboundBooking(outboundBooking);
			item.setPacking(bookInventory.getPacking());
			item.setProduct(bookInventory.getProduct());
			item.setSpec(bookInventory.getSpec());
			item.setStoreContainer(bookInventory.getStoreContainer());
			item.setWeight(weight);
			item.setBatchs(productamount[0]);
			item.setWeightMeasureUnit(bookInventory.getWeightMeasureUnit());
			outboundBookingItemDao.save(item);
		}
	    if (outboundBooking.isNew()) {
	            BookingMethod bookingMethod = bookingMethodDao.findOne(outboundBooking.getBookingMethod().getId());
	            if (bookingMethod.getCode().equals(Constants.BOOKING_METHOD_WEB)) {
	                taskService.assignOutboundBookingNotice(outboundBooking);
	            }
	    }
		
		return  outboundBooking.getId();
	}

	 /**
	  * 预计出库的重量 
	  * @param countAmount 库存商品总重量
	  * @param countWeight 库存商品总重量
	  * @param amount	       本次出库数量
	  * @return
	  */
	 public  double weightBudget(double  countAmount , double countWeight,String amount) {
	  BigDecimal b1 = new BigDecimal(Double.toString(countAmount));//总数量
	  BigDecimal b2 = new BigDecimal(Double.toString(countWeight));//总重量
	  BigDecimal b3 = new BigDecimal(amount);
	  int DEFAULT_DIV_SCALE=6;
	  BigDecimal  b4=  b2.divide(b1, DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
	  return (b3.multiply(b4)).doubleValue();
	 }

	 //验证是否还需要生成新的预约单
	 public boolean validation(Long outboundBookingId,String[] productChecks){
			Set<OutboundBookingItem>	outboundBookingItems=outboundBookingDao.findOne(outboundBookingId).getOutboundBookingItems();
			
			boolean result=false;
			for (OutboundBookingItem outboundBookingItem : outboundBookingItems) {
				boolean exist=false;
				for (String  productCheck : productChecks) {
					String[] item=productCheck.split("_");//[批次,商品ID,数量,预计托盘数]
					if(outboundBookingItem.getBatchProduct().equals(item[0]+"_"+item[1])){
						double  difference =	outboundBookingItem.getAmount()-Double.valueOf(item[2]);
						if(difference>0){
							 return true;
						}
						exist=true;
					}
				}
				if(!exist){
					return true;
				}
			}
			return result;
	}
	 
	@Transactional(readOnly = false)
	public void delete(Long id) {
		outboundBookingDao.delete(id);
	}
	@Transactional(readOnly = false)
	public void update(OutboundBooking outboundBooking,Comment conmment) {
		if(conmment!=null){
			commentDao.save(conmment);
		}
		outboundBookingDao.save(outboundBooking);
	}

	public Page<OutboundBooking> findOutboundBookingByConditions(int page,
			int rows, final String serialNo,final BookingStatus bookingStatus,final Long customerId,final String vehicleNumbers) {
		Specification<OutboundBooking> specification = new Specification<OutboundBooking>() {
			@Override
			public Predicate toPredicate(Root<OutboundBooking> root,
					CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				Path serialNoPath=	root.get("serialNo");
				Path customer=	root.get("customer").get("id");
				Path bookingStatusPath=	root.get("bookingStatus");
				Path bookTime=	root.get("bookTime");
				Path vehicleNumbersPath=  root.get("vehicleNumbers");
				if (customerId != null) {
					Predicate p1 = criteriaBuilder.equal(customer, customerId);
					list.add(p1);
				}
				if (bookingStatus != null) {
					Predicate p1 = criteriaBuilder.equal(bookingStatusPath, bookingStatus);
					list.add(p1);
				}
				if (serialNo != null && serialNo.trim().length() > 0) {
					Predicate p2 = criteriaBuilder.like(serialNoPath, "%" + serialNo + "%");
					list.add(p2);
				}
				if (vehicleNumbers != null && vehicleNumbers.trim().length() > 0) {
					Predicate p5 = criteriaBuilder.like(vehicleNumbersPath, "%" + vehicleNumbers + "%");
					list.add(p5);
				}
				Predicate[] p = new Predicate[list.size()];
				criteriaQuery.orderBy((criteriaBuilder.desc(bookTime)));
				return criteriaBuilder.and(list.toArray(p));

			}
		};
		return outboundBookingDao.findAll(specification,
				RepoUtils.buildPageRequest(page, rows));
	}

	@Transactional(readOnly = false)
	public void deleteOutboundBookingItem(String id, Long outboundBookingId) {
		String[] ids = id.split("-");
		outboundBookingItemDao.deleteOutboundBookingItem(Long.valueOf(ids[0]),
				Long.valueOf(ids[1]), outboundBookingId);
	}

	public OutboundBookingItem findOneItem(Long id) {
		return outboundBookingItemDao.findOne(id);
	}

	/**
	 * 添加出库明细
	 * 
	 * @param ids
	 *            货品ID-托盘ID,货品ID-托盘ID......
	 * @param outboundBooking出库预约单
	 */
	@Transactional(readOnly = false)
	public void saveItem(String ids, OutboundBooking outboundBooking) {
		String[] strs = ids.trim().split(",");
		OutboundBookingItem item = null;

		Long customerId = outboundBooking.getCustomer().getId();
		for (String str : strs) {
			String[] proidAndstoreIds = str.trim().split("-");// proidAndstoreIds[0]商品id,proidAndstoreIds[1]托盘ID
			BookInventory bookInventory = bookInventoryDao.findByProductIdAndstoreContainerId(Long.valueOf(proidAndstoreIds[0]),
							Long.valueOf(proidAndstoreIds[1]), customerId);

			item = new OutboundBookingItem();
			item.setAmount(bookInventory.getAmount());
			item.setAmountMeasureUnit(bookInventory.getAmountMeasureUnit());
			item.setOutboundBooking(outboundBooking);
			item.setPacking(bookInventory.getPacking());
			item.setProduct(bookInventory.getProduct());
			item.setSpec(bookInventory.getSpec());
			item.setStoreContainer(bookInventory.getStoreContainer());
			item.setWeight(bookInventory.getWeight());
			item.setWeightMeasureUnit(bookInventory.getWeightMeasureUnit());
			outboundBookingItemDao.save(item);

		}

	}

	public List<OutboundBookingItem> findOutItemsByserialNo(String serialNo) {
		List<OutboundBookingItem> OutboundBookingItemList = outboundBookingItemDao
				.findByserialNo(serialNo);

		return OutboundBookingItemList;
	}

	public List<OutboundBookingItem> findByOutboundBookingItems(
			Long outboundBookingId) {
		return outboundBookingItemDao
				.findByOutboundBookingItems(outboundBookingId);
	}

	public Page<OutboundBooking> findBySerialNoLike(String serialNo, int page,
			int rows) {
		return outboundBookingDao.findBySerialNoLike(BookingStatus.未审核,
				serialNo, RepoUtils.buildPageRequest(page, rows));
	}

	@Transactional(readOnly = false)
	public void deleteItem(Long id) {
		outboundBookingItemDao.delete(id);
	}

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    public List<Map> findOutboundBookingStat(Long[] areaIds, Date startDate, Date endDate) {
        Map<String, Object> params =  new HashMap<String, Object>();
        if (ArrayUtils.isNotEmpty(areaIds)) {
            List<Long> allAreas = new ArrayList<Long>();
            for (Long areaId : areaIds) {
                allAreas.add(areaId);
            }
            params.put("areaIds", allAreas);
        }
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return sqlSessionTemplate.selectList("findOutboundBookingStat", params);
    }

}
