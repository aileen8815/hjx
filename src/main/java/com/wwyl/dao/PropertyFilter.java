package com.wwyl.dao;

/**
 * PropertyFilter用于一些简单的条件查询场景
 * 
 * @author fyunli
 */
public class PropertyFilter {

	public enum MatchType {
		EQ, LIKE, LT, GT, LE, GE, BETWEEN, IN, ISNULL, ISNOTNULL
	}

	public enum ConjunctionType {
		AND, OR
	}

	private String fieldName;
	private Object value;
	private MatchType matchType;

	public PropertyFilter(String fieldName, Object value) {
		this(fieldName, value, MatchType.EQ);
	}

	public PropertyFilter(String fieldName, Object value, MatchType matchType) {
		this.fieldName = fieldName;
		this.value = value;
		this.matchType = matchType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getValue() {
		return value;
	}

	public MatchType getMatchType() {
		return matchType;
	}

}
