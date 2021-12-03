package com.wwyl.dao.ce;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.ce.CECalculationItem;

/**
 * @author sjwang
 */
@Repository
public interface CECalculationItemDao extends BaseRepository<CECalculationItem, Long>{
	@Query("select r from CECalculationItem r where ceRule.id = ?1")
	public Page<CECalculationItem> findByRule(Long ruleId, Pageable pageable);

}
