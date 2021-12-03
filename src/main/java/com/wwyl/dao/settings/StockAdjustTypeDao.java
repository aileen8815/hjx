package com.wwyl.dao.settings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.StockAdjustType;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author liujian
 */
@Repository
public interface StockAdjustTypeDao extends BaseRepository<StockAdjustType, Long> {

    @Override
    @Query("select s from StockAdjustType s order by s.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<StockAdjustType> findAll();
}
