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
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.StockRelocationStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.MeasureUnit;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;

/**
 * 库内移位
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_STOCK_RELOCATION")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class StockRelocation extends PersistableEntity {
	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String serialNo; // 单号;
	@NotNull
	@ManyToOne
	private Customer customer; // 客户;
    @Temporal(TemporalType.TIMESTAMP)
    private Date operatorTime;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator operator;

    @ManyToOne(fetch = FetchType.LAZY)
    private StoreLocation fromStoreLocation; // 储位
 
    @ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
    private StoreLocation toStoreLocation; // 储位

    @ManyToOne(fetch = FetchType.LAZY)
    private StoreContainer storeContainer; // 托盘
    
    @ManyToOne
    private Product product; // 商品
    @Basic
    @Column(columnDefinition="numeric(12,3) default 0")
    private double weight;// 重量
    
    @ManyToOne
	private MeasureUnit weightMeasureUnit;// 重量计量单位
    @Basic
    @Column(columnDefinition="numeric(8,0) default 0")
    private double amount;// 件数
    
    @ManyToOne
	private MeasureUnit amountMeasureUnit;// 数量计量单位
    
    @Basic
	private StockRelocationStatus stockRelocationStatus; //状态
    
    @NotBlank
	@Column(length = 100, nullable = false, unique = true)
	private String guid; // GUID唯一标识;
    	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public Customer getCustomer() {
		return customer;
	}
	public String  getCustomerName() {
		return customer!=null?customer.getName():null;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Date getOperatorTime() {
		return operatorTime;
	}
	public void setOperatorTime(Date operatorTime) {
		this.operatorTime = operatorTime;
	}
	public String getOperatorName() {
		return operator!=null?operator.getName():null;
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	public StoreLocation getFromStoreLocation() {
		return fromStoreLocation;
	}
	public void setFromStoreLocation(StoreLocation fromStoreLocation) {
		this.fromStoreLocation = fromStoreLocation;
	}
	public StoreLocation getToStoreLocation() {
		return toStoreLocation;
	}
	public void setToStoreLocation(StoreLocation toStoreLocation) {
		this.toStoreLocation = toStoreLocation;
	}
	public StoreContainer getStoreContainer() {
		return storeContainer;
	}
	public void setStoreContainer(StoreContainer storeContainer) {
		this.storeContainer = storeContainer;
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
	public StockRelocationStatus getStockRelocationStatus() {
		return stockRelocationStatus;
	}
	public void setStockRelocationStatus(StockRelocationStatus stockRelocationStatus) {
		this.stockRelocationStatus = stockRelocationStatus;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
}
