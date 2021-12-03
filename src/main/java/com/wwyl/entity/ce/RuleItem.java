package com.wwyl.entity.ce;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.ce.CEConstant.RelationOperator;

/**
 * @author sjwang
 */
@MappedSuperclass
@Access(AccessType.PROPERTY)
public abstract class RuleItem extends PersistableEntity {
	private String value;
	private RelationOperator relation;
	
	protected OptionalItem optionalItem;
	@JsonIgnore
	protected Rule ceRule;
	
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
	
	@Transient
	public abstract OptionalItem getOptionalItem();
	@Transient
	public abstract Rule getCeRule();
}
