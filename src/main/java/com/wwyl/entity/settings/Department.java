package com.wwyl.entity.settings;

import java.util.Set;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;

/**
 * 部门-组织结构
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_DEPARTMENT")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
@Cacheable
public class Department extends PersistableEntity {

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String code; // 部门编号
	@NotBlank
	@Column(length = 100, nullable = false)
	private String name; // 部门名称
	@Column(length = 255)
	private String remark; // 备注

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Department parent; // 上级部门

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OrderBy("code")
	private Set<Department> children; // 下属部门

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Department getParent() {
		return parent;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	public Set<Department> getChildren() {
		return children;
	}

	public void setChildren(Set<Department> children) {
		this.children = children;
	}

	public String getText() {
		return this.getCode() + this.getName();
	}

}
