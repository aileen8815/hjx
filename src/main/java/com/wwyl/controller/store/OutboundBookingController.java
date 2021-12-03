package com.wwyl.controller.store;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
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

import com.wwyl.Enums.BookingStatus;
import com.wwyl.Enums.CommentType;
import com.wwyl.Enums.TransactionType;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.Comment;
import com.wwyl.entity.settings.Contact;
import com.wwyl.entity.settings.Product;
import com.wwyl.entity.store.BookInventory;
import com.wwyl.entity.store.OutboundBooking;
import com.wwyl.entity.store.OutboundBookingItem;
import com.wwyl.entity.store.OutboundRegister;
import com.wwyl.entity.store.OutboundRegisterItem;
import com.wwyl.service.settings.BookingMethodService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.VehicleTypeService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.OutboundBookingService;
import com.wwyl.service.store.OutboundRegisterService;

/**
 * 出库预约
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/store/outbound-booking")
public class OutboundBookingController extends BaseController {
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
	private OutboundRegisterService outboundRegisterService;
	@Resource
	ProductService 	productService;
	@RequestMapping(method = RequestMethod.GET)
	public String index( ModelMap model) {
		model.addAttribute("bookingStatus", BookingStatus.values());
		model.addAttribute("customers", customerService.findAll());
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,String serialNo,BookingStatus bookingStatus,Long customerId,String vehicleNumbers) {
		Page<OutboundBooking> outboundBookings = outboundbookingService.findOutboundBookingByConditions(page, rows, serialNo,  bookingStatus, customerId, vehicleNumbers);
		return toEasyUiDatagridResult(outboundBookings);
	}
	@RequestMapping(value = "/bookinventory-list", method = RequestMethod.GET)
	@ResponseBody
	public List<BookInventory> bookInventoryList(Long customerId,Long selectCustomerId,Long outboundbookingId,Long outboundRegisterId ,String productId,ModelMap model) {
		customerId=selectCustomerId!=null?selectCustomerId:customerId;
		if(customerId==null){
			customerId=-1L;
		}
		List<BookInventory> bookInventoryList = bookInventoryService
				.findBookInventoryInfo(customerId, null, null, productId,null,null);
		 
		if(outboundRegisterId==null){
		
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
		} else {
			OutboundRegister outboundRegister = outboundRegisterService
					.findOne(outboundRegisterId);
			Set<OutboundRegisterItem> outboundRegisterItems = outboundRegister
					.getOutboundRegisterItems();
			Set<OutboundBookingItem> outboundBookingItems = outboundRegister
					.getOutboundBooking().getOutboundBookingItems();
			List<OutboundBookingItem> diffrence = new ArrayList<OutboundBookingItem>();
			for (OutboundBookingItem outboundBookingItem : outboundBookingItems) {
				boolean exist=false;
				for (OutboundRegisterItem outboundRegisterItem : outboundRegisterItems) {
					if (outboundBookingItem.getBatchProduct().equals(outboundRegisterItem.getBatchProduct())) {
						if (outboundBookingItem.getAmount()
								- outboundRegisterItem.getAmount() > 0) {
							// 计算未登记的商品数量
							OutboundBookingItem outboundBookingItemNew = outboundBookingItem
									.cloneOutboundBookingItem();
							outboundBookingItemNew
									.setAmount(outboundBookingItem.getAmount()
											- outboundRegisterItem.getAmount());
							Product product=outboundBookingItemNew.getProduct();
							outboundBookingItemNew.setWeight(outboundBookingItemNew.getAmount()*product.getWeight());
							outboundBookingItemNew.setStoreContainerCount((int) (outboundBookingItemNew.getAmount()/product.getBearingCapacity()+(outboundBookingItemNew.getAmount()%product.getBearingCapacity()==0?0:1)));
							diffrence.add(outboundBookingItemNew);
						}
						exist=true;
					}
				}
				//如果预约单存在，登记单未登记的情况
				if(!exist){
					diffrence.add(outboundBookingItem);
				}
			}

			
			for (BookInventory bookInventory : bookInventoryList) {
				for (OutboundBookingItem outboundBookingItem : diffrence) {
					if (bookInventory.getBatchProduct()
							.equals(outboundBookingItem.getBatchProduct())) {
						bookInventory.setOutboundAmount(outboundBookingItem
								.getAmount());
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
		return viewPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add( Long outboundRegisterId,ModelMap model) {
		setModelMap(model);
		model.put("products",productService.findAll());
		if(outboundRegisterId!=null){
			OutboundBooking outboundBooking=outboundRegisterService.findOne(outboundRegisterId).getOutboundBooking();
			OutboundBooking outboundBookingNew=new OutboundBooking();
			outboundBookingNew.setApplyOutboundTime(outboundBooking.getApplyOutboundTime());
			outboundBookingNew.setBookingMethod(outboundBooking.getBookingMethod());
			outboundBookingNew.setContact(outboundBooking.getContact());
			outboundBookingNew.setCustomer(outboundBooking.getCustomer());
			outboundBookingNew.setNote(outboundBooking.getNote());
			outboundBookingNew.setStoreArea(outboundBooking.getStoreArea());
			outboundBookingNew.setStoreContainerCount(outboundBooking.getStoreContainerCount());
			outboundBookingNew.setVehicleType(outboundBooking.getVehicleType());
			outboundBookingNew.setVehicleNumbers(outboundBooking.getVehicleNumbers());
			outboundBookingNew.setVehicleAmount(outboundBooking.getVehicleAmount());
			Set<Contact> contactSet=customerService.findOne(Long.valueOf(outboundBooking.getCustomer().getId())).getContacts();
			model.put("contactSet", contactSet);
			model.addAttribute("outboundBooking",outboundBookingNew );
			model.addAttribute("outboundRegisterId", outboundRegisterId);
		
			OutboundRegister outboundRegister = outboundRegisterService
					.findOne(outboundRegisterId);
			Set<OutboundRegisterItem> outboundRegisterItems = outboundRegister
					.getOutboundRegisterItems();
			Set<OutboundBookingItem> outboundBookingItems = outboundRegister
					.getOutboundBooking().getOutboundBookingItems();
			List<OutboundBookingItem> diffrence = new ArrayList<OutboundBookingItem>();
			for (OutboundBookingItem outboundBookingItem : outboundBookingItems) {
				boolean exist=false;//该批次的商品是否存在登记单里。
				for (OutboundRegisterItem outboundRegisterItem : outboundRegisterItems) {
					if (outboundBookingItem.getBatchProduct().equals(outboundRegisterItem.getBatchProduct())) {
						if (outboundBookingItem.getAmount()
								- outboundRegisterItem.getAmount() > 0) {
							// 计算未登记的商品数量
							OutboundBookingItem outboundBookingItemNew = outboundBookingItem
									.cloneOutboundBookingItem();
							outboundBookingItemNew
									.setAmount(outboundBookingItem.getAmount()
											- outboundRegisterItem.getAmount());
							Product product=outboundBookingItemNew.getProduct();
							outboundBookingItemNew.setWeight(outboundBookingItemNew.getAmount()*product.getWeight());
							outboundBookingItemNew.setStoreContainerCount((int) (outboundBookingItemNew.getAmount()/product.getBearingCapacity()+(outboundBookingItemNew.getAmount()%product.getBearingCapacity()==0?0:1)));
							diffrence.add(outboundBookingItemNew);
						}
						exist=true;
					}
				}
				//如果预约单存在该批次商品，在登记时未登记
				if(!exist){
					diffrence.add(outboundBookingItem);
				}
			}
			
			model.addAttribute("outboundBookingItems",diffrence );
		
		}
		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("outboundBooking") @Valid OutboundBooking outboundBooking, BindingResult result,String[] productCheck, ModelMap model) {

		outboundBooking.setSerialNo(serialNumberService.getSerialNumber(TransactionType.OutboundBooking));
		outboundBooking.setBookTime(new Date());
		outboundBooking.setBookingStatus(BookingStatus.已受理);
		Long outboundbookingId=	outboundbookingService.save(outboundBooking,productCheck);
		
		return "redirect:/store/outbound-booking/"+outboundbookingId;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		outboundbookingService.delete(id);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		model.addAttribute("_method", "put");
		setModelMap(model);
		OutboundBooking outboundBooking=outboundbookingService.findOne(id);
		Set<Contact> contactSet=customerService.findOne(Long.valueOf(outboundBooking.getCustomer().getId())).getContacts();
		model.put("contactSet", contactSet);
		model.addAttribute("outboundBooking",outboundBooking );
		model.addAttribute("outboundBookingItems",outboundBooking.getOutboundBookingItems());
		model.put("products",productService.findAll());
		
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("outboundBooking") @Valid OutboundBooking outboundBooking, BindingResult result,String[] productCheck ,ModelMap model) {
	/*	if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			List<ObjectError> errros = result.getAllErrors();
			for (ObjectError err : errros) {
				System.out.println(err.getDefaultMessage());
			}
			return formPage;
		}*/
		 
		outboundbookingService.save(outboundBooking,productCheck);
		return indexRedirect;
	}

	/*@RequestMapping(value = "/items", method = RequestMethod.GET)
	@ResponseBody
	public List<OutboundBookingItem> items(String serialNo) {
		List<OutboundBookingItem> outboundBookingItemList = outboundbookingService.findOutItemsByserialNo(serialNo);
		return outboundBookingItemList;
	}

	@RequestMapping(value = "/index-item", method = RequestMethod.GET)
	public String indexItems(@RequestParam(value = "serialNo", defaultValue = "") String serialNo, ModelMap model) {
		List<OutboundBooking> outboundBookings = outboundbookingService.findOutBySerialNo(serialNo);
		model.put("outboundBooking", outboundBookings.get(0));
		model.put("serialNo", serialNo);
		return "/store/outbound_booking_item";
	}

	@RequestMapping(value = "/{id}/delete-item", method = RequestMethod.GET)
	public String deleteItem(@PathVariable Long id, String serialNo, ModelMap model) {

		outboundbookingService.deleteItem(id);
		model.put("serialNo", serialNo);
		return "redirect:/store/outbound-booking/index-item?serialNo=" + serialNo;
	}

	@RequestMapping(value = "/save-item", method = RequestMethod.GET)
	public String saveItem(String ids, String serialNo, ModelMap model) {
		List<OutboundBooking> outboundBookings = outboundbookingService.findOutBySerialNo(serialNo);
		outboundbookingService.saveItem(ids, outboundBookings.get(0));
		model.put("serialNo", serialNo);
		return "redirect:/store/outbound-booking/index-item?serialNo=" + serialNo;
	}

	@RequestMapping(value = "/index-new-item", method = RequestMethod.GET)
	public String indexNewItem(String serialNo, String id, ModelMap model) {
		model.put("serialNo", serialNo);
		return "/store/outbound_booking_item_add";
	}

	@RequestMapping(value = "/item-list", method = RequestMethod.GET)
	@ResponseBody
	public List<BookInventory> itemList(String serialNo, String id, ModelMap model) {

		List<OutboundBooking> outboundBookings = outboundbookingService.findOutBySerialNo(serialNo);
		Set<OutboundBookingItem> OutboundBookingItems = outboundBookings.get(0).getOutboundBookingItems();
		List<Long> storeContainerids = new ArrayList<Long>();
		List<BookInventory> bookInventoryList = null;
		if (!OutboundBookingItems.isEmpty()) {
			for (OutboundBookingItem outboundBookingItem : OutboundBookingItems) {
				storeContainerids.add(outboundBookingItem.getStoreContainer().getId());
			}
			//bookInventoryList = bookInventoryService.findBookInventoryByCustomerId(outboundBookings.get(0).getCustomer().getId(), storeContainerids.toArray());
		} else {
			bookInventoryList = bookInventoryService.findBookInventoryByCustomerId(outboundBookings.get(0).getCustomer().getId());
		}
		return bookInventoryList;
	}*/
	//取消
	@RequestMapping(value="/cancel" ,method = RequestMethod.POST)
	public String cancel(Long id,String remark) {
		OutboundBooking outboundBooking=outboundbookingService.findOne(id);
		Comment conmment=new Comment();
		conmment.setCommentor(ThreadLocalHolder.getCurrentOperator());
		conmment.setCommentTime(new Date());
		conmment.setContent(remark);
		conmment.setCommentType(CommentType.取消预约);
		outboundBooking.getConmments().add(conmment);
		outboundBooking.setBookingStatus(BookingStatus.已取消);
		outboundbookingService.update(outboundBooking,conmment);
		 
		return "redirect:/store/outbound-booking/"+id;
	}
	//延迟
	@RequestMapping(value="/retard" ,method = RequestMethod.POST)
	public String retard(Long id,String remark,Date applyOutboundTime) {
		OutboundBooking outboundBooking=outboundbookingService.findOne(id);
		Comment conmment=new Comment();
		conmment.setCommentor(ThreadLocalHolder.getCurrentOperator());
		conmment.setCommentTime(new Date());
		conmment.setContent(remark);
		conmment.setCommentType(CommentType.延迟预约);
		outboundBooking.setApplyOutboundTime(applyOutboundTime);
		outboundBooking.setBookingStatus(BookingStatus.已受理);
		outboundBooking.getConmments().add(conmment);
		outboundbookingService.update(outboundBooking,conmment);
		return "redirect:/store/outbound-booking/"+id;
	}
	// 审核
	@RequestMapping(value = "/check-index", method = RequestMethod.GET)
	public String indexCheck(ModelMap model) {
		model.addAttribute("customers", customerService.findAll());
		return "/store/outbound_booking_check";
	}

	@RequestMapping(value = "/{id}/save-check", method = RequestMethod.POST)
	public String saveCheck(@PathVariable Long id, String remark, Date applyOutboundTime,boolean confirm) {
		OutboundBooking outboundBooking=outboundbookingService.findOne(id);
		outboundBooking.setConfirmTime(new Date());
		outboundBooking.setConfirmOperator(ThreadLocalHolder.getCurrentOperator());
		outboundBooking.setApplyOutboundTime(applyOutboundTime);
		outboundBooking.setConfirmRemark(remark);
		outboundBooking.setBookingStatus(confirm?BookingStatus.已受理:BookingStatus.不受理);
		outboundbookingService.update(outboundBooking,null);
		return "redirect:/store/outbound-booking/check-index";
	}

	private void setModelMap(ModelMap model) {
		model.addAttribute("bookingStatus", BookingStatus.values());
		model.addAttribute("vehicleTypes", vehicleTypeService.findAll());
		model.addAttribute("bookingMethods", bookingMethodService.findAll());
		model.addAttribute("storeAreas", storeAreaService.findValidStoreArea());
		model.addAttribute("customers", customerService.findAll());
	}

}
