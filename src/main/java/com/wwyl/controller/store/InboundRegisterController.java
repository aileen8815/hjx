package com.wwyl.controller.store;

import java.util.Date;
import java.util.HashMap;
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
import com.wwyl.Enums.HandSetStatus;
import com.wwyl.Enums.InboundType;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.StockInStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.store.BookInventoryDao;
import com.wwyl.dao.store.BookInventoryHisDao;
import com.wwyl.dao.store.InboundReceiptItemDao;
import com.wwyl.dao.store.StockInDao;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.ce.CalculatedResult;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.BookInventoryHis;
import com.wwyl.entity.store.InboundBooking;
import com.wwyl.entity.store.InboundBookingItem;
import com.wwyl.entity.store.InboundReceiptItem;
import com.wwyl.entity.store.InboundRegister;
import com.wwyl.entity.store.InboundRegisterItem;
import com.wwyl.entity.store.StockIn;
import com.wwyl.service.ce.CEFeeItemService;
import com.wwyl.service.ce.StoreCalculator;
import com.wwyl.service.finance.PaymentService;
import com.wwyl.service.settings.ChargeTypeService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.HandSetService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.PackingService;
import com.wwyl.service.settings.ProductCategoryService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.settings.TallyAreaService;
import com.wwyl.service.settings.TaskService;
import com.wwyl.service.settings.VehicleTypeService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.InboundBookingService;
import com.wwyl.service.store.InboundReceiptService;
import com.wwyl.service.store.InboundRegisterService;
import com.wwyl.service.store.InboundTarryService;
import com.wwyl.service.store.OutboundTarryService;

