package com.wwyl.dao.finance;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.PaymentStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.finance.AccountChecking;
/**
 * @author jianl
 */

@Repository
public interface AccountCheckingDao extends BaseRepository<AccountChecking,Long> {
	@Query("select ac from AccountChecking ac where ac.customer.id=?1 order by checkingTime desc") 
	public Page<AccountChecking> findAccountCheckingsByCustomer(Long customerId, Pageable pageable);
	
	@Query("select ac from AccountChecking ac order by checkingTime desc") 
	public Page<AccountChecking> findAccountCheckings(Pageable pageable);	
	
	@Query("select ac from AccountChecking ac where ac.paymentStatus=?1 order by checkingTime desc") 
	public Page<AccountChecking> findAccountCheckingsByPaymentStatus(PaymentStatus paymentStatus, Pageable pageable);
	
	@Query("select ac from AccountChecking ac where ac.customer.id=?1 and ac.paymentStatus=?2 order by checkingTime desc") 
	public Page<AccountChecking> findAccountCheckingsByCustomerAndPaymentStatus(Long customerId, PaymentStatus paymentStatus, Pageable pageable);
	
	@Query("select ac from AccountChecking ac where  ac.paymentStatus=?1  and  ac.customer.id=?2  order by checkingTime desc") 
	public List<AccountChecking> findByPaymentStatus(PaymentStatus paymentStatus,Long customerId);
	
	@Query("select ac from AccountChecking ac where   ac.customer.id=?1   and  ac.checkingTime  between  ?2 and ?3 ") 
	public List<AccountChecking> findBycheckingTime(Long customerId ,Date startTime,Date  endTime);

	@Query("select ac from AccountChecking ac where ac.paymentStatus=?1 order by checkingTime desc") 
	public List<AccountChecking> findAccountCheckingsByStatus(PaymentStatus paymentStatus);
	
}
