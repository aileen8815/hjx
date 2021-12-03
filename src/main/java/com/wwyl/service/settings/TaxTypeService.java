package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.settings.TaxTypeDao;
import com.wwyl.entity.settings.TaxType;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class TaxTypeService {

    @Resource
    private TaxTypeDao taxTypeDao;

    @Cacheable(value = "com.wwyl.entity.settings.TaxType")
    public List<TaxType> findAll() {
        return taxTypeDao.findAll();
    }

    @Cacheable(value = "com.wwyl.entity.settings.TaxType")
    public TaxType findOne(Long id) {
        return taxTypeDao.findOne(id);
    }

    @Transactional(readOnly = false)
    @CacheEvict(value = "com.wwyl.entity.settings.TaxType", allEntries = true)
    public void save(TaxType TaxType) {
        taxTypeDao.save(TaxType);
    }

    @Transactional(readOnly = false)
    @CacheEvict(value = "com.wwyl.entity.settings.TaxType", allEntries = true)
    public void delete(Long id) {
        taxTypeDao.delete(id);
    }

}
