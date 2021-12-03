package com.wwyl.entity.finance;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.ce.CEFeeItem;

/**
 * 支付明细
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_PAYMENT_ITEM")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class PaymentItem extends PersistableEntity {

    @JsonIgnore
	@ManyToOne
	private Payment payment;
    @JsonIgnore
	@ManyToOne
	private CEFeeItem feeItem; // 收费项目
	@Basic
	private BigDecimal amount; // 应收费额（元）
	@Basic
	private BigDecimal actuallyAmount;//实收费用（元）
    @Column(length = 50)
    private String paymentObjectEntity;// 结算依据，关联到结算对象名称，如InboundRegister, StoreContact
    @Basic
    private Long paymentObjectId;// 结算依据，关联到结算对象ID
    @Column(length = 255)
    private String remark;
    @Basic
    private String ruleComment; //计算规则
    @Basic
    private Enums.PaymentStatus paymentStatus;// 结算状态
   /* @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentLog paymentLog;*/
	@Temporal(TemporalType.TIMESTAMP)
	private Date payTime;// 付款时间
	
	@Basic
	@Column(columnDefinition = "float DEFAULT 0")
	private double weight;// 重量
	@Basic
	@Column(columnDefinition = "float DEFAULT 0")
	private double amountPiece;// 数量
	@Basic
	@Column(columnDefinition = "float DEFAULT 0")
	private int storeContainerCount;// 托盘数

    public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public CEFeeItem getFeeItem() {
		return feeItem;
	}

	public void setFeeItem(CEFeeItem feeItem) {
		this.feeItem = feeItem;
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

	//收费项目描述
	public String  getFeeItemRemark(){
		return this.feeItem.getRemark();
	}
	//收费项目名称
	public String  getFeeItemName(){
		return this.feeItem.getName();
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRuleComment() {
		return ruleComment;
	}

	public void setRuleComment(String ruleComment) {
		this.ruleComment = ruleComment;
	}

	public Enums.PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Enums.PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

/*    public PaymentLog getPaymentLog() {
        return paymentLog;
    }

    public void setPaymentLog(PaymentLog paymentLog) {
        this.paymentLog = paymentLog;
    }*/

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getAmountPiece() {
		return amountPiece;
	}

	public void setAmountPiece(double amountPiece) {
		this.amountPiece = amountPiece;
	}

	public int getStoreContainerCount() {
		return storeContainerCount;
	}

	public void setStoreContainerCount(int storeContainerCount) {
		this.storeContainerCount = storeContainerCount;
	}
	
}
