package com.wwyl.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Enums.StockOutStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.TallyArea;
import com.wwyl.entity.store.OutboundCheckItem;
import com.wwyl.entity.store.OutboundRegister;
import com.wwyl.entity.store.OutboundRegisterItem;
import com.wwyl.entity.store.OutboundTarry;
import com.wwyl.entity.store.StockOut;
import com.wwyl.service.settings.StoreContainerService;
import com.wwyl.service.settings.TallyAreaService;
import com.wwyl.service.settings.TaskService;
import com.wwyl.service.store.OutboundCheckService;
import com.wwyl.service.store.OutboundRegisterService;
import com.wwyl.service.store.OutboundTarryService;
import com.wwyl.service.store.StockOutService;

/**
 * @author fyunli
 */
@Controller
@RequestMapping("/api")
public class OutboundChcekApiController extends BaseController {

	@Resource
	private OutboundRegisterService outboundRegisterService;
	@Resource
	private OutboundCheckService outboundCheckService;
	@Resource
	private StoreContainerService storeContainerService;
	@Resource
	private StockOutService stockOutService;
	@Resource
	private TallyAreaService tallyAreaService;
	@Resource
	private TaskService taskService;
	@Resource
	private OutboundTarryService outboundTarryService;

	// 显示需要验货的出库登记单
	@RequestMapping(value = "/outbound-register/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listOutboundRegister(
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo,
			String mac) {
		String outboundSerialNo = "";
		if (serialNo.toUpperCase().indexOf("OUR") >= 0) {
			outboundSerialNo = serialNo;
		} else {
			outboundSerialNo = "OUR20" + serialNo;
		}
		List<OutboundTarry> outboundTarrys = outboundTarryService
				.findOutboundTarryByConditions(mac, new StockOutStatus[] {
						StockOutStatus.已派送, StockOutStatus.已拣货, StockOutStatus.拣货中 });
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		if (!outboundTarrys.isEmpty()) {
			List<Long> list = new ArrayList<Long>();
			for (OutboundTarry outboundTarry : outboundTarrys) {
				list.add(outboundTarry.getOutboundRegister().getId());
			}
			result.put("result", outboundRegisterService.findByIds(
					list.toArray(), outboundSerialNo));
		}
		return result;
	}

