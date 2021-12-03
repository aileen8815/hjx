package com.wwyl.dao.settings;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.StoreArea;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public abstract interface StoreAreaDao extends BaseRepository<StoreArea, Long> {

    @Override
    @Query("select distinct d from StoreArea d left join fetch d.children c order by d.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<StoreArea> findAll();

    @Query("select d from StoreArea d left join fetch d.children c order by d.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public abstract List<StoreArea> findAllWithChildren();

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public abstract List<StoreArea> findByCode(String paramString);

    @Query("select s from StoreArea s left join fetch s.children c  where s.id= ?1 order by s.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public abstract List<StoreArea> findByParent(Long id);

    @Query("select s from StoreArea s where s.storeAreaStatus='0' order by s.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public abstract List<StoreArea> findValidStoreArea();

    @Query("select a from StoreArea a inner join a.products p where p.id in ?1")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<StoreArea> findByProduct(Long[] productIds);
    
    //有效高温库查询(控制客户部门权限)
    @Query("select s from StoreArea s where s.storeAreaStatus='0' and (s.id ='1' or s.parent ='1') order by s.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public abstract List<StoreArea> findByValidHighArea();
    
    //有效低温库查询(控制客户部门权限)
    @Query("select s from StoreArea s where s.storeAreaStatus='0' and (s.id ='16' or s.parent ='16') order by s.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public abstract List<StoreArea> findByValidLowArea();

}
