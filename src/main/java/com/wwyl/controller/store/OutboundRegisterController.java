package com.wwyl.controller.store;

import java.text.DecimalFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.wwyl.entity.settings.Product;
import com.wwyl.entity.store.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Enums.HandSetStatus;
import com.wwyl.Enums.OutboundType;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.StockOutStatus;
import com.wwyl.Enums.StockOwnerChangeStatus;
import com.wwyl.Enums.VehicleSource;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.Enums.TransactionType;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.ce.CalculatedResult;
import com.wwyl.service.settings.BookingMethodService;
import com.wwyl.service.settings.ChargeTypeService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.HandSetService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.PackingService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.settings.TallyAreaService;
import com.wwyl.service.settings.VehicleTypeService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.InboundTarryService;
import com.wwyl.service.store.OutboundBookingService;
import com.wwyl.service.store.OutboundCheckService;
import com.wwyl.service.store.OutboundRegisterService;
import com.wwyl.service.store.OutboundTarryService;
import com.wwyl.service.store.StockOutService;
import com.wwyl.service.store.StockOwnerChangeService;
import com.wwyl.service.ce.CEFeeItemService;
import com.wwyl.service.ce.StoreCalculator;
import com.wwyl.service.finance.PaymentService;

/**
 * 出库登记
 *
 * @author jianl
 */
@Controller
@RequestMapping("/store/outbound-register")
public class OutboundRegisterController extends BaseController {
    @Resource
    OutboundRegisterService outboundRegisterService;
    @Resource
    OutboundBookingService outbookingService;
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
    private ProductService productService;
    @Resource
    private MeasureUnitService measureUnitService;
    @Resource
    private PackingService packingService;
    @Resource
    private StoreLocationService storeLocationService;
    @Resource
    private BookInventoryService bookInventoryService;
    @Resource
    TallyAreaService tallyAreaService;
    @Resource
    private OutboundCheckService outboundCheckService;
    @Resource
    private PaymentService paymentService;
    @Resource
    private ChargeTypeService chargeTypeService;
    @Resource
    private OutboundBookingService outboundBookingService;
    @Resource
    private StoreCalculator storeCalculator;
    @Resource
    private HandSetService handSetService;
    @Resource
    private StockOutService stockOutService;
    @Resource
    private StockOwnerChangeService stockOwnerChangeService;
    @Resource
    private InboundTarryService inboundTarryService;

    @Resource
    private OutboundTarryService outboundTarryService;

    @Resource
    private CEFeeItemService ceFeeItemService;


    @RequestMapping(method = RequestMethod.GET)
    public String index(Long operationType, ModelMap model) {
        model.addAttribute("outboundTypes", OutboundType.values());
        //0,出库登记，1出库结算，2延迟付款 3，出库付款，
        model.addAttribute("operationType", operationType);
        model.addAttribute("stockOutStatus", StockOutStatus.values());
        model.addAttribute("customers", customerService.findAll());
        return indexPage;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
                                    @RequestParam(value = "serialNo", defaultValue = "") String serialNo, OutboundType outboundType, Long customerId, @RequestParam(value = "operationType", defaultValue = "0") Long operationType, StockOutStatus stockOutStatu) {
        StockOutStatus stockOutStatus = null;
        PaymentStatus[] paymentStatus = null;
        if (operationType == 0) {
            stockOutStatus = stockOutStatu;
        } else if (operationType == 1) {
            stockOutStatus = StockOutStatus.已清点;
        } else if (operationType == 2) {
            paymentStatus = new PaymentStatus[]{PaymentStatus.延付待审核};
        } else if (operationType == 3) {
            paymentStatus = new PaymentStatus[]{PaymentStatus.未付款, PaymentStatus.延付已拒绝, PaymentStatus.延付已生效};
        }
        Page<OutboundRegister> outboundRegisters = outboundRegisterService.findOutboundRegisterConditions(page, rows, paymentStatus, stockOutStatus, serialNo, outboundType, customerId);
        return toEasyUiDatagridResult(outboundRegisters);
    }

