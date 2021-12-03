package com.wwyl.entity.store;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.InboundType;
import com.wwyl.Enums.StockInStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.settings.TallyArea;
import com.wwyl.entity.settings.VehicleType;

/**
 * 入库登记
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_INBOUND_REGISTER")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class InboundRegister extends PersistableEntity {

	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String serialNo; // 入库登记单号;
	@NotNull
	@ManyToOne
	private Customer customer; // 客户;
	@ManyToOne(fetch = FetchType.LAZY)
	private VehicleType vehicleType;// 来车类型
	@Basic
	private int vehicleAmount = 1; // 来车台数
	@Column(length = 100)
	private String vehicleNumbers;// 车牌号
	@Temporal(TemporalType.TIMESTAMP)
	private Date registerTime;// 登记时间
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator registerOperator; // 登记入

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inboundRegister", cascade = CascadeType.REMOVE)
	private Set<InboundRegisterItem> inboundRegisterItems; // 登记入库商品明细

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private InboundBooking inboundBooking; // 关联预约单
	@Temporal(TemporalType.TIMESTAMP)
	private Date inboundTime;
 
 
    @JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TJ_INBOUND_REG_LOCATION_PRE", joinColumns = { @JoinColumn(name = "inboundRegister") }, inverseJoinColumns = { @JoinColumn(name = "storeLocation") })
	private Set<StoreLocation> preStoreLocations;// 预分派储位

	@Basic
	private InboundType inboundType;// 入库类型
	@Basic
	private StockInStatus stockInStatus;// 整体入库状态

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inboundRegister", cascade = CascadeType.REMOVE)
	private Set<InboundReceiptItem> inboundReceiptItems; // 收货商品明细

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inboundRegister", cascade = CascadeType.REMOVE)
	private Set<StockIn> stockIns; // 上架明细

	// TODO 结算单
    @JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	private Payment payment; // 缴费单

	@Basic
	private BigDecimal guaranteeMoney;// 押金

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inboundRegister")
	private Set<InboundTarry> inboundTarrys;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public int getVehicleAmount() {
		return vehicleAmount;
	}

	public void setVehicleAmount(int vehicleAmount) {
		this.vehicleAmount = vehicleAmount;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Operator getRegisterOperator() {
		return registerOperator;
	}

	public void setRegisterOperator(Operator confirmOperator) {
		this.registerOperator = confirmOperator;
	}

	public InboundBooking getInboundBooking() {
		return inboundBooking;
	}

	public void setInboundBooking(InboundBooking inboundBooking) {
		this.inboundBooking = inboundBooking;
	}

	public Set<InboundRegisterItem> getInboundRegisterItems() {
		return inboundRegisterItems;
	}

	public void setInboundRegisterItems(Set<InboundRegisterItem> inboundRegisterItems) {
		this.inboundRegisterItems = inboundRegisterItems;
	}

	public Set<StoreLocation> getPreStoreLocations() {
		return preStoreLocations;
	}
	
	public String getPreStoreLocationsCode() {
		if (preStoreLocations != null&&!preStoreLocations.isEmpty()) {
			StringBuffer str = new StringBuffer();
			for (StoreLocation storeLocation : preStoreLocations) {
					str.append(storeLocation.getCode() + ",");
			}
			return str.toString();
		}
		return "";
 
	}
	
	public void setPreStoreLocations(Set<StoreLocation> storeLocations) {
		this.preStoreLocations = storeLocations;
	}

	public Date getInboundTime() {
		return inboundTime;
	}

	public void setInboundTime(Date inboundTime) {
		this.inboundTime = inboundTime;
	}

	public String getVehicleNumbers() {
		return vehicleNumbers;
	}

	public void setVehicleNumbers(String vehicleNumbers) {
		this.vehicleNumbers = vehicleNumbers;
	}

	public InboundType getInboundType() {
		return inboundType;
	}

	public void setInboundType(InboundType inboundType) {
		this.inboundType = inboundType;
	}

	public String getCustomerName() {
		return this.customer.getName();
	}

	public String getVehicleTypeName() {
		if (this.vehicleType != null) {
			return this.vehicleType.getName();
		} else {
			return null;
		}

	}

	public StockInStatus getStockInStatus() {
		return stockInStatus;
	}

	public void setStockInStatus(StockInStatus stockInStatus) {
		this.stockInStatus = stockInStatus;
	}

	public Set<InboundReceiptItem> getInboundReceiptItems() {
		return inboundReceiptItems;
	}

	public void setInboundReceiptItems(Set<InboundReceiptItem> inboundReceiptItems) {
		this.inboundReceiptItems = inboundReceiptItems;
	}

	public Set<StockIn> getStockIns() {
		return stockIns;
	}

	public void setStockIns(Set<StockIn> stockIns) {
		this.stockIns = stockIns;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public BigDecimal getGuaranteeMoney() {
		return guaranteeMoney;
	}

	public void setGuaranteeMoney(BigDecimal guaranteeMoney) {
		this.guaranteeMoney = guaranteeMoney;
	}

	public Set<InboundTarry> getInboundTarrys() {
		return inboundTarrys;
	}

	public void setInboundTarrys(Set<InboundTarry> inboundTarrys) {
		this.inboundTarrys = inboundTarrys;
	}
	
}
