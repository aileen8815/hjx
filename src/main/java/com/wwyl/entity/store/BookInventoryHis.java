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
 * 在册库存历史状态
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_BOOK_INVENTORY_HIS")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class BookInventoryHis extends PersistableEntity {

	@NotNull
	@ManyToOne
	@JsonIgnore
	private Customer customer; // 客户
	@NotNull
	@ManyToOne
	@JsonIgnore
	private Product product;// 货品
	@Basic
	private double weight;// 重量
	@ManyToOne
	@JsonIgnore
	private MeasureUnit weightMeasureUnit;// 重量计量单位
	@Basic
	private double amount;// 数量
	@ManyToOne
	@JsonIgnore
	private MeasureUnit amountMeasureUnit;// 数量计量单位
	@Column(length = 255)
	private String spec; // 规格
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Packing packing; // 包装
	@Basic
	private int quanlityGuaranteePeriod; // 保质期
	@Basic
	private int storeDuration;// 预期保管时间
	@Basic
	private boolean qualified;// 是否合格

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private StoreLocation storeLocation; // 储位
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private StoreContainer storeContainer; // 托盘
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Operator stockInOperator; // 上架操作员
	@Temporal(TemporalType.TIMESTAMP)
	private Date stockInTime; // 上架时间

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private StockIn stockIn; // 关联上架单
	@ManyToOne
	// FIXME :商品状态1,正常
	@JsonIgnore
	private ProductStatus productStatus;// 商品状态
	@Column(length = 50)
	public String inboundRegisterSerialNo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date productionDate; // 生产日期
	@Column(length = 255)
	private String productionPlace; // 产地
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date activeTime; // 生效时间
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date inactiveTime; // 失效时间
	
	@Column(length = 255)
	private String productDetail; // 多品明细
	
	@Column(length = 255)
	private String readCode; // 抄码
	
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

	public boolean isQualified() {
		return qualified;
	}

	public void setQualified(boolean qualified) {
		this.qualified = qualified;
	}

	public String getCustomeName() {
		return this.customer.getName();
	}

	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public Date getInactiveTime() {
		return inactiveTime;
	}

	public void setInactiveTime(Date inactiveTime) {
		this.inactiveTime = inactiveTime;
	}

	public String getInboundRegisterSerialNo() {
		return inboundRegisterSerialNo;
	}

	public void setInboundRegisterSerialNo(String inboundRegisterSerialNo) {
		this.inboundRegisterSerialNo = inboundRegisterSerialNo;
	}

	public String getProductName() {
		return this.product.getName();
	}

	public String getProductPriceRange() {
		return this.product.getPriceRange();
	}

	public String getWeightMeasureUnitName() {
		return this.weightMeasureUnit == null ? "" : this.getWeightMeasureUnit().getName();
	}

	public String getAmountMeasureUnitName() {
		return this.amountMeasureUnit == null ? "" : this.getAmountMeasureUnit().getName();
	}

	public String getPackingName() {
		return this.getPacking() == null ? "" : this.getPacking().getName();
	}

	public String getStoreLocationCode() {
		return this.getStoreLocation() == null ? "" : this.getStoreLocation().getCode();
	}
	
	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	/**
	 * 该方法是获取同一个托盘同一种货品的
	 * 
	 * @return
	 */
	public String getStoreProductID() {
		return this.product.getId() + "-" + this.storeContainer.getId();
	}
	public Long getProductId() {
		return this.product.getId();
	}
	
	public String getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(String productionPlace) {
		this.productionPlace = productionPlace;
	}

	public String getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}

	public String getReadCode() {
		return readCode;
	}

	public void setReadCode(String readCode) {
		this.readCode = readCode;
	}

	public void createFromBookInventory(BookInventory bookInventory) {
		this.setStoreLocation(bookInventory.getStoreLocation());
		this.setAmount(bookInventory.getAmount());
		this.setAmountMeasureUnit(bookInventory.getAmountMeasureUnit());
		this.setPacking(bookInventory.getPacking());
		this.setProduct(bookInventory.getProduct());
		this.setSpec(bookInventory.getSpec());
		this.setProductionPlace(bookInventory.getProductionPlace());
		this.setWeight(bookInventory.getWeight());
		this.setWeightMeasureUnit(bookInventory.getWeightMeasureUnit());
		this.setStoreContainer(bookInventory.getStoreContainer());
		this.setStockInTime(bookInventory.getStockInTime());
		this.setCustomer(bookInventory.getCustomer());
		this.setInboundRegisterSerialNo(bookInventory.getInboundRegisterSerialNo());
		this.setStockInOperator(bookInventory.getStockInOperator());
		this.setProductionDate(bookInventory.getProductionDate());
		this.setStockIn(bookInventory.getStockIn());
		this.setStoreDuration(bookInventory.getStoreDuration());
		this.setProductStatus(bookInventory.getProductStatus());
		this.setProductDetail(bookInventory.getProductDetail());
		this.setQualified(bookInventory.isQualified());
		this.setQuanlityGuaranteePeriod(bookInventory.getQuanlityGuaranteePeriod());
		this.setReadCode(bookInventory.getReadCode());
	}
}
