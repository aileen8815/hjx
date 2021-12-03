package com.wwyl.entity.store;

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
import com.wwyl.entity.settings.MeasureUnit;
import com.wwyl.entity.settings.Packing;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.StoreArea;

/**
 * 入库登记商品明细
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_INBOUND_REGISTER_ITEM")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class InboundRegisterItem extends PersistableEntity {

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private InboundRegister inboundRegister;// 登记单
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
	@Basic
	private int qualityGuaranteePeriod; // 保质期
	@Basic
	private int storeDuration;// 预期保管时间
	@Column(length = 255)
	private String productionPlace; // 产地
	@Temporal(TemporalType.TIMESTAMP)
	private Date productionDate; // 生产日期
	@Basic
	@Column(columnDefinition = "float DEFAULT 0")
	private int storeContainerCount;// 预计托盘数
	
	@ManyToOne
	private StoreArea storeArea; // 预约仓间
	
	@Transient
	public double checkamount;//已清点数量
	@Transient
	private double checkWeight;//已清点重量
	@Transient
	public double checkContainerCountAmount;//已验托盘数 
	@Column(length = 255)
	private String productDetail; // 多品明细
	
	public InboundRegister getInboundRegister() {
		return inboundRegister;
	}

	public void setInboundRegister(InboundRegister inboundBooking) {
		this.inboundRegister = inboundBooking;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public String getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(String productionPlace) {
		this.productionPlace = productionPlace;
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

	public int getQualityGuaranteePeriod() {
		return qualityGuaranteePeriod;
	}

	public void setQualityGuaranteePeriod(int quanlityGuaranteePeriod) {
		this.qualityGuaranteePeriod = quanlityGuaranteePeriod;
	}

	public Packing getPacking() {
		return packing;
	}

	public void setPacking(Packing packing) {
		this.packing = packing;
	}

	public int getStoreDuration() {
		return storeDuration;
	}

	public void setStoreDuration(int storeDuration) {
		this.storeDuration = storeDuration;
	}

	public String getProductName() {
		return this.product.getName();
	}

	public String getWeightMeasureUnitName() {
		return this.weightMeasureUnit.getName();
	}

	public String getAmountMeasureUnitName() {
		return this.amountMeasureUnit.getName();
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	
	public double getCheckamount() {
		return checkamount;
	}

	public void setCheckamount(double checkamount) {
		this.checkamount = checkamount;
	}

	public double getCheckWeight() {
		return checkWeight;
	}

	public void setCheckWeight(double checkWeight) {
		this.checkWeight = checkWeight;
	}

	public int getStoreContainerCount() {
		return storeContainerCount;
	}

	public void setStoreContainerCount(int storeContainerCount) {
		this.storeContainerCount = storeContainerCount;
	}

	public double getCheckContainerCountAmount() {
		return checkContainerCountAmount;
	}

	public void setCheckContainerCountAmount(double checkContainerCountAmount) {
		this.checkContainerCountAmount = checkContainerCountAmount;
	}

	public StoreArea getStoreArea() {
		return storeArea;
	}

	public void setStoreArea(StoreArea storeArea) {
		this.storeArea = storeArea;
	}

	public String getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}
	
}
