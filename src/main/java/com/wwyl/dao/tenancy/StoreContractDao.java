package com.wwyl.dao.tenancy;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.tenancy.StoreContract;

/**
 * @author sjwang
 */
@Repository
public interface StoreContractDao extends BaseRepository<StoreContract, Long> {
	
 	@Query("select inr from StoreContract inr  where  inr.serialNo= ?1  ")
 	public StoreContract findBySerialNo(String serialNo); 	
 	
	@Query("select c from StoreContract c where (?1 between c.startDate and c.endDate) and c.status = 1 ")
	public List<StoreContract> findValidContractByDate(Date feeDate);
	
	@Query("select c from StoreContract c where c.customer.id = ?1 and c.status = 1 ")
	public List<StoreContract> findValidContractByCustomer(Long customerId);	
	
	@Query("select c from StoreContract c where c.customer.id = ?1 and c.status = 4 ")
	public List<StoreContract> findEvictionContractByCustomer(Long customerId);	
	
	@Query("select c from StoreContract c where c.customer.id = ?1 and (c.status = 1 or c.status = 3) ")
	public List<StoreContract> findContractByCustomerID(Long customerId);
	
	@Query("select c from StoreContract c where c.storeArea.id = ?1 and c.status = 1 ")
	public StoreContract findValidContractByStoreArea(Long storeAreaId);	
	
	@Query("select c from StoreContract c where c.contractNo = ?1 and c.status = 1 ")
	public List<StoreContract> findValidContractByContractNo(String contractNo);
	
	@Query("select c from StoreContract c where (?1 between c.startDate and c.endDate) and c.customer.id = ?2 and c.status = 1 ")
	public List<StoreContract> findValidContractByDateTimeAndCustomerId(Date feeDate, Long customerId);
}
