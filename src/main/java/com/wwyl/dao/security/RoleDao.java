package com.wwyl.dao.security;

import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.security.Role;

/**
 * @author fyunli
 */
@Repository
public interface RoleDao extends BaseRepository<Role, Long> {

}
