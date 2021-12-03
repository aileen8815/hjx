package com.wwyl.dao.tenancy;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.tenancy.StoreContractPolicyItem;

/**
 * @author Administrator
 */

@Repository
public interface StoreContractPolicyItemDao extends BaseRepository<StoreContractPolicyItem, Long> {
	
 	@Query("select i from StoreContractPolicyItem i where  i.storeContract.serialNo=?1")
 	public List<StoreContractPolicyItem> findStoreContractPolicyItems(String serialNo); 		

}
