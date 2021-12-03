package com.wwyl.controller.store;

import java.util.Date;
import java.util.Map;
 
import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Result;
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
import com.wwyl.Constants;
import com.wwyl.Enums;
import com.wwyl.Enums.StockTakeResultStatus;
import com.wwyl.Enums.StockTakingStatus;
import com.wwyl.Enums.StoreLocationStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.StoreLocationDao;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.StockTaking;
import com.wwyl.entity.store.StockTakingItem;
import com.wwyl.entity.store.StockTakingResult;
import com.wwyl.service.security.SecurityService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.StockTakingService;
/**
 * 盘点
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/store/stock-taking")
public class StockTakingController extends BaseController {
	@Resource
	CustomerService customerService;
	@Resource
	private ProductService productService;
	@Resource
	private MeasureUnitService measureUnitService;
	@Resource
	private StoreLocationService storeLocationService;
	@Resource
	private StockTakingService stockTakingService;
	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private StoreAreaService storeAreaService;
	@Resource
	private SecurityService securityService;
	@Resource
	private StoreLocationDao storeLocationDao;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model,@RequestParam(value = "operate", defaultValue ="0") int operate) {
		model.addAttribute("operate", operate);
		model.addAttribute("stockTakingStatus", StockTakingStatus.values());
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo,StockTakingStatus   stockTakingStatus,@RequestParam(value = "operate", defaultValue = "0") int  operate) {
		if(operate==1){
			stockTakingStatus=StockTakingStatus.待审核;
		}
		Page<StockTaking> stockTakings = stockTakingService.findStockTakingSpecification(page, rows, serialNo, stockTakingStatus);
		return toEasyUiDatagridResult(stockTakings);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id,@RequestParam(value = "operate", defaultValue = "0") int operate,ModelMap model) {
		StockTaking stockTaking = stockTakingService.findOne(id);
		model.addAttribute("stockTaking", stockTaking);
		model.addAttribute("operate", operate);
		return "/store/stock_taking_view";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("operators",securityService.findOperatorsByRole(Constants.ROLE_KEEPER));
		model.addAttribute("storeAreas", storeAreaService.findValidStoreArea());
		return formPage;
	}

	@RequestMapping(value="/save", method = RequestMethod.POST)
	public String create(StockTaking stockTaking ,Long storeAreaId ,ModelMap model) {
		Long  stockTakingId=stockTakingService.save(storeAreaId,stockTaking);
		
		return "redirect:/store/stock-taking/"+stockTakingId;
	}

	@RequestMapping(value = "/{id}/remove", method = RequestMethod.GET)
	public String remove(@PathVariable Long id, ModelMap model) {
		stockTakingService.remove(id);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		StockTaking stockTaking = stockTakingService.findOne(id);
		model.addAttribute("stockTaking", stockTaking);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(StockTaking stockTaking ,Long storeAreaId) {
	 
		Long stockTackingId=stockTakingService.save(storeAreaId,stockTaking);
		return "redirect:/store/stock-taking/"+stockTackingId;
	}
	@RequestMapping(value = "/select-storeContainer", method = RequestMethod.POST)
	@ResponseBody
	public StockTakingResult selectStoreLocation(String storeContainer) {

		StockTakingResult stockTakingResult =null;
	 
		if (!StringUtils.isBlank(storeContainer)) {
			 
			BookInventory bookInventory =bookInventoryService.findByContainer(Long.valueOf(storeContainer));
			if(bookInventory!=null){
				stockTakingResult= new StockTakingResult();
				stockTakingResult.setAmount(bookInventory.getAmount());
				stockTakingResult.setAmountMeasureUnit(bookInventory.getAmountMeasureUnit());
				stockTakingResult.setProduct(bookInventory.getProduct());
				stockTakingResult.setStoreContainer(bookInventory.getStoreContainer());
				stockTakingResult.setStoreLocation(bookInventory.getStoreLocation());
				stockTakingResult.setWeight(bookInventory.getWeight());
				stockTakingResult.setWeightMeasureUnit(bookInventory.getWeightMeasureUnit());
				stockTakingResult.setCustomer(bookInventory.getCustomer());
			}
		}  
 
		return stockTakingResult;
	}

	@RequestMapping(value = "/{id}/delete-rsult", method = RequestMethod.GET)
	public String deleteRsult(@PathVariable Long id, ModelMap model) {
		
		StockTakingResult stockTakingResult=stockTakingService.findOneDifferentRsult(id);
		Long stockTakingId=stockTakingResult.getStockTaking().getId();
		StockTaking stockTaking=stockTakingService.findOne(stockTakingId);
		//区分复盘与初盘的 删除操作
		
		if(stockTaking.getStockTakingOld() != null){
			//初盘全盘数据
			StockTakingResult stockTakingResultNew = stockTakingService.findStockTakingRsult(stockTaking.getStockTakingOld().getId(), stockTakingResult.getStoreLocation().getId());	
			//初盘差异数据
			StockTakingResult stockTakingDifferentResult=stockTakingService.findStockTakingDifferentRsult(stockTaking.getStockTakingOld().getId(), stockTakingResult.getStoreLocation().getId());
			
			if(stockTakingDifferentResult != null && stockTakingDifferentResult.getId() != null)
			{
				stockTakingResultNew.setStockTakeResultStatus(StockTakeResultStatus.全盘变动);
				stockTakingResultNew.setStockTakingAmount(stockTakingDifferentResult.getStockTakingAmount());
				stockTakingResultNew.setStockTakingWeight(stockTakingDifferentResult.getStockTakingWeight());
				if(stockTakingDifferentResult.getToStoreLocation() != null){
					stockTakingResultNew.setToStoreLocation(stockTakingDifferentResult.getToStoreLocation());
				}else
				{
					stockTakingResultNew.setToStoreLocation(null);
				}
				stockTakingService.saveStockTakingResult(stockTakingResultNew);
			}else
			{
				stockTakingResultNew.setStockTakeResultStatus(StockTakeResultStatus.全盘变动);
				stockTakingResultNew.setStockTakingAmount(stockTakingResultNew.getAmount());
				stockTakingResultNew.setStockTakingWeight(stockTakingResultNew.getWeight());
				stockTakingResultNew.setToStoreLocation(null);
				stockTakingService.saveStockTakingResult(stockTakingResultNew);
			}
		}else
		{
			StockTakingResult stockTakingResultNew=stockTakingService.findStockTakingRsult(stockTakingId, stockTakingResult.getStoreLocation().getId());
			if(stockTakingResultNew!=null && stockTakingResultNew.getId()!=null){
				stockTakingResultNew.setStockTakeResultStatus(StockTakeResultStatus.全盘变动);
				stockTakingResultNew.setStockTakingAmount(stockTakingResultNew.getAmount());
				stockTakingResultNew.setStockTakingWeight(stockTakingResultNew.getWeight());
				stockTakingResultNew.setToStoreLocation(null);
				stockTakingService.saveStockTakingResult(stockTakingResultNew);
			}
		}
		
		stockTakingService.deleteRsult(id);
		
		return "redirect:/store/stock-taking/"+stockTakingId;
	}
	
	@RequestMapping(value = "/{id}/edit-result", method = RequestMethod.GET)
	public String editResult(@PathVariable Long id , Long resultId,ModelMap model) {
		
		StockTaking  stockTaking=stockTakingService.findOne(id);
		if(resultId!=null){
			StockTakingResult stockTakingResult=stockTakingService.findOneDifferentRsult(resultId);
			model.addAttribute("resultId", stockTakingResult.getId());
			model.addAttribute("stockTakingResult", stockTakingResult);
		}
		model.addAttribute("weightMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
		model.addAttribute("amountMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.数量));
		model.addAttribute("stockTaking", stockTaking);
		return "/store/stock_taking_item_form";
	}
	@RequestMapping(value = "/{id}/new-result", method = RequestMethod.GET)
	public String newResult(@PathVariable Long id , Long resultId,ModelMap model) {
		StockTaking  stockTaking=stockTakingService.findOne(id);
		if(resultId!=null){
			StockTakingResult stockTakingResult=stockTakingService.findOneDifferentRsult(resultId);
			model.addAttribute("stockTakingResult", stockTakingResult);
		}
		model.addAttribute("weightMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
		model.addAttribute("amountMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.数量));
		model.addAttribute("stockTaking", stockTaking);
		return "/store/stock_taking_item_form";
	}
	
	@RequestMapping(value = "/{id}/add-result", method = RequestMethod.GET)
	public String addResult(@PathVariable Long id , Long resultId,ModelMap model) {
		StockTaking  stockTaking=stockTakingService.findOne(id);
		
		if(resultId!=null){
			StockTakingResult stockTakingResult=stockTakingService.findOneResult(resultId);
			StockTakingResult stockTakingResultNew=new StockTakingResult();
			stockTakingResultNew.setAmount(stockTakingResult.getAmount());
			stockTakingResultNew.setAmountMeasureUnit(stockTakingResult.getAmountMeasureUnit());
			stockTakingResultNew.setCustomer(stockTakingResult.getCustomer());
			stockTakingResultNew.setProduct(stockTakingResult.getProduct());
			stockTakingResultNew.setStockTaking(stockTaking);
			stockTakingResultNew.setStockTakingAmount(stockTakingResult.getStockTakingAmount());
			stockTakingResultNew.setStockTakingWeight(stockTakingResult.getStockTakingWeight());
			stockTakingResultNew.setStoreContainer(stockTakingResult.getStoreContainer());
			stockTakingResultNew.setStoreLocation(stockTakingResult.getStoreLocation());
			stockTakingResultNew.setToStoreLocation(stockTakingResult.getToStoreLocation());
			stockTakingResultNew.setWeight(stockTakingResult.getWeight());
			stockTakingResultNew.setWeightMeasureUnit(stockTakingResult.getWeightMeasureUnit());
			stockTakingResultNew.setStockTakeResultStatus(StockTakeResultStatus.差异);
			stockTakingService.saveStockTakingResult(stockTakingResultNew);
		}
 
		return "redirect:/store/stock-taking/" +id; 
	}
	@RequestMapping(value = "/save-result", method = RequestMethod.POST)
	public String saveResult(@ModelAttribute("stockTakingResult") @Valid StockTakingResult stockTakingResult, BindingResult result,
		  ModelMap model) {
		StockTakingResult stockTakingResultNew=new StockTakingResult();
		StockTaking stockTaking =stockTakingService.findOne(stockTakingResult.getStockTaking().getId());
		//区分复盘与初盘
		if(stockTaking.getStockTakingOld() != null){
			stockTakingResultNew = stockTakingService.findStockTakingRsult(stockTaking.getStockTakingOld().getId(), stockTakingResult.getStoreLocation().getId());	
		}else
		{
			stockTakingResultNew = stockTakingService.findStockTakingRsult(stockTakingResult.getStockTaking().getId(), stockTakingResult.getStoreLocation().getId());
		}
		if(stockTakingResultNew!=null){
			stockTakingResultNew.setAmount(stockTakingResult.getAmount());
			stockTakingResultNew.setAmountMeasureUnit(stockTakingResult.getAmountMeasureUnit());
			stockTakingResultNew.setCustomer(stockTakingResult.getCustomer());
			stockTakingResultNew.setProduct(stockTakingResult.getProduct());
			stockTakingResultNew.setStockTakingAmount(stockTakingResult.getStockTakingAmount());
			stockTakingResultNew.setStockTakingWeight(stockTakingResult.getStockTakingWeight());
			stockTakingResultNew.setStoreContainer(stockTakingResult.getStoreContainer());
			stockTakingResultNew.setStoreLocation(stockTakingResult.getStoreLocation());
			
			if (stockTakingResult.getToStoreLocation()!=null&&StringUtils.isNotBlank(stockTakingResult.getToStoreLocation().getCode())) {
				StoreLocation tostoreLocation = storeLocationDao
						.findByCodeAndStatus(stockTakingResult.getToStoreLocation()
								.getCode(), new StoreLocationStatus[] {
								StoreLocationStatus.可使用, StoreLocationStatus.预留,
								StoreLocationStatus.维护 });
				if (tostoreLocation == null) {
					throw new RuntimeException("重选货架的编码不正确，或货架已使用和未绑定！");
				} else {
					stockTakingResultNew.setToStoreLocation(tostoreLocation);
				}
			}else{
				stockTakingResultNew.setToStoreLocation(null);
			}
			stockTakingResultNew.setWeightMeasureUnit(stockTakingResult.getWeightMeasureUnit());
			stockTakingResultNew.setStockTakeResultStatus(StockTakeResultStatus.全盘变动);
			stockTakingService.saveStockTakingResult(stockTakingResultNew);
		}
			stockTakingService.saveStockTakingResult(stockTakingResult);
		
		
		return "redirect:/store/stock-taking/" +stockTakingResult.getStockTaking().getId();
	}
	@RequestMapping(value = "/{id}/setempty", method = RequestMethod.GET)
	public String setEmpty(@PathVariable Long id) {
		StockTakingItem   stockTakingItem=stockTakingService.findOneItem(id);
		stockTakingService.setEmpty(stockTakingItem);
		return "redirect:/store/stock-taking/" +stockTakingItem.getStockTaking().getId();
	}
	@RequestMapping(value = "/relocation", method = RequestMethod.POST)
	public String relocation( Long id,String tostockLacation) {
		StockTakingItem   stockTakingItem=stockTakingService.findOneItem(id);
		stockTakingService.relocation(stockTakingItem,tostockLacation);
		return "redirect:/store/stock-taking/" +stockTakingItem.getStockTaking().getId();
	}
	
	/*@RequestMapping(value = "/{id}/edit-result", method = RequestMethod.GET)
	public String editItem(@PathVariable Long id , ModelMap model) {
		setItemModelMapvalue(model);
		StockWastageItem  stockWastageItem=stockTakingService.findOneItem(id);
		BookInventory bookInventory =bookInventoryService.findByLocation(stockWastageItem.getStoreLocation().getCode());
		model.addAttribute("stockWastageItem", stockWastageItem);
		model.addAttribute("bookInventory", bookInventory);
		model.put("stockWastageId", stockWastageItem.getStockWastage().getId());
		return "/store/stock_wastage_item_form";
	}

	@RequestMapping(value = "/save-item", method = RequestMethod.POST)
	public String saveItem(@ModelAttribute("stockWastageItem") @Valid StockWastageItem stockWastageItem, BindingResult result,String locationCode,
			 ModelMap model) {
	 
		Long stockWastageId= stockTakingService.SaveStockWastageItem(stockWastageItem, locationCode);
		 
		return "redirect:/store/stock-wastage/" + stockWastageId;
	}*/

