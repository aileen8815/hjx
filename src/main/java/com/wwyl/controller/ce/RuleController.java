package com.wwyl.controller.ce;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.mybatis.spring.SqlSessionTemplate;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.ce.CEConstant;
import com.wwyl.entity.ce.CEConstant.ItemRelation;
import com.wwyl.entity.finance.StandingBookDaily;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.CustomerGrade;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.ce.CECalculationItem;
import com.wwyl.entity.ce.CEConditionItem;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.ce.CEOptionalItem;
import com.wwyl.entity.ce.CERule;
import com.wwyl.entity.ce.CERuleItem;
import com.wwyl.entity.ce.CERuleType;
import com.wwyl.entity.ce.FeeItem;
import com.wwyl.service.ce.CEFeeItemService;
import com.wwyl.service.ce.CERuleService;
import com.wwyl.service.settings.CustomerGradeService;
import com.wwyl.service.settings.CustomerService;

/**
 * @author sjwang
 */
@Controller
@RequestMapping("ce/rule")
public class RuleController extends BaseController {
	
	@Autowired
	CERuleService ceRuleService;
	
	@Resource
    private SqlSessionTemplate sqlSessionTemplate;
		
	@Resource
    CustomerGradeService customerGradeService;
    
    @Resource
    CEFeeItemService ceFeeItemService;
	
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
			if (inboundRule.getItemRelation() == null){
				inboundRule.setItemRelation(ItemRelation.与);
			}
			ceRuleService.saveRule(inboundRule);
		} catch(Exception e) {
			return this.printMessage(1, "保存的信息不完整，保存失败");
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
			return this.printMessage(1, "保存的信息不完整，保存失败");
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
			if (inboundCalcItem.getConditionRelation() == null){
				inboundCalcItem.setConditionRelation(ItemRelation.与);
			}
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
			return this.printMessage(1, "保存的信息不完整，保存失败");
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
			return this.printMessage(1, "保存的信息不完整，保存失败");
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
	/*
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "customerId", defaultValue = "") String customerId, @RequestParam(value = "productId", defaultValue = "") String productId,
			@RequestParam(value = "storeAreaId", defaultValue = "") String storeAreaId) {
		Map<String, Object> params = null;
		List<Object> ruleDetails = new ArrayList<Object>();
		ruleDetails = sqlSessionTemplate.selectList("findRuleDetail", params);
		return toEasyUiDatagridResult();
	}
	*/
	
	@RequestMapping(value = "/rule-detail", method = RequestMethod.GET)
    public String ruleDetail(ModelMap model) {
		List<CustomerGrade> customerGrades = customerGradeService.findAll();
        model.addAttribute("customerGradelist", customerGrades);
        
        List<CEFeeItem> feeitems = ceFeeItemService.findAll();
        model.addAttribute("feeItemlist", feeitems);
        return "/ce/rule_detail";
    }
	
	@RequestMapping(value = "/search-rule-detail", method = RequestMethod.GET)
    @ResponseBody
    public List<Object> searchRuleDetail(Long customerGradeId, Long feeItemId, String businessType) {
		List<Object> ruleDetails = ceRuleService.findRuleDetail(customerGradeId, feeItemId, businessType);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValueAsString(ruleDetails);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ruleDetails;
    }
}
