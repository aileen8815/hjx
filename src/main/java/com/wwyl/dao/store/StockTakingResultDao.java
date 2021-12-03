package com.wwyl.dao.store;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.StockTakingStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StockTakingResult;
 

/**
 * @author jianl
 */
@Repository
public interface StockTakingResultDao extends BaseRepository<StockTakingResult,Long>{

	@Query("select  result from StockTakingResult result where result.stockTaking.id =?1 and result.storeLocation.id=?2 and result.stockTakeResultStatus=1")
	public StockTakingResult findStockTakingDifferentRsult(Long stockTakingId,Long storeLocationId);
	@Query("select  result from StockTakingResult result where result.stockTaking.stockTakingStatus not in ?1 and result.storeContainer.id=?2 and result.stockTakeResultStatus=1")
	public List<StockTakingResult> findUnfinishedStockTakingDifferentRsult(StockTakingStatus[] stockTakingStatus,Long storeContainerId);
	@Query("select  result from StockTakingResult result where result.id=?1 and result.stockTakeResultStatus=1")
	public StockTakingResult findOneDifferentRsult(Long resultId);
	@Query("select  result from StockTakingResult result where result.stockTaking.id =?1 and result.storeLocation.id=?2 and (result.stockTakeResultStatus=0 or result.stockTakeResultStatus=2)")
	public StockTakingResult findStockTakingRsult(Long stockTakingId,Long storeLocationId);	
}
