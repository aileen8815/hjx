package com.wwyl.dao.store;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StockAdjustItem;

/**
 * @author fyunli
 */
@Repository
public interface StockAdjustItemDao extends BaseRepository<StockAdjustItem, Long> {
/*	@Query("select  item from StockAdjustItem item where  item.stockAdjust.id=?1")
 	public List<StockAdjustItem> findByStockAdjustId(Long StockAdjustId); */
}
