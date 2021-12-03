package com.wwyl.controller.security;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.controller.BaseController;
import com.wwyl.entity.security.Role;
import com.wwyl.service.security.SecurityService;

/**
 * @author fyunli
 */
@Controller
@RequestMapping("/security/role")
public class RoleController extends BaseController {

	@Resource
	private SecurityService securityService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public List<Role> list() {
		return securityService.findAllRoles();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Role get(@PathVariable Long id) {
		return securityService.findRoleById(id);
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("_method", "post");
		model.addAttribute("role", new Role());
		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("role") @Valid Role role, Long[] perms, BindingResult result) {
		if (result.hasErrors()) {
			return formPage;
		}

		role.setRolePermissions(perms);

		securityService.saveRole(role);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		Role role = securityService.findRoleById(id);
		model.addAttribute("_method", "put");
		model.addAttribute("role", role);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("role") @Valid Role role, Long[] perms, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			this.printBindingResult(result);
			return formPage;
		}

		role.setRolePermissions(perms);

		securityService.saveRole(role);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		securityService.deleteRole(id);
		return indexRedirect;
	}

}
