package com.wwyl.interceptor;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wwyl.ThreadLocalHolder;
import com.wwyl.entity.security.OperationLog;
import com.wwyl.entity.security.Operator;
import com.wwyl.service.security.OperationLogService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Created by fyunli on 15/1/12.
 */
public class OperationLogInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogInterceptor.class);

    @Resource
    OperationLogService operationLogService;

    /**
     * 默认忽略参数
     */
    private static final String[] DEFAULT_IGNORE_PARAMETERS = new String[]{"password", "rePassword", "currentPassword"};

    /**
     * 忽略参数
     */
    private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            Operator operater = ThreadLocalHolder.getCurrentOperator();
            if (operater == null) {
                return true; // no logging
            }

            String path = request.getServletPath();
            String ip = getRemoteIPAddress(request);

            StringBuffer parameter = new StringBuffer();
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (parameterMap != null) {
                for (Entry<String, String[]> entry : parameterMap.entrySet()) {
                    String parameterName = entry.getKey();
                    if (!ArrayUtils.contains(ignoreParameters, parameterName)) {
                        String[] parameterValues = entry.getValue();
                        if (parameterValues != null) {
                            for (String parameterValue : parameterValues) {
                                parameter.append(parameterName + " = " + parameterValue + "\n");
                            }
                        }
                    }
                }
            }

            OperationLog log = new OperationLog();
            log.setLogInfo("");// ignore property;
            log.setIpAddr(ip);
            log.setOperateTime(new Date());
            log.setOperation(path);
            log.setOperator(operater);
            log.setOperatorName(operater.getName());
            log.setOperatorUsername(operater.getUsername());
            log.setParams(parameter.toString());
            operationLogService.save(log);
        } catch (Exception ignore) {
            logger.warn(ignore.getMessage());
        }
        return true;
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

    /**
     * 设置忽略参数
     *
     * @return 忽略参数
     */
    public String[] getIgnoreParameters() {
        return ignoreParameters;
    }

    /**
     * 设置忽略参数
     *
     * @param ignoreParameters 忽略参数
     */
    public void setIgnoreParameters(String[] ignoreParameters) {
        this.ignoreParameters = ignoreParameters;
    }

}