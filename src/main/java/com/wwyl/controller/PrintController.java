package com.wwyl.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wwyl.Constants;
import com.wwyl.Enums.StockTakingStatus;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.finance.PaymentItem;
import com.wwyl.entity.finance.StandingBookDaily;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.store.InboundReceiptItem;
import com.wwyl.entity.store.InboundRegister;
import com.wwyl.entity.store.InboundRegisterItem;
import com.wwyl.entity.store.OutboundCheckItem;
import com.wwyl.entity.store.OutboundRegister;
import com.wwyl.entity.store.StockTaking;
import com.wwyl.service.finance.PaymentService;
import com.wwyl.service.finance.StandingBookDailyService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.store.InboundRegisterService;
import com.wwyl.service.store.OutboundCheckService;
import com.wwyl.service.store.OutboundRegisterService;
import com.wwyl.service.store.StockOutService;
import com.wwyl.service.store.StockTakingService;
import com.wwyl.service.ws.TSCLIBClient;

/**
 * Created by fyunli on 14/11/3.
 */
@Controller
@RequestMapping("/print")
public class PrintController extends BaseController{

    @Resource
    private InboundRegisterService inboundRegisterService;
    
    @Resource
    private OutboundRegisterService outboundRegisterService;
    
    @Resource
    private StockOutService stockOutService;
    
    @Resource
    private OutboundCheckService outboundCheckService;
    
    @Resource
    TSCLIBClient tSCLIBClient;
    
    @Resource
    private StockTakingService stockTakingService;
    
    @Resource
    private StoreAreaService storeAreaService;
    
    @Resource
    private StandingBookDailyService standingBookDailyService;
    
    @Resource
    private PaymentService paymentService;
    
    @RequestMapping(value = "/inbound-register", method = RequestMethod.GET)
    public String printInboundRegister(Long id, ModelMap modelMap) {
        InboundRegister inboundRegister = inboundRegisterService.findOne(id);
        modelMap.addAttribute("inboundRegister", inboundRegister);
        Set<InboundRegisterItem>  inboundRegisterItems=inboundRegister.getInboundRegisterItems();
        modelMap.addAttribute("rowcount", inboundRegisterItems.size()+4);
        modelMap.addAttribute("size", inboundRegisterItems.size());
        double sumWeight=0;
        double sumWeightKg=0;
        for (InboundRegisterItem inboundRegisterItem : inboundRegisterItems) {
        	sumWeight+=inboundRegisterItem.getWeight();
		}
        sumWeightKg=sumWeight;
        sumWeight = conversionUnit(sumWeight);
        
        
        modelMap.addAttribute("sumWeight", sumWeight);
        modelMap.addAttribute("sumWeightKg", sumWeightKg);
        return "/print/inbound_register_print";
    }
    
    @RequestMapping(value = "/inbound-receipt", method = RequestMethod.GET)
    public String printInboundReceipt(Long id, ModelMap modelMap) {
        InboundRegister inboundRegister = inboundRegisterService.findOne(id);
        modelMap.addAttribute("inboundRegister", inboundRegister);
        
        double sumWeight=0;
        double sumWeightKg=0;
        double sumAmount=0;
        double registerAmount=0;
        double registerWeight=0;
        double sumStoreContainerCount=0;
        
        Map<Long, Object[]> receiptPrintMap = new HashMap<Long, Object[]>();
        for (InboundReceiptItem item : inboundRegister.getInboundReceiptItems()) {
            Object[] receiptPrintItem = receiptPrintMap.get(item.getProduct().getId());
            if (receiptPrintItem == null) {
                receiptPrintItem = new Object[9]; // 商品名称, 件数, 重量, 托盘数
                receiptPrintItem[0] = item.getProduct().getName();
                receiptPrintItem[1] = item.getAmount();
                receiptPrintItem[2] = item.getWeight();
                receiptPrintItem[3] = (Double) 1.00;
                receiptPrintItem[4] = item.getSpec();
                receiptPrintItem[5] = item.getStoreArea().getName();
                receiptPrintItem[6] = item.getProductDetail();
                for (InboundRegisterItem inboundRegisterItem : inboundRegister.getInboundRegisterItems()) {
                	if(receiptPrintItem[0] == inboundRegisterItem.getProductName()){
                		//7 件数 8重量
                		receiptPrintItem[7] = inboundRegisterItem.getAmount();
    	                receiptPrintItem[8] = inboundRegisterItem.getWeight();
                	}
                }
            } else {
                double amount = (Double) receiptPrintItem[1];
                double weight = (Double) receiptPrintItem[2];
                double StoreContainerCount = (Double) receiptPrintItem[3];
                receiptPrintItem[1] = amount + item.getAmount();
                receiptPrintItem[2] = weight + item.getWeight();
                receiptPrintItem[3] = StoreContainerCount + 1;
            }
            receiptPrintMap.put(item.getProduct().getId(), receiptPrintItem);
            sumWeight+=item.getWeight();
            sumAmount = sumAmount + item.getAmount();
            sumStoreContainerCount+=1;
        }
        for (InboundRegisterItem inboundRegisterItem : inboundRegister.getInboundRegisterItems()) {
        		//7 件数 8重量
        	registerAmount = registerAmount + inboundRegisterItem.getAmount();
        	registerWeight = registerWeight + inboundRegisterItem.getWeight();
        }
        sumWeightKg=sumWeight;
        sumWeight = conversionUnit(sumWeight);
        
        modelMap.addAttribute("receiptPrintMap", receiptPrintMap);
 
        modelMap.addAttribute("sumWeight", sumWeight);
        modelMap.addAttribute("sumWeightKg", sumWeightKg);
        modelMap.addAttribute("sumAmount", sumAmount);
        modelMap.addAttribute("registerWeight", registerWeight);
        modelMap.addAttribute("registerAmount", registerAmount);
        modelMap.addAttribute("sumStoreContainerCount", sumStoreContainerCount);
        modelMap.addAttribute("rowcount", receiptPrintMap.size()+4);
        modelMap.addAttribute("size", receiptPrintMap.size());
        return "/print/inbound_receipt_print";
    }
 
