package com.wwyl.entity.ce;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author sjwang
 */
@Entity
@Table(name = "TJ_DE_OPTIONAL_ITEM")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DEOptionalItem extends OptionalItem {
	private String discountType; //业务类型，如inbound, outbound,等
	
	@Column(length = 50, nullable = false, unique = false)
	public String getDiscountType() {
		return discountType;
	}
	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

}
