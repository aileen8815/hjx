package com.wwyl.entity.store;

import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.StockTakingMode;
import com.wwyl.Enums.StockTakingStatus;
import com.wwyl.Enums.StockTakingType;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;

/**
 * 盘点计划
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_STOCK_TAKING")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "new"})
public class StockTaking extends PersistableEntity {
	
	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String serialNo; // 单号;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date registerTime;// 登记时间
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator registerOperator; // 登记入
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date startTime;//盘点开始时间
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;//盘点结束时间
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator stockTakingOperator;//盘点人
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveTime;//盘点结果审核时间
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator approver;//盘点结果审核人
    @Column(length = 255)
	private String remark; // 备注信息
    
	@Basic
	private StockTakingMode stockTakingMode;// 盘点方式：库区盘点，品种盘点，客户盘点，动态盘点
	@Basic
	private StockTakingType stockTakingType;// 盘点类型：初盘，复盘，终盘
	
	@Basic
	private Long  stockTakingObjectId;// 盘点条件  ,盘点全部库区记录-1,全部客户记录-1，否则是库区盘点记录库区id；是客户盘点，记录客户ID....
	
/*    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stockTaking", cascade = CascadeType.REMOVE)
    private Set<StockTakingItem> stockTakingItems;//盘点计划明细
*/    
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stockTaking", cascade = CascadeType.REMOVE)
    private Set<StockTakingResult> stockTakingResults;//盘点库存差异结果
    
    @Basic
	private StockTakingStatus stockTakingStatus;// 盘点状态 ： 待盘点，盘点中，已完成
    
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    private StockTaking stockTakingOld;// 上一次盘点的盘点单
    
	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Operator getRegisterOperator() {
		return registerOperator;
	}

	public void setRegisterOperator(Operator registerOperator) {
		this.registerOperator = registerOperator;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Operator getStockTakingOperator() {
		return stockTakingOperator;
	}

	public void setStockTakingOperator(Operator stockTakingOperator) {
		this.stockTakingOperator = stockTakingOperator;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	public Operator getApprover() {
		return approver;
	}

	public void setApprover(Operator approver) {
		this.approver = approver;
	}

	public StockTakingMode getStockTakingMode() {
		return stockTakingMode;
	}

	public void setStockTakingMode(StockTakingMode stockTakingMode) {
		this.stockTakingMode = stockTakingMode;
	}

	public StockTakingType getStockTakingType() {
		return stockTakingType;
	}

	public void setStockTakingType(StockTakingType stockTakingType) {
		this.stockTakingType = stockTakingType;
	}

/*	public Set<StockTakingItem> getStockTakingItems() {
		return stockTakingItems;
	}

	public void setStockTakingItems(Set<StockTakingItem> stockTakingItems) {
		this.stockTakingItems = stockTakingItems;
	}*/
	
	public Set<StockTakingResult> getStockTakingResults() {
		return stockTakingResults;
	}

	public Long getStockTakingObjectId() {
		return stockTakingObjectId;
	}

	public void setStockTakingObjectId(Long stockTakingObjectId) {
		this.stockTakingObjectId = stockTakingObjectId;
	}

	public void setStockTakingResults(Set<StockTakingResult> stockTakingResults) {
		this.stockTakingResults = stockTakingResults;
	}

	public StockTakingStatus getStockTakingStatus() {
		return stockTakingStatus;
	}

	public void setStockTakingStatus(StockTakingStatus stockTakingStatus) {
		this.stockTakingStatus = stockTakingStatus;
	}
    public String  getStockTakingOperatorName(){
    	return stockTakingOperator!=null?stockTakingOperator.getName():null;
    }

	public StockTaking getStockTakingOld() {
		return stockTakingOld;
	}

	public void setStockTakingOld(StockTaking stockTakingOld) {
		this.stockTakingOld = stockTakingOld;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}
