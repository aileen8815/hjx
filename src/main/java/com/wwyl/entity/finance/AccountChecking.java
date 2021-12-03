package com.wwyl.entity.finance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.Customer;

/**
 * 对账单
 * 
 * @author jianl
 */
@Entity
@Table(name = "TJ_ACCOUNT_CHECKING")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class AccountChecking extends PersistableEntity {
	
	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String serialNo; //单号;
	
	@JsonIgnore
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer; // 客户
 
	private Date checkingTime;// 对账单生成时间
    private boolean     overdueFine;//是否收取违约金
    @Basic
    private BigDecimal     overdueMoney;//违约金额
    @Temporal(TemporalType.TIMESTAMP)
	private Date overdueTime;// 计算违约金的日期
    
    @JsonIgnore
	@OneToMany(mappedBy = "accountChecking", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Payment> payments; // 费用明细
	
	/*@Column(length = 50)
	private String period; // 对账周期
*/	
    @Temporal(TemporalType.TIMESTAMP)
	private Date startTime;// 对账开始时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;// 对账结束时间
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date payTime;// 付款时间
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date actualPayTime;// 实际付款时间
	@Basic
	private PaymentStatus paymentStatus;// 付款状态
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


	public Date getCheckingTime() {
		return checkingTime;
	}

	public void setCheckingTime(Date checkingTime) {
		this.checkingTime = checkingTime;
	}

 
	public boolean isOverdueFine() {
		return overdueFine;
	}

	public void setOverdueFine(boolean overdueFine) {
		this.overdueFine = overdueFine;
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

	// 客户名
	public String getCustomerName() {
		if (customer != null) {
			return this.customer.getName();
		} else {
			return null;
		}
	}

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Date getActualPayTime() {
		return actualPayTime;
	}

	public void setActualPayTime(Date actualPayTime) {
		this.actualPayTime = actualPayTime;
	}

	public BigDecimal getOverdueMoney() {
		return overdueMoney;
	}

	public void setOverdueMoney(BigDecimal overdueMoney) {
		this.overdueMoney = overdueMoney;
	}

	public Date getOverdueTime() {
		return overdueTime;
	}

	public void setOverdueTime(Date overdueTime) {
		this.overdueTime = overdueTime;
	}
	
}
