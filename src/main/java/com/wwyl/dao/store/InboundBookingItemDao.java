package com.wwyl.dao.store;


import java.util.List;
import org.springframework.data.jpa.repository.Query;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.InboundBookingItem;

public  interface InboundBookingItemDao  extends BaseRepository<InboundBookingItem, Long>{
 	@Query("select  initem from InboundBookingItem initem where  initem.inboundBooking.serialNo=?1")
 	public List<InboundBookingItem> findInboundBookingItems(String serialNo); 
 	
 	@Query("select  initem from InboundBookingItem initem where  initem.inboundBooking.id=?1")
 	public List<InboundBookingItem> findByInboundBookingItems(Long inboundBookingId); 
 	
 	@Query("select  initem from InboundBookingItem initem where  initem.inboundBooking.id=?1   and initem.product.id=?2 ")
 	public InboundBookingItem findBybookingIdAndProId(Long inboundBookingId,Long productId);
}
