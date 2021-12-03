package com.wwyl.entity.ce;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
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
public abstract class Rule extends PersistableEntity {

	private String name;
	private int priority;
	private ItemRelation itemRelation;
	
	@JsonIgnore
	protected RuleType ruleType;

	protected Set ruleItems = new java.util.HashSet();
	protected Set calculationItems = new java.util.HashSet();
	
	@Transient
	public abstract Set getCalculationItems();

	@Transient
	public abstract Set getRuleItems();

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

}
