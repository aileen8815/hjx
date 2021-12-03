package com.wwyl.controller.settings;

import java.util.Map;

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

import com.wwyl.Enums.StoreContainerStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.StoreContainer;
import com.wwyl.service.settings.StoreContainerService;
import com.wwyl.service.settings.StoreContainerTypeService;

/**
 * @author yche
 */
@Controller
@RequestMapping("/settings/store-container")
public class StoreContainerController extends BaseController {
	@Autowired
	StoreContainerService storeContainerService;
	@Autowired
	StoreContainerTypeService storeContainerTypeService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("storeContainerTypes", storeContainerTypeService.findAll());
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		StoreContainer storeContainer = new StoreContainer();
		storeContainer.setStoreContainerStatus(StoreContainerStatus.未绑定);
		prepareInput(model, storeContainer);
		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("storeContainer") @Valid StoreContainer storeContainer, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			prepareInput(model, storeContainer);
			return formPage;
		}
		storeContainerService.save(storeContainer);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "label", defaultValue = "") String label,StoreContainerStatus storeContainerStatus,Long storeContainerTypeId ) {
		Page<StoreContainer> storeContainers = storeContainerService.findStoreContainerByConditions(page, rows, label, storeContainerStatus, storeContainerTypeId);
		return toEasyUiDatagridResult(storeContainers);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public StoreContainer getStoreContainer(@PathVariable Long id) {
		StoreContainer storeContainer = storeContainerService.findOne(id);
		return storeContainer;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		StoreContainer storeContainer = storeContainerService.findOne(id);
		prepareInput(model, storeContainer);
		return formPage;
	}

	private void prepareInput(ModelMap model, StoreContainer storeContainer) {
		model.addAttribute("storeContainerStatus", StoreContainerStatus.values());
		model.addAttribute("storeContainerTypes", storeContainerTypeService.findAll());
		model.addAttribute("storeContainer", storeContainer);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("storeContainer") @Valid StoreContainer storeContainer, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			prepareInput(model, storeContainer);
			return formPage;
		}
		storeContainerService.save(storeContainer);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		storeContainerService.delete(id);
		return indexRedirect;
	}

	@RequestMapping(value = "/batch-add", method = RequestMethod.GET)
	public String batchAdd(ModelMap model) {
		prepareInput(model, null);
		return "/settings/store_container_batch_add";
	}

	@RequestMapping(value = "/batch-create", method = RequestMethod.POST)
	public String batchCreate(Long storeContainerTypeId, int amount, int length, int width, int height, int weight) {
		storeContainerService.batchCreate(storeContainerTypeId, amount, length, width, height, weight);
		return "/settings/store_container_batch_create";
	}

}
