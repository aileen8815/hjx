package com.wwyl.entity.finance;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.Customer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 台帐大报表
 *
 * @author hehao
 */
@Entity
@Table(name = "TJ_STANDING_BOOK_DAILY")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class StandingBookDaily extends PersistableEntity {

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date inboundDateTime;// 入库日期

    @NotNull
    @ManyToOne
    private Customer customer; // 客户;

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private double inboundWeightCount;// 重量

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private double inboundAmountCount;// 数量

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private int inboundContainerCount;// 托盘数

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private double outboundWeightCount;// 重量

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private double outboundAmountCount;// 数量

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private int outboundContainerCount;// 托盘数

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private double stockWeightCount;// 重量

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private double stockAmountCount;// 数量

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private int stockContainerCount;// 托盘数

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal containerStorage; // 仓储费（托）

    @Basic
    private String containerStorageRuleComment; //计算规则
    //以下未测试
    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal rentalAreaStorage; // 仓储费（库间）

    @Basic
    private String rentalAreaStorageRuleComment; //计算规则

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal weightStorage; // 仓储费（吨）

    @Basic
    private String weightStorageRuleComment; //计算规则

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal amountStorage; // 仓储费（件）

    @Basic
    private String amountStorageRuleComment; //计算规则
    /////
    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal shipment; // 转运费

    @Basic
    private String shipmentRuleComment; //计算规则

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal sorting; // 分拣费

    @Basic
    private String sortingRuleComment; //计算规则

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal handling; // 装卸费

    @Basic
    private String handlingRuleComment; //计算规则

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal unloading; // 倒货费

    @Basic
    private String unloadingRuleComment; //计算规则

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal shrinkwrap; // 缠膜费

    @Basic
    private String shrinkwrapRuleComment; //计算规则

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal ketonehandling; // 酮体装卸费

    @Basic
    private String ketonehandlingRuleComment; //计算规则

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal writeCode; // 抄码费

    @Basic
    private String writeCodeRuleComment; //计算规则    
    
    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal disposal; // 处置费

    @Basic
    private String disposalRuleComment; //计算规则
    
    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal colding; // 降温费

    @Basic
    private String coldingRuleComment; //计算规则
    
    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal sideUnloading; // 库间倒货费

    @Basic
    private String sideUnloadingRuleComment; //计算规则
    
    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal inUnloading; // 库内倒货费

    @Basic
    private String inUnloadingRuleComment; //计算规则

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal receivableFee; // 应收金额

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal actualFee; // 实收金额

    @Basic
    @Column(columnDefinition = "float DEFAULT 0")
    private BigDecimal receivableBalance; // 应收余额

    public Date getInboundDateTime() {
        return inboundDateTime;
    }

    public void setInboundDateTime(Date inboundDateTime) {
        this.inboundDateTime = inboundDateTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getInboundWeightCount() {
        return inboundWeightCount;
    }

    public void setInboundWeightCount(double inboundWeightCount) {
        this.inboundWeightCount = inboundWeightCount;
    }

    public double getInboundAmountCount() {
        return inboundAmountCount;
    }

    public void setInboundAmountCount(double inboundAmountCount) {
        this.inboundAmountCount = inboundAmountCount;
    }

    public int getInboundContainerCount() {
        return inboundContainerCount;
    }

    public void setInboundContainerCount(int inboundContainerCount) {
        this.inboundContainerCount = inboundContainerCount;
    }

    public double getOutboundWeightCount() {
        return outboundWeightCount;
    }

    public void setOutboundWeightCount(double outboundWeightCount) {
        this.outboundWeightCount = outboundWeightCount;
    }

    public double getOutboundAmountCount() {
        return outboundAmountCount;
    }

    public void setOutboundAmountCount(double outboundAmountCount) {
        this.outboundAmountCount = outboundAmountCount;
    }

    public int getOutboundContainerCount() {
        return outboundContainerCount;
    }

    public void setOutboundContainerCount(int outboundContainerCount) {
        this.outboundContainerCount = outboundContainerCount;
    }

    public double getStockWeightCount() {
        return stockWeightCount;
    }

    public void setStockWeightCount(double stockWeightCount) {
        this.stockWeightCount = stockWeightCount;
    }

    public double getStockAmountCount() {
        return stockAmountCount;
    }

    public void setStockAmountCount(double stockAmountCount) {
        this.stockAmountCount = stockAmountCount;
    }

    public int getStockContainerCount() {
        return stockContainerCount;
    }

    public void setStockContainerCount(int stockContainerCount) {
        this.stockContainerCount = stockContainerCount;
    }

    public BigDecimal getContainerStorage() {
        return containerStorage;
    }

    public void setContainerStorage(BigDecimal containerStorage) {
        this.containerStorage = containerStorage;
    }

    public BigDecimal getRentalAreaStorage() {
        return rentalAreaStorage;
    }

    public void setRentalAreaStorage(BigDecimal rentalAreaStorage) {
        this.rentalAreaStorage = rentalAreaStorage;
    }

    public BigDecimal getWeightStorage() {
        return weightStorage;
    }

    public void setWeightStorage(BigDecimal weightStorage) {
        this.weightStorage = weightStorage;
    }

    public BigDecimal getAmountStorage() {
        return amountStorage;
    }

    public void setAmountStorage(BigDecimal amountStorage) {
        this.amountStorage = amountStorage;
    }

    public BigDecimal getShipment() {
        return shipment;
    }

    public void setShipment(BigDecimal shipment) {
        this.shipment = shipment;
    }

    public BigDecimal getSorting() {
        return sorting;
    }

    public void setSorting(BigDecimal sorting) {
        this.sorting = sorting;
    }

    public BigDecimal getHandling() {
        return handling;
    }

    public void setHandling(BigDecimal handling) {
        this.handling = handling;
    }

    public BigDecimal getUnloading() {
        return unloading;
    }

    public void setUnloading(BigDecimal unloading) {
        this.unloading = unloading;
    }

    public BigDecimal getShrinkwrap() {
        return shrinkwrap;
    }

    public void setShrinkwrap(BigDecimal shrinkwrap) {
        this.shrinkwrap = shrinkwrap;
    }

    public BigDecimal getKetonehandling() {
        return ketonehandling;
    }

    public void setKetonehandling(BigDecimal ketonehandling) {
        this.ketonehandling = ketonehandling;
    }

    public BigDecimal getWriteCode() {
        return writeCode;
    }

    public void setWriteCode(BigDecimal writeCode) {
        this.writeCode = writeCode;
    }

    public BigDecimal getReceivableFee() {
        return receivableFee;
    }

    public void setReceivableFee(BigDecimal receivableFee) {
        this.receivableFee = receivableFee;
    }

    public BigDecimal getActualFee() {
        return actualFee;
    }

    public void setActualFee(BigDecimal actualFee) {
        this.actualFee = actualFee;
    }

    public BigDecimal getReceivableBalance() {
        return receivableBalance;
    }

    public void setReceivableBalance(BigDecimal receivableBalance) {
        this.receivableBalance = receivableBalance;
    }

    public String getContainerStorageRuleComment() {
        return containerStorageRuleComment;
    }

    public void setContainerStorageRuleComment(String containerStorageRuleComment) {
        this.containerStorageRuleComment = containerStorageRuleComment;
    }

    public String getRentalAreaStorageRuleComment() {
        return rentalAreaStorageRuleComment;
    }

    public void setRentalAreaStorageRuleComment(String rentalAreaStorageRuleComment) {
        this.rentalAreaStorageRuleComment = rentalAreaStorageRuleComment;
    }

    public String getWeightStorageRuleComment() {
        return weightStorageRuleComment;
    }

    public void setWeightStorageRuleComment(String weightStorageRuleComment) {
        this.weightStorageRuleComment = weightStorageRuleComment;
    }

    public String getAmountStorageRuleComment() {
        return amountStorageRuleComment;
    }

    public void setAmountStorageRuleComment(String amountStorageRuleComment) {
        this.amountStorageRuleComment = amountStorageRuleComment;
    }

    public String getShipmentRuleComment() {
        return shipmentRuleComment;
    }

    public void setShipmentRuleComment(String shipmentRuleComment) {
        this.shipmentRuleComment = shipmentRuleComment;
    }

    public String getSortingRuleComment() {
        return sortingRuleComment;
    }

    public void setSortingRuleComment(String sortingRuleComment) {
        this.sortingRuleComment = sortingRuleComment;
    }

    public String getHandlingRuleComment() {
        return handlingRuleComment;
    }

    public void setHandlingRuleComment(String handlingRuleComment) {
        this.handlingRuleComment = handlingRuleComment;
    }

    public String getUnloadingRuleComment() {
        return unloadingRuleComment;
    }

    public void setUnloadingRuleComment(String unloadingRuleComment) {
        this.unloadingRuleComment = unloadingRuleComment;
    }

    public String getShrinkwrapRuleComment() {
        return shrinkwrapRuleComment;
    }

    public void setShrinkwrapRuleComment(String shrinkwrapRuleComment) {
        this.shrinkwrapRuleComment = shrinkwrapRuleComment;
    }

    public String getKetonehandlingRuleComment() {
        return ketonehandlingRuleComment;
    }

    public void setKetonehandlingRuleComment(String ketonehandlingRuleComment) {
        this.ketonehandlingRuleComment = ketonehandlingRuleComment;
    }

    public String getWriteCodeRuleComment() {
        return writeCodeRuleComment;
    }

    public void setWriteCodeRuleComment(String writeCodeRuleComment) {
        this.writeCodeRuleComment = writeCodeRuleComment;
    }

    public BigDecimal getDisposal() {
		return disposal;
	}

	public void setDisposal(BigDecimal disposal) {
		this.disposal = disposal;
	}

	public String getDisposalRuleComment() {
		return disposalRuleComment;
	}

	public void setDisposalRuleComment(String disposalRuleComment) {
		this.disposalRuleComment = disposalRuleComment;
	}

	public BigDecimal getColding() {
		return colding;
	}

	public void setColding(BigDecimal colding) {
		this.colding = colding;
	}

	public String getColdingRuleComment() {
		return coldingRuleComment;
	}

	public void setColdingRuleComment(String coldingRuleComment) {
		this.coldingRuleComment = coldingRuleComment;
	}

	public BigDecimal getSideUnloading() {
		return sideUnloading;
	}

	public void setSideUnloading(BigDecimal sideUnloading) {
		this.sideUnloading = sideUnloading;
	}

	public String getSideUnloadingRuleComment() {
		return sideUnloadingRuleComment;
	}

	public void setSideUnloadingRuleComment(String sideUnloadingRuleComment) {
		this.sideUnloadingRuleComment = sideUnloadingRuleComment;
	}

	public BigDecimal getInUnloading() {
		return inUnloading;
	}

	public void setInUnloading(BigDecimal inUnloading) {
		this.inUnloading = inUnloading;
	}

	public String getInUnloadingRuleComment() {
		return inUnloadingRuleComment;
	}

	public void setInUnloadingRuleComment(String inUnloadingRuleComment) {
		this.inUnloadingRuleComment = inUnloadingRuleComment;
	}

	@Transient
    private String inboundDateText;
    @Transient
    private String customerName;

    public String getInboundDateText() {
        if (StringUtils.isNotBlank(inboundDateText)) {
            return inboundDateText;
        } else {
            return inboundDateTime == null ? "" : DateFormatUtils.format(inboundDateTime, "yyyy-MM-dd");
        }
    }

    public void setInboundDateText(String inboundDateText) {
        this.inboundDateText = inboundDateText;
    }

    public String getCustomerName() {
        if (StringUtils.isNotBlank(customerName)) {
            return customerName;
        }
        return customer == null ? "" : customer.getName();
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
