package com.wwyl.dao.settings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.CarrierType;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author liujian
 */
@Repository
public interface CarrierTypeDao extends BaseRepository<CarrierType, Long> {

    @Override
    @Query("select c from CarrierType c order by c.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<CarrierType> findAll();

}
