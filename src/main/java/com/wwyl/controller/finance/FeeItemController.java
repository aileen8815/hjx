package com.wwyl.controller.finance;

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
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.service.ce.CEFeeItemService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("finance/fee-item")
public class FeeItemController extends BaseController {

	@Autowired
	CEFeeItemService CEFeeItemService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("feeItem") @Valid CEFeeItem feeItem, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return formPage;
		}
		CEFeeItemService.save(feeItem);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<CEFeeItem> list() {
		return CEFeeItemService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public CEFeeItem getfeeItem(@PathVariable Long id) {
		CEFeeItem feeItem = CEFeeItemService.findOne(id);
		return feeItem;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		CEFeeItem feeItem = CEFeeItemService.findOne(id);
		model.addAttribute("feeItem", feeItem);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("feeItem") @Valid CEFeeItem feeItem, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return formPage;
		}
		CEFeeItemService.save(feeItem);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		CEFeeItemService.delete(id);
		return indexRedirect;
	}

}
