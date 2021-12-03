package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wwyl.dao.settings.StoreLocationTypeDao;
import com.wwyl.entity.settings.StoreLocationType;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class StoreLocationTypeService {

	@Resource
	private StoreLocationTypeDao storeLocationTypeDao;

	public List<StoreLocationType> findAll() {
		return storeLocationTypeDao.findAll();
	}

	public StoreLocationType findOne(Long id) {
		return storeLocationTypeDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(StoreLocationType StoreLocationType) {
		storeLocationTypeDao.save(StoreLocationType);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		storeLocationTypeDao.delete(id);
	}

}
