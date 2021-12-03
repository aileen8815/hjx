package com.wwyl.dao.finance;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.PaymentStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.finance.Payment;
 


/**
 * @author jianl
 */

@Repository
public interface PaymentDao extends BaseRepository<Payment,Long> {
 
		@Query("select  pay from Payment pay  where  pay.customer.id= ?1 and  pay.settledTime> ?2 "
	 			+ " and pay.settledTime< ?3  order by pay.settledTime ")
	 	public Page<Payment> queryInboundReceipts(String customerId,String startTme,String endTime ,Pageable pageable); 
		@Modifying
		@Query("update Payment set PaymentStatus = ?1 where id = ?2")
		public void updateStatus(PaymentStatus PaymentStatus, Long id);
	 	
		@Query("select p from Payment p where p.settleRange=?1 and p.paymentObjectEntity=?2 and p.paymentObjectId=?3 and p.paymentStatus<>5") //5是已作废
		public List<Payment> findBySettleRangeAndPaymentObject(String settleRange, String paymentObjectEntity, Long paymentObjectId);	 	
		
		@Query("select p from Payment p where p.paymentObjectEntity=?1 and p.paymentObjectId=?2 and p.paymentStatus<>5 and p.paymentType=1") 
		public Payment findPaymentByContractId(String paymentObjectEntity, Long paymentObjectId);	 	
		
		@Query("select p from Payment p where p.customer.id=?1  and p.paymentStatus=?2 and p.paymentType=0") 
		public List<Payment> findPaymentByCustomer(Long customerId,PaymentStatus paymentStatus);	
		
	 	@Query("select  payment from Payment payment  where  payment.serialNo like %?1%  and payment.temporary=?2 ")
	 	public Page<Payment> findBySerialNoLike(String serialNo,int temporary,Pageable pageable); 
	 	
	 	@Query("select  pay from Payment pay where pay.paymentStatus = ?1 and pay.customer.id=?2   and pay.chargeType.id=?3 and pay.settledTime between  ?4 and ?5")
	 	public List<Payment> findPayMonthlyPayments(PaymentStatus paymentStatus,Long customerId,Long chargeTypeId,Date startTime,Date endTime); 
	 	//为了财务台帐增加的查询,查询当天所有有效单据
	 	@Query("select p from Payment p where p.customer.id=?1 "
	 			+ "and p.settledTime >= to_date(?2,'yyyy-mm-dd') "
	 			+ "and p.settledTime < to_date(?3,'yyyy-mm-dd') "
	 			+ "and (p.paymentStatus<>5)") 
		public List<Payment> findPaymentByCustomerAndDateTime(Long customerId, String datetime, String datetimeAddOneDay);
}
