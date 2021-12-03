package com.wwyl.service.ce;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import com.wwyl.entity.ce.DEOptionalItem;
import com.wwyl.entity.ce.DERule;
import com.wwyl.entity.ce.DERuleItem;
import com.wwyl.entity.ce.FeeItem;
import com.wwyl.entity.ce.HierarchicalEntity;
import com.wwyl.entity.ce.CEConstant.ItemRelation;
import com.wwyl.entity.ce.CEConstant.RelationOperator;

/**
 * @author sjwang
 */
@Service
public class DECalculator {
	@Resource
	private DERuleService deRuleService;
	
	public BigDecimal getDiscountRate(Map<String, Object> propertySet, 
			FeeItem feeItem,
			String businessType) {
		List<DERule> rules = deRuleService.findRules(feeItem.getId(), businessType);
		BigDecimal result = new BigDecimal(1);
		for(DERule rule : rules) {
			if(apply(propertySet, rule)) {
				result = rule.getFactor();
				break;
			}
		}
		return result;
	}
	
	private boolean apply(Map<String, Object> propertySet, DERule rule) {
		boolean result = false;
		if (rule.getPriority() == 0) {// 缺省规则，自动匹配
			result = true;
		}
		if (rule.getRuleItems().size() == 0) {// 没有规则子项，视同缺省规则
			result = true;
		} else {
			for (DERuleItem ruleItem : rule.getRuleItems()) {
				result = match(propertySet, ruleItem);
				if (rule.getItemRelation() != null && rule.getItemRelation().equals(ItemRelation.或)) {// 规则子项的关系为“或”时，只需匹配一条规则子项即可确定匹配成功
					if (result)
						break;
				} else {// 规则子项的关系为“与”时，只要一条规则子项不匹配即停止检查，匹配失败
					if (!result)
						break;
				}
			}
		}
		return result;
	}

	private boolean match(Map<String, Object> propertySet, DERuleItem ruleItem) {
		return match(propertySet, ruleItem.getOptionalItem(), ruleItem.getRelation(), ruleItem.getValue());
	}

	private boolean match(Map<String, Object> propertySet, DEOptionalItem optionalItem, RelationOperator relationOperator, String expectedValue) {
		return check(propertySet, optionalItem, relationOperator, expectedValue);
	}

	private boolean check(Map<String, Object> propertySet, DEOptionalItem optionalItem, RelationOperator relationOperator, String expectedValue) {
		boolean result = false;
		Object actualValue = propertySet.get(optionalItem.getItemName());
		if(actualValue != null) {
			try {
				if (StringUtils.isNotBlank(optionalItem.getRefSource())) { // 有引用，即选项是一个对象属性
					if (optionalItem.getValueType().toLowerCase().equals("digit")) {// 针对对象的键值
						result = compare("digit", actualValue.toString(), relationOperator, expectedValue);
					} else if (optionalItem.getValueType().toLowerCase().equals("hierarchy")) {// 针对层次对象
						if (relationOperator == RelationOperator.属于) {
							result = ((HierarchicalEntity)actualValue).belongTo(new Long(expectedValue));
						} else if (relationOperator == RelationOperator.不属于) {
							result = !((HierarchicalEntity)actualValue).belongTo(new Long(expectedValue));
						}
						
					}
				} else { // 基本类型的属性
					if (optionalItem.getValueType().toLowerCase().equals("date")) {
						result = compare("date", DateFormatUtils.format((Date) (actualValue), "yyyy-mm-dd"), relationOperator, expectedValue);
					} else {
						result = compare(optionalItem.getValueType(), actualValue.toString(), relationOperator, expectedValue);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}

	private boolean compare(String dataType, String actualValue, RelationOperator relationOperator, String expectedValue) {
		boolean result = false;
		if (dataType.toLowerCase().equals("digit")) {
			BigDecimal v1 = new BigDecimal(actualValue);
			v1 = v1.setScale(4);
			BigDecimal v2 = new BigDecimal(expectedValue);
			v2 = v2.setScale(4);
			if (relationOperator.equals(RelationOperator.不等于))
				result = v1.compareTo(v2) != 0;
			if (relationOperator.equals(RelationOperator.等于))
				result = v1.compareTo(v2) == 0;
			if (relationOperator.equals(RelationOperator.大于))
				result = v1.compareTo(v2) == 1;
			if (relationOperator.equals(RelationOperator.大于等于))
				result = v1.compareTo(v2) != -1;
			if (relationOperator.equals(RelationOperator.小于))
				result = v1.compareTo(v2) == -1;
			if (relationOperator.equals(RelationOperator.小于等于))
				result = v1.compareTo(v2) != 1;
		} else if (dataType.toLowerCase().equals("date") || dataType.toLowerCase().equals("string")) {
			System.out.println(actualValue+relationOperator+expectedValue);
			if (relationOperator.equals(RelationOperator.不等于))
				result = actualValue.compareTo(expectedValue) != 0;
			if (relationOperator.equals(RelationOperator.等于))
				result = actualValue.compareTo(expectedValue) == 0;
			if (relationOperator.equals(RelationOperator.大于))
				result = actualValue.compareTo(expectedValue) > 0;
			if (relationOperator.equals(RelationOperator.大于等于))
				result = actualValue.compareTo(expectedValue) >= 0;
			if (relationOperator.equals(RelationOperator.小于))
				result = actualValue.compareTo(expectedValue) < 0;
			if (relationOperator.equals(RelationOperator.小于等于))
				result = actualValue.compareTo(expectedValue) <= 0;
		}
		return result;
	}
}
