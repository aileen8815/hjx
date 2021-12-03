package com.wwyl.dao.store;


import java.util.List;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.OutboundBookingItem;

public  interface OutboundBookingItemDao  extends BaseRepository<OutboundBookingItem, Long>{
 	@Query("select  outitem from OutboundBookingItem outitem where  outitem.outboundBooking.serialNo=?1")
 	public List<OutboundBookingItem> findByserialNo(String serialNo); 
 	
 	@Query("select  outitem from OutboundBookingItem outitem where  outitem.outboundBooking.id=?1")
 	public List<OutboundBookingItem> findByOutboundBookingItems(Long outboundBookingId); 
	@Modifying
 	@Query("delete from  OutboundBookingItem outItem where outItem.product.id=?1 and outItem.storeContainer.id=?2 and outItem.outboundBooking.id=?3")
 	public int deleteOutboundBookingItem(Long productId,Long storeContainerId,Long outboundBookingId); 
	@Modifying
 	@Query("delete from  OutboundBookingItem outItem where  outItem.outboundBooking.id=?1")
 	public int deleteOutboundBookingItem(Long outboundBookingId); 

}
