package com.wwyl.entity.ce;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wwyl.entity.PersistableEntity;

/**
 * @author sjwang
 */
@MappedSuperclass
@Table(name = "TJ_RULE_TYPE")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.PROPERTY)
public abstract class RuleType extends PersistableEntity {

	private String code;
	private String typeName;
	protected Set<Rule> rules;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Transient
	public abstract Set<Rule> getRules();

}
