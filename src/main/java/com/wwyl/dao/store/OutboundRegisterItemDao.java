package com.wwyl.dao.store;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import com.wwyl.dao.BaseRepository;

import com.wwyl.entity.store.OutboundRegisterItem;

public  interface OutboundRegisterItemDao  extends BaseRepository<OutboundRegisterItem, Long>{
 	@Query("select  initem from OutboundRegisterItem initem where  initem.outboundRegister.serialNo=?1")
 	public List<OutboundRegisterItem> findOutboundRegisterItems(String serialNo); 
 	@Query("select  outitem from OutboundRegisterItem outitem where  outitem.outboundRegister.id=?1")
 	public List<OutboundRegisterItem> findByOutboundRegisterItems(Long outboundRegisterId); 
 	@Query("select  outitem from OutboundRegisterItem outitem where  outitem.storeContainer.id=?1")
 	public List<OutboundRegisterItem> findByStoreContainerId(Long storeContainerId); 
 	
}
