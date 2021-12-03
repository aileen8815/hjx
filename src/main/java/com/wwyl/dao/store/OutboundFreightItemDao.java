package com.wwyl.dao.store;



import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.OutboundFreightItem;

public  interface OutboundFreightItemDao  extends BaseRepository<OutboundFreightItem, Long>{
	@Query("select  outItem from OutboundFreightItem outItem where  outItem.outboundFreight.id=?1")
 	public List<OutboundFreightItem> findOutboundFreightItemList(Long outboundFreightId);

}
