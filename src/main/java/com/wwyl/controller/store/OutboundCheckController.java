package com.wwyl.controller.store;


import java.util.Date;
import java.util.List;
 

import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wwyl.Enums;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.controller.BaseController;
 
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.store.OutboundCheckItem;
import com.wwyl.entity.store.OutboundRegister;
import com.wwyl.entity.store.StockOut;
import com.wwyl.service.settings.BookingMethodService;
import com.wwyl.service.settings.CarrierService;
import com.wwyl.service.settings.ChargeTypeService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.PackingService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.StoreContainerService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.settings.TallyAreaService;
import com.wwyl.service.settings.VehicleTypeService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.OutboundBookingService;
import com.wwyl.service.store.OutboundCheckService;
import com.wwyl.service.store.OutboundRegisterService;
import com.wwyl.service.finance.PaymentService;
import com.wwyl.service.store.StockOutService;

/**
 * 出库检查
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/store/outbound-check")
public class OutboundCheckController extends BaseController {
	@Resource
	OutboundCheckService outboundCheckService;
	@Resource
	OutboundRegisterService outboundRegisterService;
	@Resource
	OutboundBookingService outboundBookingService;
	@Resource
	VehicleTypeService vehicleTypeService;
	@Resource
	BookingMethodService bookingMethodService;
	@Resource
	StoreAreaService storeAreaService;
	@Resource
	CustomerService customerService;
	@Resource
	SerialNumberService serialNumberService;
	@Resource
	ChargeTypeService chargeTypeService;
	@Resource
	private PaymentService paymentService;
	@Autowired
	CarrierService carrierService;
	@Resource
	private ProductService productService;
	@Resource
	private MeasureUnitService measureUnitService;
	@Resource
	private PackingService packingService;
	@Resource
	private StoreLocationService storeLocationService;
	@Resource
	private StoreContainerService storeContainerService;
	@Resource
	TallyAreaService tallyAreaService;
	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private StockOutService stockOutService;
	

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	// @RequestMapping(value = "list", method = RequestMethod.GET)
	// @ResponseBody
	// public Map<String, Object> list(
	// @RequestParam(value = "page", defaultValue = "1") int page,
	// @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
	// @RequestParam(value = "serialNo", defaultValue = "") String serialNo) {
	// Page<OutboundCheck> outboundRegisters = outboundCheckService.findOutboundCheckBySerialNoLike(page, rows, serialNo);
	// return toEasyUiDatagridResult(outboundRegisters);
	// }
	//
	// @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	// public String view(@PathVariable Long id, ModelMap model) {
	// model.addAttribute("outboundCheck", outboundCheckService.findOne(id));
	// return viewPage;
	// }
	//
	// @RequestMapping(value = "/new", method = RequestMethod.GET)
	// public String add(HttpServletRequest request, HttpSession session, Long outboundRegisterId, ModelMap model) {
	// setModelMapvalue(model);
	// OutboundRegister outboundRegister = outboundRegisterService.findOne(outboundRegisterId);
	// model.addAttribute("outboundRegister", outboundRegister);
	// return formPage;
	// }

	// @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	// public String delete(@PathVariable Long id, ModelMap model) {
	// outboundRegisterService.delete(id);
	// return indexRedirect;
	// }

	// @RequestMapping(value = "items", method = RequestMethod.GET)
	// @ResponseBody
	// public List<OutboundCheckItem> items(Long outboundRegisterId) {
	// List<OutboundCheckItem> outboundCheckItemList = outboundCheckService.findOutboundCheckItems(outboundRegisterId);
	// return outboundCheckItemList;
	// }

	@RequestMapping(value = "index-item", method = RequestMethod.GET)
	public String indexItems(Long outboundRegisterId, ModelMap model) {
		OutboundRegister outboundRegister = outboundRegisterService.findOne(outboundRegisterId);
		model.put("outboundRegister", outboundRegister);
		return "/store/outbound_check_item";
	}

	@RequestMapping(value = "/{id}/delete-item", method = RequestMethod.GET)
	public String deleteItem(@PathVariable Long id, Long outboundRegisterId, ModelMap model) {
		outboundCheckService.deleteItem(id);
		return "redirect:/store/outbound-register/" + outboundRegisterId;
	}

	@RequestMapping(value = "/{id}/save-item", method = RequestMethod.POST)
	public String updateItem(@ModelAttribute("outboundCheckItem") @Valid OutboundCheckItem outboundCheckItem, BindingResult result, Long outboundRegisterId,
			HttpServletRequest request, HttpSession session, ModelMap model) {
		OutboundRegister outboundRegister = outboundRegisterService.findOne(outboundRegisterId);
		outboundCheckItem.setOutboundRegister(outboundRegister);
		outboundCheckItem.setCheckOperator(ThreadLocalHolder.getCurrentOperator());
		outboundCheckItem.setCheckTime(new Date());
		outboundCheckService.saveItem(outboundCheckItem);		
		return "redirect:/store/outbound-register/" + outboundRegisterId;
	}

	@RequestMapping(value = "/{id}/edit-item", method = RequestMethod.GET)
	public String editItem(@PathVariable Long id, Long outboundRegisterId, HttpServletRequest request, HttpSession session, ModelMap model) {
		
		model.addAttribute("outboundCheckItem", outboundCheckService.findOneItem(id));
		OutboundRegister outboundRegister = outboundRegisterService.findOne(outboundRegisterId);
		setItemModelMapvalue(model,outboundRegister.getCustomer().getProducts());
		model.put("outboundRegister", outboundRegister);
		return "/store/outbound_check_item_form";
	}

	@RequestMapping(value = "/check-storeContainer", method = RequestMethod.GET)
	public String checkStoreContainer(Long outboundRegisterId, Long storeContainerId, ModelMap model) {
		
		OutboundRegister outboundRegister = outboundRegisterService.findOne(outboundRegisterId);
		setItemModelMapvalue(model,outboundRegister.getCustomer().getProducts());
		List<StockOut> stockOutList=stockOutService.findBystoreContainer(storeContainerId);
		OutboundCheckItem outboundCheckItem = new OutboundCheckItem();
		if(!stockOutList.isEmpty()){
			StockOut stockOut=stockOutList.get(0);
			outboundCheckItem.setAmount(stockOut.getAmount());
			outboundCheckItem.setAmountMeasureUnit(stockOut.getAmountMeasureUnit());
			outboundCheckItem.setPacking(stockOut.getPacking());
			outboundCheckItem.setProduct(stockOut.getProduct());
			outboundCheckItem.setSpec(stockOut.getSpec());
			outboundCheckItem.setWeight(stockOut.getWeight());
			outboundCheckItem.setWeightMeasureUnit(stockOut.getWeightMeasureUnit());
			outboundCheckItem.setStoreContainer(stockOut.getStoreContainer());
			outboundCheckItem.setStockInTime(stockOut.getStockInTime());
		}
		
		model.addAttribute("outboundCheckItem", outboundCheckItem);
		model.put("outboundRegister", outboundRegister);
		return "/store/outbound_check_item_form";
	}

	@RequestMapping(value = "/save-item", method = RequestMethod.POST)
	public String saveItem(@ModelAttribute("outboundCheckItem") @Valid OutboundCheckItem outboundCheckItem, Long stockOutId, BindingResult result,
			HttpServletRequest request, HttpSession session, ModelMap model) {
		StockOut stockOut = stockOutService.findOne(stockOutId);
		OutboundRegister outboundRegister = stockOut.getOutboundRegister();
		outboundCheckItem.setOutboundRegister(outboundRegister);
		outboundCheckItem.setCheckOperator(ThreadLocalHolder.getCurrentOperator());
		outboundCheckItem.setCheckTime(new Date());
		outboundCheckService.saveItem(outboundCheckItem);
		model.addAttribute("outboundRegister", outboundRegister);
		return "redirect:/store/outbound-register/" + outboundRegister.getId();
	}

	@RequestMapping(value = "/new-item", method = RequestMethod.GET)
	public String newItem(HttpServletRequest request, HttpSession session, Long stockOutId, ModelMap model) {
		StockOut stockOut = stockOutService.findOne(stockOutId);
		OutboundRegister outboundRegister = stockOut.getOutboundRegister();
		Set<OutboundCheckItem> outboundCheckItems =  outboundRegister.getOutboundCheckItems();
		if(!outboundCheckItems.isEmpty()){
			for (OutboundCheckItem checkItem : outboundCheckItems) {
				if(stockOut.getStoreContainer().getId().equals(checkItem.getStoreContainer().getId())){
					throw new RuntimeException("拣货单已完成验货，请返回选择其他拣货单验货！");
				}
				
			}
		}
		model.addAttribute("stockOut", stockOut);
		model.addAttribute("outboundRegister", outboundRegister);
		setItemModelMapvalue(model,outboundRegister.getCustomer().getProducts());
		return "/store/outbound_check_item_form";
	}

	// @RequestMapping(value = "/next-add-item", method = RequestMethod.POST)
	// public String nextAddItem(OutboundCheckItem outboundCheckItem, HttpServletRequest request, HttpSession session, Long outboundRegisterId, ModelMap model) {
	// OutboundRegister outboundRegister = outboundRegisterService.findOne(outboundRegisterId);
	// outboundCheckItem.setOutboundRegister(outboundRegister);
	// outboundCheckItem.setCheckOperator(ThreadLocalHolder.getCurrentOperator());
	// outboundCheckItem.setCheckTime(new Date());
	// outboundCheckService.saveItem(outboundCheckItem);
	// model.addAttribute("outboundRegister", outboundRegister);
	// setItemModelMapvalue(model);
	// model.remove("outboundCheckItem");
	// return "/store/outbound_check_item_form";
	// }
	//
	// // @RequestMapping(value = "/payment-item", method = RequestMethod.GET)
	// public String paymentItems(Long id, ModelMap model) {
	// OutboundCheck outboundCheck = outboundCheckService.findOne(id);
	// List<PaymentItem> paymentItems = paymentService.findPaymentItems(outboundCheck.getPayment().getId());
	// model.put("paymentItems", paymentItems);
	// Payment payment = paymentService.findPaymentById(outboundCheck.getPayment().getId());
	// BigDecimal amount = paymentService.paymentAmount(paymentItems);
	// model.put("payment", payment);
	// model.put("amount", amount.toString());
	// return "/store/payment_items";
	// }

	private void setItemModelMapvalue(ModelMap model,Set<Product>  products) {
		model.addAttribute("products", products);
		model.addAttribute("weightMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
		model.addAttribute("amountMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.数量));
		model.addAttribute("packings", packingService.findAll());
		model.addAttribute("storeContainer", storeContainerService.findAll());

	}

}
