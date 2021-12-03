package com.wwyl.dao.store;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wwyl.Enums.StockTakingStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StockTaking;
 
 

/**
 * @author jianl
 */
@Repository
public interface StockTakingDao extends BaseRepository<StockTaking,Long>{
	@Modifying
	@Query("update StockTaking set stockTakingStatus = ?1 where id = ?2")
	public void updateStatus(StockTakingStatus stockTakingStatus,Long id);
}
