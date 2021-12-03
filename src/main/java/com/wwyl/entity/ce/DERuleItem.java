package com.wwyl.entity.ce;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.ce.CEConstant.RelationOperator;

/**
 * @author sjwang
 */
@Entity
@Table(name = "TJ_DE_RULE_ITEM")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Access(AccessType.PROPERTY)
public class DERuleItem extends PersistableEntity {
	private DEOptionalItem optionalItem;
	private String value;
	private RelationOperator relation;
	
	@JsonIgnore
	private DERule deRule;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity=DERule.class)
	public DERule getDeRule() {
		return deRule;
	}
	public void setDeRule(DERule deRule) {
		this.deRule = deRule;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity=DEOptionalItem.class)
	public DEOptionalItem getOptionalItem() {
		return optionalItem;
	}
	public void setOptionalItem(DEOptionalItem optionalItem) {
		this.optionalItem = optionalItem;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public RelationOperator getRelation() {
		return relation;
	}
	public void setRelation(RelationOperator relation) {
		this.relation = relation;
	}
	
	

}
