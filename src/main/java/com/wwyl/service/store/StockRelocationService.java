package com.wwyl.service.store;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Constants;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.Enums.StockRelocationStatus;
import com.wwyl.Enums.StoreLocationStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.dao.settings.CustomerDao;
import com.wwyl.dao.settings.StoreLocationDao;
import com.wwyl.dao.settings.StoreContainerDao;
import com.wwyl.dao.store.BookInventoryDao;
import com.wwyl.dao.store.BookInventoryHisDao;
import com.wwyl.dao.store.StockRelocationDao;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.StockRelocation;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreLocationService;

/**
 * @author fyunli
 */
@Service
@Transactional(readOnly = true)
public class StockRelocationService {

	@Resource
	private StockRelocationDao stockRelocationDao;

	@Resource
	private StoreLocationDao storeLocationDao;

	@Resource
	private StoreContainerDao storeContainerDao;

	@Resource
	private CustomerDao customerDao;

	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private BookInventoryHisDao bookInventoryHisDao;
	@Resource
	private BookInventoryDao bookInventoryDao;
	@Resource
	private StoreLocationService storeLocationService;
	@Resource
	private SerialNumberService serialNumberService;

	public List<StockRelocation> findAll() {
		return stockRelocationDao.findAll();
	}

	public StockRelocation findOneStockRelocation(Long id) {
		return stockRelocationDao.findOne(id);
	}

	public StockRelocation findByStoreContainer(Long id,
			StockRelocationStatus stockRelocationStatus) {
		return stockRelocationDao.findByStoreContainer(id,
				stockRelocationStatus);
	}

	@Transactional(readOnly = false)
	public StockRelocation saveStockRelocation(StockRelocation stockRelocation,
			String locaionCode) {
		if (!StringUtils.isBlank(locaionCode)) {
			StoreLocation storeLocation = storeLocationDao
					.findByCode(locaionCode);
			stockRelocation.setFromStoreLocation(storeLocation);
			stockRelocation.setStockRelocationStatus(StockRelocationStatus.移位中);
			stockRelocation.setOperator(ThreadLocalHolder.getCurrentOperator());
			stockRelocation.setOperatorTime(new Date());
			stockRelocation.setSerialNo(serialNumberService
					.getSerialNumber(TransactionType.StockRelocation));
			stockRelocationDao.save(stockRelocation);
		}
		return stockRelocation != null && stockRelocation.getId() != null ? stockRelocation
				: new StockRelocation();
	}

	/**
	 * api方法
	 * 
	 * @param storeContainerId
	 * @param storeLocationId
	 * @param customerId
	 * @return
	 */
	@Transactional(readOnly = false)
	public StockRelocation saveStockRelocation(Long storeContainerId,
			Long storeLocationId, Long customerId, String guid) {
		StockRelocation stockRelocation = new StockRelocation();
		// 储位
		StoreLocation storeLocation = storeLocationDao.findOne(storeLocationId);
		stockRelocation.setFromStoreLocation(storeLocation);
		// 托盘
		StoreContainer storeContainer = storeContainerDao
				.findOne(storeContainerId);
		stockRelocation.setStoreContainer(storeContainer);
		// 客户
		Customer customer = customerDao.findOne(customerId);
		stockRelocation.setCustomer(customer);

		BookInventory bookInventory = bookInventoryDao
				.findByStoreContainer(storeContainerId);
		// 把托盘放入周转区
		List<StoreLocation> storeLocationList = storeLocationDao
				.findByAreasAndcode(storeLocation.getStoreArea().getId(),
						Constants.STORE_LOCATION_TYPE_TURNOVER);

		if (!storeLocationList.isEmpty()) {
			StoreLocation storeLocationzz = storeLocationList.get(0);
			bookInventory.setStoreLocation(storeLocationzz);
		} else {
			throw new RuntimeException("未找到周转区");
		}
		bookInventoryService.update(bookInventory, stockRelocation
				.getFromStoreLocation().getCode());
		stockRelocation.setProduct(bookInventory.getProduct());
		stockRelocation.setAmount(bookInventory.getAmount());
		stockRelocation.setAmountMeasureUnit(bookInventory
				.getAmountMeasureUnit());
		stockRelocation.setWeight(bookInventory.getWeight());
		stockRelocation.setWeightMeasureUnit(bookInventory
				.getWeightMeasureUnit());

		stockRelocation.setStockRelocationStatus(StockRelocationStatus.移位中);
		stockRelocation.setOperator(ThreadLocalHolder.getCurrentOperator());
		stockRelocation.setOperatorTime(new Date());
		stockRelocation.setSerialNo(serialNumberService
				.getSerialNumber(TransactionType.StockRelocation));
		stockRelocation.setGuid(guid);
		stockRelocationDao.save(stockRelocation);
		// 更新储位状态
		// storeLocationDao.updateStoreLocationStatus(stockRelocation.getFromStoreLocation().getId(),StoreLocationStatus.可使用);
		return stockRelocation != null && stockRelocation.getId() != null ? stockRelocation
				: new StockRelocation();
	}

