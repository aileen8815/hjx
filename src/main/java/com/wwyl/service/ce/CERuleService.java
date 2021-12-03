package com.wwyl.service.ce;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.collections.map.HashedMap;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.ce.CECalculationItemDao;
import com.wwyl.dao.ce.CEConditionItemDao;
import com.wwyl.dao.ce.CEFeeItemDao;
import com.wwyl.dao.ce.CEOptionalItemDao;
import com.wwyl.dao.ce.CERuleDao;
import com.wwyl.dao.ce.CERuleItemDao;
import com.wwyl.dao.ce.CERuleTypeDao;
import com.wwyl.entity.ce.CECalculationItem;
import com.wwyl.entity.ce.CEConditionItem;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.ce.CEOptionalItem;
import com.wwyl.entity.ce.CERule;
import com.wwyl.entity.ce.CERuleItem;
import com.wwyl.entity.ce.CERuleType;
import com.wwyl.entity.finance.StandingBookDaily;

/**
 * @author sjwang
 */
@Service
public class CERuleService {
	
	@Resource
	EntityManagerFactory emf;

	@Resource
	private CERuleTypeDao ceRuleTypeDao;
	@Resource
	private CERuleDao ceRuleDao;
	@Resource
	private CERuleItemDao ceRuleItemDao;
	@Resource
	private CECalculationItemDao ceCalculationItemDao;
	@Resource
	private CEConditionItemDao ceConditionItemDao;
	@Resource
	private CEOptionalItemDao ceOptionalItemDao;
	@Resource
	private CEFeeItemDao ceFeeItemDao;
	@Resource
    private SqlSessionTemplate sqlSessionTemplate;
	
	public List<CERuleType> findByBusinessType(String businessType) {
		return ceRuleTypeDao.findByBusinessType(businessType);
	}
	
	public List<CEFeeItem> findAllFeeItems() {
		return ceFeeItemDao.findAll();
	}
	
	public List<CEOptionalItem> findOptionalItems(String itemType, String businessType) {
		return ceOptionalItemDao.findByItemType(itemType, businessType);
	}
	
	public CEOptionalItem findOneOptionalItem(Long id) {
		return ceOptionalItemDao.findOne(id);
	}
	
	public CEFeeItem findOneFeeItem(Long id) {
		return ceFeeItemDao.findOne(id);
	}

	public CERuleType findOneType(Long id) {
		return ceRuleTypeDao.findOne(id);
	}
	
	public Page<CERule> findRules(int page, int rows, Long ruleTypeId) {
		return ceRuleDao.findByRuleType(ruleTypeId, RepoUtils.buildPageRequest(page, rows));
	}
	
	public CERule findOneRule(Long id) {
		return ceRuleDao.findOne(id);
	}
	
	public Page<CERuleItem> findRuleItems(int page, int rows, Long ruleId) {
		return ceRuleItemDao.findByRule(ruleId, RepoUtils.buildPageRequest(page, rows));
	}
	
	public CERuleItem findOneRuleItem(Long id) {
		return ceRuleItemDao.findOne(id);
	}
	
	public Page<CECalculationItem> findCalculationItems(int page, int rows, Long ruleId) {
		return ceCalculationItemDao.findByRule(ruleId, RepoUtils.buildPageRequest(page, rows));
	}
	
	public CECalculationItem findOneCalculationItem(Long id) {
		return ceCalculationItemDao.findOne(id);
	}
	
	public Page<CEConditionItem> findConditionItems(int page, int rows, Long calculationItemId) {
		return ceConditionItemDao.findByCalculationItem(calculationItemId, RepoUtils.buildPageRequest(page, rows));
	}
	
	public CEConditionItem findOneConditionItem(Long id) {
		return ceConditionItemDao.findOne(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteRule(Long ruleId) {
		ceRuleDao.delete(ruleId);
	}
	
	public void deleteRuleItem(Long ruleItemId) {
		ceRuleItemDao.delete(ruleItemId);
	}

	public void deleteCalculationItem(Long calculationItemId) {
		ceCalculationItemDao.delete(calculationItemId);
	}
	
	public void deleteConditionItem(Long conditionItemId) {
		ceConditionItemDao.delete(conditionItemId);
	}

	public void saveRule(CERule ceRule) {
		ceRuleDao.save(ceRule);
	}
	
	public void saveRuleItem(CERuleItem ceRuleItem) {
		ceRuleItemDao.save(ceRuleItem);
	}

	public void saveCalculationItem(CECalculationItem ceCalculationItem) {
		ceCalculationItemDao.save(ceCalculationItem);
	}
	
	public void saveConditionItem(CEConditionItem ceConditionItem) {
		ceConditionItemDao.save(ceConditionItem);
	}

	@SuppressWarnings("unchecked")
	public String getReferObject(String className, 
			String refName,
			Long id) {
		try {
			Class clazz = Class.forName(className);
			EntityManager em = emf.createEntityManager();
			Object obj = em.find(clazz, id);
			Field field = clazz.getDeclaredField(refName);
			field.setAccessible(true);
			em.close();
			return field.get(obj).toString();
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public List<Object> findRuleDetail(Long customerGradeId, Long feeItemId, String businessType) {
        Map<String, Object> params = new HashedMap();
        if (0 != customerGradeId) {
            params.put("customerGradeId", customerGradeId);
        }
        if (0 != feeItemId) {
            params.put("feeItemId", feeItemId);
        }
        if ( !businessType.equals("all")) {
            params.put("businessType", businessType);
        }
        return sqlSessionTemplate.selectList("findRuleDetail", params);
    }
}
