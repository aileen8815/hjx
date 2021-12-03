package com.wwyl.controller.report;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wwyl.Enums.SystemConfigKey;
import com.wwyl.entity.ce.CEFeeItem;
import com.wwyl.entity.settings.Customer;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.SystemConfigService;
import com.wwyl.service.ce.CEFeeItemService;

@Controller
@RequestMapping("/report")
public class ReportPaymentController extends BaseReportController  {
	
	@Resource
	private CustomerService customerService;
	@Resource
	private SystemConfigService systemConfigService;
	@Resource
	private CEFeeItemService cefeeItemService;
	
	@RequestMapping("/report-payment")
    public String paymentReport(ModelMap  model) {	
		prepareInput(model);
		List<Customer> customers = customerService.findAll();
		model.addAttribute("customerlist", customers);
		List<CEFeeItem> cefeeitems = cefeeItemService.findAll();
		model.addAttribute("cefeeitemlist", cefeeitems);
        return "/report/report-payment";
    }
	
	@RequestMapping("/report-arrearagefee")
    public String arrearagefeeReport(ModelMap model) {
		prepareInput(model);
		List<Customer> customers = customerService.findAll();
		model.addAttribute("customerlist", customers);
        return "/report/report-arrearagefee";
    }
	
	
	@RequestMapping("/report-charge-collect")
    public String chargecollectReport(ModelMap model) {
		prepareInput(model);
		List<Customer> customers = customerService.findAll();
		model.addAttribute("customerlist", customers);
        return "/report/report-charge-collect";
    }
	public void  prepareInput(ModelMap model){
		Map<Object, String>   reportVal=systemConfigService.getReportVal();
		model.addAttribute("reportUrl", reportVal.get(SystemConfigKey.报表服务器URL));
		model.addAttribute("user", reportVal.get(SystemConfigKey.报表服务器用户名));
		model.addAttribute("password", reportVal.get(SystemConfigKey.报表服务器密码));
		
	}
}
