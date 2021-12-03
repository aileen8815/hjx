package com.wwyl.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.CustomerGrade;
import com.wwyl.entity.settings.Packing;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.ProductCategory;
import com.wwyl.service.settings.CustomerGradeService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.PackingService;
import com.wwyl.service.settings.ProductCategoryService;
import com.wwyl.service.settings.ProductService;

/**
 * @author sjwang
 */
@Controller
@RequestMapping("combo-refer")
public class ComboReferController extends BaseController {
	@Resource
	ProductService productService;
	@Resource
	ProductCategoryService productCategoryService;
	@Resource
	CustomerService customerService;
	@Resource
	CustomerGradeService customerGradeService;
	@Resource
	PackingService packingService;

	@RequestMapping(value = "product", method = RequestMethod.GET)
	@ResponseBody
	public List<Product> product() {
		return productService.findAll();
	}

	@RequestMapping(value = "product-category", method = RequestMethod.GET)
	@ResponseBody
	public List<ProductCategory> productCategory() {
		return productCategoryService.findAll();
	}

	@RequestMapping(value = "customer", method = RequestMethod.GET)
	@ResponseBody
	public List<Customer> customer() {
		return customerService.findAll();
	}

	@RequestMapping(value = "customer-grade", method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerGrade> customerGrade() {
		return customerGradeService.findAll();
	}

	@RequestMapping(value = "packing", method = RequestMethod.GET)
	@ResponseBody
	public List<Packing> packing() {
		return packingService.findAll();
	}
}
