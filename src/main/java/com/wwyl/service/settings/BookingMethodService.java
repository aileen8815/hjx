package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.settings.BookingMethodDao;
import com.wwyl.entity.settings.BookingMethod;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class BookingMethodService {

	@Resource
	private BookingMethodDao BookingMethodDao;

	public List<BookingMethod> findAll() {
		return BookingMethodDao.findAll();
	}

	public BookingMethod findOne(Long id) {
		return BookingMethodDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(BookingMethod BookingMethod) {
		BookingMethodDao.save(BookingMethod);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		BookingMethodDao.delete(id);
	}

}
