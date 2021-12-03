package com.wwyl.dao.store;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StockTakingItem;
 

/**
 * @author jianl
 */
@Repository
public interface StockTakingItemDao extends BaseRepository<StockTakingItem,Long>{

	@Modifying
 	@Query("delete from  StockTakingItem item where  item.stockTaking.id=?1")
 	public int deleteByStockTaking(Long stockTakingId); 

}
