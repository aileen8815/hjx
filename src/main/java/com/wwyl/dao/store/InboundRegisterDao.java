package com.wwyl.dao.store;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.StockInStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.InboundRegister;

@Repository
public interface InboundRegisterDao extends BaseRepository<InboundRegister, Long> {

	@Query("select  inr from InboundRegister inr  where  inr.serialNo= ?1  ")
	public InboundRegister findBySerialNo(String serialNo);

	@Query("select i from InboundRegister i where i.stockInStatus < ?1 and i.serialNo like %?2%")
	public List<InboundRegister> findInboundRegisterUnderStatus(StockInStatus stockInStatus, String serialNo);

	@Modifying
	@Query("update InboundRegister set stockInStatus = ?2 where id = ?1")
	public void updateStatus(Long id, StockInStatus stockInStatus);
	@Modifying
	@Query("update InboundTarry set checked = ?1 where inboundRegister.id = ?2 and tallyArea.id=?3")
	public void updateInboundTarrychecked(boolean checked,Long inboundRegisterid,Long tallyAreaid );
	@Query("select  inr from InboundRegister inr  where  inr.id  in  ?1   and inr.serialNo like %?2% ")
	public List<InboundRegister> findByids(Object[] ids,String serialNo);
}
