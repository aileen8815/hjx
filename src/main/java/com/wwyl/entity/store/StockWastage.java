package com.wwyl.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Comment;
import com.wwyl.entity.settings.Customer;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.Set;

/**
 * 报损
 *
 * @author fyunli
 */
@Entity
@Table(name = "TJ_STOCK_WASTAGE")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "new"})
public class StockWastage extends PersistableEntity {
	
	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String serialNo; // 单号;
	
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stockWastage", cascade = CascadeType.REMOVE)
    private Set<StockWastageItem> stockWastageItems;
    @Basic
    private Enums.StockWastageStatus stockWastageStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date inputTime;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator inputOperator;
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveTime;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator approver;
    @Temporal(TemporalType.TIMESTAMP)
    private Date feeApproveTime;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator feeApprover;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TJ_STOCK_WASTAGE_COMMENT", joinColumns = { @JoinColumn(name = "stock_wastage") }, inverseJoinColumns = { @JoinColumn(name = "stock_wastage_comment") })
	private Set<Comment> conmments;
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

    public Set<StockWastageItem> getStockWastageItems() {
        return stockWastageItems;
    }

    public void setStockWastageItems(Set<StockWastageItem> stockWastageItems) {
        this.stockWastageItems = stockWastageItems;
    }

    public Enums.StockWastageStatus getStockWastageStatus() {
        return stockWastageStatus;
    }

    public void setStockWastageStatus(Enums.StockWastageStatus stockWastageStatus) {
        this.stockWastageStatus = stockWastageStatus;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Operator getInputOperator() {
        return inputOperator;
    }

    public void setInputOperator(Operator inputOperator) {
        this.inputOperator = inputOperator;
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

    public Date getFeeApproveTime() {
        return feeApproveTime;
    }

    public void setFeeApproveTime(Date feeApproveTime) {
        this.feeApproveTime = feeApproveTime;
    }

    public Operator getFeeApprover() {
        return feeApprover;
    }

    public void setFeeApprover(Operator feeApprover) {
        this.feeApprover = feeApprover;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
    public Set<Comment> getConmments() {
		return conmments;
	}

	public void setConmments(Set<Comment> conmments) {
		this.conmments = conmments;
	}

	public String getInputOperatorName(){
    	return this.inputOperator!=null?this.inputOperator.getName():null;
    }
    public String getCustomerName(){
    	return this.customer!=null?this.customer.getName():null;
    }
}
