package com.wwyl.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import freemarker.template.utility.StringUtil;

/**
 * wrap a HttpServletRequest, encode it's parameters
 * 
 * @author Fyun Li
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getQueryString() {
		String queryString = super.getQueryString();
		if (StringUtils.isBlank(queryString)) {
			return queryString;
		}
		return cleanXSS(queryString).replaceAll("&amp;", "&");
	}

	@Override
	public String getParameter(String key) {
		String value = super.getParameter(key);
		if (value != null) {
			value = cleanXSS(value);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Map getParameterMap() {
		Map map = super.getParameterMap();
		if (MapUtils.isEmpty(map)) {
			return map;
		}

		Map paramMap = new HashMap(super.getParameterMap());
		for (Object key : map.keySet()) {
			Object values = map.get(key);
			if (values != null) {
				for (String value : (String[]) values) {
					value = cleanXSS((String) value);
				}
			}

			paramMap.remove(key);
			paramMap.put(cleanXSS(key.toString()), values);
		}
		return paramMap;
	}

	@Override
	public String[] getParameterValues(String key) {
		String[] value = super.getParameterValues(key);
		if (value != null) {
			for (int i = 0; i < value.length; i++) {
				value[i] = cleanXSS(value[i]);
			}
		}
		return value;
	}

	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value != null) {
			value = cleanXSS(value);
		}
		return value;
	}

	private String cleanXSS(String value) {
		if (StringUtils.isBlank(value)) {
			return value;
		}
		return StringEscapeUtils.escapeHtml4(value);
	}

}
