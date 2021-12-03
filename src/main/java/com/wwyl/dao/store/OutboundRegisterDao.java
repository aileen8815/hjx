package com.wwyl.dao.store;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.StockOutStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.OutboundRegister;

@Repository
public interface OutboundRegisterDao extends BaseRepository<OutboundRegister, Long> {

	@Query("select  out from OutboundRegister out  where  out.serialNo = ?1  ")
	public OutboundRegister findBySerialNo(String serialNo);

	@Query("select o from OutboundRegister o where o.stockOutStatus < ?1 and o.serialNo like %?2%")
	public List<OutboundRegister> findOutboundRegisterUnderStatus(StockOutStatus stockOutStatus, String serialNo);

	@Query("select  out from OutboundRegister out  where  out.id in ?1 and out.serialNo like %?2% ")
	public List<OutboundRegister> findByIds(Object[] ids,String serialNo);
	@Modifying
	@Query("update OutboundRegister set stockOutStatus = ?2 where id = ?1")
	public void updateStatus(Long id, StockOutStatus stockOutStatus);
	
	@Modifying
	@Query("update OutboundTarry set checked = ?1 where outboundRegister.id = ?2 and tallyArea.id=?3")
	public void updateInboundTarrychecked(boolean checked, Long outboundRegisterid, Long tallyAreaid);
	
}
