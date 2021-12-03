package com.wwyl.service.settings;

import com.wwyl.dao.settings.ProductCategoryDao;
import com.wwyl.entity.settings.ProductCategory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductCategoryService {

	@Resource
	private ProductCategoryDao productCategoryDao;

	public List<ProductCategory> findRootProductCategorys() {
		List<ProductCategory> ProductCategorys = this.productCategoryDao.findAll();
		List<ProductCategory> rootProductCategorys = new ArrayList<ProductCategory>();
		for (ProductCategory ProductCategory : ProductCategorys) {
			if (ProductCategory.getParent() == null) {
				if (!rootProductCategorys.contains(ProductCategory)) {
					rootProductCategorys.add(ProductCategory);
				}
			}
		}

		return rootProductCategorys;
	}

	public List<ProductCategory> findAll() {
		return this.productCategoryDao.findAll();
	}

	public ProductCategory findOne(Long id) {
		return (ProductCategory) this.productCategoryDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(ProductCategory productCategory) {
		if ((productCategory.getParent() != null) && (productCategory.getParent().getId() == null)) {
			productCategory.setParent(null);
		}
		this.productCategoryDao.save(productCategory);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		this.productCategoryDao.delete(id);
	}

	public boolean exists(String code) {
		return CollectionUtils.isNotEmpty(this.productCategoryDao.findByCode(code));
	}
}