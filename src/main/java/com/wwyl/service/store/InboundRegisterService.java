package com.wwyl.service.store;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Enums.BookingStatus;

import com.wwyl.Enums.InboundType;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.StockInStatus;
import com.wwyl.Enums.StoreLocationStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.finance.PaymentLogDao;
import com.wwyl.dao.settings.CommentDao;
import com.wwyl.dao.settings.StoreLocationDao;
import com.wwyl.dao.store.InboundBookingDao;
import com.wwyl.dao.store.InboundBookingItemDao;
import com.wwyl.dao.store.InboundRegisterDao;
import com.wwyl.dao.store.InboundRegisterItemDao;
import com.wwyl.dao.store.InboundTarryDao;
import com.wwyl.dao.finance.PaymentDao;
import com.wwyl.dao.store.StockInDao;
import com.wwyl.entity.finance.Payment;

import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.HandSet;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.settings.TallyArea;

import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.InboundBooking;
import com.wwyl.entity.store.InboundBookingItem;
import com.wwyl.entity.store.InboundReceiptItem;
import com.wwyl.entity.store.InboundRegister;
import com.wwyl.entity.store.InboundRegisterItem;
import com.wwyl.entity.store.InboundTarry;
import com.wwyl.entity.store.StockIn;
import com.wwyl.service.finance.PaymentService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.HandSetService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreContainerService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.settings.TallyAreaService;
import com.wwyl.service.settings.TaskService;
/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class InboundRegisterService {
	@Resource
	private InboundRegisterDao inboundRegisterDao;
	@Resource
	private InboundRegisterItemDao inboundRegisterItemDao;
	@Resource
	private InboundBookingItemDao inboundBookingItemDao;
	@Resource
	private StoreLocationDao storeLocationDao;
	@Resource
	private StockInDao stockInDao;
	@Resource
	private PaymentDao paymentDao;
	@Resource
	private PaymentLogDao paymentLogDao;
	@Resource
	private CommentDao commentDao;
	@Resource
	private InboundTarryDao inboundTarryDao;

	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private StoreLocationService storeLocationService;

	@Resource
	private TaskService taskService;

	@Resource
	private PaymentService paymentService;
	@Resource
	private InboundBookingDao inboundBookingDao;
	@Resource
	private InboundBookingService inboundBookingService;
	@Resource
	private HandSetService handSetServeice;
	@Resource
	private CustomerService customerService;
	@Resource
	private InboundReceiptService inboundReceiptService;
	@Resource
	private InboundTarryService inboundTarryService;
	@Resource
	StoreContainerService storeContainerService;
	@Resource
	BookInventoryService  bookInventoryService;
	@Resource
	TallyAreaService tallyAreaService;
	public List<InboundRegister> findAll() {
		return inboundRegisterDao.findAll();
	}

	public InboundRegister findOne(Long id) {
		return inboundRegisterDao.findOne(id);
	}

	public InboundRegister findBySerialNo(String serialNo) {
		return inboundRegisterDao.findBySerialNo(serialNo);
	}

	@Transactional(readOnly = false)
	public Long save(InboundRegister inboundRegister,String[] productChecks) {
		// 车型为空时候
		if (inboundRegister.getVehicleType() != null && inboundRegister.getVehicleType().getId() == null) {
			inboundRegister.setVehicleType(null);
		}

		if (inboundRegister.getInboundBooking() != null && inboundRegister.getInboundBooking().getId() != null) {
			inboundRegisterDao.save(inboundRegister);
			if (productChecks!=null&&productChecks.length>0) {
				InboundBooking inboundBooking = inboundBookingDao.findOne(inboundRegister.getInboundBooking().getId());
				Set<InboundBookingItem> inboundBookingItems = inboundBooking.getInboundBookingItems();
				List<InboundBookingItem>  mergeinboundBookingItems= inboundBookingService.mergeInboundBookingItem(inboundBookingItems);
				//根据预约单生成登记的明细
				InboundRegisterItem inboundRegisterItem = null;
				for (InboundBookingItem inboundBookingItem : mergeinboundBookingItems) {

					for (String  productCheck : productChecks) {
						String[] item = productCheck.split(":");
						if (inboundBookingItem.getProduct().getId().equals(Long.valueOf(item[0]))) {

							//设置登记单明细数量重量,保存登记单详情
							inboundRegisterItem = new InboundRegisterItem();
							double weight = weightBudget(inboundBookingItem.getAmount(),
										inboundBookingItem.getWeight(),item[1]);
							inboundRegisterItem.setAmount(Double.valueOf(item[1]));
							inboundRegisterItem.setStoreContainerCount(Integer.valueOf(item[2]));
							inboundRegisterItem.setWeight(weight);
							inboundRegisterItem.setInboundRegister(inboundRegister);
							inboundRegisterItem.setAmountMeasureUnit(inboundBookingItem.getAmountMeasureUnit());
							inboundRegisterItem.setPacking(inboundBookingItem.getPacking());
							inboundRegisterItem.setProduct(inboundBookingItem.getProduct());
							inboundRegisterItem.setQualityGuaranteePeriod(inboundBookingItem.getQualityGuaranteePeriod());
							inboundRegisterItem.setSpec(inboundBookingItem.getSpec());
							inboundRegisterItem.setStoreDuration(inboundBookingItem.getStoreDuration());
							inboundRegisterItem.setWeightMeasureUnit(inboundBookingItem.getWeightMeasureUnit());
							inboundRegisterItem.setProductionDate(inboundBookingItem.getProductionDate());
							inboundRegisterItem.setStoreArea(inboundBooking.getStoreArea());
							inboundRegisterItemDao.save(inboundRegisterItem);
						}
					 }
				}
			}
			inboundBookingDao.updateStatus(BookingStatus.已完成, inboundRegister.getInboundBooking().getId());
		} else {
			inboundRegister.setInboundBooking(null);
			inboundRegisterDao.save(inboundRegister);

		}

		taskService.assignInboundRegistNotice(inboundRegister);
		return inboundRegister.getId();
	}


	 /**
	  * 预估入库的重量
	  * @param countAmount 商品总数量
	  * @param countWeight 商品总重量
	  * @param amount	        数量
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

	@Transactional(readOnly = false)
	public void delete(Long id) {
		inboundRegisterDao.delete(id);
	}
	//验证是否还需要生成新的预约单
	public boolean validation(Long inboundBookingId,String[] productChecks){
		Set<InboundBookingItem>	inboundBookingItems=inboundBookingDao.findOne(inboundBookingId).getInboundBookingItems();
		List<InboundBookingItem>  mergeinboundBookingItems= inboundBookingService.mergeInboundBookingItem(inboundBookingItems);
		boolean result=false;
		for (InboundBookingItem inboundBookingItem : mergeinboundBookingItems) {
			boolean exist=false;
			for (String  productCheck : productChecks) {
				String[] item=productCheck.split(":");
				if(inboundBookingItem.getProduct().getId().equals(Long.valueOf(item[0]))){
					double  difference =	inboundBookingItem.getAmount()-Double.valueOf(item[1]);
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
	public Page<InboundRegister> findInboundRegisterByConditions(int page, int rows, final PaymentStatus[] paymentStatus, final StockInStatus[] stockInStatus, final String serialNo,
			final InboundType inboundType, final Long customerId) {
		Specification<InboundRegister> specification = new Specification<InboundRegister>() {
			@Override
			public Predicate toPredicate(Root<InboundRegister> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				Path serialNoPath = root.get("serialNo");
				Path customerPath = root.get("customer").get("id");
				Path inboundTypePath = root.get("inboundType");
				Path inboundTimePath = root.get("inboundTime");
				Path stockInStatusPath = root.get("stockInStatus");
				Path paymentStatuPath = root.get("payment").get("paymentStatus");
				Path paymentPath = root.get("payment");
				Path departmentPath = root.get("registerOperator").get("department").get("id");

				Long departmentId = 0L;

				Predicate orPredicate = null;
				if (customerId != null) {
					Predicate p1 = criteriaBuilder.equal(customerPath, customerId);
					list.add(p1);
				}
				if (inboundType != null) {
					Predicate p1 = criteriaBuilder.equal(inboundTypePath, inboundType);
					list.add(p1);
				}
				if (serialNo != null && serialNo.trim().length() > 0) {
					Predicate p2 = criteriaBuilder.like(serialNoPath, "%" + serialNo + "%");
					list.add(p2);
				}
				if (stockInStatus != null) {
					// 查询未结算登记单

					Predicate p1 = criteriaBuilder.isNull(paymentPath);
					list.add(p1);

					Predicate p2 = stockInStatusPath.in((Object[])stockInStatus);
					list.add(p2);
				}
				if (paymentStatus != null&&paymentStatus.length>0) {
						Predicate p  = paymentStatuPath.in((Object[])paymentStatus);
						list.add(p);
				}
				//部门高温低温库
				departmentId = ThreadLocalHolder.getCurrentOperator().getDepartment().getId();

				if (departmentId ==5) {
					Predicate p3 = criteriaBuilder.equal(departmentPath, departmentId);
					list.add(p3);
				}

				if (departmentId ==6) {
					Predicate p3 = criteriaBuilder.equal(departmentPath, departmentId);
					list.add(p3);
				}

				Predicate[] p = new Predicate[list.size()];
				criteriaQuery.orderBy((criteriaBuilder.desc(inboundTimePath)));
				return criteriaBuilder.and(list.toArray(p));
			}
		};
		return inboundRegisterDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}

	public List<InboundRegister> findUncheckedInboundRegister(final String serialNo) {
		return inboundRegisterDao.findInboundRegisterUnderStatus(StockInStatus.已清点, serialNo);
	}

	@Transactional(readOnly = false)
	public void deleteItem(Long id) {
		inboundRegisterItemDao.delete(id);
	}

	public InboundRegisterItem findOneItem(Long id) {
		return inboundRegisterItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveItem(InboundRegisterItem inboundRegisterItem) {
		InboundRegisterItem inboundRegisterItemObj=inboundRegisterItemDao.findInboundRegisterIdAndproId(inboundRegisterItem.getInboundRegister().getId(),inboundRegisterItem.getProduct().getId());
		if(inboundRegisterItemObj!=null){
			InboundRegisterItem inboundRegisterItemOld=null;
			if(inboundRegisterItem.getId()!=null){
				//编辑验证
				inboundRegisterItemOld=	inboundRegisterItemDao.findOne(inboundRegisterItem.getId());
			}
			if(inboundRegisterItem.getId()==null ||(inboundRegisterItemOld!=null &&!inboundRegisterItemObj.getProduct().getId().equals(inboundRegisterItemOld.getProduct().getId()))){
				throw new RuntimeException("商品："+inboundRegisterItemObj.getProductName()+"已添加，可进行明细修改！");
			}

		}
		// 包装为空
		if (inboundRegisterItem.getPacking().getId() == null) {
			inboundRegisterItem.setPacking(null);
		}
		inboundRegisterItemDao.save(inboundRegisterItem);
	}

	public List<InboundRegisterItem> findInboundRegisterItems(String serialNo) {
		return inboundRegisterItemDao.findInboundRegisterItems(serialNo);
	}

	@Transactional(readOnly = false)
	public void update(InboundRegister inboundRegister) {
		// 车型为空时候
		if (inboundRegister.getVehicleType().getId() == null) {
			inboundRegister.setVehicleType(null);
		}
		if (inboundRegister.getInboundBooking() != null && inboundRegister.getInboundBooking().getId() == null) {
			inboundRegister.setInboundBooking(null);
		}
		inboundRegisterDao.save(inboundRegister);
	}

	// 客户入库查询

	public Page<InboundRegister> findInboundRegisterSpecification(final Long customerId, final Date startTime, final Date endTime, final StockInStatus[] stockInStatus,int page, int rows) {
		Specification<InboundRegister> specification = new Specification<InboundRegister>() {
			@Override
			public Predicate toPredicate(Root<InboundRegister> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				Path inboundTime = root.get("inboundTime");
				Path customer = root.get("customer").get("id");
				Path stockInStatusPath = root.get("stockInStatus");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date	startDate=null;
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

				List<Predicate> list = new ArrayList<Predicate>();
				if(stockInStatus!=null){
					Predicate p1 = stockInStatusPath.in((Object[])stockInStatus);
					list.add(p1);
				}
				Predicate	p2 = criteriaBuilder.between(inboundTime, startDate,endDate);
				list.add(p2);
				if (customerId != null) {
					Predicate p3 = criteriaBuilder.equal(customer, customerId);
					list.add(p3);
				}
				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));

			}
		};
		return inboundRegisterDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}

	@Transactional(readOnly = false)
	public void updateStatus(Long id, StockInStatus stockInStatus) {
		if(stockInStatus==StockInStatus.已作废){
			InboundRegister  inboundRegister=inboundRegisterDao.findOne(id);
			if(inboundRegister.getPayment()!=null){
				throw  new RuntimeException("入库单已经结算过，不可作废！");
			}
		}
		inboundRegisterDao.updateStatus(id, stockInStatus);
	}
	@Transactional(readOnly = false)
	public void complete(Long id, StockInStatus stockInStatus) {
		/*Set<StoreLocation>  storeLocations 	=inboundRegister.getPreStoreLocations();
		//将还是预留状态的储位改为可使用
		for (StoreLocation storeLocation : storeLocations) {
			if(storeLocation.getStoreLocationStatus()==StoreLocationStatus.预留){
				storeLocationService.updateStoreLocationStatus(storeLocation.getId(), StoreLocationStatus.可使用);
			}
		}*/
		inboundRegisterDao.updateStatus(id, stockInStatus);
	}

	@Transactional(readOnly = false)
	public void updateInboundTarrychecked(Long inboundRegisterid,Long tallyAreaid, boolean checked) {
		inboundRegisterDao.updateInboundTarrychecked(checked,inboundRegisterid,tallyAreaid);
	}

	@Transactional(readOnly = false)
	public boolean  tallyareaStatus(Long id) {
		Set<InboundTarry> inboundTarrys=inboundRegisterDao.findOne(id).getInboundTarrys();
		boolean result=true;
		for (InboundTarry inboundTarry : inboundTarrys) {
			if(!inboundTarry.isChecked()){
				result = inboundTarry.isChecked();
				break;
			}
		}
		return result;
	}
	// 付款 FIXME TJ
	@Transactional(readOnly = false)
	public  Map<String,Float>  payment(Long id,String aCardMac,String sMemberCode,String password) {
		InboundRegister inboundRegister = inboundRegisterDao.findOne(id);
		Payment payment = inboundRegister.getPayment();
		Map<String,Float> result = new HashMap<String, Float>();
		if(payment!=null){
			result=	paymentService.payment(new Long[]{payment.getId()},aCardMac,sMemberCode,password);
			//发送储位分派通知
			//taskService.assignStoreLocationNotice(inboundRegister);
			if(inboundRegister.getStockInStatus().equals(StockInStatus.已上架)){
				updateStatus(id,StockInStatus.已完成);
			}
		}
		return result;
	}

	// 延迟缴费
	@Transactional(readOnly = false)
	public void delay(Long id, String remark, PaymentStatus paymentStatus) {

		InboundRegister inboundRegister = inboundRegisterDao.findOne(id);
		if(paymentStatus==PaymentStatus.延付已生效){
			//发送储位分派通知
			//taskService.assignStoreLocationNotice(inboundRegister);
			if(inboundRegister.getStockInStatus().equals(StockInStatus.已上架)){
				updateStatus(id,StockInStatus.已完成);
			}
		}
		Payment payment = inboundRegister.getPayment();
		paymentService.delay(payment.getId(), remark, paymentStatus);
	}

	@Transactional(readOnly = false)
	public void saveTallyArea(Long inboundRegisterId, Long  tallyAreaId,Long handSetId) {
		List<InboundTarry> inboundTarryList = inboundTarryDao
				.findByInboundRegisterId(inboundRegisterId);
		for (InboundTarry inboundTarry : inboundTarryList) {
			inboundTarryDao.delete(inboundTarry.getId());
		}
		HandSet handSet = handSetServeice.findOne(handSetId);
		InboundTarry inboundTarrynew = new InboundTarry();
		InboundRegister inboundRegister = new InboundRegister();
		inboundRegister.setId(Long.valueOf(inboundRegisterId));
		inboundTarrynew.setInboundRegister(inboundRegister);
		TallyArea tallyArea = new TallyArea();
		tallyArea.setId(tallyAreaId);
		inboundTarrynew.setTallyArea(tallyArea);
		inboundTarrynew.setChecked(false);
		inboundTarrynew.setHandsetAddress(handSet.getMac());
		inboundTarryDao.save(inboundTarrynew);

	}

