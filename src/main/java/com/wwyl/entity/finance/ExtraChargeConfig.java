package com.wwyl.entity.finance;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.ce.CEFeeItem;

/**
 * 
 * 市场向客户代收的费用比例
 * @author jianl
 */
@Entity
@Table(name = "TJ_EXTRA_CHARGE_CONFIG")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class ExtraChargeConfig extends PersistableEntity {

	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String code; // 编码;
	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String name; // 名字;
	
	@NotNull
	@ManyToOne
	private CEFeeItem feeItem; // 费用项
	@Basic
	private double proportion; // 市场收取费用后，需付给第三方的费用比例  0-100
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
	public CEFeeItem getFeeItem() {
		return feeItem;
	}
	public void setFeeItem(CEFeeItem feeItem) {
		this.feeItem = feeItem;
	}
	public double getProportion() {
		return proportion;
	}
	public void setProportion(double proportion) {
		this.proportion = proportion;
	}
	

}
