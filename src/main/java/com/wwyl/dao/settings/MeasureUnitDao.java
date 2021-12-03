package com.wwyl.dao.settings;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.Enums.MeasureUnitType;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.MeasureUnit;

import javax.persistence.QueryHint;

/**
 * @author liujian
 */
@Repository
public interface MeasureUnitDao extends BaseRepository<MeasureUnit, Long> {

    @Override
    @Query("select m from MeasureUnit m order by m.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<MeasureUnit> findAll();

    @Query("select o from MeasureUnit o where o.measureUnitType = ?1 ")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public List<MeasureUnit> findByMeasureUnitType(MeasureUnitType measureUnitType);

}
