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
import com.wwyl.entity.settings.ProductStatus;
import com.wwyl.service.settings.ProductStatusService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/product-status")
public class ProductStatusController extends BaseController {

	@Autowired
	ProductStatusService productStatusService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("productStatus") @Valid ProductStatus productStatus, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		productStatusService.save(productStatus);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<ProductStatus> list() {
		return productStatusService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public ProductStatus getproductStatus(@PathVariable Long id) {
		ProductStatus productStatus = productStatusService.findOne(id);
		return productStatus;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		ProductStatus productStatus = productStatusService.findOne(id);

		model.addAttribute("productStatus", productStatus);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("productStatus") @Valid ProductStatus productStatus, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		productStatusService.save(productStatus);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		productStatusService.delete(id);
		return indexRedirect;
	}

}
