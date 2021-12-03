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
import com.wwyl.entity.settings.StoreLocationType;
import com.wwyl.service.settings.StoreLocationTypeService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/store-location-type")
public class StoreLocationTypeController extends BaseController {

	@Autowired
	StoreLocationTypeService storeLocationTypeService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("storeLocationType") @Valid StoreLocationType storeLocationType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		storeLocationTypeService.save(storeLocationType);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<StoreLocationType> list() {
		return storeLocationTypeService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public StoreLocationType getstoreLocationType(@PathVariable Long id) {
		StoreLocationType storeLocationType = storeLocationTypeService.findOne(id);
		return storeLocationType;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		StoreLocationType storeLocationType = storeLocationTypeService.findOne(id);

		model.addAttribute("storeLocationType", storeLocationType);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("storeLocationType") @Valid StoreLocationType storeLocationType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		storeLocationTypeService.save(storeLocationType);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		storeLocationTypeService.delete(id);
		return indexRedirect;
	}

}
