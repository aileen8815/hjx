package com.wwyl.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.*;

import javax.persistence.*;

/**
 * Created by fyunli on 14-6-16.
 */
@Entity
@Table(name = "TJ_STOCK_WASTAGE_ITEM")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "new"})
public class StockWastageItem extends PersistableEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private StockWastage stockWastage;
    @ManyToOne(fetch = FetchType.LAZY)
    private StockWastageType stockWastageType;

    @ManyToOne
    private Product product;// 货品
    @Basic
    private double amount;// 数量
    @ManyToOne
    private MeasureUnit amountMeasureUnit;// 数量计量单位
    
	@Basic
	private double weight;// 重量
	@ManyToOne
	private MeasureUnit weightMeasureUnit;// 重量计量单位
	
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreLocation storeLocation; // 储位
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreContainer storeContainer; // 托盘

    public StockWastage getStockWastage() {
        return stockWastage;
    }

    public void setStockWastage(StockWastage stockWastage) {
        this.stockWastage = stockWastage;
    }

    public StockWastageType getStockWastageType() {
        return stockWastageType;
    }

    public void setStockWastageType(StockWastageType stockWastageType) {
        this.stockWastageType = stockWastageType;
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

	public MeasureUnit getWeightMeasureUnit() {
		return weightMeasureUnit;
	}

	public void setWeightMeasureUnit(MeasureUnit weightMeasureUnit) {
		this.weightMeasureUnit = weightMeasureUnit;
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
}
