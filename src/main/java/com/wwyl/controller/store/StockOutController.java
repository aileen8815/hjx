package com.wwyl.controller.store;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
 
import com.wwyl.Enums.StockOutStatus;
import com.wwyl.ThreadLocalHolder;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.store.StockOut;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.OutboundRegisterService;
import com.wwyl.service.store.StockOutService;

/**
 * 出库拣货
 * 
 * @author jianl
 */
@Controller
@RequestMapping("/store/stock-out")
public class StockOutController extends BaseController {
	@Resource
	private StockOutService stockOutService;

	@Resource
	OutboundRegisterService outboundRegisterService;
	@Resource
	StoreLocationService storeLocationService;
	@Resource
	BookInventoryService bookInventoryService;
	@Resource
	CustomerService customerService;
	@Resource
	StoreAreaService storeAreaService;
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		Object[]  stockOutStatus=new Object[]{StockOutStatus.待拣货,StockOutStatus.拣货中,StockOutStatus.已拣货,StockOutStatus.已作废};
		model.addAttribute("stockOutStatus", stockOutStatus);
		model.addAttribute("customers", customerService.findAll());
		model.addAttribute("storeAreas", storeAreaService.findAll());
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,String serialNo,
			StockOutStatus stockOutStatus,Long customerId, Long storeArea, Long product) {
		Page<StockOut> stockOuts = stockOutService.findStockOutItems( page, rows, serialNo,
				stockOutStatus, customerId, storeArea, product);
		return toEasyUiDatagridResult(stockOuts);
	}

	@RequestMapping(value = "/{id}/add-picking", method = RequestMethod.GET)
	public String addPicking( @PathVariable Long id, ModelMap model) {
		StockOut stockOut = stockOutService.findOne(id);
		model.addAttribute("stockOut", stockOut);
		model.put("storeLocation", stockOut.getStoreLocation());
		model.put("stockOutId", stockOut.getId());
		return "/store/stock_out_picking";
	}

	@RequestMapping(value = "/picking", method = RequestMethod.POST)
	public String Picking( Long id, ModelMap model) {
		StockOut stockOut = stockOutService.findOne(id);
		stockOut.setStockOutStatus(StockOutStatus.拣货中);
		stockOut.setStockOutStartTime(new Date());
		stockOut.setStockOutOperator(ThreadLocalHolder.getCurrentOperator());
		stockOutService.save(stockOut);
		model.addAttribute("stockOut", stockOut);
		model.put("stockOutId", stockOut.getId());
		model.put("storeLocation", stockOut.getStoreLocation());
		return "/store/stock_out_complete";
	}

	@RequestMapping(value = "/complete-picking", method = RequestMethod.POST)
	public String completePicking(Long id, ModelMap model) {
		// 更新状态和库存
		stockOutService.completePicking(id);
		return indexRedirect;
	}

}
