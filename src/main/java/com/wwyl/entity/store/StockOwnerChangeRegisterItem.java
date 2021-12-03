package com.wwyl.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.MeasureUnit;
import com.wwyl.entity.settings.Packing;
import com.wwyl.entity.settings.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;

/**
 * 货权转移登记明细
 *
 * Created by fyunli on 14-6-10.
 */
@Entity
@Table(name = "TJ_STOCK_OWNER_CHANGE_REG_ITEM")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class StockOwnerChangeRegisterItem extends PersistableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private StockOwnerChange stockOwnerChange;// 货权转移登记单
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
	@Column(length = 255)
	private String productionPlace; // 产地
    @Basic
    private int qualityGuaranteePeriod; // 保质期
    @Basic
    private int storeDuration;// 预期保管时间

    @Temporal(TemporalType.TIMESTAMP)
    private Date productionDate; // 生产日期
    
	@Column(length = 255)
	private String batchs; // 批次
    
    public StockOwnerChange getStockOwnerChange() {
        return stockOwnerChange;
    }

    public void setStockOwnerChange(StockOwnerChange stockOwnerChange) {
        this.stockOwnerChange = stockOwnerChange;
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
    
    public String getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(String productionPlace) {
		this.productionPlace = productionPlace;
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

    public void setQualityGuaranteePeriod(int qualityGuaranteePeriod) {
        this.qualityGuaranteePeriod = qualityGuaranteePeriod;
    }

    public int getStoreDuration() {
        return storeDuration;
    }

    public void setStoreDuration(int storeDuration) {
        this.storeDuration = storeDuration;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

	public String getBatchs() {
		return batchs;
	}

	public void setBatchs(String batchs) {
		this.batchs = batchs;
	}
	/**
	 * 该方法是获取同一个批次同一种货品的
	 * 
	 * @return
	 */
	public String getBatchProduct() {
		return this.batchs+"_" +this.product.getId() ;
	}
    
}
