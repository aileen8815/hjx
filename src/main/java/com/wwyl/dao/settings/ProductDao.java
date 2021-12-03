package com.wwyl.dao.settings;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.Product;

import javax.persistence.QueryHint;

/**
 * @author yche
 */
@Repository
public interface ProductDao extends BaseRepository<Product, Long> {

	@Query("select p from Product p where p.code like %?1% or p.name like %?1% order by p.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Page<Product> findByCodeOrNameLike(String name, Pageable pageable);

    @Override
	@Query("select distinct d from Product d left join fetch d.children c order by d.code, c.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public List<Product> findAll();

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public List<Product> findByCode(String code);
    
    @Query("select p from Product p left join fetch p.children c  where p.id= ?1 order by p.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public abstract List<Product> findByParent(Long id);
    @Query("select  p from Product p where  p.id in ?1")
 	public Set<Product> findByIds(Object[]  ids);
}
