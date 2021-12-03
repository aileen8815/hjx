package com.wwyl.service.store;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
 

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.wwyl.Constants;
import com.wwyl.Enums.PaymentType;
import com.wwyl.Enums.StockInStatus;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.CustomerDao;
import com.wwyl.dao.store.InboundReceiptItemDao;
import com.wwyl.dao.store.InboundRegisterDao;
import com.wwyl.dao.finance.ExtraChargeConfigDao;
import com.wwyl.dao.finance.ExtraChargeDao;
import com.wwyl.dao.finance.PaymentDao;
import com.wwyl.dao.finance.PaymentItemDao;
import com.wwyl.dao.store.StockInDao;
import com.wwyl.entity.ce.CEFeeItem;
 
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.finance.PaymentItem;
import com.wwyl.entity.settings.ChargeType;
 
import com.wwyl.entity.settings.StoreContainer;
 
import com.wwyl.entity.store.InboundReceiptItem;
import com.wwyl.entity.store.InboundRegister;
import com.wwyl.entity.store.StockIn;
import com.wwyl.service.finance.PaymentService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreContainerService;
import com.wwyl.service.settings.TaskService;

/**
 * @author jianl
 */
@Service
@Transactional(readOnly = true)
public class InboundReceiptService {

	@Resource
	private InboundReceiptItemDao inboundReceiptItemDao;
	@Resource
	private PaymentDao paymentDao;
	@Resource
	private PaymentItemDao paymentItemDao;
	@Resource
	private PaymentService paymentService;
	 
	@Resource
	private StockInDao stockInDao;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private StoreContainerService storeContainerService;
	@Resource
	private InboundRegisterDao inboundRegisterDao;
	@Resource
	private CustomerDao customerDao;
	@Resource
	private TaskService taskService;
	@Resource
	private ExtraChargeConfigDao extraChargeConfigDao;
	@Resource 
	private ExtraChargeDao  extraChargeDao;
	@Resource 
	private StockInService stockInService;
	
	@Resource 
	private BookInventoryService bookInventoryService;
	
	@Transactional(readOnly = false)
	public void settlement(InboundRegister inboundRegister,Double[] weight, Double[] amountPiece, int[] storeContainerCount,
			Long[] feeitem,String[] moneys, String[] actuallyMoneys, Long chargeTypeId,
			boolean delayed, String[] ruleComments) {
		Assert.notNull(inboundRegister);

		Payment payment = new Payment();
		ChargeType chargeType = new ChargeType();
		chargeType.setId(chargeTypeId);
		payment.setChargeType(chargeType);
		payment.setCustomer(inboundRegister.getCustomer());
		if (delayed) {
			payment.setPaymentStatus(PaymentStatus.延付待审核);
		} else {
			payment.setPaymentStatus(PaymentStatus.未付款);
		}
		payment.setPaymentType(PaymentType.正常收费);
		payment.setSettledBy(ThreadLocalHolder.getCurrentOperator());
		payment.setSerialNo(serialNumberService.getSerialNumber(TransactionType.Payment));
		payment.setSettledTime(new Date());
		payment.setPaymentObjectEntity(InboundRegister.class.getSimpleName());
		payment.setPaymentObjectId(inboundRegister.getId());
		payment.setPaymentObjectSerialNo(inboundRegister.getSerialNo());
		paymentDao.save(payment);

		for (int i = 0; i < feeitem.length; i++) {
			// 保存费用明细
			PaymentItem paymentItem = new PaymentItem();
			CEFeeItem feeItem = new CEFeeItem();
			feeItem.setId(feeitem[i]);
			paymentItem.setWeight((weight[i]));
			paymentItem.setAmountPiece((amountPiece[i]));
			paymentItem.setStoreContainerCount(storeContainerCount[i]);
			paymentItem.setFeeItem(feeItem);
			paymentItem.setAmount(new BigDecimal(moneys[i]));
			paymentItem.setPayment(payment);
			paymentItem.setPaymentStatus(PaymentStatus.未付款);
			BigDecimal actuallyMoney=new BigDecimal(actuallyMoneys[i]);
			paymentItem.setActuallyAmount(actuallyMoney);
			paymentItem.setPaymentObjectEntity(InboundRegister.class.getSimpleName());
			paymentItem.setPaymentObjectId(inboundRegister.getId());
			if (feeitem[i].equals(Constants.CE_FEE_ITEM_GUARANTEEMONEY)) {
				inboundRegister.setGuaranteeMoney(new BigDecimal(actuallyMoneys[i]));
			}

			paymentItem.setRuleComment(ruleComments[i]);
			
			paymentItemDao.save(paymentItem);
			//对设置提成比例的费用项添加到
			paymentService.saveExtraCharge(paymentItem,payment.getCustomer(),actuallyMoney);

		}
		inboundRegister.setPayment(payment);
		inboundRegisterDao.save(inboundRegister);
		if (delayed) {// 发送延迟支付申请通知
			taskService.assignDelayedFilingNotice(payment);
		}

	}

	@Transactional(readOnly = false)
	public void deleteItem(Long id) {
		StockIn stockIn=	stockInDao.findStockInByreceiptId(id, StockInStatus.待上架);
		if(stockIn!=null){
			stockInDao.delete(stockIn.getId());
			inboundReceiptItemDao.delete(id);
		} 
		
	}

	public InboundReceiptItem findOneItem(Long id) {
		return inboundReceiptItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveItem(InboundReceiptItem inboundReceiptItem) {
		if (inboundReceiptItem.getPacking() != null && inboundReceiptItem.getPacking().getId() == null) {
			inboundReceiptItem.setPacking(null);
		}	
		inboundReceiptItemDao.save(inboundReceiptItem);
		stockInService.saveInboundStockIn(inboundReceiptItem);
	/*	//将已清点的商品添加到客户历史商品列表
		Customer customer=inboundReceiptItem.getInboundRegister().getCustomer();
		Set<Product> products=customer.getProducts();
		if(products!=null&&!products.contains(inboundReceiptItem.getProduct())){
				products.add(inboundReceiptItem.getProduct());
				customer.setProducts(products);
				customerDao.save(customer);
			 
		}*/
	}

	@Transactional(readOnly = false)
	public void updateItem(InboundReceiptItem inboundReceiptItem) {
		inboundReceiptItemDao.save(inboundReceiptItem);

	}

	public InboundReceiptItem getLastInboundReceiptItemByContainerCode(String containerCode) {
		StoreContainer storeContainer = storeContainerService.findStoreContainerByLabel(containerCode);
		if (storeContainer == null) {
			return null;
		}
		Page<InboundReceiptItem> items = inboundReceiptItemDao.getLastInboundReceiptItemByContainerCode(storeContainer.getId(), RepoUtils.buildPageRequest(1, 1));
		if (CollectionUtils.isNotEmpty(items.getContent())) {
			return items.getContent().get(0);
		}
		return null;
	}
	
	public List<InboundReceiptItem>  findByInboundRegisterAndtarryArea(Long inboundRegisterid){
		return inboundReceiptItemDao.findByInboundRegisterAndtarryArea(inboundRegisterid);
	}
	
	
	public List<StoreContainer>  findByUnfinished(){
		return inboundReceiptItemDao.findByUnfinished();
	}
	
	public List<InboundReceiptItem>  findInboundReceiptItemByRegisterIdAndProductId(Long inboundRegisterid, Long productid){
		return inboundReceiptItemDao.findInboundReceiptItemByRegisterIdAndProductId(inboundRegisterid, productid);
	}
	
}