/*	@RequestMapping(value = "/new-item", method = RequestMethod.GET)
	public String newItem(Long stockWastageId, ModelMap model) {
		StockWastage stockWastage=stockTakingService.findOne(stockWastageId);
		setItemModelMapvalue(model);
		model.put("customerId", stockWastage.getCustomer().getId());
		model.put("stockWastageId",stockWastage.getId());
		return "/store/stock_wastage_item_form";
	}*/

/*	private void setItemModelMapvalue(ModelMap model) {
		model.addAttribute("products", productService.findAll());
		model.addAttribute("weightMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
		model.addAttribute("amountMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.数量));
		model.addAttribute("customers", customerService.findAll());
	}*/

	//完成
	@RequestMapping(value="{id}/complete",method = RequestMethod.GET)
	public String complete(@PathVariable Long id) {
		stockTakingService.complete(id);
		return "redirect:/store/stock-taking/"+id;
	}

	//复盘
	@RequestMapping(value="{id}/retaking",method = RequestMethod.GET)
	public String reTaking(@PathVariable Long id,Date retakingTime) {
		Long stockTakingId=stockTakingService.reTaking(id,retakingTime);
		return "redirect:/store/stock-taking/"+stockTakingId;
	} 
	
	//审核
	@RequestMapping(value="{id}/check",method = RequestMethod.POST)
	public String check(@PathVariable Long id,int operate,boolean result) {
		stockTakingService.checked(id, result);
		return "redirect:/store/stock-taking/?operate="+operate;
	} 
 
	//提交审核
	@RequestMapping(value="{id}/commit-check",method = RequestMethod.GET)
	public String commitChecked(@PathVariable Long id) {
		stockTakingService.commitChecked(id);
		return "redirect:/store/stock-taking/";
	} 
	
	
}
