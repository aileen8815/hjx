package com.wwyl.controller.store;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Enums.StockInStatus;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.store.StockIn;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.InboundReceiptService;
import com.wwyl.service.store.InboundRegisterService;
import com.wwyl.service.store.StockInService;

/**
 * 上架取货
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/store/stockin")
public class StockInController extends BaseController {
	@Resource
	private StockInService stockInService;
	@Resource
	InboundReceiptService inboundReceiptService;
	@Resource
	InboundRegisterService inboundRegisterService;
	@Resource
	StoreLocationService storeLocationService;
	@Resource
	BookInventoryService bookInventoryService;
	@Resource
	CustomerService customerService;
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		Object[]  stockInStatus=new Object[]{StockInStatus.待上架,StockInStatus.上架中,StockInStatus.已上架};
		model.addAttribute("stockInStatus", stockInStatus);
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows, String serialNo,
			StockInStatus stockInStatus,Long customerId) {
		Page<StockIn> stockIns = stockInService.findStockInItems(page, rows, serialNo,  stockInStatus,  customerId);
		return toEasyUiDatagridResult(stockIns);
	}

	@RequestMapping(value = "/{id}/add-pickup", method = RequestMethod.GET)
	public String addPickup(@PathVariable Long id, ModelMap model) {
		StockIn stockIn = stockInService.findOne(id);
		Set<StoreLocation> storeLocations = stockIn.getPreStoreLocations();
		model.addAttribute("inboundReceiptItem", stockIn.getInboundReceiptItem());
		model.addAttribute("storeLocations", storeLocations);
		model.put("stockInId", stockIn.getId());
		return "/store/stock_in_pickup";
	}

	@RequestMapping(value = "/pickup", method = RequestMethod.POST)
	public String Pickup(Long id, ModelMap model) {
		StockIn stockIn = stockInService.findOne(id);
		stockIn.setStockInStatus(StockInStatus.上架中);
		//由于需要修改计算仓储费开始时间应为手持机清点完成后，故入库开始时间不是当前时间，改为清点时间。
		stockIn.setStockInStartTime(stockIn.getInboundReceiptItem().getReceiptTime());
		stockIn.setStockInOperator(ThreadLocalHolder.getCurrentOperator());
		stockInService.save(stockIn);
		model.addAttribute("storeLocations", stockIn.getPreStoreLocations());
		model.addAttribute("inboundReceiptItem", stockIn.getInboundReceiptItem());
		model.put("stockInId", stockIn.getId());
		return "/store/stock_in_complete";
	}

	@RequestMapping(value = "/complete-pickup", method = RequestMethod.POST)
	public String completePickup(Long id, String storeLocation, ModelMap model) {
		StoreLocation storeLocationobj = storeLocationService.findByCode(storeLocation);
		stockInService.completePickup(id, storeLocationobj.getId());
		return "redirect:/store/stockin";
	}

}
