package com.wwyl.dao.store;

 

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.StockRelocationStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StockRelocation;

/**
 * @author jianl
 */
@Repository
public interface StockRelocationDao extends BaseRepository<StockRelocation,Long>{
	@Query("select  stock from StockRelocation stock where  stock.storeContainer.id=?1  and  stockRelocationStatus=?2")
 	public StockRelocation findByStoreContainer(Long storeContainerId,StockRelocationStatus stockRelocationStatus);
	
	@Query("select  stock from StockRelocation stock where  stock.guid=?1")
 	public StockRelocation findByGuid(String guid);
	
		 
}
