package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.settings.PackingDao;
import com.wwyl.entity.settings.Packing;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class PackingService {

	@Resource
	private PackingDao packingDao;

	public List<Packing> findAll() {
		return packingDao.findAll();
	}

	public Packing findOne(Long id) {
		return packingDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(Packing packing) {
		packingDao.save(packing);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		packingDao.delete(id);
	}

}
