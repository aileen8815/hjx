package com.wwyl.service.truck;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.truck.ResourceManagementDao;
import com.wwyl.entity.truck.ResourceManagement;

@Service
@Transactional(readOnly = true)
public class ResourceManagementService {

	@Resource
	private ResourceManagementDao resourceManagementDao;

	public List<ResourceManagement> findAll() {
		return this.resourceManagementDao.findAll();
	}

	public ResourceManagement findOne(Long id) {
		return (ResourceManagement) this.resourceManagementDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(ResourceManagement resourceManagement) {
		this.resourceManagementDao.save(resourceManagement);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		this.resourceManagementDao.delete(id);
	}
	
}
