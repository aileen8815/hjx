package com.wwyl.entity.store;

import java.text.SimpleDateFormat;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.MeasureUnit;
import com.wwyl.entity.settings.Packing;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;

/**
 * 出库登记商品明细
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_OUTBOUND_REGISTER_ITEM")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class OutboundRegisterItem extends PersistableEntity {

	@NotNull
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private OutboundRegister outboundRegister;// 出库登记单
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;// 货品
	@Basic
	private double weight;// 重量
	@ManyToOne(fetch = FetchType.LAZY)
	private MeasureUnit weightMeasureUnit;// 重量计量单位
	@Basic
	private double amount;// 数量
	@ManyToOne(fetch = FetchType.LAZY)
	private MeasureUnit amountMeasureUnit;// 数量计量单位
	@Column(length = 255)
	private String spec; // 规格
	@ManyToOne(fetch = FetchType.LAZY)
	private Packing packing; // 包装
	@ManyToOne(fetch = FetchType.LAZY)
	private StoreContainer storeContainer; // 托盘
	@ManyToOne(fetch = FetchType.LAZY)
	private StoreLocation storeLocation;
	@Column(length = 255)
	private String productionPlace; // 产地
	@Basic
	@Column(columnDefinition = "float DEFAULT 0")
	private int storeContainerCount;// 预计托盘数
	@Column(length = 255)
	private String batchs; // 批次
	@Column(length = 255)
	private String productDetail; // 多品明细
	
	public StoreContainer getStoreContainer() {
		return storeContainer;
	}

	public void setStoreContainer(StoreContainer storeContainer) {
		this.storeContainer = storeContainer;
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

	public Packing getPacking() {
		return packing;
	}

	public void setPacking(Packing packing) {
		this.packing = packing;
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

	public StoreLocation getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(StoreLocation storeLocation) {
		this.storeLocation = storeLocation;
	}
	
	public int getStoreContainerCount() {
		return storeContainerCount;
	}

	public void setStoreContainerCount(int storeContainerCount) {
		this.storeContainerCount = storeContainerCount;
	}
	
	public String getBatchs() {
		return batchs;
	}

	public void setBatchs(String batchs) {
		this.batchs = batchs;
	}

	public String getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}

	/**
	 * 该方法是获取同一个批次同一种货品的
	 * 
	 * @return
	 */
	public String getBatchProduct() {
		return this.batchs+"_" +this.product.getId() ;
	}
}
