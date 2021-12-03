package com.wwyl.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Enums.StockRelocationStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.StockRelocation;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.StockRelocationService;
import com.wwyl.service.settings.ProductService;

/**
 * @author fyunli
 */
@Controller
@RequestMapping("/api")
public class StockRelocationApiController extends BaseController {


	@Resource
	CustomerService customerService;

	@Resource
	private StoreLocationService storeLocationService;

	@Resource
	private StockRelocationService stockRelocationService;
	
	@Resource
	private BookInventoryService bookInventoryService;
	
	@Resource
	private ProductService productService;

 
	@RequestMapping(value = "/stock-relocation/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "serialNo", defaultValue = "") String serialNo,String stockRelocationStatus) {
		
		List<StockRelocation> stockRelocations = stockRelocationService.findStockRelocation(serialNo ,StockRelocationStatus.valueOf(stockRelocationStatus));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("result", stockRelocations);
		return result;
	}

	@RequestMapping(value = "/stock-relocation/save", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object>  create(Long storeContainerId,Long storeLocationId,Long customerId, String guid) {
		try{
			Map<String, Object> result = new HashMap<String, Object>();
			String msg = "";
			if(storeContainerId!=null&&storeLocationId!=null&&customerId!=null&&guid!=null){
				StockRelocation  stockRelocation = stockRelocationService.findByStoreContainer(storeContainerId,StockRelocationStatus.移位中);
				if(stockRelocation!=null){
					msg = "该托盘有未完成移位计划";
				}else{			
					stockRelocation= stockRelocationService.saveStockRelocation( storeContainerId, storeLocationId, customerId, guid);			
				}
				result.put("error", 0);
				result.put("msg",msg);
				result.put("result", stockRelocation);
			}else{
				return this.printMessage(1, "参数不能为空");
			}
			return result;
		} catch (Exception e) {
			return this.printMessage(1,"Exception：保存移位单失败");
		}
	}

	@RequestMapping(value = "/stock-relocation/to-store-location", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object>  toStockRelocation(String guid,String tolocationCode) {
		try{
			stockRelocationService.toStoreLocation(guid,tolocationCode);
			return this.printMessage(0, "移位完成");
		} catch (Exception e) {
			return this.printMessage(1,"Exception：提交移位单失败");
		}
	}

 	@RequestMapping(value = "/stock-relocation/select-store-container", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectContainer(String storeContainerLabel) {
 		Map<String, Object> result = new HashMap<String, Object>();
		if (!StringUtils.isBlank(storeContainerLabel)) {
			BookInventory bookInventory=bookInventoryService.findByContainerLabel(storeContainerLabel);
			if(bookInventory==null){
				return this.printMessage(1, "库存为空");
			}
			result.put("error", 0);
			result.put("bookInventory", bookInventory);
			result.put("customer", bookInventory.getCustomer());
			result.put("product", bookInventory.getProduct());
			result.put("amountMeasureUnit", bookInventory.getAmountMeasureUnit());
			result.put("packing", bookInventory.getPacking());
			result.put("weightMeasureUnit", bookInventory.getWeightMeasureUnit());
			return result;
		}else{
			return this.printMessage(1, "参数不能为空");
		}   
	} 
 	@RequestMapping(value = "/stock-relocation/check-tostore-location", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> checkToStoreLocation(String tolocationLabel) {
 		Map<String, Object> result = new HashMap<String, Object>();
		if (!StringUtils.isBlank(tolocationLabel)) {
			StoreLocation  storeLocation=stockRelocationService.checkToStoreLocation(tolocationLabel);
			if(storeLocation==null){
				return this.printMessage(1, "储位已使用或未绑定");
			}
			result.put("error", 0);
			result.put("storeLocation", storeLocation);
			return result;
		}else{
			return this.printMessage(1, "储位不能为空");
		}   
	} 
	


}
