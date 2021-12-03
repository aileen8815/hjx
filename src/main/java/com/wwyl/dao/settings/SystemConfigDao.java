package com.wwyl.dao.settings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wwyl.Enums.SystemConfigKey;
import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.SystemConfig;
 

/**
 * @author liujian
 */
@Repository
public interface SystemConfigDao extends BaseRepository<SystemConfig, Long> {

     
    @Query("select s from SystemConfig s   where  s.attribute=?1")
    SystemConfig findByAttribute(SystemConfigKey systemConfigKey);
}
