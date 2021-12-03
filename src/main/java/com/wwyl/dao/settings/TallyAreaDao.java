package com.wwyl.dao.settings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.TallyArea;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author liujian
 */
@Repository
public interface TallyAreaDao extends BaseRepository<TallyArea, Long> {

    @Override
    @Query("select t from TallyArea t order by t.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<TallyArea> findAll();

    @Query("select o from TallyArea o where o.name like %?1% or o.code like %?1% order by o.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Page<TallyArea> findByCodeOrNameLike(String name, Pageable pageable);
    
    @Query("select t from TallyArea t where  t.code = ?1 order by t.code")
	public List<TallyArea> findByCode(String code);

}
