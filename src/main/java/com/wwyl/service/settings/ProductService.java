package com.wwyl.service.settings;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.CustomerDao;
import com.wwyl.dao.settings.ProductDao;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.Product;
 

/**
 * @author yche
 */
@Service
@Transactional(readOnly = true)
public class ProductService {
	@Resource
	private ProductDao productDao;
	@Resource
	private CustomerDao customerDao;
	
	public List<Product> findAll() {
		List<Product> productList=productDao.findAll();
		List<Product> productListed=new ArrayList<Product>();
		for (Product product : productList) {
			//if(!productListed.contains(product)){
				productListed.add(product);
			//}
		}
		return productListed;
	}

	public Product findOne(Long id) {
		return productDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public Product save(Product product) {
		if (product.getParent() != null && product.getParent().getId() == null) {
			product.setParent(null);
		}
		return productDao.save(product);
	 
	}
	
	@Transactional(readOnly = false)
	public Product saveCustomerProduct(Product product,Long customerId) {
		Customer customer=customerDao.findOne(customerId);
		String productCode = customer.getProductCode();
		product.setCode(productCode);
		productDao.save(product);
		customer.getProducts().add(product);
		customerDao.save(customer);
		return product;
	}
	@Transactional(readOnly = false)
	public void delete(Long id) {
		productDao.delete(id);
	}

	public Page<Product> findByCodeOrNameLike(int page, int rows, String name) {
		return productDao.findByCodeOrNameLike(name, RepoUtils.buildPageRequest(page, rows));
	}
	public List<Product> findRootProducts() {
		List<Product> products = productDao.findAll();
		List<Product> rootProducts = new ArrayList<Product>();
		for (Product product : products) {
			if (product.getParent() == null) {
				// 过滤因left join造成的重复
				//if (!rootProducts.contains(product)) {
					rootProducts.add(product);
				//}
			}
		}
		return rootProducts;
	}
	public boolean exists(String code) {
		return CollectionUtils.isNotEmpty(productDao.findByCode(code));
	}
	
    /**
     * 获取选中下面的子节点，或所有节点
     */
    public List<Long> getProducts(Long product){
    	List<Long>  products=new ArrayList<Long>();
    	if(product!=null){
    		List<Product>	productlist=productDao.findByParent(product);
			if(CollectionUtils.isNotEmpty(productlist)){
				Product productobj=productlist.get(0);
				products.addAll(productobj.getTreeNodeIds());
			}
			
		}else{
		List<Product>	productList=productDao.findAll();
			if(CollectionUtils.isNotEmpty(productList)){
				 for (Product product2 : productList) {
					 products.add(product2.getId());
				}
			}
		}
    	return products;
    }
    
    
    
}
