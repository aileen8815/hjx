package com.wwyl.service.tenancy;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Constants;
import com.wwyl.Enums.ContractStatus;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.PaymentType;
import com.wwyl.Enums.StoreAreaRentStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.finance.PaymentDao;
import com.wwyl.dao.settings.StoreAreaDao;
import com.wwyl.dao.tenancy.StoreContractDao;
import com.wwyl.dao.tenancy.StoreContractFeeItemDao;
import com.wwyl.dao.tenancy.StoreContractLocationItemDao;
import com.wwyl.dao.tenancy.StoreContractPolicyItemDao;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.finance.PaymentItem;
import com.wwyl.entity.settings.ChargeType;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.tenancy.StoreContract;
import com.wwyl.entity.tenancy.StoreContractFeeItem;
import com.wwyl.entity.tenancy.StoreContractLocationItem;
import com.wwyl.entity.tenancy.StoreContractPolicyItem;
import com.wwyl.service.ce.CEFeeItemService;
import com.wwyl.service.finance.PaymentService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreAreaService;

/**
 * @author sjwang
 */

@Service
@Transactional(readOnly = true)
public class StoreContractService {
	@Resource
	StoreContractDao storeContractDao;
	@Resource	
	StoreContractLocationItemDao storeContractLocationItemDao;
	@Resource	
	StoreContractFeeItemDao storeContractFeeItemDao;
	@Resource
	StoreContractPolicyItemDao storeContractPolicyItemDao;
	@Resource
	SerialNumberService serialNumberService;		
	@Resource
	PaymentDao paymentDao;
	@Resource
	PaymentService paymentService;
	@Resource
	CEFeeItemService ceFeeItemService;
	@Resource
	StoreAreaService storeAreaService;
	
	private static Logger logger = LoggerFactory.getLogger(StoreContractService.class);	
	
	@Transactional(readOnly = false)	
	public void saveLocationItem(StoreContractLocationItem contractLocationItem) {
		StoreContract contract = contractLocationItem.getStoreContract();
		if(isRentable(contractLocationItem.getStoreLocation(), contract.getStartDate(), contract.getEndDate())) {
			storeContractLocationItemDao.save(contractLocationItem);
		} else {
			throw new RuntimeException("???????????????????????????");
		}
	}
	
	private boolean isRentable(StoreLocation storeLocation, Date startDate, Date endDate) {
		List<StoreContract> rented = storeContractLocationItemDao.find(startDate, endDate, storeLocation.getId());
		if (rented != null && rented.size()==0) {
			return true;
		} else {
			return false;
		}
	}

