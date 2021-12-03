package com.wwyl.controller.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.ThreadLocalHolder;
import com.wwyl.Enums.StockInStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.entity.store.StockIn;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.store.StockInService;

/**
 * @author fyunli
 */
@Controller
@RequestMapping("/api")
public class StockInApiController extends BaseController {

	@Resource
	private StockInService stockInService;
	@Resource
	private StoreLocationService storeLocationService;

	@RequestMapping(value = "/stock-in/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(Long storeAreaID) {
		if(storeAreaID == null)
		{
			return this.printMessage(1, "无效的参数");
		}
		List<StockIn> stockIns = stockInService.findUnclaimStockIn(storeAreaID);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("result", stockIns);
		return result;
	}

	@RequestMapping(value = "/stock-in", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> get(String containerCode, Long storeAreaID) {
		StockIn target = null;
		if(containerCode == null || storeAreaID== null)
		{
			return this.printMessage(1, "无效的参数");
		}
		if (StringUtils.isNotBlank(containerCode)) {
			List<StockIn> stockIns = stockInService.findUnclaimStockIn(storeAreaID);
			for (StockIn stockIn : stockIns) {
				if (StringUtils.equals(containerCode, stockIn.getInboundReceiptItem().getStoreContainer().getLabel())) {
					target = stockIn;
					break;
				}
			}
		}
		if (target == null) {
			return this.printMessage(1, "找不到对应的上架单");
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("stockIn", target);
        //result.put("preStorelocations", target.getPreStoreLocations());
		return result;
	}

	@RequestMapping(value = "/stock-in/pick", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> pick(Long id) {
		if (id == null) {
			return this.printMessage(1, "无效的参数");
		}

		StockIn stockIn = stockInService.findOne(id);
		if (stockIn == null) {
			return this.printMessage(1, "无效的参数");
		}

		if (StockInStatus.已上架.compareTo(stockIn.getStockInStatus()) <= 0) {
			return this.printMessage(1, "上架单已上架完成，不需重复操作");
		}

		stockIn.setStockInStatus(StockInStatus.上架中);
		stockIn.setStockInOperator(ThreadLocalHolder.getCurrentOperator());
		//由于需要修改计算仓储费开始时间应为手持机清点完成后，故入库开始时间不是当前时间，改为清点时间。
		stockIn.setStockInStartTime(stockIn.getInboundReceiptItem().getReceiptTime());
		stockInService.save(stockIn);
		return this.printMessage(0, stockIn.getStockInStatus().toString());
	}

	@RequestMapping(value = "/stock-in/finish", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> finish(Long id, String locationCode) {
		if (id == null || StringUtils.isBlank(locationCode)) {
			return this.printMessage(1, "无效的参数");
		}

		StoreLocation storeLocation = storeLocationService.findStoreLocationByLabel(locationCode);
		if (storeLocation == null) {
			return this.printMessage(1, "无效的储位");
		}

		try {
			StockIn stockIn = stockInService.completePickup(id, storeLocation.getId());
			return this.printMessage(0, stockIn.getStockInStatus().toString());
		} catch (Exception e) {
			return this.printMessage(e);
		}
	}

	@RequestMapping(value = "/stock-in/locate", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> locate(String locationCode) {
		StoreLocation storeLocation = storeLocationService.findStoreLocationByLabel(locationCode);
		// if (storeLocation == null) {
		// return this.printMessage(1, "无效的储位");
		// }

		// TODO 记录上架单路线

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("storeLocation", storeLocation);
		return result;
	}

}
