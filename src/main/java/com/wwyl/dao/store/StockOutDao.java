package com.wwyl.dao.store;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.wwyl.Enums.StockOutStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StockOut;

public interface StockOutDao extends BaseRepository<StockOut, Long> {

	@Query("select  stock from StockOut stock where  stock.outboundRegister.id=?1")
	public List<StockOut> findStockOutByOutboundRegisterId(Long outboundRegisterId);

	@Query("select  stock from StockOut stock where stock.stockOutStatus in ?1 and (stock.storeLocation.storeArea.id = ?2 or stock.storeLocation.storeArea.parent.id = ?2)")
	public List<StockOut> findUnclaimStockOut(StockOutStatus[] stockOutStatus, Long storeAreaID);
	
	@Query("select  stock.storeContainer.id from StockOut stock where stock.stockOutStatus in ?1 and product.id  in ?2  and  customer.id =?3 ")
	public List<Long> findUseingStockOut(StockOutStatus[] stockOutStatus,Object[] products,Long Customerid);
	
	@Query("select  stock from StockOut stock where stock.storeContainer.id =?1")
	public List<StockOut> findBystoreContainer(Long storeContainerId);
	
	@Query("select  stock from StockOut stock where stock.customer.id =?1 and stock.outboundRegister.outboundTime between ?2 and ?3 and stock.stockOutStatus in ?4")
	public List<StockOut> findDailyStockOutByCustomerId(Long customerid, Date startDate, Date endDate, StockOutStatus[] stockOutStatus);
	
}
