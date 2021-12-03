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
import com.wwyl.entity.settings.StoreContainerType;
import com.wwyl.service.settings.StoreContainerTypeService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/store-container-type")
public class StoreContainerTypeController extends BaseController {

	@Autowired
	StoreContainerTypeService storeContainerTypeService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("storeContainerType") @Valid StoreContainerType storeContainerType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return formPage;
		}
		storeContainerTypeService.save(storeContainerType);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<StoreContainerType> list() {
		return storeContainerTypeService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public StoreContainerType getstoreContainerType(@PathVariable Long id) {
		StoreContainerType storeContainerType = storeContainerTypeService.findOne(id);
		return storeContainerType;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		StoreContainerType storeContainerType = storeContainerTypeService.findOne(id);

		model.addAttribute("storeContainerType", storeContainerType);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("storeContainerType") @Valid StoreContainerType storeContainerType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		storeContainerTypeService.save(storeContainerType);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		storeContainerTypeService.delete(id);
		return indexRedirect;
	}

}
