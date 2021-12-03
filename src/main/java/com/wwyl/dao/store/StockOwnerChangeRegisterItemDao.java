package com.wwyl.dao.store;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StockOwnerChangeRegisterItem;

/**
 * @author jianl
 */
@Repository
public interface StockOwnerChangeRegisterItemDao extends BaseRepository<StockOwnerChangeRegisterItem,Long>{
	@Query("select  outItem from StockOwnerChangeRegisterItem outItem where  outItem.stockOwnerChange.id=?1")
 	public List<StockOwnerChangeRegisterItem> findBystockOwnerChangeItemid(Long stockOwnerChangeItemid);
		 
}
