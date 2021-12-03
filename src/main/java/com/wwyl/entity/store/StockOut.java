package com.wwyl.entity.store;

import java.util.Calendar;
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
import com.wwyl.Enums.StockOutStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.MeasureUnit;
import com.wwyl.entity.settings.Packing;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.settings.TallyArea;

/**
 * 拣货单
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_STOCK_OUT")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class StockOut extends PersistableEntity {

	@NotNull
	@Column(length = 20)
	private String serialNo;
	@NotNull
	@ManyToOne
	private Customer customer; // 客户;
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
	@Column(length = 255)
	private String productionPlace; // 产地
	@ManyToOne(fetch = FetchType.LAZY)
	private StoreContainer storeContainer; // 托盘

	@ManyToOne(fetch = FetchType.LAZY)
	private Operator stockOutOperator; // 拣货操作员
	@ManyToOne(fetch = FetchType.LAZY)
	private StoreLocation storeLocation; // 拣货储位
	@Temporal(TemporalType.TIMESTAMP)
	private Date stockOutStartTime; // 拣货开始时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date stockOutEndTime; // 拣货结束时间
	@Basic
	private StockOutStatus stockOutStatus;// 拣货单状态
	@ManyToOne(fetch = FetchType.LAZY)
	private TallyArea tallyArea;// 出库理货区

	@Temporal(TemporalType.TIMESTAMP)
	private Date stockInTime; // 上架时间
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date settledTime; // 结算时间
	
	@Column(length = 255)
	private String readCode; // 抄码
	

	// @ManyToOne
	// private OutboundRegisterItem outboundRegisterItem;// 出库登记单详细

	// public OutboundRegisterItem getOutboundRegisterItem() {
	// return outboundRegisterItem;
	// }
	//
	// public void setOutboundRegisterItem(OutboundRegisterItem outboundRegisterItem) {
	// this.outboundRegisterItem = outboundRegisterItem;
	// }

	public OutboundRegister getOutboundRegister() {
		return outboundRegister;
	}

	public void setOutboundRegister(OutboundRegister outboundRegister) {
		this.outboundRegister = outboundRegister;
	}

	public Operator getStockOutOperator() {
		return stockOutOperator;
	}

	public void setStockOutOperator(Operator stockOutOperator) {
		this.stockOutOperator = stockOutOperator;
	}

	public StoreLocation getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(StoreLocation storeLocation) {
		this.storeLocation = storeLocation;
	}

	public String getStoreLocationCode() {
		return storeLocation.getCode();
	}
	public Date getStockOutStartTime() {
		return stockOutStartTime;
	}

	public void setStockOutStartTime(Date stockOutStartTime) {
		this.stockOutStartTime = stockOutStartTime;
	}

	public Date getStockOutEndTime() {
		return stockOutEndTime;
	}

	public void setStockOutEndTime(Date stockOutEndTime) {
		this.stockOutEndTime = stockOutEndTime;
	}

	public StockOutStatus getStockOutStatus() {
		return stockOutStatus;
	}

	public void setStockOutStatus(StockOutStatus stockOutStatus) {
		this.stockOutStatus = stockOutStatus;
	}

	public String getWeightMeasureUnitName() {
		return this.getWeightMeasureUnit().getName();
	}

	public String getAmountMeasureUnitName() {
		return this.getAmountMeasureUnit().getName();
	}

	public String getProductName() {
		return this.getProduct().getName();
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getOutboundRegisterSerialNo() {
		return this.getOutboundRegister().getSerialNo();
	}

	public String getStoreAreaText() {
		return storeLocation.getStoreArea().getText();
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public String getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(String productionPlace) {
		this.productionPlace = productionPlace;
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

	public StoreContainer getStoreContainer() {
		return storeContainer;
	}

	public void setStoreContainer(StoreContainer storeContainer) {
		this.storeContainer = storeContainer;
	}
	public String getStoreContainerLabel() {
		return storeContainer.getLabel();
	}

	public TallyArea getTallyArea() {
		return tallyArea;
	}

	public void setTallyArea(TallyArea tallyArea) {
		this.tallyArea = tallyArea;
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void createFromBookInventory(BookInventory bookInventory) {
		 
		this.setStoreLocation(bookInventory.getStoreLocation());
		this.setAmount(bookInventory.getAmount());
		this.setAmountMeasureUnit(bookInventory.getAmountMeasureUnit());
		this.setPacking(bookInventory.getPacking());
		this.setProduct(bookInventory.getProduct());
		this.setSpec(bookInventory.getSpec());
		this.setWeight(bookInventory.getWeight());
		this.setWeightMeasureUnit(bookInventory.getWeightMeasureUnit());
		this.setStoreContainer(bookInventory.getStoreContainer());
		this.setStockInTime(bookInventory.getStockInTime());
		this.setSettledTime(bookInventory.getSettledTime());
		this.setReadCode(bookInventory.getReadCode());
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

	public String getReadCode() {
		return readCode;
	}

	public void setReadCode(String readCode) {
		this.readCode = readCode;
	}

	public long getStockInDay() {
		//计算存储天数
		Calendar cal = Calendar.getInstance();  

        cal.setTime(getStockInTime());  

        long stockInTime = cal.getTimeInMillis();               

        cal.setTime(new Date());  

        long nowDate = cal.getTimeInMillis();       

        long stockInDay=(nowDate-stockInTime)/(1000*3600*24);  
        
		return stockInDay;
	}

	
}
