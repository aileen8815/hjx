package com.wwyl.entity.settings;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;

/**
 * @author fyunli
 */
@Entity
@Table(name = "TJ_CONTACT")
@Cacheable
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class Contact extends PersistableEntity {

	@Column(length = 50, nullable = true)
	private String linkman;// 联系人
	@Column(length = 100)
	private String mobile;// 手机
	@Column(length = 100)
	private String tel;// 电话
	@Column(length = 100)
	private String fax;// 传真
	@Column(length = 100)
	private String email;// 电子邮箱
	@Column(length = 100)
	private String zip;// 邮编
	@Column(length = 255)
	private String address;// 地址

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactinfo(){
		if(this.address==null){
			this.address="";
		}
		return this.linkman+" "+this.mobile+" "+this.address;
	}
}
