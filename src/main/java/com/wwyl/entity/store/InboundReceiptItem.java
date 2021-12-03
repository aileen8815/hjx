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
import com.wwyl.entity.settings.*;

/**
 * 入库收货明细
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_INBOUND_RECEIPT_ITEM")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class InboundReceiptItem extends PersistableEntity {

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
	@Basic
	private boolean qualified;// 是否合格
	@ManyToOne(fetch = FetchType.LAZY)
	private StoreContainer storeContainer; // 托盘
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator receiptor; // 收货人
	@Temporal(TemporalType.TIMESTAMP)
	private Date receiptTime; // 收货时间

	@ManyToOne(fetch = FetchType.LAZY)
	private TallyArea tarryArea;// 理货区

	@Temporal(TemporalType.TIMESTAMP)
	private Date productionDate; // 生产日期
	@Column(length = 255)
	private String productionPlace; // 产地
	
	@ManyToOne
	private StoreArea storeArea; // 登记仓间
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private InboundRegister inboundRegister;// 入库收货单
	
	@Column(length = 255)
	private String productDetail; // 多品明细
	
	@Column(length = 255)
	private String readCode; // 扫码号

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

	public int getQualityGuaranteePeriod() {
		return qualityGuaranteePeriod;
	}

	public void setQualityGuaranteePeriod(int quanlityGuaranteePeriod) {
		this.qualityGuaranteePeriod = quanlityGuaranteePeriod;
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

	public StoreContainer getStoreContainer() {
		return storeContainer;
	}

	public void setStoreContainer(StoreContainer storeContainer) {
		this.storeContainer = storeContainer;
	}

	public Operator getReceiptor() {
		return receiptor;
	}

	public void setReceiptor(Operator receiptor) {
		this.receiptor = receiptor;
	}

	public Date getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}

	public String getWeightMeasureUnitName() {
		return this.weightMeasureUnit.getName();
	}

	public String getAmountMeasureUnitName() {
		return this.amountMeasureUnit.getName();
	}

	public String getProductName() {
		return this.product.getName();
	}

	public String getProductRemark() {
		return this.product.getRemark();
	}

	public String getProductPriceRange() {
		return this.product.getPriceRange();
	}

	public InboundRegister getInboundRegister() {
		return inboundRegister;
	}

	public void setInboundRegister(InboundRegister inboundRegister) {
		this.inboundRegister = inboundRegister;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public TallyArea getTarryArea() {
		return tarryArea;
	}

	public void setTarryArea(TallyArea tarryArea) {
		this.tarryArea = tarryArea;
	}

	public String getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(String productionPlace) {
		this.productionPlace = productionPlace;
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

	public String getReadCode() {
		return readCode;
	}

	public void setReadCode(String readCode) {
		this.readCode = readCode;
	}
	
}
