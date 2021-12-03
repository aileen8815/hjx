package com.wwyl.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Customer;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.Set;

/**
 * 货权转移
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_STOCK_OWNER_CHANGE")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class StockOwnerChange extends PersistableEntity {

	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String serialNo; // 单号;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer seller;// 转让人
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer buyer; // 受让人
	@Temporal(TemporalType.TIMESTAMP)
	private Date changeTime;// 转让时间
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator register;// 登记人
	@Temporal(TemporalType.TIMESTAMP)
	private Date regTime;// 登记时间
	@Basic
	private Enums.StockOwnerChangeStatus stockOwnerChangeStatus;
	@Temporal(TemporalType.TIMESTAMP)
	private Date effectTime; // 生效、作废时间
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "stockOwnerChange")
	private Set<StockOwnerChangeRegisterItem> stockOwnerChangeRegisterItems;
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "stockOwnerChange")
	private Set<StockOwnerChangeCheckItem> stockOwnerChangeCheckItems;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Payment sellerPayment;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Payment buyerPayment;

	public Customer getSeller() {
		return seller;
	}

	public void setSeller(Customer seller) {
		this.seller = seller;
	}

	public Customer getBuyer() {
		return buyer;
	}

	public void setBuyer(Customer buyer) {
		this.buyer = buyer;
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public Operator getRegister() {
		return register;
	}

	public void setRegister(Operator register) {
		this.register = register;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public Enums.StockOwnerChangeStatus getStockOwnerChangeStatus() {
		return stockOwnerChangeStatus;
	}

	public void setStockOwnerChangeStatus(
			Enums.StockOwnerChangeStatus stockOwnerChangeStatus) {
		this.stockOwnerChangeStatus = stockOwnerChangeStatus;
	}

	public Date getEffectTime() {
		return effectTime;
	}

	public void setEffectTime(Date effectTime) {
		this.effectTime = effectTime;
	}

	public Set<StockOwnerChangeRegisterItem> getStockOwnerChangeRegisterItems() {
		return stockOwnerChangeRegisterItems;
	}

	public void setStockOwnerChangeRegisterItems(
			Set<StockOwnerChangeRegisterItem> stockOwnerChangeRegisterItems) {
		this.stockOwnerChangeRegisterItems = stockOwnerChangeRegisterItems;
	}

	public Set<StockOwnerChangeCheckItem> getStockOwnerChangeCheckItems() {
		return stockOwnerChangeCheckItems;
	}

	public void setStockOwnerChangeCheckItems(
			Set<StockOwnerChangeCheckItem> stockOwnerChangeCheckItems) {
		this.stockOwnerChangeCheckItems = stockOwnerChangeCheckItems;
	}

	public Payment getSellerPayment() {
		return sellerPayment;
	}

	public void setSellerPayment(Payment sellerPayment) {
		this.sellerPayment = sellerPayment;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Payment getBuyerPayment() {
		return buyerPayment;
	}

	public void setBuyerPayment(Payment buyerPayment) {
		this.buyerPayment = buyerPayment;
	}

	public String getSellerName() {
		return seller != null ? seller.getName() : null;
	}

	public String getBuyerName() {
		return buyer != null ? buyer.getName() : null;
	}
}
