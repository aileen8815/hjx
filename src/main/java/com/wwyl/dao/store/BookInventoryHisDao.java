package com.wwyl.dao.store;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.BookInventoryHis;
/**
 * @author liujian
 */
@Repository
public interface BookInventoryHisDao extends BaseRepository<BookInventoryHis, Long> {
	@Query("select bookin from BookInventoryHis bookin	 where  storeContainer.id = ?1 order by activeTime desc")
	public List<BookInventoryHis> findBookInventoryHis(Long storeContainer);
	
	@Query("select bookin from BookInventoryHis bookin where bookin.customer.id = ?1 and bookin.activeTime <=?2 "
			+ " and (bookin.inactiveTime > ?2  or bookin.inactiveTime is null) ")
	public List<BookInventoryHis> findBookInventoryHisByCustomerIdAndDateTime(Long customerId, Date endDate);
	
	@Query("select bookin from  BookInventoryHis bookin  where bookin.stockIn.id= ?1 and bookin.product.id= ?2 order by bookin.productionDate  desc ")
	public BookInventoryHis findHisByStockinIdAndProductId(Long stockinId ,Long productId);
}
