package com.wwyl.controller.finance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Constants;
import com.wwyl.Enums.PaymentBoundType;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.PaymentType;
import com.wwyl.Enums.TransactionType;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.finance.AccountChecking;
import com.wwyl.entity.finance.ExtraCharge;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.finance.PaymentItem;
import com.wwyl.service.ce.CEFeeItemService;
import com.wwyl.service.ce.PaymentCalculator;
import com.wwyl.service.finance.PaymentService;
import com.wwyl.service.security.SecurityService;
import com.wwyl.service.settings.ChargeTypeService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.SerialNumberService;

/**
 * @author Administrator
 */

@Controller
@RequestMapping("finance/payment")
public class PaymentController extends BaseController {
	
	@Resource
	PaymentService paymentService;
	@Resource
	CustomerService customerService;
	@Resource
	SecurityService securityService;
	@Resource
	ChargeTypeService  chargeTypeService;
	@Resource
	ProductService  productService;
	@Resource
	PaymentCalculator  paymentCalculator;
    @Resource
    private SerialNumberService serialNumberService;
    @Resource
    private  CEFeeItemService ceFeeItemService;
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("paymentStatus", PaymentStatus.values());
		model.addAttribute("settledBys", securityService.findAllOperators());		
		return indexPage;
	}	
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, String customerId, String settledById, PaymentStatus[] paymentStatus) {
		Page<Payment> payments = paymentService.findPayments(page, rows, customerId, settledById, paymentStatus, null);
		return toEasyUiDatagridResult(payments);
	}	
	

	@RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
	public String view(@PathVariable Long id, ModelMap model) {
		Payment payment = paymentService.findOne(id);
		model.addAttribute("payment", payment);		
		model.addAttribute("paymentItems", payment.getPaymentItems());
		model.addAttribute("paymentId", payment.getId());
		//查询该客户延迟生效的付款单
		if(payment.getPaymentStatus()==PaymentStatus.未付款
				&&payment.getPaymentType()==PaymentType.正常收费
				&&(payment.getTemporary()==null||payment.getTemporary()!=1)){
			List<Payment>  paymentlist= paymentService.findPaymentsBycustomerId(payment.getCustomer().getId(),PaymentStatus.延付已生效);
			model.addAttribute("paymentlist", paymentlist);
		}

		return "/finance/payment_view";
	}

	//============================================应收费用
	@RequestMapping(value = "receivable", method = RequestMethod.GET)
	public String receivable(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("settledBys", securityService.findAllOperators());		
		return "/finance/receivables";
	}	
	
	@RequestMapping(value = "receivablesList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> receivableList(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, String customerId, String settledById) {
		Page<Payment> payments = paymentService.findPayments(page, rows, customerId, settledById,new PaymentStatus[]{ PaymentStatus.未付款,PaymentStatus.延付已拒绝,PaymentStatus.延付已生效}, PaymentType.正常收费);
		return toEasyUiDatagridResult(payments);
	}		
	
	//============================================应付费用
	@RequestMapping(value = "payable", method = RequestMethod.GET)
	public String payable(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("settledBys", securityService.findAllOperators());
		return "/finance/payables";
	}	
	
	@RequestMapping(value = "payablesList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> payableList(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, String customerId, String settledById) {
		Page<Payment> payments = paymentService.findPayments(page, rows, customerId, settledById, new PaymentStatus[]{PaymentStatus.未付款}, PaymentType.正常退费);
		return toEasyUiDatagridResult(payments);
	}		
	//============================================延迟审核
	@RequestMapping(value = "index-delay", method = RequestMethod.GET)
	public String delay(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		return "/finance/delays";
	}	
	
	@RequestMapping(value = "delaylist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> delayList(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, String customerId, String settledById) {
		Page<Payment> payments = paymentService.findPayments(page, rows, customerId, settledById, new PaymentStatus[]{PaymentStatus.延付待审核}, PaymentType.正常收费);
		return toEasyUiDatagridResult(payments);
	}	
	
	@RequestMapping(value="{id}/delay" ,method = RequestMethod.GET)
	public String delay(@PathVariable Long id,String remark,Long  paymentStatusType) { 
		paymentService.delay(id,remark,paymentStatusType==1?PaymentStatus.延付已生效:PaymentStatus.延付已拒绝);
		return "redirect:/finance/payment/index-delay";
	}
	
	@RequestMapping(value="{id}/payment" ,method = RequestMethod.POST)
	public String payment(@PathVariable Long id,Long[] payments,String aCardMac, String sMemberCode,String password,ModelMap model) {
		Map<String,Float>  paymentResult = paymentService.payment(payments,aCardMac,sMemberCode,password);
		model.put("messge", "付款成功，本次付款金额："+paymentResult.get("totalAmount")+"元，客户卡上余额："+paymentResult.get("balance")+"元。");
		return "/succeed";
	}	
	
	//============================================维护临时费用
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add( ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("paymentTypes", PaymentType.正常收费);		
		
		model.addAttribute("chargeTypes", chargeTypeService.findAll());	
		
		model.addAttribute("paymentBoundTypes", PaymentBoundType.values());
		return "/finance/temporary_form";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String create(Payment payment) {
		payment.setSerialNo(serialNumberService.getSerialNumber(TransactionType.Payment));
		payment.setTemporary(1);
		payment.setPaymentStatus(PaymentStatus.未付款);
		payment.setSettledBy(ThreadLocalHolder.getCurrentOperator());
		payment.setSettledTime(new Date());
		paymentService.save(payment);
		return "redirect:/finance/payment/"+payment.getId()+"/view";
	}
 	
	@RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id,ModelMap model) {
		Payment payment = paymentService.findOne(id);
		model.addAttribute("payment", payment);		
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("paymentTypes", PaymentType.values());	
		
		model.addAttribute("chargeTypes", chargeTypeService.findAll());	
		
		//默认取客户商品
		model.addAttribute("products", customerService.findOne(payment.getCustomer().getId()).getProducts());
		model.addAttribute("paymentBoundTypes", PaymentBoundType.values());
		return "/finance/temporary_form";
	}
	@RequestMapping(value = "{id}/save", method = RequestMethod.POST)
	public String update(@PathVariable Long id,Payment payment) {
 
		paymentService.save(payment);
		return "redirect:/finance/payment/"+payment.getId()+"/view";
	}
	
	@RequestMapping(value = "/new-item", method = RequestMethod.GET)
	public String addItem(Long  paymentId,ModelMap model) {
		Payment payment=paymentService.findOne(paymentId);
		model.addAttribute("feeItems", ceFeeItemService.findAll());
		model.addAttribute("paymentId", paymentId);
		//默认缠膜费；
		//Map<String, Object> result =paymentCalculator.calculate(payment, Double.valueOf(0), Double.valueOf(0), 0, ceFeeItemService.findOne(Constants.CE_FEE_ITEM_Shrinkwrap));
		//model.addAttribute("amount", result.get("amount"));
		//model.addAttribute("ruleComment",result.get("ruleComment"));
		
		return "/finance/temporary_item_form";
	}
	@RequestMapping(value = "/save-item", method = RequestMethod.POST)
	public String saveItem(PaymentItem  paymentItem,ModelMap model) {
		paymentItem.setPaymentStatus(PaymentStatus.未付款);
		paymentService.savePaymentItem(paymentItem);
		return "redirect:/finance/payment/"+paymentItem.getPayment().getId()+"/view";
	}
	@RequestMapping(value = "{id}/edit-item", method = RequestMethod.GET)
	public String editItem(@PathVariable Long  id,ModelMap model) {
		PaymentItem paymentItem= paymentService.findOneItem(id);
		model.addAttribute("feeItems", ceFeeItemService.findAll());
		model.addAttribute("paymentTypes", PaymentType.values());		
		model.addAttribute("paymentItem", paymentItem);
		model.addAttribute("paymentId",paymentItem.getPayment().getId());
		return "/finance/temporary_item_form";
	}
	
	@RequestMapping(value = "{id}/save-item", method = RequestMethod.POST)
	public String saveItem(@PathVariable Long id,PaymentItem  paymentItem,ModelMap model) {
		paymentService.savePaymentItem(paymentItem);
		return "redirect:/finance/payment/"+paymentItem.getPayment().getId()+"/view";
	}
	
