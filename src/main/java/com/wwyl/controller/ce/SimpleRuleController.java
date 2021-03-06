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
import com.wwyl.entity.ce.CECalculationItem;
import com.wwyl.entity.ce.CEConditionItem;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.ce.CEOptionalItem;
import com.wwyl.entity.ce.CERule;
import com.wwyl.entity.ce.CERuleItem;
import com.wwyl.entity.ce.CERuleType;
import com.wwyl.service.ce.CERuleService;

/**
 * @author hehao
 */
@Controller
@RequestMapping("ce/simple-rule")
public class SimpleRuleController extends BaseController {
	
	@Autowired
	CERuleService ceRuleService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("relationOperators", CEConstant.RelationOperator.values());	
		model.addAttribute("feeItems", ceRuleService.findAllFeeItems());
		return indexPage;
	}

	@RequestMapping(value = "findRuleTypes", method = RequestMethod.GET)
	@ResponseBody
	public List<CERuleType> findRuleTypes(@RequestParam(value = "businessType") String businessType) {
		return ceRuleService.findByBusinessType(businessType);
	}
	
	@RequestMapping(value = "findRules", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findRules(@RequestParam(value = "page", defaultValue = "1") int page, 
			@RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "typeId") Long typeId) {
		Page<CERule> rules = ceRuleService.findRules(page, rows, typeId);
		return toEasyUiDatagridResult(rules);
	}
	
	@RequestMapping(value = "findOptionalItems", method = RequestMethod.GET)
	@ResponseBody
	public List<CEOptionalItem> findOptionalItems(String itemType, String businessType) {
		return ceRuleService.findOptionalItems(itemType, businessType);
	}

	@RequestMapping(value = "findRuleItems", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findRuleItems(@RequestParam(value = "page", defaultValue = "1") int page, 
			@RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "ruleId") Long ruleId) {
		Page<CERuleItem> items = ceRuleService.findRuleItems(page, rows, ruleId);
		return toEasyUiDatagridResult(items);
	}
	
	@RequestMapping(value = "findCalculationItems", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findCalculationItems(@RequestParam(value = "page", defaultValue = "1") int page, 
			@RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "ruleId") Long ruleId) {
		Page<CECalculationItem> items = ceRuleService.findCalculationItems(page, rows, ruleId);
		return toEasyUiDatagridResult(items);
	}

	@RequestMapping(value = "findConditionItems", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findConditionItems(@RequestParam(value = "page", defaultValue = "1") int page, 
			@RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "calculationItemId") Long calculationItemId) {
		Page<CEConditionItem> items = ceRuleService.findConditionItems(page, rows, calculationItemId);
		return toEasyUiDatagridResult(items);
	}

	@RequestMapping(value = "/{id}/viewRule", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public CERule viewRule(@PathVariable Long id) {
		return ceRuleService.findOneRule(id);
	}

	@RequestMapping(value = "/{id}/viewRuleItem", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public CERuleItem viewRuleItem(@PathVariable Long id) {
		return ceRuleService.findOneRuleItem(id);
	}

	@RequestMapping(value = "/{id}/viewCalculationItem", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public CECalculationItem viewCalculationItem(@PathVariable Long id) {
		return ceRuleService.findOneCalculationItem(id);
	}

	@RequestMapping(value = "/{id}/viewConditionItem", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public CEConditionItem viewConditionItem(@PathVariable Long id) {
		return ceRuleService.findOneConditionItem(id);
	}
	
	@RequestMapping(value = "/saveRule", method = RequestMethod.POST)
	@ResponseBody
	public Map saveRule(@ModelAttribute("CERule") @Valid CERule inboundRule, Long ruleTypeId, BindingResult result,ModelMap model) {
		if (result.hasErrors()) {
			return this.printMessage(1, "Failure");
		}
		try {
			CERuleType ruleType = ceRuleService.findOneType(ruleTypeId);
			inboundRule.setRuleType(ruleType);
			ceRuleService.saveRule(inboundRule);
		} catch(Exception e) {
			return this.printMessage(1, "???????????????????????????????????????");
		}
		return  this.printMessage(0, "Success");
	}

	@RequestMapping(value = "/{id}/deleteRule", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Map deleteRule(@PathVariable Long id) {
		try {
			ceRuleService.deleteRule(id);
		} catch(Exception e) {
			return this.printMessage(1, e.getMessage());
		}
		return  this.printMessage(0, "Success");
	}
	
	@RequestMapping(value = "/saveRuleItem", method = RequestMethod.POST)
	@ResponseBody
	public Map saveRuleItem(@ModelAttribute("CERuleItem") @Valid CERuleItem inboundRuleItem, Long ruleId, 
			Long optionalItemID, BindingResult result,ModelMap model) {
		if (result.hasErrors()) {
			return this.printMessage(1, "Failure");
		}
		try {
			CERule rule = ceRuleService.findOneRule(ruleId);
			inboundRuleItem.setCeRule(rule);
			CEOptionalItem inboundOptionalItem = ceRuleService.findOneOptionalItem(optionalItemID);
			inboundRuleItem.setOptionalItem(inboundOptionalItem);
			ceRuleService.saveRuleItem(inboundRuleItem);
		} catch(Exception e) {
			return this.printMessage(1, "???????????????????????????????????????");
		}
		return  this.printMessage(0, "Success");
	}

	@RequestMapping(value = "/{id}/deleteRuleItem", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Map deleteRuleItem(@PathVariable Long id) {
		try {
			ceRuleService.deleteRuleItem(id);
		} catch(Exception e) {
			return this.printMessage(1, e.getMessage());
		}
		return  this.printMessage(0, "Success");
	}
	

	@RequestMapping(value = "/saveCalculationItem", method = RequestMethod.POST)
	@ResponseBody
	public Map saveCalculationItem(@ModelAttribute("CECalculationItem") @Valid CECalculationItem inboundCalcItem, Long ruleId, 
			Long optionalItemID, Long feeItemID, BindingResult result,ModelMap model) {
		if (result.hasErrors()) {
			return this.printMessage(1, "Failure");
		}
		try {
			CERule rule = ceRuleService.findOneRule(ruleId);
			inboundCalcItem.setCeRule(rule);
			if(optionalItemID!=null) { 
				CEOptionalItem inboundOptionalItem = ceRuleService.findOneOptionalItem(optionalItemID);
				inboundCalcItem.setOptionalItem(inboundOptionalItem);
			} else {
				inboundCalcItem.setOptionalItem(null);
			}
			CEFeeItem feeItem = ceRuleService.findOneFeeItem(feeItemID);
			inboundCalcItem.setFeeItem(feeItem);
			ceRuleService.saveCalculationItem(inboundCalcItem);
		} catch(Exception e) {
			return this.printMessage(1, "???????????????????????????????????????");
		}
		return  this.printMessage(0, "Success");
	}

	@RequestMapping(value = "/{id}/deleteCalculationItem", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Map deleteCalculationItem(@PathVariable Long id) {
		try {
			ceRuleService.deleteCalculationItem(id);
		} catch(Exception e) {
			return this.printMessage(1, e.getMessage());
		}
		return  this.printMessage(0, "Success");
	}

	@RequestMapping(value = "/saveConditionItem", method = RequestMethod.POST)
	@ResponseBody
	public Map saveConditionItem(@ModelAttribute("CEConditionItem") @Valid CEConditionItem inboundCondItem, 
			Long calculationItemID, 
			Long optionalItemID, BindingResult result,ModelMap model) {
		if (result.hasErrors()) {
			return this.printMessage(1, "Failure");
		}
		try {
			CEOptionalItem inboundOptionalItem = ceRuleService.findOneOptionalItem(optionalItemID);
			inboundCondItem.setOptionalItem(inboundOptionalItem);
			CECalculationItem inboundCalcItem = ceRuleService.findOneCalculationItem(calculationItemID);
			inboundCondItem.setCalculationItem(inboundCalcItem);
			ceRuleService.saveConditionItem(inboundCondItem);
		} catch(Exception e) {
			return this.printMessage(1, "???????????????????????????????????????");
		}
		return  this.printMessage(0, "Success");
	}
	
	@RequestMapping(value = "/{id}/deleteConditionItem", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Map deleteConditionItem(@PathVariable Long id) {
		try {
			ceRuleService.deleteConditionItem(id);
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
		return ceRuleService.getReferObject(className, refName, id);
	}
}
