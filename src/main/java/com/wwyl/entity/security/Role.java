package com.wwyl.entity.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;

/**
 * 角色
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_ROLE")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Cacheable
public class Role extends PersistableEntity {

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String code; // 角色编码
	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String name;// 角色名称
	@Column(length = 255)
	private String remark;// 备注

    @JsonIgnore
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TJ_ROLE_PERMISSION", joinColumns = { @JoinColumn(name = "role") }, inverseJoinColumns = { @JoinColumn(name = "permission") })
	private Set<Permission> permissions; // 角色权限集合

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public boolean hasPermission(String permCode) {
		if (CollectionUtils.isEmpty(permissions)) {
			return false;
		}
		for (Permission permission : permissions) {
			if (StringUtils.equals(permCode, permission.getCode())) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPermission(Permission permission) {
		if (CollectionUtils.isEmpty(permissions)) {
			return false;
		}
		return permissions.contains(permission);
	}

	public void setRolePermissions(Long[] perms) {
		// 设置权限
		Set<Permission> permissions = new HashSet<Permission>();
		if (ArrayUtils.isNotEmpty(perms)) {
			for (Long permissionId : perms) {
				Permission permission = new Permission();
				permission.setId(permissionId);
				permissions.add(permission);
			}
		}
		setPermissions(permissions);
	}

}
