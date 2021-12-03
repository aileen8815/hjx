package com.wwyl.dao.store;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wwyl.Enums.StockOwnerChangeStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StockOwnerChange;
 

/**
 * @author jianl
 */
@Repository
public interface StockOwnerChangeDao extends BaseRepository<StockOwnerChange,Long>{
	@Modifying
	@Query("update StockOwnerChange set stockOwnerChangeStatus = ?1 where id = ?2")
	public void updateStatus(StockOwnerChangeStatus stockOwnerChangeStatus,Long id );
		 
}
