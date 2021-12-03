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
import com.wwyl.entity.settings.ChargeType;
import com.wwyl.service.settings.ChargeTypeService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/charge-type")
public class ChargeTypeController extends BaseController {

	@Autowired
	ChargeTypeService chargeTypeService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("chargeType") @Valid ChargeType chargeType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		chargeTypeService.save(chargeType);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<ChargeType> list() {
		return chargeTypeService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public ChargeType getchargeType(@PathVariable Long id) {
		ChargeType chargeType = chargeTypeService.findOne(id);
		return chargeType;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		ChargeType chargeType = chargeTypeService.findOne(id);

		model.addAttribute("chargeType", chargeType);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("chargeType") @Valid ChargeType chargeType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		chargeTypeService.save(chargeType);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		chargeTypeService.delete(id);
		return indexRedirect;
	}

}
