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
import com.wwyl.entity.ce.Rule;

/**
 * @author sjwang
 */
@Entity
@Table(name = "TJ_CE_RULE")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(AccessType.PROPERTY)
public class CERule extends Rule {

	Set<CERuleItem> ruleItems;
	Set<CECalculationItem> calculationItems;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity=CERuleType.class)
	public CERuleType getRuleType() {
		return (CERuleType)ruleType;
	}

	public void setRuleType(CERuleType ruleType) {
		this.ruleType = ruleType;
	}

	@OneToMany(mappedBy="ceRule", fetch=FetchType.LAZY, targetEntity=CERuleItem.class,
			cascade=CascadeType.REMOVE)
	public Set<CERuleItem> getRuleItems() {
		return ruleItems;
	}

	public void setRuleItems(Set<CERuleItem> ruleItems) {
		this.ruleItems = ruleItems;
	}

	@OneToMany(mappedBy="ceRule", fetch=FetchType.LAZY, targetEntity=CECalculationItem.class,
			cascade=CascadeType.REMOVE)
	public Set<CECalculationItem> getCalculationItems() {
		return calculationItems;
	}

	public void setCalculationItems(Set<CECalculationItem> calculationItems) {
		this.calculationItems = calculationItems;
	}
	
}
