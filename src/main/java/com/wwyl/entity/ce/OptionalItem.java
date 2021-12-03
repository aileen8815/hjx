package com.wwyl.entity.ce;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.wwyl.entity.PersistableEntity;

/**
 * @author Administrator
 */
@MappedSuperclass
public abstract class OptionalItem extends PersistableEntity {
	private String itemName; //属性访问方法名，即获取属性的getter方法名
	private String itemTitle; //描述名称
	private String itemType; //规则项(rule)；计算项(calculation)；条件项(condition)
	private String hostName;//选项作为属性所来自的对象。如果来自计算对象，则为entity；否则，应来自计算对象的某个对象属性，此值为获取该对象属性的getter方法名。
	private String valueType;//数值型(digit), 日期型(date), 对象型(object)
	
	private String refEntity;
	private String refName;
	private String refSource;
	
	@Column(length = 50, nullable = false, unique = false)
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	
	@Column(length = 50)
	public String getRefEntity() {
		return refEntity;
	}
	public void setRefEntity(String refEntity) {
		this.refEntity = refEntity;
	}
	
	@Column(length = 50)
	public String getRefName() {
		return refName;
	}
	public void setRefName(String refName) {
		this.refName = refName;
	}
	
	@Column(length = 200)
	public String getRefSource() {
		return refSource;
	}
	public void setRefSource(String refSource) {
		this.refSource = refSource;
	}
	
	
}
