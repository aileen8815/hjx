package com.wwyl.entity.ce;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.ce.RuleItem;

/**
 * @author sjwang
 */
@Entity
@Table(name = "TJ_CE_RULE_ITEM")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(AccessType.PROPERTY)
public class CERuleItem extends RuleItem {
	@ManyToOne(fetch = FetchType.LAZY, targetEntity=CEOptionalItem.class)
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

}
