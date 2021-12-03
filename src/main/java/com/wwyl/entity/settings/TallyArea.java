package com.wwyl.entity.settings;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.TallyAreaStatus;
import com.wwyl.entity.PersistableEntity;

/**
 * 理货区
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_TALLY_AREA")
@Cacheable
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class TallyArea extends PersistableEntity {

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String code;// 编号
	@NotBlank
	@Column(length = 100, nullable = false, unique = true)
	private String name;// 名称
	@NotNull
	@ManyToOne
	private TallyAreaType tallyAreaType;// 理货区类型，如发货区、收货区、通用
	@Column(length = 255)
	private String remark;// 备注
	@Basic
	private TallyAreaStatus tallyAreaStatus;// 理货区状态

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

	public TallyAreaType getTallyAreaType() {
		return tallyAreaType;
	}

	public void setTallyAreaType(TallyAreaType tallyAreaType) {
		this.tallyAreaType = tallyAreaType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public TallyAreaStatus getTallyAreaStatus() {
		return tallyAreaStatus;
	}

	public void setTallyAreaStatus(TallyAreaStatus tallyAreaStatus) {
		this.tallyAreaStatus = tallyAreaStatus;
	}

	public String getTallyAreaTyreDesc() {
		return this.getTallyAreaType().getName();
	}

}
