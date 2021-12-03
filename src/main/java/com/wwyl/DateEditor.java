package com.wwyl;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * 日期类型转换
 * 
 * @version 3.0
 */
public class DateEditor extends PropertyEditorSupport {

	private String dateFormat = Constants.DEFAULT_DATE_FORMAT;

	private boolean emptyAsNull;

	public DateEditor(boolean emptyAsNull) {
		this.emptyAsNull = emptyAsNull;
	}

	public DateEditor(boolean emptyAsNull, String dateFormat) {
		this.emptyAsNull = emptyAsNull;
		this.dateFormat = dateFormat;
	}

	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return value != null ? new SimpleDateFormat(dateFormat).format(value) : "";
	}

	@Override
	public void setAsText(String text) {
		if (text == null) {
			setValue(null);
		} else {
			String value = text.trim();
			if (emptyAsNull && "".equals(value)) {
				setValue(null);
			} else {
				try {
					setValue(DateUtils.parseDate(value, Constants.DATE_PATTERNS));
				} catch (ParseException e) {
					setValue(null);
				}
			}
		}
	}

}