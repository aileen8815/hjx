package com.wwyl.dao.store;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.BookingStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.OutboundBooking;

@Repository
public  interface OutboundBookingDao extends BaseRepository<OutboundBooking, Long>{
 	@Query("select  out from OutboundBooking out  where  out.serialNo= ?1  ")
 	public List<OutboundBooking> findBySerialNo(String serialNo); 
 	@Query("select  out from OutboundBooking out  where out.bookingStatus=?1	 and  out.serialNo like %?2% ")
 	public Page<OutboundBooking> findBySerialNoLike(BookingStatus bookingStatus,String serialNo,Pageable pageable); 
	@Modifying
	@Query("update OutboundBooking set bookingStatus = ?1 where id = ?2")
	public void updateStatus(BookingStatus bookingStatus,Long id);
}
