package com.wwyl.entity.ce;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author sjwang
 */
public class CalculatedResult {

	private FeeItem feeItem;//费用项
	private BigDecimal amount;//总费用
	private BigDecimal factor;//基数
	private String calculatedItemName;//计费名称
	private String ruleComment;
	private BigDecimal discountRate;//折率
	
	public FeeItem getFeeItem() {
		return feeItem;
	}
	public void setFeeItem(FeeItem feeItem) {
		this.feeItem = feeItem;
	}
	public BigDecimal getAmount() {
		return amount.setScale(2);
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount.setScale(2, RoundingMode.HALF_UP);
	}
	public BigDecimal getFactor() {
		return factor;
	}
	public void setFactor(BigDecimal factor) {
		this.factor = factor.setScale(3);
	}
	public String getCalculatedItemName() {
		return calculatedItemName;
	}
	public void setCalculatedItemName(String calculatedItemName) {
		this.calculatedItemName = calculatedItemName;
	}
	public String getRuleComment() {
		return ruleComment;
	}
	public void setRuleComment(String ruleComment) {
		this.ruleComment = ruleComment;
	}
	public BigDecimal getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate.setScale(4);
	}

    public BigDecimal getDiscountedAmount(){
        return this.getAmount().multiply(this.getDiscountRate()).setScale(2, RoundingMode.HALF_UP);
    }

}
