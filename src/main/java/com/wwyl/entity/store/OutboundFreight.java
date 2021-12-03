package com.wwyl.entity.store;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Carrier;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.TallyArea;
import com.wwyl.entity.settings.VehicleType;

/**
 * 出库发货单
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_OUTBOUND_FREIGHT")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class OutboundFreight extends PersistableEntity {

	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String serialNo; // 出库发货单号
	@NotNull
	@ManyToOne
	private Customer customer; // 客户
	@Temporal(TemporalType.TIMESTAMP)
	private Date freightTime;// 发货时间
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator operator; // 操作人
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "outboundFreight", cascade = CascadeType.REMOVE)
	private Set<OutboundFreightItem> outboundFreightItems; // 发货商品明细
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private OutboundRegister outboundRegister; // 关联出库单
	@ManyToOne(fetch = FetchType.LAZY)
	private TallyArea tallyArea;// 发货理货区
	//@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Carrier carrier;// 承运商
	@ManyToOne(fetch = FetchType.LAZY)
	private VehicleType vehicleType;// 车型
	@Column(length = 50)
	private String vehicleNo;// 车牌号
	@Column(length = 50)
	private String driver;// 司机
	@Column(length = 50)
	private String driverIdNo;// 司机身份证号

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

	public Date getFreightTime() {
		return freightTime;
	}

	public void setFreightTime(Date freightTime) {
		this.freightTime = freightTime;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Set<OutboundFreightItem> getOutboundFreightItems() {
		return outboundFreightItems;
	}

	public void setOutboundFreightItems(Set<OutboundFreightItem> outboundFreightItems) {
		this.outboundFreightItems = outboundFreightItems;
	}

	public OutboundRegister getOutboundRegister() {
		return outboundRegister;
	}

	public void setOutboundRegister(OutboundRegister outboundRegister) {
		this.outboundRegister = outboundRegister;
	}

	public TallyArea getTallyArea() {
		return tallyArea;
	}

	public void setTallyArea(TallyArea tallyArea) {
		this.tallyArea = tallyArea;
	}

	public Carrier getCarrier() {
		return carrier;
	}

	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDriverIdNo() {
		return driverIdNo;
	}

	public void setDriverIdNo(String driverIdNo) {
		this.driverIdNo = driverIdNo;
	}

	public String getCustomerName() {
		return this.customer.getName();
	}

	public String getTallyAreaName() {
		if(this.tallyArea!=null){
			return this.tallyArea.getName();
		}else{
			return null;
		}
		
	}

	public String getCarrierName() {
		if(this.carrier!=null){
			return this.carrier.getFullName();
		}else{
			return null;
		}
		
	}

	public  String  getVehicleTypeName(){
		if(this.vehicleType!=null){
			return this.vehicleType.getName();
		}else{
			return null;
		}
	}
	public  String  getOutboundSerialNo(){
		if(this.outboundRegister!=null){
			return this.outboundRegister.getSerialNo();
		}else{
			return null;
		}
	}
	
}
