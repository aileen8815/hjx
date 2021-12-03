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
import com.wwyl.Enums.BookingStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.BookingMethod;
import com.wwyl.entity.settings.Contact;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.OutboundBooking;
import com.wwyl.entity.store.OutboundBookingItem;
import com.wwyl.service.settings.BookingMethodService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.VehicleTypeService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.OutboundBookingService;

/**
 * 出库预约
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/customer/customer-outbound-booking")
public class CustomerOutboundBookingController extends BaseController {
	@Resource
	OutboundBookingService outboundbookingService;
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
	private BookInventoryService bookInventoryService;
	@Resource
	private ProductService productService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index( ModelMap model) {
		model.addAttribute("bookingStatus", BookingStatus.values());
 
		return "/customer/outbound_booking";
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,String serialNo,BookingStatus bookingStatus,String vehicleNumbers, HttpServletRequest request, HttpSession session) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
		Page<OutboundBooking> outboundBookings = outboundbookingService.findOutboundBookingByConditions(page, rows, serialNo,  bookingStatus, customer.getId(), vehicleNumbers);
		return toEasyUiDatagridResult(outboundBookings);
	}
	@RequestMapping(value = "/bookinventory-list", method = RequestMethod.GET)
	@ResponseBody
	public List<BookInventory> bookInventoryList(HttpSession session,Long customerId,Long outboundbookingId, ModelMap model) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
		List<BookInventory> bookInventoryList = bookInventoryService
				.findBookInventoryInfo(customer.getId(), null, null, null,null,null);
		 	// 设置已预约的商品数量
			if (outboundbookingId != null) {
				Set<OutboundBookingItem> outboundBookingItemSet = outboundbookingService
						.findOne(outboundbookingId).getOutboundBookingItems();
				for (BookInventory bookInventory : bookInventoryList) {
					for (OutboundBookingItem outboundBookingItem : outboundBookingItemSet) {
						if (bookInventory.getBatchProduct().equals(outboundBookingItem.getBatchProduct())) {
							bookInventory.setOutboundAmount(outboundBookingItem.getAmount());
							bookInventory.setStoreContainerCount(outboundBookingItem.getStoreContainerCount());
							
						}
						 
					}
				}

			} 
			return bookInventoryList;
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id, ModelMap model) {
		model.addAttribute("outboundBooking", outboundbookingService.findOne(id));
		return "/customer/outbound_booking_view";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add( ModelMap model, HttpServletRequest request, HttpSession session) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
		setModelMap(model);
		OutboundBooking outboundBooking=new OutboundBooking();
		outboundBooking.setCustomer(customer);
		Set<Contact> contactSet=customerService.findOne(Long.valueOf(customer.getId())).getContacts();
		model.put("outboundBooking", outboundBooking);
		model.put("contactSet", contactSet);
		return "/customer/outbound_booking_form";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("outboundBooking") @Valid OutboundBooking outboundBooking, BindingResult result,String[] productCheck,  HttpServletRequest request, HttpSession session,ModelMap model) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
		outboundBooking.setSerialNo(serialNumberService.getSerialNumber(TransactionType.OutboundBooking));
		outboundBooking.setBookTime(new Date());
		outboundBooking.setBookingStatus(BookingStatus.未审核);
		outboundBooking.setCustomer(customer);
		BookingMethod bookingMethod=new BookingMethod();
		bookingMethod.setId(2L);
		outboundBooking.setBookingMethod(bookingMethod);
		Long outboundbookingId=	outboundbookingService.save(outboundBooking,productCheck);
		
		return "redirect:/customer/customer-outbound-booking/"+outboundbookingId;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		outboundbookingService.delete(id);
		return "/customer/outbound_booking";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		model.addAttribute("_method", "put");
		setModelMap(model);
		OutboundBooking outboundBooking=outboundbookingService.findOne(id);
		StringBuffer buffer=new StringBuffer();
		Set<OutboundBookingItem> outboundBookingItems=	outboundBooking.getOutboundBookingItems();
		int i=0;
		int size=outboundBookingItems.size();
		for (OutboundBookingItem outboundBookingItem : outboundBookingItems) {
			i++;
			buffer.append(outboundBookingItem.getProduct().getId()+":"+outboundBookingItem.getAmount());
			if(i!=size){
				buffer.append(",");
			}
		}
		Set<Contact> contactSet=customerService.findOne(Long.valueOf(outboundBooking.getCustomer().getId())).getContacts();
		model.put("outboundBooking", outboundBooking);
		model.put("contactSet", contactSet);
		model.addAttribute("productCheck",buffer.toString());
		model.addAttribute("outboundBooking",outboundBooking );
		return "/customer/outbound_booking_form";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("outboundBooking") @Valid OutboundBooking outboundBooking, BindingResult result,String[] productCheck ,ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			List<ObjectError> errros = result.getAllErrors();
			for (ObjectError err : errros) {
				System.out.println(err.getDefaultMessage());
			}
			return "/customer/inbound_booking_form";
		}
		outboundBooking.setBookTime(new Date());
		outboundbookingService.save(outboundBooking,productCheck);
		return "redirect:/customer/customer-outbound-booking/"+outboundBooking.getId();
	}
	@RequestMapping(value = "getproduct", method = RequestMethod.GET)
	@ResponseBody
	public Product getProduct(@RequestParam(value = "id", defaultValue = "0") Long id) {
		Product product= productService.findOne(id);
		return product;
	}
	private void setModelMap(ModelMap model) {
		model.addAttribute("bookingStatus", BookingStatus.values());
		model.addAttribute("vehicleTypes", vehicleTypeService.findAll());
		model.addAttribute("bookingMethods", bookingMethodService.findAll());
		model.addAttribute("storeAreas", storeAreaService.findValidStoreArea());
		model.addAttribute("customers", customerService.findAll());
	}

}
