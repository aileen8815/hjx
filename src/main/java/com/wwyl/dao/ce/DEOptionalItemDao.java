package com.wwyl.dao.ce;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.ce.DEOptionalItem;

/**
 * @author sjwang
 */
public interface DEOptionalItemDao extends BaseRepository<DEOptionalItem, Long> {
	@Query("select r from DEOptionalItem r where discountType=?1")
	public List<DEOptionalItem> findByBusinessType(String businessType);
}
