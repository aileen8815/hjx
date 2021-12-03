package com.wwyl.controller.customer;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Constants;
import com.wwyl.Enums;
import com.wwyl.Enums.BookingStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.BookingMethod;
import com.wwyl.entity.settings.Contact;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.store.InboundBooking;
import com.wwyl.entity.store.InboundBookingItem;
import com.wwyl.service.settings.BookingMethodService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.PackingService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.VehicleTypeService;
import com.wwyl.service.store.InboundBookingService;

/**客户入库预约
 * @author jianl
 */
@Controller
@RequestMapping("/customer/customer-inbound-booking")
public class CustomerInboundBookingController extends BaseController {
	@Resource
	InboundBookingService bookingService;
	@Resource
	VehicleTypeService vehicleTypeService;
	@Resource
	BookingMethodService bookingMethodService;
	@Resource
	StoreAreaService storeAreaService;
	@Resource
	CustomerService customerService;
	@Resource
	SerialNumberService serialNumberService;
	@Resource
	private ProductService		 	 productService;
	@Resource	
	private MeasureUnitService	 	 measureUnitService;
	@Resource
	private PackingService		     packingService;
	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpServletRequest request,HttpSession session,ModelMap model) {
		model.addAttribute("bookingStatus", BookingStatus.values());
		return "/customer/inbound_booking";
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, BookingStatus  bookingStatus,String vehicleNumbers, HttpServletRequest request, HttpSession session) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
		 
		if(customer==null){
			return null;
		}
		Page<InboundBooking> inboundBookings = bookingService
				.findInboundBookingByConditions(page, rows, serialNo, bookingStatus,customer.getId(),vehicleNumbers);
		return 	toEasyUiDatagridResult(inboundBookings);
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(HttpServletRequest request, HttpSession session,ModelMap model) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
		//客户预约
		 
		if(customer!=null){
			InboundBooking inboundBooking=new InboundBooking();
			BookingMethod bookingMethod=new BookingMethod();
			bookingMethod.setId(2L);
			inboundBooking.setCustomer(customer);
			inboundBooking.setBookingMethod(bookingMethod);
			inboundBooking.setBookingStatus(BookingStatus.未审核);
			model.addAttribute("inboundBooking",inboundBooking);
			Set<Contact> contactSet = customerService.findOne(Long.valueOf(customer.getId())).getContacts();
			model.put("contactSet", contactSet);
			setModelMap(model);
		}
	
		return "/customer/inbound_booking_form";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(
			@ModelAttribute("inboundBooking") @Valid InboundBooking inboundBooking,
			BindingResult result, ModelMap model) {
 
		inboundBooking.setSerialNo(serialNumberService.getSerialNumber(TransactionType.InboundBooking));
		inboundBooking.setBookTime(new Date());
		Long bookingId = bookingService.save(inboundBooking,null,null);
		 
		return "redirect:/customer/customer-inbound-booking/"+bookingId;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		bookingService.delete(id);
		return indexRedirect;
	}
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id, ModelMap model) {
		InboundBooking inboundBooking = bookingService.findOne(id);
		model.addAttribute("inboundBooking", inboundBooking);
		return "/customer/inbound_booking_view";
	}
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, HttpServletRequest request, HttpSession session,ModelMap model) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
 
		 
		model.addAttribute("_method", "put");
		setModelMap(model);
		Set<Contact> contactSet = customerService.findOne(Long.valueOf(customer.getId())).getContacts();
		model.put("contactSet", contactSet);
		model.addAttribute("inboundBooking", bookingService.findOne(id));
		return "/customer/inbound_booking_form";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(
			@ModelAttribute("inboundBooking") @Valid InboundBooking inboundBooking,
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			List<ObjectError> errros = result.getAllErrors();
			for (ObjectError err : errros) {
				System.out.println(err.getDefaultMessage());
			}
			return "/customer/inbound_booking_form";
		}
	 
		bookingService.update(inboundBooking,null);
		return "redirect:/customer/customer-inbound-booking/"+inboundBooking.getId();
	}

	 
	@RequestMapping(value = "/{id}/delete-item", method = RequestMethod.GET)
	public String deleteItem(@PathVariable Long id,Long inboundBookingId, ModelMap model) {
		bookingService.deleteItem(id);
		 
		return "redirect:/customer/customer-inbound-booking/"+inboundBookingId;
	}
	@RequestMapping(value = "/{id}/save-item", method = RequestMethod.POST)
	public String updateItem(
			@ModelAttribute("inboundBookingItem") @Valid InboundBookingItem inboundBookingItem,
			BindingResult result, ModelMap model) {
			
			bookingService.saveItem(inboundBookingItem);
			 
			return "redirect:/customer/customer-inbound-booking/"+inboundBookingItem.getInboundBooking().getId();
	}
	

	@RequestMapping(value = "/{id}/edit-item", method = RequestMethod.GET)
	public String editItem(@PathVariable Long id,Long inboundBookingId, ModelMap model) {
		
		InboundBookingItem  inboundBookingItem= bookingService.findOneItem(id);
		setItemModelMap(model,inboundBookingItem.getInboundBooking().getCustomer().getProducts());
		model.addAttribute("inboundBookingItem",inboundBookingItem);
		model.put("inboundBookingId", inboundBookingId);
		return "/customer/inbound_booking_item_form";
	}
	
	@RequestMapping(value = "/save-item", method = RequestMethod.POST)
	public String createItem(
			@ModelAttribute("inboundBookingItem") @Valid InboundBookingItem inboundBookingItem,
			BindingResult result,Long inboundBookingId, ModelMap model) {
			 	
			 
			bookingService.saveItem(inboundBookingItem);
			 
			return "redirect:/customer/customer-inbound-booking/"+inboundBookingItem.getInboundBooking().getId();
	}
	@RequestMapping(value = "/new-item", method = RequestMethod.GET)
	public String newItem(
			@ModelAttribute("inboundBookingItem") @Valid InboundBookingItem inboundBookingItem,
			BindingResult result,Long inboundBookingId, ModelMap model) {
			InboundBooking inboundBooking= bookingService.findOne(inboundBookingId);
			setItemModelMap(model,inboundBooking.getCustomer().getProducts());
			model.put("inboundBookingId", inboundBookingId);
			return "/customer/inbound_booking_item_form";
	}

	private void setItemModelMap(ModelMap model,Set<Product> products){
		model.addAttribute("products",products);
		model.addAttribute("products",productService.findAll());
		model.addAttribute("weightMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
		model.addAttribute("amountMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.数量));
		model.addAttribute("packings", packingService.findAll());
	}
	private void setModelMap(ModelMap model){
		model.addAttribute("vehicleTypes", vehicleTypeService.findAll());
		model.addAttribute("storeAreas", storeAreaService.findRootStoreAreas());
 
	}
	@RequestMapping(value = "getproduct", method = RequestMethod.GET)
	@ResponseBody
	public Product getProduct(@RequestParam(value = "id", defaultValue = "0") Long id) {
		Product product= productService.findOne(id);
		return product;
	}
}
