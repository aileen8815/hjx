package com.wwyl.entity.store;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.StoreArea;

/**
 * 库区分派
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_STORE_AREA_ASSIGNEE")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class StoreAreaAssignee extends PersistableEntity {

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private StoreArea storeArea; // 库区
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Operator operator; // 仓管员

	public StoreArea getStoreArea() {
		return storeArea;
	}

	public void setStoreArea(StoreArea storeArea) {
		this.storeArea = storeArea;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getOperatorUserName() {
		if (this.operator != null) {
			return this.operator.getUsername();
		}
		return null;
	}
	public String getOperatorName() {
		if (this.operator != null) {
			return this.operator.getName();
		}
		return null;
	}

	public String getstoreAreaName() {
		return storeArea!=null?storeArea.getName():null;
	}
	public String getstoreAreaCode() {
		return storeArea!=null?storeArea.getCode():null;
	}
}
