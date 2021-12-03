package com.wwyl.entity.settings;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.StoreLocationStatus;
import com.wwyl.entity.PersistableEntity;

/**
 * 储位
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_STORE_LOCATION")
@Cacheable
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class StoreLocation extends PersistableEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	private StoreArea storeArea;
	@NotBlank
	@Column(length = 255, nullable = false, unique = true)
	private String code;// 编码
	@Column(length = 255)
	private String remark; // 描述
	@ManyToOne
	private StoreLocationType storeLocationType;// 类型（货架或散堆）
	@Basic
	private double length = 120;// 长 （尺寸以厘米为单位）
	@Basic
	private double width = 100;// 宽
	@Basic
	private double height = 180;// 高
	@Basic
	private double weight = 1000;// 承重 （承重以公斤为单位）
	@Basic
	private StoreLocationStatus storeLocationStatus;// 状态（是否冻结）
	@Basic
	private int coordX; // 在库区的相对坐标，排
	@Basic
	private int coordY;// 列
	@Basic
	private int coordZ;// 层
	@Column(length = 255, unique = true)
	private String label;

	public StoreArea getStoreArea() {
		return storeArea;
	}

	public void setStoreArea(StoreArea storeArea) {
		this.storeArea = storeArea;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public StoreLocationType getStoreLocationType() {
		return storeLocationType;
	}

	public void setStoreLocationType(StoreLocationType storeLocationType) {
		this.storeLocationType = storeLocationType;
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

	public StoreLocationStatus getStoreLocationStatus() {
		return storeLocationStatus;
	}

	public void setStoreLocationStatus(StoreLocationStatus stockLocationStatus) {
		this.storeLocationStatus = stockLocationStatus;
	}

	public int getCoordX() {
		return coordX;
	}

	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}

	public int getCoordY() {
		return coordY;
	}

	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}

	public int getCoordZ() {
		return coordZ;
	}

	public void setCoordZ(int coordZ) {
		this.coordZ = coordZ;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAreaText() {
		return this.storeArea!=null?this.getStoreArea().getCode() + this.getStoreArea().getName():null;
	}

	public String getStoreLocationTypeText() {
		return storeLocationType!=null?this.getStoreLocationType().getName():null;
	}

}
