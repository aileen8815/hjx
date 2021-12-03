package com.wwyl.service.settings;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wwyl.dao.settings.StockWastageTypeDao;
import com.wwyl.entity.settings.StockWastageType;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class StockWastageTypeService {

	@Resource
	private StockWastageTypeDao stockWastageTypeDao;

	public List<StockWastageType> findAll() {
		return stockWastageTypeDao.findAll();
	}

	public StockWastageType findOne(Long id) {
		return stockWastageTypeDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(StockWastageType stockWastageType) {
		stockWastageTypeDao.save(stockWastageType);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		stockWastageTypeDao.delete(id);
	}

}
