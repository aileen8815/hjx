package com.wwyl.controller.report;

 
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.wwyl.Enums.SystemConfigKey;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.ProductService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.SystemConfigService;

/**库存汇总，库存详细报表
 * @author jianl
 */

@Controller
@RequestMapping("/report")
public class ReportBookInventoryController extends BaseReportController {
 
	@Resource
	private StoreAreaService storeAreaService;
	@Resource
	private CustomerService customerService;
	@Resource
	SystemConfigService systemConfigService;

 
	@RequestMapping(value="/report-bookinventory-index",method = RequestMethod.GET)
    public String reportBookinventory(ModelMap  model,Long type) {
		model.addAttribute("customerList", customerService.findAll());
		model.addAttribute("storeAreaList", storeAreaService.findAll());
		Map<Object, String>   reportVal=systemConfigService.getReportVal();
		model.addAttribute("reportUrl", reportVal.get(SystemConfigKey.报表服务器URL));
		model.addAttribute("user", reportVal.get(SystemConfigKey.报表服务器用户名));
		model.addAttribute("password", reportVal.get(SystemConfigKey.报表服务器密码));
		model.addAttribute("type", type);
		String prptName=type==1?"sumbookinventory.prpt":"bookinventory.prpt";
		model.addAttribute("prptName",prptName);
        return "/report/report_bookinventory";
    }

	private String reportBookinventory(ModelMap model, String prptName) {
		model.addAttribute("storeAreaList", storeAreaService.findAll());
		model.addAttribute("prptName",prptName);

		Map<Object, String>   reportVal=systemConfigService.getReportVal();
		model.addAttribute("reportUrl", reportVal.get(SystemConfigKey.报表服务器URL));
		model.addAttribute("user", reportVal.get(SystemConfigKey.报表服务器用户名));
		model.addAttribute("password", reportVal.get(SystemConfigKey.报表服务器密码));
		return "/report/book_inventory_form";
	}

	@RequestMapping(value="/book-inventory-detail",method = RequestMethod.GET)
	public String reportStockDetail(ModelMap model) {
		return reportBookinventory(model, "ReportStockDetail");
	}

	@RequestMapping(value="/book-inventory-summary",method = RequestMethod.GET)
	public String reportStockSummary(ModelMap model) {
		return reportBookinventory(model, "ReportStockSummary");
	}

	@RequestMapping(value="/book-inventory-daily",method = RequestMethod.GET)
	public String reportStockDaily(ModelMap model) {
		return reportBookinventory(model, "ReportStockDaily");
	}

}
