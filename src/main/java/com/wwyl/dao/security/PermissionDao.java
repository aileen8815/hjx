package com.wwyl.dao.security;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.security.Permission;

/**
 * @author fyunli
 */
public interface PermissionDao extends BaseRepository<Permission, Long> {

	@Override
	@Query("select p from Permission p left join fetch p.children order by p.code")
	public List<Permission> findAll();

}
