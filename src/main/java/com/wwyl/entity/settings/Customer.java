package com.wwyl.entity.settings;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.CustomerGradeStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;

/**
 * 仓储客户 s
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_CUSTOMER")
@Cacheable
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class Customer extends PersistableEntity {

	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String custemerId;// 客户号，从一体化平台引入会员ID ，详细资料存储在一体化平台
	@Column(length = 50)
	private String name;//客户名称
	@Column(length = 50)
	private String shortName;//客户简称
	@Column(length = 20)
	private String credentialsNo;// 身份证号
	
	//一体化平台引入字段
    String cdnr; // 磁道号
    String gsmc; // 公司名称
    String sAccount_NO; // 账号  
    String sMemberCode; // 会员卡号
    String sKHDM; // 客户编号
    String zkmc; // 主卡名称
    String iChildID; // 子卡ID
    String password; //密码
    
    //新一体化平台引入（大白菜+）
    String customerOID; // 客户关键字
    String sKHBH; // 客户编号
    String aCardMac; // 物理卡号
    String accountOID;//账号OID

    //联系人
    @Column(length = 100)
	private String mobile;// 手机
	@Column(length = 100)
	private String email;// 电子邮箱
	@Column(length = 100)
	private String zip;// 邮编
	@Column(length = 255)
	private String address;// 地址
	
    @JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private CustomerGrade customerGrade;//客户等级
    
    @JsonIgnore
   	@ManyToOne(fetch = FetchType.LAZY)
   	private Contact contact;//客户默认联系人
    
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TJ_CUSTOMER_CONTACT", joinColumns = { @JoinColumn(name = "customer") }, inverseJoinColumns = { @JoinColumn(name = "contact") })
	private Set<Contact> contacts;//客户联系人
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TJ_CUSTOMER_PRODUCT", joinColumns = { @JoinColumn(name = "customer") }, inverseJoinColumns = { @JoinColumn(name = "product") })
	private Set<Product> products;  //客户经营商品

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TJ_CUSTOMER_CARRIER", joinColumns = { @JoinColumn(name = "customer") }, inverseJoinColumns = { @JoinColumn(name = "carrier") })
	private Set<Carrier> carriers;//客户承运商
	@Basic
	private int payDate;//付款日期
	@Basic
	private int billDate;//对账日期
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private ChargeType chargeType;
	@Basic
	private Integer maxNumber;// 商品code编码，最大可用值
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date customerGradeStartTime;// 启用等级时间
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private CustomerGrade customerGradeNew;//当前客户等级
	
	@Basic
    private CustomerGradeStatus customerGradeStatus;// 客户当前等级审核状态
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator approver;// 审核人
	@Temporal(TemporalType.TIMESTAMP)
	private Date approveTime;// 审核时间
	
	public ChargeType getChargeType() {
		return chargeType;
	}

	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}
	
	public String getChargeTypeName() {
		if (chargeType != null) {
			return this.chargeType.getName();
		} else {
			return null;
		}
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public String getCustemerId() {
		return custemerId;
	}

	public void setCustemerId(String custemerId) {
		this.custemerId = custemerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCredentialsNo() {
		return credentialsNo;
	}

	public void setCredentialsNo(String credentialsNo) {
		this.credentialsNo = credentialsNo;
	}

	public CustomerGrade getCustomerGrade() {
		return customerGrade;
	}

	public void setCustomerGrade(CustomerGrade customerGrade) {
		this.customerGrade = customerGrade;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public String getCustomerGradeName() {
		if (customerGrade != null) {
			return this.customerGrade.getName();
		} else {
			return null;
		}
	}

	public Set<Carrier> getCarriers() {
		return carriers;
	}

	public void setCarriers(Set<Carrier> carriers) {
		this.carriers = carriers;
	}
	
	public void setCustomerCarrier(Long[] carrierids) {
		Set<Carrier> carrierset = new HashSet<Carrier>();
		if (ArrayUtils.isNotEmpty(carrierids)) {
			for (Long carrierid : carrierids) {
				Carrier carrier = new Carrier();
				carrier.setId(carrierid);
				carrierset.add(carrier);
			}
		}
		setCarriers(carrierset);
	}

	public int getPayDate() {
		return payDate;
	}

	public void setPayDate(int payDate) {
		this.payDate = payDate;
	}

	public int getBillDate() {
		return billDate;
	}

	public void setBillDate(int billDate) {
		this.billDate = billDate;
	}

	public String getCdnr() {
		return cdnr;
	}

	public void setCdnr(String cdnr) {
		this.cdnr = cdnr;
	}

	public String getGsmc() {
		return gsmc;
	}

	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
	}

	public String getsAccount_NO() {
		return sAccount_NO;
	}

	public void setsAccount_NO(String sAccount_NO) {
		this.sAccount_NO = sAccount_NO;
	}

	public String getsMemberCode() {
		return sMemberCode;
	}

	public void setsMemberCode(String sMemberCode) {
		this.sMemberCode = sMemberCode;
	}

	public String getsKHDM() {
		return sKHDM;
	}

	public void setsKHDM(String sKHDM) {
		this.sKHDM = sKHDM;
	}

	public String getZkmc() {
		return zkmc;
	}

	public void setZkmc(String zkmc) {
		this.zkmc = zkmc;
	}

	public String getiChildID() {
		return iChildID;
	}

	public void setiChildID(String iChildID) {
		this.iChildID = iChildID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Integer getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(Integer maxNumber) {
		this.maxNumber = maxNumber;
	}

	public String getText(){
		return this.getsKHDM() + this.getName();
	}
	public  String  getProductCode(){
		this.maxNumber = maxNumber!=null?maxNumber:1;
		String fixedLengthNumber =String.valueOf(maxNumber);
		int point =fixedLengthNumber.length();
		for (int i = 5; i > point; i--) {
			fixedLengthNumber = "0" + fixedLengthNumber;
		}
		setMaxNumber(maxNumber+1);
		return custemerId+fixedLengthNumber;
	}

	public CustomerGradeStatus getCustomerGradeStatus() {
		return customerGradeStatus;
	}

	public void setCustomerGradeStatus(CustomerGradeStatus customerGradeStatus) {
		this.customerGradeStatus = customerGradeStatus;
	}

	public String getCustomerGradeStatusName() {
		if (customerGradeStatus != null) {
			return this.customerGradeStatus.name();
		} else {
			return null;
		}
	}
	
	public Operator getApprover() {
		return approver;
	}

	public void setApprover(Operator approver) {
		this.approver = approver;
	}
	
	public String getApproverName() {
		if (approver != null) {
			return this.approver.getName();
		} else {
			return null;
		}
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	public Date getCustomerGradeStartTime() {
		return customerGradeStartTime;
	}

	public void setCustomerGradeStartTime(Date customerGradeStartTime) {
		this.customerGradeStartTime = customerGradeStartTime;
	}

	public CustomerGrade getCustomerGradeNew() {
		return customerGradeNew;
	}

	public void setCustomerGradeNew(CustomerGrade customerGradeNew) {
		this.customerGradeNew = customerGradeNew;
	}

	public String getCustomerOID() {
		return customerOID;
	}

	public void setCustomerOID(String customerOID) {
		this.customerOID = customerOID;
	}

	public String getsKHBH() {
		return sKHBH;
	}

	public void setsKHBH(String sKHBH) {
		this.sKHBH = sKHBH;
	}

	public String getaCardMac() {
		return aCardMac;
	}

	public void setaCardMac(String aCardMac) {
		this.aCardMac = aCardMac;
	}

	public String getAccountOID() {
		return accountOID;
	}

	public void setAccountOID(String accountOID) {
		this.accountOID = accountOID;
	}
	
	
}
