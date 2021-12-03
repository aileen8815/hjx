package com.wwyl.dao.store;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.StockWastageStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StockWastageItem;
/**
 * @author jianl
 */
@Repository
public interface StockWastageItemDao extends BaseRepository<StockWastageItem,Long>{
	@Query("select  item from StockWastageItem item where item.stockWastage.stockWastageStatus not in ?1  and  item.storeContainer.id =?2")
	public List<StockWastageItem> findStockWastageItem(StockWastageStatus[] stockWastageStatus,Long storeContainerId);
}
