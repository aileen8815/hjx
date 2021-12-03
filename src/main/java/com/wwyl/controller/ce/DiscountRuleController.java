package com.wwyl.controller.ce;

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
import com.wwyl.entity.ce.CEConstant;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.ce.DEOptionalItem;
import com.wwyl.entity.ce.DERule;
import com.wwyl.entity.ce.DERuleItem;
import com.wwyl.service.ce.DERuleService;

/**
 * @author sjwang
 */
@Controller
@RequestMapping("ce/discount-rule")
public class DiscountRuleController extends BaseController {
	@Autowired
	DERuleService deRuleService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("relationOperators", CEConstant.RelationOperator.values());	
		model.addAttribute("feeItems", deRuleService.findAllFeeItems());
		return indexPage;
	}
	
	@RequestMapping(value = "findRules", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findRules(@RequestParam(value = "page", defaultValue = "1") int page, 
			@RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "feeItemId") Long feeItemId,
			@RequestParam(value = "businessType") String businessType) {
		Page<DERule> rules = deRuleService.findRules(page, rows, feeItemId, businessType);
		return toEasyUiDatagridResult(rules);
	}

	@RequestMapping(value = "findRuleItems", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findRuleItems(@RequestParam(value = "page", defaultValue = "1") int page, 
			@RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "ruleId") Long ruleId) {
		Page<DERuleItem> items = deRuleService.findRuleItems(page, rows, ruleId);
		return toEasyUiDatagridResult(items);
	}

	@RequestMapping(value = "findOptionalItems", method = RequestMethod.GET)
	@ResponseBody
	public List<DEOptionalItem> findOptionalItems(String businessType) {
		return deRuleService.findOptionalItems(businessType);
	}

	@RequestMapping(value = "/{id}/viewRule", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public DERule viewRule(@PathVariable Long id) {
		return deRuleService.findOneRule(id);
	}

	@RequestMapping(value = "/{id}/viewRuleItem", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public DERuleItem viewRuleItem(@PathVariable Long id) {
		return deRuleService.findOneRuleItem(id);
	}

	@RequestMapping(value = "/saveRule", method = RequestMethod.POST)
	@ResponseBody
	public Map saveRule(@ModelAttribute("DERule") @Valid DERule deRule, Long feeItemId, BindingResult result,ModelMap model) {
		if (result.hasErrors()) {
			return this.printMessage(1, "Failure");
		}
		try {
			CEFeeItem feeItem = deRuleService.findOneFeeItem(feeItemId);
			deRule.setFeeItem(feeItem);
			deRuleService.saveRule(deRule);
		} catch(Exception e) {
			return this.printMessage(1, e.getMessage());
		}
		return  this.printMessage(0, "Success");
	}

	@RequestMapping(value = "/{id}/deleteRule", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Map deleteRule(@PathVariable Long id) {
		try {
			deRuleService.deleteRule(id);
		} catch(Exception e) {
			return this.printMessage(1, e.getMessage());
		}
		return  this.printMessage(0, "Success");
	}
	
	@RequestMapping(value = "/saveRuleItem", method = RequestMethod.POST)
	@ResponseBody
	public Map saveRuleItem(@ModelAttribute("DERuleItem") @Valid DERuleItem ruleItem, Long ruleId, 
			Long optionalItemID, BindingResult result,ModelMap model) {
		if (result.hasErrors()) {
			return this.printMessage(1, "Failure");
		}
		try {
			DERule rule = deRuleService.findOneRule(ruleId);
			ruleItem.setDeRule(rule);
			DEOptionalItem optionalItem = deRuleService.findOneOptionalItem(optionalItemID);
			ruleItem.setOptionalItem(optionalItem);
			deRuleService.saveRuleItem(ruleItem);
		} catch(Exception e) {
			return this.printMessage(1, e.getMessage());
		}
		return  this.printMessage(0, "Success");
	}

	@RequestMapping(value = "/{id}/deleteRuleItem", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Map deleteRuleItem(@PathVariable Long id) {
		try {
			deRuleService.deleteRuleItem(id);
		} catch(Exception e) {
			return this.printMessage(1, e.getMessage());
		}
		return  this.printMessage(0, "Success");
	}

	@RequestMapping(value = "getReferObject", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getReferObject(@RequestParam(value="className") String className, 
			@RequestParam(value="refName") String refName,
			@RequestParam(value="id") Long id) {
		return deRuleService.getReferObject(className, refName, id);
	}
}
