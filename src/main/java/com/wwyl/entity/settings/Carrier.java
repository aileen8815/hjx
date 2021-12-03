package com.wwyl.entity.settings;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;

/**
 * 承运商
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_CARRIER")
@Cacheable
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Carrier extends PersistableEntity {

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String code;// 编码
	@NotBlank
	@Column(length = 100, nullable = false)
	private String shortName;// 简称
	@Column(length = 255)
	private String fullName; // 全称
	@ManyToOne(fetch = FetchType.LAZY)
	private CarrierType carrierType; // 类别
	@Column(length = 200)
	private String address;// 详细地址
	@Column(length = 255)
	private String intro;// 车队资料
	@Column(length = 50)
	private String linkman; // 联系人
	@Column(length = 50)
	private String tel;// 电话
	@Column(length = 50)
	private String fax;// 传真
	@Column(length = 50)
	private String rank;// 承运人（供应商）评级

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public CarrierType getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(CarrierType carrierType) {
		this.carrierType = carrierType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Carrier() {

	}

	/**
	 * 获得承运商类型名称
	 * 
	 * @return
	 */
	public String getcarrierTypeName() {
		return this.carrierType.getName();
	}
}
