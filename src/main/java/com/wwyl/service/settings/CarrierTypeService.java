package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wwyl.dao.settings.CarrierTypeDao;
import com.wwyl.entity.settings.CarrierType;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class CarrierTypeService {

	@Resource
	private CarrierTypeDao carrierTypeDao;

	public List<CarrierType> findAll() {
		return carrierTypeDao.findAll();
	}

	public CarrierType findOne(Long id) {
		return carrierTypeDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(CarrierType CarrierType) {
		carrierTypeDao.save(CarrierType);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		carrierTypeDao.delete(id);
	}

}
