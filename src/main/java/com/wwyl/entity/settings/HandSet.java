package com.wwyl.entity.settings;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.HandSetStatus;
import com.wwyl.entity.PersistableEntity;

/**
 * 装卸口
 * 
 * @author zhaozz
 */
@Entity
@Table(name = "TJ_HandSet")
@Cacheable
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class HandSet extends PersistableEntity {

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String code;
	@NotBlank
	@Column(length = 100, nullable = false, unique = true)
	private String name;
	@NotBlank
	@Column(length = 100, nullable = false, unique = true)
	private String mac;
	@Basic
	private HandSetStatus handSetStatus;// 状态

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
	
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public HandSetStatus getHandSetStatus() {
		return handSetStatus;
	}

	public void setHandSetStatus(HandSetStatus handSetStatus) {
		this.handSetStatus = handSetStatus;
	}

}
