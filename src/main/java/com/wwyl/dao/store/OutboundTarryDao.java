package com.wwyl.dao.store;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
 
import com.wwyl.entity.store.OutboundTarry;

/**
 * @author jianl
 */
@Repository
public interface OutboundTarryDao extends BaseRepository<OutboundTarry,Long>{
	@Query("select it from OutboundTarry it where it.outboundRegister.id=?1")
	public List<OutboundTarry> findByOutboundRegisterId(Long outboundRegisterId);
	
	@Query("select outtarry.handsetAddress,count(outtarry.id)  from  OutboundTarry  outtarry ,OutboundRegister outregister   where   outtarry.outboundRegister.id=outregister.id    and  outregister.stockOutStatus=0  group by outtarry.handsetAddress ")
	public List<Object> findHandsetTask();
}
