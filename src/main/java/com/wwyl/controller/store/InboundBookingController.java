package com.wwyl.controller.store;

import java.util.ArrayList;
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
 
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Enums;
import com.wwyl.Enums.BookingStatus;
import com.wwyl.Enums.CommentType;
 
import com.wwyl.ThreadLocalHolder;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.Comment;
import com.wwyl.entity.settings.Contact;
import com.wwyl.entity.settings.Customer;
 
import com.wwyl.entity.store.InboundBooking;
import com.wwyl.entity.store.InboundBookingItem;
import com.wwyl.entity.store.InboundRegister;
import com.wwyl.entity.store.InboundRegisterItem;
import com.wwyl.service.settings.BookingMethodService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.MeasureUnitService;
import com.wwyl.service.settings.PackingService;
import com.wwyl.service.settings.ProductCategoryService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.VehicleTypeService;
import com.wwyl.service.store.InboundBookingService;
import com.wwyl.service.store.InboundRegisterService;

/**
 * 入库预约
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/store/inbound-booking")
public class InboundBookingController extends BaseController {
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
	private ProductService productService;
	@Resource
	private MeasureUnitService measureUnitService;
	@Resource
	private PackingService packingService;
	@Resource
	private InboundRegisterService inboundRegisterService;
	@Resource
	private ProductCategoryService productCategoryService;
	

	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpSession session, ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("bookingStatus", BookingStatus.values());
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo, BookingStatus bookingStatus, Long customerId, String vehicleNumbers) {
		Page<InboundBooking> inboundBookings = bookingService.findInboundBookingByConditions(page, rows, serialNo, bookingStatus, customerId, vehicleNumbers);
		return toEasyUiDatagridResult(inboundBookings);
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		setModelMap(model);
		
		return formPage;
	}
	@RequestMapping(value = "/new-difference", method = RequestMethod.GET)
	public String newdifference(Long inboundRegisterId,ModelMap model) {
		InboundBooking inboundBooking=inboundRegisterService.findOne(inboundRegisterId).getInboundBooking();
		InboundBooking inboundBookingNew =new InboundBooking();
		inboundBookingNew.setApplyInboundTime(inboundBooking.getApplyInboundTime());
		inboundBookingNew.setBookingMethod(inboundBooking.getBookingMethod());
		inboundBookingNew.setContact(inboundBooking.getContact());
		inboundBookingNew.setCustomer(inboundBooking.getCustomer());
		inboundBookingNew.setNote(inboundBooking.getNote());
		inboundBookingNew.setVehicleAmount(inboundBooking.getVehicleAmount());
		inboundBookingNew.setVehicleNumbers(inboundBooking.getVehicleNumbers());
		inboundBookingNew.setVehicleType(inboundBooking.getVehicleType());
		inboundBookingNew.setStoreArea(inboundBooking.getStoreArea());
		//inboundBookingNew.setStoreContainerCount(inboundBooking.getStoreContainerCount());
 
		setModelMap(model);
		model.addAttribute("inboundBooking", inboundBookingNew);
		Set<Contact> contactSet = customerService.findOne(Long.valueOf(inboundBooking.getCustomer().getId())).getContacts();
		model.put("contactSet", contactSet);
		model.put("inboundRegisterId", inboundRegisterId);
		return formPage;
	}
	@RequestMapping(value = "/list-difference", method = RequestMethod.GET)
	@ResponseBody
	public List<InboundBookingItem> fromRegisterDifference(Long inboundRegisterId) {
		if(inboundRegisterId==null){
			return null;
		}
		InboundRegister inboundRegister=inboundRegisterService.findOne(inboundRegisterId);
		Set<InboundRegisterItem> inboundRegisterItems=inboundRegister.getInboundRegisterItems();
		Set<InboundBookingItem>  inboundBookingItems=inboundRegister.getInboundBooking().getInboundBookingItems();
		List<InboundBookingItem>   mergeInboundBookingItems=bookingService.mergeInboundBookingItem(inboundBookingItems);
		List<InboundBookingItem>  differenceInboundBookingItems=new ArrayList<InboundBookingItem>();
		for (InboundBookingItem inboundBookingItem : mergeInboundBookingItems) {
			boolean exist=false;
			for (InboundRegisterItem inboundRegisterItem : inboundRegisterItems) {
				if(inboundBookingItem.getProduct().getId().equals(inboundRegisterItem.getProduct().getId())){
					if(inboundBookingItem.getAmount()-inboundRegisterItem.getAmount()>0){
						//有未登记的商品
						inboundBookingItem.setAmount(inboundBookingItem.getAmount()-inboundRegisterItem.getAmount());
						//重新计算预计托盘数
						inboundBookingItem.setStoreContainerCount((int) (inboundBookingItem.getAmount()/inboundBookingItem.getProduct().getBearingCapacity()+(inboundBookingItem.getAmount()%inboundBookingItem.getProduct().getBearingCapacity()==0?0:1)));
						differenceInboundBookingItems.add(inboundBookingItem);
					}
					exist=true;
				}
			}
			if(!exist){
				differenceInboundBookingItems.add(inboundBookingItem);
			}
		}
		 
		return differenceInboundBookingItems;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("inboundBooking") @Valid InboundBooking inboundBooking, BindingResult result, String[] productCheck,Long inboundRegisterId,ModelMap model) {
		bookingService.save(inboundBooking,productCheck,inboundRegisterId);
		return "redirect:/store/inbound-booking/" + inboundBooking.getId();
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		bookingService.delete(id);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
		model.addAttribute("_method", "put");
		setModelMap(model);
		InboundBooking inboundBooking = bookingService.findOne(id);
		model.addAttribute("inboundBooking", inboundBooking);
		Set<Contact> contactSet = customerService.findOne(Long.valueOf(inboundBooking.getCustomer().getId())).getContacts();
		model.put("contactSet", contactSet);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("inboundBooking") @Valid InboundBooking inboundBooking, BindingResult result, ModelMap model) {
	/*	if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			List<ObjectError> errros = result.getAllErrors();
			for (ObjectError err : errros) {
				System.out.println(err.getDefaultMessage());
			}
			return formPage;
		}*/
		bookingService.update(inboundBooking,null);
		return "redirect:/store/inbound-booking/" + inboundBooking.getId();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id, ModelMap model) {
		InboundBooking inboundBooking = bookingService.findOne(id);
		model.addAttribute("inboundBooking", inboundBooking);
		return "/store/inbound_booking_view";
	}

	@RequestMapping(value = "/items", method = RequestMethod.GET)
	@ResponseBody
	public List<InboundBookingItem> items(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows, @RequestParam(value = "serialNo", defaultValue = "") String serialNo) {
		List<InboundBookingItem> inboundBookingItemList = bookingService.findInboundBookingItems(serialNo);
		return inboundBookingItemList;
	}

	@RequestMapping(value = "/index-item", method = RequestMethod.GET)
	public String indexItems(@RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		InboundBooking inboundBookings = bookingService.findBySerialNo(serialNo);
		model.put("inboundBooking", inboundBookings);
		model.put("serialNo", serialNo);
		return "/store/inbound_booking_item";
	}

	@RequestMapping(value = "/{id}/delete-item", method = RequestMethod.GET)
	public String deleteItem(@PathVariable Long id, Long  inboundBookingId, ModelMap model) {
		bookingService.deleteItem(id);
		return "redirect:/store/inbound-booking/" + inboundBookingId;
	}

	@RequestMapping(value = "/{id}/save-item", method = RequestMethod.POST)
	public String updateItem(@ModelAttribute("inboundBookingItem") @Valid InboundBookingItem inboundBookingItem, BindingResult result, ModelMap model) {
		bookingService.saveItem(inboundBookingItem);
		return "redirect:/store/inbound-booking/"+inboundBookingItem.getInboundBooking().getId();
	}

	@RequestMapping(value = "/{id}/edit-item", method = RequestMethod.GET)
	public String editItem(@PathVariable Long id,Long  inboundBookingId, ModelMap model) {
		InboundBooking inboundBooking =bookingService.findOne(inboundBookingId);
	 
		setItemModelMap(model,inboundBooking.getCustomer());
		model.addAttribute("inboundBookingItem", bookingService.findOneItem(id));
		model.put("inboundBookingId", inboundBookingId);
		return "/store/inbound_booking_item_form";
	}

	@RequestMapping(value = "/save-item", method = RequestMethod.POST)
	public String addItem(@ModelAttribute("inboundBookingItem") @Valid InboundBookingItem inboundBookingItem, BindingResult result, ModelMap model) {
		 
	 
		bookingService.saveItem(inboundBookingItem);
 
		return "redirect:/store/inbound-booking/" + inboundBookingItem.getInboundBooking().getId();
	}

	@RequestMapping(value = "/new-item", method = RequestMethod.GET)
	public String newItem(@ModelAttribute("inboundBookingItem") @Valid InboundBookingItem inboundBookingItem, BindingResult result,
			Long inboundBookingId, ModelMap model) {
		InboundBooking inboundBooking =bookingService.findOne(inboundBookingId);
		setItemModelMap(model,inboundBooking.getCustomer());
		model.put("inboundBookingId", inboundBookingId);
		return "/store/inbound_booking_item_form";
	}

	// 取消
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public String cancel(Long id, String remark) {
		InboundBooking inboundBooking = bookingService.findOne(id);
		Comment comment = new Comment();
		comment.setCommentor(ThreadLocalHolder.getCurrentOperator());
		comment.setCommentTime(new Date());
		comment.setContent(remark);
		comment.setCommentType(CommentType.取消预约);
		inboundBooking.getComments().add(comment);
		inboundBooking.setBookingStatus(BookingStatus.已取消);
		bookingService.update(inboundBooking, comment);

		return "redirect:/store/inbound-booking/" + id;
	}

	// 延迟
	@RequestMapping(value = "/retard", method = RequestMethod.POST)
	public String retard(Long id, String remark, Date applyInboundTime) {
		InboundBooking inboundBooking = bookingService.findOne(id);
		Comment comment = new Comment();
		comment.setCommentor(ThreadLocalHolder.getCurrentOperator());
		comment.setCommentTime(new Date());
		comment.setContent(remark);
		comment.setCommentType(CommentType.延迟预约);
		inboundBooking.setApplyInboundTime(applyInboundTime);
		inboundBooking.setBookingStatus(BookingStatus.已受理);
		inboundBooking.getComments().add(comment);
		bookingService.update(inboundBooking, comment);
		return "redirect:/store/inbound-booking/" + id;
	}

	// 审核
	@RequestMapping(value = "/check-index", method = RequestMethod.GET)
	public String indexCheck(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		return "/store/inbound_booking_check";
	}

	@RequestMapping(value = "/{id}/save-check", method = RequestMethod.POST)
	public String saveCheck(@PathVariable Long id, String remark, Date applyInboundTime,boolean confirm) {
		InboundBooking	inboundBooking= bookingService.findOne(id);
		inboundBooking.setApplyInboundTime(applyInboundTime);
		inboundBooking.setConfirmOperator(ThreadLocalHolder.getCurrentOperator());
		inboundBooking.setConfirmRemark(remark);
		inboundBooking.setConfirmTime(new Date());
		inboundBooking.setBookingStatus(confirm?BookingStatus.已受理:BookingStatus.不受理);
		bookingService.update(inboundBooking,null);
		return "redirect:/store/inbound-booking/check-index";
	}

	private void setItemModelMap(ModelMap model,Customer customer) {
		//添加商品
	    model.addAttribute("productCategorys", productCategoryService.findAll());
	    model.addAttribute("customer",customer);
	    model.addAttribute("products",customer.getProducts());
		model.addAttribute("weightMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.重量));
		model.addAttribute("amountMeasureUnits", measureUnitService.findByMeasureUnitType(Enums.MeasureUnitType.数量));
		model.addAttribute("packings", packingService.findAll());
	}

	private void setModelMap(ModelMap model) {
		model.addAttribute("bookingStatus", BookingStatus.values());
		model.addAttribute("vehicleTypes", vehicleTypeService.findAll());
		model.addAttribute("bookingMethods", bookingMethodService.findAll());
		model.addAttribute("storeAreas", storeAreaService.findValidStoreArea());
		model.addAttribute("customers", customerService.findAll());
	}

}
