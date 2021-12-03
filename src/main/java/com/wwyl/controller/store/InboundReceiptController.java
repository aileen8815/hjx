package com.wwyl.controller.store;

 
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
 
import javax.validation.Valid;

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
import com.wwyl.ThreadLocalHolder;
import com.wwyl.Enums.StockInStatus;
import com.wwyl.controller.BaseController;
 
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.store.BookInventory;
 
import com.wwyl.entity.store.InboundReceiptItem;
import com.wwyl.entity.store.InboundRegister;
import com.wwyl.entity.store.InboundRegisterItem;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.PackingService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.StoreContainerService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.InboundReceiptService;
import com.wwyl.service.store.InboundRegisterService;

/**
 * 入库检查
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/store/inbound-receipt")
public class InboundReceiptController extends BaseController {
	@Resource
	InboundReceiptService inboundReceiptService;
	@Resource
	InboundRegisterService inboundRegisterService;
	@Resource
	private ProductService productService;
	@Resource
	private MeasureUnitService measureUnitService;
	@Resource
	private PackingService packingService;
	@Resource
	private StoreContainerService storeContainerService;
	@Resource
	private BookInventoryService bookInventoryService;
	
	@RequestMapping(value = "items", method = RequestMethod.GET)
	@ResponseBody
	public Set<InboundReceiptItem> items(Long inboundRegisterId) {
		InboundRegister inboundRegister = inboundRegisterService.findOne(inboundRegisterId);
		return inboundRegister.getInboundReceiptItems();
	}

	@RequestMapping(value = "index-item", method = RequestMethod.GET)
	public String indexItems(Long inboundRegisterId, ModelMap model) {
		InboundRegister inboundRegister = inboundRegisterService.findOne(inboundRegisterId);
		model.put("inboundRegister", inboundRegister);
		return "/store/inbound_receipt_item";
	}

	@RequestMapping(value = "/{id}/delete-item", method = RequestMethod.GET)
	public String deleteItem(@PathVariable Long id, @RequestParam(value = "inboundRegisterId", defaultValue = "1") Long inboundRegisterId, ModelMap model) {
		inboundReceiptService.deleteItem(id);
		return "redirect:/store/inbound-register/" + inboundRegisterId;
	}

	@RequestMapping(value = "/{id}/save-item", method = RequestMethod.POST)
	public String updateItem(@ModelAttribute("inboundReceiptItem") @Valid InboundReceiptItem inboundReceiptItem, BindingResult result, Long inboundRegisterId, ModelMap model) {
		return this.saveItem(inboundReceiptItem, inboundRegisterId, result, model);
	}

	@RequestMapping(value = "/save-item", method = RequestMethod.POST)
	public String saveItem(@ModelAttribute("inboundReceiptItem") @Valid InboundReceiptItem inboundReceiptItem, Long inboundRegisterId, BindingResult result, ModelMap model) {
		InboundRegister inboundRegister = inboundRegisterService.findOne(inboundRegisterId);
		inboundReceiptItem.setInboundRegister(inboundRegister);
		inboundReceiptItem.setReceiptor(ThreadLocalHolder.getCurrentOperator());
		inboundReceiptItem.setReceiptTime(new Date());
		inboundReceiptItem.setQualified(true);
		// 从来未使用，不再上架过程中
		StoreContainer storeContainerobj=storeContainerService.findOne(inboundReceiptItem.getStoreContainer().getId());
		InboundReceiptItem preReceiptItem = inboundReceiptService.getLastInboundReceiptItemByContainerCode(storeContainerobj.getLabel());
		if (preReceiptItem != null
				&& !(preReceiptItem.getInboundRegister().getStockInStatus()
						.ordinal() >= StockInStatus.已上架.ordinal())) {
			throw new RuntimeException("此托盘已清点完成 - 收货ID: " + preReceiptItem.getId());
		}

		// 并且库存为空
		BookInventory bookInventory = bookInventoryService
				.findByContainerLabel(storeContainerobj.getLabel());
		if (bookInventory != null) {
			throw  new RuntimeException("此托盘已在库内使用 - 收货ID: " + preReceiptItem.getId());
		}
		inboundReceiptService.saveItem(inboundReceiptItem);
		return "redirect:/store/inbound-register/" + inboundRegisterId;
	}

	@RequestMapping(value = "/new-item", method = RequestMethod.GET)
	public String newItem(HttpServletRequest request, @RequestParam(value = "inboundRegisterItemId", defaultValue = "1") Long inboundRegisterItemId, ModelMap model) {
		InboundRegisterItem inboundRegisterItem = inboundRegisterService.findOneItem(inboundRegisterItemId);
		InboundRegister inboundRegister = inboundRegisterItem.getInboundRegister();
		model.addAttribute("inboundRegister", inboundRegister);		
		model.addAttribute("inboundRegisterItem", inboundRegisterItem);
		InboundReceiptItem inboundReceiptItem=	new InboundReceiptItem();
		inboundReceiptItem.setQualified(true);
		model.addAttribute("inboundReceiptItem", inboundReceiptItem);
		setItemModelMapvalue(model,inboundRegister.getCustomer().getProducts());

		return "/store/inbound_receipt_item_form";
	}

	private void setItemModelMapvalue(ModelMap model,Set<Product>  products) {
		model.addAttribute("products", products);
		model.addAttribute("weightMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
		model.addAttribute("amountMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.数量));
		model.addAttribute("packings", packingService.findAll());
		List<StoreContainer>   storeContainerUnfinished =inboundReceiptService.findByUnfinished();
		List<StoreContainer>  storeContainerAll =storeContainerService.findUnusedAll(null);
		 
		if(!storeContainerUnfinished.isEmpty()&&!storeContainerAll.isEmpty()){
			storeContainerAll.removeAll(storeContainerUnfinished);
		}
		model.addAttribute("storeContainer",storeContainerAll);
	}

}
