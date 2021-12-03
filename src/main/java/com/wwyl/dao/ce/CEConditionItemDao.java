package com.wwyl.dao.ce;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.ce.CEConditionItem;

/**
 * @author sjwang
 */
@Repository
public interface CEConditionItemDao extends BaseRepository<CEConditionItem, Long>{
	@Query("select r from CEConditionItem r where calculationItem.id = ?1")
	public Page<CEConditionItem> findByCalculationItem(Long calculationItemId, Pageable pageable);

}
