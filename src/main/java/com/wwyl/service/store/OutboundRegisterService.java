package com.wwyl.service.store;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Enums.BookingStatus;
import com.wwyl.Enums.OutboundType;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.PaymentType;
import com.wwyl.Enums.StockOutStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.Constants;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.finance.ExtraChargeConfigDao;
import com.wwyl.dao.finance.ExtraChargeDao;
import com.wwyl.dao.finance.PaymentDao;
import com.wwyl.dao.finance.PaymentItemDao;
import com.wwyl.dao.finance.PaymentLogDao;
import com.wwyl.dao.settings.ChargeTypeDao;
import com.wwyl.dao.settings.CommentDao;
import com.wwyl.dao.store.BookInventoryDao;
import com.wwyl.dao.store.InboundReceiptItemDao;
import com.wwyl.dao.store.OutboundBookingDao;
import com.wwyl.dao.store.OutboundBookingItemDao;
import com.wwyl.dao.store.OutboundRegisterDao;
import com.wwyl.dao.store.OutboundRegisterItemDao;
import com.wwyl.dao.store.OutboundTarryDao;
import com.wwyl.dao.store.StockOutDao;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.ce.CalculatedResult;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.finance.PaymentItem;
import com.wwyl.entity.settings.ChargeType;
import com.wwyl.entity.settings.HandSet;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.TallyArea;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.InboundReceiptItem;
import com.wwyl.entity.store.OutboundCheckItem;
import com.wwyl.entity.store.OutboundRegister;
import com.wwyl.entity.store.OutboundRegisterItem;
import com.wwyl.entity.store.OutboundTarry;
import com.wwyl.entity.store.StockOut;
import com.wwyl.service.ce.StoreCalculator;
import com.wwyl.service.finance.PaymentService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.HandSetService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreContainerService;
import com.wwyl.service.settings.TallyAreaService;
import com.wwyl.service.settings.TaskService;

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class OutboundRegisterService {
	@Resource
	private OutboundRegisterDao outboundRegisterDao;
	@Resource
	private OutboundRegisterItemDao outboundRegisterItemDao;
	@Resource
	private OutboundBookingItemDao outboundBookingItemDao;
	@Resource
	private BookInventoryDao bookInventoryDao;
	@Resource
	private StockOutDao stockOutDao;
	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private PaymentDao paymentDao;
	@Resource
	private PaymentItemDao paymentItemDao;
	@Resource
	private ChargeTypeDao chargeTypeDao;
	@Resource
	private PaymentLogDao paymentLogDao;
	@Resource
	private CommentDao commentDao;
	@Resource
	private OutboundTarryDao outboundTarryDao;
	@Resource
	private PaymentService paymentService;
	@Resource
	private TaskService taskService;
	@Resource
	private OutboundBookingDao outboundBookingDao;
	@Resource
	private ExtraChargeConfigDao extraChargeConfigDao;
	@Resource
	private ExtraChargeDao  extraChargeDao;
	@Resource
	private  HandSetService handSetService;
	@Resource
	private  CustomerService customerService;
	@Resource
	private  StockOutService stockOutService;
	@Resource
	private  OutboundCheckService outboundCheckService;
	@Resource
	InboundReceiptItemDao inboundReceiptItemDao;
	@Resource
	StoreContainerService storeContainerService;
	@Resource
	TallyAreaService tallyAreaService;
	@Resource
	private StoreCalculator storeCalculator;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public List<OutboundRegister> findAll() {
		return outboundRegisterDao.findAll();
	}

	public OutboundRegister findOne(Long id) {
		return outboundRegisterDao.findOne(id);
	}
	public List<OutboundRegister> findByIds(Object[] ids,String serialNo) {
		return outboundRegisterDao.findByIds(ids,serialNo);
	}
	
	public OutboundRegister findBySerialNo(String serialNo) {
		return outboundRegisterDao.findBySerialNo(serialNo);
	}

	public List<OutboundRegister> findUnCheckedOutboundRegister(String serialNo) {
		return outboundRegisterDao.findOutboundRegisterUnderStatus(StockOutStatus.?????????, serialNo);
	}
	
	@Transactional(readOnly = false)
	public void updateOutboundTarrychecked(Long outboundRegisterid,Long tallyAreaid, boolean checked) {
		outboundRegisterDao.updateInboundTarrychecked(checked,outboundRegisterid,tallyAreaid);
	}
	
	@Transactional(readOnly = false)
	public void saveTallyArea(Long outboundRegisterId,Long  tallyAreaId,Long handSetId) {
		List<OutboundTarry> outboundTarryList = outboundTarryDao
				.findByOutboundRegisterId(outboundRegisterId);
		for (OutboundTarry outboundTarry : outboundTarryList) {
			outboundTarryDao.delete(outboundTarry.getId());
		}
		OutboundTarry outboundTarrynew = new OutboundTarry();
		HandSet handSet = handSetService.findOne(handSetId);
		OutboundRegister outboundRegister = new OutboundRegister();
		outboundRegister.setId(Long.valueOf(outboundRegisterId));
		outboundTarrynew.setOutboundRegister(outboundRegister);
		TallyArea tallyArea = new TallyArea();
		tallyArea.setId(tallyAreaId);
		outboundTarrynew.setTallyArea(tallyArea);
		outboundTarrynew.setChecked(false);
		outboundTarrynew.setHandsetAddress(handSet.getMac());
		outboundTarryDao.save(outboundTarrynew);

	}
	
	@Transactional(readOnly = false)
	public void create(OutboundRegister outboundRegister, String[] productCheck) {
		if(outboundRegister.getId()!=null){
			taskService.assignOutboundRegistNotice(outboundRegister);
		}
		// ????????????
		if (outboundRegister.getVehicleType() != null && outboundRegister.getVehicleType().getId() == null) {
			outboundRegister.setVehicleType(null);
		}
		// ??????ID??????
		if (outboundRegister.getOutboundBooking() != null && outboundRegister.getOutboundBooking().getId() != null) {
			outboundBookingDao.updateStatus(BookingStatus.?????????, outboundRegister.getOutboundBooking().getId());
		}else {
			outboundRegister.setOutboundBooking(null);
		}
		
		OutboundRegisterItem item = null;
		if (outboundRegister.getId() != null) {
			List<OutboundRegisterItem> outboundRegisterItems = outboundRegisterItemDao.findByOutboundRegisterItems(outboundRegister.getId());
			for (OutboundRegisterItem outboundRegisterItem : outboundRegisterItems) {
				outboundRegisterItemDao.delete(outboundRegisterItem.getId());
			}

		}
		outboundRegisterDao.save(outboundRegister);
		for (String productCheckstr : productCheck) {
			String[] productamount = productCheckstr.split("_");//[??????,??????ID,??????,???????????????]
			List<BookInventory> bookInventoryList = bookInventoryService.findBookInventorySpecification(outboundRegister.getCustomer().getId(),null,null,productamount[1],null,null,null);
			Map<String,BookInventory> bookInventoryMap=bookInventoryService.mergeBookInventorysBybatchs(bookInventoryList);
			BookInventory bookInventory	=bookInventoryMap.get(productamount[0]+"_"+productamount[1]);
			double weight = weightBudget(bookInventory.getAmount(), bookInventory.getWeight(), productamount[2]);
			item = new OutboundRegisterItem();
			item.setAmount(Double.valueOf(productamount[2]));
			item.setStoreContainerCount(Integer.valueOf(productamount[3]));
			item.setAmountMeasureUnit(bookInventory.getAmountMeasureUnit());
			item.setOutboundRegister(outboundRegister);
			item.setPacking(bookInventory.getPacking());
			item.setProduct(bookInventory.getProduct());
			item.setSpec(bookInventory.getSpec());
			item.setStoreContainer(bookInventory.getStoreContainer());
			item.setWeight(weight);
			item.setWeightMeasureUnit(bookInventory.getWeightMeasureUnit());
			item.setBatchs(productamount[0]);
			item.setProductDetail(bookInventory.getProductDetail());
			outboundRegisterItemDao.save(item);
		}
	
	}
	
	/**
	 * ?????????????????????
	 * 
	 * @param countAmount
	 *            ?????????????????????
	 * @param countWeight
	 *            ?????????????????????
	 * @param amount
	 *            ??????????????????
	 * @return
	 */
	public double weightBudget(double countAmount, double countWeight, String amount) {
		BigDecimal b1 = new BigDecimal(Double.toString(countAmount));// ?????????
		BigDecimal b2 = new BigDecimal(Double.toString(countWeight));// ?????????
		BigDecimal b3 = new BigDecimal(amount);
		int DEFAULT_DIV_SCALE = 30;
		BigDecimal b4 = b2.divide(b1, DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
		return (b3.multiply(b4)).divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		outboundRegisterDao.delete(id);
	}

	public Page<OutboundRegister> findOutboundRegisterConditions(int page, int rows, final PaymentStatus[] paymentStatus, final StockOutStatus stockOutStatus,final String serialNo, final OutboundType outboundType, final Long customerId) {
		Specification<OutboundRegister> specification = new Specification<OutboundRegister>() {
			@Override
			public Predicate toPredicate(Root<OutboundRegister> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				Path serialNoPath = root.get("serialNo");
				Path customerPath = root.get("customer").get("id");
				Path outboundTypePath = root.get("outboundType");
				Path stockOutStatusPath = root.get("stockOutStatus");
				Path paymentStatuPath = root.get("payment").get("paymentStatus");
				Path paymentPath = root.get("payment");
				Path outboundTimePath = root.get("outboundTime");
				Path departmentPath = root.get("registerOperator").get("department").get("id");
				
				Long departmentId = 0L;
				
				Predicate orPredicate=null;
				if (customerId != null) {
					Predicate p1 = criteriaBuilder.equal(customerPath, customerId);
					list.add(p1);
				}
				if (outboundType != null) {
					Predicate p1 = criteriaBuilder.equal(outboundTypePath, outboundType);
					list.add(p1);
				}
				if (serialNo != null && serialNo.trim().length() > 0) {
					Predicate p2 = criteriaBuilder.like(serialNoPath, "%" + serialNo + "%");
					list.add(p2);
				}
				if (stockOutStatus != null) {
					//????????????????????????
					if(stockOutStatus==StockOutStatus.?????????&&paymentStatus==null){
						Predicate p2 = criteriaBuilder.isNull(paymentPath);
						list.add(p2);
					}
					Predicate p2 = criteriaBuilder.equal(stockOutStatusPath,stockOutStatus);
					list.add(p2);
				}
				if (paymentStatus != null && paymentStatus.length>0) {
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
				criteriaQuery.orderBy((criteriaBuilder.desc(outboundTimePath)));
				 
				return criteriaBuilder.and(list.toArray(p));
				 
			}
		};
		return outboundRegisterDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}

	@Transactional(readOnly = false)
	public void deleteItem(Long id) {
		 
		outboundRegisterItemDao.delete(id);
	}

	public OutboundRegisterItem findOneItem(Long id) {
		return outboundRegisterItemDao.findOne(id);
	}

	
	/**
	 * ?????????????????????
	 * 
	 * @param ids
	 *            ??????ID???......
	 * @param  
	 */
	@Transactional(readOnly = false)
	public void saveItem(String ids,OutboundRegister outboundRegister) {
		if(StringUtils.isBlank(ids)){
			throw  new RuntimeException("?????????????????????");
		}
		String[] strs = ids.trim().split(",");
		//???????????????
		Set<StockOut> stockOuts=outboundRegister.getStockOuts();
		if(stockOuts!=null&&!stockOuts.isEmpty()){
			 taskService.assignStockOutNotice(outboundRegister);
		}
		 
		StockOut stockOut = null;
		//Long customerId = outboundRegister.getCustomer().getId();
		for (String str : strs) {
			List<InboundReceiptItem> inboundReceiptItems =inboundReceiptItemDao.findByUndone(Long.valueOf(str));
			BookInventory bookInventory = bookInventoryDao.findByStoreContainer(Long.valueOf(str));
			if(inboundReceiptItems!=null&&!inboundReceiptItems.isEmpty()){
				InboundReceiptItem inboundReceiptItem = inboundReceiptItems.get(0);
				throw new RuntimeException("?????????"+bookInventory.getStoreLocationCode()+"??????????????????????????????????????????????????????"+inboundReceiptItem.getInboundRegister().getSerialNo()+"???????????????????????????");
			}
	        
			// ?????????????????????
			stockOut = new StockOut();
			stockOut.setSerialNo(serialNumberService.getSerialNumber(TransactionType.StockOut));
			stockOut.setOutboundRegister(outboundRegister);
			stockOut.setCustomer(outboundRegister.getCustomer());
			// stockOut.setOutboundRegisterItem(item);
			stockOut.createFromBookInventory(bookInventory);
			stockOut.setStockInTime(bookInventory.getStockInTime());
			
			/*// ???????????????B????????????????????????B???????????????????????????????????????????????????????????????????????????????????????+??????+??????+?????????B?????????
			//B???????????????????????????????????????+??????+??????+?????????B??????
			if(bookInventory.getStoreLocation().getStoreLocationType().getId().equals(Constants.STORE_LOCATION_TYPE_STORAGE_CAGE_ID) ||
					(bookInventory.getStoreLocation().getStoreLocationType().getId().toString().equals(Constants.STORE_LOCATION_TYPE_TURNOVER) && 
							bookInventory.getStoreLocation().getStoreArea().getCode().contains("LB"))
					//????????????????????????B?????????????????????
					// || 
					//(bookInventory.getStoreLocation().getStoreLocationType().getId().toString().equals(Constants.STORE_LOCATION_TYPE_TURNOVER) && 
					//				bookInventory.getStoreLocation().getStoreArea().getCode().contains("HB"))
					){
				//setStoreContainer?????????????????????????????????????????????????????????
				bookInventory.setStoreContainer(null);
				
			}*/
			
			//?????????
			Set<OutboundTarry> outboundTarr=outboundRegister.getOutboundTarrys();
			if(outboundTarr==null||outboundTarr.isEmpty()){
				throw new RuntimeException("???????????????????????????????????????????????????????????????");
			}
			TallyArea tallyArea=null;
			for (OutboundTarry outboundTarry : outboundTarr) {
				tallyArea=outboundTarry.getTallyArea();
			}
			stockOut.setTallyArea(tallyArea);
			stockOut.setStockOutStatus(StockOutStatus.?????????);
			 
			stockOutDao.save(stockOut);

		}

	}

	public List<OutboundRegisterItem> findOutboundRegisterItems(String serialNo) {
		return outboundRegisterItemDao.findOutboundRegisterItems(serialNo);
	}

	@Transactional(readOnly = false)
	public void settlement(OutboundRegister outboundRegister,  Long[] feeitem,
			String[] money, String[] actuallyMoneys,Long chargeTypeId, 
			boolean delayed,int[] storeContainerCount, Double[] weight,Double[] amountPiece, String[] ruleComment) {
		// ?????????
		Payment payment = new Payment();
		ChargeType chargeType = chargeTypeDao.findOne(chargeTypeId);
		payment.setChargeType(chargeType);
		payment.setCustomer(outboundRegister.getCustomer());
		if (delayed) {
			payment.setPaymentStatus(PaymentStatus.???????????????);
		} else {
			payment.setPaymentStatus(PaymentStatus.?????????);
		}
		payment.setPaymentType(PaymentType.????????????);
		payment.setSerialNo(serialNumberService
				.getSerialNumber(TransactionType.Payment));
		payment.setSettledBy(ThreadLocalHolder.getCurrentOperator());
		payment.setSettledTime(new Date());
		payment.setPaymentObjectEntity(OutboundRegister.class.getSimpleName());
		payment.setPaymentObjectId(outboundRegister.getId());
		payment.setPaymentObjectSerialNo(outboundRegister.getSerialNo());
		paymentDao.save(payment);
		outboundRegister.setPayment(payment);
		outboundRegisterDao.save(outboundRegister);

		for (int i = 0; i < feeitem.length; i++) {
			// ??????????????????
			PaymentItem paymentItem = new PaymentItem();
			CEFeeItem feeItem = new CEFeeItem();
			feeItem.setId(feeitem[i]);
			paymentItem.setFeeItem(feeItem);
			paymentItem.setAmount(new BigDecimal(money[i]));
			BigDecimal actuallyMoney=new BigDecimal(actuallyMoneys[i]);
			paymentItem.setActuallyAmount(actuallyMoney);
			paymentItem.setPayment(payment);
			paymentItem.setPaymentStatus(PaymentStatus.?????????);
			paymentItem.setPaymentObjectEntity(OutboundRegister.class.getSimpleName());
			paymentItem.setPaymentObjectId(outboundRegister.getId());
			paymentItem.setWeight((weight[i]));
			paymentItem.setAmountPiece((amountPiece[i]));
			paymentItem.setStoreContainerCount(storeContainerCount[i]);
			paymentItem.setRuleComment(ruleComment[i]);
			paymentItemDao.save(paymentItem);
			//??????????????????????????????????????????
			paymentService.saveExtraCharge(paymentItem,payment.getCustomer(),actuallyMoney);
		}
	}

	// ??????????????????

	public Page<OutboundRegister> findOutboundRegisterSpecification(final Long customerId, final Date startTime, final Date endTime,final StockOutStatus[] stockOutStatus, int page, int rows) {
		Specification<OutboundRegister> specification = new Specification<OutboundRegister>() {
			@Override
			public Predicate toPredicate(Root<OutboundRegister> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				Path outboundTime = root.get("outboundTime");
				Path customer = root.get("customer").get("id");
				Path stockOutStatusPath = root.get("stockOutStatus");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				List<Predicate> list = new ArrayList<Predicate>();
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
				Predicate	p0 = criteriaBuilder.between(outboundTime, startDate, endDate);
				list.add(p0);
				if (stockOutStatus != null) {
					Predicate p1 = stockOutStatusPath.in((Object[])stockOutStatus);
					list.add(p1);
				}
				if (customerId != null) {
					Predicate p1 = criteriaBuilder.equal(customer, customerId);
					list.add(p1);
				}
				

				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));

			}
		};
		return outboundRegisterDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}
	@Transactional(readOnly = false)
	public void updateStatus(Long id, StockOutStatus stockOutStatus) {
		if(StockOutStatus.?????????==stockOutStatus){	
			OutboundRegister outboundRegister =	outboundRegisterDao.findOne(id);
			if(outboundRegister.getOutboundCheckItems()!=null&&!outboundRegister.getOutboundCheckItems().isEmpty()){
				throw new RuntimeException("??????????????????????????????????????????????????????");
			}
			if(outboundRegister.getStockOuts()!=null&&!outboundRegister.getStockOuts().isEmpty()){
				for (StockOut stockOut : outboundRegister.getStockOuts()) {
					if(stockOut.getStockOutStatus()!=StockOutStatus.?????????){
						throw new RuntimeException("??????????????????????????????????????????????????????????????????????????????");
					}
				}
			}
			if(outboundRegister.getPayment()!=null){
				throw new RuntimeException("??????????????????????????????????????????");
			}
		}
		outboundRegisterDao.updateStatus(id, stockOutStatus);
	}
	
	//??????
		@Transactional(readOnly = false)
		public Map<String,Float>  payment(Long id,String aCardMac,String sMemberCode,String password){
			OutboundRegister outboundRegister=outboundRegisterDao.findOne(id);
			Map<String,Float> result = new HashMap<String,Float>();
			Payment payment=outboundRegister.getPayment();
			if(payment!=null){
				result = paymentService.payment(new Long[]{payment.getId()},aCardMac,sMemberCode,password);
			}
			return result;
		}
		//????????????
		@Transactional(readOnly = false)
		public void  delay(Long id,String remark,PaymentStatus paymentStatus){
			OutboundRegister outboundRegister=outboundRegisterDao.findOne(id);
			Payment payment=outboundRegister.getPayment();
			paymentService.delay(payment.getId(), remark, paymentStatus);
		}

		public boolean tallyareaStatus(Long id) {
			Set<OutboundTarry> outboundTarrys=outboundRegisterDao.findOne(id).getOutboundTarrys();
			boolean result=true;
			for (OutboundTarry outboundTarry : outboundTarrys) {
				if(!outboundTarry.isChecked()){
					result = outboundTarry.isChecked();
					break;
				}
			}
			return result;
		}
/*		//??????????????????????????????????????????
		public boolean validation(Long outboundBookingId,String[] productChecks){
			List<OutboundBookingItem>	outboundBookingItemList=outboundBookingItemDao.findByOutboundBookingItems(outboundBookingId);
			boolean result=false;
			for (OutboundBookingItem outboundBookingItem : outboundBookingItemList) {
				for (String  productCheck : productChecks) {
					String[] item=productCheck.split(":");
					if(outboundBookingItem.getId().equals(Long.valueOf(item[0]))){
						double  difference =	outboundBookingItem.getAmount()-Double.valueOf(item[1]);
						if(difference>0){
							 result=true;
						}
					}
				}
			}
			return result;
		}*/
	@Transactional(readOnly = false)	
	public synchronized void saveOutboundCheckItem(Long stockOutId, int amount,
			double weight, String containerCode, Long tallyAreaid, String readCode)
			throws Exception {
		if (amount <= 0 || StringUtils.isBlank(containerCode)) {
			throw  new RuntimeException("???????????????");
		}

		StoreContainer storeContainer = storeContainerService
				.findStoreContainerByLabel(containerCode);
		if (storeContainer == null) {
			throw  new RuntimeException("??????????????????");
		}

		// TODO ??????????????????????????????????????????

		OutboundCheckItem preCheckItem = outboundCheckService
				.getLastOutboundCheckItemByContainerCode(containerCode);
		if (preCheckItem != null
				&& !(preCheckItem.getOutboundRegister().getStockOutStatus()
						.equals(StockOutStatus.?????????) 
						|| 
						preCheckItem.getOutboundRegister().getStockOutStatus()
							.equals(StockOutStatus.?????????))) {
			throw  new RuntimeException(
					"???????????????????????? - ?????????: " + preCheckItem.getId());
		}

		TallyArea tallyArea = tallyAreaService.findOne(tallyAreaid);
		if (tallyArea == null) {
			throw  new RuntimeException("??????????????????");
		}
				
		OutboundCheckItem outboundCheckItem = new OutboundCheckItem();
		
		//???????????????
		if (readCode != null) {
			outboundCheckItem.setReadCode(readCode);
		}
				
		outboundCheckItem.setAmount(amount);
		outboundCheckItem.setWeight(weight);
		outboundCheckItem.setStoreContainer(storeContainer);
		outboundCheckItem.setTarryArea(tallyArea);
		StockOut stockOut = null;
		if (outboundCheckItem.isNew()) {
			stockOut = stockOutService.findOne(stockOutId);
			outboundCheckItem.setAmountMeasureUnit(stockOut
					.getAmountMeasureUnit());
			outboundCheckItem.setOutboundRegister(stockOut
					.getOutboundRegister());
			outboundCheckItem.setPacking(stockOut.getPacking());
			outboundCheckItem.setProduct(stockOut.getProduct());
			outboundCheckItem.setSpec(stockOut.getSpec());
			outboundCheckItem.setWeightMeasureUnit(stockOut
					.getWeightMeasureUnit());
			outboundCheckItem.setCheckOperator(ThreadLocalHolder
					.getCurrentOperator());
			outboundCheckItem.setStockInTime(stockOut.getStockInTime());
			outboundCheckItem.setCheckTime(new Date());
			outboundCheckItem.setSettledTime(stockOut.getSettledTime());
			outboundCheckItem.setStoreLocation(stockOut.getStoreLocation());
		}
		if (!stockOut.getStockOutStatus().equals(StockOutStatus.?????????)) {
			throw new Exception("????????????????????????");
		}
		if (StockOutStatus.?????????.compareTo(outboundCheckItem
				.getOutboundRegister().getStockOutStatus()) <= 0) {
			throw new Exception("??????????????????????????????????????????");
		}
		outboundCheckService.saveItem(outboundCheckItem);
	}
	
	@Transactional(readOnly = false)
	public Set<CalculatedResult> saveCalculateData (OutboundRegister outboundRegister) {
		List<Map> StorageChargesList = new ArrayList<Map>();
		List<Map> storageCharges = new ArrayList<Map>();
		Map<String, Object> storageChargesAndcalc = storeCalculator.calculate(outboundRegister);
		StorageChargesList = (List<Map>)storageChargesAndcalc.get("storageChargesList");
		for(OutboundCheckItem Item : outboundRegister.getOutboundCheckItems()){
			for (Map StorageCharge : StorageChargesList) {
				if (StorageCharge.get("outboundCheckItemId").toString().equals(Item.getId().toString())){
					//?????????????????????????????????
					Item.setStorageChargesAmount(new BigDecimal(StorageCharge.get("storageCharges").toString()));
					outboundCheckService.saveItem(Item);
					break;
				}
			}
		}
		
		Set<CalculatedResult> calculatedResults = (Set<CalculatedResult>)storageChargesAndcalc.get("calculatedResults");
		
		return calculatedResults;

	}

}
