package com.wwyl.dao.settings;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.wwyl.dao.BaseRepository;
import com.wwyl.entity.settings.Department;

import javax.persistence.QueryHint;

/**
 * @author fyunli
 */
@Repository
public interface DepartmentDao extends BaseRepository<Department, Long> {

	@Override
	@Query("select d from Department d left join fetch d.children c order by d.code")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public List<Department> findAll();

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public List<Department> findByCode(String code);

}
