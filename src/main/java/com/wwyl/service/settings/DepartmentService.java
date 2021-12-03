package com.wwyl.service.settings;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.settings.DepartmentDao;
import com.wwyl.entity.settings.Department;

/**
 * @author fyunli
 */
@Service
@Transactional(readOnly = true)
public class DepartmentService {

	@Resource
	private DepartmentDao departmentDao;

	public List<Department> findRootDepartments() {
		List<Department> departments = departmentDao.findAll();
		List<Department> rootDepartments = new ArrayList<Department>();
		for (Department department : departments) {
			if (department.getParent() == null) {
				// 过滤因left join造成的重复
				if (!rootDepartments.contains(department)) {
					rootDepartments.add(department);
				}
			}
		}
		return rootDepartments;
	}

	public List<Department> findAll() {
		return departmentDao.findAll();
	}

	public Department findOne(Long id) {
		return departmentDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(Department department) {
		if (department.getParent() != null && department.getParent().getId() == null) {
			department.setParent(null);
		}
		departmentDao.save(department);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		departmentDao.delete(id);
	}

	public boolean exists(String code) {
		return CollectionUtils.isNotEmpty(departmentDao.findByCode(code));
	}
}
