package com.wwyl.controller.settings;

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
import com.wwyl.entity.settings.VehicleType;
import com.wwyl.service.settings.VehicleTypeService;

@Controller
@RequestMapping("/settings/vehicle-type")
public class VehicleTypeController extends BaseController {
	@Resource
	private VehicleTypeService vehicleTypeService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public List<VehicleType> list() {
		return vehicleTypeService.findAll();
	}

	// 创建车辆信息
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("_method", "post");
		model.addAttribute("vehicleType", new VehicleType());
		return formPage;
	}

	// 保存车辆信息
	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("vehicleType") @Valid VehicleType vehicleType, BindingResult result) {
		if (result.hasErrors()) {
			return formPage;
		}
		vehicleTypeService.save(vehicleType);
		return indexRedirect;
	}

	// 修改车辆信息页面
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		VehicleType vehicleType = vehicleTypeService.findOne(id);
		model.addAttribute("_method", "put");
		model.addAttribute("vehicleType", vehicleType);
		return formPage;
	}

	// 保存修改信息
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("vehicleType") @Valid VehicleType vehicleType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			List<ObjectError> errros = result.getAllErrors();
			for (ObjectError err : errros) {
				System.out.println(err.getDefaultMessage());
			}
			return formPage;
		}

		vehicleTypeService.save(vehicleType);
		return indexRedirect;
	}

	// 删除车辆信息
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		vehicleTypeService.delete(id);
		return indexRedirect;
	}

}
