package com.wwyl.controller.store;

import java.util.Date;
import java.util.Map;

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

import com.wwyl.Enums;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.Enums.TransactionType;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.StockAdjust;
import com.wwyl.entity.store.StockAdjustItem;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.PackingService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.ProductStatusService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StockAdjustTypeService;
import com.wwyl.service.settings.StoreContainerService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.StockAjdustService;

/**
 * @author fyunli
 */
@Controller
@Deprecated
@RequestMapping(value = "/store/stock-adjust")
public class StockAdjustController extends BaseController {

	@Resource
	private ProductService productService;
	@Resource
	private MeasureUnitService measureUnitService;
	@Resource
	private StoreContainerService storeContainerService;
	@Resource
	private StoreLocationService storeLocationService;
	@Resource
	private PackingService packingService;
	@Resource
	private ProductStatusService productStatusService;
	@Resource
	private CustomerService customerService;
	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private StockAjdustService stockAjdustService;
	@Resource
	private StockAdjustTypeService stockAdjustTypeService;
	@Resource
	SerialNumberService serialNumberService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("adjustTypes", stockAdjustTypeService.findAll());
		return indexPage;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "stockAdjustTypeId", defaultValue = "") String stockAdjustType) {
		Page<StockAdjust> stockAdjustPage = stockAjdustService.findStockAdjustByStockAdjustType(page, rows, stockAdjustType);
		return toEasyUiDatagridResult(stockAdjustPage);
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("adjustTypes", stockAdjustTypeService.findAll());
		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("stockAdjust") @Valid StockAdjust stockAdjust, BindingResult result, ModelMap model) {
		stockAdjust.setSerialNo(serialNumberService.getSerialNumber(TransactionType.StockAdjust));
		stockAdjust.setSubmitTime(new Date());
		stockAdjust.setSubmitter(ThreadLocalHolder.getCurrentOperator());
		Long stockAdjustId = stockAjdustService.saveStockAdjust(stockAdjust);
		return "redirect:/store/stock-adjust/index-item?stockAdjustId=" + stockAdjustId;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		stockAjdustService.deleteStockAdjust(id);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		model.addAttribute("adjustTypes", stockAdjustTypeService.findAll());
		model.addAttribute("stockAdjust", stockAjdustService.findOneStockAdjust(id));
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("stockAdjust") @Valid StockAdjust stockAdjust, BindingResult result, ModelMap model) {
		Long stockAdjustId = stockAjdustService.saveStockAdjust(stockAdjust);
		return "redirect:/store/stock-adjust/index-item?stockAdjustId=" + stockAdjustId;
	}

/*	@RequestMapping(value = "/items", method = RequestMethod.GET)
	@ResponseBody
	public List<StockAdjustItem> items(Long stockAdjustId) {
		//List<StockAdjustItem> stockAdjustItemList = stockAjdustService.findStockAdjustItems(stockAdjustId);
		return stockAdjustItemList;
	}*/

	@RequestMapping(value = "/index-item", method = RequestMethod.GET)
	public String indexItems(Long stockAdjustId, ModelMap model) {
		StockAdjust stockAdjust = stockAjdustService.findOneStockAdjust(stockAdjustId);
		model.put("stockAdjust", stockAdjust);
		return "/store/stock_adjust_item";
	}

	@RequestMapping(value = "/{id}/delete-item", method = RequestMethod.GET)
	public String deleteItem(@PathVariable Long id, Long stockAdjustId, ModelMap model) {
		stockAjdustService.deleteStockAdjustItem(id);
		StockAdjust stockAdjust = stockAjdustService.findOneStockAdjust(stockAdjustId);
		model.put("stockAdjust", stockAdjust);
		return "redirect:/store/stock-adjust/index-item?stockAdjustId=" + stockAdjustId;
	}

	@RequestMapping(value = "/select-storelocation", method = RequestMethod.POST)
	@ResponseBody
	public StockAdjustItem selectStoreLocation(String storeLocation) {

		StockAdjustItem stockAdjustItem = new StockAdjustItem();
	 
		if (storeLocation != null) {
			 
			BookInventory bookInventory =bookInventoryService.findByLocation(storeLocation);
			if(bookInventory!=null){
				stockAdjustItem.asInventory(bookInventory);
			}else{
				stockAdjustItem=null;
			}
		}  
 
		return stockAdjustItem;
	}

	@RequestMapping(value = "/new-item", method = RequestMethod.GET)
	public String newItem(ModelMap model) {
		setModelvalueItem(model);
		return "/store/stock_adjust_item_form";
	}

	@RequestMapping(value = "/save-item", method = RequestMethod.POST)
	public String saveItem(@ModelAttribute("stockAdjustItem") @Valid StockAdjustItem stockAdjustItem, BindingResult result,String storeLocation, ModelMap model) {
		stockAjdustService.saveStockAdjustItem(stockAdjustItem,storeLocation);
		setModelvalueItem(model);
		return "/store/stock_adjust_item_form";
	}

	@RequestMapping(value = "/{id}/save-item", method = RequestMethod.POST)
	public String updateItem(@ModelAttribute("stockAdjustItem") @Valid StockAdjustItem stockAdjustItem, BindingResult result, ModelMap model) {
		stockAjdustService.saveStockAdjustItem(stockAdjustItem,null);
		return "";
	}

	@RequestMapping(value = "/{id}/edit-item", method = RequestMethod.GET)
	public String editItem(@PathVariable Long id, ModelMap model) {
		setModelvalueItem(model);
		//model.addAttribute("stockAdjustItem", stockAjdustService.findStockAdjustItems(id));
		return "/store/stock_adjust_item_form";
	}


	void setModelvalueItem(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("storeContainers", storeContainerService.findAll());
		model.addAttribute("products", productService.findAll());
		model.addAttribute("packings", packingService.findAll());
		model.addAttribute("statuses", productStatusService.findAll());
		model.addAttribute("weightMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
		model.addAttribute("amountMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.数量));
	}

}
