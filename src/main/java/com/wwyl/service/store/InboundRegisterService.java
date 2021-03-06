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
		// ??????????????????
		if (inboundRegister.getVehicleType() != null && inboundRegister.getVehicleType().getId() == null) {
			inboundRegister.setVehicleType(null);
		}

		if (inboundRegister.getInboundBooking() != null && inboundRegister.getInboundBooking().getId() != null) {
			inboundRegisterDao.save(inboundRegister);
			if (productChecks!=null&&productChecks.length>0) {
				InboundBooking inboundBooking = inboundBookingDao.findOne(inboundRegister.getInboundBooking().getId());
				Set<InboundBookingItem> inboundBookingItems = inboundBooking.getInboundBookingItems();
				List<InboundBookingItem>  mergeinboundBookingItems= inboundBookingService.mergeInboundBookingItem(inboundBookingItems);
				//????????????????????????????????????
				InboundRegisterItem inboundRegisterItem = null;
				for (InboundBookingItem inboundBookingItem : mergeinboundBookingItems) {

					for (String  productCheck : productChecks) {
						String[] item = productCheck.split(":");
						if (inboundBookingItem.getProduct().getId().equals(Long.valueOf(item[0]))) {

							//?????????????????????????????????,?????????????????????
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
			inboundBookingDao.updateStatus(BookingStatus.?????????, inboundRegister.getInboundBooking().getId());
		} else {
			inboundRegister.setInboundBooking(null);
			inboundRegisterDao.save(inboundRegister);

		}

		taskService.assignInboundRegistNotice(inboundRegister);
		return inboundRegister.getId();
	}


	 /**
	  * ?????????????????????
	  * @param countAmount ???????????????
	  * @param countWeight ???????????????
	  * @param amount	        ??????
	  * @return
	  */
	 public  double weightBudget(double  countAmount , double countWeight,String amount) {
	  BigDecimal b1 = new BigDecimal(Double.toString(countAmount));//?????????
	  BigDecimal b2 = new BigDecimal(Double.toString(countWeight));//?????????
	  BigDecimal b3 = new BigDecimal(amount);
	  int DEFAULT_DIV_SCALE=6;
	  BigDecimal  b4=  b2.divide(b1, DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
	  return (b3.multiply(b4)).doubleValue();
	 }

	@Transactional(readOnly = false)
	public void delete(Long id) {
		inboundRegisterDao.delete(id);
	}
	//??????????????????????????????????????????
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
					// ????????????????????????

					Predicate p1 = criteriaBuilder.isNull(paymentPath);
					list.add(p1);

					Predicate p2 = stockInStatusPath.in((Object[])stockInStatus);
					list.add(p2);
				}
				if (paymentStatus != null&&paymentStatus.length>0) {
						Predicate p  = paymentStatuPath.in((Object[])paymentStatus);
						list.add(p);
				}
				//?????????????????????
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
		return inboundRegisterDao.findInboundRegisterUnderStatus(StockInStatus.?????????, serialNo);
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
				//????????????
				inboundRegisterItemOld=	inboundRegisterItemDao.findOne(inboundRegisterItem.getId());
			}
			if(inboundRegisterItem.getId()==null ||(inboundRegisterItemOld!=null &&!inboundRegisterItemObj.getProduct().getId().equals(inboundRegisterItemOld.getProduct().getId()))){
				throw new RuntimeException("?????????"+inboundRegisterItemObj.getProductName()+"????????????????????????????????????");
			}

		}
		// ????????????
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
		// ??????????????????
		if (inboundRegister.getVehicleType().getId() == null) {
			inboundRegister.setVehicleType(null);
		}
		if (inboundRegister.getInboundBooking() != null && inboundRegister.getInboundBooking().getId() == null) {
			inboundRegister.setInboundBooking(null);
		}
		inboundRegisterDao.save(inboundRegister);
	}

	// ??????????????????

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
		if(stockInStatus==StockInStatus.?????????){
			InboundRegister  inboundRegister=inboundRegisterDao.findOne(id);
			if(inboundRegister.getPayment()!=null){
				throw  new RuntimeException("??????????????????????????????????????????");
			}
		}
		inboundRegisterDao.updateStatus(id, stockInStatus);
	}
	@Transactional(readOnly = false)
	public void complete(Long id, StockInStatus stockInStatus) {
		/*Set<StoreLocation>  storeLocations 	=inboundRegister.getPreStoreLocations();
		//?????????????????????????????????????????????
		for (StoreLocation storeLocation : storeLocations) {
			if(storeLocation.getStoreLocationStatus()==StoreLocationStatus.??????){
				storeLocationService.updateStoreLocationStatus(storeLocation.getId(), StoreLocationStatus.?????????);
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
	// ?????? FIXME TJ
	@Transactional(readOnly = false)
	public  Map<String,Float>  payment(Long id,String aCardMac,String sMemberCode,String password) {
		InboundRegister inboundRegister = inboundRegisterDao.findOne(id);
		Payment payment = inboundRegister.getPayment();
		Map<String,Float> result = new HashMap<String, Float>();
		if(payment!=null){
			result=	paymentService.payment(new Long[]{payment.getId()},aCardMac,sMemberCode,password);
			//????????????????????????
			//taskService.assignStoreLocationNotice(inboundRegister);
			if(inboundRegister.getStockInStatus().equals(StockInStatus.?????????)){
				updateStatus(id,StockInStatus.?????????);
			}
		}
		return result;
	}

	// ????????????
	@Transactional(readOnly = false)
	public void delay(Long id, String remark, PaymentStatus paymentStatus) {

		InboundRegister inboundRegister = inboundRegisterDao.findOne(id);
		if(paymentStatus==PaymentStatus.???????????????){
			//????????????????????????
			//taskService.assignStoreLocationNotice(inboundRegister);
			if(inboundRegister.getStockInStatus().equals(StockInStatus.?????????)){
				updateStatus(id,StockInStatus.?????????);
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
				// ???????????????
				storeLocationDao.updateStoreLocationStatus(storeLocation.getId(), StoreLocationStatus.?????????);
			}
		}
		if (storeLocationsNew != null && !storeLocationsNew.isEmpty()) {
			for (StoreLocation storeLocation : storeLocationsNew) {
				// ????????????
				storeLocationDao.updateStoreLocationStatus(storeLocation.getId(), StoreLocationStatus.??????);
			}
		}
		inboundRegister.setPreStoreLocations(storeLocationsNew);
		inboundRegisterDao.save(inboundRegister);
		// ???????????????
	
		Set<StockIn>	stockInSet=inboundRegister.getStockIns();
		if(stockInSet==null||stockInSet.isEmpty()){
			addStockIn(inboundRegister);
			taskService.assignStockInNotice(inboundRegister);
		}
		
	}*/
	/**
	 *
	 * @param ids  ?????????id??????
	 * @param serialNo ????????????
	 * @return
	 */
	public List<InboundRegister> findByids(Object[] ids,String serialNo){
		return inboundRegisterDao.findByids(ids,serialNo);
	}

	/**
	 *
	 * @param mac
	 *            ?????????????????????
	 * @return
	 */
	public List<InboundRegister> findInboundRegisterByMac(String mac, String inboundSerialNo) {
		List<InboundRegister> result = null;
		List<Long> list = new ArrayList<Long>();
		List<InboundTarry> inboundTarrys = inboundTarryService
				.findInboundTarryByConditions(mac, StockInStatus.?????????);
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
	 * @param register  ???????????????
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
	 * @param inboundReceiptItem  ??????????????????
	 * @param inboundRegisterItemId  ????????????ID
	 * @param amount  ??????
	 * @param weight  ??????
	 * @param tallyArea  ????????????
	 * @param storeContainer  ??????
	 * @param readCode ?????????
	 */
	@Transactional(readOnly = false)
	public synchronized void saveInboundReceiptItem(Long inboundReceiptItemId,
			Long inboundRegisterItemId, int amount, double weight,
			Long tallyAreaid, String containerCode, String readCode)
			throws Exception {

		if (inboundRegisterItemId == null || amount <= 0
				|| StringUtils.isBlank(containerCode)) {
			throw  new RuntimeException("???????????????");
		}

		StoreContainer storeContainer = storeContainerService
				.findStoreContainerByLabel(containerCode);
		if (storeContainer == null) {
			throw  new RuntimeException( "??????????????????");
		}

		// ???????????????????????????????????????
		InboundReceiptItem preReceiptItem = inboundReceiptService
				.getLastInboundReceiptItemByContainerCode(containerCode);
		if (preReceiptItem != null
				&& !(preReceiptItem.getInboundRegister().getStockInStatus()
						.ordinal() >= StockInStatus.?????????.ordinal())) {
			throw  new RuntimeException(
					"???????????????????????? - ??????ID: " + preReceiptItem.getId());
		}

		// ??????????????????
		BookInventory bookInventory = bookInventoryService
				.findByContainerLabel(containerCode);
		if (bookInventory != null) {
			throw  new RuntimeException(
					"??????????????????????????? - ??????ID: " + preReceiptItem.getId());
		}

		TallyArea tallyArea = tallyAreaService.findOne(tallyAreaid);
		if (tallyArea == null) {
			throw  new RuntimeException( "??????????????????");
		}
		//


		InboundReceiptItem inboundReceiptItem = new InboundReceiptItem();
		if (inboundReceiptItemId != null) {
			inboundReceiptItem = inboundReceiptService
					.findOneItem(inboundReceiptItemId);
		}
		//???????????????
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

		if (StockInStatus.?????????.compareTo(inboundReceiptItem.getInboundRegister()
				.getStockInStatus()) <= 0) {
			throw new Exception("??????????????????????????????????????????");
		}
		inboundReceiptService.saveItem(inboundReceiptItem);
	}

	/**
	 * @param inboundReceiptItem  ??????????????????
	 * @param inboundRegisterItemId  ????????????ID
	 * @param amount  ??????
	 * @param weight  ??????
	 * @param tallyArea  ????????????
	 * @param storeContainer  ??????
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