    //出库登记单
    @RequestMapping(value = "/outbound-register", method = RequestMethod.GET)
    private String printOutboundRegister(Long id, ModelMap modelMap) throws ParseException{
    	double weightAmount = 0.00;
    	double weightAmountKG = 0.00;
    	double weightAmountTemp = 0.00;
    	BigDecimal totalAmount = new BigDecimal(0);
    	int totalStoreContainerCount = 0;
        OutboundRegister outboundRegister = outboundRegisterService.findOne(id);

        if (outboundRegister.getOutboundCheckItems() == null) {
        	throw new RuntimeException("手持机还未进行验货，不能打印票据。");
        }
        List<Map> outboundCheckItems = outboundCheckService.findCheckItemByOutboundRegisterId(outboundRegister.getId());
        for (Map outboundCheckItem : outboundCheckItems) {
        	//计算仓储天数
        /*	SimpleDateFormat datecale=new SimpleDateFormat("yyyy-MM-dd");  
        	Calendar cal = Calendar.getInstance();    
        	cal.setTime(datecale.parse(stockOutTotal.get("STOCKINTIME").toString()));    
        	long time1 = cal.getTimeInMillis();                 
        	cal.setTime(datecale.parse(getStringDateShort(outboundRegister.getRegisterTime())));    
        	long time2 = cal.getTimeInMillis();         
        	//出库当天按一天计算（+1）
        	long between_days=(time2-time1)/(1000*3600*24) + 1;  
        	*/
        	int between_days=0;
        	if(outboundRegister.getCustomer().getChargeType().equals(Constants.CHARGETYPE_MONTH)){
        		between_days=dayCount(DateUtils.parseDate(outboundCheckItem.get("STOCKINTIME").toString(), "yyyy-MM-dd"),outboundRegister.getRegisterTime(),true);
        	}else
        	{
        		between_days=dayCount(DateUtils.parseDate(outboundCheckItem.get("STOCKINTIME").toString(), "yyyy-MM-dd"),outboundRegister.getRegisterTime(),false);
        	}
        	weightAmountTemp=Double.valueOf(outboundCheckItem.get("WEIGHT").toString());
        	
        	
        	outboundCheckItem.put("WEIGHT", conversionUnit(weightAmountTemp));
        	
        	outboundCheckItem.put("WEIGHTKG", weightAmountTemp);
        	
        	outboundCheckItem.put("STOCKINDAYS", between_days);
        	weightAmount += Double.valueOf((outboundCheckItem.get("WEIGHT").toString()));
        	weightAmountKG += Double.valueOf((outboundCheckItem.get("WEIGHTKG").toString()));
        	totalAmount = totalAmount.add(new BigDecimal(outboundCheckItem.get("AMOUNT").toString()));
        	totalStoreContainerCount = totalStoreContainerCount + Integer.valueOf((outboundCheckItem.get("CONTAINERCOUNT").toString()));
        	
        }
        
        modelMap.addAttribute("outboundRegister", outboundRegister);
        modelMap.addAttribute("outboundCheckItems", outboundCheckItems);
        modelMap.addAttribute("weightAmount", weightAmount);
        modelMap.addAttribute("weightAmountKG", weightAmountKG);
        modelMap.addAttribute("totalAmount", totalAmount);
        modelMap.addAttribute("totalStoreContainerCount", totalStoreContainerCount);
        modelMap.addAttribute("rowCount", outboundCheckItems.size()+3);
        modelMap.addAttribute("size", outboundCheckItems.size());
        return "/print/outbound_register_print";
    }
    
