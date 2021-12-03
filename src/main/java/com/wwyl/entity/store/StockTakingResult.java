package com.wwyl.entity.store;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.StockTakeResultStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.MeasureUnit;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;

/**
 * 盘点结果
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_STOCK_TAKING_RESULT")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "new"})
public class StockTakingResult extends PersistableEntity {
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private StockTaking stockTaking;
	
    @ManyToOne
    private Product product;// 货品
    @Basic
    private double amount;// 数量
    @Basic
    private double stockTakingAmount;// 盘点数量
    
    @ManyToOne
    private MeasureUnit amountMeasureUnit;// 数量计量单位
    
	@Basic
	private double weight;// 重量
	@Basic
	private double stockTakingWeight;// 盘点重量
	@ManyToOne
	private MeasureUnit weightMeasureUnit;// 重量计量单位
	

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;		//客户
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreLocation storeLocation; // 储位
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreContainer storeContainer; // 托盘
    
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreLocation toStoreLocation; // 移动托盘的目标储位
    
    @Basic
	private StockTakeResultStatus stockTakeResultStatus;// 盘点状态
    
	public StockTaking getStockTaking() {
		return stockTaking;
	}
	public void setStockTaking(StockTaking stockTaking) {
		this.stockTaking = stockTaking;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getStockTakingAmount() {
		return stockTakingAmount;
	}
	public void setStockTakingAmount(double stockTakingAmount) {
		this.stockTakingAmount = stockTakingAmount;
	}
	public MeasureUnit getAmountMeasureUnit() {
		return amountMeasureUnit;
	}
	public void setAmountMeasureUnit(MeasureUnit amountMeasureUnit) {
		this.amountMeasureUnit = amountMeasureUnit;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getStockTakingWeight() {
		return stockTakingWeight;
	}
	public void setStockTakingWeight(double stockTakingWeight) {
		this.stockTakingWeight = stockTakingWeight;
	}
	public MeasureUnit getWeightMeasureUnit() {
		return weightMeasureUnit;
	}
	public void setWeightMeasureUnit(MeasureUnit weightMeasureUnit) {
		this.weightMeasureUnit = weightMeasureUnit;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public StoreLocation getStoreLocation() {
		return storeLocation;
	}
	public void setStoreLocation(StoreLocation storeLocation) {
		this.storeLocation = storeLocation;
	}
	public StoreContainer getStoreContainer() {
		return storeContainer;
	}
	public void setStoreContainer(StoreContainer storeContainer) {
		this.storeContainer = storeContainer;
	}
	public StoreLocation getToStoreLocation() {
		return toStoreLocation;
	}
	public void setToStoreLocation(StoreLocation toStoreLocation) {
		this.toStoreLocation = toStoreLocation;
	}
	
	public StockTakeResultStatus getStockTakeResultStatus() {
		return stockTakeResultStatus;
	}
	public void setStockTakeResultStatus(StockTakeResultStatus stockTakeResultStatus) {
		this.stockTakeResultStatus = stockTakeResultStatus;
	}
	public void asStockTakingItem(StockTakingItem stockTakingItem){
		setAmount(stockTakingItem.getAmount());
		setAmountMeasureUnit(stockTakingItem.getAmountMeasureUnit());
		setStockTakingAmount(stockTakingItem.getAmount());
		setWeight(stockTakingItem.getWeight());
		setWeightMeasureUnit(stockTakingItem.getWeightMeasureUnit());
		setStockTakingWeight(stockTakingItem.getWeight());
		setStoreLocation(stockTakingItem.getStoreLocation());
		setStoreContainer(stockTakingItem.getStoreContainer());
		setStockTaking(stockTakingItem.getStockTaking());
		setCustomer(stockTakingItem.getCustomer());
		setProduct(stockTakingItem.getProduct());
    }
    
	
}
