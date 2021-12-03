package com.wwyl.controller.customer;



import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Constants;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.Contact;
import com.wwyl.entity.settings.Customer;
import com.wwyl.service.settings.ContactService;
import com.wwyl.service.settings.CustomerService;


/**
 * @author jianl
 */
@Controller
@RequestMapping("/customer/contact")
public class ContactController extends BaseController {

	@Resource
	ContactService contactService;
	@Resource
	CustomerService customerService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add() {

		 
		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("carrierType") @Valid Contact contact, BindingResult result,HttpServletRequest request,HttpSession session, ModelMap model) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
		customerService.createContact(contact, customerService.findOne(customer.getId()));
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Set<Contact> list(HttpServletRequest request,HttpSession session) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
		Set<Contact> contacts =customerService.findOne(customer.getId()).getContacts();
		return contacts;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Contact getContact(@PathVariable Long id) {
		Contact contact = contactService.findOne(id);
		return contact;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		Contact contact = contactService.findOne(id);
		model.addAttribute("contact", contact);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("carrier") @Valid Contact contact, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
 
			return formPage;
		}
		customerService.updateContact(contact);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id,HttpServletRequest request,HttpSession session, ModelMap model) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
		customerService.delContact(id,customerService.findOne(customer.getId()));
		return indexRedirect;
	}
	
	 
	/**
	 * 用于出入库预约维护联系人信息
	 * @param contact
	 * @param customerId
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "create-contact", method = RequestMethod.POST)
	@ResponseBody
	public Contact  createContact(Contact contact,String customerId) {
		 	if(customerId==null||"".equals(customerId.trim())){
		 		return null;
		 	}
			Customer customer = customerService.findOne(Long.valueOf(customerId));
			return customerService.createContact(contact,customer);
		 
		 
	}
	@RequestMapping(value = "selected-customer", method = RequestMethod.POST)
	@ResponseBody
	public Set<Contact> selectedCustomer(String  customerId) {
	 	if(customerId==null||"".equals(customerId.trim())){
	 		return null;
	 	}
		Set<Contact> contactSet=customerService.findOne(Long.valueOf(customerId)).getContacts();

		return contactSet;
	}
	@RequestMapping(value = "/booking-edit-contact", method = RequestMethod.POST)
	@ResponseBody
	public Contact  bookingContact(Long id) {
		Contact contact=customerService.findOneContact(id);
		return contact;
 
	}
	@RequestMapping(value = "{id}/create-contact", method = RequestMethod.POST)
	@ResponseBody
	public Set<Contact> updateBookingContact(Contact contact,Long customerId) {
		Set<Contact> contactSet=customerService.findOne(Long.valueOf(customerId)).getContacts();
		customerService.updateContact(contact);
		return contactSet;
		  
	}
	
	@RequestMapping(value = "booking-del-contact", method = RequestMethod.POST)
	@ResponseBody
	public Set<Contact> delContact(Long id,Long customerId) {
		Set<Contact> contactSet=customerService.findOne(Long.valueOf(customerId)).getContacts();
		customerService.delContact(id,customerService.findOne(customerId));
		return contactSet;
		  
	}

}
