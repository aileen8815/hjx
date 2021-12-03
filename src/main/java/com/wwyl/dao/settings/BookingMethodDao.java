package com.wwyl.dao.settings;

import com.wwyl.entity.settings.Area;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.BookingMethod;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author liujian
 */
@Repository
public interface BookingMethodDao extends BaseRepository<BookingMethod, Long> {

    @Override
    @Query("select m from BookingMethod m order by m.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public List<BookingMethod> findAll();

}
