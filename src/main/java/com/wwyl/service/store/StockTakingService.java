package com.wwyl.service.store;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Constants;
import com.wwyl.Enums.StockTakeResultStatus;
import com.wwyl.Enums.StockTakingMode;
import com.wwyl.Enums.StockTakingStatus;
import com.wwyl.Enums.StockTakingType;
import com.wwyl.Enums.StoreLocationStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.StoreContainerDao;
import com.wwyl.dao.settings.StoreLocationDao;
import com.wwyl.dao.store.BookInventoryDao;
import com.wwyl.dao.store.StockTakingDao;
import com.wwyl.dao.store.StockTakingItemDao;
import com.wwyl.dao.store.StockTakingResultDao;
import com.wwyl.entity.settings.ProductStatus;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.StockTaking;
import com.wwyl.entity.store.StockTakingItem;
import com.wwyl.entity.store.StockTakingResult;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreLocationService;
 

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class StockTakingService {
	@Resource
	private BookInventoryDao bookInventoryDao;
	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private StoreLocationService storeLocationService;
	@Resource
	private StockTakingDao stockTakingDao;
	@Resource
	private StockTakingItemDao stockTakingItemDao;
	@Resource
	private StockTakingResultDao stockTakingResultDao;
	@Resource
	private StoreLocationDao storeLocationDao;
	@Resource
	private StoreContainerDao storeContainerDao;
	@Resource
	private StoreAreaAssigneeService storeAreaAssigneeService;
	@Resource
    private SqlSessionTemplate sqlSessionTemplate;
	
	public List<StockTaking> findAll() {
		return stockTakingDao.findAll();
	}

	public StockTaking findOne(Long id) {
		return stockTakingDao.findOne(id);
	}

	public Page<StockTaking> findStockTakingSpecification(int page, int rows, final String serialNo,final StockTakingStatus stockTakingStatus) {
		Specification<StockTaking> specification = new Specification<StockTaking>() {
			@Override
			public Predicate toPredicate(Root<StockTaking> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				Path serialNoPath = root.get("serialNo");
				 
				Path startTimePath = root.get("startTime");
				Path stockTakingStatusPath = root.get("stockTakingStatus");
				 
	 
				if (serialNo != null) {
					Predicate p1 = criteriaBuilder.like(serialNoPath, "%" + serialNo + "%");
					list.add(p1);
				}
				if (stockTakingStatus != null) {
					Predicate p1 = criteriaBuilder.equal(stockTakingStatusPath, stockTakingStatus);
					list.add(p1);
				}
	 
				Predicate[] p = new Predicate[list.size()];
				criteriaQuery.orderBy((criteriaBuilder.desc(startTimePath)));
				return criteriaBuilder.and(list.toArray(p));
			
			}
		};
		return stockTakingDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}
	
	@Transactional(readOnly = false)
	public Long save(Long storeAreaId,StockTaking stockTaking) {
		stockTaking.setSerialNo(serialNumberService.getSerialNumber(TransactionType.StockTaking));
		stockTaking.setRegisterTime(new Date());
		stockTaking.setRegisterOperator(ThreadLocalHolder.getCurrentOperator());
		stockTaking.setStockTakingStatus(StockTakingStatus.?????????);
		stockTaking.setStockTakingMode(StockTakingMode.????????????);
		stockTaking.setStockTakingType(StockTakingType.??????);
		stockTaking.setStockTakingObjectId(storeAreaId);
		stockTakingDao.save(stockTaking);
		List<Long> storeLocationIds=new ArrayList<Long>();
		List<StoreLocation> storeLocations = storeLocationDao.findByArea(storeAreaId);
		
		for (StoreLocation storeLocation : storeLocations) {
			storeLocationIds.add(storeLocation.getId());
		}
		Long[] ids=new Long[]{};
		List<BookInventory> bookInventorys = bookInventoryService.findByStoreLocationids(storeLocationIds.toArray(ids));
		StockTakingResult stockTakingResult=null;
		//??????????????????
		for (BookInventory bookInventory : bookInventorys) {
			stockTakingResult=new StockTakingResult();
			stockTakingResult.setAmount(bookInventory.getAmount());
			stockTakingResult.setAmountMeasureUnit(bookInventory.getAmountMeasureUnit());
			stockTakingResult.setCustomer(bookInventory.getCustomer());
			stockTakingResult.setProduct(bookInventory.getProduct());
			stockTakingResult.setStockTaking(stockTaking);
			stockTakingResult.setStoreContainer(bookInventory.getStoreContainer());
			stockTakingResult.setStoreLocation(bookInventory.getStoreLocation());
			stockTakingResult.setWeight(bookInventory.getWeight());
			stockTakingResult.setWeightMeasureUnit(bookInventory.getWeightMeasureUnit());
			stockTakingResult.setStockTaking(stockTaking);
			stockTakingResult.setStockTakingAmount(bookInventory.getAmount());
			stockTakingResult.setStockTakingWeight(bookInventory.getWeight());
			stockTakingResult.setStockTakeResultStatus(StockTakeResultStatus.??????);
			stockTakingResultDao.save(stockTakingResult);
		}
		return stockTaking.getId();
	}
	@Transactional(readOnly = false)
	public Long  update(Long storeAreaId,StockTaking stockTaking) {
		StockTaking  oldStockTaking=stockTakingDao.findOne(stockTaking.getId());
		oldStockTaking.setStockTakingOperator(stockTaking.getStockTakingOperator());
		oldStockTaking.setStartTime(stockTaking.getStartTime());
		
		stockTakingItemDao.deleteByStockTaking(stockTaking.getId());
		stockTakingDao.save(stockTaking);
		saveStockTakingItem(storeAreaId,stockTaking);
		return stockTaking.getId();
	}
	@Transactional(readOnly = false)
	public void  saveStockTakingItem(Long  storeAreaId,StockTaking stockTaking) {
		List<StoreArea> storeAreaList = storeAreaAssigneeService.findStoreAreas(storeAreaId);
		List<Long> storeAreaIds = new ArrayList<Long>();
		for (StoreArea storeArea : storeAreaList) {
			storeAreaIds.add(storeArea.getId());
		}
		Long[] ids=new Long[]{};
		List<StoreLocation>  storeLocations=storeLocationDao.findByAreas(storeAreaIds.toArray(ids));
		StockTakingItem stockTakingItem=null;
		ArrayList<StockTakingItem>  stockTakingItemList=new ArrayList<StockTakingItem>();
		ArrayList<Long>  storeLocationids=new ArrayList<Long>();
		for (StoreLocation storeLocation : storeLocations) {
				stockTakingItem=new StockTakingItem();
			if(storeLocation.getStoreLocationStatus()!=StoreLocationStatus.?????????&&storeLocation.getStoreLocationStatus()!=StoreLocationStatus.?????????){
				stockTakingItem.setStoreLocation(storeLocation);
				stockTakingItem.setStockTaking(stockTaking);
				stockTakingItemList.add(stockTakingItem);
			}else if(storeLocation.getStoreLocationStatus()==StoreLocationStatus.?????????){
				storeLocationids.add(storeLocation.getId());
			}
			
		}
		if(!storeLocationids.isEmpty()){
			stockTakingItemList.addAll(this.getBookInventoryStockTakingItems(storeLocationids,stockTaking));
		}
		stockTakingItemDao.save(stockTakingItemList);
 
	}
	@Transactional(readOnly = false)
	public  List<StockTakingItem> getBookInventoryStockTakingItems(List<Long> storeLocationIds,StockTaking stockTaking){
		List<BookInventory> bookInventorys = bookInventoryService.findByStoreLocationids(storeLocationIds.toArray());
		StockTakingItem stockTakingItem=null;
		ArrayList<StockTakingItem>  stockTakingItemList=new ArrayList<StockTakingItem>();
		for (BookInventory bookInventory : bookInventorys) {
			stockTakingItem=new StockTakingItem();
			stockTakingItem.setAmount(bookInventory.getAmount());
			stockTakingItem.setAmountMeasureUnit(bookInventory.getAmountMeasureUnit());
			stockTakingItem.setCustomer(bookInventory.getCustomer());
			stockTakingItem.setProduct(bookInventory.getProduct());
			stockTakingItem.setStockTaking(stockTaking);
			stockTakingItem.setStoreContainer(bookInventory.getStoreContainer());
			stockTakingItem.setStoreLocation(bookInventory.getStoreLocation());
			stockTakingItem.setWeight(bookInventory.getWeight());
			stockTakingItem.setWeightMeasureUnit(bookInventory.getWeightMeasureUnit());
			stockTakingItemList.add(stockTakingItem);
		}
		return stockTakingItemList;
	}
	@Transactional(readOnly = false)
	public void saveStockTakingResult(StockTakingResult stockTakingResult) {
		StoreContainer storeContainer=storeContainerDao.findBylabel(stockTakingResult.getStoreContainer().getLabel());
		if(storeContainer==null){
			throw new RuntimeException("??????????????????????????????");
		}
		stockTakingResult.setStoreContainer(storeContainer);
		if(stockTakingResult.getId()==null){
			List<StockTakingResult> stockTakingResultList= stockTakingResultDao.findUnfinishedStockTakingDifferentRsult(new StockTakingStatus[]{StockTakingStatus.?????????,StockTakingStatus.?????????,StockTakingStatus.?????????},storeContainer.getId());
			if(!stockTakingResultList.isEmpty()){
				throw new RuntimeException("??????????????????????????????????????????????????????????????????");
			}
		}
		
		//BookInventory storeContainerValidate =bookInventoryService.findByContainerLabel(storeContainer.getLabel());
		//BookInventory locationValidate =bookInventoryService.findByLocation(stockTakingResult.getStoreLocation().getCode());
/*		if((locationValidate!=null&&storeContainerValidate!=null&&!locationValidate.getStoreContainer().getLabel().equals(storeContainer.getLabel()))
			||(locationValidate==null&&storeContainerValidate!=null)){
						throw new RuntimeException("????????????????????????");
		}
		 */
/*		if(locationValidate==null&&storeContainerValidate!=null){
							throw new RuntimeException("????????????????????????");
		}*/
					
		  
		if(stockTakingResult.getStockTakingAmount()==0||stockTakingResult.getStockTakingWeight()==0){
				 
				stockTakingResult.setStockTakingAmount(0);
				stockTakingResult.setStockTakingWeight(0);
				stockTakingResult.setToStoreLocation(null);
		}
		if (stockTakingResult.getToStoreLocation()!=null&&StringUtils.isNotBlank(stockTakingResult.getToStoreLocation().getCode())) {
			StoreLocation tostoreLocation = storeLocationDao
					.findByCodeAndStatus(stockTakingResult.getToStoreLocation()
							.getCode(), new StoreLocationStatus[] {
							StoreLocationStatus.?????????, StoreLocationStatus.??????,
							StoreLocationStatus.?????? });
			if (tostoreLocation == null) {
				throw new RuntimeException("??????????????????????????????????????????????????????????????????");
			} else {
				stockTakingResult.setToStoreLocation(tostoreLocation);
			}
		}else{
			stockTakingResult.setToStoreLocation(null);
		}
		if(stockTakingResult.getStockTakeResultStatus() == null){
			stockTakingResult.setStockTakeResultStatus(StockTakeResultStatus.??????);
		}else if(!stockTakingResult.getStockTakeResultStatus().equals(StockTakeResultStatus.????????????)){
			stockTakingResult.setStockTakeResultStatus(StockTakeResultStatus.??????);
		}
		stockTakingResultDao.save(stockTakingResult);
		StockTaking stockTaking=this.findOne(stockTakingResult.getStockTaking().getId());
		if(stockTaking.getStockTakingStatus().equals(StockTakingStatus.?????????)){
			updateStatus(stockTakingResult.getStockTaking().getId());
		}
	}
	@Transactional(readOnly = false)
	public void relocation(StockTakingItem stockTakingItem,String  tostockLacation){
		StoreLocation tostoreLocation = storeLocationDao.findByCodeAndStatus(tostockLacation,
				new StoreLocationStatus[]{StoreLocationStatus.?????????,StoreLocationStatus.??????,StoreLocationStatus.??????});
			if(tostoreLocation==null){
				throw new RuntimeException("??????????????????????????????????????????????????????????????????");
			}
			StockTakingResult stockTakingResult =stockTakingResultDao.findStockTakingDifferentRsult(stockTakingItem.getStockTaking().getId(), stockTakingItem.getStoreLocation().getId());
			if(stockTakingResult==null){
				stockTakingResult= new StockTakingResult();
			}
			stockTakingResult.asStockTakingItem(stockTakingItem);
			stockTakingResult.setToStoreLocation(tostoreLocation);
			updateStatus(stockTakingItem.getStockTaking().getId());
			stockTakingResultDao.save(stockTakingResult);
	}
	@Transactional(readOnly = false)
	public void setEmpty(StockTakingItem stockTakingItem){
			  
			StockTakingResult stockTakingResult =stockTakingResultDao.findStockTakingDifferentRsult(stockTakingItem.getStockTaking().getId(), stockTakingItem.getStoreLocation().getId());
			if(stockTakingResult==null){
				stockTakingResult= new StockTakingResult();
			}
			stockTakingResult.asStockTakingItem(stockTakingItem);
			stockTakingResult.setStockTakingAmount(0);
			stockTakingResult.setToStoreLocation(null);
			stockTakingResult.setStockTakingWeight(0);
			updateStatus(stockTakingItem.getStockTaking().getId());
			stockTakingResultDao.save(stockTakingResult);
	}
	@Transactional(readOnly = false) 
	public void updateStatus(Long id){
		stockTakingDao.updateStatus(StockTakingStatus.?????????,id);
	}
	@Transactional(readOnly = false)
	public void remove(Long id) {
		stockTakingDao.updateStatus(StockTakingStatus.?????????,id);
	}
	public StockTakingItem findOneItem(Long id) {
		return stockTakingItemDao.findOne(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteRsult(Long id) {
		stockTakingResultDao.delete(id);
	}
	public StockTakingResult findOneResult(Long id) {
		return stockTakingResultDao.findOne(id);
	}
	public StockTakingResult findOneDifferentRsult(Long id) {
		return stockTakingResultDao.findOneDifferentRsult(id);
	}
	public StockTakingResult findStockTakingDifferentRsult(Long stockTakingId,Long locationId) {
		return stockTakingResultDao.findStockTakingDifferentRsult(stockTakingId,locationId);
	}
	public StockTakingResult findStockTakingRsult(Long stockTakingId,Long locationId) {
		return stockTakingResultDao.findStockTakingRsult(stockTakingId,locationId);
	}
	//??????
	@Transactional(readOnly = false)
	public void complete(Long id){
		StockTaking  stockTaking=stockTakingDao.findOne(id);
		stockTaking.setStockTakingStatus(StockTakingStatus.?????????);
		stockTaking.setEndTime(new Date());
		stockTakingDao.save(stockTaking);
		Set<StockTakingResult> stockTakingResults=stockTaking.getStockTakingResults();
		for (StockTakingResult stockTakingResult : stockTakingResults) {
			BookInventory bookInventory = bookInventoryService.findByContainer(stockTakingResult.getStoreContainer().getId());
			if(bookInventory==null&&stockTakingResult.getStockTakingAmount()!=0){
				//??????
				BookInventory bookInventoryNew=new  BookInventory();
				asBookInventory(bookInventoryNew,stockTakingResult);
				ProductStatus productStatus=new ProductStatus();
				productStatus.setId(Constants.PRODUCT_STATUS_QUALIFIED);
				bookInventoryNew.setProductStatus(productStatus);
				bookInventoryService.save(bookInventoryNew,false);
			}else if(bookInventory!=null&&stockTakingResult.getStockTakeResultStatus().equals(StockTakeResultStatus.??????)){
				 
				
				if(stockTakingResult.getStockTakingAmount()==0){
					//???????????????
					bookInventoryService.delete(bookInventory.getId());
				}else{
					
					//??????????????????
					asBookInventory(bookInventory,stockTakingResult);
					String storeLocationOld=null;
					if(stockTakingResult.getToStoreLocation()!=null){
						//???????????????
						bookInventory.setStoreLocation(stockTakingResult.getToStoreLocation());
						storeLocationOld=stockTakingResult.getStoreLocation().getCode();
					}
					bookInventoryService.update(bookInventory,storeLocationOld);
				}
			}
		}
		
		 
	}
	public BookInventory asBookInventory(BookInventory bookInventory ,StockTakingResult stockTakingResult){
			bookInventory.setAmount(stockTakingResult.getStockTakingAmount());
			bookInventory.setAmountMeasureUnit(stockTakingResult.getAmountMeasureUnit());
			bookInventory.setWeight(stockTakingResult.getStockTakingWeight());
			bookInventory.setWeightMeasureUnit(stockTakingResult.getWeightMeasureUnit());
			bookInventory.setCustomer(stockTakingResult.getCustomer());
			bookInventory.setProduct(stockTakingResult.getProduct());
			bookInventory.setStoreContainer(stockTakingResult.getStoreContainer());
			bookInventory.setStoreLocation(stockTakingResult.getStoreLocation());
			return bookInventory;
	}
	/**
	 * ??????
	 * @param id ??????????????????ID
	 * @param stockTaking 
	 * @return
	 */
	@Transactional(readOnly = false)
	public Long reTaking(Long id,Date reTakingTime){
		StockTaking stockTakingOld=	stockTakingDao.findOne(id);
		Set<StockTakingResult>   stockTakingResults =stockTakingOld.getStockTakingResults();
		StockTaking stockTaking=new StockTaking();
		stockTaking.setSerialNo(serialNumberService.getSerialNumber(TransactionType.StockTaking));
		stockTaking.setRegisterTime(new Date());
		stockTaking.setRegisterOperator(ThreadLocalHolder.getCurrentOperator());
		stockTaking.setStockTakingStatus(StockTakingStatus.?????????);
		stockTaking.setStockTakingMode(StockTakingMode.????????????);
		stockTaking.setStockTakingType(StockTakingType.??????);
		stockTaking.setStockTakingOld(stockTakingOld);
		stockTaking.setStockTakingObjectId(stockTakingOld.getStockTakingObjectId());
		stockTaking.setStockTakingOperator(stockTakingOld.getStockTakingOperator());
		stockTaking.setStartTime(reTakingTime);
		stockTakingDao.save(stockTaking);
		stockTakingOld.setStockTakingStatus(StockTakingStatus.?????????);
		stockTakingDao.save(stockTakingOld);
		/*StockTakingItem stockTakingItem=null;
		for (StockTakingResult stockTakingResult : stockTakingResults) {
				stockTakingItem=new StockTakingItem();
				stockTakingItem.setAmount(stockTakingResult.getStockTakingAmount());
				stockTakingItem.setAmountMeasureUnit(stockTakingResult.getAmountMeasureUnit());
				stockTakingItem.setCustomer(stockTakingResult.getCustomer());
				stockTakingItem.setProduct(stockTakingResult.getProduct());
				stockTakingItem.setStockTaking(stockTaking);
				stockTakingItem.setStoreContainer(stockTakingResult.getStoreContainer());
				if(stockTakingResult.getToStoreLocation()!=null){
					stockTakingItem.setStoreLocation(stockTakingResult.getToStoreLocation());
				}else{
					stockTakingItem.setStoreLocation(stockTakingResult.getStoreLocation());
				}
				
				stockTakingItem.setWeight(stockTakingResult.getStockTakingWeight());
				stockTakingItem.setWeightMeasureUnit(stockTakingResult.getWeightMeasureUnit());
		 
				stockTakingItemDao.save(stockTakingItem);
			
		}*/
		return stockTaking.getId();
	}
	/**
	 * ??????
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public void  checked(Long id,boolean result) {
		StockTaking stockTaking = stockTakingDao.findOne(id);
		stockTaking.setApprover(ThreadLocalHolder.getCurrentOperator());
		stockTaking.setApproveTime(new Date());
		stockTaking.setStockTakingStatus(result?StockTakingStatus.?????????:StockTakingStatus.?????????);
		stockTakingDao.save(stockTaking);
	}
	
	/**
	 * ????????????
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public void  commitChecked(Long id) {
		stockTakingDao.updateStatus(StockTakingStatus.?????????,id);
	}
	/**
	 * ???????????????
	 * @param stockTakingId
	 * @return
	 */
	public List<Map> findTotalByStockTakingId(Long stockTakingId) {
    	Map<String, Object> params = null;
    	params = new HashMap<String, Object>();
        params.put("stockTakingId", stockTakingId);
        return sqlSessionTemplate.selectList("findTotalByStockTakingId", params);
    }
}
