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
import com.wwyl.entity.settings.Packing;
import com.wwyl.service.settings.PackingService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/packing")
public class PackingController extends BaseController {

	@Autowired
	PackingService packingService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("packing") @Valid Packing packing, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		packingService.save(packing);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<Packing> list() {
		return packingService.findAll();
	}
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		Packing packing = packingService.findOne(id);
		model.addAttribute("packing",packing);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("packing") @Valid Packing packing, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			
			return formPage;
		}
		packingService.save(packing);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		packingService.delete(id);
		return indexRedirect;
	}
}
