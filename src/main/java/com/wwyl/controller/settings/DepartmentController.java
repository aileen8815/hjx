package com.wwyl.controller.settings;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.Department;
import com.wwyl.service.settings.DepartmentService;

/**
 * @author fyunli
 */
@Controller
@RequestMapping("/settings/department")
public class DepartmentController extends BaseController {

	@Resource
	private DepartmentService departmentService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "tree", method = RequestMethod.GET)
	@ResponseBody
	public List<Department> tree() {
		return departmentService.findRootDepartments();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Department get(@PathVariable Long id) {
		return departmentService.findOne(id);
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model, Long parentId) {
		Department department = new Department();
		if (parentId != null && parentId > 0) {
			Department parent = departmentService.findOne(parentId);
			department.setParent(parent);
		}
		model.addAttribute("_method", "post");
		model.addAttribute("department", department);
		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("department") @Valid Department department, BindingResult result) {
		if (result.hasErrors()) {
			return formPage;
		}
		departmentService.save(department);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		Department department = departmentService.findOne(id);
		model.addAttribute("_method", "put");
		model.addAttribute("department", department);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("department") @Valid Department department, String perms, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			this.printBindingResult(result);
			return formPage;
		}

		departmentService.save(department);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		departmentService.delete(id);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/organise", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable Long id, Long parentId, String point) {
		Department department = departmentService.findOne(id);
		if ("append".equals(point)) {
			Department parent = departmentService.findOne(parentId);
			department.setParent(parent);
		} else {
			department.setParent(null);
		}
		try {
			departmentService.save(department);
			return printMessage(0);
		} catch (Exception e) {
			return printMessage(1, e.getMessage());
		}
	}

	@RequestMapping(value = "/valid-code", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity validCode(String oldCode, String code) {
		if (StringUtils.equals(oldCode, code)) {
			return new ResponseEntity(HttpStatus.OK);
		}

		if (departmentService.exists(code)) {
			return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity(HttpStatus.OK);
	}
}
