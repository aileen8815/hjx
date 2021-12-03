package com.wwyl.service.store;


 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Enums.StoreLocationStatus;
import com.wwyl.Enums.SystemConfigKey;
import com.wwyl.dao.DynamicSpecifications;
import com.wwyl.dao.PropertyFilter;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.StoreLocationDao;
import com.wwyl.dao.settings.SystemConfigDao;
import com.wwyl.dao.store.BookInventoryDao;
import com.wwyl.dao.store.BookInventoryHisDao;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.settings.SystemConfig;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.BookInventoryHis;
import com.wwyl.service.settings.StoreContainerService;
import com.wwyl.service.settings.TaskService;

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class BookInventoryService {

	@Resource
	private BookInventoryDao bookInventoryDao;
	@Resource
	private BookInventoryHisDao bookInventoryHisDao;
	@Resource
	StoreAreaAssigneeService storeAreaAssigneeService;
	@Resource
	private StoreLocationDao storeLocationDao;
	@Resource
	private TaskService	taskService;

	@Resource
	private StoreContainerService storeContainerService;
 
	@Resource
	private InboundReceiptService inboundReceiptService;
	
	@Resource
	private SystemConfigDao systemConfigDao;
	
	public List<BookInventory> findAll() {
		return bookInventoryDao.findAll();
	}

	public BookInventory findOne(Long id) {
		return bookInventoryDao.findOne(id);
	}
	
	public  List<BookInventory>  findByStoreLocationids(Object[]   storeLocationids){
		return bookInventoryDao.findByStoreLocationids(storeLocationids);
	}
	//保存库存更新历史:清点时间计算入库时间；由于上架时，操作员存在没有上架的时候；故所有上传上的入库单数据都以清点时间为准；
	//由于其他操作也会用到该方法，故使用参数inboundType区分；
	@Transactional(readOnly = false)
	public void save(BookInventory bookInventory, Boolean inboundType) {
		if (bookInventory.getPacking()!=null&&bookInventory.getPacking().getId() == null) {
			bookInventory.setPacking(null);
		}
		if (bookInventory.getStockIn()!=null&&bookInventory.getStockIn().getId() == null) {
			bookInventory.setStockIn(null);
		}
		
		//如果是仓储笼，则可使用；
		//if (StringUtils.equals("2", bookInventory.getStoreLocation().getStoreLocationType().getCode())){
		//	storeLocationDao.updateStoreLocationStatus(bookInventory.getStoreLocation().getId(), StoreLocationStatus.可使用);
		//}else
		//{
			// 更新使用的储位为使用中
			storeLocationDao.updateStoreLocationStatus(bookInventory.getStoreLocation().getId(), StoreLocationStatus.使用中);
		//}
		
		bookInventoryDao.save(bookInventory);
		//保存库存历史
		savebookInventoryHis(bookInventory,inboundType);
	}

	@Transactional(readOnly = false)
	public void update(BookInventory bookInventory,String storeLocationOld) {
		if (bookInventory.getPacking()!=null&&bookInventory.getPacking().getId() == null) {
			bookInventory.setPacking(null);
		}
		if (bookInventory.getStockIn()!=null&&bookInventory.getStockIn().getId() == null) {
			bookInventory.setStockIn(null);
		}
		if(StringUtils.isNotBlank(storeLocationOld)&&!StringUtils.equals(bookInventory.getStoreLocationCode(),storeLocationOld)){
			// 更新使用的储位为使用中
			StoreLocation storeLocationOldObj = storeLocationDao.findByCode(storeLocationOld);
			if(storeLocationOldObj!=null){
				// 更新储位使用状态
				storeLocationDao.updateStoreLocationStatus(storeLocationOldObj.getId(), StoreLocationStatus.可使用);
			}
		}
		storeLocationDao.updateStoreLocationStatus(bookInventory.getStoreLocation().getId(), StoreLocationStatus.使用中);
		bookInventoryDao.save(bookInventory);
	
		//把上个历史设置失效时间
		updatebookInventoryHis(bookInventory.getStoreContainer().getId());
		//保存库存更新历史
		savebookInventoryHis(bookInventory,false);
	}

    //把上个历史设置失效时间
    public void updatebookInventoryHis(Long storeContainerid) {
    	List<BookInventoryHis> bookInventoryHislist=bookInventoryHisDao.findBookInventoryHis(storeContainerid);
		if(bookInventoryHislist!=null&&!bookInventoryHislist.isEmpty()){
			BookInventoryHis inactivebookInventoryHis=bookInventoryHislist.get(0);
			inactivebookInventoryHis.setInactiveTime(new Date());
			bookInventoryHisDao.save(inactivebookInventoryHis);
		}
    }

	//保存库存更新历史
	//保存库存更新历史:清点时间计算入库时间；由于上架时，操作员存在没有上架的时候；故所有上传上的入库单数据都以清点时间为准；
	public void savebookInventoryHis(BookInventory bookInventory, Boolean inboundType){
		BookInventoryHis bookInventoryhis=new BookInventoryHis();
		//清点时间计算入库时间；
		if (inboundType) {
			bookInventoryhis.setActiveTime(bookInventory.getStockInTime());
		}else
		{
			bookInventoryhis.setActiveTime(new Date());
		}
		
		bookInventoryhis.createFromBookInventory(bookInventory);
		bookInventoryHisDao.save(bookInventoryhis);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		BookInventory bookInventory=bookInventoryDao.findOne(id);
		storeLocationDao.updateStoreLocationStatus(bookInventory.getStoreLocation().getId(), StoreLocationStatus.可使用);
		updatebookInventoryHis(bookInventory.getStoreContainer().getId());
		bookInventoryDao.delete(id);
	}

	public Page<BookInventory> findBookInventorys(int page, int rows, String customerId, String productId, String storeAreaId, String storeContainerLabel) {

		return findBookInventorySpecification(customerId, productId, storeAreaId, storeContainerLabel, page, rows);
	}


	public BookInventory findBookInventoryByCustomerId(Long customerId, Long storeContainer) {
		BookInventory bookInventory = bookInventoryDao.findByCustomerIdAndStoreContainer(customerId, storeContainer);
		return bookInventory;
	}
	/**
	 * 查找指定客户和客户商品，商品名称和入库单号排序
	 * @param customerId
	 * @param products
	 * @return
	 */
	public List<BookInventory> findByCustomerIdAndproductId( Object[] storeContainers,Object[] products,Long customerId) {
		if(storeContainers.length==0){
			return bookInventoryDao.findByCustomerIdAndproductId(customerId, products);
		}else{
			return  bookInventoryDao.findByCustomerIdAndproductId(storeContainers, products,customerId);
		}
	}
	public List<BookInventory> findByCustomerIdAndproductId(Long customerId, Object[] products) {
		List<BookInventory> bookInventorylist = bookInventoryDao.findByCustomerIdAndproductId(customerId, products);
		return bookInventorylist;
	}
	public List<BookInventory> findBookInventoryByCustomerId(Long customerId) {
		List<BookInventory> bookInventorylist = bookInventoryDao.findByCustomerId(customerId);
		return bookInventorylist;
	}

	public Page<BookInventory> findBookInventorySpecification(final String customerId, final String productId, final String storeAreaId, final String storeContainerLabel, int page, int rows) {
		Specification<BookInventory> specification = new Specification<BookInventory>() {
			@Override
			public Predicate toPredicate(Root<BookInventory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				Path storeArea = root.get("storeLocation").get("storeArea").get("id");
				Path customer = root.get("customer").get("id");
				Path product = root.get("product").get("id");
				Path storeContainer = root.get("storeContainer").get("label");
				
				
				List<Predicate> list = new ArrayList<Predicate>();
				if (customerId != null && customerId.trim().length() > 0) {
					Predicate p1 = criteriaBuilder.equal(customer, customerId);
					list.add(p1);
				}
				if (productId != null && productId.trim().length() > 0) {
					Predicate p2 = criteriaBuilder.equal(product, productId);
					list.add(p2);
				}
				if (storeAreaId != null && storeAreaId.trim().length() > 0) {
					List<StoreArea> storeAreaList = storeAreaAssigneeService.findStoreAreas(Long.valueOf(storeAreaId));
					List<Long> storeAreaIds = new ArrayList<Long>();
					for (StoreArea storeArea2 : storeAreaList) {
						storeAreaIds.add(storeArea2.getId());
					}
					Predicate p3 = storeArea.in(storeAreaIds.toArray());
					list.add(p3);
				}
				if (storeContainerLabel != null && storeContainerLabel.trim().length() > 0) {
					Predicate p4 = criteriaBuilder.equal(storeContainer, storeContainerLabel);
					list.add(p4);
				}

				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));

			}
		};
		return bookInventoryDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}

    public List<BookInventory> findBookInventorySpecificationList(
            final Long[] products, final String[] productBatch, final Long customerId, final Long storeArea) {
        Specification<BookInventory> specification = new Specification<BookInventory>() {
            @Override
            public Predicate toPredicate(Root<BookInventory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                Path storeAreaPath = root.get("storeLocation").get("storeArea").get("id");
                Path customerPath = root.get("customer").get("id");
                Path productPath = root.get("product").get("id");
                /*Path storeContainerPath = root.get("storeContainer").get("id");*/
                Path stockInTimePath = root.get("stockInTime");
                Path inboundRegisterSerialNoPath = root.get("inboundRegisterSerialNo");

                List<Predicate> list = new ArrayList<Predicate>();
                if (customerId != null) {
                    Predicate p1 = criteriaBuilder.equal(customerPath, customerId);
                    list.add(p1);
                }
        /*		if (storeContainers != null && storeContainers.length > 0) {
                     for (Long Container : storeContainers) {
						 Predicate p2 = criteriaBuilder.notEqual(storeContainerPath, Container);
						 list.add(p2);
					}

				}*/

                if (products != null && products.length > 0) {
                    Predicate wholeProductPredicates = criteriaBuilder.notEqual(criteriaBuilder.literal(1), 1);
                    for (int i = 0; i < products.length; i++) {
                        Predicate productPredicate = criteriaBuilder.equal(productPath, products[i]);
                        Predicate batchPredicate = criteriaBuilder.equal(inboundRegisterSerialNoPath, productBatch[i]);
                        Predicate matchProductPredicate = criteriaBuilder.and(productPredicate, batchPredicate);
                        wholeProductPredicates = criteriaBuilder.or(wholeProductPredicates, matchProductPredicate);
                    }
                    list.add(wholeProductPredicates);
                }

                if (storeArea != null) {
                    Predicate p3 = criteriaBuilder.equal(storeAreaPath, storeArea);
                    list.add(p3);
                }
                Predicate[] p = new Predicate[list.size()];
                criteriaQuery.orderBy((criteriaBuilder.desc(stockInTimePath)));
                return criteriaBuilder.and(list.toArray(p));

            }
        };
        return bookInventoryDao.findAll(specification);
    }

	
	
	public List<BookInventory> findBookInventorysByFilter(List<PropertyFilter> filters) {
		Specification<BookInventory> spec = DynamicSpecifications.buildSpecifitions(BookInventory.class, filters);
		return bookInventoryDao.findAll(spec);
	}

	public BookInventory findByContainer(final Long storeContainerId) {
		return bookInventoryDao.findOne(new Specification<BookInventory>() {
			@Override
			public Predicate toPredicate(Root<BookInventory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				Path storeContainerlabelPath=	root.get("storeContainer").get("id");
				return criteriaBuilder.equal(storeContainerlabelPath, storeContainerId);
			}
		});
	}
	
	public BookInventory findByContainerLabel(final String storeContainerLabel) {
		return bookInventoryDao.findOne(new Specification<BookInventory>() {
			@Override
			public Predicate toPredicate(Root<BookInventory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				Path storeContainerlabelPath=	root.get("storeContainer").get("label");
				return criteriaBuilder.equal(storeContainerlabelPath, storeContainerLabel);
			}
		});
	}
	public BookInventory findByLocation(final String  storeLocation) {
		return bookInventoryDao.findOne(new Specification<BookInventory>() {
			@Override
			public Predicate toPredicate(Root<BookInventory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				Path storeLocationpath=	root.get("storeLocation").get("code");
				return criteriaBuilder.equal(storeLocationpath, storeLocation);
			}
		});
	}

	@Transactional(readOnly = false)
	public void deleteOutboundRegisterItem(Long productId, Long storeContainerId) {
		bookInventoryDao.deleteByProductAndStoreContainer(productId, storeContainerId);
	}

	// 客户库存查询

	public List<Product> queryProductBycustomerId(Long customerId) {
		List<Product> productList = bookInventoryDao.queryProductBycustomerId(customerId);
		return productList;
	}

	public List<BookInventory> findBookInventoryInfo(Long customerId, Date startTime, Date endTime, String productId, final String productionPlace, final Date productionDate) {
		List<BookInventory> bookInventoryList = findBookInventorySpecification(customerId, startTime, endTime, productId,null, productionPlace, productionDate);
		List<BookInventory> bookInventoryMerges =new ArrayList<BookInventory>();
		bookInventoryMerges.addAll(mergeBookInventorysBybatchs(bookInventoryList).values());
		return bookInventoryMerges;
	}

	public List<BookInventory> findBookInventorySpecification(final Long customerId, final Date startTime, final Date endTime, final String productId, final Long storeAreaId, final String productionPlace, final Date productionDate) {
		Specification<BookInventory> specification = new Specification<BookInventory>() {
			@Override
			public Predicate toPredicate(Root<BookInventory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				Path productPath= root.get("product").get("id");
				Path stockInTime = root.get("stockInTime");
				Path customer = root.get("customer").get("id");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Path storeArea = root.get("storeLocation").get("storeArea").get("id");
				Path inboundRegisterSerialNo = root.get("inboundRegisterSerialNo");
				List<Predicate> list = new ArrayList<Predicate>();
				Date startDate=null;
				Path productionPlacePath = root.get("productionPlace");
				Path productionDatePath = root.get("productionDate");
				
				if(startTime==null){
					try {
					  startDate=format.parse("1900-12-12");
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else{
					  startDate=startTime;
				}
				Date	endDate=null;
				if(endTime==null){
						endDate=new Date();
				}else{
						endDate=endTime;
				}
				if(startTime!=null||endTime!=null){
					Predicate	p0 = criteriaBuilder.between(stockInTime, startDate, endDate);
				    list.add(p0);
				}
				
				if (customerId != null) {
					Predicate p1 = criteriaBuilder.equal(customer, customerId);
					list.add(p1);
				}
				if (StringUtils.isNotBlank(productId)) {
					Predicate p5 = criteriaBuilder.equal(productPath, productId);
					list.add(p5);
				}
				if (storeAreaId != null ) {
					List<StoreArea> storeAreaList = storeAreaAssigneeService.findStoreAreas(Long.valueOf(storeAreaId));
					List<Long> storeAreaIds = new ArrayList<Long>();
					for (StoreArea storeArea2 : storeAreaList) {
						storeAreaIds.add(storeArea2.getId());
					}
					Predicate p3 = storeArea.in(storeAreaIds.toArray());
					list.add(p3);
				}
				
				if(productionDate!=null){
					Predicate p6 = criteriaBuilder.between(productionDatePath, productionDate, productionDate);
				    list.add(p6);
				}
				
				if (StringUtils.isNotBlank(productionPlace)) {
					Predicate p7 = criteriaBuilder.equal(productionPlacePath, productionPlace);
					list.add(p7);
				}
				
				Predicate[] p = new Predicate[list.size()];
				criteriaQuery.orderBy((criteriaBuilder.asc(inboundRegisterSerialNo)));
				return criteriaBuilder.and(list.toArray(p));

			}
		};
		return bookInventoryDao.findAll(specification);
	}

	/**
	 * 合并商品信息,按商品和批次分类,没有批次号按日期分类
	 * 
	 * @param bookInventorys
	 * @return
	 */
	public Map<String,BookInventory> mergeBookInventorysBybatchs(List<BookInventory> bookInventorys) {
		Map<String, BookInventory> jsonlist = new LinkedHashMap<String, BookInventory>();
		BookInventory obj=null;
		//SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		for (BookInventory bookInventory : bookInventorys) { 
			String key = "";
			 //if(StringUtils.isNotBlank(bookInventory.getInboundRegisterSerialNo())){
			key=bookInventory.getInboundRegisterSerialNo()+"_"+bookInventory.getProduct().getId();
			//}else{
				// continue; 
				  //key = format.format(bookInventory.getStockInTime())+"_"+bookInventory.getProduct().getId();
			//}
	 
			if (!jsonlist.containsKey(key)) {
				obj=new BookInventory();
				obj.setAmount(bookInventory.getAmount());
				obj.setAmountMeasureUnit(bookInventory.getAmountMeasureUnit());
				obj.setCustomer(bookInventory.getCustomer());
				obj.setInboundRegisterSerialNo(bookInventory.getInboundRegisterSerialNo());
				obj.setPacking(bookInventory.getPacking());
				obj.setProduct(bookInventory.getProduct());
				obj.setProductDetail(bookInventory.getProductDetail());
				obj.setProductStatus(bookInventory.getProductStatus());
				obj.setQualified(bookInventory.isQualified());
				obj.setQuanlityGuaranteePeriod(bookInventory.getQuanlityGuaranteePeriod());
				obj.setSpec(bookInventory.getSpec());
				obj.setProductionPlace(bookInventory.getProductionPlace());
				obj.setStoreDuration(bookInventory.getStoreDuration());
				obj.setWeight(bookInventory.getWeight());
				obj.setWeightMeasureUnit(bookInventory.getWeightMeasureUnit());
				obj.setStockIn(bookInventory.getStockIn());
				obj.setStockInOperator(bookInventory.getStockInOperator());
				obj.setStockInTime(bookInventory.getStockInTime());
				obj.setStoreContainer(bookInventory.getStoreContainer());
				obj.setStoreLocation(bookInventory.getStoreLocation());
				obj.setProductionDate(bookInventory.getProductionDate());
				obj.setTotalStoreContainer(1);//计算预计托盘数
				jsonlist.put(key, obj);
			} else {
				BookInventory putbookInventory = jsonlist.get(key);
				putbookInventory.setWeight(putbookInventory.getWeight() + bookInventory.getWeight());
				putbookInventory.setAmount(putbookInventory.getAmount() + bookInventory.getAmount());
				putbookInventory.setTotalStoreContainer(putbookInventory.getTotalStoreContainer()+1);
				jsonlist.put(key, putbookInventory);
			}
		}
			 
			return jsonlist;
	}
	
	
	/**
	 * 
	 * @param period 保管到预警或变质期预警提前的天数
	 * @param WarningType 	     1. 到期预警  2.变质预警 
	 * @return
	 */
	@Transactional(readOnly = false)
	public void inventoryWarning(int period,int warningType){
		Map<String,BookInventory> bookInventorys=mergeBookInventorysBybatchs(bookInventoryDao.findAll());
		 	Date date=null;
		 	int days=0;
		 	Calendar	calendar   =   new   GregorianCalendar(); 
			calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH),
						calendar.get(calendar.DAY_OF_MONTH),23,59,0);
			int currentDay=calendar.get(calendar.DATE);
			Long currentime= calendar.getTimeInMillis();
			for (BookInventory bookInventory : bookInventorys.values()) {
					 if(warningType==1){
						 date= bookInventory.getStockInTime();
						 days= bookInventory.getStoreDuration();
					 }else if(warningType==2){
						 date=bookInventory.getProductionDate();
						 days= bookInventory.getQuanlityGuaranteePeriod();
					 }
					 if(date==null||days==0){
						 continue;
					 }
				
				     calendar.setTime(date); 
				     calendar.add(calendar.DATE,days-period);//把日期往后增加天.整数往后推,负数往前移动 
				     Long datatime= calendar.getTimeInMillis();
				     
				     if(currentime>datatime&&calendar.get(calendar.DATE)==currentDay){
				    	 if(warningType==1){
								taskService.assignRetentionPeriodNotice(bookInventory);
						}else if(warningType==2){
								taskService.assignGuaranteePeriodNotice(bookInventory);
						}
				     }
			}
	 }
	/**
	 * 获得录入商品可选择的托盘
	 * @param id
	 * @return
	 */
	public List<StoreContainer> findstoreContainers(Long id,String label) {
		
		List<StoreContainer> storeContainerList=new ArrayList<StoreContainer>();
		if(id!=null){
			BookInventory bookInventory = bookInventoryDao.findOne(id);
			storeContainerList.add(bookInventory.getStoreContainer());
		}
		List<StoreContainer>   storeContainerUnfinished =inboundReceiptService.findByUnfinished();
		List<StoreContainer>  storeContainerAll =storeContainerService.findUnusedAll(label);
		 
		if(!storeContainerUnfinished.isEmpty()&&!storeContainerAll.isEmpty()){
			storeContainerAll.removeAll(storeContainerUnfinished);
		}
		storeContainerList.addAll(storeContainerAll);
		return  storeContainerList;
	}
	/**
	 * 保管期满预警
	 */
	@Transactional(readOnly = false)
	public  void  expireWarning(){
		/*SimpleDateFormat  format=	new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("**********保管期满提醒**************"+format.format(new Date()));*/
		SystemConfig  sysconfig=systemConfigDao.findByAttribute(SystemConfigKey.保管期预警提前天数);
		if(StringUtils.isNotBlank(sysconfig.getValue())){
			inventoryWarning(Integer.valueOf(sysconfig.getValue()),1);
		}
	}
	/**
	 * 保质过期预警
	 */
	@Transactional(readOnly = false)
	public  void  metamorphicWarning(){
	/*	SimpleDateFormat  format=	new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("**********保质过期提醒**************"+format.format(new Date()));*/
		SystemConfig  sysconfig=systemConfigDao.findByAttribute(SystemConfigKey.保质期预警提前天数);
		if(StringUtils.isNotBlank(sysconfig.getValue())){
			inventoryWarning(Integer.valueOf(sysconfig.getValue()),2);
		}
		
	}
	/**
	 * 查询即将到三天内到期的库存
	 * 1.保管到期，2，保质到期
	 */
	public  List<BookInventory>  findExpire(Long type){
		List<BookInventory>  list=null;
		Calendar   calendar=new GregorianCalendar();
		calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH),
				calendar.get(calendar.DAY_OF_MONTH),0,0,0);
		Date nowDate=calendar.getTime();
		calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH),
				calendar.get(calendar.DAY_OF_MONTH)+4,0,0,0);
		Date endDate=calendar.getTime();
		if(type!=null&&type==1){
			list=bookInventoryDao.findByStockInTime(nowDate,endDate);
		}else{
			list=bookInventoryDao.findByProductionDate(nowDate,endDate);
		}
		return list;
	}
}
