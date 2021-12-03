package com.wwyl.dao.settings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.Carrier;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author liujian
 */
@Repository
public interface CarrierDao extends BaseRepository<Carrier, Long> {

    @Query("select o from Carrier o where o.code like %?1% or o.shortName like %?1% ")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public Page<Carrier> findByCodeOrShortNameLike(String fullName, Pageable pageable);

    @Override
    @Query("select c from Carrier c order by c.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Carrier> findAll();

}
