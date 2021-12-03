package com.wwyl.controller.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Enums.StoreLocationStatus;
import com.wwyl.Enums.TransactionType;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.service.settings.SerialNumberService;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.StoreContainerService;
import com.wwyl.service.settings.StoreLocationService;

/**
 * @author fyunli
 */
@Controller
@RequestMapping("/api")
public class RfidBindController extends BaseController {

	@Resource
	private StoreContainerService storeContainerService;
	@Resource
	private StoreLocationService storeLocationService;
	@Resource
	private SerialNumberService serialNumberService;	
	@Resource
	private StoreAreaService storeAreaService;

	@RequestMapping(value = "/store-container/get-serial", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getContainerSerial() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("serialNo", serialNumberService.getSerialNumber(TransactionType.StoreContainer));
		return result;
	}

	@RequestMapping(value = "/store-container/bind")
	@ResponseBody
	public Map<String, Object> bindStoreContainer(String serialNo) {
		if (StringUtils.isBlank(serialNo)) {
			return this.printMessage(1, "无效的托盘序列号");
		}
		try { 
			storeContainerService.bind(serialNo);
			return this.printMessage(0, "托盘绑定RFID成功");
		} catch (Exception e) {
			return this.printMessage(1, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/store-container/get-store-container")
	@ResponseBody
	public Map<String, Object> getStoreContainer(String storeContainerLable) {
		if (StringUtils.isBlank(storeContainerLable)) {
			return this.printMessage(1, "参数无效");
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		StoreContainer storeContainer = storeContainerService
				.findStoreContainerByLabel(storeContainerLable);
		result.put("error", 0);
		result.put("result", storeContainer);
		return result;
	}

	@RequestMapping(value = "/store-location/get-serial", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getLocationSerial() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("serialNo", serialNumberService.getSerialNumber(TransactionType.StoreLocation));
		return result;
	}

	@RequestMapping(value = "/store-location", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getStoretLocation(String locationCode) {
		if (StringUtils.isBlank(locationCode)) {
			return this.printMessage(1, "无效的货架编码");
		}

		StoreLocation storeLocation = storeLocationService.findStoreLocationByCode(locationCode);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("storeLocation", storeLocation);
		return result;
	}

	@RequestMapping(value = "/store-location/bind")
	@ResponseBody
	public Map<String, Object> bindStoreLocation(String locationLable, String locationCode, boolean ignoreBindCheck) {
		if (StringUtils.isBlank(locationLable)) {
			return this.printMessage(1, "无效的货架序列号");
		}
		if (StringUtils.isBlank(locationCode)) {
			return this.printMessage(1, "无效的货架编码");
		}

		try {
			if(!ignoreBindCheck)
			{
				StoreLocation storeLocation = storeLocationService.findStoreLocationByCode(locationCode);
				if (!StoreLocationStatus.未绑定.equals(storeLocation.getStoreLocationStatus()))
				{					
					return this.printMessage(10, "货架已绑定RFID，不需要重复绑定");
				}				
			}			
			storeLocationService.bind(locationLable, locationCode);
			return this.printMessage(0, "货架绑定RFID成功");
		} catch (Exception e) {
			return this.printMessage(1, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/store-location/unbind")
	@ResponseBody
	public Map<String, Object> unBindStoreLocation(String locationLable) {
		if (StringUtils.isBlank(locationLable)) {
			return this.printMessage(1, "无效的货架序列号");
		}

		try {						
			storeLocationService.unBind(locationLable);
			return this.printMessage(0, "货架解除绑定RFID成功");
		} catch (Exception e) {
			return this.printMessage(1, e.getMessage());
		}
	}

	@RequestMapping(value = "/store-location/check", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> checkStoretLocation(String locationLabel) {
		if (StringUtils.isBlank(locationLabel)) {
			return this.printMessage(1, "无效的RFID");
		}

		StoreLocation storeLocation = storeLocationService.findStoreLocationByLabel(locationLabel);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("storeLocation", storeLocation);
		return result;
	}
	
	@RequestMapping(value = "/store-container/get-storeArea", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getStoreArea() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("storeArealist", storeAreaService.findAll());
		return result;
	}
	
	@RequestMapping(value = "/get-server-date", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getServerDate() {
		try {
			SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("error", 0L);
			result.put("serverdate", format.format(new Date()));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return this.printMessage(e);
		}
	}
}
