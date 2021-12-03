package com.wwyl.entity.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 货权转移货位选择
 *
 * Created by fyunli on 14-6-10.
 */
@Entity
@Table(name = "TJ_STOCK_OWNER_CHANGE_CHK_ITEM")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class StockOwnerChangeCheckItem extends PersistableEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private StockOwnerChange stockOwnerChange;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreLocation storeLocation;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreContainer storeContainer;

    public StockOwnerChange getStockOwnerChange() {
        return stockOwnerChange;
    }

    public void setStockOwnerChange(StockOwnerChange stockOwnerChange) {
        this.stockOwnerChange = stockOwnerChange;
    }

    public StoreLocation getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(StoreLocation storeLocation) {
        this.storeLocation = storeLocation;
    }

    public StoreContainer getStoreContainer() {
        return storeContainer;
    }

    public void setStoreContainer(StoreContainer storeContainer) {
        this.storeContainer = storeContainer;
    }
}
