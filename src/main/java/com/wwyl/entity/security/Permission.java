package com.wwyl.entity.security;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;

/**
 * 权限
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_PERMISSION")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
@Cacheable
public class Permission extends PersistableEntity {

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String code; // 权限编码
	@NotBlank
	@Column(length = 50, nullable = false)
	private String name; // 权限名称
	@NotBlank
	@Column(length = 200)
	private String remark; // 备注
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Permission parent; // 上级权限

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("code")
	private Set<Permission> children; // 下级权限

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

	public Permission getParent() {
		return parent;
	}

	public void setParent(Permission parent) {
		this.parent = parent;
	}

	public Set<Permission> getChildren() {
		return children;
	}

	public void setChildren(Set<Permission> children) {
		this.children = children;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getText() {
		return this.code + ' ' + this.name;
	}

	public static final String PERM_TALLY = "1001"; // 理货

}
