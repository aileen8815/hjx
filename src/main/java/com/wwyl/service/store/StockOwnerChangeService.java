package com.wwyl.service.store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Enums.CommentType;
import com.wwyl.Enums.PaymentType;
import com.wwyl.Enums.StockOwnerChangeStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.finance.PaymentLogDao;
import com.wwyl.dao.settings.ChargeTypeDao;
import com.wwyl.dao.settings.CommentDao;
import com.wwyl.dao.settings.CustomerDao;
import com.wwyl.dao.settings.ProductDao;
import com.wwyl.dao.store.BookInventoryDao;
import com.wwyl.dao.store.StockOwnerChangeDao;
import com.wwyl.dao.store.StockOwnerChangeCheckItemDao;
import com.wwyl.dao.store.StockOwnerChangeRegisterItemDao;
import com.wwyl.dao.finance.ExtraChargeConfigDao;
import com.wwyl.dao.finance.ExtraChargeDao;
import com.wwyl.dao.finance.PaymentDao;
import com.wwyl.dao.finance.PaymentItemDao;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.finance.ExtraCharge;
import com.wwyl.entity.finance.ExtraChargeConfig;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.finance.PaymentItem;
import com.wwyl.entity.finance.PaymentLog;
import com.wwyl.entity.settings.ChargeType;
import com.wwyl.entity.settings.Comment;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.StockOut;
import com.wwyl.entity.store.StockOwnerChange;
import com.wwyl.entity.store.StockOwnerChangeCheckItem;
import com.wwyl.entity.store.StockOwnerChangeRegisterItem;
import com.wwyl.service.finance.PaymentService;
import com.wwyl.service.settings.SerialNumberService;
 

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class StockOwnerChangeService {
 
 
	@Resource
	private BookInventoryDao bookInventoryDao;

	@Resource
	private BookInventoryService bookInventoryService;

	@Resource
	private PaymentDao paymentDao;
	@Resource
	private PaymentItemDao paymentItemDao;
	@Resource
	private ChargeTypeDao chargeTypeDao;
	@Resource
	private PaymentLogDao paymentLogDao;
	@Resource
	private CommentDao commentDao;
	@Resource
	private StockOwnerChangeCheckItemDao stockOwnerChangeCheckItemDao;
	@Resource
	private StockOwnerChangeRegisterItemDao stockOwnerChangeRegisterItemDao;
	@Resource
	private StockOwnerChangeDao stockOwnerChangeDao;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private PaymentService paymentService;
	
	@Resource
	private  ExtraChargeConfigDao  extraChargeConfigDao;
	@Resource
	private ExtraChargeDao extraChargeDao;
	@Resource
	private CustomerDao customerDao;
	@Resource
	private ProductDao productDao;
	public List<StockOwnerChange> findAll() {
		return stockOwnerChangeDao.findAll();
	}

	public StockOwnerChange findOne(Long id) {
		return stockOwnerChangeDao.findOne(id);
	}

	public void update(StockOwnerChange stockOwnerChange) {

		stockOwnerChangeDao.save(stockOwnerChange);

	}
	
	public Page<StockOwnerChange> findStockOwnerChangeSpecification(int page, int rows, final Long sellerId,final Long buyerId,final StockOwnerChangeStatus stockOwnerChangeStatus,final PaymentStatus[]  paymentStatus,final Long customerType) {
		Specification<StockOwnerChange> specification = new Specification<StockOwnerChange>() {
			@Override
			public Predicate toPredicate(Root<StockOwnerChange> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				Path sellerPath = root.get("seller").get("id");
				Path buyerPath = root.get("buyer").get("id");
				Path regTimePath = root.get("regTime");
				Path stockOwnerChangeStatusPath = root.get("stockOwnerChangeStatus");
				Path sellerPaymentStatusPath = root.get("sellerPayment").get("paymentStatus");
				Path buyerPaymentStatusPath = root.get("buyerPayment").get("paymentStatus");
				Path sellerPaymentPath = root.get("sellerPayment");
				Path buyerPaymentPath = root.get("buyerPayment");
				Predicate orPredicate=null;
				if (sellerId != null) {
					Predicate p1 = criteriaBuilder.equal(sellerPath, sellerId);
					list.add(p1);
				}
				if (buyerId != null) {
					Predicate p1 = criteriaBuilder.equal(buyerPath, buyerId);
					list.add(p1);
				}
				if (stockOwnerChangeStatus != null) {
					//查询未结算
					if(stockOwnerChangeStatus==StockOwnerChangeStatus.已选位){
						Predicate p2 = criteriaBuilder.isNull(customerType==0?sellerPaymentPath:buyerPaymentPath);
						list.add(p2);
					}
					Predicate p1 = criteriaBuilder.equal(stockOwnerChangeStatusPath, stockOwnerChangeStatus);
					list.add(p1);
				}
				if (paymentStatus != null&&paymentStatus.length>0) {
						Path	paymentStatusPath=customerType==0?sellerPaymentStatusPath:buyerPaymentStatusPath;
						Predicate p2 = paymentStatusPath.in((Object[])paymentStatus);
						list.add(p2);
					 
					 
				}
				Predicate[] p = new Predicate[list.size()];
				criteriaQuery.orderBy((criteriaBuilder.desc(regTimePath)));
				return criteriaBuilder.and(list.toArray(p));
				 
			}
		};
		return stockOwnerChangeDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}
	
	@Transactional(readOnly = false)
	public Long  create(StockOwnerChange stockOwnerChange, String[] productCheck) {
		
		stockOwnerChange.setSerialNo(serialNumberService.getSerialNumber(TransactionType.StockOwnerChange));
		stockOwnerChange.setChangeTime(new Date());
		stockOwnerChange.setRegister(ThreadLocalHolder.getCurrentOperator());
		stockOwnerChange.setRegTime(new Date());
		stockOwnerChange.setStockOwnerChangeStatus(StockOwnerChangeStatus.待处理);
		stockOwnerChangeDao.save(stockOwnerChange);
		saveRegisterItem(productCheck,stockOwnerChange);

		return stockOwnerChange.getId();
	}
	@Transactional(readOnly = false)
	public Long  update(StockOwnerChange stockOwnerChange, String[] productCheck) {

		StockOwnerChange stockOwnerChangeNew =stockOwnerChangeDao.findOne(stockOwnerChange.getId());
		stockOwnerChangeNew.setSeller(stockOwnerChange.getSeller());
		stockOwnerChangeNew.setBuyer(stockOwnerChange.getBuyer());
		stockOwnerChangeDao.save(stockOwnerChangeNew);
		saveRegisterItem(productCheck,stockOwnerChange);

		return stockOwnerChange.getId();
	}
	 
	public void saveRegisterItem(String[] productCheck,StockOwnerChange stockOwnerChange){
		if (stockOwnerChange.getId() != null) {
			List<StockOwnerChangeRegisterItem> stockOwnerChangeRegisterItems = stockOwnerChangeRegisterItemDao.findBystockOwnerChangeItemid(stockOwnerChange.getId());
			if(!stockOwnerChangeRegisterItems.isEmpty())
			for (StockOwnerChangeRegisterItem stockOwnerChangeRegisrsterItem : stockOwnerChangeRegisterItems) {
				stockOwnerChangeRegisterItemDao.delete(stockOwnerChangeRegisrsterItem.getId());
			}

		}
		StockOwnerChangeRegisterItem item = null;
		for (String productCheckstr : productCheck) {
			String[] productamount = productCheckstr.split("_");
			List<BookInventory> bookInventoryList = bookInventoryService.findBookInventorySpecification(stockOwnerChange.getSeller().getId(),null,null,productamount[1],null,null,null);
			Map<String,BookInventory> bookInventoryMap=bookInventoryService.mergeBookInventorysBybatchs(bookInventoryList);
			BookInventory bookInventory	=bookInventoryMap.get(productamount[0]+"_"+productamount[1]);
			double weight = weightBudget(bookInventory.getAmount(), bookInventory.getWeight(), productamount[2]);
			item = new StockOwnerChangeRegisterItem();
			item.setAmount(Double.valueOf(productamount[2]));
			item.setAmountMeasureUnit(bookInventory.getAmountMeasureUnit());
			item.setStockOwnerChange(stockOwnerChange);
			item.setPacking(bookInventory.getPacking());
			item.setProduct(bookInventory.getProduct());
			item.setSpec(bookInventory.getSpec());
			item.setProductionDate(bookInventory.getProductionDate());
			item.setWeight(weight);
			item.setWeightMeasureUnit(bookInventory.getWeightMeasureUnit());
			item.setQualityGuaranteePeriod(bookInventory.getQuanlityGuaranteePeriod());
			item.setStoreDuration(bookInventory.getStoreDuration());
			item.setBatchs(productamount[0]);
			stockOwnerChangeRegisterItemDao.save(item);
		}
	}
	/**
	 * 预计出库的重量
	 * 
	 * @param countAmount
	 *            库存商品总重量
	 * @param countWeight
	 *            库存商品总重量
	 * @param amount
	 *            本次出库数量
	 * @return
	 */
	public double weightBudget(double countAmount, double countWeight, String amount) {
		BigDecimal b1 = new BigDecimal(Double.toString(countAmount));// 总数量
		BigDecimal b2 = new BigDecimal(Double.toString(countWeight));// 总重量
		BigDecimal b3 = new BigDecimal(amount);
		int DEFAULT_DIV_SCALE = 6;
		BigDecimal b4 = b2.divide(b1, DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
		return (b3.multiply(b4)).doubleValue();
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		stockOwnerChangeDao.delete(id);
	}



	@Transactional(readOnly = false)
	public void deleteItem(Long id) {
		stockOwnerChangeRegisterItemDao.delete(id);
	}

	public StockOwnerChangeRegisterItem findOneItem(Long id) {
		return stockOwnerChangeRegisterItemDao.findOne(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteCheckItem(Long id) {
		stockOwnerChangeCheckItemDao.delete(id);
	}
	/**
	 * 
	 * 
	 * @param ids
	 *            货品ID-托盘ID,货品ID-托盘ID......
	 * @param 
	 */
	@Transactional(readOnly = false)
	public void saveItem(String ids, Long stockOwnerChangeid) {
		String[] strs = ids.trim().split(",");
		StockOwnerChangeCheckItem item = null;
		StockOut stockOut = null;
		StockOwnerChange stockOwnerChange =stockOwnerChangeDao.findOne(stockOwnerChangeid);
		Long customerId = stockOwnerChange.getSeller().getId();
		for (String str : strs) {
			String[] proidAndstoreIds = str.trim().split("-");// [0]商品id,[1]托盘ID
			BookInventory bookInventory = bookInventoryDao.findByProductIdAndstoreContainerId(Long.valueOf(proidAndstoreIds[0]), Long.valueOf(proidAndstoreIds[1]), customerId);
			item =new  StockOwnerChangeCheckItem();
			item.setStockOwnerChange(stockOwnerChange);
			item.setStoreContainer(bookInventory.getStoreContainer());
			item.setStoreLocation(bookInventory.getStoreLocation());
			
			stockOwnerChangeCheckItemDao.save(item);
			stockOwnerChangeDao.updateStatus(StockOwnerChangeStatus.已选位, stockOwnerChangeid);

		}

	}

	@Transactional(readOnly = false)
	public void settlement(Long customerType, Long stockOwnerChangeId,
			Long chargeTypeId, String[] feeitem, String[] money,
			String[] actuallyMoneys, boolean delayed) {
		if (feeitem == null) {
			return;
		}
		// 费用单
		StockOwnerChange stockOwnerChange = stockOwnerChangeDao
				.findOne(stockOwnerChangeId);
		Payment payment = new Payment();
		ChargeType chargeType = chargeTypeDao.findOne(chargeTypeId);
		payment.setChargeType(chargeType);

		if (delayed) {
			payment.setPaymentStatus(PaymentStatus.延付待审核);
		} else {
			payment.setPaymentStatus(PaymentStatus.未付款);
		}
		payment.setPaymentType(PaymentType.正常收费);
		payment.setSerialNo(serialNumberService.getSerialNumber(TransactionType.Payment));
		payment.setSettledBy(ThreadLocalHolder.getCurrentOperator());
		payment.setSettledTime(new Date());
		payment.setPaymentObjectEntity(StockOwnerChange.class.getSimpleName());
		payment.setPaymentObjectId(stockOwnerChange.getId());
		payment.setPaymentObjectSerialNo(stockOwnerChange.getSerialNo());
		if (customerType == 0) {
			payment.setCustomer(stockOwnerChange.getSeller());
			stockOwnerChange.setSellerPayment(payment);
		} else {
			payment.setCustomer(stockOwnerChange.getBuyer());
			stockOwnerChange.setBuyerPayment(payment);
		}
		paymentDao.save(payment);

		stockOwnerChangeDao.save(stockOwnerChange);
		CEFeeItem feeItem = null;
		PaymentItem paymentItem = null;
		for (int i = 0; i < feeitem.length; i++) {
			// 保存费用明细
			paymentItem = new PaymentItem();
			feeItem = new CEFeeItem();
			feeItem.setId(Long.valueOf(feeitem[i]));
			paymentItem.setFeeItem(feeItem);
			paymentItem.setAmount(new BigDecimal(money[i]));
			BigDecimal  actuallyMoney=new BigDecimal(actuallyMoneys[i]);
			paymentItem.setActuallyAmount(actuallyMoney);
			paymentItem.setPayment(payment);
			paymentItem.setPaymentStatus(PaymentStatus.未付款);
			paymentItem.setPaymentObjectEntity(StockOwnerChange.class.getSimpleName());
			paymentItem.setPaymentObjectId(stockOwnerChange.getId());
			paymentItemDao.save(paymentItem);
			//对设置提成比例的费用项添加到
			paymentService.saveExtraCharge(paymentItem,payment.getCustomer(),actuallyMoney);
		}
	}

	 
	@Transactional(readOnly = false)
	public void updateStatus(Long id, StockOwnerChangeStatus stockOwnerChangeStatus) {
		stockOwnerChangeDao.updateStatus(stockOwnerChangeStatus, id);
	}
	
		//付款
		@Transactional(readOnly = false)
		public void  payment(Long id,Long customerType,String aCardMac, String sMemberCode,String password){
			StockOwnerChange stockOwnerChange=stockOwnerChangeDao.findOne(id);
			Payment payment=null;
			if(customerType==0){
				payment=stockOwnerChange.getSellerPayment();
			}else{
				payment=stockOwnerChange.getBuyerPayment();
			}
 
			if(payment!=null){
				paymentService.payment(new Long[]{payment.getId()},aCardMac, sMemberCode,password);
			}

		}
		//延迟付款
		@Transactional(readOnly = false)
		public void  delay(Long id,Long customerType,String remark,PaymentStatus paymentStatus){
			StockOwnerChange stockOwnerChange=stockOwnerChangeDao.findOne(id);
			Payment payment=null;
			if(customerType==0){
				payment=stockOwnerChange.getSellerPayment();
			}else{
				payment=stockOwnerChange.getBuyerPayment();
			}
			if(payment!=null){
				paymentService.delay(payment.getId(), remark, paymentStatus);
			}
		}
	//生效
	@Transactional(readOnly = false)
	public void complete(Long id){
		StockOwnerChange stockOwnerChange=stockOwnerChangeDao.findOne(id);
		stockOwnerChange.getStockOwnerChangeCheckItems();
		Set<StockOwnerChangeCheckItem> stockOwnerChangeCheckItems= stockOwnerChange.getStockOwnerChangeCheckItems();
		Object[] containers=new Object[stockOwnerChangeCheckItems.size()];
		int i=0;
		for (StockOwnerChangeCheckItem stockOwnerChangeCheckItem : stockOwnerChangeCheckItems) {
			containers[i]=stockOwnerChangeCheckItem.getStoreContainer().getId();
			i++;
		}
		List<BookInventory> bookInventorys= bookInventoryDao.findByStoreContainers(stockOwnerChange.getSeller().getId(),containers);
		Customer buyer=stockOwnerChange.getBuyer();
		Set<Product>  products=buyer.getProducts();
		for (BookInventory bookInventory : bookInventorys) {
			bookInventoryService.updatebookInventoryHis(bookInventory.getStoreContainer().getId());
			bookInventory.setCustomer(buyer);
			bookInventory.setStockInTime(new Date());
			bookInventory.setStockInOperator(ThreadLocalHolder.getCurrentOperator());
			//货权转移，新增一个商品，编号按规则产生
			Product product=null;
			for (Product pro : products) {
				if(bookInventory.getProduct().getName().equals(pro.getName())){
					product=pro;
				}
			}
			if(product==null){
				product=new Product();
				product.clone(bookInventory.getProduct());
				
				String productCode=buyer.getProductCode();
				product.setCode(productCode);
				productDao.save(product);
				products.add(product);
				customerDao.save(buyer);
			}
			bookInventory.setProduct(product);
			//customerDao.save(buyer);
			bookInventoryDao.save(bookInventory);
			bookInventoryService.savebookInventoryHis(bookInventory,false);
		}
		stockOwnerChange.setStockOwnerChangeStatus(StockOwnerChangeStatus.已完成);
		stockOwnerChange.setEffectTime(new Date());
		stockOwnerChangeDao.save(stockOwnerChange);
	}
	//取消
	@Transactional(readOnly = false)
	public void cancel(Long id){
		StockOwnerChange stockOwnerChange=stockOwnerChangeDao.findOne(id);
		stockOwnerChange.setStockOwnerChangeStatus(StockOwnerChangeStatus.已取消);
		stockOwnerChange.setEffectTime(new Date());
		stockOwnerChangeDao.save(stockOwnerChange);
	}
	
	public List<Long>  findByStockOwnerChangeStatus(StockOwnerChangeStatus stockOwnerChangeStatus){
		return stockOwnerChangeCheckItemDao.findByStockOwnerChangeStatus(stockOwnerChangeStatus);
	}
}
