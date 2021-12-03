package com.wwyl.dao.ce;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.ce.DERuleItem;

/**
 * @author sjwang
 */
@Repository
public interface DERuleItemDao extends BaseRepository<DERuleItem, Long> {
	@Query("select r from DERuleItem r where deRule.id = ?1")
	public Page<DERuleItem> findByRule(Long ruleId, Pageable pageable);

}
