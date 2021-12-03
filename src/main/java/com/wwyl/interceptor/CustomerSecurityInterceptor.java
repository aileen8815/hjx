package com.wwyl.interceptor;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wwyl.Constants;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Customer;
import com.wwyl.service.security.AccessInterceptConfig;

/**
 * @author fyunli
 */
public class CustomerSecurityInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CustomerSecurityInterceptor.class);

    @Resource
    private AccessInterceptConfig accessInterceptConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute(Constants.SESSION_CUSTOMER_KEY);

        String path = request.getRequestURI().replace(request.getContextPath(), "");
        if (logger.isDebugEnabled()) {
            logger.debug("intercept(ActionInvocation) - {}", "path: " + path); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (customer == null) {
            response.sendRedirect(request.getContextPath() + "/customer/login");
            return false;
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        request.setAttribute("servletPath", request.getRequestURI());
    }


}
