package com.wwyl.entity.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by fyunli on 14-6-16.
 */
@Entity
@Table(name = "TJ_STOCK_WASTAGE_TYPE")
@Cacheable
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "new"})
public class StockWastageType extends PersistableEntity {

    @NotBlank
    @Column(length = 20, nullable = false, unique = true)
    private String code;
    @NotBlank
    @Column(length = 100, nullable = false, unique = true)
    private String name;

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

}
