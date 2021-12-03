package com.wwyl.dao.settings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.ChargeType;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author liujian
 */
@Repository
public interface ChargeTypeDao extends BaseRepository<ChargeType, Long> {

    @Override
    @Query("select c from ChargeType c order by c.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<ChargeType> findAll();

}
