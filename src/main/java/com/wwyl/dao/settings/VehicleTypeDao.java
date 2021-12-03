package com.wwyl.dao.settings;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.VehicleType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public abstract interface VehicleTypeDao extends BaseRepository<VehicleType, Long> {

    @Override
    @Query("select v from VehicleType v order by v.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<VehicleType> findAll();

}