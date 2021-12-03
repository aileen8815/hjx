package com.wwyl.dao.store;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.OutboundCheckItem;

public interface OutboundCheckItemDao extends BaseRepository<OutboundCheckItem, Long> {
	
	@Query("select  initem from OutboundCheckItem initem where  initem.outboundRegister.id=?1")
	public List<OutboundCheckItem> findOutboundCheckItems(Long outboundRegisterId);

	@Query("select o from OutboundCheckItem o where o.storeContainer.id = ? order by checkTime desc")
	public Page<OutboundCheckItem> getLastOutboundCheckItemByContainerCode(Long id, Pageable pageable);
	@Query("select o from OutboundCheckItem o where o.outboundRegister.id = ?1 and o.product.id=?2")
	public List<OutboundCheckItem> findByOutboundRegisterAndProduct(Long outboundRegisterId, Long productId);
	
	@Query("select o from OutboundCheckItem o where o.outboundRegister.id = ?1 and o.tarryArea.id=?2")
	public List<OutboundCheckItem> findOutboundChecItemByOutboudregisterAndTallyArea(
			Long outboundRegisterid, Long tallyareaid);
	
}
