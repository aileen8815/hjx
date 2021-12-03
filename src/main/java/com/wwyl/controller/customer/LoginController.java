package com.wwyl.controller.customer;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.myemsp.security.Des;

import com.wwyl.Constants;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.Customer;
import com.wwyl.service.settings.CustomerService;

/**客户登录
 * @author jianl
 */
@Controller
@RequestMapping("/customer/login")
public class LoginController extends BaseController {

	@Resource
	private CustomerService customerService;

	@RequestMapping(method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpSession session) {
		session.removeAttribute(Constants.SESSION_CUSTOMER_KEY);
		this.renewSession(request, session);
		return indexPage;
	}

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> signin( String custemerId,String passWord ,ModelMap model,HttpServletRequest request, HttpSession session) {
		String desPass ="";
		try {
			desPass = Des.encrypt(passWord, Des.KEY);
		} catch (Exception e) {
			 
			e.printStackTrace();
		}
		Customer customer = customerService.findByCustomerIdAndPassword(custemerId,desPass);
		/*if (Constants.WS_WEBSERVICE_INTERFACE) {
			if (customer == null) {
				customer = customerService.getCustomerInfo(custemerId, null,
						passWord, null);
				if (customer != null) {
					customerService.save(customer);
				}

			}
		}*/
		Map<String, Object> result = new HashMap<String, Object>();
		if(customer!=null){
			result.put("error", 0L);
			session.setAttribute(Constants.SESSION_CUSTOMER_KEY, customer);
			 
		}else{
			result.put("error", 1L);
			result.put("message", "登录失败，用户不可用或用户名密码错误");
			 
		}
		return result;
	 
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpSession session) {
		session.removeAttribute(Constants.SESSION_CUSTOMER_KEY);
		this.renewSession(request, session);
		return "redirect:/customer/login";
	}
}
