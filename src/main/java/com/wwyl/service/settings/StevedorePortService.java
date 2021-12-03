package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.StevedorePortDao;
import com.wwyl.entity.settings.StevedorePort;

/**
 * @author zhaozz
 */
@Service
@Transactional(readOnly = true)
public class StevedorePortService {

	@Resource
	private StevedorePortDao stevedorePortDao;

	public List<StevedorePort> findAll() {
		return stevedorePortDao.findAll();
	}

	public StevedorePort findOne(Long id) {
		return stevedorePortDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(StevedorePort stevedorePort) {
		stevedorePortDao.save(stevedorePort);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		stevedorePortDao.delete(id);
	}
	
	public Page<StevedorePort> findByCodeOrNameLike(int page, int rows, String name) {
		return stevedorePortDao.findByCodeOrNameLike(name, RepoUtils.buildPageRequest(page, rows));
	}
	public boolean exists(String code) {
		return CollectionUtils.isNotEmpty(stevedorePortDao.findByCode(code));
	}
}

