package com.wwyl.service.settings;

import com.wwyl.dao.settings.VehicleTypeDao;
import com.wwyl.entity.settings.VehicleType;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VehicleTypeService {

	@Resource
	private VehicleTypeDao vehicleTypeDao;

	public List<VehicleType> findAll() {
		return this.vehicleTypeDao.findAll();
	}

	public VehicleType findOne(Long id) {
		return (VehicleType) this.vehicleTypeDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(VehicleType vehicleType) {
		this.vehicleTypeDao.save(vehicleType);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		this.vehicleTypeDao.delete(id);
	}

}