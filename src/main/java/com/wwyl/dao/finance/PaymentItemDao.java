package com.wwyl.dao.finance;

 
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.PaymentStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.finance.PaymentItem;

/**
 * @author jianl
 */
@Repository
public interface PaymentItemDao extends BaseRepository<PaymentItem,Long>{
 
	 	@Query("select  item from PaymentItem item where  item.payment.id=?1")
	 	public List<PaymentItem> findPaymentItems(Long paymentId); 
	 	@Query("select  item from PaymentItem item where  item.feeItem.id=?1 and item.payment.customer.id=?2  and item.paymentStatus=?3")
	 	public List<PaymentItem> findByfeeItemIdAndcustomerId(Long feeItemId,Long customerId,PaymentStatus paymentStatus); 
		@Modifying
		@Query("update PaymentItem set paymentStatus = ?1 where payment.id = ?2")
		public void updateStatus(PaymentStatus paymentStatus,Long id);
		//为了财务台帐增加的查询,查询当天已付款,已退款的单据
		
	 	@Query("select item from PaymentItem item where item.payment.customer.id=?1 "
	 			+ "and item.payTime >= to_date(?2, 'yyyy-mm-dd')  "
	 			+ "and item.payTime < to_date(?3, 'yyyy-mm-dd') "
	 			+ "and (item.paymentStatus=1 or item.paymentStatus=5)") 
		public List<PaymentItem> findPaymentByCustomerAndDateTime(Long customerId, String datetime, String datetimeAddOneDay);
}
