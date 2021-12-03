package com.wwyl.dao.ce;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.ce.CEOptionalItem;

/**
 * @author sjwang
 */
@Repository
public interface CEOptionalItemDao extends BaseRepository<CEOptionalItem, Long> {
	@Query("select o from CEOptionalItem o where itemType=?1 and businessType=?2")
	public List<CEOptionalItem> findByItemType(String itemType, String businessType);
}
