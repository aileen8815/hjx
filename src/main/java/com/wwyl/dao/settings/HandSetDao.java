package com.wwyl.dao.settings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.HandSetStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.HandSet;

import javax.persistence.QueryHint;

import java.util.List;

@Repository
public interface HandSetDao extends BaseRepository<HandSet, Long> {

    @Override
    @Query("select h from HandSet h order by h.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<HandSet> findAll();
    
    @Query("select h from HandSet h where h.name like %?1% or h.code like %?1% order by h.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Page<HandSet> findByCodeOrNameLike(String name, Pageable pageable);
    
    @Query("select h from HandSet h where  h.code = ?1")
	public List<HandSet> findByCode(String code);
    
    @Query("select h from HandSet h where  h.handSetStatus=?1 order by h.code")
	public List<HandSet> findHandSetStatus(HandSetStatus handSetStatus);
    
    @Query("select h from HandSet h where  h.mac = ?1")
	public HandSet findByMAC(String mac);
}