    @RequestMapping(value = "/bookinventory-list", method = RequestMethod.GET)
    @ResponseBody
    public List<BookInventory> bookInventoryList(Long customerId, Long selectCustomerId, Long outboundRegisterId, Long outboundbookingId, String productId, String productionPlace,Date productionDate, ModelMap model) {
        customerId = selectCustomerId != null ? selectCustomerId : customerId;
        if (customerId == null) {
            customerId = -1L;
        }
        // 设置已预约的商品数量
        List<BookInventory> bookInventoryList = bookInventoryService
                .findBookInventoryInfo(customerId, null, null, productId, productionPlace, productionDate);

        // 从出库预约单正式登记
        if (outboundRegisterId == null && outboundbookingId != null) {
            Set<OutboundBookingItem> outboundBookingItemSet = outboundBookingService.findOne(outboundbookingId).getOutboundBookingItems();
            for (BookInventory bookInventory : bookInventoryList) {
                for (OutboundBookingItem outboundBookingItem : outboundBookingItemSet) {
                    if (bookInventory.getBatchProduct()
                            .equals(outboundBookingItem.getBatchProduct())) {
                        bookInventory.setOutboundAmount(outboundBookingItem
                                .getAmount());
                        bookInventory.setStoreContainerCount(outboundBookingItem.getStoreContainerCount());
                    }
                }
            }
        } else if (outboundRegisterId != null) {
            // 编辑
            Set<OutboundRegisterItem> outboundRegisterItemsSet = outboundRegisterService.findOne(outboundRegisterId).getOutboundRegisterItems();
            for (BookInventory bookInventory : bookInventoryList) {
                for (OutboundRegisterItem outboundRegisterItem : outboundRegisterItemsSet) {
                    if (bookInventory.getBatchProduct()
                            .equals(outboundRegisterItem.getBatchProduct())) {
                        bookInventory.setOutboundAmount(outboundRegisterItem
                                .getAmount());
                        bookInventory.setStoreContainerCount(outboundRegisterItem.getStoreContainerCount());
                    }
                }
            }

        }

        return bookInventoryList;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String view(@PathVariable Long id, Long operationType, boolean newbooking, ModelMap model) {
        OutboundRegister outboundRegister = outboundRegisterService.findOne(id);
        List<OutboundCheckItem> outboundCheckItems = outboundCheckService.findOutboundCheckItems(outboundRegister.getId());
        model.addAttribute("outboundRegister", outboundRegister);
        
        List<Map> outItemTotal = stockOutService.findStockOutTotalByOutboundRegisterId(outboundRegister.getId(), StockOutStatus.已拣货);
        
        model.addAttribute("outItemTotal", outItemTotal);
        String serialNo = outboundRegister.getSerialNo();
        if (serialNo.length() > 9) {
            serialNo = serialNo.substring(serialNo.length() - 9, serialNo.length());
        }
        //打印条码code
        model.addAttribute("serialNo", serialNo);
        model.addAttribute("outboundCheckItems", outboundCheckItems);
        model.addAttribute("tallyAreas", tallyAreaService.findAll());
        model.addAttribute("handSets", handSetService.findHandSetStatus(HandSetStatus.正常));
        model.addAttribute("newbooking", newbooking);
        //0,出库登记，1出库结算，2延迟付款 3，出库付款，
        model.addAttribute("operationType", operationType);
        model.addAttribute("outboundTarry", outboundTarryService.findByhandsetTask());
        model.addAttribute("inboundTarry", inboundTarryService.findHandsetTask());
        //图形分派拣货单，不需要显示的储位托盘id
        Set<OutboundRegisterItem> outboundRegisterItems = outboundRegister.getOutboundRegisterItems();
        if (!outboundRegisterItems.isEmpty()) {
            List<BookInventory> bookInventoryList = matchOutboundInventories(outboundRegister, null, null);
            String selectableStoreContainerstr = "";
            for (BookInventory bookInventory : bookInventoryList) {
                selectableStoreContainerstr += bookInventory.getStoreContainer().getId() + ",";

            }
            if (StringUtils.isNotBlank(selectableStoreContainerstr)) {
                selectableStoreContainerstr = selectableStoreContainerstr.substring(0, selectableStoreContainerstr.length() - 1);
            } else {
                selectableStoreContainerstr = "-1";
            }
            model.addAttribute("selectableStoreContainer", selectableStoreContainerstr);
        }
        return viewPage;
    }

    //保存分配的理货区
    @RequestMapping(value = "/{id}/save-tallyArea", method = RequestMethod.POST)
    public String saveTallyArea(@PathVariable Long id, Long tallyAreaId, Long handSetId) {
        //if(!StringUtils.isBlank(tallyAreas)){
        outboundRegisterService.saveTallyArea(id, tallyAreaId, handSetId);


        return "redirect:/store/outbound-register/" + id;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("outboundRegister", new OutboundRegister());
        model.put("products", productService.findAll());
        setModelMapvalue(model);
        return formPage;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String save(@ModelAttribute("outboundRegister") @Valid OutboundRegister outboundRegister, BindingResult result, String[] productCheck, ModelMap model) {
        outboundRegister.setSerialNo(serialNumberService.getSerialNumber(TransactionType.OutboundRegister));
        outboundRegister.setRegisterTime(new Date());
        outboundRegister.setRegisterOperator(ThreadLocalHolder.getCurrentOperator());
        outboundRegister.setStockOutStatus(StockOutStatus.已派送);
        outboundRegisterService.create(outboundRegister, productCheck);
        boolean newbooking = false;
        if (outboundRegister.getOutboundBooking() != null && outboundRegister.getOutboundBooking().getId() != null) {
            newbooking = outboundBookingService.validation(outboundRegister.getOutboundBooking().getId(), productCheck);
        }
        return "redirect:/store/outbound-register/" + outboundRegister.getId() + "?newbooking=" + newbooking;
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, ModelMap model) {
        outboundRegisterService.delete(id);
        return indexRedirect;
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, ModelMap model) {
        setModelMapvalue(model);
        OutboundRegister outboundRegister = outboundRegisterService.findOne(id);
        model.addAttribute("_method", "put");
        //StringBuffer buffer=new StringBuffer();
        //Set<OutboundRegisterItem> outboundRegisterItems=	outboundRegister.getOutboundRegisterItems();
        /*int i=0;
        int size=outboundRegisterItems.size();
		for (OutboundRegisterItem outboundRegisterItem : outboundRegisterItems) {
			i++;
			buffer.append(outboundRegisterItem.getProduct().getId()+"_"+outboundRegisterItem.getAmount());
			if(i!=size){
				buffer.append(",");
			}
		}*/
        //model.addAttribute("productCheck",buffer.toString());
        model.put("products", productService.findAll());
        model.addAttribute("outboundRegister", outboundRegister);
        return formPage;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String update(@ModelAttribute("outboundRegister") @Valid OutboundRegister outboundRegister, BindingResult result, String[] productCheck, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("_method", "put");
            List<ObjectError> errros = result.getAllErrors();
            for (ObjectError err : errros) {
                System.out.println(err.getDefaultMessage());
            }
            return formPage;
        }
        outboundRegister.setRegisterTime(new Date());
        outboundRegister.setRegisterOperator(ThreadLocalHolder.getCurrentOperator());
        outboundRegisterService.create(outboundRegister, productCheck);
        return "redirect:/store/outbound-register/" + outboundRegister.getId();
    }

    //完成
    @RequestMapping(value = "/complete", method = RequestMethod.GET)
    public String complete(Long id) {
        outboundRegisterService.updateStatus(id, StockOutStatus.已完成);
        return "redirect:/store/outbound-register/" + id;
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    @ResponseBody
    public List<OutboundRegisterItem> items(String serialNo) {
        List<OutboundRegisterItem> outboundRegisterItemList = outboundRegisterService.findOutboundRegisterItems(serialNo);
        return outboundRegisterItemList;
    }

/*	@RequestMapping(value = "/index-item", method = RequestMethod.GET)
    public String indexItems(@RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		List<OutboundRegister> outboundBookings = outboundRegisterService.findBySerialNo(serialNo);
		model.put("outboundRegister", outboundBookings.get(0));
		model.put("serialNo", serialNo);
		return "/store/outbound_register_item";
	}*/

    @RequestMapping(value = "/{id}/delete-item", method = RequestMethod.GET)
    public String deleteItem(@PathVariable Long id, String serialNo, ModelMap model) {
        outboundRegisterService.deleteItem(id);
        model.put("serialNo", serialNo);
        return "redirect:/store/outbound-register/index-item?serialNo=" + serialNo;
    }

    @RequestMapping(value = "/{id}/register", method = RequestMethod.GET)
    public String register(@PathVariable Long id, ModelMap model) {
        OutboundBooking outboundBooking = outbookingService.findOne(id);
        OutboundRegister outboundRegister = new OutboundRegister();
        outboundRegister.setOutboundBooking(outboundBooking);
        outboundRegister.setCustomer(outboundBooking.getCustomer());
        outboundRegister.setOutboundTime(outboundBooking.getApplyOutboundTime());
        outboundRegister.setVehicleAmount(outboundBooking.getVehicleAmount());
        outboundRegister.setVehicleNumbers(outboundBooking.getVehicleNumbers());
        outboundRegister.setVehicleType(outboundBooking.getVehicleType());
        model.put("outboundBooking", outboundBooking);
        model.put("outboundRegister", outboundRegister);
        model.put("products", productService.findAll());
        setModelMapvalue(model);
        return "/store/outbound_register_form";
    }

    private void setModelMapvalue(ModelMap model) {
        model.addAttribute("outboundTypes", OutboundType.values());
        model.addAttribute("vehicleTypes", vehicleTypeService.findAll());
        model.addAttribute("tallyAreas", tallyAreaService.findAll());
        model.addAttribute("storeAreas", storeAreaService.findValidStoreArea());
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("vehicleSources", VehicleSource.values());
    }


    //分派托盘
    @RequestMapping(value = "/save-item", method = RequestMethod.GET)
    public String saveItem(String ids, Long registerId, ModelMap model) {
        OutboundRegister outboundRegister = outboundRegisterService.findOne(registerId);
        outboundRegisterService.saveItem(ids, outboundRegister);

        return "redirect:/store/outbound-register/" + outboundRegister.getId();
    }

    //删除已分派的托盘
    @RequestMapping(value = "/del-stockout", method = RequestMethod.GET)
    public String delstockout(Long stockoutId, Long outboundRegisterId, ModelMap model) {
        stockOutService.cancellation(stockoutId);
        return "redirect:/store/outbound-register/" + outboundRegisterId;
    }

    @RequestMapping(value = "/index-new-item", method = RequestMethod.GET)
    public String indexNewItem(Long registerId, String id, ModelMap model) {
        model.put("registerId", registerId);
        OutboundRegister outboundRegister = outboundRegisterService.findOne(registerId);
        Set<OutboundRegisterItem> outboundRegisterItems = outboundRegister.getOutboundRegisterItems();
        double amount = 0.00;
        double weight = 0.00;
        List<Product> products = new ArrayList<Product>();
        for (OutboundRegisterItem outboundRegisterItem : outboundRegisterItems) {
            amount += outboundRegisterItem.getAmount();
            weight += outboundRegisterItem.getWeight();
            products.add(outboundRegisterItem.getProduct());
        }
        DecimalFormat df = new DecimalFormat("######0.00");

        model.addAttribute("outboundTarrys", outboundRegister.getOutboundTarrys());
        model.addAttribute("amount", df.format(amount));
        model.addAttribute("weight", df.format(weight));
        model.addAttribute("storeAreas", storeAreaService.findAll());
        model.addAttribute("customer", outboundRegister.getCustomer().getId());
        model.addAttribute("products", products);
        return "/store/outbound_register_item_add";
    }

    @RequestMapping(value = "/item-list", method = RequestMethod.GET)
    @ResponseBody
    public List<BookInventory> itemList(Long registerId, Long product, Long storeArea, ModelMap model) {
        OutboundRegister outboundRegister = outboundRegisterService.findOne(registerId);
        List<BookInventory> bookInventoryList = null;

        if (!outboundRegister.getOutboundRegisterItems().isEmpty()) {
            bookInventoryList = matchOutboundInventories(outboundRegister, product, storeArea);
        }

        return bookInventoryList;
    }

    private List<BookInventory> matchOutboundInventories(OutboundRegister outboundRegister, Long product, Long storeArea) {
        List<BookInventory> bookInventoryList;
        List<Long> products = new ArrayList<Long>();
        List<String> batchNos = new ArrayList<String>();
        Long[] productIds;
        String[] productBatch;
        Set<OutboundRegisterItem> outboundRegisterItems = outboundRegister.getOutboundRegisterItems();
        for (OutboundRegisterItem outboundRegisterItem : outboundRegisterItems) {
            if (product == null || product.equals(outboundRegisterItem.getProduct().getId())) {
                products.add(outboundRegisterItem.getProduct().getId());
                batchNos.add(outboundRegisterItem.getBatchs());
            }
        }
        productIds = new Long[products.size()];
        products.toArray(productIds);
        productBatch = new String[batchNos.size()];
        batchNos.toArray(productBatch);
        bookInventoryList = bookInventoryService.findBookInventorySpecificationList(
                productIds, productBatch, outboundRegister.getCustomer().getId(), storeArea);

        List<BookInventory> removeBookInvenrory = new ArrayList<BookInventory>();
        List<Long> storeContainers = new ArrayList<Long>();
        Long[] storeContainerIds = new Long[storeContainers.size()];
        storeContainers.toArray(storeContainerIds);
        //已存在未完成的拣货单
        List<Long> stockOutStoreContainers = stockOutService.findUseingStockOut(products.toArray(), outboundRegister.getCustomer().getId());
        //货权转移已选位的托盘
        List<Long> checkedStoreContainers = stockOwnerChangeService.findByStockOwnerChangeStatus(StockOwnerChangeStatus.已选位);
        if (!checkedStoreContainers.isEmpty()) {
            storeContainers.addAll(checkedStoreContainers);
        }
        if (!stockOutStoreContainers.isEmpty()) {
            storeContainers.addAll(stockOutStoreContainers);
        }
        /**
         * 去掉正在使用的托盘
         */
        for (BookInventory bookInventory : bookInventoryList) {
            for (Long storeContainerid : storeContainers) {
                if (bookInventory.getStoreContainerId().equals(storeContainerid.toString())) {
                    removeBookInvenrory.add(bookInventory);
                }
            }
        }
        if (!removeBookInvenrory.isEmpty()) {
            bookInventoryList.removeAll(removeBookInvenrory);
        }
        return bookInventoryList;
    }

    //结算
    @RequestMapping(value = "/{id}/settlement", method = RequestMethod.GET)
    public String settlement(@PathVariable Long id, ModelMap model) {
        model.addAttribute("id", id);
        model.addAttribute("chargeTypes", chargeTypeService.findAll());
        OutboundRegister outboundRegister = outboundRegisterService.findOne(id);
        Set<CalculatedResult> calculatedResults = outboundRegisterService.saveCalculateData(outboundRegister);
        model.addAttribute("calculatedResults", calculatedResults);
        model.addAttribute("customer", outboundRegister.getCustomer());
        return "/store/outbound_register_settlement";
    }

    //	明细单项计费
    @RequestMapping(value = "{id}/calculate-item", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> calculateItem(@PathVariable Long id, Long feeitemId, int storeContainerCount, double weightAmount, double amountPiece, ModelMap model) {
        OutboundRegister outboundRegister = outboundRegisterService.findOne(id);
        model.addAttribute("chargeTypes", chargeTypeService.findAll());
        Map<String, Object> result = new HashMap<String, Object>();
        CEFeeItem feeitem = ceFeeItemService.findOne(feeitemId);
        result = storeCalculator.calculateOutboundRegisterItem(outboundRegister, feeitem, storeContainerCount, weightAmount, amountPiece);
        model.addAttribute("amount", result.get("amount"));
        model.addAttribute("ruleComment", result.get("ruleComment"));
        return result;
    }

    @RequestMapping(value = "/{id}/charge", method = RequestMethod.POST)
    public String charge(@PathVariable Long id, Long[] feeitem,
                         String[] money, String[] actuallyMoney, Long chargeTypeId, boolean delayed, int[] storeContainerCount, Double[] weight, Double[] amountPiece, String[] ruleComment, ModelMap model) {
        OutboundRegister outboundRegister = outboundRegisterService.findOne(id);
        outboundRegisterService.settlement(outboundRegister, feeitem,
                money, actuallyMoney, chargeTypeId, delayed, storeContainerCount, weight, amountPiece, ruleComment);
/*			//0,出库登记，1出库结算，2延迟付款 3，出库付款，
            model.addAttribute("operationType", 1);
			model.addAttribute("customers", customerService.findAll());
			model.addAttribute("mesg", "结算成功，点击确认打印单据！");
			model.addAttribute("id", id);
			model.addAttribute("outboundTypes", OutboundType.values());*/

        return "redirect:/store/outbound-register/" + id;
    }


    //付款

    @RequestMapping(value = "{id}/payment", method = RequestMethod.POST)
    public String payment(@PathVariable Long id, String aCardMac, String sMemberCode, String password, ModelMap model) {
        Map<String, Float> paymentResult = outboundRegisterService.payment(id, aCardMac, sMemberCode, password);
        //0,出库登记，1出库结算，2延迟付款 3，出库付款，
        /*model.addAttribute("operationType", 3);
        model.addAttribute("customers", customerService.findAll());
		model.addAttribute("mesg", "付款成功！");
		model.addAttribute("outboundTypes", OutboundType.values());*/
        model.put("messge", "付款成功，本次付款金额：" + paymentResult.get("totalAmount") + "元，客户卡上余额：" + paymentResult.get("balance") + "元。");
        return "/succeed";
    }

    //延迟付款
    @RequestMapping(value = "{id}/delay", method = RequestMethod.GET)
    public String delay(@PathVariable Long id, String remark, Long paymentStatus, ModelMap model) {
        outboundRegisterService.delay(id, remark, paymentStatus == 1 ? PaymentStatus.延付已生效 : PaymentStatus.延付已拒绝);
        //0,出库登记，1出库结算，2延迟付款 3，出库付款，
        model.addAttribute("operationType", 2);
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("mesg", paymentStatus == 1 ? "延迟付款生效！" : "延迟付款拒绝，要求付款!");
        model.addAttribute("outboundTypes", OutboundType.values());
        return indexPage;
    }

    //完成清点
    @RequestMapping(value = "{id}/complete-check", method = RequestMethod.GET)
    public String completeCheck(@PathVariable Long id) {
        OutboundRegister outboundRegister = outboundRegisterService.findOne(id);
        Set<StockOut> stockOuts = outboundRegister.getStockOuts();
        //允许客户将所有货物出库，其他不需要出库的商品（B库），自动进行作废。
        for (StockOut stockOut : stockOuts) {
            //检查是否B库
            if (stockOut.getStockOutStatus().equals(StockOutStatus.待拣货) &&
                    stockOut.getStoreLocation().getStoreArea().getCode().contains("LB")) {
                stockOut.setStockOutStatus(StockOutStatus.已作废);
            }
        }
        outboundRegisterService.updateStatus(id, StockOutStatus.已清点);
        return "redirect:/store/outbound-register/" + id;
    }

    //检查清点状态
    @RequestMapping(value = "{id}/confirm-complete-check", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> confirmCompleteCheck(@PathVariable Long id) {
        OutboundRegister outboundRegister = outboundRegisterService.findOne(id);
        int registerAmount = 0;
        Set<OutboundRegisterItem> outboundRegisterItems = outboundRegister.getOutboundRegisterItems();
        Set<OutboundCheckItem> outboundCheckItems = outboundRegister.getOutboundCheckItems();
        for (OutboundRegisterItem registerItem : outboundRegisterItems) {
            registerAmount += registerItem.getAmount();
        }

        int receiptAmount = 0;
        for (OutboundCheckItem receiptItem : outboundCheckItems) {
            receiptAmount += receiptItem.getAmount();
        }

        if (registerAmount != receiptAmount) {
            return this.printMessage(1, "出库登记数量与出库验货数量不符，请确认是否继续清点完成；" + "\n" + "     注意：低温库B库区待拣货单系统会自动作废！");
        }

        return this.printMessage(0, "验证成功");
    }

    //取消已清点状态
    @RequestMapping(value = "{id}/cancel-check", method = RequestMethod.GET)
    public String cancelCheck(@PathVariable Long id) {
        outboundRegisterService.updateStatus(id, StockOutStatus.已派送);
        return "redirect:/store/outbound-register/" + id;
    }

    //作废
    @RequestMapping(value = "{id}/cancel-register", method = RequestMethod.GET)
    public String cancelRgister(@PathVariable Long id) {
        outboundRegisterService.updateStatus(id, StockOutStatus.已作废);
        return "redirect:/store/outbound-register/" + id;
    }

    //重新启用
    @RequestMapping(value = "{id}/start-using", method = RequestMethod.GET)
    public String StartUsingRgister(@PathVariable Long id) {
        outboundRegisterService.updateStatus(id, StockOutStatus.已派送);
        return "redirect:/store/outbound-register/" + id;
    }
}
