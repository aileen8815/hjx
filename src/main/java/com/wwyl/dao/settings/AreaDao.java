package com.wwyl.dao.settings;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.Area;

import javax.persistence.QueryHint;

/**
 * @author liujian
 */
@Repository
public interface AreaDao extends BaseRepository<Area, Long> {

	@Override
	@Query("select a from Area a left join fetch a.children c order by a.code, c.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public List<Area> findAll();

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public List<Area> findByCode(String code);

}
