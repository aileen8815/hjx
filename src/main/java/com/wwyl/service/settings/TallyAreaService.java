package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.TallyAreaDao;
import com.wwyl.entity.settings.TallyArea;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class TallyAreaService {

	@Resource
	private TallyAreaDao tallyAreaDao;

	public List<TallyArea> findAll() {
		return tallyAreaDao.findAll();
	}

	public TallyArea findOne(Long id) {
		return tallyAreaDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(TallyArea tallyArea) {
		tallyAreaDao.save(tallyArea);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		tallyAreaDao.delete(id);
	}

	public Page<TallyArea> findByCodeOrNameLike(int page, int rows, String name) {
		return tallyAreaDao.findByCodeOrNameLike(name, RepoUtils.buildPageRequest(page, rows));
	}
	public boolean exists(String code) {
		return CollectionUtils.isNotEmpty(tallyAreaDao.findByCode(code));
	}
}
