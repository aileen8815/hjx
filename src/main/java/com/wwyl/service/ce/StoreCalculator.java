package com.wwyl.service.ce;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import com.wwyl.Constants;
import com.wwyl.entity.ce.CalculatedResult;
import com.wwyl.entity.ce.FeeItem;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.InboundReceiptItem;
import com.wwyl.entity.store.InboundRegister;
import com.wwyl.entity.store.OutboundCheckItem;
import com.wwyl.entity.store.OutboundRegister;
import com.wwyl.entity.store.StockOwnerChange;
import com.wwyl.entity.store.StockOwnerChangeCheckItem;
import com.wwyl.entity.tenancy.StoreContract;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.tenancy.StoreContractService;

/**
 * @author sjwang
 */
@Service
public class StoreCalculator {
	@Resource 
	private CECalculator ceCalculator;
	
	@Resource
	private CERuleService ceRuleService;
	
	@Resource
	private DECalculator deCalculator;
	
	@Resource
	private BookInventoryService bookInventoryService;
	
	@Resource
	private StoreContractService storeContractService;
	
	@Resource
	private CustomerService customerService;
	
	public Set<CalculatedResult> calculate(InboundRegister inboundRegister) {
		double weightAmount = 0.00;
		double pieceWeight = 0.00;
		double amountPiece = 0;
		
		//计算所有费用
		for(InboundReceiptItem item : inboundRegister.getInboundReceiptItems())
		{
			weightAmount = weightAmount + item.getWeight()/1000;
			pieceWeight = pieceWeight + item.getWeight();
			amountPiece = amountPiece + item.getAmount();
		}
		pieceWeight = pieceWeight/inboundRegister.getInboundReceiptItems().size();
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("PalletAmount", inboundRegister.getInboundReceiptItems().size());
		props.put("WeightAmount", weightAmount);
		props.put("Weight", pieceWeight);
		props.put("AmountPiece", amountPiece);
		props.put("CustomerGrade", inboundRegister.getCustomer().getCustomerGrade().getId());
		//默认一个，虽然后面计算了，但是怕改动没有默认规则；
		props.put("CommonPacking", Constants.Common_Packing_1);

		//按包装类型计算（装卸费、倒货费）
		Map<String, Object> totalFeeItemAmount = calculateInboundPacking(inboundRegister);
		
		Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("inbound"));
		for(CalculatedResult obj : results) {
			obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "inbound"));
			if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Handling)){
				obj.setAmount((BigDecimal)totalFeeItemAmount.get("totalHandlingAmount"));
				obj.setRuleComment(totalFeeItemAmount.get("totalHandlingRuleComment").toString());
			}
			if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Unloading)){
				obj.setAmount((BigDecimal)totalFeeItemAmount.get("totalUnloadingAmount"));
				obj.setRuleComment(totalFeeItemAmount.get("totalUnloadingRuleComment").toString());
			}
		}
		
		return results;
	}	
	//单项收费，由于无法区分合计数量的货物类型，故与市场协商先暂时按照普通货物计算；
	public Map<String, Object> calculateInboundRegisterItem(InboundRegister inboundRegister, FeeItem feeItem, int palletAmount, double weightAmount, double amountPiece){
		Map<String, Object> result = new HashMap<String, Object>();	
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("PalletAmount", palletAmount);
		props.put("WeightAmount", (weightAmount/1000));
		props.put("AmountPiece", amountPiece);
		props.put("CustomerGrade", inboundRegister.getCustomer().getCustomerGrade().getId());
		props.put("CommonPacking", Constants.Common_Packing_1);
		
		Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("inbound"));
		for(CalculatedResult obj : results) {
			if (obj.getFeeItem().getId().equals(feeItem.getId())){
				result.put("amount", obj.getAmount());
				result.put("ruleComment", obj.getRuleComment());
				break;
			}
		}
		return result;
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

	public Map<String, Object> calculate(OutboundRegister outboundRegister) {
		Map<String, Object> resultss = new HashMap<String, Object>();
		List<Map> StorageChargesList = new ArrayList<Map>();
		
		BigDecimal StorageChargesAmount = new BigDecimal(0);
		int palletDay = 0;  //单个托盘天数（包含合同内的）
		int palletDay1 = 0; //单个托盘合同内天数
		
		double weight = 0.00; //单个托盘重量
		double weight1 = 0.00; //单个托盘合同内重量
		
		double weightDay = 0.00;//总天数*单个托盘吨数（包含合同内的）
		double weightDay1 = 0.00;//总天数*单个托盘吨数
		
		int palletDayAmount = 0;  //总天数（包含合同内的）
		int palletDayAmount1 = 0; //合同内总天数
		
		double weightAmount = 0.00; //总重量
		double weightAmount1 = 0.00; //合同内总重量
		
		double weightDayAmount = 0.00;//总天数*托盘吨数
		double weightDayAmount1 = 0.00;//总天数*托盘吨数（合同内）		

		Set<OutboundCheckItem> sutboundCheckItems = outboundRegister.getOutboundCheckItems();
		
		//包含合同托盘在内的仓储费用(单个托盘计算)
		for(OutboundCheckItem item : sutboundCheckItems) {
			palletDay = dayCount(item.getSettledTime(), item.getCheckTime(),false);
			weight = (item.getWeight())/1000;
			weightDay = palletDay*weight;
			
			palletDayAmount += dayCount(item.getSettledTime(), item.getCheckTime(),false);
			weightAmount += (item.getWeight())/1000;
			weightDayAmount += weightDay;
			
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("PalletAmount", 1);
			props.put("WeightAmount", weight);
			props.put("PalletDayAmount", palletDay);
			props.put("WeightDayAmount", weightDay);
			props.put("CustomerGrade", outboundRegister.getCustomer().getCustomerGrade().getId());
			props.put("CommonPacking", item.getProduct().getCommonPacking().getId());
			
			Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findOneType(Constants.CE_RULE_TYPE));
			for(CalculatedResult obj : results) {
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)){
					Map StorageCharges = new HashMap();
					StorageCharges.put("outboundCheckItemId", item.getId());
					StorageCharges.put("storageCharges", obj.getAmount());
					StorageCharges.put("storageChargesRuleComment", obj.getRuleComment());
					StorageChargesList.add(StorageCharges);
					StorageChargesAmount = StorageChargesAmount.add(obj.getAmount());
					break;
				}
			}
		}
		
		//不包含合同托盘在内的仓储费用(单个托盘计算)
		Map<String, Object> maps = new HashMap<String, Object>();
		List<StoreContract> storeContracts = storeContractService.findContractByCustomerID(outboundRegister.getCustomer().getId());
		if(storeContracts != null && !storeContracts.isEmpty())
		{
			for(OutboundCheckItem item : sutboundCheckItems) {
				for(StoreContract storeContract: storeContracts )
				{
					if(item.getStoreLocation().getStoreArea().getId() == storeContract.getStoreArea().getId())
					{
						maps = getEffectiveContactDayAmount(storeContract.getStartDate(),storeContract.getEndDate(), item.getSettledTime(),item.getCheckTime(),item.getWeight(), false);
						weight1 = (Double) maps.get("weightAmount")/1000;
						palletDay1 = (Integer) maps.get("palletDayAmount");
						weightDay1 = palletDay1 * weight1;
						
						weightAmount1 += (Double) maps.get("weightAmount")/1000;
						palletDayAmount1 += (Integer) maps.get("palletDayAmount");
						weightDayAmount1 += weightDay1;
						
						
						Map<String, Object> props1 = new HashMap<String, Object>();
						props1.put("PalletAmount", 1);
						props1.put("WeightAmount", item.getWeight()/1000 - weight1);
						props1.put("PalletDayAmount", dayCount(item.getSettledTime(), item.getCheckTime(),false) - palletDay1);
						props1.put("CustomerGrade", outboundRegister.getCustomer().getCustomerGrade().getId());
						props1.put("WeightDayAmount", (dayCount(item.getSettledTime(), item.getCheckTime(),false) * item.getWeight()/1000) - weightDayAmount1);
						props1.put("CommonPacking", item.getProduct().getCommonPacking().getId());
						Set<CalculatedResult> results = ceCalculator.calculate(props1, ceRuleService.findOneType(Constants.CE_RULE_TYPE));
						for(CalculatedResult obj : results) {
							if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)){
								for (Map StorageChargesTemp : StorageChargesList) {
									if (StorageChargesTemp.get("outboundCheckItemId").equals(item.getId()))
									{
										StorageChargesTemp.put("storageCharges", 
												(new BigDecimal(StorageChargesTemp.get("storageCharges").toString()).subtract(obj.getAmount())));
										StorageChargesAmount = StorageChargesAmount.subtract(obj.getAmount());
										
									}
								}
								break;
							}
						}
					}
				}
			}
		}
		
		//只取出ruleComment即可；
		Map<String, Object> totalFeeItemAmount = calculateOutboundPacking(outboundRegister);
		
		//包含合同托盘在内的费用
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("PalletAmount", outboundRegister.getOutboundCheckItems().size());
		props.put("WeightAmount", weightAmount);
		props.put("PalletDayAmount", palletDayAmount);
		props.put("WeightDayAmount", weightDayAmount);
		props.put("CustomerGrade", outboundRegister.getCustomer().getCustomerGrade().getId());
		//默认一个，虽然后面计算了，但是怕改动没有默认规则；
		props.put("CommonPacking", Constants.Common_Packing_1);
		Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("outbound"));
		for(CalculatedResult obj : results) {
			obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "outbound"));
		}
		
		//不包含合同托盘在内的费用
		Map<String, Object> props1 = new HashMap<String, Object>();
		props1.put("PalletAmount", outboundRegister.getOutboundCheckItems().size());
		props1.put("WeightAmount", weightAmount - weightAmount1);
		props1.put("PalletDayAmount", palletDayAmount - palletDayAmount1);
		props1.put("WeightDayAmount", weightDayAmount - weightDayAmount1);
		props1.put("CustomerGrade", outboundRegister.getCustomer().getCustomerGrade().getId());
		//默认一个，虽然后面计算了，但是怕改动没有默认规则；
		props.put("CommonPacking", Constants.Common_Packing_1);
		Set<CalculatedResult> results1 = ceCalculator.calculate(props1, ceRuleService.findByBusinessType("outbound"));
		for (CalculatedResult obj : results1) {
			obj.setDiscountRate(deCalculator.getDiscountRate(props1, obj.getFeeItem(), "outbound"));
		}
		
		//将包含合同托盘在内的费用中的仓储费替换成不包含合同托盘在内的仓储费
		for(CalculatedResult obj : results)
		{
			for(CalculatedResult obj1 : results1)
			{
				if(obj.getFeeItem().getId()== obj1.getFeeItem().getId() && obj.getFeeItem().getId()==Constants.CE_FEE_ITEM_StorageCharges)
				{
					//为了单个计费与合计汇总计费金额一致；
					//单托（重量）计费的仓储费直接汇总合计，写入汇仓储值	
				//	obj.setAmount(obj1.getAmount());
					obj.setAmount(StorageChargesAmount);
					obj.setCalculatedItemName(obj1.getCalculatedItemName());
					obj.setDiscountRate(obj1.getDiscountRate());
					obj.setFactor(obj1.getFactor());
					obj.setFeeItem(obj1.getFeeItem());
					obj.setRuleComment(totalFeeItemAmount.get("totalStorageChargesRuleComment").toString());
				}
			}
		}
		resultss.put("storageChargesList", StorageChargesList);
		resultss.put("calculatedResults", results);
		
		return resultss;
	}
	
	
