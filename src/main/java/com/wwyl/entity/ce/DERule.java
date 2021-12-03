package com.wwyl.entity.ce;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.ce.CEConstant.ItemRelation;

/**
 * @author sjwang
 */
@Entity
@Table(name = "TJ_DE_RULE")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(AccessType.PROPERTY)
public class DERule extends PersistableEntity {
	private String name;
	private int priority;
	private ItemRelation itemRelation;
	private BigDecimal factor;
	private CEFeeItem feeItem;
	private String businessType;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity=CEFeeItem.class)
	public CEFeeItem getFeeItem() {
		return feeItem;
	}

	public void setFeeItem(CEFeeItem feeItem) {
		this.feeItem = feeItem;
	}

	private Set<DERuleItem> ruleItems;

	@OneToMany(mappedBy="deRule", fetch=FetchType.LAZY, targetEntity=DERuleItem.class,
			cascade=CascadeType.REMOVE)
	public Set<DERuleItem> getRuleItems() {
		return ruleItems;
	}

	public void setRuleItems(Set<DERuleItem> ruleItems) {
		this.ruleItems = ruleItems;
	}

	@Column(length = 100, nullable = false, unique = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public ItemRelation getItemRelation() {
		return itemRelation;
	}

	public void setItemRelation(ItemRelation itemRelation) {
		this.itemRelation = itemRelation;
	}
	
	public BigDecimal getFactor() {
		return factor;
	}
	
	public void setFactor(BigDecimal factor) {
		this.factor = factor.setScale(4);
	}

	@Column(length = 100, nullable = false, unique = false)
	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
}