//	明细单项计费
	@RequestMapping(value = "{id}/calculate-item", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> calculateItem(@PathVariable Long id, Double weight, double amountPiece, int storeContainerCount, Long feeitemId, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();	
		Payment payment=paymentService.findOne(id);
		CEFeeItem feeitem=ceFeeItemService.findOne(feeitemId);
	//	BigDecimal result =paymentCalculator.calculate(payment, weight, amountPiece, storeContainerCount, feeitem);
		result =paymentCalculator.calculate(payment, weight, amountPiece, storeContainerCount, feeitem);
		model.addAttribute("amount", result.get("amount"));
		model.addAttribute("ruleComment",result.get("ruleComment"));
		return result;
	}
	
	@RequestMapping(value = "{id}/del-item", method = RequestMethod.GET)
	public String delPaymentItem(@PathVariable Long id,Long paymentId) {
		paymentService.delPaymentItem(id);
		return "redirect:/finance/payment/"+paymentId+"/view";
	}
	//============================================客户押金查询  并统计总数
	@RequestMapping(value = "query-guarantee-money", method = RequestMethod.GET)
	public String guaranteeMoney( Long customerId,ModelMap model) {
		if(customerId!=null){
			List<PaymentItem>	 paymentItems=paymentService.findByfeeItemIdAndcustomerId(Constants.CE_FEE_ITEM_GUARANTEEMONEY,customerId);
			model.addAttribute("customerId", customerId);
			model.addAttribute("paymentItems",  paymentItems);
			BigDecimal totalMoney=paymentService.countGuaranteeMoney(customerId);
			model.addAttribute("totalMoney",  totalMoney);
			
		}
		model.addAttribute("customers", customerService.findAll());
		return "/finance/guarantee_money";
	}
	//============================================对账单查询
	@RequestMapping(value="account-checking",method = RequestMethod.GET)
	public String checkIndex(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("paymentStatus", PaymentStatus.values());
		return "/finance/account_checking";
	}	
	
	@RequestMapping(value = "account-checking-list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> chelist(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows, Long  customerId,PaymentStatus paymentStatus) {
		Page<AccountChecking> accountCheckings = paymentService.findAccountCheckings(page, rows, customerId, paymentStatus);
		return toEasyUiDatagridResult(accountCheckings);
	}	
	

	@RequestMapping(value = "/{id}/account-checking-view", method = RequestMethod.GET)
	public String accountCheckingView(@PathVariable Long id, ModelMap model) {
		AccountChecking accountChecking  = paymentService.findOneAccountChecking(id);
		if(accountChecking.isOverdueFine()){
			if (accountChecking.getOverdueTime()==null||(accountChecking.getOverdueTime() != null
					&& !DateUtils.isSameDay(accountChecking.getOverdueTime(),
							new Date()))) {
					paymentService.overdueFine(accountChecking);
			}
		}
		model.addAttribute("accountChecking", accountChecking);		
		return "/finance/account_checking_view";
	}
	
	@RequestMapping(value = "{id}/edit-account-checking-item", method = RequestMethod.GET)
	public String editAccountCheckingItem(@PathVariable Long  id,Long accountCheckingId, ModelMap model) {
		PaymentItem paymentItem= paymentService.findOneItem(id);
		model.addAttribute("feeItems", ceFeeItemService.findAll());
		model.addAttribute("paymentTypes", PaymentType.values());		
		model.addAttribute("paymentItem", paymentItem);
		model.addAttribute("paymentId",paymentItem.getPayment().getId());
		model.addAttribute("accountCheckingId",accountCheckingId);
		return "/finance/temporary_account_checking_item_form";
	}
	
	@RequestMapping(value = "{id}/save-account-checking-item", method = RequestMethod.POST)
	public String saveAccountCheckingItem(@PathVariable Long id,PaymentItem  paymentItem,Long accountCheckingId, ModelMap model) {
		paymentService.savePaymentItem(paymentItem);
		AccountChecking accountChecking  = paymentService.findOneAccountChecking(accountCheckingId);
		model.addAttribute("accountChecking", accountChecking);	
		return "/finance/account_checking_view";
	}
	
	/**
	 * 违约金的是否收取
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}/overduefine", method = RequestMethod.GET)
	public String overdueFine(@PathVariable Long id, boolean  overduefine,ModelMap model) {
		AccountChecking accountChecking  = paymentService.findOneAccountChecking(id);
		accountChecking.setOverdueFine(overduefine);
		paymentService.saveAccountChecking(accountChecking);
		return "redirect:/finance/payment/"+id+"/account-checking-view";
	}
	@RequestMapping(value = "/{id}/payment-account", method = RequestMethod.GET)
	public String paymentAccount(@PathVariable Long id, ModelMap model) {
		AccountChecking accountChecking  = paymentService.findOneAccountChecking(id);
		paymentService.payAccountChecking(accountChecking);
		return "redirect:/finance/payment/"+id+"/account-checking-view";
	}
	@RequestMapping(value = "/{id}/payment-complete", method = RequestMethod.GET)
	public String paymentComplete(@PathVariable Long id, ModelMap model) {
		AccountChecking accountChecking  = paymentService.findOneAccountChecking(id);
		paymentService.accountCheckingComplete(accountChecking);
		return "redirect:/finance/payment/"+id+"/account-checking-view";
	}
	//============================================搬运费提成费用查询
	@RequestMapping(value="extra-charge",method = RequestMethod.GET)
	public String extraChargeIndex(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		return "/finance/extra_charge";
	}	
	
	@RequestMapping(value = "extra-charge-list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> extraChargeList(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows, Long  customerId,String serialNo) {
		Page<ExtraCharge> extraCharges = paymentService.findExtraCharges(customerId,serialNo,page, rows);
		return toEasyUiDatagridResult(extraCharges);
	}	
	
	//撤销收费
	@RequestMapping(value="{id}/cancel-payment" ,method = RequestMethod.GET)
	public String CancelPayment(@PathVariable Long id) {
		paymentService.CancelPayment(id);
		return "redirect:/finance/payment";
	}	
	
	//===========================================退款单据
	
	@RequestMapping(value = "return-fee", method = RequestMethod.GET)
	public String returnFee(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("paymentStatus", PaymentStatus.values());
		model.addAttribute("settledBys", securityService.findAllOperators());
		return "/finance/payment_return";
	}
	
	@RequestMapping(value = "returnsList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> returnsList(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, String customerId, String settledById, PaymentStatus[] paymentStatus) {
		Page<Payment> payments = paymentService.findPayments(page, rows, customerId, settledById, paymentStatus, null);
		return toEasyUiDatagridResult(payments);
	}		
	
	
	@RequestMapping(value = "/payment-return/new", method = RequestMethod.GET)
	public String addReturn( ModelMap model) {
		model.addAttribute("customers", customerService.findAll());	
		model.addAttribute("paymentTypes", new PaymentType[]{PaymentType.正常退费,PaymentType.撤销收费,PaymentType.撤销退费});	
		
		model.addAttribute("chargeTypes", chargeTypeService.findAll());	
		
		model.addAttribute("paymentBoundTypes", PaymentBoundType.values());
		return "/finance/temporary_form";
	}
}
