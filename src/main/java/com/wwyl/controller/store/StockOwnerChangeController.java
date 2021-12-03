package com.wwyl.controller.store;

 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

 
import com.wwyl.Enums.PaymentStatus;
 
import com.wwyl.Enums.StockOwnerChangeStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.ce.CalculatedResult;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.StockOwnerChange;
 
import com.wwyl.entity.store.StockOwnerChangeRegisterItem;
import com.wwyl.service.settings.ChargeTypeService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.StockOutService;
import com.wwyl.service.store.StockOwnerChangeService;
import com.wwyl.service.ce.StoreCalculator;
import com.wwyl.service.finance.PaymentService;

/**
 *	货权转移
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/store/stock-owner-change")
public class StockOwnerChangeController extends BaseController {

	@Resource
	StoreAreaService storeAreaService;
	@Resource
	CustomerService customerService;
	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private PaymentService paymentService;
	@Resource
	private ChargeTypeService chargeTypeService;
	@Resource
	private StockOwnerChangeService stockOwnerChangeService;
	@Resource
	private StoreCalculator storeCalculator;
	@Resource
	private StockOutService stockOutService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(Long customerType,Long operationType,ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("customerType", customerType);
		model.addAttribute("operationType", operationType);
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
		 final Long sellerId,final Long buyerId,@RequestParam(value = "operationType", defaultValue = "0")   Long operationType,Long customerType) {
		//1，结算，2，延迟付款审核 3，付款，
		PaymentStatus[] paymentStatus=null;
		StockOwnerChangeStatus stockOwnerChangeStatus=null;
		if(operationType==1){
			stockOwnerChangeStatus=StockOwnerChangeStatus.已选位;
		}else if(operationType==2){
			paymentStatus=new PaymentStatus[]{PaymentStatus.延付待审核};
		}else if(operationType==3){
			paymentStatus=new PaymentStatus[]{PaymentStatus.未付款,PaymentStatus.延付已生效,PaymentStatus.延付已拒绝};
		}
		Page<StockOwnerChange> stockOwnerChanges = stockOwnerChangeService.findStockOwnerChangeSpecification(page, rows, sellerId,buyerId,stockOwnerChangeStatus,paymentStatus,customerType);
		return toEasyUiDatagridResult(stockOwnerChanges);
	}

	@RequestMapping(value = "/bookinventory-list", method = RequestMethod.GET)
	@ResponseBody
	public List<BookInventory> bookInventoryList(Long customerId,Long selectCustomerId,Long stockOwnerChangeId, ModelMap model) {
		customerId=selectCustomerId!=null?selectCustomerId:customerId;
		if(customerId==null){
			customerId=-1L;
		}
		//设置转让的商品数量
		List<BookInventory>	bookInventoryList = bookInventoryService.findBookInventoryInfo(customerId,null,null,null,null,null);
	 
			if(stockOwnerChangeId!=null){
				//编辑
				Set<StockOwnerChangeRegisterItem> stockOwnerChangeRegisterItemSet=	stockOwnerChangeService.findOne(stockOwnerChangeId).getStockOwnerChangeRegisterItems();
				for (BookInventory bookInventory : bookInventoryList) {
					for (StockOwnerChangeRegisterItem stockOwnerChangeRegisterItem : stockOwnerChangeRegisterItemSet) {
						if(bookInventory.getBatchProduct().equals(stockOwnerChangeRegisterItem.getBatchProduct())){
							bookInventory.setOutboundAmount(stockOwnerChangeRegisterItem.getAmount()); 
						}
					}
				}
			}
			return  bookInventoryList;
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id, Long customerType,Long operationType,ModelMap model) {
		
		
		StockOwnerChange stockOwnerChange = stockOwnerChangeService.findOne(id);
		model.addAttribute("stockOwnerChange", stockOwnerChange);
		model.addAttribute("customerType", customerType);
		//0,货权转移单，1结算，2延迟付款 3，付款，
		model.addAttribute("operationType", operationType);
		return viewPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		return "/store/stock_owner_change_registerItem_form";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("stockOwnerChange") @Valid StockOwnerChange stockOwnerChange, BindingResult result,String[] productCheck, ModelMap model) {
		 
		Long stockOwnerChangeId=stockOwnerChangeService.create(stockOwnerChange,productCheck);
		return "redirect:/store/stock-owner-change/" + stockOwnerChangeId;
	}
	@RequestMapping(value = "/{id}" , method = RequestMethod.POST)
	public String update(@PathVariable Long id,@ModelAttribute("stockOwnerChange") @Valid StockOwnerChange stockOwnerChange, BindingResult result,String[] productCheck, ModelMap model) {
		 
		Long stockOwnerChangeId=stockOwnerChangeService.update(stockOwnerChange,productCheck);
		return "redirect:/store/stock-owner-change/" + stockOwnerChangeId;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		stockOwnerChangeService.delete(id);
		return indexRedirect;
	}
	@RequestMapping(value = "/{id}/delete-check-item", method = RequestMethod.GET)
	public String deleteCheckItem(@PathVariable Long id,Long stockOwnerChangeId, ModelMap model) {
		stockOwnerChangeService.deleteCheckItem(id);
		return "redirect:/store/stock-owner-change/" + stockOwnerChangeId;
	}
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		StockOwnerChange stockOwnerChange = stockOwnerChangeService.findOne(id);
		
		StringBuffer buffer=new StringBuffer();
		Set<StockOwnerChangeRegisterItem> stockOwnerChangeRegisterItems	=	stockOwnerChange.getStockOwnerChangeRegisterItems();
		int i=0;
		int size=stockOwnerChangeRegisterItems.size();
		for (StockOwnerChangeRegisterItem stockOwnerChangeRegisterItem : stockOwnerChangeRegisterItems) {
			i++;
			buffer.append(stockOwnerChangeRegisterItem.getProduct().getId()+":"+stockOwnerChangeRegisterItem.getAmount());
			if(i!=size){
				buffer.append(",");
			}
		}
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("productCheck",buffer.toString());
		model.addAttribute("stockOwnerChange", stockOwnerChange);
		return "/store/stock_owner_change_registerItem_form";
	}

/*	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("outboundRegister") @Valid StockOwnerChange outboundRegister, BindingResult result, String[] productCheck,ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			List<ObjectError> errros = result.getAllErrors();
			for (ObjectError err : errros) {
				System.out.println(err.getDefaultMessage());
			}
			return formPage;
		}
		outboundRegister.setRegisterTime(new Date());
		outboundRegister.setRegisterOperator(ThreadLocalHolder.getCurrentOperator());
		stockOwnerChangeService.create(outboundRegister, productCheck);
		return "redirect:/store/stock-owner-change/" + outboundRegister.getId();
	}*/
	//完成
	@RequestMapping(value="/complete" ,method = RequestMethod.GET)
	public String complete(Long id) {
			stockOwnerChangeService.complete(id);
		return "redirect:/store/stock-owner-change/"+id;
	}
	//取消
	@RequestMapping(value="/cancel" ,method = RequestMethod.GET)
	public String cancel(Long id) {
		stockOwnerChangeService.cancel(id);
		return "redirect:/store/stock-owner-change/"+id;
	}
 
	//分派托盘
	@RequestMapping(value = "/save-item", method = RequestMethod.GET)
	public String saveItem(String ids, Long stockOwnerChangeId, ModelMap model) {
		stockOwnerChangeService.saveItem(ids,stockOwnerChangeId);
 
		return "redirect:/store/stock-owner-change/" + stockOwnerChangeId;
	}

	@RequestMapping(value = "/index-new-item", method = RequestMethod.GET)
	public String indexNewItem(Long stockOwnerChangeId, ModelMap model) {
		model.put("stockOwnerChangeId", stockOwnerChangeId);
		Set<StockOwnerChangeRegisterItem> stockOwnerChangeRegisterItems = stockOwnerChangeService.findOne(stockOwnerChangeId).getStockOwnerChangeRegisterItems();
		double amount=0;
		double weight=0;
		for (StockOwnerChangeRegisterItem stockOwnerChangeRegisterItem : stockOwnerChangeRegisterItems) {
			amount+=stockOwnerChangeRegisterItem.getAmount();
			weight+=stockOwnerChangeRegisterItem.getWeight();
		}
	 
		model.addAttribute("amount", amount);
		model.addAttribute("weight", weight);
		return "/store/stock_owner_change_checkItem_add";
	}
	@RequestMapping(value = "/item-list", method = RequestMethod.GET)
	@ResponseBody
	public List<BookInventory> itemList(Long stockOwnerChangeId, ModelMap model) {
		StockOwnerChange stockOwnerChange = stockOwnerChangeService.findOne(stockOwnerChangeId);
		Set<StockOwnerChangeRegisterItem> stockOwnerChangeRegisterItems = stockOwnerChange.getStockOwnerChangeRegisterItems();
		 
		List<Long> storeContainers = new ArrayList<Long>();
		List<Long> productids = new ArrayList<Long>();
		List<BookInventory> bookInventoryList = null;
		if (!stockOwnerChangeRegisterItems.isEmpty()) {
			for (StockOwnerChangeRegisterItem stockOwnerChangeRegisterItem : stockOwnerChangeRegisterItems) {
				productids.add(stockOwnerChangeRegisterItem.getProduct().getId());
			}
			//货权转移已选位的托盘
			List<Long>  checkedStoreContainers=stockOwnerChangeService.findByStockOwnerChangeStatus(StockOwnerChangeStatus.已选位);
			if(!checkedStoreContainers.isEmpty()){
				storeContainers.addAll(checkedStoreContainers);
			}
			//已存在未完成的拣货单
			List<Long>  stockOutStoreContainers=stockOutService.findUseingStockOut(productids.toArray(),stockOwnerChange.getSeller().getId());
			if(!stockOutStoreContainers.isEmpty()){
				storeContainers.addAll(stockOutStoreContainers);
			}
			bookInventoryList = bookInventoryService.findByCustomerIdAndproductId(storeContainers.toArray(), productids.toArray(),stockOwnerChange.getSeller().getId());
		}
		return bookInventoryList;
	}
	
	//结算
	@RequestMapping(value = "/{id}/settlement", method = RequestMethod.GET)
	public String settlement(@PathVariable Long id, Long customerType ,long operationType,ModelMap model) {
		StockOwnerChange stockOwnerChange	=stockOwnerChangeService.findOne(id);
		model.addAttribute("id", id);
		model.addAttribute("customerType", customerType);
		model.addAttribute("operationType", operationType);
		model.addAttribute("chargeTypes", chargeTypeService.findAll());
		Set<CalculatedResult>  calculatedResults =null;
		Customer custoemr=null;
		if(customerType==0){
			 calculatedResults =storeCalculator.Calculate4Seller(stockOwnerChange);
			 custoemr=stockOwnerChange.getSeller();
		}else{
			 calculatedResults =storeCalculator.Calculate4Buyer(stockOwnerChange);
			 custoemr=stockOwnerChange.getBuyer();
		}
		model.addAttribute("calculatedResults",calculatedResults);
		model.addAttribute("custoemr",custoemr);
			return "/store/stock_owner_change_settlement_form";
	}

	@RequestMapping(value = "/{id}/charge", method = RequestMethod.POST)
	public String charge(@PathVariable Long id,String[] feeitem,String[] money,String[] actuallyMoney,Long customerType,Long chargeTypeId,boolean delayed,Long operationType,ModelMap model) {
		
		stockOwnerChangeService.settlement(customerType,id, chargeTypeId,feeitem,money,actuallyMoney,delayed);
		return "redirect:/store/stock-owner-change?customerType="+customerType+"&&operationType="+operationType;
	}
