package com.wwyl.entity.settings;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;

/**
 * 行政区划
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_AREA")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Cacheable
public class Area extends PersistableEntity {

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String code;// 编号
	@NotBlank
	@Column(length = 100, nullable = false)
	private String name;// 地名
	@Column(length = 10)
	private String areaCode; // 电话区号
	@Column(length = 10)
	private String zip;// 邮编
	@Column(length = 255)
	private String remark;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Area parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("code")
	private Set<Area> children;
	@Transient
	private String state;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Area getParent() {
		return parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	public Set<Area> getChildren() {
		return children;
	}

	public void setChildren(Set<Area> children) {
		this.children = children;
	}

	public String getPath() {
		if (parent == null) {
			return name;
		}
		return parent.getPath() + "," + name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
