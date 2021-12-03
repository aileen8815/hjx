package com.wwyl.entity.store;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.BookingStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.MeasureUnit;
import com.wwyl.entity.settings.Packing;
import com.wwyl.entity.settings.Product;

/**
 * 入库预约商品明细
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_INBOUND_BOOKING_ITEM")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class InboundBookingItem extends PersistableEntity {

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private InboundBooking inboundBooking;// 预约单
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
	
	@Column(length = 255)
	private String productionPlace; // 产地
	@ManyToOne(fetch = FetchType.LAZY)
	private Packing packing; // 包装
	@Basic
	private int qualityGuaranteePeriod; // 保质期
	@Basic
	private Date productionDate; // 生产日期
	
	@Basic
	private int storeDuration;// 预期保管时间
	@Basic
	private BookingStatus bookingStatus;// 预约状态
	
	@Basic
	private int storeContainerCount;// 预分派托盘数
	public InboundBooking getInboundBooking() {
		return inboundBooking;
	}

	public void setInboundBooking(InboundBooking inboundBooking) {
		this.inboundBooking = inboundBooking;
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

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public int getQualityGuaranteePeriod() {
		return qualityGuaranteePeriod;
	}

	public void setQualityGuaranteePeriod(int qualityGuaranteePeriod) {
		this.qualityGuaranteePeriod = qualityGuaranteePeriod;
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

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	public String  getProductName(){
		return this.product.getName();
	}
	public Long  getProductId(){
		return this.product!=null?this.product.getId():null;
	}
 
	public String getWeightMeasureUnitName(){
		return this.weightMeasureUnit.getName();
	}
	public String getAmountMeasureUnitName(){
		return this.amountMeasureUnit.getName();
	}
	
	public int getStoreContainerCount() {
		return storeContainerCount;
	}

	public void setStoreContainerCount(int storeContainerCount) {
		this.storeContainerCount = storeContainerCount;
	}
	
	public String getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(String productionPlace) {
		this.productionPlace = productionPlace;
	}

	public InboundBookingItem cloneInboundBookingItem(){
		InboundBookingItem inboundBookingItemNew=new InboundBookingItem();
		inboundBookingItemNew.setAmount(getAmount());
		inboundBookingItemNew.setAmountMeasureUnit(getAmountMeasureUnit());
		inboundBookingItemNew.setBookingStatus(getBookingStatus());
		inboundBookingItemNew.setPacking(getPacking());
		inboundBookingItemNew.setProduct(getProduct());
		inboundBookingItemNew.setProductionDate(getProductionDate());
		inboundBookingItemNew.setQualityGuaranteePeriod(getQualityGuaranteePeriod());
		inboundBookingItemNew.setSpec(getSpec());
		inboundBookingItemNew.setProductionPlace(getProductionPlace());
		inboundBookingItemNew.setStoreContainerCount(getStoreContainerCount());
		inboundBookingItemNew.setStoreDuration(getStoreDuration());
		inboundBookingItemNew.setWeight(getWeight());
		inboundBookingItemNew.setWeightMeasureUnit(getWeightMeasureUnit());
		return inboundBookingItemNew;
	}
}
