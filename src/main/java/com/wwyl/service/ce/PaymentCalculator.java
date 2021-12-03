package com.wwyl.service.ce;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wwyl.Enums.PaymentBoundType;
import com.wwyl.entity.ce.CalculatedResult;
import com.wwyl.entity.ce.FeeItem;
import com.wwyl.entity.finance.Payment;

/**
 * @author hehao
 */
@Service
public class PaymentCalculator {
	@Resource 
	private CECalculator ceCalculator;
	
	@Resource
	private CERuleService ceRuleService;
	
	@Resource
	private DECalculator deCalculator;
	
	public Map<String,Object> calculate(Payment payment, Double weightTotal, Double amountPiece, int storeContainerCount, FeeItem feeItem) {
		Map<String, Object> props = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();	
		props.put("PalletAmount", storeContainerCount);
		props.put("WeightAmount", weightTotal/1000);
		props.put("CustomerGrade", payment.getCustomer().getCustomerGrade().getId());
		props.put("Product", payment.getProduct().getId());
		props.put("AmountPiece", amountPiece);
		props.put("WeightDayAmount", 0);
		props.put("CommonPacking", payment.getProduct().getCommonPacking().getId());
		if (payment.getPaymentBoundType().equals(PaymentBoundType.入库管理)){
			Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("inbound"));
			for(CalculatedResult obj : results) {
				if (obj.getFeeItem().getId().equals(feeItem.getId())){
					result.put("amount", obj.getAmount());
					result.put("ruleComment", obj.getRuleComment());
				//	amount = obj.getAmount();
				//	ruleComment = obj.getRuleComment();
					break;
				}
			}
		}
		else
		{
			Set<CalculatedResult> results = ceCalculator.calculate(props, ceRuleService.findByBusinessType("outbound"));
			for(CalculatedResult obj : results) {
				if (obj.getFeeItem().getId().equals(feeItem.getId())){
				//	amount = obj.getAmount();
				//	ruleComment = obj.getRuleComment();
					result.put("amount", obj.getAmount());
					result.put("ruleComment", obj.getRuleComment());
					break;
				}
			}
		}
		return result;
	}	
	
}
