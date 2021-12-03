package com.wwyl.controller;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.google.common.base.CaseFormat;
import com.wwyl.Constants;
import com.wwyl.DateEditor;
import com.wwyl.dao.DynamicSpecifications;
import com.wwyl.dao.PropertyFilter;

/**
 * @author fyunli
 */
public class BaseController {

    protected String subPackage = getClass().getPackage().getName().replaceAll("com.wwyl.controller.", "");
    protected String lowerUnderscoreEntityName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, getClass().getSimpleName().replace("Controller", ""));
    protected String indexPage = "/" + subPackage + "/" + lowerUnderscoreEntityName;
    protected String formPage = "/" + subPackage + "/" + lowerUnderscoreEntityName + "_form";
    protected String viewPage = "/" + subPackage + "/" + lowerUnderscoreEntityName + "_view";
    protected String indexRedirect = "redirect:/" + subPackage + "/" + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, lowerUnderscoreEntityName);

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static Map<String, Object> toEasyUiDatagridResult(Page persons) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", persons.getTotalElements());
        result.put("rows", persons.getContent());
        return result;
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     * <p/>
     * 返回的结果的Parameter名已去除前缀.
     */
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Assert.notNull(request, "Request must not be null");
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();

        prefix = (prefix == null) ? "" : prefix;

        while ((paramNames != null) && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if ((values == null) || (values.length == 0)) {
                    // Do nothing
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    /* 约定查询参数遵守 search_fieldName_MatchType 格式，如 search_lastName_EQ */
    public static List<PropertyFilter> parseSearchParams(ServletRequest request) {
        Map<String, Object> searchParams = BaseController.getParametersStartingWith(request, "search_");
        return DynamicSpecifications.parseSearchParams(searchParams);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
        binder.registerCustomEditor(Date.class, new DateEditor(true));
    }

    protected void printBindingResult(BindingResult result) {
        List<ObjectError> errros = result.getAllErrors();
        for (ObjectError err : errros) {
            logger.debug(err.getDefaultMessage());
        }
    }

    /**
     * 向页面输出信息，约定error:0表示表示操作成功，error:1表示操作失败，message为对应的信息内容
     */
    protected Map<String, Object> printMessage(int error, String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("error", error);
        map.put("message", message);
        return map;
    }

    protected Map<String, Object> printMessage(int error) {
        return printMessage(error, null);
    }

    protected Map<String, Object> printMessage(Exception e) {
        return printMessage(1, e.getMessage());
    }

    public static String getRemoteIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return StringUtils.substringBefore(ip, ",");
    }

    protected void renewSession(HttpServletRequest request, HttpSession session) {
        session.removeAttribute(Constants.SESSION_OPERATOR_KEY);
        session.invalidate();
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
            }
        }
        request.getSession();
    }

}