  //出库登记单 - 备份
    /*
    @RequestMapping(value = "/outbound-register", method = RequestMethod.GET)
    private String printOutboundRegister(Long id, ModelMap modelMap) throws ParseException{
    	double weightAmount = 0.00;
    	double weightAmountKG = 0.00;
    	double weightAmountTemp = 0.00;
    	BigDecimal totalAmount = new BigDecimal(0);
    	int totalStoreContainerCount = 0;
        OutboundRegister outboundRegister = outboundRegisterService.findOne(id);

        if (outboundRegister.getStockOuts() == null) {
        	throw new RuntimeException("还未进行托盘分配，不能打印票据。");
        }
         
        List<Map> stockOutTotals = stockOutService.findTotalByOutboundRegisterId(outboundRegister.getId());
        for (Map stockOutTotal : stockOutTotals) {
        	//计算仓储天数

        	int between_days=0;
        	if(outboundRegister.getCustomer().getChargeType().equals(Constants.CHARGETYPE_MONTH)){
        		between_days=dayCount(DateUtils.parseDate(stockOutTotal.get("STOCKINTIME").toString(), "yyyy-MM-dd"),outboundRegister.getRegisterTime(),true);
        	}else
        	{
        		between_days=dayCount(DateUtils.parseDate(stockOutTotal.get("STOCKINTIME").toString(), "yyyy-MM-dd"),outboundRegister.getRegisterTime(),false);
        	}
        	weightAmountTemp=Double.valueOf(stockOutTotal.get("WEIGHT").toString());
        	
        	
        	stockOutTotal.put("WEIGHT", conversionUnit(weightAmountTemp));
        	
        	stockOutTotal.put("WEIGHTKG", weightAmountTemp);
        	
        	stockOutTotal.put("STOCKINDAYS", between_days);
        	weightAmount += Double.valueOf((stockOutTotal.get("WEIGHT").toString()));
        	weightAmountKG += Double.valueOf((stockOutTotal.get("WEIGHTKG").toString()));
        	totalAmount = totalAmount.add(new BigDecimal(stockOutTotal.get("AMOUNT").toString()));
        	totalStoreContainerCount = totalStoreContainerCount + Integer.valueOf((stockOutTotal.get("CONTAINERCOUNT").toString()));
        	
        }
        
        modelMap.addAttribute("outboundRegister", outboundRegister);
        modelMap.addAttribute("stockOutTotals", stockOutTotals);
        modelMap.addAttribute("weightAmount", weightAmount);
        modelMap.addAttribute("weightAmountKG", weightAmountKG);
        modelMap.addAttribute("totalAmount", totalAmount);
        modelMap.addAttribute("totalStoreContainerCount", totalStoreContainerCount);
        modelMap.addAttribute("rowCount", stockOutTotals.size()+3);
        modelMap.addAttribute("size", stockOutTotals.size());
        return "/print/outbound_register_print";
    }
    */
   //出库结算单
    @RequestMapping(value = "/outbound-payment", method = RequestMethod.GET)
    private String printOutboundPayment(Long id, ModelMap modelMap) throws ParseException{
    	double sumWeight = 0;
    	double amountPiece=0;
        int sumStoreContainerCount=0;
        OutboundRegister outboundRegister = outboundRegisterService.findOne(id);
        Set<OutboundCheckItem> outboundCheckItems=outboundRegister.getOutboundCheckItems();
        
        for (OutboundCheckItem outboundCheckItem : outboundCheckItems) {
        	sumStoreContainerCount +=1;
        	sumWeight +=outboundCheckItem.getWeight();
        	amountPiece +=outboundCheckItem.getAmount();
        }
        sumWeight = conversionUnit(sumWeight);
        
        List<Map> outboundCheckItemTotals = outboundCheckService.findCheckItemTotalByOutboundRegisterId(outboundRegister.getId());
        
        for (Map outboundCheckTotal : outboundCheckItemTotals) {
        	//计算仓储天数
        /*	SimpleDateFormat datecale=new SimpleDateFormat("yyyy-MM-dd");  
        	Calendar cal = Calendar.getInstance();    
        	cal.setTime(datecale.parse(outboundCheckTotal.get("STOCKINTIME").toString()));    
        	long time1 = cal.getTimeInMillis();                 
        	cal.setTime(datecale.parse(getStringDateShort(outboundRegister.getRegisterTime())));    
        	long time2 = cal.getTimeInMillis();         
        	//出库当天按一天计算（+1）
        	long between_days=(time2-time1)/(1000*3600*24) + 1;  
        	*/
        	int between_days=0;
        	if(outboundRegister.getCustomer().getChargeType().equals(Constants.CHARGETYPE_MONTH)){
        		between_days=dayCount(DateUtils.parseDate(outboundCheckTotal.get("STOCKINTIME").toString(), "yyyy-MM-dd"),outboundRegister.getRegisterTime(),true);
        	}else
        	{
        		between_days=dayCount(DateUtils.parseDate(outboundCheckTotal.get("STOCKINTIME").toString(), "yyyy-MM-dd"),outboundRegister.getRegisterTime(),false);
        	}
        	
        	outboundCheckTotal.put("STOCKINDAYS", between_days);
        }
        modelMap.addAttribute("sumStoreContainerCount", sumStoreContainerCount);
        modelMap.addAttribute("sumWeight", sumWeight);
        modelMap.addAttribute("amountPiece", amountPiece);
        modelMap.addAttribute("outboundCheckItemTotals", outboundCheckItemTotals);
        modelMap.addAttribute("itemRowCount", outboundCheckItemTotals.size());
        modelMap.addAttribute("payment", outboundRegister.getPayment());
        modelMap.addAttribute("rowCount", outboundRegister.getPayment().getPaymentItems().size()+ outboundCheckItemTotals.size() +1);
        
        return "/print/outbound_payment_print";
    }
    
//    @RequestMapping(value = "/outbound-payment", method = RequestMethod.GET)
//    private String printOutboundPayment(Long id, ModelMap modelMap) {
//    	double sumWeight = 0;
//        OutboundRegister outboundRegister = outboundRegisterService.findOne(id);
//        Set<OutboundCheckItem> outboundCheckItems=outboundRegister.getOutboundCheckItems();
//        for(OutboundCheckItem item :  outboundCheckItems)
//        {
//        	sumWeight += conversionUnit(item.getWeight());
//        }
//      
//        modelMap.addAttribute("payment", outboundRegister.getPayment());
//        modelMap.addAttribute("sumWeight", sumWeight);
//        modelMap.addAttribute("rowCount", outboundRegister.getPayment().getPaymentItems().size()+2);
//        modelMap.addAttribute("count", outboundCheckItems.size());
//        return "/print/outbound_payment_print";
//    }
    
