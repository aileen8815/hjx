package com.wwyl.service.settings;

import com.wwyl.Constants;
import com.wwyl.Enums.StoreLocationStatus;
import com.wwyl.dao.settings.ProductDao;
import com.wwyl.dao.settings.StoreAreaDao;
import com.wwyl.dao.settings.StoreLocationDao;
import com.wwyl.dao.settings.StoreLocationTypeDao;
import com.wwyl.dao.store.StoreAreaAssigneeDao;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.settings.StoreLocationType;
import com.wwyl.entity.store.StoreAreaAssignee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StoreAreaService {

    @Resource
    private StoreAreaDao storeAreaDao;
    @Resource
    private StoreAreaAssigneeDao storeAreaAssigneeDao;
    @Resource
    private StoreLocationTypeDao storeLocationTypeDao;
    @Resource
    private StoreLocationDao storeLocationDao;
    @Resource
    private CustomerService customerService;
    @Resource
    private ProductDao productDao;

    public List<StoreArea> findAll() {
        return this.storeAreaDao.findAll();
    }

    public StoreArea findOne(Long id) {
        return (StoreArea) this.storeAreaDao.findOne(id);
    }

    public StoreArea findOneWithChildren(Long id) {
        List<StoreArea> storeArea = this.storeAreaDao.findByParent(id);
        return CollectionUtils.isNotEmpty(storeArea) ? storeArea.get(0) : null;
    }

    @Transactional(readOnly = false)
    public void delete(Long id) {
        StoreLocation storeLocation = storeLocationDao.findByStoreArea(id, Constants.STORE_LOCATION_TYPE_TURNOVER);
        if (storeLocation != null) {
            storeLocationDao.delete(storeLocation);
        }
        this.storeAreaDao.delete(id);
    }

    public List<StoreArea> findRootStoreAreas() {
        List<StoreArea> storeAreas = this.storeAreaDao.findAllWithChildren();
        List<StoreArea> rootStoreAreas = new ArrayList<StoreArea>();
        for (StoreArea storeArea : storeAreas) {
            if (storeArea.getParent() == null) {
                // 过滤因left join造成的重复
                if (!rootStoreAreas.contains(storeArea)) {
                    rootStoreAreas.add(storeArea);
                }
            }
        }

        return rootStoreAreas;
    }

    @Transactional(readOnly = false)
    public void save(StoreArea storeArea, Long[] operatorIds) {
        //新增一个默认周转区
        if (storeArea.getId() == null) {
            StoreLocationType storeLocationType = storeLocationTypeDao.findByCode(Constants.STORE_LOCATION_TYPE_TURNOVER);
            if (storeLocationType != null) {
                StoreLocation storeLocation = new StoreLocation();
                storeLocation.setCode(storeArea.getCode() + "-00-00-00");
                storeLocation.setStoreArea(storeArea);
                storeLocation.setStoreLocationType(storeLocationType);
                storeLocation.setStoreLocationStatus(StoreLocationStatus.可使用);
                storeLocation.setRemark("临时放置托盘的区域");
                storeLocationDao.save(storeLocation);
            }
        } else {
            storeArea.setProducts(storeAreaDao.findOne(storeArea.getId()).getProducts());
        }
        if ((storeArea.getParent() != null) && (storeArea.getParent().getId() == null)) {
            storeArea.setParent(null);
        }
        this.storeAreaDao.save(storeArea);

        List<StoreAreaAssignee> storeAreaAssigneeList = storeAreaAssigneeDao.findBystoreAreaId(storeArea.getId());
        if (CollectionUtils.isNotEmpty(storeAreaAssigneeList)) {
            for (StoreAreaAssignee storeAreaAssignee2 : storeAreaAssigneeList) {
                storeAreaAssigneeDao.delete(storeAreaAssignee2.getId());
            }
        }
        StoreAreaAssignee storeAreaAssignee = null;
        Operator operator = null;
        if (operatorIds != null) {
            for (Long operatorId : operatorIds) {
                storeAreaAssignee = new StoreAreaAssignee();
                operator = new Operator();
                operator.setId(operatorId);
                storeAreaAssignee.setStoreArea(storeArea);
                storeAreaAssignee.setOperator(operator);
                storeAreaAssigneeDao.save(storeAreaAssignee);
            }
        }

    }

    public boolean exists(String code) {
        return CollectionUtils.isNotEmpty(this.storeAreaDao.findByCode(code));
    }

    public List<StoreArea> findValidStoreArea() {
        return storeAreaDao.findValidStoreArea();
    }

    public List<StoreArea> findByValidHighArea() {
        return storeAreaDao.findByValidHighArea();
    }
    
    public List<StoreArea> findByValidLowArea() {
        return storeAreaDao.findByValidLowArea();
    }
    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    public List<?> findAreaStat(Long[] areaIds) {
        return this.findAreaStat(areaIds, true);
    }

    public List<?> findAreaStat(Long[] areaIds, boolean withChildren) {
        Map<String, Object> params = null;
        if (ArrayUtils.isNotEmpty(areaIds)) {
            List<Long> allAreas = new ArrayList<Long>();
            for (Long areaId : areaIds) {
                if (withChildren) {
                    StoreArea storeArea = this.findOneWithChildren(areaId);
                    allAreas.addAll(storeArea.getTreeNodeIds());
                } else {
                    allAreas.add(areaId);
                }
            }
            params = new HashMap<String, Object>();
            params.put("areaIds", allAreas);
        }
        return sqlSessionTemplate.selectList("findStoreAreaStat", params);
    }

    public List<StoreArea> findByProduct(Long[] productIds) {
        return storeAreaDao.findByProduct(productIds);
    }

    /**
     * 获取选中下面的子节点，或所有节点
     */
    public List<Long> getStoreAreas(Long storeArea) {
        List<Long> storeAreas = new ArrayList<Long>();
        if (storeArea != null) {
            StoreArea storeAreaObj = this.findOneWithChildren(storeArea);
            storeAreas.addAll(storeAreaObj.getTreeNodeIds());
        } else {
            List<StoreArea> storeAreaList = storeAreaDao.findAll();
            if (CollectionUtils.isNotEmpty(storeAreaList)) {
                for (StoreArea storeArea2 : storeAreaList) {
                    storeAreas.add(storeArea2.getId());
                }
            }
        }
        return storeAreas;
    }

    /**
     * 添加库区可存放商品
     */
    @Transactional(readOnly = false)
    public void addproduct(Long[] productIds, Long storeAreaId, Long customerId) {
        StoreArea storeArea = storeAreaDao.findOne(storeAreaId);
        Set<Product> products = customerService.findOne(customerId).getProducts();
        Set<Product> storeAreaProducts = storeArea.getProducts();
        for (Product product : products) {
            storeAreaProducts.remove(product);
        }
        Set<Product> checkproducts = productDao.findByIds(productIds);
        storeAreaProducts.addAll(checkproducts);
        storeAreaDao.save(storeArea);
    }

    /**
     * 删除已添加的库区存放商品
     */
    @Transactional(readOnly = false)
    public void delStoreProduct(Long productId, Long storeAreaId) {
        StoreArea storeArea = storeAreaDao.findOne(storeAreaId);
        Set<Product> storeAreaProducts = storeArea.getProducts();
        Product product = productDao.findOne(productId);
        storeAreaProducts.remove(product);
        storeAreaDao.save(storeArea);
    }

}