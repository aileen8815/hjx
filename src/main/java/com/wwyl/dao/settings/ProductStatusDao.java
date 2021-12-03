package com.wwyl.dao.settings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.ProductStatus;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author liujian
 */
@Repository
public interface ProductStatusDao extends BaseRepository<ProductStatus, Long> {

    @Override
    @Query("select p from ProductStatus p order by p.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<ProductStatus> findAll();
}
