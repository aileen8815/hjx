package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.settings.StoreContainerTypeDao;
import com.wwyl.entity.settings.StoreContainerType;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class StoreContainerTypeService {

	@Resource
	private StoreContainerTypeDao storeContainerTypeDao;

	public List<StoreContainerType> findAll() {
		return storeContainerTypeDao.findAll();
	}

	public StoreContainerType findOne(Long id) {
		return storeContainerTypeDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(StoreContainerType StoreContainerType) {
		storeContainerTypeDao.save(StoreContainerType);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		storeContainerTypeDao.delete(id);
	}

}
