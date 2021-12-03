package com.wwyl.entity.tenancy;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.StoreLocation;

/**
 * @author sjwang 仓库租赁合同之储位明细
 */
@Entity
@Table(name = "TJ_STORE_CONTRACT_LOCATION")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StoreContractLocationItem extends PersistableEntity {
	

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private StoreContract storeContract;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)	
	private StoreLocation storeLocation;

	public StoreContract getStoreContract() {
		return storeContract;
	}

	public void setStoreContract(StoreContract storeContract) {
		this.storeContract = storeContract;
	}

	public StoreLocation getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(StoreLocation storeLocation) {
		this.storeLocation = storeLocation;
	}
	
	

}
