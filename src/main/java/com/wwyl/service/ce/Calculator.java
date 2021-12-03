package com.wwyl.service.ce;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import com.wwyl.entity.PersistableEntity;
import com.wwyl.util.ReflectionUtils;
import com.wwyl.entity.ce.CEConstant.RelationOperator;
import com.wwyl.entity.ce.CalculableEntity;
import com.wwyl.entity.ce.CalculatedResult;
import com.wwyl.entity.ce.CalculationItem;
import com.wwyl.entity.ce.ConditionItem;
import com.wwyl.entity.ce.HierarchicalEntity;
import com.wwyl.entity.ce.OptionalItem;
import com.wwyl.entity.ce.Rule;
import com.wwyl.entity.ce.RuleItem;
import com.wwyl.entity.ce.RuleType;
import com.wwyl.entity.ce.CEConstant.ItemRelation;

/**
 * @author sjwang
 */
@Service
public class Calculator {

	private boolean apply(CalculableEntity entity, Rule rule) {
		boolean result = false;
		if (rule.getPriority() == 0) {// 缺省规则，自动匹配
			result = true;
		}
		if (rule.getRuleItems().size() == 0) {// 没有规则子项，视同缺省规则
			result = true;
		} else {
			for (Object ruleItem : rule.getRuleItems()) {
				result = match(entity, (RuleItem) ruleItem);
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

	private boolean match(CalculableEntity calculableEntity, RuleItem ruleItem) {
		return match(calculableEntity, ruleItem.getOptionalItem(), ruleItem.getRelation(), ruleItem.getValue());
	}

	private boolean match(CalculableEntity calculableEntity, CalculationItem calculationItem) {
		boolean result = false;
		if (calculationItem.getConditionItems().size() == 0) {// 无条件子项时，自动匹配
			result = true;
		} else {
			for (Object condItem : calculationItem.getConditionItems()) {
				result = match(calculableEntity, ((ConditionItem) condItem).getOptionalItem(), ((ConditionItem) condItem).getRelation(), ((ConditionItem) condItem).getValue());
				if (calculationItem.getConditionRelation() != null && calculationItem.getConditionRelation().equals(ItemRelation.或)) {// 条件子项的关系为“或”时，只需匹配一条条件规则子项即可确定匹配成功
					if (result)
						break;
				} else {// 条件子项间的关系为“与”时，只要一条规则子项不匹配即停止检查，匹配失败
					if (!result)
						break;
				}
			}
		}
		return result;
	}

	/*
	 * 选项有两种来源，一是来自计算对象的直接属性，其hostName=entity； 还有一种是来自计算对象的对象属性的属性，其hostName=对象属性名。
	 *  对直接属性：itemName表示访问该属性getter方法名。如果它对应的是一个实体对象（refSource不为空），而其valueType='digit'，表示对这个选项，直接检查其id值与期望值间的关系；若valueType='hierarchy',
	 *  则表示要检查从计算对象获取的这个对象与指定层次对象的从属关系。
	 *  对对象属性：hostName是计算对象所引用的对象，itemName只是此对象的某个属性名。如果itemName实际上对应的是hostName对象的一个对象属性， 而valueType='digit'，则 此时的匹配检查是针对该属性对象的属性对象的Id进行的。
	 * itemType表示选项被用于何种用途：rule, calculation, condition。
	 */
	private boolean check(Object entity, OptionalItem optionalItem, RelationOperator relationOperator, String expectedValue) {
		String actualValue = "unknown";
		boolean result = false;
		try {
			if (StringUtils.isNotBlank(optionalItem.getRefSource())) { // 有引用，即选项是一个对象属性
				if (optionalItem.getValueType().toLowerCase().equals("digit")) {// 针对对象的键值
					actualValue = ((PersistableEntity) ReflectionUtils.forceInvokeMethod(entity, optionalItem.getItemName())).getId().toString();
					result = compare("digit", actualValue, relationOperator, expectedValue);
				} else if (optionalItem.getValueType().toLowerCase().equals("hierarchy")) {// 针对层次对象
					if (relationOperator == RelationOperator.属于) {
						result = ((HierarchicalEntity) (ReflectionUtils.forceInvokeMethod(entity, optionalItem.getItemName()))).belongTo(new Long(expectedValue));
					} else if (relationOperator == RelationOperator.不属于) {
						result = !((HierarchicalEntity) (ReflectionUtils.forceInvokeMethod(entity, optionalItem.getItemName()))).belongTo(new Long(expectedValue));
					}
				}
			} else { // 基本类型的属性
				if (optionalItem.getValueType().toLowerCase().equals("date")) {
					actualValue = DateFormatUtils.format((Date) (ReflectionUtils.forceInvokeMethod(entity, optionalItem.getItemName())), "yyyy-MM-dd");
				} else {
					actualValue = ReflectionUtils.forceInvokeMethod(entity, optionalItem.getItemName()).toString();
				}
				result = compare(optionalItem.getValueType(), actualValue, relationOperator, expectedValue);
			}
			System.out.println("compare:"+actualValue+relationOperator+expectedValue+";result:"+result);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	private boolean match(CalculableEntity calculatedEntity, OptionalItem optionalItem, RelationOperator relationOperator, String expectedValue) {
		boolean result = false;
		if (optionalItem.getHostName().toLowerCase().equals("entity")) {// 直接属性
			result = check(calculatedEntity, optionalItem, relationOperator, expectedValue);
		} else {// 对象属性
			try {
				Object host = ReflectionUtils.forceInvokeMethod(calculatedEntity, optionalItem.getHostName());
				result = check(host, optionalItem, relationOperator, expectedValue);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			} catch (Exception e) {
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

	private BigDecimal getAmount(CalculableEntity entity, CalculationItem calculationItem) {
		BigDecimal amount = null;
		if (calculationItem.getOptionalItem() == null) {
			amount = calculationItem.getFactor();
		} else {
			try {
				amount = (new BigDecimal(ReflectionUtils.forceInvokeMethod(entity, calculationItem.getOptionalItem().getItemName()).toString()))
						.subtract(calculationItem.getDecrease()).multiply(calculationItem.getFactor()).add(calculationItem.getAddedValue());
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
		return amount;
	}

	private CalculatedResult calculate(CalculableEntity calculatedEntity, CalculationItem calculationItem) {
		CalculatedResult result = null;
		if (match(calculatedEntity, calculationItem)) {
			result = new CalculatedResult();
			result.setAmount(getAmount(calculatedEntity, calculationItem));
			result.setFeeItem(calculationItem.getFeeItem());
			result.setFactor(calculationItem.getFactor());
			if(calculationItem.getOptionalItem() == null) result.setCalculatedItemName("按次计费");
			else result.setCalculatedItemName(calculationItem.getOptionalItem().getItemTitle());
		}
		return result;
	}

	private Set<CalculatedResult> calculate(CalculableEntity entity, Rule rule) {
		Set<CalculatedResult> result = new HashSet<CalculatedResult>();
		if (apply(entity, rule)) {
			for (Object calculationItem : rule.getCalculationItems()) {
				CalculatedResult calculatedResult = calculate(entity, (CalculationItem) calculationItem);
				if (calculatedResult != null) {
					result.add(calculatedResult);
				}
			}
		}
		return result;
	}

	public Set<CalculatedResult> calculate(CalculableEntity entity, List<? extends RuleType> ruleTypes) {
		Set<CalculatedResult> results = new HashSet<CalculatedResult>();
		for (RuleType ruleType : ruleTypes) {
			Set<CalculatedResult> result = calculate(entity, ruleType);
			if(result != null) { results.addAll(result);}
		}
		return results;
	}

	public Set<CalculatedResult> calculate(CalculableEntity entity, RuleType ruleType) {
		Set<CalculatedResult> result = null;
		for (Rule rule : ruleType.getRules()) {
			result = calculate(entity, rule);
			if (result.size() != 0)
				break;
		}
		if(result == null || result.size()==0) {
			throw new RuntimeException("请检查计费规则！["+ruleType.getTypeName()+"]没有缺省规则");
		}
		return result;
	}
	
	public Set<CalculatedResult> calculate(Map<String, Object> propertySet, List<? extends RuleType> ruleTypes) {
		Set<CalculatedResult> results = new HashSet<CalculatedResult>();
		for(RuleType ruleType : ruleTypes) {
			Set<CalculatedResult> result = calculate(propertySet, ruleType);
			if(result != null) { results.addAll(result);}
		}
		return results;
	}

	public Set<CalculatedResult> calculate(Map<String, Object> propertySet, RuleType ruleType) {
		Set<CalculatedResult> result = null;
		for(Rule rule : ruleType.getRules()) {
			result = calculate(propertySet, rule);
			if(result.size() !=0) break;
		}
		if(result == null || result.size()==0) {
			throw new RuntimeException("请检查计费规则！["+ruleType.getTypeName()+"]没有缺省规则");
		}
		return result;
	}
	
	private Set<CalculatedResult> calculate(Map<String, Object> propertySet, Rule rule) {
		Set<CalculatedResult> result = new HashSet<CalculatedResult>();
		if (apply(propertySet, rule)) {
			for (Object calculationItem : rule.getCalculationItems()) {
				CalculatedResult calculatedResult = calculate(propertySet, (CalculationItem) calculationItem);
				if (calculatedResult != null) {
					result.add(calculatedResult);
				}
			}
		}
		return result;
	}

	private CalculatedResult calculate(Map<String, Object> propertySet, CalculationItem calculationItem) {
		CalculatedResult result = null;
		if (match(propertySet, calculationItem)) {
			result = new CalculatedResult();
			result.setAmount(getAmount(propertySet, calculationItem));
			result.setFeeItem(calculationItem.getFeeItem());
			result.setFactor(calculationItem.getFactor());
			if(calculationItem.getOptionalItem() == null) result.setCalculatedItemName("按次计费");
			else result.setCalculatedItemName(calculationItem.getOptionalItem().getItemTitle());
		}
		return result;
	}

	private BigDecimal getAmount(Map<String, Object> propertySet, CalculationItem calculationItem) {
		BigDecimal amount = null;
		if (calculationItem.getOptionalItem() == null) {
			amount = calculationItem.getFactor();
		} else {
			try {
				amount = (new BigDecimal(propertySet.get(calculationItem.getOptionalItem().getItemName()).toString()))
						.subtract(calculationItem.getDecrease()).multiply(calculationItem.getFactor()).add(calculationItem.getAddedValue());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return amount;
	}
	
	private boolean apply(Map<String, Object> propertySet, Rule rule) {
		boolean result = false;
		if (rule.getPriority() == 0) {// 缺省规则，自动匹配
			result = true;
		}
		if (rule.getRuleItems().size() == 0) {// 没有规则子项，视同缺省规则
			result = true;
		} else {
			for (Object ruleItem : rule.getRuleItems()) {
				result = match(propertySet, (RuleItem) ruleItem);
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

	private boolean match(Map<String, Object> propertySet, RuleItem ruleItem) {
		return match(propertySet, ruleItem.getOptionalItem(), ruleItem.getRelation(), ruleItem.getValue());
	}

	private boolean match(Map<String, Object> propertySet, CalculationItem calculationItem) {
		boolean result = false;
		if (calculationItem.getConditionItems().size() == 0) {// 无条件子项时，自动匹配
			result = true;
		} else {
			for (Object condItem : calculationItem.getConditionItems()) {
				result = match(propertySet, ((ConditionItem) condItem).getOptionalItem(), ((ConditionItem) condItem).getRelation(), ((ConditionItem) condItem).getValue());
				if (calculationItem.getConditionRelation() !=null && calculationItem.getConditionRelation().equals(ItemRelation.或)) {// 条件子项的关系为“或”时，只需匹配一条条件规则子项即可确定匹配成功
					if (result)
						break;
				} else {// 条件子项间的关系为“与”时，只要一条规则子项不匹配即停止检查，匹配失败
					if (!result)
						break;
				}
			}
		}
		return result;
	}

	private boolean match(Map<String, Object> propertySet, OptionalItem optionalItem, RelationOperator relationOperator, String expectedValue) {
		return check(propertySet, optionalItem, relationOperator, expectedValue);
	}

	private boolean check(Map<String, Object> propertySet, OptionalItem optionalItem, RelationOperator relationOperator, String expectedValue) {
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

}
