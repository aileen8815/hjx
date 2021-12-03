package com.wwyl.dao.tenancy;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.tenancy.StoreContractFeeItem;
import com.wwyl.entity.tenancy.StoreContractLocationItem;

/**
 * @author sjwang
 */
@Repository
public interface StoreContractFeeItemDao extends BaseRepository<StoreContractFeeItem, Long>{
	
 	@Query("select i from StoreContractFeeItem i where  i.storeContract.serialNo=?1")
 	public List<StoreContractFeeItem> findStoreContractFeeItems(String serialNo); 	
 	
 	@Query("select i from StoreContractFeeItem i where  i.storeContract.id=?1")
 	public List<StoreContractFeeItem> findFeeItemByStoreContractId(Long storeContractId); 	
 
}
