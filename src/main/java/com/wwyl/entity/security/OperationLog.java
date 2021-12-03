package com.wwyl.entity.security;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wwyl.entity.PersistableEntity;

/**
 * 操作日志
 * 
 * @author fyunli
 */
@Entity
@Table(name = "TJ_OPERATION_LOG")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "new" })
public class OperationLog extends PersistableEntity {

	@NotNull
	@ManyToOne
	private Operator operator;// 操作人
	@Temporal(TemporalType.TIMESTAMP)
	private Date operateTime;// 操作时间
	@Column(length = 100)
	private String operation;// 操作
	@NotBlank
	@Column(length = 2000)
	private String logInfo;// 日志内容
	@Column(length = 100)
	private String operatorUsername;// 操作人用户名
	@Column(length = 100)
	private String operatorName;// 操作人名字
	@Column(length = 100)
	private String ipAddr;// IP地址
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String params;// 操作参数

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operateType) {
		this.operation = operateType;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public String getOperatorUsername() {
		return operatorUsername;
	}

	public void setOperatorUsername(String operatorUsername) {
		this.operatorUsername = operatorUsername;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
