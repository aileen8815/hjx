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

import com.wwyl.Enums;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.MeasureUnit;
import com.wwyl.service.settings.MeasureUnitService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/measure-unit")
public class MeasureUnitController extends BaseController {

	@Autowired
	MeasureUnitService measureUnitService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("measureUnitTypes", Enums.MeasureUnitType.values());
		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("measureUnit") @Valid MeasureUnit measureUnit, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("measureUnitTypes", Enums.MeasureUnitType.values());
			return formPage;
		}
		measureUnitService.save(measureUnit);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<MeasureUnit> list() {
		return measureUnitService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public MeasureUnit getmeasureUnit(@PathVariable Long id) {
		MeasureUnit measureUnit = measureUnitService.findOne(id);
		return measureUnit;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		MeasureUnit measureUnit = measureUnitService.findOne(id);
		model.addAttribute("measureUnitTypes", Enums.MeasureUnitType.values());
		model.addAttribute("measureUnit", measureUnit);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("measureUnit") @Valid MeasureUnit measureUnit, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("measureUnitTypes", Enums.MeasureUnitType.values());
			return formPage;
		}
		measureUnitService.save(measureUnit);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		measureUnitService.delete(id);
		return indexRedirect;
	}

}
