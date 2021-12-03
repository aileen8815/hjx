package com.wwyl.controller.truck;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.controller.BaseController;
import com.wwyl.entity.truck.ResourceManagement;
import com.wwyl.service.settings.VehicleTypeService;
import com.wwyl.service.truck.ResourceManagementService;

@Controller
@RequestMapping("/truck/resource-management")
public class ResourceManagementController extends BaseController {

	@Resource
	private ResourceManagementService resourceManagementService;

	@Resource
	private VehicleTypeService vehicleTypeService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public List<ResourceManagement> list() {
		return resourceManagementService.findAll();
	}

	// 创建资源信息
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("_method", "post");
		model.addAttribute("resourceManagement", new ResourceManagement());
		model.addAttribute("vehicleTypes", vehicleTypeService.findAll());
		return formPage;
	}

	// 保存资源信息
	@RequestMapping(method = RequestMethod.POST)
	public String create(
			@ModelAttribute("resourceManagement") @Valid ResourceManagement resourceManagement,
			BindingResult result) {
		if (result.hasErrors()) {
			return formPage;
		}
		resourceManagementService.save(resourceManagement);
		return indexRedirect;
	}

	// 修改资源页面
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		ResourceManagement resourceManagement = resourceManagementService
				.findOne(id);
		model.addAttribute("_method", "put");
		model.addAttribute("resourceManagement", resourceManagement);
		model.addAttribute("vehicleTypes", vehicleTypeService.findAll());
		return formPage;
	}

	// 保存修改信息
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(
			@ModelAttribute("resourceManagement") @Valid ResourceManagement resourceManagement,
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			List<ObjectError> errros = result.getAllErrors();
			for (ObjectError err : errros) {
				System.out.println(err.getDefaultMessage());
			}
			return formPage;
		}

		resourceManagementService.save(resourceManagement);
		return indexRedirect;
	}

	// 删除运力资源信息
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		resourceManagementService.delete(id);
		return indexRedirect;
	}

}
