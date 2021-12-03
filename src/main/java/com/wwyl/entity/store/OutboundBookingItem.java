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

/**
 * 出库预约商品明细
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_OUTBOUND_BOOKING_ITEM")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class OutboundBookingItem extends PersistableEntity {

	@NotNull
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private OutboundBooking outboundBooking;// 预约单
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
	@Basic
	private int storeContainerCount;// 预计托盘数
	@Column(length = 255)
	private String batchs; // 批次
	public StoreContainer getStoreContainer() {
		return storeContainer;
	}

	public void setStoreContainer(StoreContainer storeContainer) {
		this.storeContainer = storeContainer;
	}

	public OutboundBooking getOutboundBooking() {
		return outboundBooking;
	}

	public void setOutboundBooking(OutboundBooking outboundBooking) {
		this.outboundBooking = outboundBooking;
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
	
	public String getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(String productionPlace) {
		this.productionPlace = productionPlace;
	}

	public String getProductName(){
		return this.product.getName();
	}
	public String getWeightMeasureUnitName(){
		return this.weightMeasureUnit.getName();
	}
	public String getAmountMeasureUnitName(){
		return this.amountMeasureUnit.getName();
	}
 
	/**
	 * 该方法是获取同一个批次同一种货品的
	 * 
	 * @return
	 */
	public String getBatchProduct() {
		return this.batchs+"_" +this.product.getId() ;
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

	public OutboundBookingItem cloneOutboundBookingItem(){
		OutboundBookingItem outboundBookingItemNew=new OutboundBookingItem();
		outboundBookingItemNew.setAmount(getAmount());
		outboundBookingItemNew.setAmountMeasureUnit(getAmountMeasureUnit());
		outboundBookingItemNew.setPacking(getPacking());
		outboundBookingItemNew.setProduct(getProduct());
		outboundBookingItemNew.setSpec(getSpec());
		outboundBookingItemNew.setProductionPlace(getProductionPlace());
		outboundBookingItemNew.setWeight(getWeight());
		outboundBookingItemNew.setWeightMeasureUnit(getWeightMeasureUnit());
		outboundBookingItemNew.setBatchs(this.batchs);
		return outboundBookingItemNew;
	}
}
