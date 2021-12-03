package com.wwyl.controller.report;
 
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.wwyl.Enums.SystemConfigKey;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.SystemConfigService;

/**仓库报表
 * @author jianl
 */

@Controller
@RequestMapping("/report")
public class ReportWarehouseDailyController extends BaseReportController {
 
	@Resource
	private StoreAreaService storeAreaService;
	@Resource
	SystemConfigService  systemConfigService;
	
	@RequestMapping(value="/report-warehouse",method = RequestMethod.GET)
    public String reportWarehouse(ModelMap  model) {
		prepareInput(model);
        return "/report/report_warehouse";
    }
	
	@RequestMapping(value="/report-warehouse-daily",method = RequestMethod.GET)
    public String reportBookinventory(ModelMap  model) {
		prepareInput(model);
        return "/report/report_warehouse_daily";
    }

 

	public void  prepareInput(ModelMap model){
		List<StoreArea>	storeAreaList=storeAreaService.findAll();
		model.addAttribute("storeAreaList", storeAreaList);
		Map<Object, String>   reportVal=systemConfigService.getReportVal();
		model.addAttribute("reportUrl", reportVal.get(SystemConfigKey.报表服务器URL));
		model.addAttribute("user", reportVal.get(SystemConfigKey.报表服务器用户名));
		model.addAttribute("password", reportVal.get(SystemConfigKey.报表服务器密码));
	}
}
