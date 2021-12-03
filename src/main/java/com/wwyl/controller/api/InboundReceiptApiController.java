package com.wwyl.controller.api;

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

import com.wwyl.Enums.StockInStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.store.InboundReceiptItem;
import com.wwyl.entity.store.InboundRegister;
import com.wwyl.entity.store.InboundRegisterItem;
import com.wwyl.entity.store.InboundTarry;
import com.wwyl.entity.store.StockIn;
import com.wwyl.service.settings.StoreContainerService;
import com.wwyl.service.settings.TallyAreaService;
import com.wwyl.service.settings.TaskService;
import com.wwyl.service.store.BookInventoryService;
import com.wwyl.service.store.InboundReceiptService;
import com.wwyl.service.store.InboundRegisterService;
import com.wwyl.service.store.InboundTarryService;

/**
 * @author fyunli
 */
@Controller
@RequestMapping("/api")
public class InboundReceiptApiController extends BaseController {

	@Resource
	private InboundRegisterService inboundRegisterService;
	@Resource
	private InboundReceiptService inboundReceiptService;
	@Resource
	private StoreContainerService storeContainerService;
	@Resource
	private TallyAreaService tallyAreaService;
	@Resource
	private TaskService taskService;
	@Resource
	private BookInventoryService bookInventoryService;
	@Resource
	private InboundTarryService inboundTarryService;

	// 列出需要验货的入库登记单
	@RequestMapping(value = "/inbound-register/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listInboundRegister(
			@RequestParam(value = "serialNo", defaultValue = "") String serialNo,
			String mac) {
		String inboundSerialNo = "";
		if (serialNo.toUpperCase().indexOf("INR") >= 0) {
			inboundSerialNo = serialNo;
		} else {
			inboundSerialNo = "INR20" + serialNo;
		}
		List<InboundRegister> inboundRegisters = inboundRegisterService
				.findInboundRegisterByMac(mac, inboundSerialNo);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		if (inboundRegisters != null)
			result.put("result", inboundRegisters);

		return result;
	}

