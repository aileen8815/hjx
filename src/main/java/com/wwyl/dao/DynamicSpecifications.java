package com.wwyl.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import com.wwyl.dao.PropertyFilter.ConjunctionType;
import com.wwyl.dao.PropertyFilter.MatchType;

/**
 * @author fyunli
 */
public class DynamicSpecifications {

	public static List<PropertyFilter> parseSearchParams(Map<String, Object> searchParams) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			// 过滤掉空值
			if (StringUtils.isBlank((String) value)) {
				continue;
			}

			// 拆分MatchType与FieldName
			String[] names = StringUtils.split(key, "_");
			if (names.length != 2) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			String filedName = names[0];
			MatchType matchType = MatchType.valueOf(names[1]);
			PropertyFilter filter = new PropertyFilter(filedName, value, matchType);
			filters.add(filter);
		}

		return filters;
	}

	public static <T> Specification<T> buildSpecifitions(final Class<T> entityClazz, PropertyFilter filter) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(filter);
		return buildSpecifitions(entityClazz, filters, ConjunctionType.AND);
	}

	public static <T> Specification<T> buildSpecifitions(final Class<T> entityClazz, final Collection<PropertyFilter> filters) {
		return buildSpecifitions(entityClazz, filters, ConjunctionType.AND);
	}

	public static <T> Specification<T> buildSpecifitions(final Class<T> entityClazz, final Collection<PropertyFilter> filters, final ConjunctionType conjunctionType) {
		return new Specification<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (CollectionUtils.isNotEmpty(filters)) {

					List<Predicate> predicates = new ArrayList<Predicate>();
					for (PropertyFilter filter : filters) {
						// 解析属性名
						String[] names = StringUtils.split(filter.getFieldName(), ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}

						switch (filter.getMatchType()) {
						case EQ:
							predicates.add(builder.equal(expression, filter.getValue()));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%" + filter.getValue() + "%"));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression, (Comparable) filter.getValue()));
							break;
						case LT:
							predicates.add(builder.lessThan(expression, (Comparable) filter.getValue()));
							break;
						case GE:
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.getValue()));
							break;
						case LE:
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.getValue()));
							break;
						case BETWEEN:
							Object[] params = (Object[]) filter.getValue();
							Assert.isTrue(params != null && params.length == 2);
							predicates.add(builder.between(expression, (Comparable) params[0], (Comparable) params[1]));
							break;
						case IN:
							predicates.add(expression.in((Object[]) filter.getValue()));
							break;
						case ISNOTNULL:
							predicates.add(expression.isNotNull());
							break;
						case ISNULL:
							predicates.add(expression.isNull());
							break;
						default:
							break;
						}
					}

					// 将所有条件用联合起来
					if (predicates.size() > 0) {
						if (conjunctionType == ConjunctionType.OR) {
							return builder.or(predicates.toArray(new Predicate[predicates.size()]));
						} else {
							return builder.and(predicates.toArray(new Predicate[predicates.size()]));
						}
					}
				}

				return builder.conjunction();
			}
		};
	}

}
