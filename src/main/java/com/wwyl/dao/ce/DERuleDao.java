package com.wwyl.dao.ce;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.ce.DERule;

/**
 * @author sjwang
 */
@Repository
public interface DERuleDao extends BaseRepository<DERule, Long> {
	@Query("SELECT r FROM DERule r WHERE feeItem.id = ?1 AND businessType=?2")
	public Page<DERule> find(Long feeItemId, String businessType, Pageable pageable);
	
	@Query("SELECT r FROM DERule r WHERE feeItem.id = ?1 AND businessType=?2 ORDER BY priority DESC")
	public List<DERule> find(Long feeItemId, String businessType);
}
