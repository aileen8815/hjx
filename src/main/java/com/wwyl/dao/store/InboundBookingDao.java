package com.wwyl.dao.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wwyl.Enums.BookingStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.InboundBooking;

@Repository
public  interface InboundBookingDao extends BaseRepository<InboundBooking, Long>{
 	@Query("select  inr from InboundBooking inr  where  inr.serialNo= ?1  ")
 	public InboundBooking findBySerialNo(String serialNo); 
 	@Query("select  inr from InboundBooking inr  where inr.bookingStatus=?1	 and  inr.serialNo like %?2% ")
 	public Page<InboundBooking> findBySerialNoLike(BookingStatus bookingStatus,String serialNo,Pageable pageable); 
	@Modifying
	@Query("update InboundBooking set bookingStatus = ?1 where id = ?2")
	public void updateStatus(BookingStatus bookingStatus,Long id);
}
