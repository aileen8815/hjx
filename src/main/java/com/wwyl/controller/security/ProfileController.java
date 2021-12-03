package com.wwyl.controller.security;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wwyl.ThreadLocalHolder;
import com.wwyl.Enums.Sex;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.security.Operator;
import com.wwyl.service.security.SecurityService;

/**
 * 已登陆操作员显示、修改资料、密码
 * 
 * @author fyunli
 */
@Controller
@RequestMapping("/security/profile")
public class ProfileController extends BaseController {

	@Resource
	private SecurityService securityService;

	@RequestMapping(method = RequestMethod.GET)
	public String profile(ModelMap model) {
		Operator operator = ThreadLocalHolder.getCurrentOperator();
		model.addAttribute("operator", operator);
		return "/security/profile";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(ModelMap model) {
		Operator operator = ThreadLocalHolder.getCurrentOperator();
		model.addAttribute("operator", operator);
		model.addAttribute("sexes", Sex.values());
		return "/security/profile_form";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute("operator") @Valid Operator operator, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			this.printBindingResult(result);
			return "/security/profile_form";
		}

		Operator targetOperator = securityService.findOperatorById(ThreadLocalHolder.getCurrentOperator().getId());
		targetOperator.copyProfile(operator);
		securityService.saveOperator(targetOperator);
		model.addAttribute("operator", targetOperator);
		return "/security/profile";
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.GET)
	public String changePassword(ModelMap model) {
		return "security/change_password";
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public String changePwd(String oldPassword, String newPassword, ModelMap model) {
		Operator operator = securityService.findOperatorById(ThreadLocalHolder.getCurrentOperator().getId());
		if (!operator.validatePassword(oldPassword)) {
			model.addAttribute("message", "旧密码不正确！");
		} else {
			operator.flushPassword(newPassword);
			securityService.saveOperator(operator);
			model.addAttribute("message", "修改密码成功，请记住您的新密码！");
			model.addAttribute("success", true);
		}
		return "security/change_password";
	}

}
