package com.wwyl.service.finance;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Constants;
import com.wwyl.Enums.CommentType;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.PaymentType;
import com.wwyl.Enums.SystemConfigKey;
import com.wwyl.Enums.TransactionType;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.finance.AccountCheckingDao;
import com.wwyl.dao.finance.ExtraChargeConfigDao;
import com.wwyl.dao.finance.ExtraChargeDao;
import com.wwyl.dao.finance.PaymentDao;
import com.wwyl.dao.finance.PaymentItemDao;
import com.wwyl.dao.finance.PaymentLogDao;
import com.wwyl.dao.settings.ChargeTypeDao;
import com.wwyl.dao.settings.CommentDao;
import com.wwyl.dao.settings.SystemConfigDao;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.ce.CalculatedResult;
import com.wwyl.entity.finance.AccountChecking;
import com.wwyl.entity.finance.ExtraCharge;
import com.wwyl.entity.finance.ExtraChargeConfig;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.finance.PaymentItem;
import com.wwyl.entity.finance.PaymentLog;
import com.wwyl.entity.settings.ChargeType;
import com.wwyl.entity.settings.Comment;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.SystemConfig;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.service.ce.StoreCalculator;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.TaskService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.tenancy.StoreContractService;
import com.wwyl.service.ws.AuctionToCrmClient;
import com.wwyl.service.ws.CustomerInfo;

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class PaymentService {
 
	@Resource
	private PaymentDao paymentDao;
	@Resource
	private PaymentItemDao paymentItemDao;
	@Resource
	PaymentLogDao	paymentLogDao;
	@Resource
	CommentDao	commentDao;
	@Resource
	TaskService	taskService;
	@Resource
	CustomerService customerService;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private AccountCheckingDao accountCheckingDao;
	@Resource
	private ExtraChargeConfigDao  extraChargeConfigDao;
	@Resource
	private ExtraChargeDao  extraChargeDao;
	@Resource
	private AuctionToCrmClient auctionToCrmClient;
	
	@Resource
	private  StoreCalculator  storeCalculator;
	@Resource
	private  SystemConfigDao  systemConfigDao;
	@Resource
	private  ChargeTypeDao  chargeTypeDao;
	@Resource
	private  BookInventoryService  bookInventorySearvice;
	@Resource
	private  StoreContractService  storeContractService;
	
	
	public PaymentItem findOneItem(Long id){
		   return paymentItemDao.findOne(id);
	}
	public List<PaymentItem>  findPaymentItems(Long paymentId){
		List<PaymentItem>   paymentItems=paymentItemDao.findPaymentItems(paymentId);
		return  paymentItems;
	}
	public List<Payment>  findPaymentsBycustomerId(Long customerId,PaymentStatus paymentStatus ){
		 
		return paymentDao.findPaymentByCustomer(customerId,paymentStatus);
	}
	public Payment findOne(Long paymentId){
		Payment payment = paymentDao.findOne(paymentId);
		return  payment;
	}
	
	public Payment findPaymentByContractId(String classNameStr, Long contractId) {
		Payment payment = paymentDao.findPaymentByContractId(classNameStr, contractId);
		return payment;
	}
	
	public List<Payment> findPaymentByCustomerAndDateTime(Long customerId, String datetime, String datetimeAddOneDay) {
		List<Payment> paymentList = paymentDao.findPaymentByCustomerAndDateTime(customerId, datetime, datetimeAddOneDay);
		return paymentList;
	}
	@Transactional(readOnly = false)
	public void clearing(Payment payment){
		paymentDao.save(payment);
	}
	
	@Transactional(readOnly = false)
	public void save(Payment payment){
		paymentDao.save(payment);
	}	
	
	/**
	 * ???????????????
	 */	
	@Transactional(readOnly = false)
	public void cancel(Long paymentId){
		Payment payment = paymentDao.findOne(paymentId);
		if (payment == null) {
			throw new RuntimeException("?????????????????????????????????????????????????????????");			
		}
		if (payment.getPaymentStatus() == PaymentStatus.????????? || payment.getPaymentStatus() == PaymentStatus.?????????) {
			throw new RuntimeException("?????????????????????????????????????????????????????????");
		}
		payment.setPaymentStatus(PaymentStatus.?????????);
		paymentDao.save(payment);
	}
	
	/**
	 * ????????????
	 */	
	@Transactional(readOnly = false)
	public void CancelPayment(Long paymentId){
		Payment payment = paymentDao.findOne(paymentId);
	
		if (payment == null) {
			throw new RuntimeException("?????????????????????????????????????????????????????????");			
		}
		if (!payment.getPaymentStatus().equals(PaymentStatus.?????????)) {
			throw new RuntimeException("?????????????????????????????????????????????????????????????????????");
		}
		boolean isMinus=false;
		if(payment.getPaymentType().equals(PaymentType.????????????)||payment.getPaymentType().equals(PaymentType.????????????)){
			isMinus=true;
		}
		this.payment(payment,payment.getCustomer().getPassword(), isMinus);
		payment.setPaymentStatus(PaymentStatus.?????????);
		Set<PaymentItem> paymentItems=   payment.getPaymentItems();
		for (PaymentItem paymentItem : paymentItems) {
				paymentItem.setPaymentStatus(PaymentStatus.?????????);
				//paymentItemDao.save(paymentItem);
		} 
		paymentDao.save(payment);
	}
	
	/**
	 * ??????????????????
	 */
	@Transactional(readOnly = false)
	public void savePaymentItem(PaymentItem paymentItem){
		paymentItemDao.save(paymentItem);
	}
	
 
	@Transactional(readOnly = false)
	public void delPaymentItem(Long  id){
		paymentItemDao.delete(id);
	}
	/**
	 * ??????????????????
	 */
	@Transactional(readOnly = false)
	public void savePaymentLog(List<PaymentLog> paymentLogs){
		for (PaymentLog paymentLog : paymentLogs) {
			paymentLogDao.save(paymentLog);
		}
	}
	
	public Page<Payment> findPayments(int page, int rows, final String customerId, final String settledById, final PaymentStatus[] paymentStatus, final PaymentType paymentType) {
		Specification<Payment> specification = new Specification<Payment>() {
			@Override
			public Predicate toPredicate(Root<Payment> root,
					CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {

				Path customerPath = root.get("customer").get("id");		
				Path settledByPath = root.get("settledBy").get("id");						
				Path paymentStatusPath = root.get("paymentStatus");			
				Path paymentTypePath = root.get("paymentType");	
				Path paymentSerialNo = root.get("serialNo");
				Path psymentTime = root.get("settledTime");
				List<Predicate> list = new ArrayList<Predicate>();
				if (customerId != null && customerId.trim().length() > 0) {
					Predicate p1 = criteriaBuilder.equal(customerPath, customerId);
					list.add(p1);
				}		
				
				if (settledById != null && settledById.trim().length() > 0) {
					Predicate p4 = criteriaBuilder.equal(settledByPath, settledById);
					list.add(p4);
				}		
				
				if (paymentStatus != null&&paymentStatus.length>0) {
					Predicate p2 = paymentStatusPath.in((Object[])paymentStatus);
					list.add(p2);
				}	
			
				if (paymentType != null) {
					Predicate p3 = criteriaBuilder.equal(paymentTypePath, paymentType);
					list.add(p3);
				}
				
				Predicate[] p = new Predicate[list.size()];
				criteriaQuery.orderBy(criteriaBuilder.asc(paymentStatusPath), criteriaBuilder.desc(paymentTypePath), criteriaBuilder.desc(psymentTime), criteriaBuilder.desc(paymentSerialNo));
				return criteriaBuilder.and(list.toArray(p));				
			}
		};
		return paymentDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}	
	
	/**
	 * ??????????????????
	 * @param customerId
	 * @param startTime
	 * @param endTime
	 * @param paymentStatus
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Payment> findPaymentSpecification(final Long customerId,final Date startTime,final Date endTime,final Long paymentStatus,int page,int rows) {
		Specification<Payment> specification = new Specification<Payment>() {
			@Override
			public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				 
				Path customer=	root.get("customer").get("id");
				Path status=	root.get("paymentStatus");		
				Path paymentTypePath = root.get("paymentType");	
				Path paymentSerialNo = root.get("serialNo");
				Path psymentTime = root.get("settledTime");
				SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
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
				Predicate	p2 = criteriaBuilder.between(psymentTime, startDate,endDate);
				list.add(p2);

				if(customerId!=null){
					Predicate p1 = criteriaBuilder.equal(customer,customerId);
					list.add(p1);
				}

				if(paymentStatus!=null){
					Predicate p3 = criteriaBuilder.equal(status,PaymentStatus.?????????);
					list.add(p3);
				}
				
				Predicate[] p = new Predicate[list.size()];  
				criteriaQuery.orderBy(criteriaBuilder.asc(status),criteriaBuilder.desc(paymentTypePath),criteriaBuilder.desc(paymentSerialNo),criteriaBuilder.desc(psymentTime));
				return 	criteriaBuilder.and(list.toArray(p));		 
			}
		};
		return paymentDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}
	
	//??????
	/**
	 * 
	 * @param paymentIds 	    payment.id
	 * @param aCardMac			????????????
	 * @param sMemberCode		????????????
	 * @param password			??????
	 */
	@Transactional(readOnly = false)
	public Map<String,Float>  payment(Long[] paymentIds,String aCardMac, String sMemberCode,String password) {
		double totalAmount=0.00;
		Customer customer=null;
		if (Constants.USE_WS_INTERFACE_FOR_PAYMENT) {
			//???????????????????????????+
			customer = customerService.getCustomerInfo(aCardMac, sMemberCode, password, paymentDao.findOne(paymentIds[0]).getCustomer());
			customerService.save(customer);
		}else{
			customer = paymentDao.findOne(paymentIds[0]).getCustomer();
		}
		for (Long paymentId : paymentIds) {
			Payment payment = paymentDao.findOne(paymentId);
			if(customer!=null){
				payment.setCustomer(customer);
			}
			if (payment == null) {
				throw new RuntimeException("????????????????????????????????????");
			}
			if (payment.getPaymentStatus() == PaymentStatus.?????????
					|| payment.getPaymentStatus() == PaymentStatus.?????????
					|| payment.getPaymentStatus() == PaymentStatus.?????????) {
				throw new RuntimeException("??????????????????????????????????????????????????????");
			}
		
			boolean isMinus=false;
			if(PaymentType.????????????.equals(payment.getPaymentType())||PaymentType.????????????.equals(payment.getPaymentType())){
				isMinus=true;
			}
			
			totalAmount	=totalAmount+this.payment(payment,password,isMinus);
		}
		
		Map<String, Float>  result=new HashMap<String,Float>();
		result.put("totalAmount", (float)totalAmount);
		if (Constants.USE_WS_INTERFACE_FOR_PAYMENT) {
			CustomerInfo  info=auctionToCrmClient.getCustomer(customer.getaCardMac(),customer.getsMemberCode());
			result.put("balance", Float.parseFloat(info.getmBalance()));
		}
		return result;
	}
	//??????
	/**
	 * 
	 * @param payment
	 * @param passWord ??????
	 * @param isMinus  true???????????????????????????false????????????????????????
	 */
	@Transactional(readOnly = false)
	public double  payment(Payment payment,String passWord, boolean isMinus) {
		Set<PaymentItem> paymentItems = payment.getPaymentItems();
		String piTradeId = "";
		Double totalAmount =this.totalAmount(paymentItems, false,isMinus).doubleValue();//??????????????????,????????????
		 Customer customer = payment.getCustomer();
		 //String username=ThreadLocalHolder.getCurrentOperator()!=null?ThreadLocalHolder.getCurrentOperator().getUsername():null;
		if (Constants.USE_WS_INTERFACE_FOR_PAYMENT) {
			piTradeId = auctionToCrmClient.payCashCard(passWord, Integer.valueOf(payment.getId().toString()), 
					Integer.valueOf(customer.getCustomerOID()), customer.getaCardMac(),customer.getsMemberCode(), totalAmount,"");
		}
		if(PaymentType.????????????.equals(payment.getPaymentType()) || PaymentType.????????????.equals(payment.getPaymentType())){
			//??????????????????amount???????????????
			for (PaymentItem paymentItem : paymentItems) {
				paymentItem.setAmount(paymentItem.getAmount().negate());
				paymentItem.setActuallyAmount(paymentItem.getActuallyAmount().negate());
			}
			
			payment.setPaymentStatus(PaymentStatus.?????????);
		}else
	    	payment.setPaymentStatus(PaymentStatus.?????????);
		
		paymentDao.save(payment);
		 
		
		// ??????????????????
		PaymentLog log = new PaymentLog();
		Date payTime = new Date();
		log.setAmount(totalAmount(paymentItems, false,isMinus));
		log.setFromCustomer(payment.getCustomer());
		log.setPayer(ThreadLocalHolder.getCurrentOperator());
		log.setPayTime(payTime);
		log.setPiTradeId(piTradeId);
		log.setPayStatus(true);
		Set<PaymentItem>  set=new LinkedHashSet<PaymentItem>();
		set.addAll(paymentItems);
		log.setPaymentItems(set);
		paymentLogDao.save(log);
		for (PaymentItem paymentItem : paymentItems) {
			paymentItem.setPaymentObjectEntity(payment.getPaymentObjectEntity());
			paymentItem.setPaymentObjectId(payment.getPaymentObjectId());
			paymentItem.setPayTime(payTime);
			if(PaymentType.????????????.equals(payment.getPaymentType()) || PaymentType.????????????.equals(payment.getPaymentType())){
				paymentItem.setPaymentStatus(PaymentStatus.?????????);
			}else
			{
				paymentItem.setPaymentStatus(PaymentStatus.?????????);
			}
			//paymentItem.setPaymentLog(log);
			paymentItemDao.save(paymentItem);
		}
		return totalAmount;
	}
	/**
	 * ??????????????????
	 */
	//??????????????????
	@Transactional(readOnly = false)
	public void  delay(Long id,String remark,PaymentStatus paymentStatus){
		Payment payment=paymentDao.findOne(id);
		if (payment != null) {
			if (payment.getPaymentStatus() != PaymentStatus.??????????????? ) {
				throw new RuntimeException("?????????????????????????????????????????????????????????????????????");
			}
			
		payment.setPaymentStatus(paymentStatus);
		payment.setDelayApprover(ThreadLocalHolder.getCurrentOperator());
		Comment comment = new Comment();
		comment.setCommentor(ThreadLocalHolder.getCurrentOperator());
		comment.setCommentTime(new Date());
		comment.setContent(remark);
		comment.setCommentType(CommentType.????????????);
		payment.getComments().add(comment);
		commentDao.save(comment);
		paymentDao.save(payment);
		paymentItemDao.updateStatus(paymentStatus, id);
		taskService.assignDelayedCheckNotice(payment);
		}
	}
	/**
	 * ??????????????????
	 * @param serialNo
	 * @param temporary
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<Payment> findBySerialNoLike(String serialNo,int page,int rows) {
		return 	paymentDao.findBySerialNoLike(serialNo,1, RepoUtils.buildPageRequest(page, rows));
	}
	
	public List<PaymentItem> findByfeeItemIdAndcustomerId(Long feeItemId,Long customerId){
		return paymentItemDao.findByfeeItemIdAndcustomerId(feeItemId, customerId,PaymentStatus.?????????);
	}
	
	@Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW)
	public void createAccountChking(Customer customer){
		 
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH),
				customer.getPayDate(), 0, 0, 0);
		Date payTime = calendar.getTime();

		calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH),
				customer.getBillDate(), 0, 0, 0);
		Date endTime = calendar.getTime();
		
		calendar.add(calendar.MONTH, -1);
		Date startTime = calendar.getTime();

	
		List<Payment> payments = paymentDao.findPayMonthlyPayments(PaymentStatus.?????????, customer.getId(),
				Constants.CHARGETYPE_MONTH, startTime, endTime);
		if (CollectionUtils.isNotEmpty(payments)) {
			AccountChecking accountChecking = new AccountChecking();
			accountChecking.setSerialNo(serialNumberService
					.getSerialNumber(TransactionType.AccountChecking));
			accountChecking.setCustomer(customer);
			accountChecking.setCheckingTime(new Date());
			accountChecking.setStartTime(startTime);
			accountChecking.setEndTime(endTime);
			accountChecking.setPayTime(payTime);
			accountChecking.setPaymentStatus(PaymentStatus.?????????);
			SystemConfig  sysconfig=systemConfigDao.findByAttribute(SystemConfigKey.?????????????????????);
			if(StringUtils.isNotBlank(sysconfig.getValue())&&sysconfig.getValue().equals("1")){
				accountChecking.setOverdueFine(true);
			}else{
				accountChecking.setOverdueFine(false);
			}
			accountCheckingDao.save(accountChecking);
			for (Payment payment : payments) {
				payment.setAccountChecking(accountChecking);
				paymentDao.save(payment);
			}
		}
	}
	public Page<AccountChecking> findAccountCheckings(int page, int rows,Long customerId,PaymentStatus paymentStatus){
		Page<AccountChecking>  accountCheckings =null;
		if(customerId!=null && paymentStatus != null)
		{
			accountCheckings=accountCheckingDao.findAccountCheckingsByCustomerAndPaymentStatus(customerId, paymentStatus, RepoUtils.buildPageRequest(page, rows));			
		}
		else  if(paymentStatus != null){
			accountCheckings=accountCheckingDao.findAccountCheckingsByPaymentStatus(paymentStatus, RepoUtils.buildPageRequest(page, rows));			
			
		}
		else if(customerId!=null){
			accountCheckings=accountCheckingDao.findAccountCheckingsByCustomer(customerId,RepoUtils.buildPageRequest(page, rows));
		}else{
			accountCheckings=accountCheckingDao.findAccountCheckings(RepoUtils.buildPageRequest(page, rows));
		}
		return accountCheckings; 
	}
 
	public AccountChecking findOneAccountChecking(Long accountCheckingId){
		return accountCheckingDao.findOne(accountCheckingId);
	}
	@Transactional(readOnly = false)
	public AccountChecking saveAccountChecking(AccountChecking AccountChecking){
		return accountCheckingDao.save(AccountChecking);
	}
	/**
	 * 
	 */
	public  void saveExtraCharge(PaymentItem paymentItem,Customer customer,BigDecimal actuallyMoney){
		//??????????????????????????????????????????
		List<ExtraChargeConfig> extraChargeConfigs=extraChargeConfigDao.findExtraChargeConfigs(paymentItem.getFeeItem().getId());
		if(!extraChargeConfigs.isEmpty()){
			ExtraChargeConfig extraChargeConfig=extraChargeConfigs.get(0);
			ExtraCharge extraCharge=new ExtraCharge();
			extraCharge.setFeeItem(paymentItem.getFeeItem());
			BigDecimal discount=actuallyMoney.multiply(new BigDecimal(extraChargeConfig.getProportion()/100));
			extraCharge.setActuallyAmount(discount);
			extraCharge.setAmount(actuallyMoney);
			extraCharge.setCustomer(customer);
			extraCharge.setPaymentItem(paymentItem);
			extraCharge.setSerialNo(serialNumberService.getSerialNumber(TransactionType.ExtraCharge));
			extraCharge.setTime(new Date());
			extraChargeDao.save(extraCharge);
		}
	}
	
	/**
	 * ?????????????????????
	 * @param customerId
	 * @param serialNo
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<ExtraCharge> findExtraCharges(final Long customerId,final String  serialNo,int page,int rows) {
		Specification<ExtraCharge> specification = new Specification<ExtraCharge>() {
			@Override
			public Predicate toPredicate(Root<ExtraCharge> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				Path serialNoPath=	root.get("serialNo");
				Path customer=	root.get("customer").get("id");
				 
				List<Predicate> list = new ArrayList<Predicate>();  
				if(customerId!=null){
					Predicate p1 = criteriaBuilder.equal(customer,customerId);
					list.add(p1);
				}
				 
				if(StringUtils.isNotBlank(serialNo)){
					Predicate p2 = criteriaBuilder.like(serialNoPath, "%" + serialNo + "%");
					list.add(p2);
				}
				Predicate[] p = new Predicate[list.size()];  
				return 	criteriaBuilder.and(list.toArray(p));
		 
			}
		};
		return extraChargeDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}
	/**
	 * ????????????????????????
	 * @param customerId
	 * @return
	 */
	public  BigDecimal  countGuaranteeMoney(Long customerId){
		List<PaymentItem>	 paymentItems=findByfeeItemIdAndcustomerId(Constants.CE_FEE_ITEM_GUARANTEEMONEY,customerId);
		BigDecimal	bigDecimaltotalMoney=new BigDecimal("0");
		for (PaymentItem paymentItem : paymentItems) {
		 
			if(paymentItem.getPayment().getPaymentType()==PaymentType.????????????||paymentItem.getPayment().getPaymentType()==PaymentType.????????????){
				bigDecimaltotalMoney=	bigDecimaltotalMoney.subtract(paymentItem.getActuallyAmount());
			}else{
				bigDecimaltotalMoney=bigDecimaltotalMoney.add(paymentItem.getActuallyAmount());
				 
			}
		}
		return bigDecimaltotalMoney;
	}
	/**
	 * ???????????????????????????????????????
	 * @param paymentItems  ?????????
	 * @param conversion   ?????????????????? ?????????true ??????
	 * @param isMinus ???????????? ???true??????
	 * @return ?????????
	 */
	public  BigDecimal totalAmount(Set<PaymentItem> paymentItems,boolean conversion,boolean isMinus){
		BigDecimal	totalMoney=new BigDecimal("0");// 
		if(CollectionUtils.isNotEmpty(paymentItems)){
			for (PaymentItem paymentItem : paymentItems) {
				totalMoney=totalMoney.add(paymentItem.getActuallyAmount());
			}
			if(isMinus){
				totalMoney=totalMoney.multiply(new BigDecimal("-1"));
			}
			if(conversion){
				totalMoney=totalMoney.multiply(new BigDecimal("100")).setScale(0,BigDecimal.ROUND_HALF_UP);
			}
		}
		return  totalMoney;
	}
	/**
	 * ??????????????????
	 */
	 
	@Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW)
	public void  monthlyStatement(Customer customer){
		Set<CalculatedResult> calculatedResults =storeCalculator.CalculateMonthKnot(customer.getId());
		List<BookInventory> bookInventorylist = bookInventorySearvice.findBookInventoryByCustomerId(customer.getId());
		if (CollectionUtils.isNotEmpty(calculatedResults)) {
			for (BookInventory bookInventory : bookInventorylist) {
				bookInventory.setSettledTime(new Date());
			}
			Payment payment = new Payment();
			ChargeType chargeType = new ChargeType();
			chargeType.setId(Constants.CHARGETYPE_MONTH);
			payment.setChargeType(chargeType);
			payment.setCustomer(customer);
			payment.setPaymentStatus(PaymentStatus.?????????);
			payment.setPaymentType(PaymentType.????????????);
			if ( ThreadLocalHolder.getCurrentOperator() != null ){
			 payment.setSettledBy(ThreadLocalHolder.getCurrentOperator());
			}
			payment.setSerialNo(serialNumberService
					.getSerialNumber(TransactionType.Payment));
			payment.setSettledTime(new Date());

			paymentDao.save(payment);
			for (CalculatedResult calculatedResult : calculatedResults) {
				PaymentItem paymentItem = new PaymentItem();
				CEFeeItem feeItem = new CEFeeItem();
				feeItem.setId(calculatedResult.getFeeItem().getId());
				paymentItem.setFeeItem(feeItem);
				paymentItem.setAmount(calculatedResult.getAmount());
				paymentItem.setPayment(payment);
				paymentItem.setPaymentStatus(PaymentStatus.?????????);
				paymentItem.setActuallyAmount(calculatedResult
						.getDiscountedAmount());
				paymentItemDao.save(paymentItem);
			}

		}
	}
	/**
	 * ??????????????????????????????????????????????????????????????????????????????????????????
	 */
	@Transactional(readOnly = false)
	public void accountChecking(){
		//SimpleDateFormat  format=	new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		//System.out.println("????????????"+format.format(new Date()));
		List<Customer> customers= customerService.findMonthlyAll();
		Calendar nowDate = new GregorianCalendar();
		int day= nowDate.get(nowDate.DAY_OF_MONTH);
		Date  nowTime=nowDate.getTime();
		for (Customer customer : customers) {
			if(day==customer.getBillDate()){
				//??????????????????????????????
				monthlyStatement(customer);
				storeContractService.carryoverContractByCustomer(customer,new Date());
				createAccountChking(customer);
			}else if(day==(customer.getBillDate()+1)){
				nowDate.add(Calendar.DAY_OF_MONTH, -2);
				Date  startTime=nowDate.getTime();
				List<AccountChecking> accountCheckings = accountCheckingDao.findBycheckingTime(customer.getId(),startTime,nowTime);
				if(CollectionUtils.isEmpty(accountCheckings)){
					createAccountChking(customer);
				}
			}
			
		}
	}
	/**
	 * ???????????????
	 * @param accountChecking
	 */
	@Transactional(readOnly = false)
	public void payAccountChecking(AccountChecking accountChecking){
		if(accountChecking.isOverdueFine()){
				//???????????????
				overdueFine(accountChecking);
				//????????????????????????
				overdueFinePayment(accountChecking);
		}
		Set<Payment> payments=  accountChecking.getPayments();
		if(CollectionUtils.isNotEmpty(payments)){
			for (Payment payment : payments) {
				if(payment.getPaymentStatus()==PaymentStatus.?????????){
					this.payment(payment,accountChecking.getCustomer().getPassword(), false);
				}
				
			}
		}
		accountChecking.setPaymentStatus(PaymentStatus.?????????);
		accountChecking.setActualPayTime(new Date());
		accountCheckingDao.save(accountChecking);
	}
	
	/**
	 * ???????????????
	 * @param accountCheckingComplete
	 */
	@Transactional(readOnly = false)
	public void accountCheckingComplete(AccountChecking accountChecking){
		Set<Payment> payments=  accountChecking.getPayments();
		if(CollectionUtils.isNotEmpty(payments)){
			for (Payment payment : payments) {
				for(PaymentItem paymentItem : payment.getPaymentItems()){
					paymentItem.setPaymentStatus(PaymentStatus.?????????);
					paymentItem.setRemark("????????????????????????-??????????????????");
					paymentItemDao.save(paymentItem);
				}
					payment.setPaymentStatus(PaymentStatus.?????????);
					payment.setRemark("????????????????????????-??????????????????");
					paymentDao.save(payment);
			}
		}
		accountChecking.setPaymentStatus(PaymentStatus.?????????);
		accountChecking.setActualPayTime(new Date());
		accountCheckingDao.save(accountChecking);
	}
	
	/**
	 * ?????????????????????????????????
	 */
	@Transactional(readOnly = false)
	public void payMonthly(){
		//SimpleDateFormat  format=	new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//System.out.println("????????????"+format.format(new Date()));
		List<Customer> customers= customerService.findMonthlyAll();
		Calendar nowDate=Calendar.getInstance(); 
		int day= nowDate.get(nowDate.DAY_OF_MONTH);
		for (Customer customer : customers) {
			if(day==customer.getPayDate()){
				List<AccountChecking> accountCheckings = accountCheckingDao.findByPaymentStatus(PaymentStatus.?????????, customer.getId());
				if(CollectionUtils.isNotEmpty(accountCheckings)){
					for (AccountChecking accountChecking : accountCheckings) {
						this.payAccountChecking(accountChecking);
					}
				}
			}
	
		}
	}
	/**
	 * ???????????????
	 * @param accountChecking
	 */
	@Transactional(readOnly = false)
	public  void   overdueFine(AccountChecking accountChecking ){
		// ???????????????
		if (accountChecking != null) {

				Set<Payment> payments=  accountChecking.getPayments();
				Calendar calendar = new GregorianCalendar();
				Date currentTime = calendar.getTime();
				calendar.setTime(accountChecking.getPayTime());
				calendar.set(calendar.get(calendar.YEAR),
						calendar.get(calendar.MONTH),
						calendar.get(calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
				Date endPayTime = calendar.getTime();
				if (currentTime.getTime() >= endPayTime.getTime()) {
					BigDecimal total = new BigDecimal("0");// ???????????????
					int between_days = 1;
					for (Date d = endPayTime; !DateUtils.isSameDay(currentTime,
							d); d = DateUtils.addDays(d, 1)) {
						between_days++;
					}
					 
					SystemConfig sysconfig = systemConfigDao
							.findByAttribute(SystemConfigKey.???????????????);
					Set<PaymentItem> paymentItems = null;
					for (Payment payment : payments) {
						paymentItems = payment.getPaymentItems();
						for (PaymentItem paymentItem : paymentItems) {
							total = total.add(paymentItem.getActuallyAmount());
						}
					}
					if (StringUtils.isNotBlank(sysconfig.getValue())) {
						BigDecimal totalMoney = total.multiply(new BigDecimal(String.valueOf(between_days)))
								.multiply(new BigDecimal(sysconfig.getValue()))
								.setScale(2, BigDecimal.ROUND_HALF_UP);
						accountChecking.setOverdueMoney(totalMoney);
						accountChecking.setOverdueTime(new Date());
						accountCheckingDao.save(accountChecking);
					}

				}
			 
			}
		 
	}
	 /**
	  * ????????????????????????
	  * @param accountChecking
	  */
	@Transactional(readOnly = false)
	public  void  overdueFinePayment(AccountChecking accountChecking){
		if (accountChecking != null&&accountChecking.getOverdueMoney()!=null) {
		Set<Payment> payments=  accountChecking.getPayments();
						BigDecimal totalMoney = accountChecking.getOverdueMoney();
						Payment payment = new Payment();
						payment.setSerialNo(serialNumberService
								.getSerialNumber(TransactionType.Payment));
						payment.setTemporary(1);
						payment.setCustomer(accountChecking.getCustomer());
						payment.setPaymentType(PaymentType.????????????);
						payment.setPaymentStatus(PaymentStatus.?????????);
						if ( ThreadLocalHolder.getCurrentOperator() != null ){
							 payment.setSettledBy(ThreadLocalHolder.getCurrentOperator());
							}
						payment.setSettledTime(new Date());
						ChargeType chargeType = chargeTypeDao
								.findOne(Constants.CHARGETYPE_MONTH);
						payment.setChargeType(chargeType);
						payment.setAccountChecking(accountChecking);
						payment.setPaymentObjectEntity("?????????");
						paymentDao.save(payment);
						PaymentItem paymentItemNew = new PaymentItem();
						CEFeeItem feeItem = new CEFeeItem();
						feeItem.setId(Constants.CE_FEE_ITEM_Overdue);
						paymentItemNew.setPaymentStatus(PaymentStatus.?????????);
						paymentItemNew.setActuallyAmount(totalMoney);
						paymentItemNew.setAmount(totalMoney);
						paymentItemNew.setPayment(payment);
						paymentItemNew.setFeeItem(feeItem);
						paymentItemNew.setPaymentObjectEntity("?????????");
						paymentItemDao.save(paymentItemNew);
						Set<PaymentItem> item = new HashSet<PaymentItem>();
						item.add(paymentItemNew);
						payment.setPaymentItems(item);
						payments.add(payment);
			 
			}
		 
	}

	/**
	 * 
	 * @param period ???????????????????????????????????????	      
	 * @return
	*/ 
	@Transactional(readOnly = false)
	public void accountCheckingWarning(int period){
		List<AccountChecking> accountCheckings=accountCheckingDao.findAccountCheckingsByStatus(PaymentStatus.?????????);
		Date Timepoint = null; 	
		 //	?????????????????? - ???????????????????????????????????? = ??????????????????
		 //	(????????? + ????????????) >= ??????????????????  ??????assignAccountCheckingPeriodNotice
		
			Date currentTime = new Date();
			
			for (AccountChecking accountChecking : accountCheckings) {
				
				Date endPayTime = accountChecking.getPayTime();
				
				if(endPayTime==null){
					continue;
				}
				Timepoint = DateUtils.addDays(currentTime,period);
				     
				if ( DateUtils.isSameDay(endPayTime, Timepoint)){
					taskService.assignAccountCheckingPeriodNotice(accountChecking);
				}
			}
	 }	
	
	/**
	 * ?????????????????????
	 */
	@Transactional(readOnly = false)
	public  void  accountCheckingExpireWarning(){
		/*SimpleDateFormat  format=	new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("**********?????????????????????**************"+format.format(new Date()));*/
		SystemConfig  sysconfig=systemConfigDao.findByAttribute(SystemConfigKey.?????????????????????????????????);
		if(StringUtils.isNotBlank(sysconfig.getValue())){
			accountCheckingWarning(Integer.valueOf(sysconfig.getValue()));
		}
	}
	 
}
