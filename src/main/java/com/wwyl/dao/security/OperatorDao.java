package com.wwyl.dao.security;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.security.Operator;

/**
 * @author fyunli
 */
@Repository
public interface OperatorDao extends BaseRepository<Operator, Long> {

	public List<Operator> findByUsername(String username);

	@Query("select o from Operator o inner join o.roles r where r.code = ?1")
	public List<Operator> findByRole(String roleCode);

	@Query("select o from Operator o inner join o.roles r inner join r.permissions p where p.code = ?1")
	public List<Operator> findByPermission(String permCode);
	
	public List<Operator> findByUsernameLike(String username);

}
