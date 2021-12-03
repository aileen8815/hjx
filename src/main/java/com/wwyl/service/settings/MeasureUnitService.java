package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Enums.MeasureUnitType;
import com.wwyl.dao.settings.MeasureUnitDao;
import com.wwyl.entity.settings.MeasureUnit;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class MeasureUnitService {

	@Resource
	private MeasureUnitDao measureUnitDao;

	public List<MeasureUnit> findAll() {
		return measureUnitDao.findAll();
	}

	public List<MeasureUnit> findByMeasureUnitType(MeasureUnitType measureUnitType) {
		return measureUnitDao.findByMeasureUnitType(measureUnitType);
	}

	public MeasureUnit findOne(Long id) {
		return measureUnitDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(MeasureUnit measureUnit) {
		measureUnitDao.save(measureUnit);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		measureUnitDao.delete(id);
	}

}
