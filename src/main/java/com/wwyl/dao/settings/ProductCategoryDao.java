package com.wwyl.dao.settings;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.ProductCategory;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public  interface ProductCategoryDao extends BaseRepository<ProductCategory, Long> {

    @Override
    @Query("select distinct p from ProductCategory p left join fetch p.children c order by p.code")
    public abstract List<ProductCategory> findAll();

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public abstract List<ProductCategory> findByCode(String paramString);

}