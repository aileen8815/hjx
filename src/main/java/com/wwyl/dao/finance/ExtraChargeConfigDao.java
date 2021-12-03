package com.wwyl.dao.finance;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.finance.ExtraChargeConfig;
/**
 * @author jianl
 */
@Repository
public interface ExtraChargeConfigDao extends BaseRepository<ExtraChargeConfig,Long>{
	
	@Query("select p from ExtraChargeConfig p where p.feeItem.id=?1  order by  p.code") 
	public List<ExtraChargeConfig> findExtraChargeConfigs(Long feeItemId );	
	
 
}
