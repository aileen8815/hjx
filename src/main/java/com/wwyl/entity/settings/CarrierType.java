package com.wwyl.entity.settings;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;

/**
 * 承运商类型
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_CARRIER_TYPE")
@Cacheable
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class CarrierType extends PersistableEntity {

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String code;
	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String name;
	@Column(length = 255)
	private String remark;

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

}
