package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.settings.ChargeTypeDao;
import com.wwyl.entity.settings.ChargeType;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class ChargeTypeService {

	@Resource
	private ChargeTypeDao chargeTypeDao;

	public List<ChargeType> findAll() {
		return chargeTypeDao.findAll();
	}

	public ChargeType findOne(Long id) {
		return chargeTypeDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(ChargeType ChargeType) {
		chargeTypeDao.save(ChargeType);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		chargeTypeDao.delete(id);
	}

}