    @RequestMapping(value = "/inbound-payment", method = RequestMethod.GET)
    public String printInboundPayment(Long id, ModelMap modelMap) {
        InboundRegister inboundRegister = inboundRegisterService.findOne(id);
        modelMap.addAttribute("inboundRegister", inboundRegister);
        int totalStoreContainerCount = 0;
        double amountPiece = 0.00;
        Set<InboundReceiptItem>  inboundReceiptItems=inboundRegister.getInboundReceiptItems();
        modelMap.addAttribute("rowCount", inboundRegister.getPayment().getPaymentItems().size()+2);
        modelMap.addAttribute("rightCount", inboundRegister.getPayment().getPaymentItems().size()+5);
        double sumWeight=0;
        for (InboundReceiptItem inboundReceiptItem : inboundReceiptItems) {
        	sumWeight+=inboundReceiptItem.getWeight();
        	totalStoreContainerCount = totalStoreContainerCount + 1;
        	amountPiece+=inboundReceiptItem.getAmount();
		}
        sumWeight = conversionUnit(sumWeight);
        modelMap.addAttribute("type", 1);
        modelMap.addAttribute("payment", inboundRegister.getPayment());
        modelMap.addAttribute("sumWeight", sumWeight);
        modelMap.addAttribute("totalStoreContainerCount", totalStoreContainerCount);
        modelMap.addAttribute("amountPiece", amountPiece);
        modelMap.addAttribute("count", inboundReceiptItems.size());
        
        return "/print/inbound_payment_print";
    }
    
