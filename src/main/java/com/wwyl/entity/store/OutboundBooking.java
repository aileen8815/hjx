package com.wwyl.entity.store;

import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.BookingStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.BookingMethod;
import com.wwyl.entity.settings.Comment;
import com.wwyl.entity.settings.Contact;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.settings.VehicleType;

/**
 * 出库预约
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_OUTBOUND_BOOKING")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class OutboundBooking extends PersistableEntity {

	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String serialNo; // 预约单号;
	@NotNull
	@ManyToOne
	private Customer customer; // 客户;
	@NotNull
	@ManyToOne
	private StoreArea storeArea; // 预约仓间
	@Temporal(TemporalType.TIMESTAMP)
	private Date bookTime;// 预约时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date applyOutboundTime;// 申请出库时间
	@ManyToOne(fetch = FetchType.LAZY)
	private VehicleType vehicleType;// 来车类型
	@Basic
	private int vehicleAmount; // 来车台数
	@Column(length = 100)
	private String vehicleNumbers;// 车牌号
	@Column(length = 255)
	private String note; // 客户留言

	@NotNull
	@ManyToOne
	private BookingMethod bookingMethod;// 预约方式
	@NotNull
	@Basic
	private BookingStatus bookingStatus;// 预约状态
	@Temporal(TemporalType.TIMESTAMP)
	private Date confirmOutboundTime; // 确认出库库时间
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator confirmOperator; // 确认人
	@Temporal(TemporalType.TIMESTAMP)
	private Date confirmTime; // 确认时间
	@Column(length = 255)
	private String confirmRemark;// 确认备注
	@ManyToOne
	private Contact contact; // 客户联系人;
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "outboundBooking", cascade = CascadeType.REMOVE)
	private Set<OutboundBookingItem> outboundBookingItems; // 预约入库商品明细
	@Basic
	private int storeContainerCount;// 预分派托盘数
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TJ_OUTBOUND_BOOKING_COMMENT", joinColumns = { @JoinColumn(name = "outbound_booking") }, inverseJoinColumns = { @JoinColumn(name = "outbound_comment") })
	private Set<Comment> conmments;

	public String getSerialNo() {
		return serialNo;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Set<Comment> getConmments() {
		return conmments;
	}

	public void setConmments(Set<Comment> conmments) {
		this.conmments = conmments;
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

	public StoreArea getStoreArea() {
		return storeArea;
	}

	public void setStoreArea(StoreArea storeArea) {
		this.storeArea = storeArea;
	}

	public int getStoreContainerCount() {
		return storeContainerCount;
	}

	public void setStoreContainerCount(int storeContainerCount) {
		this.storeContainerCount = storeContainerCount;
	}

	public Date getBookTime() {
		return bookTime;
	}

	public void setBookTime(Date bookTime) {
		this.bookTime = bookTime;
	}

	public Date getApplyOutboundTime() {
		return applyOutboundTime;
	}

	public void setApplyOutboundTime(Date applyOutboundTime) {
		this.applyOutboundTime = applyOutboundTime;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BookingMethod getBookingMethod() {
		return bookingMethod;
	}

	public void setBookingMethod(BookingMethod bookingMethod) {
		this.bookingMethod = bookingMethod;
	}

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public Date getConfirmOutboundTime() {
		return confirmOutboundTime;
	}

	public void setConfirmOutboundTime(Date confirmOutboundTime) {
		this.confirmOutboundTime = confirmOutboundTime;
	}

	public Operator getConfirmOperator() {
		return confirmOperator;
	}

	public void setConfirmOperator(Operator confirmOperator) {
		this.confirmOperator = confirmOperator;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getConfirmRemark() {
		return confirmRemark;
	}

	public void setConfirmRemark(String confirmRemark) {
		this.confirmRemark = confirmRemark;
	}

	public Set<OutboundBookingItem> getOutboundBookingItems() {
		return outboundBookingItems;
	}

	public void setOutboundBookingItems(Set<OutboundBookingItem> outboundBookingItems) {
		this.outboundBookingItems = outboundBookingItems;
	}

	public String getVehicleNumbers() {
		return vehicleNumbers;
	}

	public void setVehicleNumbers(String vehicleNumbers) {
		this.vehicleNumbers = vehicleNumbers;
	}
	public  String  getCustomerName(){
		return this.customer.getName();
	}
	public  String  getStoreAreaName(){
		if(this.storeArea!=null){
			return this.storeArea.getName();
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
	public  String  getBookingMethodName(){
		return this.bookingMethod.getName();
	}

}
