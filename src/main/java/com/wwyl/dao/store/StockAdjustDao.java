package com.wwyl.dao.store;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StockAdjust;

/**
 * @author fyunli
 */
@Repository
public interface StockAdjustDao extends BaseRepository<StockAdjust, Long> {
/*	@Query("select  stok from StockAdjust stok where  stok.stockAdjustStatus=0 ")
 	public Page<StockAdjust> findStockAdjustCheck( Pageable pageable); */
}
