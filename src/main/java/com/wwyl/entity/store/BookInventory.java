package com.wwyl.entity.store;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.MeasureUnit;
import com.wwyl.entity.settings.Packing;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.ProductStatus;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;

/**
 * 在册库存
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_BOOK_INVENTORY")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class BookInventory extends PersistableEntity {

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
	private StoreLocation storeLocation; // 储位
	@ManyToOne(fetch = FetchType.LAZY)
	private StoreContainer storeContainer; // 托盘
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Operator stockInOperator; // 上架操作员
	@Temporal(TemporalType.TIMESTAMP)
	private Date stockInTime; // 上架时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date settledTime;// 结算时间
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
	@Transient
	public double outboundAmount;//当前出库数量，仅用做出库预约和登记,货权转移
	@Transient
	private int storeContainerCount;//用作出库预计托盘数
	@Transient
	private int totalStoreContainer;//总托数	
	@Column(length = 255)
	private String productDetail; // 多品明细
	
	@Column(length = 255)
	private String readCode; // 抄码
	
	
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JsonIgnore
	// private InboundReceipt inboundReceipt;

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
	public String  getStoreContainerLabel() {
		return storeContainer!=null?storeContainer.getLabel():"";
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
	
	public Date getSettledTime() {
		return settledTime;
	}

	public void setSettledTime(Date settledTime) {
		this.settledTime = settledTime;
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

	// public InboundReceipt getInboundReceipt() {
	// return inboundReceipt;
	// }
	//
	// public void setInboundReceipt(InboundReceipt inboundReceipt) {
	// this.inboundReceipt = inboundReceipt;
	// }
	//
	// public String getInboundReceiptSerialNo() {
	// if (this.inboundReceipt != null) {
	// return inboundReceipt.getSerialNo();
	// }
	// return null;
	//
	// }

	public String getCustomeName() {
		return this.customer.getName();
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
	public double getBearingCapacity() {
		return this.product.getBearingCapacity();
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
	/**
	 * 该方法是获取托盘id
	 * 
	 * @return
	 */
	public String getStoreContainerId() {
		return  this.storeContainer.getId().toString();
	}
	public Long getProductId() {
		return this.product.getId();
	}
	/**
	 * 当前出库数量，仅用做出库预约和登记
	 * @return
	 */
	public double getOutboundAmount() {
		return outboundAmount;
	}
	/**
	 * 当前出库数量，仅用做出库预约和登记
	 * @param outOutboundAmount
	 */
	public void setOutboundAmount(double outboundAmount) {
		this.outboundAmount = outboundAmount;
	}

	public int getStoreContainerCount() {
		return storeContainerCount;
	}

	public void setStoreContainerCount(int storeContainerCount) {
		this.storeContainerCount = storeContainerCount;
	}
	
	public int getTotalStoreContainer() {
		return totalStoreContainer;
	}

	public void setTotalStoreContainer(int totalStoreContainer) {
		this.totalStoreContainer = totalStoreContainer;
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

	/**
	 * 该方法是获取同一个批次同一种货品的
	 * 
	 * @return
	 */
	public String getBatchProduct() {
		String batchs="";
		if(StringUtils.isNotBlank(this.inboundRegisterSerialNo)){
			batchs=this.inboundRegisterSerialNo;
		}
		return batchs+"_" +this.product.getId() ;
	}
	
}
