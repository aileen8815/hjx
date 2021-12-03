package com.wwyl.controller.security;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Enums.LockStatus;
import com.wwyl.Enums.Sex;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.security.OperationLog;
import com.wwyl.entity.security.Operator;
import com.wwyl.service.security.OperationLogService;
import com.wwyl.service.security.SecurityService;
import com.wwyl.service.settings.DepartmentService;

/**
 * @author fyunli
 */
@Controller
@RequestMapping("/security/operator")
public class OperatorController extends BaseController {

	@Resource
	private SecurityService securityService;
	@Resource
	private DepartmentService departmentService;
	@Resource
	private OperationLogService operationLogService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public List<Operator> list(@RequestParam(value = "username", defaultValue = "") String username) {
		List<Operator> operators = securityService.findOperatorByUsernameLike(username);
		return operators;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Operator get(@PathVariable Long id) {
		return securityService.findOperatorById(id);
	}

	@RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
	public String view(@PathVariable Long id, ModelMap model) {
		Operator operator = securityService.findOperatorById(id);
		model.addAttribute("operator", operator);
		return viewPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("_method", "post");
		Operator operator = new Operator();
		operator.setSex(Sex.男);
		prepareInput(model, operator);
		return formPage;
	}

	private void prepareInput(ModelMap model, Operator operator) {
		model.addAttribute("operator", operator);
		model.addAttribute("sexes", Sex.values());
		model.addAttribute("departments", departmentService.findAll());
		model.addAttribute("roles", securityService.findAllRoles());
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("operator") @Valid Operator operator, Long[] assignRoles, BindingResult result) {
		if (result.hasErrors()) {
			this.printBindingResult(result);
			return formPage;
		}

		// 设置新用户初始信息
		operator.setRegisterTime(new Date());
		operator.setLockStatus(LockStatus.正常);

		// 处理密码并加盐
		operator.flushPassword(operator.getPassword());
		operator.setOperatorRoles(assignRoles);

		securityService.saveOperator(operator);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		Operator operator = securityService.findOperatorById(id);
		model.addAttribute("_method", "put");
		prepareInput(model, operator);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("operator") @Valid Operator operator, Long[] assignRoles, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			prepareInput(model, operator);
			this.printBindingResult(result);
			return formPage;
		}

		Operator targetOperator = securityService.findOperatorById(operator.getId());
		targetOperator.copyProfile(operator);
		targetOperator.setOperatorRoles(assignRoles);

		securityService.saveOperator(targetOperator);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		if (id > 1) {// 不允许删除 id=1('admin')
			securityService.deleteOperator(id);
		}
		return indexRedirect;
	}

	@RequestMapping(value = "/valid-username", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity validUsername(String oldUsername, String username) {
		if (StringUtils.equals(oldUsername, username)) {
			return new ResponseEntity(HttpStatus.OK);
		}

		if (securityService.isUsernameExists(username)) {
			return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/toggle-lock", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> toggleLock(@PathVariable Long id) {
		Operator operator = securityService.findOperatorById(id);
		operator.toggleLock();
		securityService.saveOperator(operator);
		return this.printMessage(0);
	}
	@RequestMapping(value = "/log-index", method = RequestMethod.GET)
	public String logindex(ModelMap model) {
		List<Operator>  operators=securityService.findAllOperators();
		model.put("operators", operators);
		return "/security/operation_log";
	}
	@RequestMapping(value = "/list-log", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			  Long operationId, Date startTime,Date  endTime) {
		Page<OperationLog> operators  = operationLogService.findOperationLogs(operationId, startTime, endTime,page, rows);
		return toEasyUiDatagridResult(operators);
	}
 
}
