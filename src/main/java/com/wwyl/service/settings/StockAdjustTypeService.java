package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.settings.StockAdjustTypeDao;
import com.wwyl.entity.settings.StockAdjustType;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class StockAdjustTypeService {

	@Resource
	private StockAdjustTypeDao stockAdjustTypeDao;

	public List<StockAdjustType> findAll() {
		return stockAdjustTypeDao.findAll();
	}

	public StockAdjustType findOne(Long id) {
		return stockAdjustTypeDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(StockAdjustType stockAdjustType) {
		stockAdjustTypeDao.save(stockAdjustType);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		stockAdjustTypeDao.delete(id);
	}

}
