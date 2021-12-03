package com.wwyl.entity.ce;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.ce.Rule;
import com.wwyl.entity.ce.RuleType;

/**
 * @author sjwang
 */
@Entity
@Table(name = "TJ_CE_RULE_TYPE")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(AccessType.PROPERTY)
public class CERuleType extends RuleType {
	private String businessType;
	
	@OneToMany(mappedBy="ruleType", fetch=FetchType.LAZY, targetEntity=CERule.class)
	@OrderBy("priority DESC")
	@Override
	public Set<Rule> getRules() {
		return rules;
	}

	public void setRules(Set<Rule> rules) {
		this.rules = rules;
	}

	@Column(length = 50, nullable = false, unique = false)
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	
}
