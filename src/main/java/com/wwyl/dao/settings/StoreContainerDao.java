package com.wwyl.dao.settings;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.wwyl.Enums.StoreContainerStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.StoreContainer;

import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface StoreContainerDao extends BaseRepository<StoreContainer, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	@Query("select c from StoreContainer c where c.label like %?1% order by label")
	public Page<StoreContainer> findByLabelLike(String paramString, Pageable pageable);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	@Query("select c from StoreContainer c order by label")
	public Page<StoreContainer> findAllOrderByLabel(Pageable pageable);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Page<StoreContainer> findByStoreContainerStatusEquals(StoreContainerStatus storeContainerStatus, Pageable pageable);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	@Query("select c from StoreContainer c  where c.label=?1")
	public StoreContainer findBylabel(String label);
   
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	@Query("select container from StoreContainer container where id not in (select bookin.storeContainer.id  from BookInventory bookin)  and  label is not null and label like %?1%  order by label")
	public List<StoreContainer> findUnusedAll(String label);
    
}
