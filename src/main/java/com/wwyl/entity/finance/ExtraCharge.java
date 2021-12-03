package com.wwyl.entity.finance;

import java.math.BigDecimal;
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
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.finance.PaymentItem;
import com.wwyl.entity.settings.Customer;
 

/**
 * 
 * 市场向客户代收的费用
 * @author jianl
 */
@Entity
@Table(name = "TJ_EXTRA_CHARGE")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class ExtraCharge extends PersistableEntity {

	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String serialNo; // 单号;
	@NotNull
	@ManyToOne
	private Customer customer; // 支付客户;
	@NotNull
	@ManyToOne
	private CEFeeItem feeItem; // 费用项
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;// 记录时间
 
	@ManyToOne(fetch = FetchType.LAZY)
	private PaymentItem paymentItem;// 费用明细
	@Basic
	private BigDecimal amount; // 市场收取的实收费用（元）
	@Basic
	private BigDecimal actuallyAmount;//市场需要付给第三方费用（元）
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
	public CEFeeItem getFeeItem() {
		return feeItem;
	}
	public void setFeeItem(CEFeeItem feeItem) {
		this.feeItem = feeItem;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public PaymentItem getPaymentItem() {
		return paymentItem;
	}
	public void setPaymentItem(PaymentItem paymentItem) {
		this.paymentItem = paymentItem;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getActuallyAmount() {
		return actuallyAmount;
	}
	public void setActuallyAmount(BigDecimal actuallyAmount) {
		this.actuallyAmount = actuallyAmount;
	}
	public String customerName(){
		return customer.getName();
	}
	public String feeItemName(){
		return  feeItem.getName();
	}
}
