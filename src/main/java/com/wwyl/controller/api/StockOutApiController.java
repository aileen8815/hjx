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
import com.wwyl.Enums.StockOutStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.store.StockOut;
import com.wwyl.service.store.StockOutService;

/**
 * @author fyunli
 */
@Controller
@RequestMapping("/api")
public class StockOutApiController extends BaseController {

	@Resource
	private StockOutService stockOutService;

	@RequestMapping(value = "/stock-out/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(Long storeAreaID) {
		if(storeAreaID == null)
		{
			return this.printMessage(1, "无效的参数");
		}
		List<StockOut> stockOuts = stockOutService.findUnclaimStockOut(storeAreaID);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("result", stockOuts);
		return result;
	}

	@RequestMapping(value = "/stock-out", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> get(String containerLabel, Long storeAreaID) {
		StockOut target = null;
		if(storeAreaID == null)
		{
			return this.printMessage(1, "无效的参数");
		}
		if (StringUtils.isNoneBlank(containerLabel)) {
			List<StockOut> stockOuts = stockOutService.findUnclaimStockOut(storeAreaID);
			for (StockOut stockOut : stockOuts) {
				if (StringUtils.equals(stockOut.getStoreContainer().getLabel(), containerLabel)) {
					target = stockOut;
				}
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("stockOut", target);
		return result;
	}

	@RequestMapping(value = "/stock-out/pick", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> pick(Long id) {
		if (id == null) {
			return this.printMessage(1, "无效的参数");
		}
		StockOut stockOut = stockOutService.findOne(id);
		if (stockOut == null) {
			return this.printMessage(1, "无效的参数");
		}

		if (StockOutStatus.已拣货.compareTo(stockOut.getStockOutStatus()) <= 0) {
			return this.printMessage(1, "拣货单已拣货完成，不需要重复操作");
		}

		// TODO 验证货主，验证库存情况是否一致

		stockOut.setStockOutOperator(ThreadLocalHolder.getCurrentOperator());
		stockOut.setStockOutStartTime(new Date());
		stockOut.setStockOutStatus(StockOutStatus.拣货中);
		stockOutService.save(stockOut);
		return this.printMessage(0, stockOut.getStockOutStatus().toString());
	}

	@RequestMapping(value = "/stock-out/finish", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> finish(Long id) {
		if (id == null) {
			return this.printMessage(1, "无效的参数");
		}
		try {
			StockOut stockOut = stockOutService.completePicking(id);
			return this.printMessage(0, stockOut.getStockOutStatus().toString());
		} catch (Exception e) {
			return this.printMessage(e);
		}
	}

}