    @RequestMapping(value = "/inbound-label", method = RequestMethod.GET)
    public void printInboundLabel(Long id, ModelMap modelMap) {
    	double sumWeight=0;
    	int i=0;
        InboundRegister inboundRegister = inboundRegisterService.findOne(id);
        Set<InboundRegisterItem>  inboundRegisterItems=inboundRegister.getInboundRegisterItems();
        for(InboundRegisterItem inboundRegisterItem: inboundRegisterItems){
        	for(i=1;i<=inboundRegisterItem.getStoreContainerCount();i++)
        	{
        		sumWeight=inboundRegisterItem.getProduct().getWeight() * inboundRegisterItem.getAmount();
	        	tSCLIBClient.printLabel(inboundRegisterItem.getInboundRegister().getRegisterTime().toString(), 
	        			inboundRegisterItem.getInboundRegister().getCustomerName(),
	        			inboundRegisterItem.getProductName(),
	        		    String.valueOf(inboundRegisterItem.getProduct().getBearingCapacity()),
	        		    String.valueOf(sumWeight),
	        		    String.valueOf(i));
        	}
        } 
    }
    
    //盘点完成汇总打印表
    @RequestMapping(value = "/stock-taking-completed", method = RequestMethod.GET)
    private String printStockTakingCompleted(Long id, ModelMap modelMap) throws ParseException{
    	String storeAreaName = "";
    	double amountTotal = 0.00;
    	double storeContainerCountTotal = 0.00;
    	double weightTotal = 0.00;
    	double stockTakingAmountTotal = 0.00;
    	double storeTakingContainerCountTotal = 0.00;
    	double stockTakingWeightTotal = 0.00;
    	double amountDifferentTotal = 0.00;
    	double weightDifferentTotal = 0.00;
    	double containerCountDifferentTotal = 0.00;
    	
    	StockTaking stockTaking = stockTakingService.findOne(id);

        if ((stockTaking == null) ||(!stockTaking.getStockTakingStatus().equals(StockTakingStatus.已完成)) ) {
        	throw new RuntimeException("还未盘点完成，不能打印盘点汇总表。");
        }
         
        List<Map> stockTakingTotals = stockTakingService.findTotalByStockTakingId(stockTaking.getId());
        for (Map stockTakingTotal : stockTakingTotals) {
        	amountTotal += Double.valueOf(stockTakingTotal.get("AMOUNT").toString());
        	storeContainerCountTotal += Double.valueOf(stockTakingTotal.get("STORECONTAINERCOUNT").toString());
        	weightTotal += Double.valueOf(stockTakingTotal.get("WEIGHT").toString());
        	stockTakingAmountTotal += Double.valueOf(stockTakingTotal.get("STOCKTAKINGAMOUNT").toString());
        	storeTakingContainerCountTotal += Double.valueOf(stockTakingTotal.get("STORETAKINGCONTAINERCOUNT").toString());
        	stockTakingWeightTotal += Double.valueOf(stockTakingTotal.get("STOCKTAKINGWEIGHT").toString());
        	amountDifferentTotal += Double.valueOf(stockTakingTotal.get("AMOUNTNEW").toString());
        	weightDifferentTotal += Double.valueOf(stockTakingTotal.get("WEIGHTNEW").toString());
        	containerCountDifferentTotal += Double.valueOf(stockTakingTotal.get("CONTAINERCOUNTNEW").toString());
        }
        
        StoreArea storeArea = storeAreaService.findOne(stockTaking.getStockTakingObjectId());
        if(storeArea != null){
        	storeAreaName = storeArea.getCode() + storeArea.getName();
        };
        
        modelMap.addAttribute("storeArea", storeAreaName);
        modelMap.addAttribute("stockTakingTotals", stockTakingTotals);
        modelMap.addAttribute("rowCount", stockTakingTotals.size()+3);
        modelMap.addAttribute("size", stockTakingTotals.size());
        //汇总合计
        modelMap.addAttribute("amountTotal", amountTotal);
        modelMap.addAttribute("storeContainerCountTotal", storeContainerCountTotal);
        modelMap.addAttribute("weightTotal", weightTotal);
        modelMap.addAttribute("stockTakingAmountTotal", stockTakingAmountTotal);
        modelMap.addAttribute("storeTakingContainerCountTotal", storeTakingContainerCountTotal);
        modelMap.addAttribute("stockTakingWeightTotal", stockTakingWeightTotal);
        modelMap.addAttribute("amountDifferentTotal", amountDifferentTotal);
        modelMap.addAttribute("weightDifferentTotal", weightDifferentTotal);
        modelMap.addAttribute("containerCountDifferentTotal", containerCountDifferentTotal);
        return "/print/stock_taking_completed_print";
    }
    
