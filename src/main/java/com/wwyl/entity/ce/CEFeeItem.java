package com.wwyl.entity.ce;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.ce.FeeItem;

/**
 * @author sjwang
 */
@Entity
@Table(name = "TJ_CE_FEE_ITEM")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class CEFeeItem extends FeeItem {


}
