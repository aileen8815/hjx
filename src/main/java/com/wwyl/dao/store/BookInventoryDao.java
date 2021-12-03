package com.wwyl.dao.store;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.store.BookInventory;

/**
 * @author liujian
 */
@Repository
public interface BookInventoryDao extends BaseRepository<BookInventory, Long> {

	@Query("select bookin from BookInventory bookin	 where  storeContainer.id not in ?1   and product.id in ?2  and bookin.customer.id= ?3   order by product.name,inboundRegisterSerialNo")
	public List<BookInventory> findByCustomerIdAndproductId(Object[] storeContainers, Object[] products,Long customerId);
	
	@Query("select bookin from BookInventory bookin	 where  bookin.customer.id= ?1 and product.id in ?2   order by product.name,inboundRegisterSerialNo")
	public List<BookInventory> findByCustomerIdAndproductId(Long customerId, Object[] products);
	
	@Query("select bookin from BookInventory bookin	 where  bookin.customer.id= ?1 and   bookin.storeContainer.id in ?2  ")
	public List<BookInventory> findByStoreContainers(Long customerId, Object[] storeContainers);
	@Query("select bookin from BookInventory bookin	 where   bookin.storeContainer.id = ?1  ")
	public BookInventory findByStoreContainer(Long storeContainerId);
	@Query("select bookin from BookInventory bookin	 where  bookin.customer.id= ?1")
	public List<BookInventory> findByCustomerId(Long customerId);

	@Query("select bookin from BookInventory bookin	 where   bookin.product.id= ?1 and bookin.storeContainer.id= ?2 and bookin.customer.id= ?3")
	public BookInventory findByProductIdAndstoreContainerId(Long productId, Long storeContainerId, Long customerId);

	@Modifying
	@Query("delete from  BookInventory book where book.product.id=?1  and book.storeContainer.id=?2")
	public int deleteByProductAndStoreContainer(Long productId, Long storeContainerId);

	@Query("select booki.product from BookInventory booki where " + " booki.customer.id= ?1 order by  booki.product.name ")
	public List<Product> queryProductBycustomerId(Long customerId);
	
	@Query("select bookin from BookInventory bookin	 where  bookin.customer.id= ?1 and storeContainer.id = ?2")
	public BookInventory findByCustomerIdAndStoreContainer(Long customerId, Long storeContainers);
	
	@Query("select bookin from BookInventory bookin	 where  bookin.storeLocation.id in ?1 ")
	public List<BookInventory> findByStoreLocationids(Object[] StoreLocationids);
	
	@Query("select bookin from BookInventory  bookin where  bookin.stockInTime  between  ?1 and ?2  order by bookin.stockInTime desc")
	public  List<BookInventory>  findByStockInTime(Date startTime ,Date endTime);
	@Query("select bookin from  BookInventory bookin  where bookin.productionDate between  ?1 and ?2   order by   bookin.productionDate  desc ")
	public  List<BookInventory>  findByProductionDate(Date startTime ,Date  endTime);
	
	@Query("select bookin from  BookInventory bookin  where bookin.stockIn.id= ?1 and bookin.product.id= ?2 order by   bookin.productionDate  desc ")
	public BookInventory findByStockinIdAndProductId(Long stockinId ,Long productId);
	
}
