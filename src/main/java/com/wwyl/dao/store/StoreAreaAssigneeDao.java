package com.wwyl.dao.store;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.store.StoreAreaAssignee;

/**
 * @author liujian
 */
@Repository
public interface StoreAreaAssigneeDao extends BaseRepository<StoreAreaAssignee, Long> {
 
	
	@Query("select  o from StoreAreaAssignee o where o.operator.id= ?1 ")
	public List<StoreAreaAssignee> findByOperatorId(Long id);
	@Modifying
	@Query("delete  from StoreAreaAssignee o where o.storeArea.id =?1 ")
	public int deleteBystoreAreaId(Long storeAreaId);
	
	@Modifying
	@Query("delete  from StoreAreaAssignee  where  operator.id =?1 ")
	public int deleteByOperatorId(Long operatorId);
	@Query("select o from StoreAreaAssignee o where o.storeArea.id =?1 ")
	public List<StoreAreaAssignee> findBystoreAreaId(Long storeAreaId);

}
