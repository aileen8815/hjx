package com.wwyl.service.store;

import java.util.ArrayList;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.store.BookInventoryHisDao;
import com.wwyl.dao.store.StockAdjustDao;
import com.wwyl.dao.store.StockAdjustItemDao;
import com.wwyl.entity.settings.StockAdjustType;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.BookInventoryHis;
import com.wwyl.entity.store.StockAdjust;
import com.wwyl.entity.store.StockAdjustItem;
import com.wwyl.service.settings.StoreLocationService;

/**
 * @author fyunli
 */
@Service
@Transactional(readOnly = true)
public class StockAjdustService {

	@Resource
	private StockAdjustDao stockAjustDao;

	@Resource
	private StockAdjustItemDao stockAjustItemDao;
	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private BookInventoryHisDao bookInventoryHisDao;
	@Resource
	private StoreLocationService storeLocationService;
	public List<StockAdjust> findAll() {
		return stockAjustDao.findAll();
	}

	public StockAdjust findOneStockAdjust(Long id) {
		return stockAjustDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public Long saveStockAdjust(StockAdjust stockAdjust) {
		stockAjustDao.save(stockAdjust);
		return stockAdjust.getId();
	}

	@Transactional(readOnly = false)
	public void deleteStockAdjust(Long id) {
		stockAjustDao.delete(id);
	}

	public Page<StockAdjust> findStockAdjustByStockAdjustType(int page, int rows,final String stockAdjustType) {
		Specification<StockAdjust> specification = new Specification<StockAdjust>() {
			@Override
			public Predicate toPredicate(Root<StockAdjust> root,
					CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				Join<StockAdjust, StockAdjustType> stockAdjustTypeJoin = root
						.join(root.getModel().getSingularAttribute(
								"stockAdjustType", StockAdjustType.class),
								JoinType.INNER);
				if (stockAdjustType != null && stockAdjustType.trim() != "") {
					Predicate p1 = criteriaBuilder.equal(stockAdjustTypeJoin
							.get("id").as(Long.class), stockAdjustType);
					list.add(p1);
				}

				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));

			}
		};
		return stockAjustDao.findAll(specification,
				RepoUtils.buildPageRequest(page, rows));
	}

	@Transactional(readOnly = false)
	public void deleteStockAdjustItem(Long id) {
		stockAjustItemDao.delete(id);
	}

	public StockAdjustItem findOneStockAdjustItem(Long id) {
		return stockAjustItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveStockAdjustItem(StockAdjustItem stockAdjustItem,String storeLocation) {
	
		BookInventory bookInventory =bookInventoryService.findByLocation(storeLocation);
		if(bookInventory!=null){

			//保存调整单
			List<BookInventoryHis> bookInventoryHislist=bookInventoryHisDao.findBookInventoryHis( bookInventory.getStoreContainer().getId());
			BookInventoryHis inactivebookInventoryHis=null;
			if(!bookInventoryHislist.isEmpty()){
				inactivebookInventoryHis=bookInventoryHislist.get(0);
			}
			stockAdjustItem.setBookInventoryHis(inactivebookInventoryHis);
			stockAdjustItem.setStoreLocation(bookInventory.getStoreLocation());
			stockAdjustItem.setCustomer(bookInventory.getCustomer());
			stockAdjustItem.setStoreContainer(bookInventory.getStoreContainer());
			stockAdjustItem.setStockIn(bookInventory.getStockIn());
			stockAdjustItem.setStockInOperator(bookInventory.getStockInOperator());
			stockAdjustItem.setStockInTime(bookInventory.getStockInTime());
			stockAjustItemDao.save(stockAdjustItem); 
			//根据调整单更新库存
			if(stockAdjustItem.isEmpty()){
				//把储位置为空,删除库存信息
				bookInventoryService.delete(bookInventory.getId());
			}else{
		 
				bookInventory.setAmount(stockAdjustItem.getAmount());
				bookInventory.setAmountMeasureUnit(stockAdjustItem.getAmountMeasureUnit());
				bookInventory.setWeight(stockAdjustItem.getWeight());
				bookInventory.setWeightMeasureUnit(stockAdjustItem.getWeightMeasureUnit());
				bookInventory.setPacking(stockAdjustItem.getPacking());
				bookInventory.setProduct(stockAdjustItem.getProduct());
				bookInventory.setProductionDate(stockAdjustItem.getProductionDate());
				bookInventory.setProductStatus(stockAdjustItem.getProductStatus());
				bookInventory.setQualified(stockAdjustItem.isQualified());
				bookInventory.setQuanlityGuaranteePeriod(stockAdjustItem.getQuanlityGuaranteePeriod());
				bookInventory.setSpec(stockAdjustItem.getSpec());
				bookInventory.setStoreDuration(stockAdjustItem.getStoreDuration());
				bookInventoryService.update(bookInventory,null); 
				
			}

		}else{
			//新增库存信息
			//保存调整单
			if(!stockAdjustItem.isEmpty()){
			stockAdjustItem.setStoreLocation(storeLocationService.findByCode(storeLocation));
			stockAjustItemDao.save(stockAdjustItem); 
			//根据调整单保存库存
			bookInventory=new BookInventory();
			bookInventory.setAmount(stockAdjustItem.getAmount());
			bookInventory.setAmountMeasureUnit(stockAdjustItem.getAmountMeasureUnit());
			bookInventory.setWeight(stockAdjustItem.getWeight());
			bookInventory.setWeightMeasureUnit(stockAdjustItem.getWeightMeasureUnit());
			bookInventory.setPacking(stockAdjustItem.getPacking());
			bookInventory.setProduct(stockAdjustItem.getProduct());
			bookInventory.setProductionDate(stockAdjustItem.getProductionDate());
			bookInventory.setProductStatus(stockAdjustItem.getProductStatus());
			bookInventory.setQualified(stockAdjustItem.isQualified());
			bookInventory.setQuanlityGuaranteePeriod(stockAdjustItem.getQuanlityGuaranteePeriod());
			bookInventory.setSpec(stockAdjustItem.getSpec());
			bookInventory.setStoreDuration(stockAdjustItem.getStoreDuration());
			bookInventory.setCustomer(stockAdjustItem.getCustomer());
			bookInventory.setStoreLocation(storeLocationService.findByCode(storeLocation));
			bookInventory.setStoreContainer(stockAdjustItem.getStoreContainer());
			bookInventoryService.save(bookInventory,false); 
			
			}
		}
		
	}
/*	public Page<StockAdjust> findStockAdjustCheck(int page,int rows) {
		return stockAjustDao.findStockAdjustCheck(RepoUtils.buildPageRequest(page, rows));
	}

	
	public List<StockAdjustItem> findStockAdjustItems(Long stockAdjustId) {
		return stockAjustItemDao.findByStockAdjustId(stockAdjustId);
	}
*/

}