	@Transactional(readOnly = false)
	public void deleteStockRelocation(Long id) {
		stockRelocationDao.delete(id);
	}

	public List<StockRelocation> findStockRelocation(final String serialNo,
			final StockRelocationStatus stockRelocationStatus) {
		Specification<StockRelocation> specification = new Specification<StockRelocation>() {
			@Override
			public Predicate toPredicate(Root<StockRelocation> root,
					CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				Path serialNoPath = root.get("serialNo");
				Path operatorTimePath = root.get("operatorTime");
				Path stockRelocationStatusPath = root
						.get("stockRelocationStatus");
				if (StringUtils.isNotBlank(serialNo)) {
					Predicate p1 = criteriaBuilder.like(serialNoPath, "%"
							+ serialNo + "%");
					list.add(p1);
				}
				if (stockRelocationStatus != null) {
					Predicate p1 = criteriaBuilder.equal(
							stockRelocationStatusPath, stockRelocationStatus);
					list.add(p1);
				}

				Predicate[] p = new Predicate[list.size()];
				criteriaQuery.orderBy((criteriaBuilder.desc(operatorTimePath)));
				return criteriaBuilder.and(list.toArray(p));

			}
		};

		return stockRelocationDao.findAll(specification);

	}

	public StoreLocation checkToStoreLocation(String label) {

		StoreLocation storeLocation = storeLocationDao.findByLabelAndStatus(
				label, new StoreLocationStatus[] { StoreLocationStatus.可使用,
						StoreLocationStatus.预留, StoreLocationStatus.维护 });

		return storeLocation;
	}

	// 生效
	@Transactional(readOnly = false)
	public void toStoreLocation(Long id, String locaionCode) {
		StoreLocation storeLocation = storeLocationDao.findByCodeAndStatus(
				locaionCode, new StoreLocationStatus[] {
						StoreLocationStatus.可使用, StoreLocationStatus.预留,
						StoreLocationStatus.维护 });

		StockRelocation stockRelocation = stockRelocationDao.findOne(id);
		BookInventory bookInventorys = bookInventoryService
				.findByContainerLabel(stockRelocation.getStoreContainer()
						.getLabel());
		bookInventoryService.updatebookInventoryHis(bookInventorys
				.getStoreContainer().getId());

		// storeLocationDao.updateStoreLocationStatus(stockRelocation.getFromStoreLocation().getId(),StoreLocationStatus.可使用);
		bookInventorys.setStoreLocation(storeLocation);
		bookInventoryService.save(bookInventorys,false);

		stockRelocation.setToStoreLocation(storeLocation);
		stockRelocation.setStockRelocationStatus(StockRelocationStatus.完成);
		stockRelocationDao.save(stockRelocation);
	}

	// 生效
	@Transactional(readOnly = false)
	public void toStoreLocation(String guid, String locaionCode) {
		StoreLocation storeLocation = storeLocationDao.findByCodeAndStatus(
				locaionCode, new StoreLocationStatus[] {
						StoreLocationStatus.可使用, StoreLocationStatus.预留,
						StoreLocationStatus.维护 });

		StockRelocation stockRelocation = stockRelocationDao.findByGuid(guid);
		BookInventory bookInventorys = bookInventoryService
				.findByContainerLabel(stockRelocation.getStoreContainer()
						.getLabel());
		bookInventoryService.updatebookInventoryHis(bookInventorys
				.getStoreContainer().getId());

		// storeLocationDao.updateStoreLocationStatus(stockRelocation.getFromStoreLocation().getId(),StoreLocationStatus.可使用);
		bookInventorys.setStoreLocation(storeLocation);
		bookInventoryService.save(bookInventorys,false);

		stockRelocation.setToStoreLocation(storeLocation);
		stockRelocation.setStockRelocationStatus(StockRelocationStatus.完成);
		stockRelocationDao.save(stockRelocation);
	}

}
