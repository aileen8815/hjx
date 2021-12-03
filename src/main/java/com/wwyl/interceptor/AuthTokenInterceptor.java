package com.wwyl.interceptor;

import com.wwyl.Constants;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.entity.security.Operator;
import com.wwyl.service.security.AccessInterceptConfig;
import com.wwyl.service.security.SecurityService;
import com.wwyl.util.MD5Encryption;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author fyunli
 */
public class AuthTokenInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthTokenInterceptor.class);

	@Resource
	private SecurityService securityService;
	@Resource
	private AccessInterceptConfig accessInterceptConfig;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Operator operator = (Operator) session
				.getAttribute(Constants.SESSION_OPERATOR_KEY);

		String path = request.getRequestURI().replace(request.getContextPath(),
				"");
		if (logger.isDebugEnabled()) {
			logger.debug("intercept(ActionInvocation) - {}", "path: " + path); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (operator == null) {
			try {
				operator = this.getOperatorByAuthToken(request);
			} catch (Exception e) {
				request.getRequestDispatcher("/error?error=" + e.getMessage())
						.forward(request, response);
				return false;
			}
		}

		ThreadLocalHolder.setCurrentOperator(operator);
		logger.debug("auth operator for client is {}", ThreadLocalHolder
				.getCurrentOperator().getUsername());

		if (!accessInterceptConfig.checkPermission(path, operator)) {
			response.addHeader("loginStatus", "accessDenied");
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}

		return super.preHandle(request, response, handler);
	}

	private Operator getOperatorByAuthToken(HttpServletRequest request) {
		String authToken = request.getParameter("authToken");
		if (logger.isDebugEnabled()) {
			logger.debug("intercept(ActionInvocation) authToken - {}",
					authToken);
		}

		if (StringUtils.isBlank(authToken)) {
			if (logger.isDebugEnabled()) {
				logger.debug("intercept(ActionInvocation) - invalid authToken");
			}
			throw new RuntimeException("无效的请求");
		}

		try {
			String nonce = request.getParameter("nonce");
			if (logger.isDebugEnabled()) {
				logger.debug("intercept(ActionInvocation) nonce - {}", nonce);
			}

			// 特殊处理数据同步使用的特别TOKEN和KEY
			String signature = request.getParameter("signature");
			if (Constants.FAKE_AUTH_TOKEN.equals(authToken)) {
				if (!this.checkSignature(nonce, authToken, signature)) {
					if (logger.isDebugEnabled()) {
						logger.debug("intercept(ActionInvocation) - invalid signature");
					}
					throw new RuntimeException("无效的请求");
				}
				String username = request.getParameter("username");
				return securityService.findOperatorByUsername(username);
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("intercept(ActionInvocation) signature - {}",
							signature);
				}

				if (StringUtils.isBlank(authToken)
						|| StringUtils.isBlank(signature)
						|| !securityService.validateAuthToken(authToken)) {
					if (logger.isDebugEnabled()) {
						logger.debug("intercept(ActionInvocation) - invalid authToken");
					}
					throw new RuntimeException("无效的请求");
				}

				if (!this.checkSignature(nonce, authToken, signature)) {
					if (logger.isDebugEnabled()) {
						logger.debug("intercept(ActionInvocation) - invalid signature");
					}
					throw new RuntimeException("无效的请求");
				}

				// 刷新缓存，保持会话
				return securityService.lengthenAuthToken(authToken);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("无效的请求");
		}
	}

	private boolean checkSignature(String nonce, String authToken,
			String signature) throws IOException {
		String secketKey = null;
		if (!StringUtils.equals(Constants.FAKE_AUTH_TOKEN, authToken)) {
			secketKey = securityService.getSecketKeyByAuthToken(authToken);
		} else {
			secketKey = Constants.FAKE_SECRET_KEY;
		}
		String unSignString = secketKey + "authToken" + authToken + "nonce"
				+ nonce;
		String hash = MD5Encryption.encode(unSignString);
		if (logger.isDebugEnabled()) {
			logger.debug(
					"intercept(ActionInvocation) - {}", unSignString + " ---- " + hash); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return StringUtils.equals(signature, hash);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		request.setAttribute("servletPath", request.getRequestURI());
		ThreadLocalHolder.setCurrentOperator(null);
	}

}