/*	@RequestMapping(value="/index-settlement",method = RequestMethod.GET)
	public String indexSettlement(Long customerType,ModelMap model) {
		model.addAttribute("outboundTypes", OutboundType.values());
		model.addAttribute("customerType", customerType);
		model.addAttribute("customers", customerService.findAll());
		return "/store/stock_owner_change_settlement";
	}*/

/*	@RequestMapping(value = "/list-settlement", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listSettlement(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
		Long 	sellerId, Long buyerId,Long customerType) {
		Page<StockOwnerChange> stockOwnerChanges = stockOwnerChangeService.findStockOwnerChangeSpecification(page, rows, sellerId, buyerId, StockOwnerChangeStatus.已选位, null, customerType);
		return toEasyUiDatagridResult(stockOwnerChanges);
	}*/
	
	/*//缴费
	@RequestMapping(value="/index-payment",method = RequestMethod.GET)
	public String indexPayment(Long customerType,ModelMap model) {
		model.addAttribute("customerType", customerType);
		model.addAttribute("customers", customerService.findAll());
		return "/store/stock_owner_change_payment";
	}	*/

	/*@RequestMapping(value = "/list-payment", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listPayment(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			Long sellerId, Long buyerId,Long customerType) {
		Page<StockOwnerChange> stockOwnerChanges =null;
		stockOwnerChanges = stockOwnerChangeService.findStockOwnerChangeSpecification(page, rows, sellerId, buyerId, null, PaymentStatus.未付款,customerType);
		return toEasyUiDatagridResult(stockOwnerChanges);
	}
	
	@RequestMapping(value = "/{id}/payment-view", method = RequestMethod.GET)
	public String paymentView(@PathVariable Long id,String customerType, ModelMap model) {
		StockOwnerChange stockOwnerChange = stockOwnerChangeService.findOne(id);
		model.addAttribute("customerType", customerType);
		model.addAttribute("stockOwnerChange", stockOwnerChange);
		return "/store/stock_owner_change_payment_view";
	}*/
	@RequestMapping(value="{id}/payment" ,method = RequestMethod.POST)
	public String payment(@PathVariable Long id,Long customerType,Long operationType,String aCardMac, String sMemberCode,String password) {
		stockOwnerChangeService.payment(id,customerType,aCardMac, sMemberCode,password);
		return "redirect:/store/stock-owner-change?customerType="+customerType+"&&operationType="+operationType;
	}
	
	//延迟缴费
	/*@RequestMapping(value="/index-delay",method = RequestMethod.GET)
	public String delayCheck(Long customerType,ModelMap model) {
		model.addAttribute("customerType", customerType);
		model.addAttribute("customers", customerService.findAll());
		return "/store/stock_owner_change_payment_delay";
	}	

	@RequestMapping(value = "/list-delay", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listDelay(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			Long sellerId, Long buyerId,Long customerType) {
		Page<StockOwnerChange> stockOwnerChanges =null;
		stockOwnerChanges=	stockOwnerChangeService.findStockOwnerChangeSpecification(page, rows, sellerId, buyerId, null, PaymentStatus.延付待审核,customerType);
		return toEasyUiDatagridResult(stockOwnerChanges);
	}
	@RequestMapping(value = "/{id}/payment-delay-view", method = RequestMethod.GET)
	public String paymentDelay(@PathVariable Long id,Long customerType, ModelMap model) {
		StockOwnerChange stockOwnerChange = stockOwnerChangeService.findOne(id);
		model.addAttribute("stockOwnerChange", stockOwnerChange);
		model.addAttribute("customerType", customerType);
		return "/store/stock_owner_change_payment_delay_view";
	}*/
	@RequestMapping(value="{id}/delay" ,method = RequestMethod.POST)
	public String delay(@PathVariable Long id,Long customerType,String remark,Long paymentStatus,Long operationType) { 
		stockOwnerChangeService.delay(id,customerType,remark,paymentStatus==1?PaymentStatus.延付已生效:PaymentStatus.延付已拒绝);
		return "redirect:/store/stock-owner-change?customerType="+customerType+"&&operationType="+operationType;
	}

}
