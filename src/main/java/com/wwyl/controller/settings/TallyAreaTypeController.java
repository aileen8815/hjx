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
import com.wwyl.entity.settings.TallyAreaType;
import com.wwyl.service.settings.TallyAreaTypeService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/tally-area-type")
public class TallyAreaTypeController extends BaseController {

	@Autowired
	TallyAreaTypeService tallyAreaTypeService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("tallyAreaType") @Valid TallyAreaType tallyAreaType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return formPage;
		}
		tallyAreaTypeService.save(tallyAreaType);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public  List<TallyAreaType> list() {
		return tallyAreaTypeService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public TallyAreaType gettallyAreaType(@PathVariable Long id) {
		TallyAreaType tallyAreaType = tallyAreaTypeService.findOne(id);
		return tallyAreaType;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		TallyAreaType tallyAreaType = tallyAreaTypeService.findOne(id);
		model.addAttribute("tallyAreaType", tallyAreaType);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("tallyAreaType") @Valid TallyAreaType tallyAreaType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return formPage;
		}
		tallyAreaTypeService.save(tallyAreaType);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		tallyAreaTypeService.delete(id);
		return indexRedirect;
	}

}
