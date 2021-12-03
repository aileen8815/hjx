package com.wwyl.controller.store;

import java.text.SimpleDateFormat;
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

import com.wwyl.Enums;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.PackingService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.ProductStatusService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.StoreContainerService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.InboundReceiptService;

/**
 * 库存初始化
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/store/book-inventory")
public class BookInventoryController extends BaseController {

	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private CustomerService customerService;
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
	private ProductStatusService productStatusService;
	@Resource
	private StoreAreaService storeAreaService;
	@Resource
	private InboundReceiptService inboundReceiptService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		Long departmentId = 0L;
		model.addAttribute("customers", customerService.findAll());
		//model.addAttribute("products", productService.findAll());
		
		model.addAttribute("storeAreas", storeAreaService.findAll());
	//	model.addAttribute("storeContainers", storeContainerService.findAll());
		
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "customerId", defaultValue = "") String customerId, @RequestParam(value = "productId", defaultValue = "") String productId,
			@RequestParam(value = "storeAreaId", defaultValue = "") String storeAreaId, @RequestParam(value = "storeContainerId", defaultValue = "") String storeContainerId) {
		Page<BookInventory> bookInventorys = bookInventoryService.findBookInventorys(page, rows, customerId, productId, storeAreaId, storeContainerId);
		return toEasyUiDatagridResult(bookInventorys);
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		setModelMapValue(model,null);
		//List<StoreContainer> storeContainerList=bookInventoryService.findstoreContainers(null,null);
		//model.addAttribute("storeContainer",storeContainerList);
		model.addAttribute("_method", "post");
		return formPage;
	}
	
	@RequestMapping(value = "findstoreContainers", method = RequestMethod.GET)
	@ResponseBody
	public List<StoreContainer> findstoreContainers(Long id,String label) {
		
		List<StoreContainer> storeContainerList=bookInventoryService.findstoreContainers(id, label);
		 
		return  storeContainerList;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("bookInventory") @Valid BookInventory bookInventory, BindingResult result, String storeAreaid, String storeLocation, ModelMap model) {
		StoreLocation storeLocationobj = storeLocationService.findByCode(storeLocation.trim());
		bookInventory.setStoreLocation(storeLocationobj);
		
		//bookInventory.setStockInTime(new Date());	//初始化库存上架时间为当前时间，上架人为当前操作人
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		bookInventory.setInboundRegisterSerialNo(format.format(bookInventory.getStockInTime()));
		bookInventory.setStockInOperator(ThreadLocalHolder.getCurrentOperator());
		bookInventoryService.save(bookInventory,false);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		BookInventory bookInventory = bookInventoryService.findOne(id);
		setModelMapValue(model,bookInventory.getCustomer().getProducts());
		model.addAttribute("bookInventory", bookInventory);
		//List<StoreContainer> storeContainerList=bookInventoryService.findstoreContainers(id,null);
		//model.addAttribute("storeContainer", storeContainerList);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("bookInventory") @Valid BookInventory bookInventory, BindingResult result, String storeLocation,String  storeLocationOld,ModelMap model) {
		StoreLocation storeLocationobj = storeLocationService.findByCode(storeLocation);
		bookInventory.setStoreLocation(storeLocationobj);
		bookInventoryService.update(bookInventory,storeLocationOld);
		return indexRedirect;
	}

	@RequestMapping(value = "get", method = RequestMethod.GET)
	@ResponseBody
	public BookInventory get(@RequestParam(value = "id", defaultValue = "") Long id) {
		BookInventory bookInventory = bookInventoryService.findOne(id);
		return bookInventory;
	}
	
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		BookInventory bookInventory = bookInventoryService.findOne(id);
		model.put("bookInventory", bookInventory);
		return "/store/book_inventory_view";
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		bookInventoryService.delete(id);
		return indexRedirect;
	}
	@RequestMapping(value = "/expire", method = RequestMethod.GET)
	public  String findExpire(Long type,ModelMap model){
		List<BookInventory>    bookinventorylist=bookInventoryService.findExpire(type);
		model.addAttribute("bookinventoryList", bookinventorylist);
		model.addAttribute("type",type);
		return  "/store/inventory_expire";
	}
	private void setModelMapValue(ModelMap model,Set<Product>  products) {
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("products", products);
		model.addAttribute("weightMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
		model.addAttribute("amountMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.数量));
		model.addAttribute("packings", packingService.findAll());
		
		model.addAttribute("productStatus", productStatusService.findAll());
	}
}
