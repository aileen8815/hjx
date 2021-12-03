package com.wwyl.entity.store;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.MeasureUnit;
import com.wwyl.entity.settings.Packing;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.settings.TallyArea;

/**
 * 出库检验明细
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_OUTBOUND_CHECK_ITEM")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class OutboundCheckItem extends PersistableEntity {

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private OutboundRegister outboundRegister;// 出库登记单
	@NotNull
	@ManyToOne
	private Product product;// 货品
	@Basic
	private double weight;// 重量
	@ManyToOne
	private MeasureUnit weightMeasureUnit;// 重量计量单位
	@Basic
	private double amount;// 数量
	@ManyToOne
	private MeasureUnit amountMeasureUnit;// 数量计量单位
	@Column(length = 255)
	private String spec; // 规格
	@ManyToOne(fetch = FetchType.LAZY)
	private Packing packing; // 包装
	@Column(length = 255)
	private String productionPlace; // 产地
	@ManyToOne(fetch = FetchType.LAZY)
	private StoreContainer storeContainer; // 托盘
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator checkOperator; // 清点人
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkTime; // 清点时间
	@Transient
	private double freighAmountt;// 发车货品数量，只用作发车记录数量
	
	//@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private TallyArea tarryArea;// 理货区

	@Temporal(TemporalType.TIMESTAMP)
	private Date stockInTime; // 上架时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date settledTime; // 结算时间
	@ManyToOne(fetch = FetchType.LAZY)
	private StoreLocation storeLocation; // 拣货的储位
	@Basic
	private BigDecimal storageChargesAmount;//仓储费用（元）

	@Column(length = 255)
	private String readCode; // 扫码号
	
	public TallyArea getTarryArea() {
		return tarryArea;
	}

	public void setTarryArea(TallyArea tarryArea) {
		this.tarryArea = tarryArea;
	}

	public OutboundRegister getOutboundRegister() {
		return outboundRegister;
	}

	public void setOutboundRegister(OutboundRegister outboundRegister) {
		this.outboundRegister = outboundRegister;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public String getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(String productionPlace) {
		this.productionPlace = productionPlace;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public MeasureUnit getWeightMeasureUnit() {
		return weightMeasureUnit;
	}

	public void setWeightMeasureUnit(MeasureUnit weightMeasureUnit) {
		this.weightMeasureUnit = weightMeasureUnit;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public MeasureUnit getAmountMeasureUnit() {
		return amountMeasureUnit;
	}

	public void setAmountMeasureUnit(MeasureUnit amountMeasureUnit) {
		this.amountMeasureUnit = amountMeasureUnit;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Packing getPacking() {
		return packing;
	}

	public void setPacking(Packing packing) {
		this.packing = packing;
	}

	public StoreContainer getStoreContainer() {
		return storeContainer;
	}

	public void setStoreContainer(StoreContainer storeContainer) {
		this.storeContainer = storeContainer;
	}

	public Operator getCheckOperator() {
		return checkOperator;
	}

	public void setCheckOperator(Operator checkOperator) {
		this.checkOperator = checkOperator;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public double getFreighAmountt() {
		return freighAmountt;
	}

	public void setFreighAmountt(double freighAmountt) {
		this.freighAmountt = freighAmountt;
	}

	public String getProductName() {
		return this.product.getName();
	}

	public String getProductPriceRange() {
		return this.product.getPriceRange();
	}

	public String getWeightMeasureUnitName() {
		return this.weightMeasureUnit.getName();
	}

	public String getAmountMeasureUnitName() {
		return this.amountMeasureUnit.getName();
	}

	public String getProductRemark() {
		return this.product.getRemark();
	}

	public Long getProductId() {
		return product != null ? this.product.getId() : null;
	}

	public Date getStockInTime() {
		return stockInTime;
	}

	public void setStockInTime(Date stockInTime) {
		this.stockInTime = stockInTime;
	}

	public Date getSettledTime() {
		return settledTime;
	}

	public void setSettledTime(Date settledTime) {
		this.settledTime = settledTime;
	}

	public StoreLocation getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(StoreLocation storeLocation) {
		this.storeLocation = storeLocation;
	}

	public BigDecimal getStorageChargesAmount() {
		return storageChargesAmount;
	}

	public void setStorageChargesAmount(BigDecimal storageChargesAmount) {
		this.storageChargesAmount = storageChargesAmount;
	}

	public String getReadCode() {
		return readCode;
	}

	public void setReadCode(String readCode) {
		this.readCode = readCode;
	}
	
}
