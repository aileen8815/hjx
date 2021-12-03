package com.wwyl.controller.store;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Enums;
import com.wwyl.Enums.OutboundType;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.Enums.TransactionType;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.store.OutboundCheckItem;
import com.wwyl.entity.store.OutboundFreight;
import com.wwyl.entity.store.OutboundFreightItem;
import com.wwyl.entity.store.OutboundRegister;
import com.wwyl.service.settings.CarrierService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.PackingService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.TallyAreaService;
import com.wwyl.service.settings.VehicleTypeService;
import com.wwyl.service.store.OutboundCheckService;
import com.wwyl.service.store.OutboundFreightService;
import com.wwyl.service.store.OutboundRegisterService;

/**
 * 出库发货
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/store/outbound-freight")
public class OutboundFreightController extends BaseController {
	@Resource
	OutboundCheckService outboundCheckService;
	@Resource
	VehicleTypeService vehicleTypeService;
	@Resource
	SerialNumberService serialNumberService;
	@Resource
	CarrierService carrierService;
	@Resource
	private ProductService productService;
	@Resource
	private MeasureUnitService measureUnitService;
	@Resource
	private PackingService packingService;

	@Resource
	TallyAreaService tallyAreaService;
	
	@Resource
	CustomerService customerService;
	
	@Resource
	OutboundFreightService outboundFreightService;
	@Resource
	private OutboundRegisterService outboundRegisterService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public List<OutboundFreight> list(String serialNo,Long customerId) {
		List<OutboundFreight> outboundFreights = outboundFreightService.findByoutboundSpecification(serialNo,customerId);
		return outboundFreights;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id, ModelMap model) {
		model.addAttribute("outboundFreight", outboundFreightService.findOne(id));
		return viewPage;
	}

	@RequestMapping(value = "/outboundCheckItem-list", method = RequestMethod.GET)
	@ResponseBody
	public List<OutboundCheckItem> outboundCheckItemList(Long outboundFreightId,Long outboundRegisterId ,ModelMap model) {

		//设置已预约的商品数量
		Set<OutboundCheckItem>	outboundCheckItemSet = outboundRegisterService.findOne(outboundRegisterId).getOutboundCheckItems();
		List<OutboundCheckItem> outboundCheckItems = outboundCheckService.mergeProductinfo(outboundCheckItemSet);
		if(outboundFreightId!=null){
			Set<OutboundFreightItem> outboundFreightItemSet=	outboundFreightService.findOne(outboundFreightId).getOutboundFreightItems();
			for (OutboundCheckItem outboundCheckItem : outboundCheckItems) {
				for (OutboundFreightItem outboundFreightItem : outboundFreightItemSet) {
					if(outboundCheckItem.getProduct().getId().equals(outboundFreightItem.getProduct().getId())){
						outboundCheckItem.setFreighAmountt(outboundFreightItem.getAmount()); 
					}
				}
			}
			 		
		 }
			return  outboundCheckItems;
	}
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(Long outboundRegisterId, ModelMap model) {
		model.addAttribute("outboundRegisterId", outboundRegisterId);
		setModelMapvalue(model);
		return formPage;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		setModelMapvalue(model);
		OutboundFreight	outboundFreight=outboundFreightService.findOne(id);
		Set<OutboundFreightItem> outboundFreightItemSet=outboundFreight.getOutboundFreightItems();
		int i=0;
		StringBuffer buffer=new StringBuffer();
		int size=outboundFreightItemSet.size();
		for (OutboundFreightItem outboundFreightItem : outboundFreightItemSet) {
			i++;
			buffer.append(outboundFreightItem.getProduct().getId()+":"+outboundFreightItem.getAmount());
			if(i!=size){
				buffer.append(",");
			}
		}
		model.addAttribute("productCheck",buffer.toString());
		model.addAttribute("outboundRegisterId",outboundFreight.getOutboundRegister().getId() );
		model.addAttribute("outboundFreight",outboundFreight);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("outboundFreight") @Valid OutboundFreight outboundFreight, BindingResult result,String[]  productCheck,ModelMap model) {
	  
		outboundFreight.setFreightTime(new Date());
		outboundFreight.setOperator(ThreadLocalHolder.getCurrentOperator());
		Long outboundFreightId=outboundFreightService.save(outboundFreight,productCheck);
		return "redirect:/store/outbound-freight/"+ outboundFreightId;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("outboundFreight") @Valid OutboundFreight outboundFreight, BindingResult result, Long outboundRegisterId,String[]  productCheck, ModelMap model) {
		// 出库发车单
		OutboundRegister outboundRegister = outboundRegisterService.findOne(outboundRegisterId);
		outboundFreight.setSerialNo(serialNumberService.getSerialNumber(TransactionType.OutboundFreight));
		outboundFreight.setFreightTime(new Date());
		outboundFreight.setOperator(ThreadLocalHolder.getCurrentOperator());
		outboundFreight.setCustomer(outboundRegister.getCustomer());
		outboundFreight.setOutboundRegister(outboundRegister);
		Long outboundFreightId=outboundFreightService.save(outboundFreight,productCheck);
		return "redirect:/store/outbound-freight/"+ outboundFreightId;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		outboundFreightService.delete(id);
		return "redirect:/store/outbound-freight";
	}

	@RequestMapping(value = "items", method = RequestMethod.GET)
	@ResponseBody
	public List<OutboundFreightItem> items(Long outboundFreightId) {
		List<OutboundFreightItem> outboundCheckItemList = outboundFreightService.findOutboundFreightItems(outboundFreightId);
		return outboundCheckItemList;
	}

	@RequestMapping(value = "index-item", method = RequestMethod.GET)
	public String indexItems(Long outboundFreightId, Long outboundCheckId, ModelMap model) {
		OutboundFreight outboundFreight = outboundFreightService.findOne(outboundFreightId);
		model.put("outboundFreight", outboundFreight);
		model.put("outboundCheckId", outboundCheckId);
		return "/store/outbound_freight_item";
	}

	@RequestMapping(value = "/{id}/delete-item", method = RequestMethod.GET)
	public String deleteItem(@PathVariable Long id, Long outboundFreightId, Long outboundCheckId, ModelMap model) {
		outboundFreightService.deleteItem(id);
		return "redirect:/store/outbound-freight/index-item?outboundCheckId=" + outboundCheckId + "&outboundFreightId=" + outboundFreightId;
	}

	@RequestMapping(value = "/{id}/save-item", method = RequestMethod.POST)
	public String updateItem(@ModelAttribute("outboundFreightItem") @Valid OutboundFreightItem outboundFreightItem, BindingResult result, Long outboundFreightId,
			Long outboundCheckId, ModelMap model) {
		outboundFreightService.saveItem(outboundFreightItem, outboundFreightId);
		return "redirect:/store/outbound-freight/index-item?outboundCheckId=" + outboundCheckId + "&outboundFreightId=" + outboundFreightId;
	}

	@RequestMapping(value = "/{id}/edit-item", method = RequestMethod.GET)
	public String editItem(@PathVariable Long id, Long outboundFreightId, Long outboundCheckId, ModelMap model) {
		setItemModelMapvalue(model);
		model.put("outboundFreightItem", outboundFreightService.findOneItem(id));
		model.put("outboundFreightId", outboundFreightId);
		model.put("outboundCheckId", outboundCheckId);
		return "/store/outbound_freight_item_form";
	}

	@RequestMapping(value = "/save-item", method = RequestMethod.POST)
	public String saveItem(@ModelAttribute("outboundFreightItem") @Valid OutboundFreightItem outboundFreightItem, BindingResult result, Long outboundFreightId,
			Long outboundCheckId, ModelMap model) {
		outboundFreightService.saveItem(outboundFreightItem, outboundFreightId);
		return "redirect:/store/outbound-freight/index-item?outboundCheckId=" + outboundCheckId + "&outboundFreightId=" + outboundFreightId;
	}

	@RequestMapping(value = "/new-item", method = RequestMethod.GET)
	public String newItem(Long outboundFreightId, Long outboundCheckId, ModelMap model) {
		setItemModelMapvalue(model);
		model.put("outboundFreightId", outboundFreightId);
		model.put("outboundCheckId", outboundCheckId);
		return "/store/outbound_freight_item_form";
	}

	private void setItemModelMapvalue(ModelMap model) {
		model.addAttribute("products", productService.findAll());
		model.addAttribute("weightMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
		model.addAttribute("amountMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.数量));
		model.addAttribute("packings", packingService.findAll());

	}

	private void setModelMapvalue(ModelMap model) {
		model.addAttribute("outboundTypes", OutboundType.values());
		model.addAttribute("tallyAreas", tallyAreaService.findAll());
		model.addAttribute("carriers", carrierService.findAll());
		model.addAttribute("vehicleTypes", vehicleTypeService.findAll());
	}
}
