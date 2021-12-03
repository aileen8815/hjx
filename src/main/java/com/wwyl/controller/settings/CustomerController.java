package com.wwyl.controller.settings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Constants;
import com.wwyl.Enums;
import com.wwyl.Enums.CustomerGradeStatus;
import com.wwyl.Enums.InboundType;
import com.wwyl.Enums.PaymentType;
import com.wwyl.Enums.StockInStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.PropertyFilter;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.Contact;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.CustomerGrade;
import com.wwyl.entity.settings.Product;
import com.wwyl.service.settings.ChargeTypeService;
import com.wwyl.service.settings.CustomerGradeService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.PackingService;
import com.wwyl.service.settings.ProductCategoryService;
import com.wwyl.service.settings.ProductService;

import cn.myemsp.security.Des;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/customer")
public class CustomerController  extends BaseController{

	@Resource
	CustomerService customerService;
	@Resource
	ProductService productService;
	@Resource
	CustomerGradeService customerGradeService;
	@Resource
	private ProductCategoryService productCategoryService;
	@Resource
	private PackingService packingService;
	@Resource
	private MeasureUnitService measureUnitService;
	@Resource
	private ChargeTypeService chargeTypeService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add( ModelMap model) {
		Customer customer = new Customer();
		model.addAttribute("customer",customer);
		model.addAttribute("customerGradeDefaults", customerGradeService.findHighLowDefaultGrade());
		model.addAttribute("customerGradeNew", customerGradeService.findAll());
		model.addAttribute("chargeTypes", chargeTypeService.findAll());
		model.addAttribute("customerGradeStatuss", CustomerGradeStatus.待审核);
		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("customer") @Valid Customer customer, BindingResult result,ModelMap model) {
		if (customerService.findCustomerByCustomerOIDOrMemberCode(customer.getCustomerOID(), customer.getsMemberCode()) != null)
		{
			throw new RuntimeException("此卡已注册会员信息，不能重复注册；" + " 已存在客户ID："+customer.getCustemerId().toString());
		}
		//加密密码
		String desPass ="";
		try {
			desPass = Des.encrypt(customer.getPassword(), Des.KEY);
		} catch (Exception e) {
			 
			e.printStackTrace();
		}
		customer.setPassword(desPass);
		customerService.save(customer);
		return  "redirect:/settings/customer/" + customer.getId();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id, ModelMap model) {
		Customer customer = customerService.findOne(id);
		model.addAttribute("customer", customer);

		model.addAttribute("customerGrades",customer.getCustomerGrade());
		return "/settings/customer_view";
	}