	// 显示入库登记单详情
	@RequestMapping(value = "/inbound-register", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getInboundRegister(String inboundTally) {
		if (inboundTally == null) {
			return this.printMessage(1, "无效的参数");
		}
		String serialNo = "";
		if (inboundTally.toUpperCase().indexOf("INR") >= 0) {
			serialNo = inboundTally;
		} else {
			serialNo = "INR20" + inboundTally;
		}
		InboundRegister register = inboundRegisterService
				.findBySerialNo(serialNo);
		if (register == null) {
			return this.printMessage(1, "无效的参数");
		}

		if (!register.getStockInStatus().equals(StockInStatus.已派送)) {
			return this.printMessage(1,
					"入库登记单状态已更改，当前状态: " + register.getStockInStatus());
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("result", register);

		Set<InboundRegisterItem> inboundRegisterItems = inboundRegisterService
				.getInboundRegisterItems(register);

		result.put("items", inboundRegisterItems);
		Set<InboundTarry> inboundTarrys = register.getInboundTarrys();
		result.put("assignTarrys", inboundTarrys);
		for (InboundTarry inboundTarry : inboundTarrys) {
			result.put("tallyarea", tallyAreaService.findOne(inboundTarry
					.getTallyArea().getId()));
		}
		return result;
	}

	// 显示入库登记单明细项详情
	@RequestMapping(value = "/inbound-register-item", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getInboundRegisterItem(Long id) {
		if (id == null) {
			this.printMessage(1, "无效的参数");
		}

		InboundRegisterItem item = inboundRegisterService
				.getInboundRegisterItem(id);

		if (!item.getInboundRegister().getStockInStatus()
				.equals(StockInStatus.已派送)) {
			return this.printMessage(1, "入库登记单状态已更改，当前状态: "
					+ item.getInboundRegister().getStockInStatus());
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("registerItem", item);
		result.put("register", item.getInboundRegister());
		Set<InboundTarry> inboundTarrys = item.getInboundRegister()
				.getInboundTarrys();
		for (InboundTarry inboundTarry : inboundTarrys) {
			result.put("tallyarea", tallyAreaService.findOne(inboundTarry
					.getTallyArea().getId()));
		}

		return result;
	}

	// 显示入库登记单明细项详情
	@RequestMapping(value = "/inbound-receipt-item", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getInboundReceiptItem(String containerCode) {
		if (StringUtils.isBlank(containerCode)) {
			this.printMessage(1, "无效的参数");
		}

		InboundReceiptItem receiptItem = inboundReceiptService
				.getLastInboundReceiptItemByContainerCode(containerCode);
		if (receiptItem == null) {
			return this.printMessage(1, "找不到收货记录");
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("receiptItem", receiptItem);
		result.put("register", receiptItem.getInboundRegister());
		return result;
	}

	// 删除入库登记单明细项详情
	@RequestMapping(value = "/inbound-receipt-item/delete", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> deleteInboundReceiptItem(Long id) {
		InboundReceiptItem item = inboundReceiptService.findOneItem(id);

		if (!item.getInboundRegister().getStockInStatus()
				.equals(StockInStatus.已派送)) {
			return this.printMessage(1, "入库登记单状态已更改，当前状态: "
					+ item.getInboundRegister().getStockInStatus());
		}

		inboundReceiptService.deleteItem(id);

		return this.printMessage(0, "入库收货检验项已删除");
	}

	// 保存入库验货数据
	@RequestMapping(value = "/inbound-receipt-item/save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveReceiptItem(Long inboundReceiptItemId,
			Long inboundRegisterItemId, int amount, double weight,
			String containerCode, Long tallyAreaid, String readCode) {
		try {
			inboundRegisterService.saveInboundReceiptItem(inboundReceiptItemId,
					inboundRegisterItemId, amount, weight, tallyAreaid,
					containerCode,readCode);
		} catch (Exception e) {
			return this.printMessage(1, e.getMessage());
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("registerItem", inboundRegisterService
				.getInboundRegisterItem(inboundRegisterItemId));
		resultMap.putAll(this.printMessage(0, "保存收货明细成功"));
		return resultMap;
	}

	// 入库收货批次预览
	@RequestMapping(value = "/inbound-receipt-item/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findInboundReceiptItems(Long inboundRegisterId) {
		InboundRegister inboundRegister = inboundRegisterService
				.findOne(inboundRegisterId);
		List<InboundReceiptItem> receiptItems = inboundReceiptService
				.findByInboundRegisterAndtarryArea(inboundRegisterId);
		
		for (InboundReceiptItem receiptItem:receiptItems) {
			//手持机用到，返回值为null时，手持机报错；
			if (receiptItem.getReadCode() == null) {
				receiptItem.setReadCode("");
			}
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("receiptItems", receiptItems);
		result.put("register", inboundRegister);
		return result;
	}

	// 完成入库验货
	@RequestMapping(value = "/inbound-register/checked", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> checked(Long inboundRegisterId,
			boolean ignoreAmountCheck, Long tallyareaId) {
		InboundRegister inboundRegister = inboundRegisterService
				.findOne(inboundRegisterId);
		if (!inboundRegister.getStockInStatus().equals(StockInStatus.已派送)) {
			this.printMessage(1,
					"入库登记单状态已更改，当前状态: " + inboundRegister.getStockInStatus());
		}

		if (CollectionUtils.isEmpty(inboundRegister.getInboundReceiptItems())) {
			return this.printMessage(1, "入库登记单尚未收货，不能结束清点操作");
		}

		if (!ignoreAmountCheck) {
			int registerAmount = 0;
			for (InboundRegisterItem registerItem : inboundRegister
					.getInboundRegisterItems()) {
				registerAmount += registerItem.getAmount();
			}

			int receiptAmount = 0;
			for (InboundReceiptItem receiptItem : inboundRegister
					.getInboundReceiptItems()) {
				receiptAmount += receiptItem.getAmount();
			}

			if (registerAmount != receiptAmount) {
				return this.printMessage(10, "入库登记数量与入库收货数量不符");
			}
		}
		inboundRegisterService.updateInboundTarrychecked(
				inboundRegister.getId(), tallyareaId, true);
		// boolean ischecked =
		// inboundRegisterService.tallyareaStatus(inboundRegister.getId());
		// if (ischecked) {
	     Set<StockIn> stockInList = inboundRegister.getStockIns();
	        boolean result = true;
	        for (StockIn stockIn2 : stockInList) {
	            if (stockIn2.getStockInStatus() != StockInStatus.已上架) {
	                result = false;
	                break;
	            }
	      }
	      if(result){
	    	//上架完毕发通知
	    	  if (inboundRegister.getStockInStatus() != StockInStatus.已完成 && inboundRegister.getStockInStatus() != StockInStatus.已作废){
	    		  inboundRegisterService.updateStatus(inboundRegister.getId(),
			  				StockInStatus.已上架);
		          taskService.assignStockInEndNotice(inboundRegister); 
	    	  }
	      }else{
	    	  if (inboundRegister.getStockInStatus() != StockInStatus.已完成 && inboundRegister.getStockInStatus() != StockInStatus.已作废){
	    		  if(inboundRegister.getStockInStatus()==StockInStatus.已清点){
	    			  inboundRegister.setStockInStatus(StockInStatus.上架中);
	    		  }
	    		  if(inboundRegister.getStockInStatus()==StockInStatus.已派送){
		    		  inboundRegisterService.updateStatus(inboundRegister.getId(),
		  	  				StockInStatus.已清点);
		  	  		  taskService.assignInboundReceiptNotice(inboundRegister);
	    		  }
	    	  }
	      } 
		
		
		
		// }
		return this.printMessage(0, "入库单清点完成");
	}

}
