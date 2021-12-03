package com.wwyl.controller.settings;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

 
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.BookingMethod;
import com.wwyl.service.settings.BookingMethodService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("/settings/booking-method")
public class BookingMethodController extends BaseController {

	@Resource
	BookingMethodService bookingMethodService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("bookingMethod") @Valid BookingMethod bookingMethod, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		bookingMethodService.save(bookingMethod);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<BookingMethod> list() {
		return bookingMethodService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public BookingMethod getBookingMethod(@PathVariable Long id) {
		BookingMethod bookingMethod = bookingMethodService.findOne(id);
		return bookingMethod;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		BookingMethod bookingMethod = bookingMethodService.findOne(id);

		model.addAttribute("bookingMethod", bookingMethod);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("bookingMethod") @Valid BookingMethod bookingMethod, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {

			return formPage;
		}
		bookingMethodService.save(bookingMethod);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		bookingMethodService.delete(id);
		return indexRedirect;
	}

}
