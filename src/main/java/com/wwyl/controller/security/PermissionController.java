package com.wwyl.controller.security;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.controller.BaseController;
import com.wwyl.entity.security.Permission;
import com.wwyl.service.security.SecurityService;

/**
 * @author fyunli
 */
@Controller
@RequestMapping("/security/permission")
public class PermissionController extends BaseController {

	@Resource
	private SecurityService securityService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "tree", method = RequestMethod.GET)
	@ResponseBody
	public List<Permission> tree() {
		List<Permission> rootPermissions = securityService.findRootPermissions();
		return rootPermissions;
	}

}
