package com.wwyl.entity.finance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Customer;

/**
 * 支付日志
 *
 * @author fyunli
 */
@Entity
@Table(name = "TJ_PAYMENT_LOG")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class PaymentLog extends PersistableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer fromCustomer;// 付款客户
    @ManyToOne(fetch = FetchType.LAZY)
    private Operator payer; //支付操作人员
    @Temporal(TemporalType.TIMESTAMP)
    private Date payTime;// 支付时间
    @Basic
    private BigDecimal amount;// 支付金额（元） 以负数表示市场向客户付款
    @Basic
    private boolean payStatus;//是否支付成功
    @Column(length = 20)
    private String piTradeId;//一体化平台支付号
    
    @JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TJ_PAYMENT_ITEM_LOG", joinColumns = { @JoinColumn(name = "payment_log_id") }, inverseJoinColumns = { @JoinColumn(name = "payment_item_id") })
	private Set<PaymentItem> paymentItems;
    
    public Customer getFromCustomer() {
        return fromCustomer;
    }

    public void setFromCustomer(Customer fromCustomer) {
        this.fromCustomer = fromCustomer;
    }

    public Operator getPayer() {
        return payer;
    }

    public void setPayer(Operator payer) {
        this.payer = payer;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isPayStatus() {
        return payStatus;
    }

    public void setPayStatus(boolean payStatus) {
        this.payStatus = payStatus;
    }

    public String getPiTradeId() {
        return piTradeId;
    }

    public void setPiTradeId(String piTradeId) {
        this.piTradeId = piTradeId;
    }

    public Set<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(Set<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }
}