/*	@Transactional(readOnly = false)
	public void saveLocation(Long inboundRegisterId,String locationCodes) {
	
		Set<StoreLocation> storeLocationsNew = storeLocationService.findByCodeIn(locationCodes.split(","));
		InboundRegister inboundRegister =inboundRegisterDao.findOne(inboundRegisterId);
		Set<StoreLocation> storeLocationsOld = inboundRegister.getPreStoreLocations();
	 
		if (storeLocationsOld != null && !storeLocationsOld.isEmpty()) {
			for (StoreLocation storeLocation : storeLocationsOld) {
				// 改为可使用
				storeLocationDao.updateStoreLocationStatus(storeLocation.getId(), StoreLocationStatus.可使用);
			}
		}
		if (storeLocationsNew != null && !storeLocationsNew.isEmpty()) {
			for (StoreLocation storeLocation : storeLocationsNew) {
				// 改为预留
				storeLocationDao.updateStoreLocationStatus(storeLocation.getId(), StoreLocationStatus.预留);
			}
		}
		inboundRegister.setPreStoreLocations(storeLocationsNew);
		inboundRegisterDao.save(inboundRegister);
		// 保存上架单
	
		Set<StockIn>	stockInSet=inboundRegister.getStockIns();
		if(stockInSet==null||stockInSet.isEmpty()){
			addStockIn(inboundRegister);
			taskService.assignStockInNotice(inboundRegister);
		}
		
	}*/
	/**
	 *
	 * @param ids  登记单id数组
	 * @param serialNo 登记单号
	 * @return
	 */
	public List<InboundRegister> findByids(Object[] ids,String serialNo){
		return inboundRegisterDao.findByids(ids,serialNo);
	}

	/**
	 *
	 * @param mac
	 *            手持机内部标识
	 * @return
	 */
	public List<InboundRegister> findInboundRegisterByMac(String mac, String inboundSerialNo) {
		List<InboundRegister> result = null;
		List<Long> list = new ArrayList<Long>();
		List<InboundTarry> inboundTarrys = inboundTarryService
				.findInboundTarryByConditions(mac, StockInStatus.已派送);
		if (!inboundTarrys.isEmpty()) {
			for (InboundTarry inboundTarry : inboundTarrys) {
				list.add(inboundTarry.getInboundRegister().getId());
			}
		}
		if(list.size() > 0)
		{
			result = findByids(list.toArray(),inboundSerialNo);
		}
		return result;
	}

	/**
	 *
	 * @param register  入库登记单
	 * @return
	 */
	public Set<InboundRegisterItem> getInboundRegisterItems(
			InboundRegister register) {
		Set<InboundRegisterItem> inboundRegisterItems = register
				.getInboundRegisterItems();
		Set<InboundReceiptItem> inboundReceiptItems = register
				.getInboundReceiptItems();
		if (!inboundReceiptItems.isEmpty()) {
			for (InboundRegisterItem inboundRegisterItem : inboundRegisterItems) {
				for (InboundReceiptItem inboundReceiptItem : inboundReceiptItems) {
					if (inboundRegisterItem.getProduct().getId()
							.equals(inboundReceiptItem.getProduct().getId())) {
						inboundRegisterItem.setCheckamount(inboundRegisterItem
								.getCheckamount()
								+ inboundReceiptItem.getAmount());
						inboundRegisterItem.setCheckWeight(inboundRegisterItem
								.getCheckWeight()
								+ inboundReceiptItem.getWeight());
						inboundRegisterItem
								.setCheckContainerCountAmount(inboundRegisterItem
										.getCheckContainerCountAmount() + 1);
					}
				}

			}
		}
		return inboundRegisterItems;
	}

	/**
	 * @param inboundReceiptItem  入库验货明细
	 * @param inboundRegisterItemId  入库明细ID
	 * @param amount  数量
	 * @param weight  重量
	 * @param tallyArea  卸货平台
	 * @param storeContainer  托盘
	 * @param readCode 抄码号
	 */
	@Transactional(readOnly = false)
	public synchronized void saveInboundReceiptItem(Long inboundReceiptItemId,
			Long inboundRegisterItemId, int amount, double weight,
			Long tallyAreaid, String containerCode, String readCode)
			throws Exception {

		if (inboundRegisterItemId == null || amount <= 0
				|| StringUtils.isBlank(containerCode)) {
			throw  new RuntimeException("无效的参数");
		}

		StoreContainer storeContainer = storeContainerService
				.findStoreContainerByLabel(containerCode);
		if (storeContainer == null) {
			throw  new RuntimeException( "无效的托盘号");
		}

		// 从来未使用，不再上架过程中
		InboundReceiptItem preReceiptItem = inboundReceiptService
				.getLastInboundReceiptItemByContainerCode(containerCode);
		if (preReceiptItem != null
				&& !(preReceiptItem.getInboundRegister().getStockInStatus()
						.ordinal() >= StockInStatus.已上架.ordinal())) {
			throw  new RuntimeException(
					"此托盘已清点完成 - 收货ID: " + preReceiptItem.getId());
		}

		// 并且库存为空
		BookInventory bookInventory = bookInventoryService
				.findByContainerLabel(containerCode);
		if (bookInventory != null) {
			throw  new RuntimeException(
					"此托盘已在库内使用 - 收货ID: " + preReceiptItem.getId());
		}

		TallyArea tallyArea = tallyAreaService.findOne(tallyAreaid);
		if (tallyArea == null) {
			throw  new RuntimeException( "无效的理货区");
		}
		//


		InboundReceiptItem inboundReceiptItem = new InboundReceiptItem();
		if (inboundReceiptItemId != null) {
			inboundReceiptItem = inboundReceiptService
					.findOneItem(inboundReceiptItemId);
		}
		//入库抄码号
		if (readCode != null) {
			inboundReceiptItem.setReadCode(readCode);
		}

		inboundReceiptItem.setAmount(amount);
		inboundReceiptItem.setWeight(weight);
		inboundReceiptItem.setStoreContainer(storeContainer);
		inboundReceiptItem.setTarryArea(tallyArea);
		InboundRegisterItem inboundRegisterItem = null;
		if (inboundReceiptItem.isNew()) {
			inboundRegisterItem = findOneItem(inboundRegisterItemId);
			inboundReceiptItem.setAmountMeasureUnit(inboundRegisterItem
					.getAmountMeasureUnit());
			inboundReceiptItem.setInboundRegister(inboundRegisterItem
					.getInboundRegister());
			inboundReceiptItem.setPacking(inboundRegisterItem.getPacking());
			inboundReceiptItem.setProduct(inboundRegisterItem.getProduct());
			inboundReceiptItem.setQualified(true);
			inboundReceiptItem.setProductionPlace(inboundRegisterItem
					.getProductionPlace());
			inboundReceiptItem.setProductionDate(inboundRegisterItem
					.getProductionDate());
			inboundReceiptItem.setQualityGuaranteePeriod(inboundRegisterItem
					.getQualityGuaranteePeriod());
			inboundReceiptItem.setReceiptor(ThreadLocalHolder
					.getCurrentOperator());
			inboundReceiptItem.setReceiptTime(new Date());
			inboundReceiptItem.setSpec(inboundRegisterItem.getSpec());
			inboundReceiptItem.setStoreDuration(inboundRegisterItem
					.getStoreDuration());
			inboundReceiptItem.setWeightMeasureUnit(inboundRegisterItem
					.getWeightMeasureUnit());
			inboundReceiptItem.setStoreArea(inboundRegisterItem.getStoreArea());
		}

		if (StockInStatus.已清点.compareTo(inboundReceiptItem.getInboundRegister()
				.getStockInStatus()) <= 0) {
			throw new Exception("本次入库已完成，无法继续收货");
		}
		inboundReceiptService.saveItem(inboundReceiptItem);
	}

	/**
	 * @param inboundReceiptItem  入库验货明细
	 * @param inboundRegisterItemId  入库明细ID
	 * @param amount  数量
	 * @param weight  重量
	 * @param tallyArea  卸货平台
	 * @param storeContainer  托盘
	 */
	public InboundRegisterItem getInboundRegisterItem(Long inboundRegisterItemId)
	{

		InboundRegisterItem inboundRegisterItem = findOneItem(inboundRegisterItemId);
		Set<InboundReceiptItem> inboundReceiptItems = findOne(inboundRegisterItem.getInboundRegister().getId())
				.getInboundReceiptItems();
		for (InboundReceiptItem inboundReceiptItemObj : inboundReceiptItems) {
			if (inboundRegisterItem.getProduct().getId()
					.equals(inboundReceiptItemObj.getProduct().getId())) {
				inboundRegisterItem.setCheckamount(inboundRegisterItem
						.getCheckamount() + inboundReceiptItemObj.getAmount());
				inboundRegisterItem.setCheckWeight(inboundRegisterItem
						.getCheckWeight() + inboundReceiptItemObj.getWeight());
				inboundRegisterItem
						.setCheckContainerCountAmount(inboundRegisterItem
								.getCheckContainerCountAmount() + 1);
			}
		}
		return inboundRegisterItem;
	}
}
