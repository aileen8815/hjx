package com.wwyl.service.store;

import java.math.BigDecimal;
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

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Constants;
import com.wwyl.Enums.CommentType;
import com.wwyl.Enums.PaymentType;
import com.wwyl.Enums.StockWastageStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.finance.PaymentLogDao;
import com.wwyl.dao.settings.ChargeTypeDao;
import com.wwyl.dao.settings.CommentDao;
import com.wwyl.dao.store.BookInventoryDao;
import com.wwyl.dao.store.StockWastageDao;
import com.wwyl.dao.store.StockWastageItemDao;
import com.wwyl.dao.finance.PaymentDao;
import com.wwyl.dao.finance.PaymentItemDao;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.finance.PaymentItem;
import com.wwyl.entity.settings.ChargeType;
import com.wwyl.entity.settings.Comment;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.StockWastage;
import com.wwyl.entity.store.StockWastageItem;
import com.wwyl.service.finance.PaymentService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.settings.TaskService;
 

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class StockWastageService {
 
 
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
	private StockWastageDao stockWastageDao;
	@Resource
	private StockWastageItemDao stockWastageItemDao;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private StoreLocationService storeLocationService;
	@Resource
	private PaymentService paymentService;
	@Resource
	private TaskService taskService;
	@Resource
	private CommentDao commentDao;
	
	
	public List<StockWastage> findAll() {
		return stockWastageDao.findAll();
	}

	public StockWastage findOne(Long id) {
		return stockWastageDao.findOne(id);
	}

	public Page<StockWastage> findStockWastageSpecification(int page, int rows, final String serialNo,final Long customerId,final StockWastageStatus stockWastageStatus) {
		Specification<StockWastage> specification = new Specification<StockWastage>() {
			@Override
			public Predicate toPredicate(Root<StockWastage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				Path serialNoPath = root.get("serialNo");
				Path customerPath = root.get("customer").get("id");
				Path inputTimePath = root.get("inputTime");
				Path stockWastageStatusPath = root.get("stockWastageStatus");
				Path paymentStatusPath = root.get("payment").get("paymentStatus");
				if (customerId != null) {
					Predicate p1 = criteriaBuilder.equal(customerPath, customerId);
					list.add(p1);
				}
				if (serialNo != null) {
					Predicate p1 = criteriaBuilder.like(serialNoPath, "%" + serialNo + "%");
					list.add(p1);
				}
				if (stockWastageStatus != null) {
					Predicate p1 = criteriaBuilder.equal(stockWastageStatusPath, stockWastageStatus);
					list.add(p1);
				}
				Predicate[] p = new Predicate[list.size()];
				criteriaQuery.orderBy((criteriaBuilder.desc(inputTimePath)));
				return criteriaBuilder.and(list.toArray(p));
			
			}
		};
		return stockWastageDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}
	
	@Transactional(readOnly = false)
	public Long  save(StockWastage stockWastage) {

		stockWastage.setSerialNo(serialNumberService.getSerialNumber(TransactionType.StockWastage));
		stockWastage.setInputTime(new Date());
		stockWastage.setInputOperator(ThreadLocalHolder.getCurrentOperator());
		stockWastage.setStockWastageStatus(StockWastageStatus.待处理);
		stockWastageDao.save(stockWastage);
		taskService.assignBreakageNotice(stockWastage);
		return stockWastage.getId();
	}
	/**
	 * 提交审核
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public void  commitCheck(Long id) {
		StockWastage stockWastage=stockWastageDao.findOne(id);
		stockWastage.setStockWastageStatus(StockWastageStatus.待审核报损);
		stockWastageDao.save(stockWastage);
	}
	@Transactional(readOnly = false)
	public void  saveStockWastageItem(StockWastageItem stockWastageItemNew,String locationCode) {
/*		BookInventory bookInventory = bookInventoryService.findByLocation(locationCode);

		if (bookInventory != null) {
			stockWastageItemNew.setStoreContainer(bookInventory.getStoreContainer());
			stockWastageItemNew.setStoreLocation(bookInventory.getStoreLocation());
		}else{
			throw new RuntimeException("储位选择错误！");
		}*/
		List<StockWastageItem> stockWastageItemOldList=stockWastageItemDao.findStockWastageItem(new StockWastageStatus[]{StockWastageStatus.已作废,StockWastageStatus.已完成},stockWastageItemNew.getStoreContainer().getId());
		 
		if(!stockWastageItemOldList.isEmpty()){
			throw new RuntimeException("此托盘已有报损记录！");
		}
		stockWastageItemDao.save(stockWastageItemNew);
	}
	

	@Transactional(readOnly = false)
	public void delete(Long id) {
		stockWastageDao.delete(id);
	}
	@Transactional(readOnly = false)
	public void updateItem(StockWastageItem stockWastageItem) {
		stockWastageItemDao.save(stockWastageItem);
	}



	@Transactional(readOnly = false)
	public void deleteItem(Long id) {
		stockWastageItemDao.delete(id);
	}

	public StockWastageItem findOneItem(Long id) {
		return stockWastageItemDao.findOne(id);
	}
	//协商费用
	@Transactional(readOnly = false)
	public void settlement(Long stockWastageId,Long paymentItemId,String  money) {
		if (paymentItemId == null) {
			// 费用单
			StockWastage stockWastage = stockWastageDao.findOne(stockWastageId);
			Payment payment = new Payment();
			ChargeType chargeType = new ChargeType();
			chargeType.setId(Constants.CHARGETYPE_NOW);
			payment.setChargeType(chargeType);
			payment.setPaymentType(PaymentType.正常退费);
			payment.setPaymentStatus(PaymentStatus.未付款);
			payment.setSerialNo(serialNumberService
					.getSerialNumber(TransactionType.Payment));
			payment.setSettledBy(ThreadLocalHolder.getCurrentOperator());
			payment.setSettledTime(new Date());
			payment.setPaymentObjectEntity(StockWastage.class.getSimpleName());
			payment.setPaymentObjectId(stockWastage.getId());
			payment.setPaymentObjectSerialNo(stockWastage.getSerialNo());
			payment.setCustomer(stockWastage.getCustomer());
			
			paymentDao.save(payment);
			stockWastage.setPayment(payment);
			stockWastage.setStockWastageStatus(StockWastageStatus.待审核报损赔偿);
			stockWastageDao.save(stockWastage);

			// 保存费用明细
            PaymentItem paymentItem = new PaymentItem();

            CEFeeItem feeItem = new CEFeeItem();
			feeItem.setId(Constants.CE_FEE_ITEM_BREAKAGE);// 报损费用
			paymentItem.setFeeItem(feeItem);
			paymentItem.setAmount(new BigDecimal(money));
			paymentItem.setActuallyAmount(new BigDecimal(money));
			paymentItem.setPayment(payment);
			paymentItem.setPaymentStatus(PaymentStatus.未付款);
			paymentItem.setPaymentObjectEntity(StockWastage.class.getSimpleName());
			paymentItem.setPaymentObjectId(stockWastage.getId());
			paymentItemDao.save(paymentItem);

			taskService.assignFeeBreakageNotice(stockWastage);
		} else {
			PaymentItem paymentItem = paymentItemDao.findOne(paymentItemId);
			paymentItem.setAmount(new BigDecimal(money));
			paymentItem.setActuallyAmount(new BigDecimal(money));
			paymentItemDao.save(paymentItem);
			StockWastage stockWastage = stockWastageDao.findOne(stockWastageId);
			stockWastage.setStockWastageStatus(StockWastageStatus.待审核报损赔偿);
			stockWastageDao.save(stockWastage);
		}
		
	}

	


	//生效
	@Transactional(readOnly = false)
	public void complete(Long id){
		StockWastage stockWastage=stockWastageDao.findOne(id);
		if(stockWastage.getPayment().getPaymentStatus()==PaymentStatus.已付款||stockWastage.getPayment().getPaymentStatus()==PaymentStatus.已作废){
			throw new RuntimeException("不是可支付状态，不能执行该操作。");		
		}
		paymentService.payment(stockWastage.getPayment(),stockWastage.getCustomer().getPassword(), true);
		stockWastage.setStockWastageStatus(StockWastageStatus.已完成);
		Set<StockWastageItem> stockWastageItems= stockWastage.getStockWastageItems();
		for (StockWastageItem stockWastageItem : stockWastageItems) {
			BookInventory bookInventorys=bookInventoryService.findByLocation(stockWastageItem.getStoreLocation().getCode());
			if(bookInventorys.getAmount()==stockWastageItem.getAmount()){
				bookInventoryService.delete(bookInventorys.getId());
			}else{
				bookInventorys.setAmount(bookInventorys.getAmount()-stockWastageItem.getAmount());
				bookInventorys.setWeight(bookInventorys.getWeight()-stockWastageItem.getWeight());
				bookInventoryService.update(bookInventorys,null);
			}
		}
		stockWastageDao.save(stockWastage);
	}
	//作废
	@Transactional(readOnly = false)
	public void cancel(Long id){
		StockWastage stockWastage=stockWastageDao.findOne(id);
		stockWastage.setStockWastageStatus(StockWastageStatus.已作废);
		stockWastageDao.save(stockWastage);
	}
	@Transactional(readOnly = false)
	public void check(Long id,Long type,String remark){
		StockWastage stockWastage=stockWastageDao.findOne(id);
		stockWastage.setStockWastageStatus((type!=null&&type==1)?StockWastageStatus.已同意报损:StockWastageStatus.已作废);
		stockWastage.setApprover(ThreadLocalHolder.getCurrentOperator());
		stockWastage.setApproveTime(new Date());
		//评论
		Comment comment = new Comment();
		comment.setCommentor(ThreadLocalHolder.getCurrentOperator());
		comment.setCommentTime(new Date());
		comment.setContent(remark);
		comment.setCommentType(CommentType.报损审核);
		commentDao.save(comment);
		stockWastage.getConmments().add(comment);
		stockWastageDao.save(stockWastage);
		taskService.assignBreakageEndNotice(stockWastage);
	}
	@Transactional(readOnly = false)
	public void feeCheck(Long id,Long type,String remark){
		StockWastage stockWastage=stockWastageDao.findOne(id);
		stockWastage.setStockWastageStatus((type!=null&&type==1)?StockWastageStatus.已同意报损赔偿:StockWastageStatus.被驳回报损赔偿);
		stockWastage.setApprover(ThreadLocalHolder.getCurrentOperator());
		stockWastage.setFeeApproveTime(new Date());
		//评论
		Comment comment = new Comment();
		comment.setCommentor(ThreadLocalHolder.getCurrentOperator());
		comment.setCommentTime(new Date());
		comment.setContent(remark);
		comment.setCommentType(CommentType.报损费用审核);
		commentDao.save(comment);
		stockWastage.getConmments().add(comment);
		stockWastageDao.save(stockWastage);
		taskService.assignFeeBreakageEndNotice(stockWastage);
	}
}
