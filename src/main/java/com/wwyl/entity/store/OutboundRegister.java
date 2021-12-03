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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.OutboundType;
import com.wwyl.Enums.StockOutStatus;
import com.wwyl.Enums.VehicleSource;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.VehicleType;

/**
 * 出库登记单
 *
 * @author fyunli
 */
@Entity
@Table(name = "TJ_OUTBOUND_REGISTER")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "new"})
public class OutboundRegister extends PersistableEntity {

    @NotBlank
    @Column(length = 50, nullable = false, unique = true)
    private String serialNo; // 登记单号;
    @NotNull
    @ManyToOne
    private Customer customer; // 客户;
    @Temporal(TemporalType.TIMESTAMP)
    private Date outboundTime;// 出库时间
    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleType vehicleType;// 来车类型
    @Basic
    private int vehicleAmount = 1; // 来车台数
    @Column(length = 100)
    private String vehicleNumbers;// 车牌号
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "outboundRegister", cascade = CascadeType.REMOVE)
    private Set<OutboundRegisterItem> outboundRegisterItems; // 出库商品明细

    @ManyToOne(fetch = FetchType.LAZY)
    private Operator registerOperator; // 登记入
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerTime;// 登记时间

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private OutboundBooking outboundBooking; // 关联预约单

    @JsonIgnore
    @OneToMany(mappedBy = "outboundRegister", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<StockOut> stockOuts; // 拣货单
    @Temporal(TemporalType.TIMESTAMP)
    private Date completeTime;// 拣货完成时间
    @Basic
    private StockOutStatus stockOutStatus;// 整体出库状态
    @Basic
    private VehicleSource vehicleSource;// 车辆来源
    @Basic
    private boolean haveProxy;// 是否携带委托书
    @Basic
    private OutboundType outboundType;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "outboundRegister", cascade = CascadeType.REMOVE)
    private Set<OutboundCheckItem> outboundCheckItems; // 出库商品明细

    // TODO 结算单
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment; // 缴费单

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "outboundRegister", cascade = CascadeType.REMOVE)
    private Set<OutboundFreight> outboundFreights; // 出库发货单

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "outboundRegister")
    private Set<OutboundTarry> outboundTarrys;


    public Set<OutboundTarry> getOutboundTarrys() {
        return outboundTarrys;
    }

    public void setOutboundTarrys(Set<OutboundTarry> outboundTarrys) {
        this.outboundTarrys = outboundTarrys;
    }

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

    public Date getOutboundTime() {
        return outboundTime;
    }

    public void setOutboundTime(Date outboundTime) {
        this.outboundTime = outboundTime;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getVehicleAmount() {
        return vehicleAmount;
    }

    public void setVehicleAmount(int vehicleAmount) {
        this.vehicleAmount = vehicleAmount;
    }

    public Set<OutboundRegisterItem> getOutboundRegisterItems() {
        return outboundRegisterItems;
    }

    public void setOutboundRegisterItems(Set<OutboundRegisterItem> outboundRegisterItems) {
        this.outboundRegisterItems = outboundRegisterItems;
    }

    public Operator getRegisterOperator() {
        return registerOperator;
    }

    public void setRegisterOperator(Operator registerOperator) {
        this.registerOperator = registerOperator;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public OutboundBooking getOutboundBooking() {
        return outboundBooking;
    }

    public void setOutboundBooking(OutboundBooking outboundBooking) {
        this.outboundBooking = outboundBooking;
    }

    public Set<StockOut> getStockOuts() {
        return stockOuts;
    }

    public void setStockOuts(Set<StockOut> stockOuts) {
        this.stockOuts = stockOuts;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public StockOutStatus getStockOutStatus() {
        return stockOutStatus;
    }

    public void setStockOutStatus(StockOutStatus stockOutStatus) {
        this.stockOutStatus = stockOutStatus;
    }

    public String getVehicleNumbers() {
        return vehicleNumbers;
    }

    public void setVehicleNumbers(String vehicleNumbers) {
        this.vehicleNumbers = vehicleNumbers;
    }

    public OutboundType getOutboundType() {
        return outboundType;
    }

    public void setOutboundType(OutboundType outboundType) {
        this.outboundType = outboundType;
    }

    public String getCustomerName() {
        return this.customer.getName();
    }

    public String getVehicleTypeName() {
        if (this.vehicleType != null) {
            return this.vehicleType.getName();
        } else {
            return null;
        }
    }

    public Set<OutboundCheckItem> getOutboundCheckItems() {
        return outboundCheckItems;
    }

    public void setOutboundCheckItems(Set<OutboundCheckItem> outboundCheckItems) {
        this.outboundCheckItems = outboundCheckItems;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Set<OutboundFreight> getOutboundFreights() {
        return outboundFreights;
    }

    public void setOutboundFreights(Set<OutboundFreight> outboundFreights) {
        this.outboundFreights = outboundFreights;
    }

    public VehicleSource getVehicleSource() {
        return vehicleSource;
    }

    public void setVehicleSource(VehicleSource vehicleSource) {
        this.vehicleSource = vehicleSource;
    }

    public boolean isHaveProxy() {
        return haveProxy;
    }

    public void setHaveProxy(boolean haveProxy) {
        this.haveProxy = haveProxy;
    }

}
