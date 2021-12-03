package com.wwyl.dao.settings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.CustomerGrade;

import javax.persistence.QueryHint;
import java.util.List;


/**
 * @author liujian
 */
@Repository
public interface CustomerGradeDao extends BaseRepository<CustomerGrade, Long> {

    @Override
    @Query("select g from CustomerGrade g order by g.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<CustomerGrade> findAll();

    @Query("select g from CustomerGrade g where g.code = 1 or g.code= 2 or g.code= 15 ")
    List<CustomerGrade> findHighLowDefaultGrade();

}
