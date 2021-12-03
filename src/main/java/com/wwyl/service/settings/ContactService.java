package com.wwyl.service.settings;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wwyl.dao.DynamicSpecifications;
import com.wwyl.dao.PropertyFilter;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.ContactDao;
import com.wwyl.entity.settings.Contact;


/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class ContactService {


	@Resource
	private ContactDao contactDao;

	public List<Contact> findAll() {
		return contactDao.findAll();
	}

	public Contact findOne(Long id) {
		return contactDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void save(Contact contact) {
		contactDao.save(contact);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		contactDao.delete(id);
	}
	public List<Contact> findContactsByFilter(List<PropertyFilter> filters) {
		Specification<Contact> spec = DynamicSpecifications.buildSpecifitions(Contact.class, filters);
		return contactDao.findAll(spec);
	}
	
	public Page<Contact> findByLinkmanLike(int page, int rows, String name) {
		return contactDao.findByLinkmanLike("%" + name + "%", RepoUtils.buildPageRequest(page, rows));
	}
}
