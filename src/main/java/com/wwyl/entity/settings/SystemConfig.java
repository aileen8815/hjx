package com.wwyl.entity.settings;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.SystemConfigKey;
import com.wwyl.entity.PersistableEntity;

/**
 * 系统参数配置
 * 
 * @author lj
 */
@Entity
@Table(name = "TJ_SYSTEM_CONFIG")
@Cacheable
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class SystemConfig extends PersistableEntity {

	@NotBlank
	private SystemConfigKey attribute;//参数key
	@NotBlank
	@Column(length = 100, nullable = false)
	private String value;//参数value
	public SystemConfigKey getAttribute() {
		return attribute;
	}
	public void setAttribute(SystemConfigKey attribute) {
		this.attribute = attribute;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	

}
