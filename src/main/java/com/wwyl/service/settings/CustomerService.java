package com.wwyl.service.settings;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.myemsp.security.Des;

import com.wwyl.ThreadLocalHolder;
import com.wwyl.Enums.CustomerGradeStatus;
import com.wwyl.Enums.PaymentStatus;
import com.wwyl.dao.DynamicSpecifications;
import com.wwyl.dao.PropertyFilter;
import com.wwyl.dao.RepoUtils;
import com.wwyl.dao.settings.ContactDao;
import com.wwyl.dao.settings.CustomerDao;
import com.wwyl.dao.settings.ProductDao;
import com.wwyl.entity.finance.Payment;
import com.wwyl.entity.settings.Contact;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.store.OutboundRegister;
import com.wwyl.service.ws.AuctionToCrmClient;
import com.wwyl.service.ws.CustomerInfo;
import com.wwyl.service.ws.TraceInfo;
import com.wwyl.util.MD5Encryption;

/**
 * @author liujian
 */
@Service
@Transactional(readOnly = true)
public class CustomerService {


	@Resource
	private CustomerDao customerDao;
	@Resource
	private ContactDao contactDao;
	@Resource
	private AuctionToCrmClient auctionToCrmClient;
	@Resource
	private ProductDao productDao;
	
	
	public List<Customer> findAll() {
		return customerDao.findAll();
	}
	
	public List<Customer> findMonthlyAll() {
		return customerDao.findMonthlyAll();
	}

	public Customer findOne(Long id) {
		return customerDao.findOne(id);
	}
	
	public Customer findCustomerByCdnr(String cdnr){
		return customerDao.findCustomerByCdnr(cdnr);
	}
	
	public Customer findCustomerByCustomerOIDOrMemberCode(String customerOID, String sMemberCode){
		return customerDao.findCustomerByCustomerOIDOrMemberCode(customerOID, sMemberCode);
	}

	@Transactional(readOnly = false)
	public void save(Customer customer) {
		Contact  contact=null;
		if(customer.getId()!=null&&customer.getContact()!=null){
			contact=customerDao.findOne(customer.getId()).getContact();
			
		}else{
			contact=new Contact();
		}
		contact.setAddress(customer.getAddress());
		contact.setEmail(customer.getEmail());
		contact.setLinkman(customer.getName());
		contact.setMobile(customer.getMobile());
		contact.setZip(customer.getZip());
		if(customer.getId()==null){
			Set<Contact>  contacts=new HashSet<Contact>();
			contacts.add(contact);
			customer.setContacts(contacts);
		}else{
			Customer  customerDB=customerDao.findOne(customer.getId());
			customer.setContacts(customerDB.getContacts());
			customer.setProducts(customerDB.getProducts());
			customer.setCarriers(customerDB.getCarriers());
		}
		contactDao.save(contact);
		customer.setContact(contact);
		if(customer.getCustomerGradeNew() != null){
			if(customer.getCustomerGrade().getId() != customer.getCustomerGradeNew().getId()){
				customer.setCustomerGradeStatus(CustomerGradeStatus.待审核);
			}
		}
		
		customerDao.save(customer);
	}
	
	public Customer findByCustomerIdAndPassword(String customerId,String password) {
		return customerDao.findByCustemerIdAndPassword(customerId,password);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		Customer	customer=customerDao.findOne(id);
		if(customer.getContacts()!=null&&!customer.getContacts().isEmpty()){
			Set<Contact> contacts=customer.getContacts();
			for (Contact contact : contacts) {
				contactDao.delete(contact.getId());
			}
		}
		customerDao.delete(id);
	}

	public Page<Customer> findCustomersByNameLike(int page, int rows, String name) {
		return customerDao.findByNameLike("%" + name + "%", RepoUtils.buildPageRequest(page, rows));
	}

