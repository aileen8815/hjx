package com.wwyl.controller.customer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
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
import com.wwyl.Enums.StockInStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.Customer;
import com.wwyl.entity.store.InboundRegister;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.store.InboundRegisterService;


/**
 * 客户入库信息查询
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/customer/inbound-receipt-query")
public class InboundReceiptQueryController extends BaseController {
	@Resource
	CustomerService customerService;
	@Resource
	InboundRegisterService inboundRegisterService;
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows, Date startTime, Date endTime) {
		Map<String,Object>  result=new HashMap<String, Object>();
		Customer customer = (Customer) session.getAttribute(Constants.SESSION_CUSTOMER_KEY);
		if(customer!=null){
			Page<InboundRegister> inboundRegisters = inboundRegisterService.findInboundRegisterSpecification(customer.getId(), startTime, endTime,new StockInStatus[]{StockInStatus.已上架,StockInStatus.已清点,StockInStatus.已完成}, page, rows);
			result= toEasyUiDatagridResult(inboundRegisters);
		}
			return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Long id, ModelMap model) {
		InboundRegister  inboundRegister = inboundRegisterService.findOne(id);
		model.put("inboundRegister", inboundRegister);
		return "customer/inbound_view";
	}

}
