package com.wwyl.service.ce;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.ce.CEFeeItemDao;
import com.wwyl.entity.ce.CEFeeItem;

/**
 * @author pixf
 */

@Service
public class CEFeeItemService {
	
	@Resource
	private CEFeeItemDao ceFeeItemDao;
	
	public List<CEFeeItem> findAll() {
		return ceFeeItemDao.findAll();
	}

	public CEFeeItem findOne(Long id) {
		return ceFeeItemDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(CEFeeItem feeItem) {
		ceFeeItemDao.save(feeItem);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		ceFeeItemDao.delete(id);
	}

}
