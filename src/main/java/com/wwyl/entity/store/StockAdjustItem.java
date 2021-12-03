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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.MeasureUnit;
import com.wwyl.entity.settings.Packing;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.ProductStatus;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;

/**
 * 库存调整明细
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_STOCK_ADJUST_ITEM")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class StockAdjustItem extends PersistableEntity {


	@ManyToOne
	private Customer customer; // 客户
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
	private int quanlityGuaranteePeriod; // 保质期
	@Temporal(TemporalType.TIMESTAMP)
	private Date productionDate; // 生产日期
	@Basic
	private int storeDuration;// TODO: 预期保管时间
	@Basic
	private boolean qualified;// 是否合格

	@ManyToOne(fetch = FetchType.LAZY)
	private StoreLocation storeLocation; // 储位
	@ManyToOne(fetch = FetchType.LAZY)
	private StoreContainer storeContainer; // 托盘
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator stockInOperator; // 上架操作员
	@Temporal(TemporalType.TIMESTAMP)
	private Date stockInTime; // 上架时间
	@Column(length = 255)
	private String productionPlace; // 产地
	@ManyToOne(fetch = FetchType.LAZY)
	private StockIn stockIn; // 关联上架单
	@ManyToOne(fetch = FetchType.LAZY)
	private ProductStatus productStatus;// 商品状态
 
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private BookInventoryHis bookInventoryHis;//调整前的库存状态
	@Basic
	private boolean empty;
	
	public BookInventoryHis getBookInventoryHis() {
		return bookInventoryHis;
	}

	public void setBookInventoryHis(BookInventoryHis bookInventoryHis) {
		this.bookInventoryHis = bookInventoryHis;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
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
	
	public String getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(String productionPlace) {
		this.productionPlace = productionPlace;
	}

	public Packing getPacking() {
		return packing;
	}

	public void setPacking(Packing packing) {
		this.packing = packing;
	}

	public int getQuanlityGuaranteePeriod() {
		return quanlityGuaranteePeriod;
	}

	public void setQuanlityGuaranteePeriod(int quanlityGuaranteePeriod) {
		this.quanlityGuaranteePeriod = quanlityGuaranteePeriod;
	}

	public int getStoreDuration() {
		return storeDuration;
	}

	public void setStoreDuration(int storeDuration) {
		this.storeDuration = storeDuration;
	}

	public boolean isQualified() {
		return qualified;
	}

	public void setQualified(boolean qualified) {
		this.qualified = qualified;
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

	public Operator getStockInOperator() {
		return stockInOperator;
	}

	public void setStockInOperator(Operator stockInOperator) {
		this.stockInOperator = stockInOperator;
	}

	public Date getStockInTime() {
		return stockInTime;
	}

	public void setStockInTime(Date stockInTime) {
		this.stockInTime = stockInTime;
	}

	public StockIn getStockIn() {
		return stockIn;
	}

	public void setStockIn(StockIn stockIn) {
		this.stockIn = stockIn;
	}

	public ProductStatus getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ProductStatus productStatus) {
		this.productStatus = productStatus;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	public  String getStoreLocationCode(){
		return this.storeLocation.getCode();
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
	public void asInventory(BookInventory bookInventory) {
		this.setCustomer(bookInventory.getCustomer());
		this.setAmount(bookInventory.getAmount());
		this.setAmountMeasureUnit(bookInventory.getAmountMeasureUnit());
		this.setPacking(bookInventory.getPacking());
		this.setProduct(bookInventory.getProduct());
		this.setProductStatus(bookInventory.getProductStatus());
		this.setQualified(bookInventory.isQualified());
		this.setQuanlityGuaranteePeriod(bookInventory.getQuanlityGuaranteePeriod());
		this.setSpec(bookInventory.getSpec());
		this.setStockIn(bookInventory.getStockIn());
		this.setStockInOperator(bookInventory.getStockInOperator());
		this.setStockInTime(bookInventory.getStockInTime());
		this.setStoreContainer(bookInventory.getStoreContainer());
		this.setStoreDuration(bookInventory.getStoreDuration());
		this.setStoreLocation(bookInventory.getStoreLocation());
		this.setWeight(bookInventory.getWeight());
		this.setWeightMeasureUnit(bookInventory.getWeightMeasureUnit());
		this.setProductionDate(bookInventory.getProductionDate());
	}
	
}
