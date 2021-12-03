package com.wwyl.dao.settings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.StevedorePort;

import javax.persistence.QueryHint;

import java.util.List;

@Repository
public interface StevedorePortDao extends BaseRepository<StevedorePort, Long> {

    @Override
    @Query("select s from StevedorePort s order by s.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<StevedorePort> findAll();
    
    @Query("select s from StevedorePort s where s.name like %?1% or s.code like %?1%")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Page<StevedorePort> findByCodeOrNameLike(String name, Pageable pageable);
    
    @Query("select s from StevedorePort s where  s.code = ?1")
	public List<StevedorePort> findByCode(String code);
}
