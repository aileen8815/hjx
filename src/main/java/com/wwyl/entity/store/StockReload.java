package com.wwyl.entity.store;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.Customer;

/**
 * 库内移位作业单
 * 
 * @author fyunli
 */
public class StockReload extends PersistableEntity {
	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String serialNo; // 单号;
	@NotNull
	@ManyToOne
	private Customer customer; // 客户;
}
