package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.settings.TallyAreaTypeDao;
import com.wwyl.entity.settings.TallyAreaType;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class TallyAreaTypeService {

	@Resource
	private TallyAreaTypeDao tallyAreaTypeDao;

	public List<TallyAreaType> findAll() {
		return tallyAreaTypeDao.findAll();
	}

	public TallyAreaType findOne(Long id) {
		return tallyAreaTypeDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(TallyAreaType TallyAreaType) {
		tallyAreaTypeDao.save(TallyAreaType);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		tallyAreaTypeDao.delete(id);
	}

}
