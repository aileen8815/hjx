package com.wwyl.entity.store;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

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
import com.wwyl.Enums.StockInStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.settings.StoreLocation;

/**
 * 上架单
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_STOCK_IN")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class StockIn extends PersistableEntity {

	@NotNull
	@Column(length = 20)
	private String serialNo;
	@NotNull
	@ManyToOne
	private Customer customer; // 客户;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private InboundRegister inboundRegister;// 入库收货单
	@NotNull
	@ManyToOne
	private InboundReceiptItem inboundReceiptItem; // 入库收货单明细

	@ManyToOne(fetch = FetchType.LAZY)
	private Operator stockInOperator; // 上架操作员
	@ManyToOne(fetch = FetchType.LAZY)
	private StoreLocation storeLocation; // 上架储位
	@Temporal(TemporalType.TIMESTAMP)
	private Date stockInStartTime; // 上架开始时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date stockInEndTime; // 上架结束时间
	
	@ManyToOne
	private StoreArea storeArea; // 登记仓间
	@Basic
	private StockInStatus stockInStatus;// 上架单状态

	public InboundRegister getInboundRegister() {
		return inboundRegister;
	}

	public void setInboundRegister(InboundRegister inboundRegister) {
		this.inboundRegister = inboundRegister;
	}

	public Operator getStockInOperator() {
		return stockInOperator;
	}

	public void setStockInOperator(Operator stockInOperator) {
		this.stockInOperator = stockInOperator;
	}

	public StoreLocation getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(StoreLocation storeLocation) {
		this.storeLocation = storeLocation;
	}

	public Date getStockInStartTime() {
		return stockInStartTime;
	}

	public void setStockInStartTime(Date stockInStartTime) {
		this.stockInStartTime = stockInStartTime;
	}

	public Date getStockInEndTime() {
		return stockInEndTime;
	}

	public void setStockInEndTime(Date stockInEndTime) {
		this.stockInEndTime = stockInEndTime;
	}

	public StockInStatus getStockInStatus() {
		return stockInStatus;
	}

	public void setStockInStatus(StockInStatus stockInStatus) {
		this.stockInStatus = stockInStatus;
	}

	public InboundReceiptItem getInboundReceiptItem() {
		return inboundReceiptItem;
	}

	public void setInboundReceiptItem(InboundReceiptItem inboundReceiptItem) {
		this.inboundReceiptItem = inboundReceiptItem;
	}

	public Set<StoreLocation> getPreStoreLocations() {
		return null; //this.getInboundRegister().getPreStoreLocations();
	}

	public String getWeightMeasureUnitName() {
		return this.inboundReceiptItem.getWeightMeasureUnitName();
	}

	public double getAmount() {
		return this.inboundReceiptItem.getAmount();
	}

	public double getWeight() {
		return this.inboundReceiptItem.getWeight();
	}

	public String getAmountMeasureUnitName() {
		return this.inboundReceiptItem.getAmountMeasureUnitName();
	}

	public String getProductName() {
		return this.inboundReceiptItem.getProductName();
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getStoreContainerLabel(){
		return inboundReceiptItem!=null?inboundReceiptItem.getStoreContainer().getLabel():"";
	}
	public String getInboundReceiptSerialNo() {
		return this.getInboundRegister().getSerialNo();
	}
	public String getProductDetail() {
		return this.getInboundReceiptItem().getProductDetail();
	}
	public String getStoreAreainfo(){
		Set<StoreLocation> 	preStoreLocations= getPreStoreLocations();
		if(preStoreLocations!=null){
			Set<Long> storeareaSet=new LinkedHashSet<Long>();
			StringBuffer bufer=new StringBuffer();
			for (StoreLocation storeLocation : preStoreLocations) {
				StoreArea storeArea=storeLocation.getStoreArea();
				if(!storeareaSet.contains(storeArea.getId())){
					storeareaSet.add(storeArea.getId());
					bufer.append(storeArea.getName()+" ");
				}
			}
			return bufer.toString();
		}else{
			return null;
		}
	}

	public StoreArea getStoreArea() {
		return storeArea;
	}

	public void setStoreArea(StoreArea storeArea) {
		this.storeArea = storeArea;
	}
	
}
