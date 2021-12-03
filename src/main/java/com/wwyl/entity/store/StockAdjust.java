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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.StockAdjustStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.StockAdjustType;

/**
 * 库存调整
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_STOCK_ADJUST")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class StockAdjust extends PersistableEntity {

	@Column(length = 50, unique = true, nullable = false)
	private String serialNo; // 调整作业单号
	@ManyToOne
	private StockAdjustType stockAdjustType;// 调整类型：盘点，移位，报损，etc
	@Column(length = 255)
	private String remark;// 备注
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator submitter;// 提交人
	@Temporal(TemporalType.TIMESTAMP)
	private Date submitTime;// 提交时间
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator approver;// 审核人
	@Temporal(TemporalType.TIMESTAMP)
	private Date approveTime;// 审核时间
	@Basic
	private StockAdjustStatus stockAdjustStatus; // 审核状态
/*
	@OneToMany(mappedBy = "stockAdjust", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<StockAdjustItem> stockAdjustItems;// 调整明细
*/
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public StockAdjustType getStockAdjustType() {
		return stockAdjustType;
	}

	public void setStockAdjustType(StockAdjustType stockAdjustType) {
		this.stockAdjustType = stockAdjustType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Operator getSubmitter() {
		return submitter;
	}

	public void setSubmitter(Operator submitter) {
		this.submitter = submitter;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTyee) {
		this.submitTime = submitTyee;
	}

	public Operator getApprover() {
		return approver;
	}

	public void setApprover(Operator approver) {
		this.approver = approver;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

/*	public Set<StockAdjustItem> getStockAdjustItems() {
		return stockAdjustItems;
	}

	public void setStockAdjustItems(Set<StockAdjustItem> stockAdjustItems) {
		this.stockAdjustItems = stockAdjustItems;
	}*/

	public StockAdjustStatus getStockAdjustStatus() {
		return stockAdjustStatus;
	}

	public void setStockAdjustStatus(StockAdjustStatus stockAdjustStatus) {
		this.stockAdjustStatus = stockAdjustStatus;
	}

	public String stockAdjustTypeName() {
		return this.stockAdjustType.getName();
	}
}
