package com.wwyl.entity.finance;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.PaymentBoundType;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.Enums.PaymentType;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.ChargeType;
import com.wwyl.entity.settings.Comment;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.MeasureUnit;
import com.wwyl.entity.settings.Product;

/**
 * 支付
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_PAYMENT")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Payment extends PersistableEntity {
	
	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String serialNo; //单号;
	
	@JsonIgnore
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer; // 客户
	@Temporal(TemporalType.TIMESTAMP)
	private Date settledTime;// 结算时间
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator settledBy; // 结算员
	@Basic
	private PaymentStatus paymentStatus;// 结算状态
	@Basic
	private PaymentType paymentType;
	@Basic
	private PaymentBoundType paymentBoundType;
	@ManyToOne
	private Product product;// 货品
	@JsonIgnore
	@OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<PaymentItem> paymentItems; // 费用明细
	@JsonIgnore
	@ManyToOne
	private ChargeType chargeType; // 计费类型：现付、月结..etc
	@Column(length = 50)
	private String paymentObjectEntity;// 结算依据，关联到结算对象名称，如InboundRegister, StoreContact
	@Basic
	private Long paymentObjectId;// 结算依据，关联到结算对象ID
	@Column(length = 50)
	private String paymentObjectSerialNo;// 结算依据，关联到结算对象流水号
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator delayApprover;
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TJ_PAYMENT_COMMENT", joinColumns = { @JoinColumn(name = "payment_id") }, inverseJoinColumns = { @JoinColumn(name = "comment_id") })
	private Set<Comment> comments;
	@Column(length = 20)
	private String settleRange; // 结算周期
	 
	private Integer   temporary;//1是临时杂费
	
	@Column(length = 255)
    private String remark;
	
	@JsonIgnore
	@ManyToOne
	private AccountChecking accountChecking;
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

	public Date getSettledTime() {
		return settledTime;
	}

	public void setSettledTime(Date settledTime) {
		this.settledTime = settledTime;
	}

	public Operator getSettledBy() {
		return settledBy;
	}

	public void setSettledBy(Operator settledBy) {
		this.settledBy = settledBy;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Set<PaymentItem> getPaymentItems() {
		return paymentItems;
	}

	public void setPaymentItems(Set<PaymentItem> paymentItems) {
		this.paymentItems = paymentItems;
	}

	public ChargeType getChargeType() {
		return chargeType;
	}

	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}

	public String getPaymentObjectEntity() {
		return paymentObjectEntity;
	}

	public void setPaymentObjectEntity(String paymentObjectEntity) {
		this.paymentObjectEntity = paymentObjectEntity;
	}

	public Long getPaymentObjectId() {
		return paymentObjectId;
	}

	public void setPaymentObjectId(Long paymentObjectId) {
		this.paymentObjectId = paymentObjectId;
	}

	// 结算人员名
	public String getSettledByName() {
		if (settledBy != null) {
			return this.settledBy.getName();
		} else {
			return null;
		}
	}

	// 结算类型名
	public String getChargeTypeName() {
		if (chargeType != null) {
			return this.chargeType.getName();
		} else {
			return null;
		}

	}

	// 客户名
	public String getCustomerName() {
		if (customer != null) {
			return this.customer.getName();
		} else {
			return null;
		}
	}

	public Operator getDelayApprover() {
		return delayApprover;
	}

	public void setDelayApprover(Operator delayApprover) {
		this.delayApprover = delayApprover;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public String getPaymentObjectSerialNo() {
		return paymentObjectSerialNo;
	}

	public void setPaymentObjectSerialNo(String paymentObjectSerialNo) {
		this.paymentObjectSerialNo = paymentObjectSerialNo;
	}

	public String getSettleRange() {
		return settleRange;
	}

	public void setSettleRange(String settleRange) {
		this.settleRange = settleRange;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getTemporary() {
		return temporary;
	}

	public void setTemporary(Integer temporary) {
		this.temporary = temporary;
	}

	public AccountChecking getAccountChecking() {
		return accountChecking;
	}

	public void setAccountChecking(AccountChecking accountChecking) {
		this.accountChecking = accountChecking;
	}
	public PaymentBoundType getPaymentBoundType() {
		return paymentBoundType;
	}

	public void setPaymentBoundType(PaymentBoundType paymentBoundType) {
		this.paymentBoundType = paymentBoundType;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
