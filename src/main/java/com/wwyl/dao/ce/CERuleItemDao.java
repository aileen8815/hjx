package com.wwyl.dao.ce;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.ce.CERuleItem;

/**
 * @author sjwang
 */
@Repository
public interface CERuleItemDao extends BaseRepository<CERuleItem, Long>{

	@Query("select r from CERuleItem r where ceRule.id = ?1")
	public Page<CERuleItem> findByRule(Long ruleId, Pageable pageable);
	
}
