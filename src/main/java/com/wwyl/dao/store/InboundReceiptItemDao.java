package com.wwyl.dao.store;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.store.InboundReceiptItem;

public interface InboundReceiptItemDao extends BaseRepository<InboundReceiptItem, Long> {

	@Query("select i from InboundReceiptItem i where i.storeContainer.id = ?1 order by i.receiptTime desc")
	public Page<InboundReceiptItem> getLastInboundReceiptItemByContainerCode(Long storeContainerId, Pageable pageable);

	@Query("select i from InboundReceiptItem i where i.inboundRegister.id = ?1  order by i.receiptTime desc")
	public List<InboundReceiptItem> findByInboundRegisterAndtarryArea(Long inboundRegisterid);
	@Query("select i.storeContainer from InboundReceiptItem i where i.inboundRegister.stockInStatus < 4")
	public List<StoreContainer> findByUnfinished();
	/**
	 * 查看托盘是否存在于未完成的入库单
	 * @param storeContainer
	 * @return
	 */
	@Query("select i from InboundReceiptItem i where i.inboundRegister.stockInStatus < 5  and i.storeContainer.id=?1")
	public List<InboundReceiptItem> findByUndone(Long storeContainer);
	
	@Query("select i from InboundReceiptItem i where i.inboundRegister.id = ?1  order by i.product.name ")
	public List<InboundReceiptItem> findInboundReceiptItemByRegisterId(Long inboundRegisterid);

	@Query("select i from InboundReceiptItem i where i.inboundRegister.id = ?1 and i.product.id = ?2 order by i.product.name ")
	public List<InboundReceiptItem> findInboundReceiptItemByRegisterIdAndProductId(Long inboundRegisterid, Long productid);
}
