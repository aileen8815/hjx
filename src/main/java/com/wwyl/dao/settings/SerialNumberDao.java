package com.wwyl.dao.settings;

import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.SerialNumber;

/**
 * @author fyunli
 */
@Repository
public interface SerialNumberDao extends BaseRepository<SerialNumber, Long> {

}
