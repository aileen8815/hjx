package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.settings.CustomerGradeDao;
import com.wwyl.entity.settings.CustomerGrade;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class CustomerGradeService {

	@Resource
	private CustomerGradeDao customerGradeDao;

	public List<CustomerGrade> findAll() {
		return customerGradeDao.findAll();
	}

	public CustomerGrade findOne(Long id) {
		return customerGradeDao.findOne(id);
	}
	
	public List<CustomerGrade> findHighLowDefaultGrade() {
		return customerGradeDao.findHighLowDefaultGrade();
	}

	@Transactional(readOnly = false)
	public void save(CustomerGrade customerGrade) {
		customerGradeDao.save(customerGrade);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		customerGradeDao.delete(id);
	}

}
