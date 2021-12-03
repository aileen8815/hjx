package com.wwyl.controller.settings;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.CarrierType;
import com.wwyl.service.settings.CarrierTypeService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/carrier-type")
public class CarrierTypeController extends BaseController {

	@Autowired
	CarrierTypeService carrierTypeService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("carrierType") @Valid CarrierType carrierType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		carrierTypeService.save(carrierType);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<CarrierType> list() {
		return carrierTypeService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public CarrierType getCarrierType(@PathVariable Long id) {
		CarrierType carrierType = carrierTypeService.findOne(id);
		return carrierType;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		CarrierType carrierType = carrierTypeService.findOne(id);

		model.addAttribute("carrierType", carrierType);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("carrierType") @Valid CarrierType carrierType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		carrierTypeService.save(carrierType);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		carrierTypeService.delete(id);
		return indexRedirect;
	}

}