/**
 * 入库登记
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/store/inbound-register")
public class InboundRegisterController extends BaseController {

	@Resource
	InboundRegisterService inboundRegisterService;
	@Resource
	InboundBookingService bookingService;
	@Resource
	VehicleTypeService vehicleTypeService;
	@Resource
	StoreAreaService storeAreaService;
	@Resource
	CustomerService customerService;
	@Resource
	SerialNumberService serialNumberService;
	@Resource
	private ProductService productService;
	@Resource
	private MeasureUnitService measureUnitService;
	@Resource
	private PackingService packingService;
	@Resource
	private StoreLocationService storeLocationService;
	@Resource
	TallyAreaService tallyAreaService;
	@Resource
	private InboundReceiptService inboundReceiptService;
	@Resource
	private PaymentService paymentService;
	@Resource
	private ChargeTypeService chargeTypeService;
		
	@Resource
	private  StoreCalculator  storeCalculator;
	
	@Resource
	private  ProductCategoryService  productCategoryService;
	
	@Resource
	private HandSetService handSetService;
	@Resource
	private InboundTarryService inboundTarryService;
	
	@Resource
	private OutboundTarryService outboundTarryService;
	
	@Resource
	private CEFeeItemService ceFeeItemService;
	
	@Resource
	private StockInDao stockInDao;
	
	@Resource
	private InboundReceiptItemDao inboundReceiptItemDao;
	
	@Resource
	private BookInventoryDao bookInventoryDao;
	@Resource
	private BookInventoryHisDao bookInventoryHisDao;
	
	
	
	@Resource
	TaskService  taskService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model,Long operationType) {
		model.addAttribute("inboundTypes", InboundType.values());
		model.addAttribute("stockInStatus", StockInStatus.values());
		model.addAttribute("customers", customerService.findAll());
		//0,入库登记，1入库结算，2延迟付款 3，入库付款，4分配储位
		model.addAttribute("operationType", operationType);
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo,InboundType   inboundType,Long customerId,@RequestParam(value = "operationType", defaultValue = "0") Long operationType,StockInStatus  stockInStatu) {
		StockInStatus[] stockInStatus=null;
		PaymentStatus[] paymentStatus=null;
		if(operationType==0&&stockInStatu!=null){
			stockInStatus=new StockInStatus[]{stockInStatu};
		}else if(operationType==1){
			stockInStatus=new StockInStatus[]{StockInStatus.已清点,StockInStatus.上架中,StockInStatus.已上架};
		}else if(operationType==2){
			paymentStatus=new PaymentStatus[]{PaymentStatus.延付待审核};
		}else if(operationType==3){
			paymentStatus=new PaymentStatus[]{PaymentStatus.未付款,PaymentStatus.延付已生效,PaymentStatus.延付已拒绝};
		}/*else if(operationType==4){
			paymentStatus=new PaymentStatus[]{PaymentStatus.已付款,PaymentStatus.延付已生效};
			stockInStatus=StockInStatus.已清点;
		}*/
		Page<InboundRegister> inboundRegisters = inboundRegisterService.findInboundRegisterByConditions(page, rows,paymentStatus, stockInStatus,serialNo,  inboundType, customerId);
		return toEasyUiDatagridResult(inboundRegisters);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id,@RequestParam(value = "operationType", defaultValue = "0") Long operationType,boolean newbooking, ModelMap model) {
		//InboundRegister inboundRegister = inboundRegisterService.findOne(id);
		InboundRegister inboundRegister = inboundRegisterService.findOne(id);
		model.addAttribute("inboundRegister", inboundRegister);
		List<InboundReceiptItem> inboundReceiptItems = inboundReceiptItemDao.findInboundReceiptItemByRegisterId(inboundRegister.getId());
		List<StockIn> stockIns = stockInDao.findStockInByRegisterId(inboundRegister.getId());
        String serialNo=inboundRegister.getSerialNo();
        if(serialNo.length()>9){
        	serialNo=serialNo.substring(serialNo.length()-9, serialNo.length());
        }
        //打印条码code
        model.addAttribute("serialNo", serialNo);
        model.addAttribute("stockIns", stockIns);
		model.addAttribute("inboundReceiptItems", inboundReceiptItems);
		model.addAttribute("tallyAreas", tallyAreaService.findAll());
		model.addAttribute("handSets", handSetService.findHandSetStatus(HandSetStatus.正常));
		model.addAttribute("newbooking", newbooking);
		model.addAttribute("operationType",operationType);
		model.addAttribute("outboundTarry",outboundTarryService.findByhandsetTask());
		model.addAttribute("inboundTarry",inboundTarryService.findHandsetTask());
		//储位
		String storeLocationCodes  = inboundRegister.getPreStoreLocationsCode();
		if (storeLocationCodes!="") {
			model.addAttribute("locationCodes", storeLocationCodes);
		}
		return "/store/inbound_register_view";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("inboundRegister", new InboundRegister());
		setModelMapvalue(model);
		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("inboundRegister") @Valid InboundRegister inboundRegister, BindingResult result,
			String[] productCheck,ModelMap model) {
		inboundRegister.setSerialNo(serialNumberService.getSerialNumber(TransactionType.InboundRegister));
		inboundRegister.setRegisterTime(new Date());
		inboundRegister.setRegisterOperator(ThreadLocalHolder.getCurrentOperator());
		inboundRegister.setStockInStatus(StockInStatus.已派送);
		Long inboundRegisterId=inboundRegisterService.save(inboundRegister,productCheck );
		boolean newbooking=false;
		if(productCheck!=null&&inboundRegister.getInboundBooking()!=null&&inboundRegister.getInboundBooking().getId()!=null){
			newbooking=inboundRegisterService.validation(inboundRegister.getInboundBooking().getId(),productCheck);
		}
		return "redirect:/store/inbound-register/" + inboundRegisterId+"?newbooking="+newbooking;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		inboundRegisterService.delete(id);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		setModelMapvalue(model);
		InboundRegister inboundRegister = inboundRegisterService.findOne(id);
		model.addAttribute("_method", "put");
		model.addAttribute("inboundRegister", inboundRegister);
		InboundBooking inbooking = inboundRegister.getInboundBooking();
		if (inbooking != null) {
			model.addAttribute("inboundBookingId", inbooking.getId());
		}
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("inboundRegister") @Valid InboundRegister inboundRegister, BindingResult result, String storeContainerCountIds, String inboundBookingId,
			ModelMap model) {
/*		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			this.printBindingResult(result);
			return formPage;
		}*/
		//Set<StoreLocation> storeLocationSet = storeLocationService.findByCodeIn(storeContainerCountIds.split(";"));
		//inboundRegister.setPreStoreLocations(storeLocationSet);
		inboundRegister.setRegisterOperator(ThreadLocalHolder.getCurrentOperator());
		inboundRegisterService.update(inboundRegister);
		return "redirect:/store/inbound-register/" + inboundRegister.getId();
	}
	

	@RequestMapping(value = "items", method = RequestMethod.GET)
	@ResponseBody
	public List<InboundRegisterItem> items(@RequestParam(value = "serialNo", defaultValue = "") String serialNo) {
		List<InboundRegisterItem> inboundRegisterItemList = inboundRegisterService.findInboundRegisterItems(serialNo);
		return inboundRegisterItemList;
	}

	
	@RequestMapping(value = "index-item", method = RequestMethod.GET)
	public String indexItems(@RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		InboundRegister InboundRegisters = inboundRegisterService.findBySerialNo(serialNo);
		model.put("inboundRegister", InboundRegisters);
		model.put("serialNo", serialNo);
		return "/store/inbound_register_item";
	}

	@RequestMapping(value = "/{id}/delete-item", method = RequestMethod.GET)
	public String deleteItem(@PathVariable Long id,   Long inboundRegisterId, ModelMap model) {
		inboundRegisterService.deleteItem(id);
 
		return "redirect:/store/inbound-register/" + inboundRegisterId;
	}

	@RequestMapping(value = "/{id}/save-item", method = RequestMethod.POST)
	public String updateItem(@ModelAttribute("inboundRegisterItem") @Valid InboundRegisterItem inboundRegisterItem, BindingResult result, ModelMap model) {
		 
		inboundRegisterService.saveItem(inboundRegisterItem);
		 
		return "redirect:/store/inbound-register/" + inboundRegisterItem.getInboundRegister().getId();
	}

	@RequestMapping(value = "/{id}/edit-item", method = RequestMethod.GET)
	public String editItem(@PathVariable Long id, Long inboundRegisterId, ModelMap model) {
		InboundRegister inboundRegister=inboundRegisterService.findOne(inboundRegisterId);
		setItemModelMapvalue(model,inboundRegister.getCustomer());
		model.addAttribute("inboundRegisterItem", inboundRegisterService.findOneItem(id));
		model.put("inboundRegisterId", inboundRegisterId);
		return "/store/inbound_register_item_form";
	}
	
	@RequestMapping(value = "/{id}/edit-product-detail", method = RequestMethod.GET)
	public String editProductDetail(@PathVariable Long id, Long inboundRegisterId, ModelMap model) {
		InboundRegister inboundRegister=inboundRegisterService.findOne(inboundRegisterId);
		setItemModelMapvalue(model,inboundRegister.getCustomer());
		model.addAttribute("inboundRegisterItem", inboundRegisterService.findOneItem(id));
		model.put("inboundRegisterId", inboundRegisterId);
		return "/store/inbound_register_product_detail_form";
	}
	
	//修改所有改动过的多品明细
	@RequestMapping(value = "/{id}/save-product-detail", method = RequestMethod.POST)
	public String updateProductDetail(@ModelAttribute("inboundRegisterItem") @Valid InboundRegisterItem inboundRegisterItem, BindingResult result, ModelMap model) {
		 
		String productDetail = inboundRegisterItem.getProductDetail();
		//明细
		InboundRegisterItem inboundRegisterItemTemp = inboundRegisterService.findOneItem(inboundRegisterItem.getId());
		inboundRegisterItemTemp.setProductDetail(productDetail);
		inboundRegisterService.saveItem(inboundRegisterItemTemp);
		Long productId = inboundRegisterItem.getProduct().getId(); 
		 
		//收货检验
		List<InboundReceiptItem> inboundReceiptItems = inboundReceiptService.findInboundReceiptItemByRegisterIdAndProductId(inboundRegisterItem.getInboundRegister().getId(), productId);
		if (inboundReceiptItems != null) {
			for (InboundReceiptItem inboundReceiptItem:inboundReceiptItems){
				inboundReceiptItem.setProductDetail(productDetail);
				inboundReceiptItemDao.save(inboundReceiptItem);
			//库存
			StockIn stockIn = stockInDao.findStockInByInboundReceiptItemId(inboundReceiptItem.getId());
			if (stockIn != null) {
				BookInventory bookInventory = bookInventoryDao.findByStockinIdAndProductId(stockIn.getId(), productId);
				if (bookInventory != null) {
					bookInventory.setProductDetail(productDetail);
					bookInventoryDao.save(bookInventory);
				}
				//历史库存
				BookInventoryHis bookInventoryhis = bookInventoryHisDao.findHisByStockinIdAndProductId(stockIn.getId(), productId);
				if (bookInventoryhis != null) {
					bookInventoryhis.setProductDetail(productDetail);
					bookInventoryHisDao.save(bookInventoryhis);
				}
			}
			}
		}
		
		
		
		
		return "redirect:/store/inbound-register/" + inboundRegisterItem.getInboundRegister().getId();
	}

	@RequestMapping(value = "/save-item", method = RequestMethod.POST)
	public String addItem(@ModelAttribute("inboundRegisterItem") @Valid InboundRegisterItem inboundRegisterItem, BindingResult result , ModelMap model) {
		inboundRegisterService.saveItem(inboundRegisterItem);
		return "redirect:/store/inbound-register/" + inboundRegisterItem.getInboundRegister().getId();
	}

	@RequestMapping(value = "/new-item", method = RequestMethod.GET)
	public String newItem(Long inboundRegisterId, ModelMap model) {
		InboundRegister inboundRegister=inboundRegisterService.findOne(inboundRegisterId);
		setItemModelMapvalue(model,inboundRegister.getCustomer());
		model.put("inboundRegisterId", inboundRegisterId);
		return "/store/inbound_register_item_form";
	}
	//通过预约单进行入库登记
	@RequestMapping(value = "/{id}/register", method = RequestMethod.GET)
	public String register(@PathVariable Long id, ModelMap model) {
		InboundBooking inboundBooking = bookingService.findOne(id);
		InboundRegister inboundRegister = new InboundRegister();
		inboundRegister.setInboundBooking(inboundBooking);
		inboundRegister.setCustomer(inboundBooking.getCustomer());
		inboundRegister.setInboundTime(inboundBooking.getApplyInboundTime());
		inboundRegister.setVehicleAmount(inboundBooking.getVehicleAmount());
		inboundRegister.setVehicleNumbers(inboundBooking.getVehicleNumbers());
		inboundRegister.setVehicleType(inboundBooking.getVehicleType());
		
		model.put("inboundRegister", inboundRegister);
		setModelMapvalue(model);
		return "/store/inbound_register_form";
	}
	
	@RequestMapping(value = "/inboundbook-items", method = RequestMethod.GET)
	@ResponseBody
	public List<InboundBookingItem> inboundBookItems(Long inboundbookingId) {
		//设置已预约的商品数量
		Set<InboundBookingItem>	inboundBookingItems=null;
		List<InboundBookingItem>	margeinboundBookingItems=null;
		if(inboundbookingId!=null){
				inboundBookingItems = bookingService.findOne(inboundbookingId).getInboundBookingItems();
				margeinboundBookingItems=bookingService.mergeInboundBookingItem(inboundBookingItems);
		}
		return  margeinboundBookingItems;
	}
	@RequestMapping(value = "vali", method = RequestMethod.GET)
	@ResponseBody
	public boolean vali(Long inboundBookingId,String[] productChecks) {
		boolean  result = inboundRegisterService.validation(inboundBookingId, productChecks);
		return result;
	}
	private void setItemModelMapvalue(ModelMap model,Customer customer) {
		Long departmentId = 0L;
		//添加商品
		model.addAttribute("productCategorys", productCategoryService.findAll());
	    model.addAttribute("customer",customer);
	    
	    model.addAttribute("products",customer.getProducts());
	    
	    //部门高温低温库
		departmentId = ThreadLocalHolder.getCurrentOperator().getDepartment().getId();
		
		if (departmentId ==5) {
	    	model.addAttribute("storeAreas", storeAreaService.findByValidHighArea());
	    }else if(departmentId ==6){
	    	model.addAttribute("storeAreas", storeAreaService.findByValidLowArea());
	    }else
	    {
	    	model.addAttribute("storeAreas", storeAreaService.findValidStoreArea());	    	
	    }

		model.addAttribute("weightMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
		model.addAttribute("amountMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.数量));
		model.addAttribute("packings", packingService.findAll());
	}

	private void setModelMapvalue(ModelMap model) {
		Long departmentId = 0L;
		model.addAttribute("inboundTypes", InboundType.values());
		model.addAttribute("vehicleTypes", vehicleTypeService.findAll());
		model.addAttribute("tallyAreas", tallyAreaService.findAll());
		
		if (departmentId ==5) {
	    	model.addAttribute("storeAreas", storeAreaService.findByValidHighArea());
	    }else if(departmentId ==6){
	    	model.addAttribute("storeAreas", storeAreaService.findByValidLowArea());
	    }else
	    {
	    	model.addAttribute("storeAreas", storeAreaService.findValidStoreArea());	    	
	    }
		
		model.addAttribute("customers", customerService.findAll());
	}



	//完成
	@RequestMapping(value="/complete" ,method = RequestMethod.GET)
	public String complete(Long id) {
		inboundRegisterService.complete(id,StockInStatus.已完成);
		return "redirect:/store/inbound-register/"+id;
	}
	
/*
	//保存选择分配储位
	@RequestMapping(value="/save-location" ,method = RequestMethod.POST)
	public String saveLocation(Long inboundRegisterId,String locationCodes) {
	
		inboundRegisterService.saveLocation( inboundRegisterId, locationCodes);
		return "redirect:/store/inbound-register/"+inboundRegisterId+"?operationType=4";
	}*/
	//保存分配的理货区
	@RequestMapping(value="/{id}/save-tallyArea" ,method = RequestMethod.POST)
	public String saveTallyArea(@PathVariable Long id,Long  tallyAreaId,Long handSetId) {
		inboundRegisterService.saveTallyArea( id, tallyAreaId,handSetId);
		return "redirect:/store/inbound-register/"+id;
	}
	//结算

	@RequestMapping(value = "/{id}/settlement", method = RequestMethod.GET)
	public String settlement(@PathVariable Long id, ModelMap model) {
		model.addAttribute("id", id);
		InboundRegister inboundRegister=inboundRegisterService.findOne(id);
		model.addAttribute("chargeTypes", chargeTypeService.findAll());
		Set<CalculatedResult> calculatedResults =storeCalculator.calculate(inboundRegister);
		model.addAttribute("calculatedResults", calculatedResults);
		model.addAttribute("customer", inboundRegister.getCustomer());
		return "/store/inbound_register_settlement";
	}
	
//	明细单项计费
	@RequestMapping(value = "{id}/calculate-item", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> calculateItem(@PathVariable Long id, Long feeitemId, int storeContainerCount, double weightAmount, double amountPiece, ModelMap model) {
		InboundRegister inboundRegister=inboundRegisterService.findOne(id);
		model.addAttribute("chargeTypes", chargeTypeService.findAll());
		Map<String, Object> result = new HashMap<String, Object>();	
		CEFeeItem feeitem=ceFeeItemService.findOne(feeitemId);
		result =storeCalculator.calculateInboundRegisterItem(inboundRegister, feeitem, storeContainerCount, weightAmount, amountPiece);
		model.addAttribute("amount", result.get("amount"));
		model.addAttribute("ruleComment",result.get("ruleComment"));
		return result;
	}
		
	@RequestMapping(value = "/{id}/charge", method = RequestMethod.POST)
	public String charge(@PathVariable Long id, Double[] weight, Double[] amountPiece, int[] storeContainerCount, Long[] feeitem,String[] money,String[] actuallyMoney,Long chargeTypeId,boolean delayed,String[] ruleComment,  ModelMap model) {
		InboundRegister inboundRegister = inboundRegisterService.findOne(id);
		inboundReceiptService.settlement(inboundRegister, weight, amountPiece, storeContainerCount, feeitem, money, actuallyMoney, chargeTypeId,delayed,ruleComment);
/*		model.addAttribute("inboundTypes", InboundType.values());
		model.addAttribute("customers", customerService.findAll());
		//0,入库登记，1入库结算，2延迟付款 3，入库付款，4分配储位
		model.addAttribute("operationType", 1);
		model.addAttribute("mesg", "结算成功，点击确认打印付款单据！");
		model.addAttribute("id", id);*/
		return "redirect:/store/inbound-register/"+id;
	}
	
	//付款
	@RequestMapping(value="{id}/payment" ,method = RequestMethod.POST)
	public String payment(@PathVariable Long id, String aCardMac, String sMemberCode,String password,ModelMap model) {
		Map<String,Float> paymentResult = inboundRegisterService.payment(id,aCardMac,sMemberCode,password);
	/*	model.addAttribute("inboundTypes", InboundType.values());
		model.addAttribute("customers", customerService.findAll());
		//0,入库登记，1入库结算，2延迟付款 3，入库付款，4分配储位
		model.addAttribute("operationType", 3);
		model.addAttribute("mesg", "付款成功！");*/
		model.put("messge", "付款成功，本次付款金额："+paymentResult.get("totalAmount")+"元，客户卡上余额："+paymentResult.get("balance")+"元。");
		return "/succeed";
	}
	//延迟付款审核
	@RequestMapping(value="{id}/delay" ,method = RequestMethod.GET)
	public String delay(@PathVariable Long id,String remark,Long  paymentStatusType,ModelMap model) { 
		inboundRegisterService.delay(id,remark,paymentStatusType==1?PaymentStatus.延付已生效:PaymentStatus.延付已拒绝);
		model.addAttribute("inboundTypes", InboundType.values());
		model.addAttribute("customers", customerService.findAll());
		//0,入库登记，1入库结算，2延迟付款 3，入库付款，4分配储位
		model.addAttribute("operationType",2);
		model.addAttribute("mesg", paymentStatusType==1?"延迟付款已生效！":"延迟付款拒绝，要求付款！");
		return indexPage;
	}
	
	//完成清点
	@RequestMapping(value="{id}/complete-receipt" ,method = RequestMethod.GET)
	public String completeReceipt( @PathVariable Long id) {
		InboundRegister inboundRegister=inboundRegisterService.findOne(id);
		 Set<StockIn> stockInList = inboundRegister.getStockIns();
	        boolean result = true;
	        for (StockIn stockIn2 : stockInList) {
	            if (stockIn2.getStockInStatus() != StockInStatus.已上架) {
	                result = false;
	                break;
	            }
	      }
	      if(result){
	    	//上架完毕发通知
	    	  if (inboundRegister.getStockInStatus() != StockInStatus.已完成 && inboundRegister.getStockInStatus() != StockInStatus.已作废){
	    		  inboundRegisterService.updateStatus(inboundRegister.getId(),
			  				StockInStatus.已上架);
		          taskService.assignStockInEndNotice(inboundRegister); 
	    	  }
	      }else{
	    	  if (inboundRegister.getStockInStatus() != StockInStatus.已完成 && inboundRegister.getStockInStatus() != StockInStatus.已作废){
	    		  if(inboundRegister.getStockInStatus()==StockInStatus.已清点){
	    			  inboundRegister.setStockInStatus(StockInStatus.上架中);
	    		  }
	    		  if(inboundRegister.getStockInStatus()==StockInStatus.已派送 || inboundRegister.getStockInStatus()==StockInStatus.上架中){
		    		  inboundRegisterService.updateStatus(inboundRegister.getId(),
		  	  				StockInStatus.已清点);
		  	  		  taskService.assignInboundReceiptNotice(inboundRegister);
	    		  }
	    	  }
	      } 
		return "redirect:/store/inbound-register/"+id;
	}
	@RequestMapping(value="{id}/cancel-receipt" ,method = RequestMethod.GET)
	public String cancelReceipt(@PathVariable Long id) {
		inboundRegisterService.updateStatus(id,StockInStatus.已派送);
		return "redirect:/store/inbound-register/"+id;
	}
	//取消登记单
	@RequestMapping(value="{id}/cancel-register" ,method = RequestMethod.GET)
	public String cancelRegister(@PathVariable Long id) {
		inboundRegisterService.updateStatus(id,StockInStatus.已作废);
		return "redirect:/store/inbound-register/"+id;
	}
	//重新启用登记单
	@RequestMapping(value="{id}/start-using" ,method = RequestMethod.GET)
	public String startUsing(@PathVariable Long id) {
		inboundRegisterService.updateStatus(id,StockInStatus.已派送);
		return "redirect:/store/inbound-register/"+id;
	}
	
}
