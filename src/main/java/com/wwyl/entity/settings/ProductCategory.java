package com.wwyl.entity.settings;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
 

/**
 * 商品类别
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_PRODUCT_CATEGORY")
@Cacheable
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class ProductCategory extends PersistableEntity {

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	String code;
	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	String name;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	ProductCategory parent;
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OrderBy("code")
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	Set<ProductCategory> children;

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

	public ProductCategory getParent() {
		return parent;
	}

	public void setParent(ProductCategory parent) {
		this.parent = parent;
	}

	public Set<ProductCategory> getChildren() {
		return children;
	}

	public void setChildren(Set<ProductCategory> children) {
		this.children = children;
	}

	public String getText() {
		return this.getCode() + " " + this.getName();
	}
	
	public boolean belongTo(Long parentId) {
		if(parentId.equals(getId())) return true;
		if(parent==null) return false;
		if(parentId.equals(parent.getId())) return true;
		else return parent.belongTo(parentId);
	}
}
