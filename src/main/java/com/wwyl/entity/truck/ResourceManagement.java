package com.wwyl.entity.truck;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.VehicleType;

@Entity
@Table(name = "TJ_TRUCK_ResourceManagement")
@Cacheable
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })


public class ResourceManagement extends PersistableEntity {
	

	@NotNull
	@Column(length = 20, nullable = false, unique = true)
	private String vehicleNo;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private VehicleType vehicleType;
	@Column(length = 20, nullable = false, unique = true)
	
	@Basic
	private double weight;// 重量
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;// 开始有效日期
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;// 结束有效日期
	
	@NotNull
	@Column(length = 20, nullable = false, unique = true)
	private String name;
	
	@NotNull
	@Column(length = 20, nullable = false, unique = true)
	private String telePhone;
	
	@Column(length = 255)
	private String remark;
	
	public  String  getVehicleTypeName(){
		if(this.vehicleType!=null){
			return this.vehicleType.getName();
		}else{
			return null;
		}
	}
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}	

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}	
}