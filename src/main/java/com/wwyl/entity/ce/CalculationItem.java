package com.wwyl.entity.ce;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.ce.CEConstant.ItemRelation;

/**
 * @author sjwang
 */
@MappedSuperclass
@Access(AccessType.PROPERTY)
public abstract class CalculationItem extends PersistableEntity {
	private BigDecimal factor;
	private BigDecimal addedValue;
	private BigDecimal decrease;
	private ItemRelation conditionRelation;
	private String itemComment;
	
	protected FeeItem feeItem;
	protected OptionalItem optionalItem;
	@JsonIgnore
	protected Rule ceRule;
	
	protected Set conditionItems = new HashSet();
	
	public BigDecimal getFactor() {
		return factor;
	}
	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}
	public BigDecimal getAddedValue() {
		if(addedValue == null) return new BigDecimal(0);
		else return addedValue;
	}
	public void setAddedValue(BigDecimal addedValue) {
		this.addedValue = addedValue;
	}
	public BigDecimal getDecrease() {
		if(decrease == null) return new BigDecimal(0);
		else return decrease;
	}
	public void setDecrease(BigDecimal decrease) {
		this.decrease = decrease;
	}
	public ItemRelation getConditionRelation() {
		return conditionRelation;
	}
	public void setConditionRelation(ItemRelation conditionRelation) {
		this.conditionRelation = conditionRelation;
	}
	public String getItemComment() {
		return itemComment;
	}
	public void setItemComment(String itemComment) {
		this.itemComment = itemComment;
	}
	@Transient
	public abstract FeeItem getFeeItem();
	@Transient
	public abstract OptionalItem getOptionalItem();
	@Transient
	public abstract Rule getCeRule();
	@Transient
	public abstract Set getConditionItems();
	
}
