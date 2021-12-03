package com.wwyl.entity.settings;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.StoreContainerStatus;
import com.wwyl.entity.PersistableEntity;

/**
 * 储存容器，本项目主要是托盘，周转箱
 * 
 * @author fyunli
 */

@Entity
@Table(name = "TJ_STORE_CONTAINER")
@Cacheable
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class StoreContainer extends PersistableEntity {

	@Column(length = 50)
	private String code1;// 编号1，托盘对应第一个RFID，周转箱对应编号
	@Column(length = 50)
	private String code2;// 编码2, 托盘对应第二个RFID，周转箱无
	@Column(length = 255)
	private String remark; // 备注
	@Column(length = 50, unique = true)
	@OrderBy
	private String label; // 标签号
	@ManyToOne
	private StoreContainerType storeContainerType;// 类型（托盘，周转箱）
	@Basic
	private StoreContainerStatus storeContainerStatus;// 状态
	@Basic
	private double length = 120;// 长 （尺寸以厘米为单位）
	@Basic
	private double width = 100;// 宽
	@Basic
	private double height = 20;// 高
	@Basic
	private double weight = 1000;// 承重 （承重以公斤为单位）

	public String getCode1() {
		return code1;
	}

	public void setCode1(String code1) {
		this.code1 = code1;
	}

	public String getCode2() {
		return code2;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public StoreContainerType getStoreContainerType() {
		return storeContainerType;
	}

	public void setStoreContainerType(StoreContainerType storeContainerType) {
		this.storeContainerType = storeContainerType;
	}

	public StoreContainerStatus getStoreContainerStatus() {
		return storeContainerStatus;
	}

	public void setStoreContainerStatus(StoreContainerStatus stockContainerStatus) {
		this.storeContainerStatus = stockContainerStatus;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getStoreContainerTypeName() {
		return this.getStoreContainerType().getName();
	}
	public String getText(){
		return 	this.getLabel();
	}
}
