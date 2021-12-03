package com.wwyl.entity.tenancy;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.ContractStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.StoreArea;

/**
 * @author sjwang 仓位租赁合同
 */
@Entity
@Table(name = "TJ_STORE_CONTRACT")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StoreContract extends PersistableEntity {

	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String serialNo;// 单据编号
	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String contractNo;// 合同编号
	@NotNull
	@ManyToOne
	private Customer customer;// 客户

	private ContractStatus status;// 合同状态
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date signedDate;// 签订日期
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;// 开始日期
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;// 结束日期
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date chargeDate;// 计费起始日期
	@Column(length = 255)
	private String remark;
	@Temporal(TemporalType.TIMESTAMP)
	private Date operateTime;
	@NotNull
	@ManyToOne
	private StoreArea storeArea;// 库区
	@NotNull
	private double rentalArea;      //使用面积



	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "storeContract", cascade = CascadeType.REMOVE)
	private Set<StoreContractFeeItem> contractFeeItems;          //固定费用 
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "storeContract", cascade = CascadeType.REMOVE)
	private Set<StoreContractLocationItem> contractLocationItems;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "storeContract", cascade = CascadeType.REMOVE)
	private Set<StoreContractPolicyItem> contractPolicyItems;    //协议费用
	
	public StoreArea getStoreArea() {
		return storeArea;
	}

	public void setStoreArea(StoreArea storeArea) {
		this.storeArea = storeArea;
	}
	
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	

	public ContractStatus getStatus() {
		return status;
	}

	public void setStatus(ContractStatus status) {
		this.status = status;
	}

	public Date getSignedDate() {
		return signedDate;
	}

	public void setSignedDate(Date signedDate) {
		this.signedDate = signedDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getChargeDate() {
		return chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}
	
	public double getRentalArea() {
		return rentalArea;
	}
	
	public void setRentalArea(double rentalArea) {
		this.rentalArea = rentalArea;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Set<StoreContractFeeItem> getContractFeeItems() {
		return contractFeeItems;
	}

	public void setContractFeeItems(Set<StoreContractFeeItem> contractItems) {
		this.contractFeeItems = contractItems;
	}

	public Set<StoreContractLocationItem> getContractLocationItems() {
		return contractLocationItems;
	}

	public void setContractLocationItems(Set<StoreContractLocationItem> contractLocationItems) {
		this.contractLocationItems = contractLocationItems;
	}
	
	public Set<StoreContractPolicyItem> getContractPolicyItems() {
		return contractPolicyItems;
	}

	public void setContractPolicyItems(Set<StoreContractPolicyItem> contractPolicyItems) {
		this.contractPolicyItems = contractPolicyItems;
	}	

	public String getCustomerName(){
		return this.customer.getName();
	}	
	
	public String getCustomerText(){
		return this.customer.getsMemberCode() + " " +this.customer.getName();
	}	
	
	public String getStoreAreaText(){
		return this.storeArea.getCode()+ "  " +this.storeArea.getName();
	}	
}
