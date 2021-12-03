package com.wwyl.controller.security;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Constants;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.security.Operator;
import com.wwyl.service.security.SecurityService;
import com.wwyl.util.RandomGenerator;

/**
 * @author fyunli
 */
@Controller
public class AccessController extends BaseController {

	@Resource
	private SecurityService securityService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpSession session) {
		this.renewSession(request, session);
		return "/login";
	}

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> login(String username, String password, HttpServletRequest request, HttpSession session) {
		try {
			Operator operator = securityService.login(username, password, getRemoteIPAddress(request));

			String frog = RandomGenerator.randomString(32, true);
			String secretKey = RandomGenerator.randomString(32, true);
			securityService.cacheFrog(frog, secretKey, operator);

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("error", 0L);
			result.put("frog", frog);
			result.put("secretKey", secretKey);
			session.setAttribute(Constants.SESSION_OPERATOR_KEY, operator);
			return result;
		} catch (Exception e) {
			return this.printMessage(e);
		}
	}

	@RequestMapping(value = "/signout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpSession session) {
		if (session.getAttribute(Constants.SESSION_OPERATOR_KEY) != null) {
			this.renewSession(request, session);
		}
		return "redirect:/login";
	}

	@RequestMapping(value = "/get-token", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getToken(String frog, String signature) {
		try {
			if (!securityService.validateFrog(frog, signature)) {
				return this.printMessage(1, "无效的请求");
			}

			String authToken = RandomGenerator.randomString(32, true);
			Operator operator = securityService.cacheAuthToken(frog, authToken);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("error", 0L);
			result.put("authToken", authToken);
			result.put("loginOperator", operator);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return this.printMessage(e);
		}
	}
}
