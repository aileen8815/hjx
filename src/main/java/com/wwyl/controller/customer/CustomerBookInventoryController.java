package com.wwyl.controller.customer;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Constants;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.store.BookInventoryService;

/**客户库存查询
 * @author jianl
 */
@Controller
@RequestMapping("/customer/customer-book-inventory")
public class CustomerBookInventoryController extends BaseController {

	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private ProductService productService;
	

	@Resource
	CustomerService customerService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpServletRequest request,HttpSession session,ModelMap model) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
		//model.addAttribute("products", productService.findAll());
		model.addAttribute("products", customerService.findOne(customer.getId()).getProducts());
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public List<BookInventory> list(HttpServletRequest request,HttpSession session,Date startTime, Date endTime,String productCategory) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
	
		List<BookInventory> bookInventoryinfo = bookInventoryService.findBookInventoryInfo(customer.getId(), startTime, endTime,productCategory,null,null);
		return  bookInventoryinfo;
	}

}
