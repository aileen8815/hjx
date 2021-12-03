package com.wwyl.controller.store;


 
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.wwyl.entity.finance.PaymentItem;
 
import org.apache.commons.lang3.StringUtils;
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

import com.wwyl.Enums;
import com.wwyl.Enums.StockWastageStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.StockWastage;
import com.wwyl.entity.store.StockWastageItem;
import com.wwyl.service.settings.ChargeTypeService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.StockWastageTypeService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.StockWastageService;
import com.wwyl.service.finance.PaymentService;

/**
 * 报损
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/store/stock-wastage")
public class StockWastageController extends BaseController {

	@Resource
	CustomerService customerService;

	@Resource
	private ProductService productService;
	@Resource
	private MeasureUnitService measureUnitService;

	@Resource
	private StoreLocationService storeLocationService;

	@Resource
	private PaymentService paymentService;
	@Resource
	private ChargeTypeService chargeTypeService;
	@Resource
	private StockWastageService stockWastageService;
	@Resource
	private StockWastageTypeService stockWastageTypeService;
	@Resource
	private BookInventoryService bookInventoryService;
	
	

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model,@RequestParam(value = "operatorType", defaultValue = "0")  Long operatorType) {
		model.addAttribute("stockWastageStatus", StockWastageStatus.values()); 
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("operatorType", operatorType);
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo,StockWastageStatus   stockWastageStatus,@RequestParam(value = "operatorType", defaultValue = "0") Long operatorType,Long customerId) {
		if(operatorType==1){
			stockWastageStatus= StockWastageStatus.待审核报损;
		}else if(operatorType==2){
			stockWastageStatus= StockWastageStatus.待审核报损赔偿;
		}
		Page<StockWastage> stockWastages = stockWastageService.findStockWastageSpecification(page, rows,serialNo,customerId ,stockWastageStatus);
		return toEasyUiDatagridResult(stockWastages);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id, Long operatorType,ModelMap model) {
		StockWastage stockWastage = stockWastageService.findOne(id);
		if(stockWastage.getPayment()!=null){
			Set<PaymentItem> paymentItems =stockWastage.getPayment().getPaymentItems();
			for (PaymentItem paymentItem : paymentItems) {
				model.addAttribute("paymentItem", paymentItem);
			}
			
		}
	 
		model.addAttribute("stockWastage", stockWastage);
		model.addAttribute("operatorType", operatorType);
		
		return "/store/stock_wastage_view";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		
		
		model.addAttribute("customers", customerService.findAll());
		return formPage;
	}

	@RequestMapping(value="/save", method = RequestMethod.POST)
	public String create(StockWastage stockWastage ,ModelMap model) {
		 
		Long  stockWastageId=	stockWastageService.save(stockWastage);
		return "redirect:/store/stock-wastage/"+stockWastageId;
	}
	
	@RequestMapping(value="/cancel", method = RequestMethod.GET)
	public String cancel(Long stockWastageId) {
		 
		stockWastageService.cancel(stockWastageId);
		return "redirect:/store/stock-wastage/"+stockWastageId;
	}
	
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		stockWastageService.delete(id);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		StockWastage stockWastage = stockWastageService.findOne(id);
		model.addAttribute("stockWastage", stockWastage);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("stockWastage") @Valid StockWastage stockWastage, BindingResult result,ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("customers", customerService.findAll());
			this.printBindingResult(result);
			return formPage;
		}
		Long stockWastageId=stockWastageService.save(stockWastage);
		return "redirect:/store/stock-wastage/"+stockWastageId;
	}
	


	@RequestMapping(value = "/select-storeContainer", method = RequestMethod.POST)
	@ResponseBody
	public StockWastageItem selectStoreLocation(String storeContainer) {

		StockWastageItem stockWastageItem =null;
	 
		if (!StringUtils.isBlank(storeContainer)) {
			 
			BookInventory bookInventory =bookInventoryService.findByContainer(Long.valueOf(storeContainer));
			if(bookInventory!=null){
				stockWastageItem= new StockWastageItem();
				stockWastageItem.setAmount(bookInventory.getAmount());
				stockWastageItem.setAmountMeasureUnit(bookInventory.getAmountMeasureUnit());
				stockWastageItem.setProduct(bookInventory.getProduct());
				stockWastageItem.setStoreContainer(bookInventory.getStoreContainer());
				//bookInventory.getStoreContainer().getCreatedBy();
				stockWastageItem.setStoreLocation(bookInventory.getStoreLocation());
				stockWastageItem.setWeight(bookInventory.getWeight());
				stockWastageItem.setWeightMeasureUnit(bookInventory.getWeightMeasureUnit());
			}
		}  
 
		return stockWastageItem;
	}
	@RequestMapping(value = "/{id}/delete-item", method = RequestMethod.GET)
	public String deleteItem(@PathVariable Long id, ModelMap model) {
		StockWastageItem stockWastageItem=stockWastageService.findOneItem(id);
		Long StockWastageId=stockWastageItem.getStockWastage().getId();
		stockWastageService.deleteItem(id);
		return "redirect:/store/stock-wastage/"+StockWastageId;
	}
	@RequestMapping(value = "/{id}/cancel", method = RequestMethod.GET)
	public String cancel(@PathVariable Long id, ModelMap model) {
		stockWastageService.cancel(id);
		return "redirect:/store/stock-wastage/"+id;
	}
	@RequestMapping(value = "/{id}/save-item", method = RequestMethod.POST)
	public String updateItem(StockWastageItem stockWastageItem,
			  ModelMap model) {
		stockWastageService.updateItem(stockWastageItem);
		return "redirect:/store/stock-wastage/" + stockWastageItem.getStockWastage().getId();
	}

	@RequestMapping(value = "/{id}/edit-item", method = RequestMethod.GET)
	public String editItem(@PathVariable Long id , ModelMap model) {
		setItemModelMapvalue(model);
		StockWastageItem  stockWastageItem=stockWastageService.findOneItem(id);
		BookInventory bookInventory =bookInventoryService.findByContainer(stockWastageItem.getStoreContainer().getId());
		model.addAttribute("stockWastageItem", stockWastageItem);
		model.addAttribute("bookInventory", bookInventory);
		model.put("stockWastageId", stockWastageItem.getStockWastage().getId());
        model.put("customerId", stockWastageItem.getStockWastage().getCustomer().getId());
		return "/store/stock_wastage_item_form";
	}

	@RequestMapping(value = "/save-item", method = RequestMethod.POST)
	public String saveItem(@ModelAttribute("stockWastageItem") @Valid StockWastageItem stockWastageItem, BindingResult result,String locationCode,
			 ModelMap model) {
	 
		 stockWastageService.saveStockWastageItem(stockWastageItem, locationCode);
		 
		return "redirect:/store/stock-wastage/" + stockWastageItem.getStockWastage().getId();
	}

	@RequestMapping(value = "/new-item", method = RequestMethod.GET)
	public String newItem(Long stockWastageId, ModelMap model) {
		StockWastage stockWastage=stockWastageService.findOne(stockWastageId);
		setItemModelMapvalue(model);
		model.put("customerId", stockWastage.getCustomer().getId());
		model.put("stockWastageId",stockWastage.getId());
		return "/store/stock_wastage_item_form";
	}

	private void setItemModelMapvalue(ModelMap model) {
		model.addAttribute("products", productService.findAll());
		model.addAttribute("weightMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
		model.addAttribute("amountMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.数量));
		model.addAttribute("stockWastageTypes", stockWastageTypeService.findAll());	
	}

	//完成
	@RequestMapping(value="{id}/complete",method = RequestMethod.GET)
	public String complete(@PathVariable Long id) {
		stockWastageService.complete(id);
		return "redirect:/store/stock-wastage/"+id;
	}

 

	//审核报损单
	@RequestMapping(value="/{id}/check",method = RequestMethod.POST)
	public String checked(@PathVariable Long id ,Long type,Long operatorType,String remark) {
		stockWastageService.check(id, type,remark);
		return "redirect:/store/stock-wastage?operatorType="+operatorType;
	}
	//审核报损单费用
	@RequestMapping(value="/{id}/fee-check",method = RequestMethod.POST)
	public String feeChecked(@PathVariable Long id ,Long type,Long operatorType,String remark) {
		stockWastageService.feeCheck(id, type,remark);
		return "redirect:/store/stock-wastage?operatorType="+operatorType;
	}
	@RequestMapping(value="/settlement",method = RequestMethod.POST)
	public String settlement(Long stockWastageId ,Long paymentItemId,String  money) {
		stockWastageService.settlement(stockWastageId,paymentItemId,money);
		return "redirect:/store/stock-wastage/"+stockWastageId;
	}
	@RequestMapping(value="/{id}/commitcheck",method = RequestMethod.GET)
	public String commitCheck(@PathVariable Long id) {
		stockWastageService.commitCheck(id);
		return "redirect:/store/stock-wastage/"+id;
	}
	
	
}