/*备份
	public Set<CalculatedResult> calculate(OutboundRegister outboundRegister) {
		int palletDayAmount = 0;  //总天数（包含合同内的）
		int palletDayAmount1 = 0; //合同内总天数
		double weightAmount = 0.00; //总重量
		double weightAmount1 = 0.00; //合同内总重量
		Set<OutboundCheckItem> sutboundCheckItems = outboundRegister.getOutboundCheckItems();
		
		for(OutboundCheckItem item : sutboundCheckItems) {
			palletDayAmount += dayCount(item.getSettledTime(), item.getCheckTime(),false);
			weightAmount += (item.getWeight())/1000;
		}
		
		Map<String, Object> maps = new HashMap<String, Object>();
		List<StoreContract> storeContracts = storeContractService.findContractByCustomerID(outboundRegister.getCustomer().getId());
		if(storeContracts != null && !storeContracts.isEmpty())
		{
			for(OutboundCheckItem item : sutboundCheckItems) {				
				for(StoreContract storeContract: storeContracts )
				{
					if(item.getStoreLocation().getStoreArea().getId() == storeContract.getStoreArea().getId())
					{
						maps = getEffectiveContactDayAmount(storeContract.getStartDate(),storeContract.getEndDate(), item.getSettledTime(),item.getCheckTime(),item.getWeight(), false);
						weightAmount1 += (Double) maps.get("weightAmount")/1000;
						palletDayAmount1 += (Integer) maps.get("palletDayAmount");
					}
				}
			}
		}
				
		//包含合同托盘在内的费用
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("PalletAmount", outboundRegister.getOutboundCheckItems().size());
		props.put("WeightAmount", weightAmount);
		props.put("PalletDayAmount", palletDayAmount);
		props.put("CustomerGrade", outboundRegister.getCustomer().getCustomerGrade().getId());
		Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("outbound"));
		for(CalculatedResult obj : results) {
			obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "outbound"));
		}
		
		//不包含合同托盘在内的费用
		Map<String, Object> props1 = new HashMap<String, Object>();
		props1.put("PalletAmount", outboundRegister.getOutboundCheckItems().size());
		props1.put("WeightAmount", weightAmount - weightAmount1);
		props1.put("PalletDayAmount", palletDayAmount - palletDayAmount1);
		props1.put("CustomerGrade", outboundRegister.getCustomer().getCustomerGrade().getId());		
		Set<CalculatedResult> results1 = ceCalculator.calculate(props1, ceRuleService.findByBusinessType("outbound"));
		for (CalculatedResult obj : results1) {
			obj.setDiscountRate(deCalculator.getDiscountRate(props1, obj.getFeeItem(), "outbound"));
		}
		
		//将包含合同托盘在内的费用中的仓储费替换成不包含合同托盘在内的仓储费
		for(CalculatedResult obj : results)
		{		
			for(CalculatedResult obj1 : results1)
			{
				if(obj.getFeeItem().getId()== obj1.getFeeItem().getId() && obj.getFeeItem().getId()==Constants.CE_FEE_ITEM_StorageCharges)
				{
					obj.setAmount(obj1.getAmount());
					obj.setCalculatedItemName(obj1.getCalculatedItemName());
					obj.setDiscountRate(obj1.getDiscountRate());
					obj.setFactor(obj1.getFactor());
					obj.setFeeItem(obj1.getFeeItem());
				}
			}
		}
		return results;
	}*/
	
	//单项收费，由于无法区分合计数量的货物类型，故与市场协商先暂时按照普通货物计算；
	public Map<String, Object> calculateOutboundRegisterItem(OutboundRegister outboundRegister, FeeItem feeItem, double palletAmount, double weightAmount, double amountPiece){
		int palletDayAmount = 0;  
		int palletDayAmount1 = 0;
		double weightDayAmount = 0;
		double weightDayAmount1 = 0;
		Map<String, Object> result = new HashMap<String, Object>();	
		Set<OutboundCheckItem> sutboundCheckItems = outboundRegister.getOutboundCheckItems();
		for(OutboundCheckItem item : sutboundCheckItems) {
			palletDayAmount += dayCount(item.getSettledTime(), item.getCheckTime(),false);
			weightDayAmount += (item.getWeight()/1000) * palletDayAmount;
		}
		
		Map<String, Object> maps = new HashMap<String, Object>();
		List<StoreContract> storeContracts = storeContractService.findContractByCustomerID(outboundRegister.getCustomer().getId());
		if(storeContracts != null && !storeContracts.isEmpty())
		{
			for(OutboundCheckItem item : sutboundCheckItems) {				
				for(StoreContract storeContract: storeContracts )
				{
					if(item.getStoreLocation().getStoreArea().getId() == storeContract.getStoreArea().getId())
					{
						maps = getEffectiveContactDayAmount(storeContract.getStartDate(),storeContract.getEndDate(), item.getSettledTime(),item.getCheckTime(),item.getWeight(), false);
						palletDayAmount1 += (Integer) maps.get("palletDayAmount");
						weightDayAmount1 += (Double) maps.get("weightDayAmount");
					}
				}
			}
		}
		
		
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("PalletAmount", palletAmount);
		props.put("WeightAmount", (weightAmount/1000));
		props.put("PalletDayAmount", palletDayAmount - palletDayAmount1);
		props.put("WeightDayAmount", weightDayAmount - weightDayAmount1);
		props.put("AmountPiece", amountPiece);
		props.put("CustomerGrade", outboundRegister.getCustomer().getCustomerGrade().getId());
		props.put("CommonPacking", Constants.Common_Packing_1);
		Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("outbound"));
		for(CalculatedResult obj : results) {
			if (obj.getFeeItem().getId().equals(feeItem.getId())){
				result.put("amount", obj.getAmount());
				result.put("ruleComment", obj.getRuleComment());
				break;
			}
		}
		return result;
	}
	
	//买方货权转移计费
	public Set<CalculatedResult> Calculate4Buyer(StockOwnerChange stockOwnerChange) {			
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("PalletAmount", stockOwnerChange.getStockOwnerChangeCheckItems().size());
		props.put("CustomerGrade", stockOwnerChange.getBuyer().getCustomerGrade().getId());
		Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("ownerchangeforbuyer"));
		for(CalculatedResult obj : results) {
			obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "ownerchangeforbuyer"));
		}
		return results;
	}
	
	
	//卖方货权转移计费
	public Set<CalculatedResult> Calculate4Seller(StockOwnerChange stockOwnerChange) {		
		int palletDayAmount = 0;  //总天数（包含合同内的）
		int palletDayAmount1 = 0; //合同内总天数
		double weightAmount = 0.00; //总重量
		double weightAmount1 = 0.00; //合同内总重量
		BookInventory bookInventory = new BookInventory();
		Set<StockOwnerChangeCheckItem> sets = stockOwnerChange.getStockOwnerChangeCheckItems();
		for (StockOwnerChangeCheckItem item : sets) {
			bookInventory = bookInventoryService.findBookInventoryByCustomerId(stockOwnerChange.getSeller().getId(), item.getStoreContainer().getId());
			palletDayAmount += dayCount(bookInventory.getSettledTime(), item.getCreatedTime(),false);
			weightAmount += bookInventory.getAmount()/1000;
		}
		
		Map<String, Object> maps = new HashMap<String, Object>();
		List<StoreContract> storeContracts = storeContractService.findContractByCustomerID(stockOwnerChange.getSeller().getId());
		if(storeContracts != null && !storeContracts.isEmpty())
		{
			for(StockOwnerChangeCheckItem item : sets) {				
				for(StoreContract storeContract: storeContracts )
				{
					if(item.getStoreLocation().getStoreArea().getId() == storeContract.getStoreArea().getId())
					{
						bookInventory = bookInventoryService.findBookInventoryByCustomerId(stockOwnerChange.getSeller().getId(), item.getStoreContainer().getId());
						maps = getEffectiveContactDayAmount(storeContract.getStartDate(),storeContract.getEndDate(), bookInventory.getSettledTime(),item.getCreatedTime(),bookInventory.getWeight(), false);
						weightAmount1 += (Double) maps.get("weightAmount")/1000;
						palletDayAmount1 += (Integer) maps.get("palletDayAmount");
					}
				}
			}
		}		
		
		//包含合同托盘在内的费用
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("PalletAmount", stockOwnerChange.getStockOwnerChangeCheckItems().size());
		props.put("PalletDayAmount", palletDayAmount);
		props.put("WeightAmount", weightAmount);
		props.put("CustomerGrade", stockOwnerChange.getSeller()
				.getCustomerGrade().getId());
		Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("ownerchangeforseller"));
		for (CalculatedResult obj : results) {
			obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "ownerchangeforseller"));
		}
		
		//不包含合同托盘在内的费用
		Map<String, Object> props1 = new HashMap<String, Object>();
		props1.put("PalletAmount", stockOwnerChange.getStockOwnerChangeCheckItems().size());
		props1.put("PalletDayAmount", palletDayAmount - palletDayAmount1);
		props1.put("WeightAmount", weightAmount-weightAmount1);
		props1.put("CustomerGrade", stockOwnerChange.getSeller()
				.getCustomerGrade().getId());		
		Set<CalculatedResult> results1 = ceCalculator.calculate(props1, ceRuleService.findByBusinessType("ownerchangeforseller"));
		for (CalculatedResult obj : results1) {
			obj.setDiscountRate(deCalculator.getDiscountRate(props1, obj.getFeeItem(), "ownerchangeforseller"));
		}
		
		//将包含合同托盘在内的费用中的仓储费替换成不包含合同托盘在内的仓储费
		for(CalculatedResult obj : results)
		{		
			for(CalculatedResult obj1 : results1)
			if(obj.getFeeItem().getId()== obj1.getFeeItem().getId() && obj.getFeeItem().getId()==Constants.CE_FEE_ITEM_StorageCharges)
			{
				obj.setAmount(obj1.getAmount());
				obj.setCalculatedItemName(obj1.getCalculatedItemName());
				obj.setDiscountRate(obj1.getDiscountRate());
				obj.setFactor(obj1.getFactor());
				obj.setFeeItem(obj1.getFeeItem());
			}			
		}
		return results;
	}
	
	//月结仓储费
	public Set<CalculatedResult> CalculateMonthKnot(Long customerId) {	
		int palletDayAmount = 0;  //总天数（包含合同范围内的）
		int palletDayAmount1 = 0;  //合同范围内总天数
		double weightAmount = 0.00;    //总重量（包含合同范围内的）
		double weightAmount1 = 0.00;    //合同范围内的总重量
		double weightDayAmount = 0;  //总天数*重量（包含合同范围内的）
		double weightDayAmount1 = 0;  //合同范围内总天数*重量
		
		List<BookInventory> list = bookInventoryService.findBookInventoryByCustomerId(customerId);
		Set<CalculatedResult> results=new HashSet<CalculatedResult>();
		if (CollectionUtils.isNotEmpty(list)) {
			for(BookInventory item : list) {
				palletDayAmount += dayCount(item.getSettledTime(), new Date(),true);
				weightAmount += item.getAmount()/1000;	
				weightDayAmount += palletDayAmount * weightAmount;
			}
		}else{
			return results;
		}
		Map<String, Object> props1 = new HashMap<String, Object>();
		List<StoreContract> storeContracts = storeContractService.findContractByCustomerID(customerId);
		if(storeContracts != null && !storeContracts.isEmpty())
		{
			for(BookInventory item : list) {
				for(StoreContract storeContract: storeContracts )
				{
					if(item.getStoreLocation().getStoreArea().getId() == storeContract.getStoreArea().getId())
					{
						props1 = getEffectiveContactDayAmount(storeContract.getStartDate(),storeContract.getEndDate(), item.getSettledTime(),new Date(),item.getWeight(), true);
						weightAmount1 += (Double) props1.get("weightAmount")/1000;
						palletDayAmount1 += (Integer) props1.get("palletDayAmount");
						weightDayAmount1 += (Double) props1.get("weightDayAmount");
					}
				}
			}
		}		
		DecimalFormat df=new DecimalFormat("0.00");
		weightAmount=(weightAmount-weightAmount1);
		Customer customer = customerService.findOne(customerId);
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("PalletDayAmount", palletDayAmount-palletDayAmount1);
		props.put("WeightAmount", df.format(weightAmount));
		props.put("CustomerGrade", customer.getCustomerGrade().getId());
		props.put("WeightDayAmount", weightDayAmount-weightDayAmount1);
		//默认一个，虽然后面计算了，但是怕改动没有默认规则；
		props.put("CommonPacking", Constants.Common_Packing_1);		
		//按包装类型计算（仓储费）
		Map<String, Object> totalFeeItemAmount = CalculatePackingMonthKnot(customerId);
				
		results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("monthknot"));
		for(CalculatedResult obj : results) {
			obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "monthknot"));
			if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)){
				obj.setAmount((BigDecimal)totalFeeItemAmount.get("totalStorageChargesAmount"));
				obj.setRuleComment(totalFeeItemAmount.get("totalStorageChargesRuleComment").toString());
			}
		}
		return results;
	}

	//计算合同内的存储天数和重量
	private Map<String, Object> getEffectiveContactDayAmount(Date storeContractStartTime, Date storeContractEndTime, Date settledTime, Date createTime, double weight, boolean isMonthKnot)
	{
		int palletDayAmount =0;
		double weightAmount = 0.00;	
		double weightDayAmount = 0.00;
		//合同开始日期在上一个结算日期之前，合同结束日期在当前日期之后
		if(storeContractStartTime.before(settledTime) && storeContractEndTime.after(createTime))
		{
			palletDayAmount += dayCount(settledTime, createTime,isMonthKnot);
			weightAmount += weight/1000;
			weightDayAmount += palletDayAmount * weightAmount;
		}
		else
		{
			//合同开始日期在上一个结算日期之后，合同结束日期在当前时间之前
			if(storeContractStartTime.after(settledTime) && storeContractEndTime.before(createTime))
			{
				palletDayAmount += dayCount(storeContractStartTime, storeContractEndTime, isMonthKnot);
				weightAmount += weight/1000;
				weightDayAmount += palletDayAmount * weightAmount;
			}		
			else
			{
				//合同开始日期在上一个结算日期之后，在当前时间之前，合同结束日期在当前日期之后
				if(storeContractStartTime.after(settledTime) && storeContractStartTime.before(createTime) && storeContractEndTime.after(createTime))
				{
					palletDayAmount += dayCount(storeContractStartTime, createTime, isMonthKnot);
					weightAmount += weight/1000;
					weightDayAmount += palletDayAmount * weightAmount;
				}
				else
				{
					//合同开始时间在上一个结算日期之前，合同结束日期在上一个结算日期之后但在当前时间之前
					if(storeContractStartTime.before(settledTime) && storeContractEndTime.after(settledTime) && storeContractEndTime.before(createTime))
					{
						palletDayAmount += dayCount(settledTime, storeContractEndTime,isMonthKnot);
						weightAmount += weight/1000;
						weightDayAmount += palletDayAmount * weightAmount;
					}
				}
			}
		}
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("PalletDayAmount", palletDayAmount);
		results.put("WeightAmount", weightAmount);
		results.put("WeightDayAmount", weightDayAmount);
		return results;
	}
	
	 private static double formatDouble4(double d) {
		 BigDecimal bg = new BigDecimal(d).setScale(4, RoundingMode.UP);
		 return bg.doubleValue();
	 }
	 
	 private Map<String, Object> calculateInboundPacking(InboundRegister inboundRegister) {
		//区分包装类型来计费（装卸费、倒货费）
		double weightAmountCommon_Packing_1 = 0.00;
		double weightAmountCommon_Packing_2 = 0.00;
		double weightAmountCommon_Packing_3 = 0.00;
		int palletAmountCommon_Packing_1 = 0;
		int palletAmountCommon_Packing_2 = 0;
		int palletAmountCommon_Packing_3 = 0;
		double amountPieceCommon_Packing_1 = 0;
		double amountPieceCommon_Packing_2 = 0;
		double amountPieceCommon_Packing_3 = 0;
		//装卸费合计
		BigDecimal totalHandlingAmount = new BigDecimal(0);
		String totalHandlingRuleComment = "";
		//倒货费合计
		BigDecimal totalUnloadingAmount = new BigDecimal(0);
		String totalUnloadingRuleComment = "";
		
		Map<String, Object> totalFeeItemAmount = new HashMap<String, Object>();
		
		for(InboundReceiptItem item : inboundRegister.getInboundReceiptItems()){
			if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_1))
			{
				weightAmountCommon_Packing_1 = weightAmountCommon_Packing_1 + item.getWeight()/1000;
				palletAmountCommon_Packing_1 = palletAmountCommon_Packing_1 +1;
				amountPieceCommon_Packing_1 = amountPieceCommon_Packing_1 + item.getAmount();
			}else if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_2))
			{
				weightAmountCommon_Packing_2 = weightAmountCommon_Packing_2 + item.getWeight()/1000;
				palletAmountCommon_Packing_2 = palletAmountCommon_Packing_2 +1;
				amountPieceCommon_Packing_2 = amountPieceCommon_Packing_2 + item.getAmount();
			}else if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_3))
			{
				weightAmountCommon_Packing_3 = weightAmountCommon_Packing_3 + item.getWeight()/1000;
				palletAmountCommon_Packing_3 = palletAmountCommon_Packing_3 +1;
				amountPieceCommon_Packing_3 = amountPieceCommon_Packing_3 + item.getAmount();
			}
		}
		if (palletAmountCommon_Packing_1 > 0){
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("PalletAmount", palletAmountCommon_Packing_1);
			props.put("WeightAmount", weightAmountCommon_Packing_1);
			props.put("AmountPiece", amountPieceCommon_Packing_1);
			props.put("CustomerGrade", inboundRegister.getCustomer().getCustomerGrade().getId());
			props.put("CommonPacking", Constants.Common_Packing_1);
			Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("inbound"));
			for(CalculatedResult obj : results) {
				obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "inbound"));
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Handling)){
					totalHandlingAmount = totalHandlingAmount.add(obj.getAmount());
					totalHandlingRuleComment = totalHandlingRuleComment + obj.getRuleComment();
				}
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Unloading)){
					totalUnloadingAmount = totalUnloadingAmount.add(obj.getAmount());
					totalUnloadingRuleComment = totalUnloadingRuleComment + obj.getRuleComment();
				}
			}
		}
		if (palletAmountCommon_Packing_2 > 0){
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("PalletAmount", palletAmountCommon_Packing_2);
			props.put("WeightAmount", weightAmountCommon_Packing_2);
			props.put("AmountPiece", amountPieceCommon_Packing_2);
			props.put("CustomerGrade", inboundRegister.getCustomer().getCustomerGrade().getId());
			props.put("CommonPacking", Constants.Common_Packing_2);
			Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("inbound"));
			for(CalculatedResult obj : results) {
				obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "inbound"));
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Handling)){
					totalHandlingAmount = totalHandlingAmount.add(obj.getAmount());
					totalHandlingRuleComment = totalHandlingRuleComment + obj.getRuleComment();
				}
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Unloading)){
					totalUnloadingAmount = totalUnloadingAmount.add(obj.getAmount());
					totalUnloadingRuleComment = totalUnloadingRuleComment + obj.getRuleComment();
				}
			}
		}
		if (palletAmountCommon_Packing_3 > 0){
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("PalletAmount", palletAmountCommon_Packing_3);
			props.put("WeightAmount", weightAmountCommon_Packing_3);
			props.put("AmountPiece", amountPieceCommon_Packing_3);
			props.put("CustomerGrade", inboundRegister.getCustomer().getCustomerGrade().getId());
			props.put("CommonPacking", Constants.Common_Packing_3);
			Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("inbound"));
			for(CalculatedResult obj : results) {
				obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "inbound"));
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Handling)){
					totalHandlingAmount = totalHandlingAmount.add(obj.getAmount());
					totalHandlingRuleComment = totalHandlingRuleComment + obj.getRuleComment();
				}
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_Unloading)){
					totalUnloadingAmount = totalUnloadingAmount.add(obj.getAmount());
					totalUnloadingRuleComment = totalUnloadingRuleComment + obj.getRuleComment();
				}
			}
		}
		totalFeeItemAmount.put("totalHandlingAmount", totalHandlingAmount);
		totalFeeItemAmount.put("totalHandlingRuleComment", totalHandlingRuleComment);
		totalFeeItemAmount.put("totalUnloadingAmount", totalUnloadingAmount);
		totalFeeItemAmount.put("totalUnloadingRuleComment", totalUnloadingRuleComment);
		return totalFeeItemAmount;
		
	 }
	 
	 private Map<String, Object> calculateOutboundPacking(OutboundRegister outboundRegister) {
			//区分包装类型来计费（装卸费、倒货费）
			double weightAmountCommon_Packing_1 = 0.00;
			double weightAmountCommon_Packing_2 = 0.00;
			double weightAmountCommon_Packing_3 = 0.00;
			int palletAmountCommon_Packing_1 = 0;
			int palletAmountCommon_Packing_2 = 0;
			int palletAmountCommon_Packing_3 = 0;
			double amountPieceCommon_Packing_1 = 0;
			double amountPieceCommon_Packing_2 = 0;
			double amountPieceCommon_Packing_3 = 0;
			int palletDayAmountCommon_Packing_1 = 0;  //总天数
			int palletDayAmountCommon_Packing_2 = 0;  //总天数
			int palletDayAmountCommon_Packing_3 = 0;  //总天数
			double weightDayAmountCommon_Packing_1 = 0.00;//总天数*托盘吨数
			double weightDayAmountCommon_Packing_2 = 0.00;//总天数*托盘吨数
			double weightDayAmountCommon_Packing_3 = 0.00;//总天数*托盘吨数
			
			//仓储费合计
			BigDecimal totalStorageChargesAmount = new BigDecimal(0);
			String totalStorageChargesRuleComment = "";
			
			Map<String, Object> totalFeeItemAmount = new HashMap<String, Object>();
			
			for(OutboundCheckItem item : outboundRegister.getOutboundCheckItems()){
				if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_1))
				{
					weightAmountCommon_Packing_1 = weightAmountCommon_Packing_1 + item.getWeight()/1000;
					palletAmountCommon_Packing_1 = palletAmountCommon_Packing_1 +1;
					amountPieceCommon_Packing_1 = amountPieceCommon_Packing_1 + item.getAmount();
					palletDayAmountCommon_Packing_1 += dayCount(item.getSettledTime(), item.getCheckTime(),false);
					weightDayAmountCommon_Packing_1 += weightAmountCommon_Packing_1 * palletDayAmountCommon_Packing_1;
				}else if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_2))
				{
					weightAmountCommon_Packing_2 = weightAmountCommon_Packing_2 + item.getWeight()/1000;
					palletAmountCommon_Packing_2 = palletAmountCommon_Packing_2 +1;
					amountPieceCommon_Packing_2 = amountPieceCommon_Packing_2 + item.getAmount();
					palletDayAmountCommon_Packing_2 += dayCount(item.getSettledTime(), item.getCheckTime(),false);
					weightDayAmountCommon_Packing_2 += weightAmountCommon_Packing_2 * palletDayAmountCommon_Packing_2;
				}else if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_3))
				{
					weightAmountCommon_Packing_3 = weightAmountCommon_Packing_3 + item.getWeight()/1000;
					palletAmountCommon_Packing_3 = palletAmountCommon_Packing_3 +1;
					amountPieceCommon_Packing_3 = amountPieceCommon_Packing_3 + item.getAmount();
					palletDayAmountCommon_Packing_3 += dayCount(item.getSettledTime(), item.getCheckTime(),false);
					weightDayAmountCommon_Packing_3 += weightAmountCommon_Packing_3 * palletDayAmountCommon_Packing_3;
				}
			}
			
			if (palletAmountCommon_Packing_1 > 0){
				Map<String, Object> props = new HashMap<String, Object>();
				props.put("PalletDayAmount", palletDayAmountCommon_Packing_1);
				props.put("WeightDayAmount", weightDayAmountCommon_Packing_1);
				props.put("WeightAmount", weightAmountCommon_Packing_1);
				props.put("PalletAmount", palletAmountCommon_Packing_1);
				props.put("CustomerGrade", outboundRegister.getCustomer().getCustomerGrade().getId());		
				props.put("CommonPacking", Constants.Common_Packing_1);
				Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("outbound"));
				for(CalculatedResult obj : results) {
					obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "outbound"));
					if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)){
						totalStorageChargesAmount = totalStorageChargesAmount.add(obj.getAmount());
						totalStorageChargesRuleComment = totalStorageChargesRuleComment + obj.getRuleComment();
					}
				}
			}
			if (palletAmountCommon_Packing_2 > 0){
				Map<String, Object> props = new HashMap<String, Object>();
				props.put("PalletDayAmount", palletDayAmountCommon_Packing_2);
				props.put("WeightDayAmount", weightDayAmountCommon_Packing_2);
				props.put("WeightAmount", weightAmountCommon_Packing_2);
				props.put("PalletAmount", palletAmountCommon_Packing_2);
				props.put("CustomerGrade", outboundRegister.getCustomer().getCustomerGrade().getId());		
				props.put("CommonPacking", Constants.Common_Packing_2);
				Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("outbound"));
				for(CalculatedResult obj : results) {
					obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "outbound"));
					if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)){
						totalStorageChargesAmount = totalStorageChargesAmount.add(obj.getAmount());
						totalStorageChargesRuleComment = totalStorageChargesRuleComment + obj.getRuleComment();
					}
				}
			}
			if (palletAmountCommon_Packing_3 > 0){
				Map<String, Object> props = new HashMap<String, Object>();
				props.put("PalletDayAmount", palletDayAmountCommon_Packing_3);
				props.put("WeightDayAmount", weightDayAmountCommon_Packing_3);
				props.put("WeightAmount", weightAmountCommon_Packing_3);
				props.put("PalletAmount", palletAmountCommon_Packing_3);
				props.put("CustomerGrade", outboundRegister.getCustomer().getCustomerGrade().getId());		
				props.put("CommonPacking", Constants.Common_Packing_3);
				Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("outbound"));
				for(CalculatedResult obj : results) {
					obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "outbound"));
					if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)){
						totalStorageChargesAmount = totalStorageChargesAmount.add(obj.getAmount());
						totalStorageChargesRuleComment = totalStorageChargesRuleComment + obj.getRuleComment();
					}
				}
			}
			totalFeeItemAmount.put("totalStorageChargesAmount", totalStorageChargesAmount);
			totalFeeItemAmount.put("totalStorageChargesRuleComment", totalStorageChargesRuleComment);
			return totalFeeItemAmount;
		 }
	 
	//月结仓储费（包装类型）
	public Map<String, Object> CalculatePackingMonthKnot(Long customerId) {	
		//区分包装类型来计费（装卸费、倒货费）
		double weightAmountCommon_Packing_1 = 0.00;
		double weightAmountCommon_Packing_2 = 0.00;
		double weightAmountCommon_Packing_3 = 0.00;
		int palletDayAmountCommon_Packing_1 = 0;  //总天数
		int palletDayAmountCommon_Packing_2 = 0;  //总天数
		int palletDayAmountCommon_Packing_3 = 0;  //总天数
		double weightDayAmountCommon_Packing_1 = 0.00;//总天数*托盘吨数
		double weightDayAmountCommon_Packing_2 = 0.00;//总天数*托盘吨数
		double weightDayAmountCommon_Packing_3 = 0.00;//总天数*托盘吨数
		//合同内的包装类型
		double weightAmountStoreContractCommon_Packing_1 = 0.00;
		double weightAmountStoreContractCommon_Packing_2 = 0.00;
		double weightAmountStoreContractCommon_Packing_3 = 0.00;
		int palletDayAmountStoreContractCommon_Packing_1 = 0;  //总天数
		int palletDayAmountStoreContractCommon_Packing_2 = 0;  //总天数
		int palletDayAmountStoreContractCommon_Packing_3 = 0;  //总天数
		double weightDayAmountStoreContractCommon_Packing_1 = 0.00;//总天数*托盘吨数
		double weightDayAmountStoreContractCommon_Packing_2 = 0.00;//总天数*托盘吨数
		double weightDayAmountStoreContractCommon_Packing_3 = 0.00;//总天数*托盘吨数
		
		//仓储费合计
		BigDecimal totalStorageChargesAmount = new BigDecimal(0);
		String totalStorageChargesRuleComment = "";
		Map<String, Object> totalFeeItemAmount = new HashMap<String, Object>();
		
		List<BookInventory> list = bookInventoryService.findBookInventoryByCustomerId(customerId);
		if (CollectionUtils.isNotEmpty(list)) {
			for(BookInventory item : list) {
				if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_1)){
					palletDayAmountCommon_Packing_1 += dayCount(item.getSettledTime(), new Date(),true);
					weightAmountCommon_Packing_1 += item.getAmount()/1000;	
					weightDayAmountCommon_Packing_1 += palletDayAmountCommon_Packing_1 * weightAmountCommon_Packing_1;
				}else if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_2)){
					palletDayAmountCommon_Packing_2 += dayCount(item.getSettledTime(), new Date(),true);
					weightAmountCommon_Packing_2 += item.getAmount()/1000;	
					weightDayAmountCommon_Packing_2 += palletDayAmountCommon_Packing_2 * weightAmountCommon_Packing_2;
				}else if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_3)){
					palletDayAmountCommon_Packing_3 += dayCount(item.getSettledTime(), new Date(),true);
					weightAmountCommon_Packing_3 += item.getAmount()/1000;	
					weightDayAmountCommon_Packing_3 += palletDayAmountCommon_Packing_3 * weightAmountCommon_Packing_3;
				}
			}
		}else{
			totalFeeItemAmount.put("totalStorageChargesAmount", 0);
			totalFeeItemAmount.put("totalStorageChargesRuleComment", "");
			return totalFeeItemAmount;
		}
		Map<String, Object> props1 = new HashMap<String, Object>();
		Map<String, Object> props2 = new HashMap<String, Object>();
		Map<String, Object> props3 = new HashMap<String, Object>();
		List<StoreContract> storeContracts = storeContractService.findContractByCustomerID(customerId);
		if(storeContracts != null && !storeContracts.isEmpty())
		{
			for(BookInventory item : list) {
				for(StoreContract storeContract: storeContracts )
				{
					if(item.getStoreLocation().getStoreArea().getId() == storeContract.getStoreArea().getId())
					{
						if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_1)){
							props1 = getEffectiveContactDayAmount(storeContract.getStartDate(),storeContract.getEndDate(), item.getSettledTime(),new Date(),item.getWeight(), true);
							weightAmountStoreContractCommon_Packing_1 += (Double) props1.get("weightAmount")/1000;
							palletDayAmountStoreContractCommon_Packing_1 += (Integer) props1.get("palletDayAmount");
							weightDayAmountStoreContractCommon_Packing_1 += (Double) props1.get("weightDayAmount");
						}else if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_2)){
							props2 = getEffectiveContactDayAmount(storeContract.getStartDate(),storeContract.getEndDate(), item.getSettledTime(),new Date(),item.getWeight(), true);
							weightAmountStoreContractCommon_Packing_2 += (Double) props2.get("weightAmount")/1000;
							palletDayAmountStoreContractCommon_Packing_2 += (Integer) props2.get("palletDayAmount");
							weightDayAmountStoreContractCommon_Packing_2 += (Double) props2.get("weightDayAmount");
						}else if(item.getProduct().getCommonPacking().getId().equals(Constants.Common_Packing_3)){
							props3 = getEffectiveContactDayAmount(storeContract.getStartDate(),storeContract.getEndDate(), item.getSettledTime(),new Date(),item.getWeight(), true);
							weightAmountStoreContractCommon_Packing_3 += (Double) props3.get("weightAmount")/1000;
							palletDayAmountStoreContractCommon_Packing_3 += (Integer) props3.get("palletDayAmount");
							weightDayAmountStoreContractCommon_Packing_3 += (Double) props3.get("weightDayAmount");
						}
						
					}
				}
			}
		}		
		DecimalFormat df=new DecimalFormat("0.00");
		Customer customer = customerService.findOne(customerId);
		if (palletDayAmountCommon_Packing_1 > 0){
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("PalletDayAmount", palletDayAmountCommon_Packing_1 - palletDayAmountStoreContractCommon_Packing_1);
			props.put("WeightDayAmount", weightDayAmountCommon_Packing_1 - weightDayAmountStoreContractCommon_Packing_1);
			props.put("WeightAmount", df.format(weightAmountCommon_Packing_1 - weightAmountStoreContractCommon_Packing_1));
			props.put("CustomerGrade", customer.getCustomerGrade().getId());
			props.put("CommonPacking", Constants.Common_Packing_1);
			Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("monthknot"));
			for(CalculatedResult obj : results) {
				obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "monthknot"));
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)){
					totalStorageChargesAmount = totalStorageChargesAmount.add(obj.getAmount());
					totalStorageChargesRuleComment = totalStorageChargesRuleComment + obj.getRuleComment();
				}
			}
		}
		
		if (palletDayAmountCommon_Packing_2 > 0){
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("PalletDayAmount", palletDayAmountCommon_Packing_2 - palletDayAmountStoreContractCommon_Packing_2);
			props.put("WeightDayAmount", weightDayAmountCommon_Packing_2 - weightDayAmountStoreContractCommon_Packing_2);
			props.put("WeightAmount", df.format(weightAmountCommon_Packing_2 - weightAmountStoreContractCommon_Packing_2));
			props.put("CustomerGrade", customer.getCustomerGrade().getId());		
			props.put("CommonPacking", Constants.Common_Packing_2);
			Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("monthknot"));
			for(CalculatedResult obj : results) {
				obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "monthknot"));
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)){
					totalStorageChargesAmount = totalStorageChargesAmount.add(obj.getAmount());
					totalStorageChargesRuleComment = totalStorageChargesRuleComment + obj.getRuleComment();
				}
			}
		}
		
		if (palletDayAmountCommon_Packing_3 > 0){
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("PalletDayAmount", palletDayAmountCommon_Packing_3 - palletDayAmountStoreContractCommon_Packing_3);
			props.put("WeightDayAmount", weightDayAmountCommon_Packing_3 - weightDayAmountStoreContractCommon_Packing_3);
			props.put("WeightAmount", df.format(weightAmountCommon_Packing_3 - weightAmountStoreContractCommon_Packing_3));
			props.put("CustomerGrade", customer.getCustomerGrade().getId());		
			props.put("CommonPacking", Constants.Common_Packing_3);
			Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("monthknot"));
			for(CalculatedResult obj : results) {
				obj.setDiscountRate(deCalculator.getDiscountRate(props, obj.getFeeItem(), "monthknot"));
				if (obj.getFeeItem().getId().equals(Constants.CE_FEE_ITEM_StorageCharges)){
					totalStorageChargesAmount = totalStorageChargesAmount.add(obj.getAmount());
					totalStorageChargesRuleComment = totalStorageChargesRuleComment + obj.getRuleComment();
				}
			}
		}
		
		totalFeeItemAmount.put("totalStorageChargesAmount", totalStorageChargesAmount);
		totalFeeItemAmount.put("totalStorageChargesRuleComment", totalStorageChargesRuleComment);
		return totalFeeItemAmount;
	}
}
