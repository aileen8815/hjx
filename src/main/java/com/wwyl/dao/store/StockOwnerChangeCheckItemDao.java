package com.wwyl.dao.store;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wwyl.Enums.StockOwnerChangeStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StockOwnerChangeCheckItem;

/**
 * @author jianl
 */
@Repository
public interface StockOwnerChangeCheckItemDao extends BaseRepository<StockOwnerChangeCheckItem,Long>{
	@Query("select  storeContainer.id  from StockOwnerChangeCheckItem  where   stockOwnerChange.stockOwnerChangeStatus=?1")
 	public List<Long> findByStockOwnerChangeStatus(StockOwnerChangeStatus stockOwnerChangeStatus);
		 
}
