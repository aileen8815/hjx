package com.wwyl.dao.settings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.StoreLocationType;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author fyunli
 */
@Repository
public interface StoreLocationTypeDao extends BaseRepository<StoreLocationType, Long> {

    @Override
    @Query("select s from StoreLocationType s order by s.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<StoreLocationType> findAll();
  
    @Query("select s from StoreLocationType s  where  s.code=?1")
    
    StoreLocationType findByCode(String code);
}
