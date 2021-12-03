package com.wwyl.dao.settings;

import java.util.List;
import java.util.Set;

import com.wwyl.Enums.StoreLocationStatus;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.StoreLocation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public abstract interface StoreLocationDao extends BaseRepository<StoreLocation, Long> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	@Query("select s from StoreLocation s where s.code like %?1% order by s.coordX, s.coordY, s.coordZ asc")
	public  Page<StoreLocation> findByCodeLike(String code, Pageable paramPageable);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Set<StoreLocation> findByCodeIn(String[] codes);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	@Query("select s from StoreLocation s where s.code=?1")
	public StoreLocation findByCode(String code);
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	@Query("select s from StoreLocation s where s.storeArea.id = ?1  and s.storeLocationType.code=?2")
	public StoreLocation findByStoreArea(Long storeArea,String code);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	@Query("select s from StoreLocation s where s.code=?1  and  s.storeLocationStatus in ?2")
	public StoreLocation findByCodeAndStatus(String code,StoreLocationStatus[] storeLocationStatus);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	@Query("select s from StoreLocation s where s.label=?1  and  s.storeLocationStatus in ?2")
	public StoreLocation findByLabelAndStatus(String label,StoreLocationStatus[] storeLocationStatus);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	@Query("select s from StoreLocation s where s.storeArea.id = ?1 order by s.coordX, s.coordY, s.coordZ desc")
	public List<StoreLocation> findByArea(Long areaId);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	@Query("select s from StoreLocation s where s.storeArea.id in ?1 order by s.coordX, s.coordY, s.coordZ desc")
	public List<StoreLocation> findByAreas(Long[] areaId);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public Page<StoreLocation> findByStoreLocationStatusEquals(StoreLocationStatus storeContainerStatus, Pageable pageable);

 	@Modifying
 	@Query("update StoreLocation store set store.storeLocationStatus=?2 where store.id=?1")
 	public int updateStoreLocationStatus(Long storeLocationId,StoreLocationStatus storeLocationStatus); 
 	
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	@Query("select s from StoreLocation s where s.storeArea.id = ?1 and  s.storeLocationType.code=?2")
	public List<StoreLocation> findByAreasAndcode(Long areaId  ,String code);

}
