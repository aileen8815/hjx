package com.wwyl.service.store;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.wwyl.Enums;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Constants;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.Enums.StockInStatus;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.StoreLocationDao;
import com.wwyl.dao.store.BookInventoryHisDao;
import com.wwyl.dao.store.InboundRegisterDao;
import com.wwyl.dao.store.StockInDao;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.settings.ProductStatus;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.InboundReceiptItem;
import com.wwyl.entity.store.InboundRegister;
import com.wwyl.entity.store.StockIn;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.TaskService;

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class StockInService {

    @Resource
    private StockInDao stockInDao;
    @Resource
    private InboundRegisterDao inboundRegisterDao;
    @Resource
    private BookInventoryService bookInventoryService;
    @Resource
    private StoreLocationDao storeLocationDao;
    @Resource
    private BookInventoryHisDao bookInventoryHisDao;
    @Resource
    private TaskService taskService;
    @Resource
    private SerialNumberService serialNumberService;

    public List<StockIn> findAll() {
        return stockInDao.findAll();
    }

    public StockIn findOne(Long id) {
        return stockInDao.findOne(id);
    }

    @Transactional(readOnly = false)
    public void save(StockIn stockIn) {
        stockInDao.save(stockIn);
    }

    public Page<StockIn> findStockInItems(int page, int rows, final String serialNo, final StockInStatus stockInStatus, final Long customerId) {
        Specification<StockIn> specification = new Specification<StockIn>() {
            @Override
            public Predicate toPredicate(Root<StockIn> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                Path SerialNoPath = root.get("inboundRegister").get("serialNo");
                Path customerPath = root.get("inboundRegister").get("customer").get("id");
                Path stockInStatusPath = root.get("stockInStatus");
                //	Path bookTime=	root.get("bookTime");
                if (customerId != null) {
                    Predicate p1 = criteriaBuilder.equal(customerPath, customerId);
                    list.add(p1);
                }
                if (stockInStatus != null) {
                    Predicate p1 = criteriaBuilder.equal(stockInStatusPath, stockInStatus);
                    list.add(p1);
                }
                if (serialNo != null && serialNo.trim().length() > 0) {
                    Predicate p2 = criteriaBuilder.like(SerialNoPath, "%" + serialNo + "%");
                    list.add(p2);
                }

                if (Long.class != criteriaQuery.getResultType()) {
                    root.fetch("inboundReceiptItem");
                    root.fetch("inboundRegister");
                }
                Predicate[] p = new Predicate[list.size()];
                criteriaQuery.orderBy((criteriaBuilder.desc(root.get("serialNo"))));
                return criteriaBuilder.and(list.toArray(p));
            }
        };
        return stockInDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
    }

    /**
     * @param stockInId ?????????id
     */
    @Transactional(readOnly = false)
    public StockIn completePickup(Long stockInId, Long storeLocationId) {
        StockIn stockIn = findOne(stockInId);
        if (stockIn == null) {
            throw new RuntimeException("???????????????");
        }

        if (StockInStatus.?????????.compareTo(stockIn.getStockInStatus()) <= 0) {
            throw new RuntimeException("?????????????????????????????????????????????");
        }

        // ???????????????????????????????????????
        BookInventory oldBookInventory = bookInventoryService.findByContainer(stockIn.getInboundReceiptItem().getStoreContainer().getId());
        if (oldBookInventory != null) {
            throw new RuntimeException("????????????????????????????????????????????????");
        }

        StoreLocation storeLocation = storeLocationDao.findOne(storeLocationId);
        if (storeLocation == null) {
            throw new RuntimeException("?????????????????????");
        }

        if (Enums.StoreLocationStatus.??????.equals(storeLocation.getStoreLocationStatus())) {
            throw new RuntimeException("???????????????????????????????????????");
        }

        // ???????????????????????????????????????(2????????????????????????)
        if (!StringUtils.equals("2", storeLocation.getStoreLocationType().getCode()) 
        		&& bookInventoryService.findByLocation(storeLocation.getCode()) != null) {
            throw new RuntimeException("???????????????????????????????????????????????????");
        }

        // ?????????????????????
        stockIn.setStockInStatus(StockInStatus.?????????);
        stockIn.setStockInEndTime(new Date());
        stockIn.setStockInOperator(ThreadLocalHolder.getCurrentOperator());
        stockIn.setStoreLocation(storeLocation);

        InboundRegister inboundRegister = stockIn.getInboundRegister();


        // ????????????
        BookInventory bookInventory = new BookInventory();
        bookInventory.setAmount(stockIn.getInboundReceiptItem().getAmount());
        bookInventory.setAmountMeasureUnit(stockIn.getInboundReceiptItem().getAmountMeasureUnit());
        bookInventory.setCustomer(inboundRegister.getCustomer());
        bookInventory.setInboundRegisterSerialNo(inboundRegister.getSerialNo());
        bookInventory.setPacking(stockIn.getInboundReceiptItem().getPacking());
        bookInventory.setProduct(stockIn.getInboundReceiptItem().getProduct());
        bookInventory.setProductionDate(stockIn.getInboundReceiptItem().getProductionDate());
        bookInventory.setProductionPlace(stockIn.getInboundReceiptItem().getProductionPlace());
        bookInventory.setQualified(stockIn.getInboundReceiptItem().isQualified());
        bookInventory.setQuanlityGuaranteePeriod(stockIn.getInboundReceiptItem().getQualityGuaranteePeriod());
        bookInventory.setSpec(stockIn.getInboundReceiptItem().getSpec());
        bookInventory.setStockIn(stockIn);
        bookInventory.setStockInOperator(ThreadLocalHolder.getCurrentOperator());
        bookInventory.setStockInTime(stockIn.getStockInStartTime());
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        bookInventory.setSettledTime(stockIn.getInboundReceiptItem().getReceiptTime());
        bookInventory.setStoreContainer(stockIn.getInboundReceiptItem().getStoreContainer());
        bookInventory.setStoreDuration(stockIn.getInboundReceiptItem().getStoreDuration());
        bookInventory.setStoreLocation(stockIn.getStoreLocation());
        bookInventory.setWeight(stockIn.getInboundReceiptItem().getWeight());
        bookInventory.setWeightMeasureUnit(stockIn.getInboundReceiptItem().getWeightMeasureUnit());
        bookInventory.setProductDetail(stockIn.getInboundReceiptItem().getProductDetail());
        //??????
        bookInventory.setReadCode(stockIn.getInboundReceiptItem().getReadCode());
        ProductStatus productStatus = new ProductStatus();
        productStatus.setId(Constants.PRODUCT_STATUS_QUALIFIED);
        bookInventory.setProductStatus(productStatus);

        stockInDao.save(stockIn);
        // ?????????????????????????????????
        Set<StockIn> stockInList = inboundRegister.getStockIns();
        boolean result = true;
        for (StockIn stockIn2 : stockInList) {
            if (stockIn2.getStockInStatus() != StockInStatus.????????? && stockIn2.getStockInStatus() != StockInStatus.?????????) {
                result = false;
                break;
            }
        }
        // false:?????????, ?????????; true:?????????, ?????????
        if (result) {
        	Payment payment = inboundRegister.getPayment();
            if(inboundRegister.getStockInStatus()==StockInStatus.?????????){
            	if (payment != null && (payment.getPaymentStatus().equals(PaymentStatus.?????????) || payment.getPaymentStatus().equals(PaymentStatus.???????????????))) {
	
	                inboundRegister.setStockInStatus(StockInStatus.?????????);
	            } else{
	                inboundRegister.setStockInStatus(StockInStatus.?????????);
	                //?????????????????????
	                taskService.assignStockInEndNotice(inboundRegister);
	            }
            }
        } else {
        	if(inboundRegister.getStockInStatus()==StockInStatus.?????????){
        		inboundRegister.setStockInStatus(StockInStatus.?????????);
        	}
        }
        inboundRegisterDao.save(inboundRegister);
        bookInventoryService.save(bookInventory,true);
        return stockIn;
    }

    public List<StockIn> findUnclaimStockIn(Long storeAreaID) {
        return stockInDao.findUnclaimStockIn(new StockInStatus[]{StockInStatus.?????????, StockInStatus.?????????}, storeAreaID);
    }

    public void saveInboundStockIn(InboundReceiptItem inboundReceiptItem) {
        /*Set<InboundReceiptItem> inboundReceiptItemList = inboundRegister.getInboundReceiptItems();
        StockIn stockIn = null;
		for (InboundReceiptItem inboundReceiptItem2 : inboundReceiptItemList) {*/
        StockIn stockIn = new StockIn();
        stockIn.setSerialNo(serialNumberService.getSerialNumber(TransactionType.StockIn));
        stockIn.setInboundRegister(inboundReceiptItem.getInboundRegister());
        stockIn.setStockInStatus(StockInStatus.?????????);
        stockIn.setInboundReceiptItem(inboundReceiptItem);
        stockIn.setStoreArea(inboundReceiptItem.getStoreArea());
        stockIn.setCustomer(inboundReceiptItem.getInboundRegister().getCustomer());
        stockIn.setStockInStartTime(inboundReceiptItem.getReceiptTime());
        stockInDao.save(stockIn);
    }

}
