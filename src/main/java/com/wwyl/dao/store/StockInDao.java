package com.wwyl.dao.store;

import java.util.List;

 

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wwyl.Enums.StockInStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StockIn;

public interface StockInDao extends BaseRepository<StockIn, Long> {
	@Query("select  stock from StockIn stock where stock.stockInStatus in ?1 and (stock.storeArea.id = ?2 or stock.storeArea.parent.id = ?2 )")
	public List<StockIn> findUnclaimStockIn(StockInStatus[] stockInStatus, Long storeAreaID);
	@Modifying
	@Query("delete  from  StockIn stock where  stock.inboundReceiptItem.id=?1 and stock.stockInStatus = ?2")
	public void delStockIn(Long inboundReceiptItemId,StockInStatus stockInStatus);
	@Query("select  stock from StockIn stock where  stock.inboundReceiptItem.id=?1 and stock.stockInStatus = ?2")
	public StockIn findStockInByreceiptId(Long inboundReceiptItemId,StockInStatus stockInStatus);
	@Query("select  stock from StockIn stock where  stock.inboundRegister.id=?1 order by stock.stockInStatus")
	public List<StockIn> findStockInByRegisterId(Long inboundRegisterId);
	@Query("select  stock from StockIn stock where  stock.inboundReceiptItem.id=?1 order by stock.stockInStatus")
	public StockIn findStockInByInboundReceiptItemId(Long inboundReceiptItemId);
}
