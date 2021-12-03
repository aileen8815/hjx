package com.wwyl.entity.tenancy;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.OperateType;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.ce.CEFeeItem;

/**
 * @author sjwang 仓库租赁合同费用明细
 */
@Entity
@Table(name = "TJ_STORE_CONTRACT_FEE_ITEM")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StoreContractFeeItem extends PersistableEntity {

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private StoreContract storeContract;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private CEFeeItem feeItem;//档位收费项目

	@NotNull
	@Basic
	private double amount;//收费金额
	@NotNull
	@Basic
	private int period;//收费周期，只能填入数字1--12
	@NotNull
	private OperateType operateType;//操作类型
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date ehargeEndDate;// 最后计费日期

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

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public OperateType getOperateType() {
		return operateType;
	}

	public void setOperateType(OperateType operateType) {
		this.operateType = operateType;
	}
	
	public String getFeeName() {
		return this.feeItem.getName();
	}

	public Date getEhargeEndDate() {
		return ehargeEndDate;
	}

	public void setEhargeEndDate(Date ehargeEndDate) {
		this.ehargeEndDate = ehargeEndDate;
	}

}
