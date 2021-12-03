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
import com.wwyl.entity.settings.CustomerGrade;
import com.wwyl.service.settings.CustomerGradeService;


/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/customer-grade")
public class CustomerGradeController extends BaseController {

	@Resource
	CustomerGradeService customerGradeService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("customerGrade") @Valid CustomerGrade customerGrade, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		customerGradeService.save(customerGrade);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerGrade> list() {
		return customerGradeService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public CustomerGrade getcustomerGrade(@PathVariable Long id) {
		CustomerGrade customerGrade = customerGradeService.findOne(id);
		return customerGrade;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		CustomerGrade customerGrade = customerGradeService.findOne(id);

		model.addAttribute("customerGrade", customerGrade);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("customerGrade") @Valid CustomerGrade customerGrade, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		customerGradeService.save(customerGrade);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		customerGradeService.delete(id);
		return indexRedirect;
	}

}
