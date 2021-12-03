package com.wwyl.dao.ce;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.ce.CERuleType;

/**
 * @author sjwang
 */
@Repository
public interface CERuleTypeDao extends BaseRepository<CERuleType, Long>{
	@Query("select r from CERuleType r where businessType = ?1 order by id asc")
	public List<CERuleType> findByBusinessType(String businessType);
}
