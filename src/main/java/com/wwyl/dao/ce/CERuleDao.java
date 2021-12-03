package com.wwyl.dao.ce;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.ce.CERule;

/**
 * @author sjwang
 */
@Repository
public interface CERuleDao extends BaseRepository<CERule, Long>{
	@Query("select r from CERule r where ruleType.id = ?1")
	public Page<CERule> findByRuleType(Long ruleTypeId, Pageable pageable);


}
