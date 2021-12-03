package com.wwyl.controller.settings;

import java.util.List;
import java.util.Map;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.Carrier;
import com.wwyl.entity.settings.CarrierType;
import com.wwyl.service.settings.CarrierService;
import com.wwyl.service.settings.CarrierTypeService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/carrier")
public class CarrierController extends BaseController {

	@Autowired
	CarrierService carrierService;

	@Autowired
	CarrierTypeService carrierTypeService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		List<CarrierType> carrierTypes = carrierTypeService.findAll();
		model.addAttribute("carrierTypes", carrierTypes);

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("carrier") @Valid Carrier carrier, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			List<CarrierType> carrierTypes = carrierTypeService.findAll();
			model.addAttribute("carrierTypes", carrierTypes);

			return formPage;
		}
		carrierService.save(carrier);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "name", defaultValue = "") String name) {
		Page<Carrier> carriers = carrierService.findByCodeOrShortNameLike(page, rows, name);
		return toEasyUiDatagridResult(carriers);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Carrier getcarrier(@PathVariable Long id) {
		Carrier carrier = carrierService.findOne(id);
		return carrier;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		Carrier carrier = carrierService.findOne(id);
		List<CarrierType> carrierTypes = carrierTypeService.findAll();
		model.addAttribute("carrierTypes", carrierTypes);

		model.addAttribute("carrier", carrier);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("carrier") @Valid Carrier carrier, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			List<CarrierType> carrierTypes = carrierTypeService.findAll();
			model.addAttribute("carrierTypes", carrierTypes);

			return formPage;
		}
		carrierService.save(carrier);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		carrierService.delete(id);
		return indexRedirect;
	}

}