	@RequestMapping(value="/list" ,method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "name", defaultValue = "") String name, @RequestParam(value = "customerGradeStatus", defaultValue = "") CustomerGradeStatus customerGradeStatus) {
		List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
		if(StringUtils.isNotBlank(name)){
			propertyFilters.add(new PropertyFilter("name", name));
		}
		if(customerGradeStatus != null){
			propertyFilters.add(new PropertyFilter("customerGradeStatus", customerGradeStatus));
		}
		
		Page<Customer> customers = customerService.findCustomersByFilter(
				propertyFilters, RepoUtils.buildPageRequest(page, rows));
		return toEasyUiDatagridResult(customers);
	}
	
	@RequestMapping(value = "search", method = RequestMethod.GET)
	@ResponseBody
	public List<Customer> search(ServletRequest servletRequest) {
		List<PropertyFilter> filters = BaseController.parseSearchParams(servletRequest); 
	
		return customerService.findCustomersByFilter(filters);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Customer getcustomer(@PathVariable Long id) {
		Customer customer = customerService.findOne(id);
		return customer;
	}
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		Customer customer = customerService.findOne(id);
		model.addAttribute("customerGrades", customer.getCustomerGrade());
		model.addAttribute("customerGradeNew", customerGradeService.findAll());
		model.addAttribute("customer",customer);
		model.addAttribute("chargeTypes", chargeTypeService.findAll());
		model.addAttribute("customerGradeStatuss", customer.getCustomerGradeStatus());
		return formPage;
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("customer") @Valid Customer customer, BindingResult result, ModelMap model) {
		Customer customerTemp = customerService.findCustomerByCustomerOIDOrMemberCode(customer.getCustomerOID(), customer.getsMemberCode());
		if (customerTemp != null && !customerTemp.getId().equals(customer.getId()))
		{
			throw new RuntimeException("此卡已注册会员信息，不能重复注册；" + " 已存在客户ID："+customer.getCustemerId().toString());
		}
		if (result.hasErrors()) {
			
			return formPage;
		}
		customerService.save(customer);
		return  "redirect:/settings/customer/"+customer.getId();
	}
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		customerService.delete(id);
		return indexRedirect;
	}
	@RequestMapping(value = "/del-contact", method = RequestMethod.GET)
	public String delContact( Long contactId,Long customerId, ModelMap model) {
		customerService.delContact(contactId,customerService.findOne(customerId));
		return "redirect:/settings/customer/"+customerId;
	}
	
	@RequestMapping(value = "/new-contact", method = RequestMethod.GET)
	public String addContact(Long customerId, ModelMap model) {
	
		model.addAttribute("customer", customerService.findOne(customerId));
		return "/settings/contact_form";
	}
	
	@RequestMapping(value = "/edit-contact", method = RequestMethod.GET)
	public String editContact(Long contactId,Long customerId, ModelMap model) {
		Contact contact=customerService.findOneContact(contactId);
		model.addAttribute("contact", contact);
		model.addAttribute("customer", customerService.findOne(customerId));
		return "/settings/contact_form";
	}
	
	@RequestMapping(value = "/save-contact", method = RequestMethod.POST)
	public String saveContact(Contact contact,Long customerId, ModelMap model) {
		Customer customer = customerService.findOne(customerId);
		customerService.createContact(contact,customer);
		return "redirect:/settings/customer/"+customerId;
	}
	@RequestMapping(value = "{id}/save-contact", method = RequestMethod.POST)
	public String updateContact(@PathVariable Long id,Contact contact,Long customerId, ModelMap model) {
		
		customerService.updateContact(contact);
		return "redirect:/settings/customer/"+customerId;
	}
    //维护客户商品
	@RequestMapping(value = "/del-product", method = RequestMethod.GET)
	public String delProduct( Long productId,Long customerId, ModelMap model) {
		customerService.delcustomerPro(productId,customerId);
		return "redirect:/settings/customer/"+customerId;
	}
	
	@RequestMapping(value = "/new-product", method = RequestMethod.GET)
	public String addProduct(Long customerId, ModelMap model) {
		 
		model.addAttribute("customer", customerService.findOne(customerId));
		model.addAttribute("measureUnit", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
	    model.addAttribute("productCategorys", productCategoryService.findAll());
	    model.addAttribute("packings", packingService.findAll());
		return "/settings/product_form";
	}
	
	@RequestMapping(value = "/edit-product", method = RequestMethod.GET)
	public String editProduct(Long productId,Long customerId, ModelMap model) {
		Product product=productService.findOne(productId);
		model.addAttribute("product", product);
		model.addAttribute("customer", customerService.findOne(customerId));
		model.addAttribute("measureUnit", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
	    model.addAttribute("productCategorys", productCategoryService.findAll());
	    model.addAttribute("packings", packingService.findAll());
		return "/settings/product_form";
	}
	
	@RequestMapping(value = "/save-product", method = RequestMethod.POST)
	public String saveProduct(Product product,Long customerId, ModelMap model) {
		 
		customerService.createcustomerPro(product,customerId);
		return "redirect:/settings/customer/"+customerId;
	}
	@RequestMapping(value = "{id}/save-product", method = RequestMethod.POST)
	public String updateProduct(@PathVariable Long id,Product product,Long customerId, ModelMap model) {
		
		customerService.updateCustomerPro(product);
		return "redirect:/settings/customer/"+customerId;
	}
	
	@RequestMapping(value = "customer-products", method = RequestMethod.GET)
	@ResponseBody
	public Set<Product> getCustomerProduct(@RequestParam(value = "id", defaultValue = "") Long id) {
		Set<Product>  products=new HashSet<Product>();
		if(id!=null){
			products= customerService.findOne(id).getProducts();
		}
		return products;
	}
	@RequestMapping(value = "getproduct", method = RequestMethod.GET)
	@ResponseBody
	public Product getProduct(@RequestParam(value = "id", defaultValue = "") Long id) {
		Product product= productService.findOne(id);
		return product;
	}
	
	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity exists(String code) {
		if (customerService.exists(code)) {
			return new ResponseEntity(HttpStatus.OK);
		} else {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}	
	
	@RequestMapping(value = "/new-customercarrier", method = RequestMethod.GET)
	public String addCustomerCarrier(Long customerId, ModelMap model) {
		Customer customer=customerService.findOne(customerId);
		model.addAttribute("customer",customer);
		model.put("carriers",customer.getCarriers());
		return "/settings/customer_carrier_add";
	}
	
	@RequestMapping(value = "/create-customercarrier", method = RequestMethod.GET)
	public String CreateCustomerCarrier(Long customerid,Long[] carrierids, ModelMap model) {
		Customer customer = customerService.findOne(customerid);		
		customer.setCustomerCarrier(carrierids);
		customerService.save(customer);
		return "redirect:/settings/customer/"+customerid;
	}
	/*旧接口
	@RequestMapping(value = "/get-customer-cdnr", method = RequestMethod.GET)
	@ResponseBody
	public Customer getCustomerProduct(String  cdnr,String password) {
		try
		{
		    Customer customer =customerService.getCustomerInfo(null,cdnr,password,null);
		    return customer;
		}
		catch(Exception e)
		{
		    return null;
		}
		
	}
	*/
	
	/*
	 * 新接口（大白菜+）
	 */
	@RequestMapping(value = "/get-customer-cardMac", method = RequestMethod.GET)
	@ResponseBody
	public Customer getCustomerCardMac(String cardMac,String sMemberCode, String passWord) {
		try
		{
		    Customer customer =customerService.getCustomerInfo(cardMac,sMemberCode,passWord,null);
		    return customer;
		}
		catch(Exception e)
		{
		    return null;
		}
		
	}
	/*
	 * 新接口（大白菜+）
	 */
	@RequestMapping(value = "/get-customer-Message", method = RequestMethod.GET)
	@ResponseBody
	public Customer getCustomerMessage(String cardMac,String sMemberCode) {
		try
		{
		    return customerService.getCustomerMessage(cardMac,sMemberCode);
		}
		catch(Exception e)
		{
		    return null;
		}
		
	}
	
	@RequestMapping(value = "/grade-approve", method = RequestMethod.GET)
	public String getgradeApprove(ModelMap model) {
		model.addAttribute("customerGradeStatuss", CustomerGradeStatus.values());
		return "/settings/customer_grade_approve";
	}
	
	@RequestMapping(value = "/{id}/grade-approve", method = RequestMethod.GET)
	public String gradeApproveView(@PathVariable Long id, ModelMap model) {
		Customer customer = customerService.findOne(id);
		model.addAttribute("customer", customer);
		return "/settings/customer_grade_approve_view";
	}

	//客户等级审批
	@RequestMapping(value="{id}/save-grade-approve" ,method = RequestMethod.GET)
	public String gradeApprove(@PathVariable Long id,Long customerGradeStatus,ModelMap model) { 
		customerService.gradeApprove(id,customerGradeStatus==1?CustomerGradeStatus.已审核:CustomerGradeStatus.待审核);
		Customer customer = customerService.findOne(id);
		model.addAttribute("customer", customer);
		return "/settings/customer_grade_approve_view";
	}
}
