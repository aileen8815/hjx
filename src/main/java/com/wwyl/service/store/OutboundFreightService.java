package com.wwyl.service.store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.store.OutboundCheckItemDao;
import com.wwyl.dao.store.OutboundFreightDao;
import com.wwyl.dao.store.OutboundFreightItemDao;
import com.wwyl.dao.store.OutboundRegisterDao;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.OutboundCheckItem;
import com.wwyl.entity.store.OutboundFreight;
import com.wwyl.entity.store.OutboundFreightItem;

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class OutboundFreightService {

	@Resource
	private OutboundFreightDao outboundFreightDao;
	@Resource
	private OutboundFreightItemDao outboundFreightItemDao;
	@Resource
	private OutboundRegisterDao outboundRegisterDao;
	@Resource
	private OutboundCheckItemDao outboundCheckItemDao;
	
	public List<OutboundFreight> findAll() {
		return outboundFreightDao.findAll();
	}

	public OutboundFreight findOne(Long id) {
		return outboundFreightDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public Long save(OutboundFreight  outboundFreight,String[] productCheck) {
		if (outboundFreight.getCarrier() !=null && outboundFreight.getCarrier().getId() ==null){
			outboundFreight.setCarrier(null);
		}
		if (outboundFreight.getTallyArea() !=null && outboundFreight.getTallyArea().getId() ==null){
			outboundFreight.setTallyArea(null);
		}
		if (outboundFreight.getVehicleType() !=null && outboundFreight.getVehicleType().getId() ==null){
			outboundFreight.setVehicleType(null);
		}
		outboundFreightDao.save(outboundFreight);
		OutboundFreightItem item = null;
		if (outboundFreight.getId() != null) {
			List<OutboundFreightItem> outboundFreightItems =	findOutboundFreightItems(outboundFreight.getId());
			for (OutboundFreightItem outboundFreightItem : outboundFreightItems) {
				outboundFreightItemDao.delete(outboundFreightItem.getId());
			}

		}
		for (String productCheckstr : productCheck) {
			String[] productamount = productCheckstr.split(":");
			List<OutboundCheckItem> outboundCheckItemList = outboundCheckItemDao.findByOutboundRegisterAndProduct(outboundFreight.getOutboundRegister().getId(), Long.valueOf(productamount[0]));
			OutboundCheckItem outboundCheckItem = outboundCheckItemList.get(0);
			double weight = weightBudget(outboundCheckItem.getAmount(), outboundCheckItem.getWeight(), productamount[1]);
			item = new OutboundFreightItem();
			item.setAmount(Double.valueOf(productamount[1]));
			item.setAmountMeasureUnit(outboundCheckItem.getAmountMeasureUnit());
			item.setOutboundFreight(outboundFreight);
			item.setPacking(outboundCheckItem.getPacking());
			item.setProduct(outboundCheckItem.getProduct());
			item.setSpec(outboundCheckItem.getSpec());
			item.setWeight(weight);
			item.setWeightMeasureUnit(outboundCheckItem.getWeightMeasureUnit());
			outboundFreightItemDao.save(item);
		}
		return outboundFreight.getId();
	}
	
	/**
	 * 预计出库的重量
	 * 
	 * @param countAmount
	 *            库存商品总重量
	 * @param countWeight
	 *            库存商品总重量
	 * @param amount
	 *            本次出库数量
	 * @return
	 */
	public double weightBudget(double countAmount, double countWeight, String amount) {
		BigDecimal b1 = new BigDecimal(Double.toString(countAmount));// 总数量
		BigDecimal b2 = new BigDecimal(Double.toString(countWeight));// 总重量
		BigDecimal b3 = new BigDecimal(amount);
		int DEFAULT_DIV_SCALE = 6;
		BigDecimal b4 = b2.divide(b1, DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
		return (b3.multiply(b4)).doubleValue();
	}
	@Transactional(readOnly = false)
	public void delete(Long id) {
		outboundFreightDao.delete(id);
	}
	public List<OutboundFreight> findByoutboundSpecification(final String  serialNo,final Long customerId) {
			Specification<OutboundFreight> specification = new Specification<OutboundFreight>() {
				@Override
				public Predicate toPredicate(Root<OutboundFreight> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
					Path outboundRegister=	root.get("outboundRegister").get("serialNo");
					Path customer=	root.get("customer").get("id");
					Path freightTime=	root.get("freightTime");
					List<Predicate> list = new ArrayList<Predicate>();
					if (customerId != null) {
						Predicate p1 = criteriaBuilder.equal(customer, customerId);
						list.add(p1);
					}
					if (serialNo != null && serialNo.trim().length() > 0) {
						Predicate p2 = criteriaBuilder.like(outboundRegister, "%" + serialNo + "%");
						list.add(p2);
					}
					Predicate[] p = new Predicate[list.size()];
					criteriaQuery.orderBy((criteriaBuilder.desc(freightTime)));
					return criteriaBuilder.and(list.toArray(p));
				}
			};
			return  outboundFreightDao.findAll(specification);
	 
	}
	@Transactional(readOnly = false)
	public void deleteItem(Long id) {
		outboundFreightItemDao.delete(id);
	}
	public OutboundFreightItem findOneItem(Long id) {
		return outboundFreightItemDao.findOne(id);
	}
	@Transactional(readOnly = false)
	public void saveItem(OutboundFreightItem outboundFreightItem,Long outboundFreightId) {
			OutboundFreight outboundFreight=new OutboundFreight();
			outboundFreight.setId(outboundFreightId);
			outboundFreightItem.setOutboundFreight(outboundFreight);
			//包装为空时候
			if(outboundFreightItem.getPacking().getId()==null){
				outboundFreightItem.setPacking(null);
			}
			outboundFreightItemDao.save(outboundFreightItem);
	}
	public  List<OutboundFreightItem> findOutboundFreightItems(Long  outboundFreightId) {
		return outboundFreightItemDao.findOutboundFreightItemList(outboundFreightId);
	}
	
}
