package com.wwyl.entity.security;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Constants;
import com.wwyl.Enums;
import com.wwyl.Enums.LockStatus;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.Department;
import com.wwyl.entity.store.StoreAreaAssignee;
import com.wwyl.util.MD5Encryption;
import com.wwyl.util.RandomGenerator;

/**
 * 系统操作员
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_OPERATOR")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class Operator extends PersistableEntity {
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Department department; // 所属部门

	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String username; // 用户名
	@JsonIgnore
	@Column(length = 32, nullable = false)
	private String password; // 密码
	@JsonIgnore
	@Column(length = 50, nullable = false)
	private String passwordSalt; // 盐值

	@Column(length = 200)
	private String name; // 名字
	@Column(length = 1, nullable = false)
	private Enums.Sex sex; // 性别
	@Column(length = 50)
	private String mobile; // 手机
	@Column(length = 100)
	private String email; // 电子邮箱
	@Column(length = 255)
	private String address; // 联系地址
	@Column(length = 6)
	private String zip; // 邮编
	@Column(length = 50)
	private String telephone; // 电话
	@Column(length = 50)
	private String fax; // 传真
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date birthday; // 生日
	@Column(length = 500)
	private String remark; // 备注

	@Temporal(value = TemporalType.TIMESTAMP)
	private Date registerTime; // 注册时间

	@Basic
	private LockStatus lockStatus; // 锁定状态
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date lockTime; // 锁定时间
	@Basic
	private Integer errorCount; // 登录出错次数
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date lastLoginTime; // 最后登录时间
	@Column(length = 50)
	private String lastLoginIpAddr; // 最后登录IP

	@Version
	private int version;

    @JsonIgnore
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToMany(fetch = FetchType.LAZY)
	@OrderBy("code")
	@JoinTable(name = "TJ_OPERATOR_ROLE", joinColumns = { @JoinColumn(name = "operator") }, inverseJoinColumns = { @JoinColumn(name = "role") })
	Set<Role> roles;

    @JsonIgnore
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TJ_OPERATOR_PERMISSION", joinColumns = { @JoinColumn(name = "operator") }, inverseJoinColumns = { @JoinColumn(name = "permission") })
	@OrderBy("code")
	Set<Permission> permissions;
    
    @JsonIgnore
    @OneToMany(mappedBy = "operator", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<StoreAreaAssignee> storeAreaAssignees;

    
	public boolean hasPermission(String permCode) {
		if(this.getId()==Constants.ADMIN_OPERATOR_ID){//admin用户ID为1，拥有全部权限
			return true;
		}
		if (CollectionUtils.isNotEmpty(roles)) {
			for (Role role : roles) {
				if (role.hasPermission(permCode)) {
					return true;
				}
			}
		}

		if (CollectionUtils.isEmpty(permissions)) {
			return false;
		}
		for (Permission perm : permissions) {
			if (StringUtils.equals(permCode, perm.getCode())) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPermission(Permission permission) {
		// 扮演的角色是否有权限？
		if (CollectionUtils.isNotEmpty(roles)) {
			for (Role role : roles) {
				if (role.hasPermission(permission)) {
					return true;
				}
			}
		}

		// 自身是否特别分派有该权限？
		if (CollectionUtils.isEmpty(permissions)) {
			return false;
		}
		return permissions.contains(permission);
	}

	public void increaseErrorCount() {
		if (this.errorCount == null) {
			this.errorCount = 0;
		}
		errorCount++;
	}

	public String getText() {
		return "(" + username + ")" + name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Enums.Sex getSex() {
		return sex;
	}

	public void setSex(Enums.Sex sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getLastLoginIpAddr() {
		return lastLoginIpAddr;
	}

	public void setLastLoginIpAddr(String lastLoginIpAddr) {
		this.lastLoginIpAddr = lastLoginIpAddr;
	}

	public Enums.LockStatus getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Enums.LockStatus lockStatus) {
		this.lockStatus = lockStatus;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> privileges) {
		this.permissions = privileges;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getDepartmentName() {
		return this.getDepartment().getText();
	}

	/**
	 * 仅复制基本资料，以下信息不被复制：<br/>
	 * 用户名、注册时间、锁定状态、锁定时间、登录出错次数、最后登陆时间、最后登陆IP
	 */
	public void copyProfile(Operator source) {
		this.setAddress(source.getAddress());
		this.setBirthday(source.getBirthday());
		this.setDepartment(source.getDepartment());
		this.setEmail(source.getEmail());
		this.setFax(source.getFax());
		this.setMobile(source.getMobile());
		this.setName(source.getName());
		this.setRemark(source.getRemark());
		this.setSex(source.getSex());
		this.setTelephone(source.getTelephone());
		this.setZip(source.getZip());

		// 处理密码并加盐，如源对象密码为空则保留旧密码
		if (StringUtils.isNotBlank(source.getPassword())) {
			this.flushPassword(source.getPassword());
		}

	}

	/**
	 * 刷新密码并加盐
	 */
	public void flushPassword(String newPassword) {
		this.setPasswordSalt(RandomGenerator.randomString(20, true));
		this.setPassword(MD5Encryption.encode(MD5Encryption.encode(newPassword + this.getPasswordSalt())));
	}

	/**
	 * 验证密码
	 */
	public boolean validatePassword(String password) {
		return MD5Encryption.encode(MD5Encryption.encode(password + this.getPasswordSalt())).equals(this.getPassword());
	}

	public void toggleLock() {
		if (LockStatus.正常.equals(getLockStatus())) {
			setLockStatus(LockStatus.人工锁定);
			setLockTime(new Date());
		} else {
			setLockStatus(LockStatus.正常);
			setLockTime(null);
		}
	}

	public void setOperatorRoles(Long[] assignRoles) {
		// 授予角色
		Set<Role> roles = new HashSet<Role>();
		if (ArrayUtils.isNotEmpty(assignRoles)) {
			for (Long roleId : assignRoles) {
				Role role = new Role();
				role.setId(roleId);
				roles.add(role);
			}
		}
		setRoles(roles);
	}

	public Set<StoreAreaAssignee> getStoreAreaAssignees() {
		return storeAreaAssignees;
	}

	public void setStoreAreaAssignees(Set<StoreAreaAssignee> storeAreaAssignees) {
		this.storeAreaAssignees = storeAreaAssignees;
	}
	public String getStoreAreaAssigneeText(){
        if(CollectionUtils.isEmpty(this.storeAreaAssignees)){
            return "";
        }

        String result = "";
        int index=0;
        int size=storeAreaAssignees.size();
        for(StoreAreaAssignee storeAreaAssignee: storeAreaAssignees){
        	index++;
            result += storeAreaAssignee.getstoreAreaCode() + storeAreaAssignee.getstoreAreaName() + (index==size?"":", ");
            
        }
        return result;
    }

	public static void main(String[] args) {
		System.out.println(MD5Encryption.encode(MD5Encryption.encode("June2020$" + "sA3rMG6f0mL4K0o9a8D2")));
	}

}
