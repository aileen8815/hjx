package com.wwyl.controller.customer;
 
import java.util.Date;
import java.util.HashMap;
 
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Constants;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.finance.Payment;
 
import com.wwyl.entity.settings.Customer;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.finance.PaymentService;

/**
 * 客户费用查询
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/customer/payment-query")
public class PaymentQueryController extends BaseController {

	@Resource
	private PaymentService paymentService;

	@Resource
	CustomerService customerService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(String paymentStatus, ModelMap model) {
		model.put("paymentStatus", paymentStatus);
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(
			HttpServletRequest request,HttpSession session,
			Date startTime,
			Date endTime,
			Long  paymentStatus,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows) {
		Customer customer = (Customer) session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
		Map<String,Object>  result=new HashMap<String, Object>(); 
		if(customer!=null){
			Page<Payment> pamentpage = paymentService
					.findPaymentSpecification(customer.getId(), startTime,
							endTime, paymentStatus, page, rows);
			result= toEasyUiDatagridResult(pamentpage);
		}
		return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id,
			ModelMap model) {
 
		Payment payment = paymentService.findOne(id);
		//BigDecimal amount = paymentService.paymentAmount(paymentItems);
		model.put("payment", payment);
		//model.put("amount", amount);
		return "customer/payment_view";
	}

}
