package com.wwyl.dao.tenancy;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.tenancy.StoreContract;
import com.wwyl.entity.tenancy.StoreContractLocationItem;

/**
 * @author sjwang
 */
@Repository
public interface StoreContractLocationItemDao extends BaseRepository<StoreContractLocationItem, Long> {
	
 	@Query("select i from StoreContractLocationItem i where  i.storeContract.serialNo=?1")
 	public List<StoreContractLocationItem> findStoreContractLocationItems(String serialNo); 		
 	
	@Query("select c from StoreContractLocationItem c where (?1 between c.storeContract.startDate and c.storeContract.endDate" +
			" or ?2 between c.storeContract.startDate and c.storeContract.endDate" +
			" or c.storeContract.startDate between ?1 and ?2" +
			" or c.storeContract.endDate between ?1 and ?2) and (c.storeContract.status = 1)" +
			" and (c.storeLocation.id = ?3)")
	public List<StoreContract> find(Date startDate, Date endDate, Long locationId); 	

}
