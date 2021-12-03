package com.wwyl.dao.store;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.InboundRegisterItem;

public  interface InboundRegisterItemDao  extends BaseRepository<InboundRegisterItem, Long>{
 	@Query("select  initem from InboundRegisterItem initem where  initem.inboundRegister.serialNo=?1")
 	public List<InboundRegisterItem> findInboundRegisterItems(String serialNo); 
 	
 	@Query("select  initem from InboundRegisterItem initem where  initem.inboundRegister.id=?1   and initem.product.id=?2 ")
 	public InboundRegisterItem findInboundRegisterIdAndproId(Long inboundRegisterId,Long productId); 

}
