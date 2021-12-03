package com.wwyl.dao.truck;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.truck.ResourceManagement;

@Repository
public interface ResourceManagementDao extends BaseRepository<ResourceManagement, Long>{
	
	
    @Query("select m from ResourceManagement m order by m.vehicleNo")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    public List<ResourceManagement> findAll();
}
 