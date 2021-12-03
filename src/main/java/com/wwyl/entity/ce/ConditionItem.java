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
public abstract class ConditionItem extends PersistableEntity {
	private String value;
	private RelationOperator relation;
	private String itemComment;

	protected OptionalItem optionalItem;
	@JsonIgnore
	protected CalculationItem calculationItem;
	
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
	public String getItemComment() {
		return itemComment;
	}
	public void setItemComment(String itemComment) {
		this.itemComment = itemComment;
	}
	@Transient
	public abstract OptionalItem getOptionalItem();
	@Transient
	public abstract CalculationItem getCalculationItem();
}
