package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.CarrierDao;
import com.wwyl.entity.settings.Carrier;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class CarrierService {

	@Resource
	private CarrierDao carrierDao;

	public List<Carrier> findAll() {
		return carrierDao.findAll();
	}

	public Carrier findOne(Long id) {
		return carrierDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(Carrier carrier) {
		carrierDao.save(carrier);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		carrierDao.delete(id);
	}

	public Page<Carrier> findByCodeOrShortNameLike(int page, int rows, String name) {
		return carrierDao.findByCodeOrShortNameLike(name, RepoUtils.buildPageRequest(page, rows));
	}

}
