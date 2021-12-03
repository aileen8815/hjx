package com.wwyl.service.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.store.OutboundCheckItemDao;
import com.wwyl.dao.store.OutboundRegisterDao;
import com.wwyl.dao.store.OutboundRegisterItemDao;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.store.OutboundCheckItem;
import com.wwyl.entity.store.OutboundRegisterItem;
import com.wwyl.service.settings.StoreContainerService;

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class OutboundCheckService {

	@Resource
	private OutboundCheckItemDao outboundCheckItemDao;
	@Resource
	private OutboundRegisterItemDao outboundRegisterItemDao;
	@Resource
	private StoreContainerService storeContainerService;
	@Resource
	private OutboundRegisterDao outboundRegisterDao;
	@Resource
    private SqlSessionTemplate sqlSessionTemplate;

	@Transactional(readOnly = false)
	public void deleteItem(Long id) {
		outboundCheckItemDao.delete(id);
	}

	public OutboundCheckItem findOneItem(Long id) {
		return outboundCheckItemDao.findOne(id);
	}

	public OutboundRegisterItem findByStoreContainerId(Long storeContainerId) {
		return outboundRegisterItemDao.findByStoreContainerId(storeContainerId).get(0);
	}

	public List<OutboundRegisterItem> findByOutboundRegisterItems(Long outboundRegisterId) {
		return outboundRegisterItemDao.findByOutboundRegisterItems(outboundRegisterId);
	}	

	public List<OutboundCheckItem> findOutboundChecItemByOutboudregisterAndTallyArea(Long outboundRegisterid, Long tallyareaid){
		return outboundCheckItemDao.findOutboundChecItemByOutboudregisterAndTallyArea(outboundRegisterid, tallyareaid);
	}

	@Transactional(readOnly = false)
	public void saveItem(OutboundCheckItem outboundCheckItem) {
		// 包装为空
		if (outboundCheckItem.getPacking() != null && outboundCheckItem.getPacking().getId() == null) {
			outboundCheckItem.setPacking(null);
		}
		outboundCheckItemDao.save(outboundCheckItem);
	}

	@Transactional(readOnly = false)
	public void updateItem(OutboundCheckItem outboundCheckItem) {
		// 包装为空
		if (outboundCheckItem.getPacking() != null  && outboundCheckItem.getPacking().getId() == null) {
			outboundCheckItem.setPacking(null);
		}
		outboundCheckItemDao.save(outboundCheckItem);

	}

	public List<OutboundCheckItem> findOutboundCheckItems(Long inboundRegisterId) {
		return outboundCheckItemDao.findOutboundCheckItems(inboundRegisterId);
	}

	/**
	 * 合并商品信息,按商品分类
	 * 
	 * @param bookInventorys
	 * @return
	 */
	public List<OutboundCheckItem> mergeProductinfo(
			Set<OutboundCheckItem> outboundCheckItems) {
		List<OutboundCheckItem> resultList = new ArrayList<OutboundCheckItem>();
		Map<String, OutboundCheckItem> jsonlist = new LinkedHashMap<String, OutboundCheckItem>();
		OutboundCheckItem outboundCheckItemNew=null;
		for (OutboundCheckItem outboundCheckItem : outboundCheckItems) {
			String key = outboundCheckItem.getProduct().getId().toString();
			if (!jsonlist.containsKey(key)) {
				outboundCheckItemNew=new OutboundCheckItem();
				outboundCheckItemNew.setAmount(outboundCheckItem.getAmount());
				outboundCheckItemNew.setAmountMeasureUnit(outboundCheckItem.getAmountMeasureUnit());
				outboundCheckItemNew.setOutboundRegister(outboundCheckItem.getOutboundRegister());
				outboundCheckItemNew.setPacking(outboundCheckItem.getPacking());
				outboundCheckItemNew.setProduct(outboundCheckItem.getProduct());
				outboundCheckItemNew.setSpec(outboundCheckItem.getSpec());
				outboundCheckItemNew.setStoreContainer(outboundCheckItem.getStoreContainer());
				outboundCheckItemNew.setWeight(outboundCheckItem.getWeight());
				outboundCheckItemNew.setWeightMeasureUnit(outboundCheckItem.getWeightMeasureUnit());
				jsonlist.put(key, outboundCheckItemNew);
			} else {
				OutboundCheckItem putOutboundCheckItem = jsonlist.get(key);
				putOutboundCheckItem.setWeight(putOutboundCheckItem.getWeight()
						+ outboundCheckItem.getWeight());
				putOutboundCheckItem.setAmount(putOutboundCheckItem.getAmount()
						+ outboundCheckItem.getAmount());
				jsonlist.put(key, putOutboundCheckItem);
			}
		}
		resultList.addAll(jsonlist.values());
		return resultList;
	}
	//
	// public Page<OutboundCheck> findOutboundCheckSpecification(final Long customerId, final String startTime, final String endTime, int page, int rows) {
	// Specification<OutboundCheck> specification = new Specification<OutboundCheck>() {
	// @Override
	// public Predicate toPredicate(Root<OutboundCheck> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
	// // Join<OutboundCheck,Product> proJoin = root.join(root.getModel().getSingularAttribute("product",Product.class) , JoinType.INNER);
	// Path checkTime = root.get("checkTime");
	// Path customer = root.get("customer").get("id");
	// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	// List<Predicate> list = new ArrayList<Predicate>();
	// if (customerId != null) {
	// Predicate p1 = criteriaBuilder.equal(customer, customerId);
	// list.add(p1);
	// }
	// if ((endTime != null && endTime.trim().length() > 0) && (startTime != null && startTime.trim().length() > 0)) {
	//
	// Predicate p2 = null;
	// try {
	// p2 = criteriaBuilder.between(checkTime, format.parse(startTime), format.parse(endTime));
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// list.add(p2);
	// } else if ((endTime != null && endTime.trim().length() > 0) && (startTime == null || startTime.trim().length() == 0)) {
	//
	// Predicate p3 = null;
	// try {
	// p3 = criteriaBuilder.between(checkTime, format.parse("1900-12-12"), format.parse(endTime));
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// list.add(p3);
	// } else if ((endTime == null || endTime.trim().length() == 0) && (startTime != null && startTime.trim().length() > 0)) {
	// Predicate p4 = null;
	// try {
	// p4 = criteriaBuilder.between(checkTime, format.parse(startTime), new Date());
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// list.add(p4);
	// }
	//
	// Predicate[] p = new Predicate[list.size()];
	// return criteriaBuilder.and(list.toArray(p));
	//
	// }
	// };
	// return outboundCheckDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	// }

	public OutboundCheckItem getLastOutboundCheckItemByContainerCode(String containerCode) {
		StoreContainer storeContainer = storeContainerService.findStoreContainerByLabel(containerCode);
		if (storeContainer == null) {
			return null;
		}
		Page<OutboundCheckItem> items = outboundCheckItemDao.getLastOutboundCheckItemByContainerCode(storeContainer.getId(), RepoUtils.buildPageRequest(1, 1));
		if (CollectionUtils.isNotEmpty(items.getContent())) {
			return items.getContent().get(0);
		}
		return null;
	}
	//缴费单
	public List<Map> findCheckItemTotalByOutboundRegisterId(Long outboundRegisterId) {
    	Map<String, Object> params = null;
    	params = new HashMap<String, Object>();
        params.put("outboundRegisterId", outboundRegisterId);
        return sqlSessionTemplate.selectList("findCheckItemTotalByOutboundRegisterId", params);
    }
	//出库单
	public List<Map> findCheckItemByOutboundRegisterId(Long outboundRegisterId) {
    	Map<String, Object> params = null;
    	params = new HashMap<String, Object>();
        params.put("outboundRegisterId", outboundRegisterId);
        return sqlSessionTemplate.selectList("findCheckItemByOutboundRegisterId", params);
    }
}
