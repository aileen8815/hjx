package com.wwyl.entity.ce;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.ce.OptionalItem;

/**
 * @author sjwang
 */
@Entity
@Table(name = "TJ_CE_OPTIONAL_ITEM")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CEOptionalItem extends OptionalItem {
	private String businessType;
	
	@Column(length = 50, nullable = false, unique = false)
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
}
