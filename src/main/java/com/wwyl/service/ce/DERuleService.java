package com.wwyl.service.ce;

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.ce.CEFeeItemDao;
import com.wwyl.dao.ce.DEOptionalItemDao;
import com.wwyl.dao.ce.DERuleDao;
import com.wwyl.dao.ce.DERuleItemDao;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.ce.DEOptionalItem;
import com.wwyl.entity.ce.DERule;
import com.wwyl.entity.ce.DERuleItem;

/**
 * @author sjwang
 */
@Service
public class DERuleService {
	@Resource
	EntityManagerFactory emf;

	@Resource
	private CEFeeItemDao ceFeeItemDao;
	@Resource
	private DERuleDao deRuleDao;
	@Resource
	private DERuleItemDao deRuleItemDao;
	@Resource
	private DEOptionalItemDao deOptionalItemDao;
	

	public List<CEFeeItem> findAllFeeItems() {
		return ceFeeItemDao.findAll();
	}
	
	public Page<DERule> findRules(int page, int rows, Long feeItemId, String businessType) {
		return deRuleDao.find(feeItemId, businessType, RepoUtils.buildPageRequest(page, rows));
	}
	
	public List<DERule> findRules(Long feeItemId, String businessType) {
		return deRuleDao.find(feeItemId, businessType);
	}

	public DERule findOneRule(Long id) {
		return deRuleDao.findOne(id);
	}

	public Page<DERuleItem> findRuleItems(int page, int rows, Long ruleId) {
		return deRuleItemDao.findByRule(ruleId, RepoUtils.buildPageRequest(page, rows));
	}

	public DERuleItem findOneRuleItem(Long id) {
		return deRuleItemDao.findOne(id);
	}

	public CEFeeItem findOneFeeItem(Long id) {
		return ceFeeItemDao.findOne(id);
	}

	public List<DEOptionalItem> findOptionalItems(String businessType) {
		return deOptionalItemDao.findByBusinessType(businessType);
	}

	public DEOptionalItem findOneOptionalItem(Long id) {
		return deOptionalItemDao.findOne(id);
	}

	public void saveRule(DERule deRule) {
		deRuleDao.save(deRule);
	}

	public void saveRuleItem(DERuleItem deRuleItem) {
		deRuleItemDao.save(deRuleItem);
	}

	public void deleteRule(Long ruleId) {
		deRuleDao.delete(ruleId);
	}
	
	public void deleteRuleItem(Long ruleItemId) {
		deRuleItemDao.delete(ruleItemId);
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
}
