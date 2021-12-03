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
import com.wwyl.entity.settings.StockAdjustType;
import com.wwyl.service.settings.StockAdjustTypeService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/stock-adjust-type")
public class StockAdjustTypeController extends BaseController {

	@Autowired
	StockAdjustTypeService stockAdjustTypeService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("stockAdjustType") @Valid StockAdjustType stockAdjustType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		stockAdjustTypeService.save(stockAdjustType);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<StockAdjustType> list() {
		return stockAdjustTypeService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public StockAdjustType getstockAdjustType(@PathVariable Long id) {
		StockAdjustType stockAdjustType = stockAdjustTypeService.findOne(id);
		return stockAdjustType;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		StockAdjustType stockAdjustType = stockAdjustTypeService.findOne(id);

		model.addAttribute("stockAdjustType", stockAdjustType);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("stockAdjustType") @Valid StockAdjustType stockAdjustType, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		stockAdjustTypeService.save(stockAdjustType);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		stockAdjustTypeService.delete(id);
		return indexRedirect;
	}

}
