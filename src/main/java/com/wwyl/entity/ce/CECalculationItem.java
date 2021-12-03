package com.wwyl.entity.ce;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.ce.CalculationItem;

/**
 * @author sjwang
 */
@Entity
@Table(name = "TJ_CE_CALCULATION_ITEM")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(AccessType.PROPERTY)
public class CECalculationItem extends CalculationItem {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CEFeeItem.class)
	public CEFeeItem getFeeItem() {
		return (CEFeeItem)feeItem;
	}
	public void setFeeItem(CEFeeItem feeItem) {
		this.feeItem = feeItem;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity= CEOptionalItem.class)
	public CEOptionalItem getOptionalItem() {
		return (CEOptionalItem)optionalItem;
	}
	public void setOptionalItem(CEOptionalItem optionalItem) {
		this.optionalItem = optionalItem;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity=CERule.class)
	public CERule getCeRule() {
		return (CERule)ceRule;
	}
	public void setCeRule(CERule ceRule) {
		this.ceRule = ceRule;
	}
	
	@SuppressWarnings("unchecked")
	@OneToMany(mappedBy="calculationItem", fetch=FetchType.LAZY, targetEntity=CEConditionItem.class,
			cascade=CascadeType.REMOVE)
	public Set<CEConditionItem> getConditionItems() {
		return conditionItems;
	}
	public void setConditionItems(Set<CEConditionItem> conditionItems) {
		this.conditionItems = conditionItems;
	}
	

}
