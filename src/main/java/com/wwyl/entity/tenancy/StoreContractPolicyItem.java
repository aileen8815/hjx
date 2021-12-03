package com.wwyl.entity.tenancy;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.settings.MeasureUnit;

/**
 * @author pixf
 */
@Entity
@Table(name = "TJ_STORE_CONTRACT_POLICY_ITEM")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StoreContractPolicyItem extends PersistableEntity {
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private StoreContract storeContract;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private CEFeeItem feeItem;//档位收费项目	
	
	@NotNull
	@Basic
	private double amount;    //收费单价	
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private MeasureUnit measureUnit;// 计量单位	
	
	@Basic	
	private double useLimited;         //使用限量
	
	public StoreContract getStoreContract() {
		return storeContract;
	}

	public void setStoreContract(StoreContract stallContract) {
		this.storeContract = stallContract;
	}

	public CEFeeItem getFeeItem() {
		return feeItem;
	}

	public void setFeeItem(CEFeeItem feeItem) {
		this.feeItem = feeItem;
	}
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public MeasureUnit getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(MeasureUnit measureUnit) {
		this.measureUnit = measureUnit;
	}	
	
	public double getUseLimited() {
		return useLimited;
	}
	
	public void setUseLimited(double useLimited) {
		this.useLimited = useLimited;
	}
	
	public String getFeeName() {
		return this.feeItem.getName();
	}	
	
	public String getMeasureUnitName(){
		return this.measureUnit.getName();
	}	
}
