package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.settings.ProductStatusDao;
import com.wwyl.entity.settings.ProductStatus;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class ProductStatusService {

	@Resource
	private ProductStatusDao productStatusDao;

	public List<ProductStatus> findAll() {
		return productStatusDao.findAll();
	}

	public ProductStatus findOne(Long id) {
		return productStatusDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(ProductStatus productStatus) {
		productStatusDao.save(productStatus);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		productStatusDao.delete(id);
	}

}
