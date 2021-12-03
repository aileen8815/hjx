package com.wwyl.entity.settings;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.Enums.TransactionType;
import com.wwyl.entity.PersistableEntity;

/**
 * 各业务单据编号
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_SERIAL_NUMBER")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class SerialNumber extends PersistableEntity {

	@Basic
	private TransactionType transactionType;// 类型
	@Column(length = 50)
	private String prefix;// 编号前缀
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date serialDate; // 流水日期
	@Basic
	private Integer maxNumber;// 可用值
	@NotNull
	@Column(length = 10)
	private String dateFormat;
	@Basic
	private int length;

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Date getSerialDate() {
		return serialDate;
	}

	public void setSerialDate(Date serialDate) {
		this.serialDate = serialDate;
	}

	public Integer getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(Integer maxNumber) {
		this.maxNumber = maxNumber;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
