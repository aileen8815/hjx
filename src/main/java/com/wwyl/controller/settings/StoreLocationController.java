package com.wwyl.controller.settings;

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Enums.StoreLocationStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.StoreLocation;
import com.wwyl.service.settings.StoreAreaService;
import com.wwyl.service.settings.StoreLocationService;
import com.wwyl.service.settings.StoreLocationTypeService;

/**
 * @author yucaohe
 */
@Controller
@RequestMapping("/settings/store-location")
public class StoreLocationController extends BaseController {

	@Resource
	private StoreLocationService storeLocationService;
	@Autowired
	private StoreLocationTypeService storeLocationTypeService;
	@Autowired
	private StoreAreaService storeAreaService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("storeLocationStatus", StoreLocationStatus.values());
		model.addAttribute("storeLocationTypes", storeLocationTypeService.findAll());
		model.addAttribute("storeAreas", storeAreaService.findAll());
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "code", defaultValue = "") String code,StoreLocationStatus storeLocationStatus,Long storeLocationTypeId,Long storeAreaId) {
		Page<StoreLocation> storeLocations = storeLocationService.findStoreLocationByConditions(page, rows, code, storeLocationStatus, storeLocationTypeId, storeAreaId);
		return toEasyUiDatagridResult(storeLocations);
	}

	// 创建储位
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("_method", "post");
		StoreLocation storeLocation = new StoreLocation();
		storeLocation.setStoreLocationStatus(StoreLocationStatus.未绑定);
		model.addAttribute("storeLocation", storeLocation);
		prepareInput(model);
		return formPage;
	}

	private void prepareInput(ModelMap model) {
		model.addAttribute("storeLocationTypes", storeLocationTypeService.findAll());
		model.addAttribute("storeAreas", storeAreaService.findAll());
		model.addAttribute("storeLocationStatusList", StoreLocationStatus.values());
	}

	// 保存储位信息
	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("storeLocation") @Valid StoreLocation storeLocation, BindingResult result, ModelMap model) {
/*		if (result.hasErrors()) {
			return formPage;
		}*/
		storeLocationService.create(storeLocation);
		return indexRedirect;
	}

	// 修改储位信息
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		StoreLocation storeLocation = storeLocationService.findOne(id);
		model.addAttribute("_method", "put");
		model.addAttribute("storeLocation", storeLocation);
		prepareInput(model);
		return formPage;
	}

	// 保存修改信息
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("storeLocation") @Valid StoreLocation storeLocation, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			prepareInput(model);
			this.printBindingResult(result);
			return formPage;
		}
		storeLocationService.update(storeLocation);
		return indexRedirect;
	}

	// 删除储位信息
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		storeLocationService.delete(id);
		return indexRedirect;
	}
	//批量添加储位

	@RequestMapping(value = "/batch-add", method = RequestMethod.GET)
	public String batchAdd(ModelMap model) {
		prepareInput(model);
		return "/settings/store_location_batch_add";
	}

	@RequestMapping(value = "/batch-create", method = RequestMethod.POST)
	public String batchCreate(StoreLocation storeLocation) {
		storeLocationService.batchCreate( storeLocation);
		return indexRedirect;
	}
}
