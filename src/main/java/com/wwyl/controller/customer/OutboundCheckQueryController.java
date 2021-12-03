package com.wwyl.controller.customer;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Constants;
import com.wwyl.Enums.StockOutStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.store.OutboundRegister;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.store.OutboundRegisterService;


/**客户出库信息查询
 * @author jianl
 */
@Controller
@RequestMapping("/customer/outbound-check-query")
public class OutboundCheckQueryController extends BaseController {

	@Resource
	OutboundRegisterService outboundRegisterService;
 
	@Autowired
	CustomerService customerService;
 

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(HttpServletRequest request,HttpSession session, Date startTime,Date endTime, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows) {
		Customer customer = (Customer)session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
	 
		Page<OutboundRegister> outboundChecks = outboundRegisterService.findOutboundRegisterSpecification(customer.getId(), startTime, endTime,new StockOutStatus[]{StockOutStatus.已拣货,StockOutStatus.已清点,StockOutStatus.已完成},page,rows );
		return  toEasyUiDatagridResult(outboundChecks);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String  view(@PathVariable Long id,ModelMap model) {
		OutboundRegister outboundRegister=outboundRegisterService.findOne(id);
		model.put("outboundRegister", outboundRegister);
		return  "customer/outbound_view";
	}

}
