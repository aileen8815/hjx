package com.wwyl.dao.settings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.Packing;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author liujian
 */
@Repository
public interface PackingDao extends BaseRepository<Packing, Long> {

    @Override
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select p from Packing p order by p.code")
    List<Packing> findAll();

}
