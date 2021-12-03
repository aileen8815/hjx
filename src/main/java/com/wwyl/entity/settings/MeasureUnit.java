package com.wwyl.entity.settings;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.MeasureUnitType;
import com.wwyl.entity.PersistableEntity;

/**
 * 计量单位
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_MEASURE_UNIT")
@Cacheable
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class MeasureUnit extends PersistableEntity {

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String code;
	@NotBlank
	@Column(length = 100, nullable = false, unique = true)
	private String name;
	@Basic
	private MeasureUnitType measureUnitType;

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

	public MeasureUnitType getMeasureUnitType() {
		return measureUnitType;
	}

	public void setMeasureUnitType(MeasureUnitType measureUnitType) {
		this.measureUnitType = measureUnitType;
	}

}
