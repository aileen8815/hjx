package com.wwyl.dao.security;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.security.OperationLog;
import org.springframework.stereotype.Repository;

/**
 * Created by fyunli on 15/2/3.
 */
@Repository
public interface OperationLogDao extends BaseRepository<OperationLog, Long> {
}
