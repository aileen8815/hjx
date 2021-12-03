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
import com.wwyl.entity.settings.TaxType;
import com.wwyl.service.settings.TaxTypeService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/tax-type")
public class TaxTypeController extends BaseController {

	@Autowired
	TaxTypeService taxTypeService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("taxType") @Valid TaxType taxType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return formPage;
		}
		taxTypeService.save(taxType);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<TaxType> list() {
		return taxTypeService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public TaxType gettaxType(@PathVariable Long id) {
		TaxType taxType = taxTypeService.findOne(id);
		return taxType;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		TaxType taxType = taxTypeService.findOne(id);

		model.addAttribute("taxType", taxType);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("taxType") @Valid TaxType taxType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		taxTypeService.save(taxType);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		taxTypeService.delete(id);
		return indexRedirect;
	}

}
