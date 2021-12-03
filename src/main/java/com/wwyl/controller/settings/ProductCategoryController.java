package com.wwyl.controller.settings;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.ProductCategory;
import com.wwyl.service.settings.ProductCategoryService;

/**
 * @author yche
 */
@Controller
@RequestMapping("/settings/product-category")
public class ProductCategoryController extends BaseController {
	@Resource
	private ProductCategoryService productCategoryService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "tree", method = RequestMethod.GET)
	@ResponseBody
	public List<ProductCategory> tree() {
		return productCategoryService.findRootProductCategorys();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public ProductCategory get(@PathVariable Long id) {
		return productCategoryService.findOne(id);
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model, Long parentId) {
		ProductCategory productCategory = new ProductCategory();
		if (parentId != null && parentId > 0) {
			ProductCategory parent = productCategoryService.findOne(parentId);
			productCategory.setParent(parent);
		}
		model.addAttribute("_method", "post");
		model.addAttribute("productCategory", productCategory);
		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("productCategory") @Valid ProductCategory productCategory, BindingResult result) {
		if (result.hasErrors()) {
			return formPage;
		}
		productCategoryService.save(productCategory);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		ProductCategory productCategory = productCategoryService.findOne(id);
		model.addAttribute("_method", "put");
		model.addAttribute("productCategory", productCategory);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("productCategory") @Valid ProductCategory productCategory, String perms, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			this.printBindingResult(result);
			return formPage;
		}

		productCategoryService.save(productCategory);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		productCategoryService.delete(id);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/organise", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable Long id, Long parentId, String point) {
		ProductCategory productCategory = productCategoryService.findOne(id);
		if ("append".equals(point)) {
			ProductCategory parent = productCategoryService.findOne(parentId);
			productCategory.setParent(parent);
		} else {
			productCategory.setParent(null);
		}
		try {
			productCategoryService.save(productCategory);
			return printMessage(0);
		} catch (Exception e) {
			return printMessage(1, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity exists(String oldCode,String code) {
		if (StringUtils.equals(oldCode, code)) {
	            return new ResponseEntity(HttpStatus.OK);
	    }

		if (productCategoryService.exists(code)) {
			 return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);

			
		} 
			return new ResponseEntity(HttpStatus.OK);
	 
	}
}
