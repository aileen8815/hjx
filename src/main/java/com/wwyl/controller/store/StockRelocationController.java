package com.wwyl.controller.store;


import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wwyl.Enums.StockRelocationStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.StockRelocation;
import com.wwyl.service.settings.ChargeTypeService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.StockRelocationService;
import com.wwyl.service.finance.PaymentService;

/**
 * 库内移位
 * 
 * @author jianl
 */
@Controller
@Deprecated
@RequestMapping("/store/Stock-relocation")
public class StockRelocationController extends BaseController {

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
	private StockRelocationService stockRelocationService;
	
	@Resource
	private BookInventoryService bookInventoryService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		 
		model.addAttribute("customers", customerService.findAll());
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public List<StockRelocation> list(@RequestParam(value = "serialNo", defaultValue = "") String serialNo,StockRelocationStatus stockRelocationStatus) {
		List<StockRelocation> stockRelocation = stockRelocationService.findStockRelocation(serialNo ,stockRelocationStatus);
		return  stockRelocation;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id,ModelMap model) {
		StockRelocation stockRelocation = stockRelocationService.findOneStockRelocation(id);
		model.addAttribute("stockRelocation", stockRelocation);
		
		return "/store/stock_relocation_view";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		return formPage;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String create(StockRelocation stockRelocation,String fromlocationCode,ModelMap model) {
		
		StockRelocation  stockRelocation2=	stockRelocationService.saveStockRelocation(stockRelocation,fromlocationCode);
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("stockRelocation", stockRelocation2);
	
		return formPage;
	}
 
	@RequestMapping(value = "/{id}/save", method = RequestMethod.POST)
	public String toStockRelocation( @PathVariable Long id,String tolocationCode,ModelMap model) {
	 
		stockRelocationService.toStoreLocation(id,tolocationCode);
		return "redirect:/store/Stock-relocation";
	}
	


 	@RequestMapping(value = "/select-storelocation", method = RequestMethod.POST)
	@ResponseBody
	public StockRelocation selectStoreLocation(String storeLocation) {
 		 
 		StockRelocation stockRelocation=null;
		if (!StringUtils.isBlank(storeLocation)) {
			BookInventory bookInventory=bookInventoryService.findByLocation(storeLocation);
			stockRelocation=new StockRelocation();
			stockRelocation.setCustomer(bookInventory.getCustomer());
			stockRelocation.setStoreContainer(bookInventory.getStoreContainer());
		}  
 
		return stockRelocation;
	} 
	

	
	
}
