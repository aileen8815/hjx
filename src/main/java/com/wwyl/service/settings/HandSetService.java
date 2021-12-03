package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.Enums.HandSetStatus;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.HandSetDao;
import com.wwyl.entity.settings.HandSet;

/**
 * @author zhaozz
 */
@Service
@Transactional(readOnly = true)
public class HandSetService {

	@Resource
	private HandSetDao handSetDao;

	public List<HandSet> findAll() {
		return handSetDao.findAll();
	}
	public List<HandSet> findHandSetStatus(HandSetStatus handSetStatus) {
		return handSetDao.findHandSetStatus(handSetStatus);
	}
	
	
	public HandSet findOne(Long id) {
		return handSetDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(HandSet HandSet) {
		handSetDao.save(HandSet);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		handSetDao.delete(id);
	}
	
	public Page<HandSet> findByCodeOrNameLike(int page, int rows, String name) {
		return handSetDao.findByCodeOrNameLike(name, RepoUtils.buildPageRequest(page, rows));
	}
	
	public boolean exists(String code) {
		return CollectionUtils.isNotEmpty(handSetDao.findByCode(code));
	}
	
	public HandSet findByMac(String mac){
		return handSetDao.findByMAC(mac);	
	}
}