	@RequestMapping(value = "/outbound-register", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getOutboundRegister(String outboundTallyArea) {
		if (outboundTallyArea == null) {
			return this.printMessage(1, "无效的参数");
		}

		String serialNo = "";
		if (outboundTallyArea.toUpperCase().indexOf("OUR") >= 0) {
			serialNo = outboundTallyArea;
		} else {
			serialNo = "OUR20" + outboundTallyArea;
		}
		OutboundRegister register = outboundRegisterService
				.findBySerialNo(serialNo);
		if (register == null) {
			return this.printMessage(1, "无效的参数");
		}
		if (register.getStockOutStatus().equals(StockOutStatus.已清点)
				|| register.getStockOutStatus().equals(StockOutStatus.已完成)
				|| register.getStockOutStatus().equals(StockOutStatus.已作废)) {
			this.printMessage(1,
					"出库登记单状态已更改，当前状态: " + register.getStockOutStatus());
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("register", register);
		result.put("stockOuts", register.getStockOuts());
		Set<OutboundTarry> outboundTarrys = register.getOutboundTarrys();
		result.put("assignTarrys", outboundTarrys);
		for (OutboundTarry outboundTarry : outboundTarrys) {
			result.put("tallyarea", tallyAreaService.findOne(outboundTarry
					.getTallyArea().getId()));
		}
		return result;
	}

	@RequestMapping(value = "/stock-out-detail", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findStockOut(Long id) {
		StockOut stockOut = stockOutService.findOne(id);
		if (stockOut == null) {
			this.printMessage(1, "查不到收货数据");
		}

		OutboundRegister register = stockOut.getOutboundRegister();
		if (register.getStockOutStatus().equals(StockOutStatus.已清点)
				|| register.getStockOutStatus().equals(StockOutStatus.已完成)) {
			this.printMessage(1,
					"出库登记单状态已更改，当前状态: " + register.getStockOutStatus());
		}

		if (StockOutStatus.已拣货.compareTo(stockOut.getStockOutStatus()) > 0) {
			this.printMessage(1, "托盘尚未拣货出库");
		}

		// TODO 已验货检查

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("stockOut", stockOut);
		result.put("register", stockOut.getOutboundRegister());
		return result;
	}

	@RequestMapping(value = "/outbound-check-item/save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveCheckItem(Long stockOutId, int amount,
			double weight, String containerCode, Long tallyAreaid, String readCode) {
		try {
			outboundRegisterService.saveOutboundCheckItem(stockOutId, amount,
					weight, containerCode, tallyAreaid, readCode);
		} catch (Exception e) {
			return this.printMessage(1, e.getMessage());
		}

		OutboundRegister outboundRegister = (stockOutService
				.findOne(stockOutId)).getOutboundRegister();
		Set<StockOut> stockOuts = outboundRegister.getStockOuts();
		Set<OutboundCheckItem> outboundCheckItems = outboundRegister
				.getOutboundCheckItems();
		
		int sumAmount = 0;
		for (StockOut stockOutObj : stockOuts) {
			if (!stockOutObj.getStockOutStatus().equals(StockOutStatus.已作废)) {
				sumAmount += 1;
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.putAll(this.printMessage(0, "保存出库验货明细成功"));
		resultMap.put("checkeamount", outboundCheckItems.size());
		//拣货单数量
		resultMap.put("amount", sumAmount);
		return resultMap;
	}

	@RequestMapping(value = "/outbound-check-item/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findOutboundCheckItems(Long outboundRegisterId,
			Long tallyareaid) {
		OutboundRegister outboundRegister = outboundRegisterService
				.findOne(outboundRegisterId);
		List<OutboundCheckItem> receiptItems = outboundCheckService
				.findOutboundChecItemByOutboudregisterAndTallyArea(
						outboundRegisterId, tallyareaid);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("receiptItems", receiptItems);
		result.put("register", outboundRegister);
		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/outbound-register/checked", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> checked(Long outboundRegisterId,
			boolean ignoreAmountCheck, Long tallyareaid) {
		OutboundRegister outboundRegister = outboundRegisterService
				.findOne(outboundRegisterId);
		if (outboundRegister.getStockOutStatus().equals(StockOutStatus.已清点)
				|| outboundRegister.getStockOutStatus().equals(
						StockOutStatus.已完成)) {
			this.printMessage(1,
					"出库登记单状态已更改，当前状态: " + outboundRegister.getStockOutStatus());
		}

		List<OutboundCheckItem> receiptItems = outboundCheckService
				.findOutboundCheckItems(outboundRegisterId);
		if (CollectionUtils.isEmpty(receiptItems)) {
			return this.printMessage(1, "出库登记单尚未验货，不能结束验货操作");
		}

		if (!ignoreAmountCheck) {
			int registerAmount = 0;
			for (OutboundRegisterItem registerItem : outboundRegister
					.getOutboundRegisterItems()) {
				registerAmount += registerItem.getAmount();
			}

			int receiptAmount = 0;
			for (OutboundCheckItem receiptItem : receiptItems) {
				receiptAmount += receiptItem.getAmount();
			}

			if (registerAmount != receiptAmount) {
				return this.printMessage(10, "出库登记数量与出库验货数量不符");
			}
		}

		outboundRegisterService.updateOutboundTarrychecked(
				outboundRegister.getId(), tallyareaid, true);
		/*
		 * boolean ischecked = outboundRegisterService
		 * .tallyareaStatus(outboundRegister.getId());
		 */
		// if (ischecked) {
		outboundRegisterService.updateStatus(outboundRegister.getId(),
				StockOutStatus.已清点);
		taskService.assignOutboundCheckNotice(outboundRegister);
		// }
		return this.printMessage(0, "出库验货完成");
	}

	// 显示出库登记单明细项详情
	@RequestMapping(value = "/outbound-check-item", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getOutboundCheckItem(String containerCode) {
		OutboundCheckItem checkItem = outboundCheckService
				.getLastOutboundCheckItemByContainerCode(containerCode);
		if (checkItem == null) {
			return this.printMessage(1, "找不到出库验货记录");
		}
		if ((checkItem.getOutboundRegister().getStockOutStatus() == StockOutStatus.已清点)
		  || (checkItem.getOutboundRegister().getStockOutStatus() == StockOutStatus.已完成)){
			return this.printMessage(1, "出库单已完成，该托盘已出库完毕");
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("receiptItem", checkItem);
		result.put("register", checkItem.getOutboundRegister());
		return result;
	}

	// 删除出库登记单明细
	@RequestMapping(value = "/outbound-check-item/delete", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> deleteOutboundCheckItem(Long id) {
		// TODO 检测状态
		outboundCheckService.deleteItem(id);
		return this.printMessage(0, "入库收货检验项已删除");
	}

	@RequestMapping(value = "/stock-out-check", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> get(Long outboundRegisterId,
			String containerLabel) {
		StockOut target = null;
		OutboundRegister outboundRegister = null;
		if (outboundRegisterId != null
				&& StringUtils.isNoneBlank(containerLabel)) {
			outboundRegister = outboundRegisterService
					.findOne(outboundRegisterId);
			if (outboundRegister == null) {
				return this.printMessage(1, "查不到出库通知单");
			}
			//增加(!stockOut.getStockOutStatus().equals(StockOutStatus.已作废)) ，有可能存在作废了又新增同一托盘的情况。
			for (StockOut stockOut : outboundRegister.getStockOuts()) {
				if ((StringUtils.equals(stockOut.getStoreContainer().getLabel(),
						containerLabel)) && (!stockOut.getStockOutStatus().equals(StockOutStatus.已作废))) {
					target = stockOut;
				}
			}
		}
		if (target == null) {
			return this.printMessage(1, "查不到收货数据");
		}
		//手持机用到，返回值为null时，手持机报错；
		if (target.getReadCode() == null) {
			target.setReadCode("");
		}
		// TODO 已检测过
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("stockOut", target);
		result.put("register", target.getOutboundRegister());
		Set<StockOut> stockOuts = outboundRegister.getStockOuts();
		Set<OutboundCheckItem> outboundCheckItems = outboundRegister
				.getOutboundCheckItems();
		int sumAmount = 0;
		for (StockOut stockOutObj : stockOuts) {
			if (!stockOutObj.getStockOutStatus().equals(StockOutStatus.已作废)) {
				sumAmount += 1;
			}
		}
		result.put("checkeamount", outboundCheckItems.size());
		//拣货单数量
		result.put("amount", sumAmount);
		return result;
	}

}
