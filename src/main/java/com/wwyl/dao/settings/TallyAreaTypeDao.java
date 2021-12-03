package com.wwyl.dao.settings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.TallyAreaType;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author liujian
 */
@Repository
public interface TallyAreaTypeDao extends BaseRepository<TallyAreaType, Long> {

    @Override
    @Query("select t from TallyAreaType t order by t.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<TallyAreaType> findAll();
}
