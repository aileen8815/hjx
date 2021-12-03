package com.wwyl.controller.settings;

import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.StockWastageType;
import com.wwyl.service.settings.StockWastageTypeService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/stock-wastage-type")
public class StockWastageTypeController extends BaseController {

	@Resource
	StockWastageTypeService stockWastageTypeService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("stockWastageType") @Valid StockWastageType stockWastageType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		stockWastageTypeService.save(stockWastageType);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<StockWastageType> list() {
		return stockWastageTypeService.findAll();
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		StockWastageType stockWastageType = stockWastageTypeService.findOne(id);
		model.addAttribute("stockWastageType",stockWastageType);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("stockWastageType") @Valid StockWastageType stockWastageType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return formPage;
		}
		stockWastageTypeService.save(stockWastageType);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		stockWastageTypeService.delete(id);
		return indexRedirect;
	}
}