    //客户对账打印表
    @RequestMapping(value = "/standing-book-daily-billing", method = RequestMethod.GET)
    private String printStandingBookDailyBilling(Date startDate, Date endDate, Long customerId, ModelMap modelMap) throws ParseException{
    	BigDecimal amountTotal = new BigDecimal(0);
    	if (startDate != null && endDate != null && customerId != null) {
    		modelMap.addAttribute("startDate", startDate);
    		modelMap.addAttribute("endDate", endDate);
    		modelMap.addAttribute("customerId", customerId);
            StandingBookDaily standingBookDaily = standingBookDailyService
                    .findFinanceBillingByDateRangeAndCustomerId(startDate, endDate, customerId);
            if (standingBookDaily != null) {
            	modelMap.addAttribute("billing", standingBookDaily);
            	amountTotal = getTotalAmount(standingBookDaily);
            	modelMap.addAttribute("amountTotal", amountTotal);
            }
        }
    	
        return "/print/standing_book_daily_billing_print";
    }
    
    /**
     * 把千克转换成吨
     * @return
     */
    private double conversionUnit(double  kilogram ){
    	BigDecimal  big=new BigDecimal(kilogram);
    	//精度为4，舍入模式为大于0.5进1，否则舍弃。 
    	/*    	MathContext mc = new MathContext(2, RoundingMode.HALF_DOWN);*/
		return  big.divide(new BigDecimal(1000)).setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    private int dayCount(Date start, Date end, boolean isMonthKnot) {
		 
		//判断是否月结
		int	result=0;//(int) ((end.getTime()-start.getTime()) / (1000 * 60 * 60 * 24)); 
		for(Date d = start; !DateUtils.isSameDay(end, d); d=DateUtils.addDays(d, 1)){ 
			 result++;
		}
		if(!isMonthKnot){	
			result=result+1;
		}
		return result;
	}
  //合计金额；
    public BigDecimal getTotalAmount(StandingBookDaily standingBookDaily){
    	BigDecimal totalAmount = new BigDecimal(0);
    	totalAmount = standingBookDaily.getContainerStorage().add(standingBookDaily.getRentalAreaStorage()
    			.add(standingBookDaily.getWeightStorage()
    					.add(standingBookDaily.getAmountStorage()
    							.add(standingBookDaily.getShipment()
    									.add(standingBookDaily.getSorting()
    											.add(standingBookDaily.getHandling()
    													.add(standingBookDaily.getUnloading()
    															.add(standingBookDaily.getShrinkwrap()
    																	.add(standingBookDaily.getKetonehandling()
    																			.add(standingBookDaily.getWriteCode()
    																					.add(standingBookDaily.getDisposal()
    																							.add(standingBookDaily.getColding()
    																									.add(standingBookDaily.getSideUnloading()
    																											.add(standingBookDaily.getInUnloading()))))))))))))));
    	return totalAmount;
    }
    
    //费用管理-临时交费单
    @RequestMapping(value = "/payment-fee", method = RequestMethod.GET)
    public String printPaymentFee(Long id, ModelMap modelMap) {
    	Payment payment = paymentService.findOne(id);
        modelMap.addAttribute("payment", payment);
        int totalStoreContainerCount = 0;
        double amountPiece = 0.00;
        Set<PaymentItem>  paymentItems=payment.getPaymentItems();
        modelMap.addAttribute("rowCount", payment.getPaymentItems().size()+2);
        modelMap.addAttribute("rightCount", payment.getPaymentItems().size()+5);
        double sumWeight=0;
        for (PaymentItem paymentItem : paymentItems) {
        	sumWeight+=paymentItem.getWeight();
        	totalStoreContainerCount = totalStoreContainerCount + 1;
        	amountPiece+=paymentItem.getAmountPiece();
		}
        sumWeight = conversionUnit(sumWeight);
        modelMap.addAttribute("type", 1);
        modelMap.addAttribute("payment", payment);
        modelMap.addAttribute("sumWeight", sumWeight);
        modelMap.addAttribute("totalStoreContainerCount", totalStoreContainerCount);
        modelMap.addAttribute("amountPiece", amountPiece);
        modelMap.addAttribute("count", paymentItems.size());
        
        return "/print/payment_fee_print";
    }
}
