package com.wwyl.entity.ce;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.wwyl.entity.PersistableEntity;

/**
 * @author sjwang
 */
@MappedSuperclass
public abstract class FeeItem extends PersistableEntity {
	@Column(length = 20, nullable = false, unique = true)
	private String code;
	@Column(length = 20, nullable = false, unique = true)
	private String name;
	@Column(length = 255)
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