	public Page<StoreContract> findStoreContractBySerialNoLike(int page,
			int rows, final String customerId, final ContractStatus status, final String storeAreaId) {
		Specification<StoreContract> specification = new Specification<StoreContract>() {
			@Override
			public Predicate toPredicate(Root<StoreContract> root,
					CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				Path customer = root.get("customer").get("id");
				Path statusPath = root.get("status");
				Path storeAreaPath = root.get("storeArea").get("id");
				List<Predicate> list = new ArrayList<Predicate>();
				if (customerId != null && customerId.trim().length() > 0) {
					Predicate p1 = criteriaBuilder.equal(customer, customerId);
					list.add(p1);
				}
				if (status != null) {
					Predicate p2 = criteriaBuilder.equal(statusPath, status);
					list.add(p2);

				}
				if (storeAreaId != null && storeAreaId.trim().length() > 0) {
					Predicate p3 = criteriaBuilder.equal(storeAreaPath, storeAreaId);
					list.add(p3);

				}

				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));
			}
		};
		return storeContractDao.findAll(specification,
				RepoUtils.buildPageRequest(page, rows));
	}
 
	public StoreContract findOne(Long id) {
		return storeContractDao.findOne(id);
	}	
	
	public StoreContract findBySerialNo(String serialNo) {
		return storeContractDao.findBySerialNo(serialNo);
	}
	
	@SuppressWarnings("deprecation")
	@Transactional(readOnly = false)
	public void save(StoreContract storeContract) {
		if (storeContract.getStatus() != ContractStatus.?????????) {			
			throw new RuntimeException("?????????????????????????????????????????????");					
		}
		
		if ( storeContract.getStartDate().getTime() > storeContract.getEndDate().getTime() ) {
			throw new RuntimeException("??????????????????????????????????????????????????????");				
		}
		
		if ( storeContract.getChargeDate().getTime() > storeContract.getEndDate().getTime() ) {
			throw new RuntimeException("????????????????????????????????????????????????????????????");				
		}
		if ( storeContract.getStartDate().getTime() > storeContract.getChargeDate().getTime() ) {
			throw new RuntimeException("????????????????????????????????????????????????????????????");				
		}			
		storeContractDao.save(storeContract);		
	}	
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		StoreContract storeContract = storeContractDao.findOne(id);
		if (storeContract != null) {
			if (storeContract.getStatus() == ContractStatus.?????????) {
				storeContractDao.delete(id);
			} else {
				throw new RuntimeException("?????????????????????????????????????????????????????????");				
			}
		} else {
			throw new RuntimeException("?????????????????????");		
		}
	}	
	
	public StoreContractFeeItem findOneFeeItem(Long id) {
		return 	storeContractFeeItemDao.findOne(id);
	}	
	
	public StoreContractPolicyItem findOnePolicyItem(Long id) {
		return 	storeContractPolicyItemDao.findOne(id);
	}		
	
	public  List<StoreContractFeeItem> findStoreContractFeeItems(String serialNo) {
		return storeContractFeeItemDao.findStoreContractFeeItems(serialNo);
	}
	
	public  List<StoreContractPolicyItem> findStoreContractPolicyItems(String serialNo ) {
		return storeContractPolicyItemDao.findStoreContractPolicyItems(serialNo);
	}	
	
	public List<StoreContract> findValidContractByCustomer(Long customerId) {
		return storeContractDao.findValidContractByCustomer(customerId);
	}
	
	public List<StoreContract> findEvictionContractByCustomer(Long customerId) {
		return storeContractDao.findEvictionContractByCustomer(customerId);
	}	
	
	public StoreContract findValidContractByStoreArea(Long storeAreaId) {
		return storeContractDao.findValidContractByStoreArea(storeAreaId);
	}
	
	public List<StoreContract> findValidContractByDateTimeAndCustomerId(Date feeDate, Long customerId) {
		return storeContractDao.findValidContractByDateTimeAndCustomerId(feeDate,customerId);
	}
	
	public boolean exists(String contractNo) {
		return CollectionUtils.isNotEmpty(this.storeContractDao.findValidContractByContractNo(contractNo));
	}
	
	@Transactional(readOnly = false)
	public void saveFeeItem(StoreContractFeeItem storeContractFeeItem) {
		
		StoreContract storeContract = storeContractDao.findOne(storeContractFeeItem.getStoreContract().getId());

		if (storeContract.getStatus() == ContractStatus.?????????) {
			storeContractFeeItemDao.save(storeContractFeeItem);
		} else {
			throw new RuntimeException("??????????????????????????????????????????????????????");
		}
	}			
	
	@Transactional(readOnly = false)
	public void savePolicyItem(StoreContractPolicyItem storeContractPolicyItem) {
		
		StoreContract storeContract = storeContractDao.findOne(storeContractPolicyItem.getStoreContract().getId());		
		System.out.println(storeContract.getStatus());
		if (storeContract.getStatus() == ContractStatus.?????????) {
			storeContractPolicyItemDao.save(storeContractPolicyItem);
		} else {
			throw new RuntimeException("??????????????????????????????????????????????????????");
		}
	}		
	
	@Transactional(readOnly = false)
	public void deleteFeeItem(Long id) {
		StoreContract storeContract = storeContractFeeItemDao.findOne(id).getStoreContract();
		if (storeContract != null) {
			if (storeContract.getStatus() == ContractStatus.?????????) {
				storeContractFeeItemDao.delete(id);
			} else {
				throw new RuntimeException("?????????????????????????????????????????????????????????");				
			}
		} else {
			throw new RuntimeException("?????????????????????????????????");		
		}
	}	
	
	@Transactional(readOnly = false)
	public void deletePolicyItem(Long id) {
		StoreContract storeContract = storeContractPolicyItemDao.findOne(id).getStoreContract();
		if (storeContract != null) {
			if (storeContract.getStatus() == ContractStatus.?????????) {
				storeContractPolicyItemDao.delete(id);
			} else {
				throw new RuntimeException("?????????????????????????????????????????????????????????");				
			}
		} else {
			throw new RuntimeException("?????????????????????????????????");		
		}
	}		
	
	public StoreContractLocationItem findOneLocationItem(Long id) {
		return 	storeContractLocationItemDao.findOne(id);
	}		
	
	public  List<StoreContractLocationItem> findStoreContractLocationItems(String serialNo ) {
		return storeContractLocationItemDao.findStoreContractLocationItems(serialNo);
	}	
	
	@Transactional(readOnly = false)
	public void deleteLocationItem(Long id) {
		StoreContract storeContract = storeContractLocationItemDao.findOne(id).getStoreContract();
		if (storeContract != null) {
			if (storeContract.getStatus() == ContractStatus.?????????) {
				storeContractLocationItemDao.delete(id);
			} else {
				throw new RuntimeException("???????????????????????????????????????????????????");					
			}
		} else {
			throw new RuntimeException("???????????????????????????");			
		}
	}
	
	//???????????????
	private void generatePayment(StoreContract storeContract, String settleRange) throws ParseException{
		
		List<Payment> paymentList = paymentDao.findBySettleRangeAndPaymentObject(settleRange, storeContract.getClass().getSimpleName(), storeContract.getId());
		if (paymentList.size() > 0) {
			//throw new RuntimeException("??????????????????????????????????????????????????????");
			logger.error("??????????????????????????????????????????????????????");	
		}else
		{
			long between_days = 0;
			String sdf="";
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");  
			Payment payment = new Payment();
			payment.setSerialNo(serialNumberService.getSerialNumber(TransactionType.Payment));
			payment.setCustomer(storeContract.getCustomer());
			payment.setSettledTime(new Date());
			payment.setPaymentStatus(PaymentStatus.?????????);
			payment.setPaymentObjectEntity(storeContract.getClass().getSimpleName());
			payment.setPaymentObjectId(storeContract.getId());
			payment.setPaymentObjectSerialNo(storeContract.getSerialNo());
			payment.setSettleRange(settleRange);
			payment.setPaymentType(PaymentType.????????????);
			payment.setChargeType(storeContract.getCustomer().getChargeType());
	        payment.setSettledBy(ThreadLocalHolder.getCurrentOperator());
			paymentService.save(payment);
			
			for (StoreContractFeeItem feeItem : storeContract.getContractFeeItems()) {
				PaymentItem paymentItem = new PaymentItem();
				paymentItem.setPayment(payment);
				paymentItem.setFeeItem(feeItem.getFeeItem());
				//??????3???????????????????????????
				if(feeItem.getFeeItem().getId() == Constants.CE_FEE_ITEM_StorageCharges){
					//???????????????????????????????????????????????????????????????????????????????????????????????????????????????
					if(feeItem.getEhargeEndDate().after(feeItem.getStoreContract().getEndDate()))
					{
						//???0
						paymentItem.setAmount(new BigDecimal(0));
						paymentItem.setActuallyAmount(paymentItem.getAmount());
					}else
					{
						DecimalFormat df = new DecimalFormat("0.00");
						//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
						if(storeContract.getEndDate().before(new Date())){
							sdf= dateFormat.format(storeContract.getEndDate());
						}else
						{
							sdf= dateFormat.format(new Date());
						}
						
						between_days=calcfeeDate(feeItem.getEhargeEndDate(),sdf);
						//???????????????????????????*?????????
						paymentItem.setAmount(new BigDecimal(df.format(feeItem.getAmount()*between_days)));
						paymentItem.setActuallyAmount(paymentItem.getAmount());
					}
					
				}else
				{
					paymentItem.setAmount(new BigDecimal(feeItem.getAmount()));
					paymentItem.setActuallyAmount(paymentItem.getAmount());
				}
				paymentItem.setPaymentObjectEntity(feeItem.getClass().getSimpleName());
				paymentItem.setPaymentObjectId(feeItem.getId());
				paymentService.savePaymentItem(paymentItem);
				
				//??????????????????????????????
				feeItem.setEhargeEndDate(new Date());
				storeContractFeeItemDao.save(feeItem);
			}
		}
	}
	
	//???????????????????????????
	private Long calcfeeDate(Date startDate,String endDate) throws ParseException{
		//??????????????????
    	SimpleDateFormat datecale=new SimpleDateFormat("yyyy-MM-dd");  
    	Calendar cal = Calendar.getInstance();    
    	cal.setTime(datecale.parse(startDate.toString()));
    	long time1 = cal.getTimeInMillis();
    	cal.setTime(datecale.parse(endDate));
    	long time2 = cal.getTimeInMillis();         
    	//??????????????????????????????+1???
    	long between_days=(time2-time1)/(1000*3600*24) + 1; 
    	
    	if(between_days<0){
    		between_days=0;
    	}
    	
    	return between_days;
	}
				
	//????????????
	@Transactional(readOnly = false)
	public void startContract(StoreContract storeContract) {
		StoreContract storeContract1= findValidContractByStoreArea(storeContract.getStoreArea().getId());
		if ( storeContract1 != null && storeContract1.getId() != null){
			throw new RuntimeException("????????????????????????????????????????????????????????????");
		}
		if (storeContract.getStatus() != ContractStatus.?????????) {
			throw new RuntimeException("???????????????????????????????????????????????????");			
		}
		for (StoreContractLocationItem locationItem : storeContract.getContractLocationItems()) {
			if (!isRentable(locationItem.getStoreLocation(), storeContract.getStartDate(), storeContract.getEndDate())) {
				throw new RuntimeException("????????????" + locationItem.getStoreLocation().getLabel() + "??????????????????");
			}			
		}
		
		try {
		    String settleRange = calculateSettleRange(storeContract.getChargeDate());
			generatePayment(storeContract, settleRange);
			storeContract.setStatus(ContractStatus.?????????);
			storeContractDao.save(storeContract);
			
			Long[] operatorIds = new Long[1];
			operatorIds[0] = ThreadLocalHolder.getCurrentOperator().getId();
			StoreArea storeArea = storeAreaService.findOne(storeContract.getStoreArea().getId());
			storeArea.setStoreAreaRentStatus(StoreAreaRentStatus.??????);
			storeAreaService.save(storeArea,operatorIds);
			
		} catch (Exception e) {
			logger.error(e.getMessage());			
			throw new RuntimeException("?????????????????????" + e.getMessage());
		}
	}
	
	//??????????????????
	private String calculateSettleRange(Date feeDate) {
	    Calendar c1 = Calendar.getInstance();
	    c1.setTime(feeDate);		
	    String settleRange = "";
	    if (c1.get(Calendar.MONTH) >= 10) {
	    	settleRange = String.valueOf(c1.get(Calendar.YEAR)) + String.valueOf(c1.get(Calendar.MONTH));
	    } else {
	    	settleRange = String.valueOf(c1.get(Calendar.YEAR)) + "0" + String.valueOf(c1.get(Calendar.MONTH));
	    }
	    return settleRange;
	}
	
	//????????????
	@Transactional(readOnly = false)
	public void stopContract(StoreContract storeContract) {
		if (storeContract.getStatus() != ContractStatus.?????????) {
			 throw new RuntimeException("??????????????????????????????????????????????????? ");
		}
		storeContract.setStatus(ContractStatus.?????????);
		storeContractDao.save(storeContract);
		
		Long[] operatorIds = new Long[1];
		operatorIds[0] = ThreadLocalHolder.getCurrentOperator().getId();
		StoreArea storeArea = storeAreaService.findOne(storeContract.getStoreArea().getId());
		storeArea.setStoreAreaRentStatus(StoreAreaRentStatus.??????);
		storeAreaService.save(storeArea,operatorIds);
	}
	
	//????????????
	@Transactional(readOnly = false)
	public void carryoverContract(Date feeDate) {
		List<StoreContract> storeContracts = storeContractDao.findValidContractByDate(feeDate);
		if (storeContracts.size() > 0) {
		    String settleRange = calculateSettleRange(feeDate);
			for (StoreContract contract : storeContracts) {
				try {
					generatePayment(contract, settleRange);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					
				}
			}
		}
	}
	
	//??????????????????????????????
	@Transactional(readOnly = false)
	public void carryoverContractByCustomer(Customer customer,Date feeDate) {
		List<StoreContract> storeContracts = storeContractDao.findValidContractByCustomer(customer.getId());
		if (storeContracts.size() > 0) {
		    String settleRange = calculateSettleRange(feeDate);
			for (StoreContract contract : storeContracts) {
				try {
					generatePayment(contract, settleRange);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					
				}
			}
		}
	}
	
	//????????????
	@Transactional(readOnly = false)
	public void copyContract(StoreContract fromStoreContract, StoreContract toStoreContract) {
		
		try {
			toStoreContract.setStartDate(fromStoreContract.getStartDate());
			toStoreContract.setEndDate(fromStoreContract.getEndDate());
			toStoreContract.setChargeDate(fromStoreContract.getChargeDate());
			toStoreContract.setCustomer(fromStoreContract.getCustomer());
			toStoreContract.setStoreArea(fromStoreContract.getStoreArea());
			toStoreContract.setRentalArea(fromStoreContract.getRentalArea());
			
			toStoreContract.setSerialNo(serialNumberService.getSerialNumber(TransactionType.StoreContract));
			toStoreContract.setContractNo(toStoreContract.getSerialNo());	  //??????????????????	
			toStoreContract.setSignedDate(new Date());
			toStoreContract.setStatus(ContractStatus.?????????);
			
			storeContractDao.save(toStoreContract);	
			
			for (StoreContractLocationItem fromLocationItem : fromStoreContract.getContractLocationItems()) {
				StoreContractLocationItem toLcationItem = new StoreContractLocationItem();
				toLcationItem.setStoreContract(toStoreContract);
				toLcationItem.setStoreLocation(fromLocationItem.getStoreLocation());
				storeContractLocationItemDao.save(toLcationItem);
			}
			
			for (StoreContractFeeItem fromFeeItem : fromStoreContract.getContractFeeItems()) {
				StoreContractFeeItem toFeeItem = new StoreContractFeeItem();
				toFeeItem.setStoreContract(toStoreContract);
				toFeeItem.setFeeItem(fromFeeItem.getFeeItem());
				toFeeItem.setPeriod(fromFeeItem.getPeriod());
				toFeeItem.setOperateType(fromFeeItem.getOperateType());
				toFeeItem.setAmount(fromFeeItem.getAmount());
				storeContractFeeItemDao.save(toFeeItem);
			}		
			
			for (StoreContractPolicyItem fromPolicyItem : fromStoreContract.getContractPolicyItems()) {
				StoreContractPolicyItem toPolicyItem = new StoreContractPolicyItem();
				toPolicyItem.setStoreContract(toStoreContract);
				toPolicyItem.setFeeItem(fromPolicyItem.getFeeItem());
				toPolicyItem.setAmount(fromPolicyItem.getAmount());
				toPolicyItem.setMeasureUnit(fromPolicyItem.getMeasureUnit());
				toPolicyItem.setUseLimited(fromPolicyItem.getUseLimited());
				storeContractPolicyItemDao.save(toPolicyItem);
			}
			
		} catch(Exception e) {
			logger.error(e.getMessage());			
			throw new RuntimeException("?????????????????????" + e.getMessage());
		}
	}
	
	//????????????
	@Transactional(readOnly = false)	
	public Long evictionContract(Long contractId, List<PaymentItem> paymentItem) {
		
	    try {		
			StoreContract storeContract = storeContractDao.findOne(contractId);
			if (storeContract == null) {
				throw new RuntimeException("??????????????????????????????????????????????????????");			
			}
			
			if (storeContract.getStatus() != ContractStatus.?????????) {
				throw new RuntimeException("???????????????????????????????????????????????????");					
			}
			
		    String settleRange = calculateSettleRange(new Date());
		    
			Payment payment = new Payment();	    		
			payment.setSerialNo(serialNumberService.getSerialNumber(TransactionType.Payment));
			payment.setCustomer(storeContract.getCustomer());
			payment.setSettledTime(new Date());
			payment.setSettledBy(ThreadLocalHolder.getCurrentOperator());
			ChargeType chargeType = new ChargeType();
			chargeType.setId(Constants.CHARGETYPE_NOW);
			payment.setChargeType(chargeType);
			payment.setPaymentStatus(PaymentStatus.?????????);
			payment.setPaymentObjectEntity(storeContract.getClass().getSimpleName());
			payment.setPaymentObjectId(storeContract.getId());
			payment.setPaymentObjectSerialNo(storeContract.getSerialNo());
			payment.setSettleRange(settleRange);
			payment.setPaymentType(PaymentType.????????????);
			paymentService.save(payment);
			 
			for (PaymentItem payItem : paymentItem) {
				payItem.setPayment(payment);
				payItem.setPaymentObjectEntity(storeContract.getClass().getSimpleName());
				payItem.setPaymentObjectId(storeContract.getId());
				paymentService.savePaymentItem(payItem);
				  
			}	
			storeContract.setStatus(ContractStatus.?????????);
			storeContractDao.save(storeContract);	
			
			return payment.getId() ;			
		
		} catch(Exception e) {
			logger.error(e.getMessage());			
			throw new RuntimeException("?????????????????????" + e.getMessage());
		}			   
	}
	
	//??????
	@Transactional(readOnly = false)	
	public void cancelEviction(Long contractId, Long paymentId) {
		StoreContract storeContract = storeContractDao.findOne(contractId);		
		storeContract.setStatus(ContractStatus.?????????);
		storeContractDao.save(storeContract);		
		
		paymentService.cancel(paymentId);
		logger.info("????????????: " + ThreadLocalHolder.getCurrentOperator().getUsername() + ThreadLocalHolder.getCurrentOperator().getName() + "; ????????????????????????" + storeContract.getSerialNo());	
	}
	
	public List<StoreContractFeeItem> findFeeItemByStoreContractId(Long storeContractId){
		return storeContractFeeItemDao.findFeeItemByStoreContractId(storeContractId);
	}
	
	public List<StoreContract> findContractByCustomerID(Long customerId) {
		return storeContractDao.findContractByCustomerID(customerId);
	} 
	public List<PaymentItem> settlement(String[] feeitem, String[] money) {
		ArrayList<PaymentItem> list = new ArrayList<PaymentItem>();
		if (feeitem == null) {
			return list;
		}

		CEFeeItem feeItem = null;
		PaymentItem paymentItem = null;
		for (int i = 0; i < feeitem.length; i++) {
			// ??????????????????
			paymentItem = new PaymentItem();
			paymentItem.setFeeItem(ceFeeItemService.findOne(Long.valueOf(feeitem[i])));
			paymentItem.setAmount(new BigDecimal(money[i]));
			paymentItem.setPaymentStatus(PaymentStatus.?????????);
			paymentItem.setActuallyAmount(new BigDecimal(money[i]));
			list.add(paymentItem);
		}
		return list;
	}
}