	public Page<Customer> findCustomersByFirstNameLike(int page, int rows, final String name) {
		Specification<Customer> specification = new Specification<Customer>() {
			@Override
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("name").as(String.class), "%" + name + "%");
			}
		};
		return customerDao.findAll(specification, RepoUtils.buildPageRequest(page, rows));
	}

	public List<Customer> findCustomersByFilter(List<PropertyFilter> filters) {
		Specification<Customer> spec = DynamicSpecifications.buildSpecifitions(Customer.class, filters);
		return customerDao.findAll(spec);
	}
	
	public Page<Customer> findCustomersByFilter(List<PropertyFilter> filters, Pageable page) {
		Specification<Customer> spec = DynamicSpecifications.buildSpecifitions(Customer.class, filters);
		return customerDao.findAll(spec, page);
	}
	
	public Customer findByCustomerId(String custemerId) {
		return customerDao.findByCustemerId(custemerId);
	}
	
	public boolean exists(String custemerId) {
		Customer customer=customerDao.findByCustemerId(custemerId);
		boolean result=true;
		if(customer==null){
			result=false;
		}
		return result;
	}

	@Transactional(readOnly = false)
	public Contact createContact(Contact contact,Customer customer) {
		contactDao.save(contact);
		Set<Contact>  contactSet=customer.getContacts();
		contactSet.add(contact);
		customer.setContacts(contactSet);
		customerDao.save(customer);
		return contact;
	}
	@Transactional(readOnly = false)
	public Contact updateContact(Contact contact) {
		return contactDao.save(contact);

	}

	public Contact findOneContact(Long contact) {
		return contactDao.findOne(contact);
	}
	@Transactional(readOnly = false)
	public void delContact(Long contactId,Customer customer) {
		
		Set<Contact>  contactSet=customer.getContacts();
		contactSet.remove(contactDao.findOne(contactId));
		customer.setContacts(contactSet);
		contactDao.delete(contactId);
		customerDao.save(customer);
	}
	
	/**
     * 添加客户商品
     * @param product
     * @param customerId
     * @return
     */
	@Transactional(readOnly = false)
	public void  createcustomerPro(Product product,Long customerId) {
		Customer  customer=customerDao.findOne(customerId);
		String productCode=customer.getProductCode();
		product.setCode(productCode);
		productDao.save(product);
		customer.getProducts().add(product);
		customerDao.save(customer);
	}
	@Transactional(readOnly = false)
	public void updateCustomerPro(Product product) {
		productDao.save(product);
		 
	}
	@Transactional(readOnly = false)
	public void delcustomerPro(Long productId,Long customerId) {
		Customer  customer=customerDao.findOne(customerId);
		customer.getProducts().remove(productDao.findOne(productId));
		productDao.delete(productId);
		customerDao.save(customer);
	}
	/**iMemberID卡号
	 * trace 磁道号
	 * customer 客户，如果没有传null
	 * 根据磁道号或卡号    和密码向客户一体化平台取信息
	 * 旧一体化平台的数据（已不适用）
	public Customer getCustomerInfo(String iMemberID, String trace, String password, Customer customer) {
		CustomerInfo customerInfo = null;
		TraceInfo traceInfo = null;
		if (StringUtils.isNotBlank(iMemberID)) {
			traceInfo = auctionToCrmClient.getCDNR(iMemberID);
			customerInfo = auctionToCrmClient.getCustomer(traceInfo.getCdnr(), password);
		} else {
			customerInfo = auctionToCrmClient.getCustomer(trace, password);
			traceInfo = auctionToCrmClient.getCDNR(customerInfo.getsMemberCode());
		}
		
		if (customer != null) {
			Customer memberCustomer=customerDao.findByCustemerId(customerInfo.getiMemberID());
			if(memberCustomer==null||!memberCustomer.getId().equals(customer.getId())){
				throw new RuntimeException("不是可支付客户");
			}
		} else {
			customer = new Customer();
		}
		customer.setCdnr(traceInfo.getCdnr());
		customer.setCustemerId(customerInfo.getiMemberID());
		customer.setGsmc(traceInfo.getGsmc());
		customer.setiChildID(customerInfo.getiChildID());
		customer.setName(traceInfo.getKhmc());
		String desPass = "";
		try {
			desPass = Des.encrypt(password, Des.KEY);
		} catch (Exception e) {

			e.printStackTrace();
		}
		customer.setPassword(desPass);
		customer.setsAccount_NO(customerInfo.getsAccount_NO());
		customer.setsKHDM(customerInfo.getsKHDM());
		customer.setsMemberCode(customerInfo.getsMemberCode());
		customer.setZkmc(traceInfo.getZkmc());
		return customer;
	}
	*/
	
	/**iMemberID卡号
	 * cardMac 物理卡号
	 * password 密码
	 * customer 客户，如果没有传null
	 * 根据磁道号或卡号    和密码向客户一体化平台取信息（新一体化平台数据接口）
	 */
	public Customer getCustomerInfo(String cardMac, String sMemberCode, String passWord, Customer customer) {
		CustomerInfo customerInfo = null;
			customerInfo = auctionToCrmClient.getCustomer(cardMac, sMemberCode);
		
		if (customer != null) {
			Customer memberCustomer=customerDao.findCustomerByCustomerOIDOrMemberCode(customerInfo.getCustomerOID(),sMemberCode);
			if(memberCustomer==null||!memberCustomer.getId().equals(customer.getId())){
				throw new RuntimeException("不是可支付客户");
			}
		} else {
			customer = new Customer();
		}
		if((passWord != null) && (!passWord.isEmpty())){
			String desPassword = MD5Encryption.encode(passWord);
			customer.setPassword(desPassword);
		}
		
	    //新一体化平台引入（大白菜+）
		customer.setsKHBH(customerInfo.getsKHBH());
	    customer.setCustomerOID(customerInfo.getCustomerOID());
	    customer.setAccountOID(customerInfo.getAccountOID());
	    customer.setaCardMac(cardMac);
	    if(sMemberCode.length() == 10){
	    	customer.setsMemberCode(sMemberCode);
	    }
	    
	    //旧接口（大白菜+数据引入）
		customer.setName(customerInfo.getsKHMC());
		customer.setsKHDM(customerInfo.getsKHBH());
		customer.setCustemerId(customerInfo.getsKHBH());
		customer.setShortName(customerInfo.getsKHMC());
		customer.setZkmc(customerInfo.getsKHMC());
		
		//customer.setPassword(desPass);
		return customer;
	}
	public Customer getCustomerMessage(String cardMac, String sMemberCode){
		CustomerInfo customerInfo = null;
		customerInfo = auctionToCrmClient.getCustomer(cardMac, sMemberCode);
		return customerDao.findCustomerByCustomerOIDOrMemberCode(customerInfo.getCustomerOID(), sMemberCode);
	}
	
	//客户等级审批
	@Transactional(readOnly = false)
	public void  gradeApprove(Long id,CustomerGradeStatus customerGradeStatus){
		Customer customer=customerDao.findOne(id);
		//设置审批或未审批
		if (customerGradeStatus.equals(CustomerGradeStatus.已审核)){
			//审批后的规则覆盖当前客户规则
			customer.setCustomerGrade(customer.getCustomerGradeNew());
			customer.setCustomerGradeStatus(CustomerGradeStatus.已审核);
			customer.setApprover(ThreadLocalHolder.getCurrentOperator());
			customer.setApproveTime(new Date());
			//在审批的同时，需要计算该客户从审批启用开始的一段时间内的所有费用；
		}else
		{
			customer.setCustomerGradeStatus(CustomerGradeStatus.待审核);
		}
		save(customer);
	}
}
