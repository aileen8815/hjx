package com.wwyl.controller.settings;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Constants;
import com.wwyl.Enums.StoreAreaRentStatus;
import com.wwyl.Enums.StoreAreaStatus;
import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.StoreArea;
import com.wwyl.service.security.SecurityService;
import com.wwyl.service.settings.CustomerService;
import com.wwyl.service.settings.StoreAreaService;

/**
 * @author yucaohe
 */
@Controller
@RequestMapping("/settings/store-area")
public class StoreAreaController extends BaseController {
	@Resource
	private StoreAreaService storeAreaService;
	@Resource
	private SecurityService securityService;
	@Resource
	private CustomerService customerService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "tree", method = RequestMethod.GET)
	@ResponseBody
	public List<StoreArea> tree() {
		List<StoreArea> st = storeAreaService.findRootStoreAreas();
		return st;
	}

	// 新增仓间节点信息
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model, Long parentId) {
		StoreArea storeArea = new StoreArea();
		if (parentId != null && parentId > 0) {
			StoreArea parent = storeAreaService.findOne(parentId);
			storeArea.setParent(parent);
		}
		model.addAttribute("_method", "post");
		prepareInput(model, storeArea);
		return formPage;
	}

	// 保存提交的数据
	@RequestMapping(method = RequestMethod.POST)
	public String create(ModelMap model,@ModelAttribute("storeArea") @Valid StoreArea storeArea,Long[] storeproducts,Long[] operatorIds, BindingResult result) {
		if (result.hasErrors()) {
			prepareInput(model, storeArea);
			return formPage;
		}
		
		storeArea.setStoreProducts(storeproducts);
		storeAreaService.save(storeArea,operatorIds);
		return indexRedirect;
	}

	// 通过ID取到仓间信息
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public StoreArea get(@PathVariable Long id) {
		return storeAreaService.findOne(id);
	}

	// 跳转修改仓间页面
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		StoreArea storeArea = storeAreaService.findOne(id);
		model.addAttribute("_method", "put");
		prepareInput(model, storeArea);
		return formPage;
	}

	private void prepareInput(ModelMap model, StoreArea storeArea) {
		model.addAttribute("storeArea", storeArea);
		model.addAttribute("storeAreaStatusList", StoreAreaStatus.values());
		model.addAttribute("storeAreaRentStatusList", StoreAreaRentStatus.values());
		model.addAttribute("operators",securityService.findOperatorsByRole(Constants.ROLE_KEEPER));
	}

	// 保存修改后的记录
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("storeArea") @Valid StoreArea storeArea, BindingResult result,String perms,Long[] storeproducts,Long[] operatorIds, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			prepareInput(model, storeArea);
			this.printBindingResult(result);
			return formPage;
		}
		
		storeArea.setStoreProducts(storeproducts);
		storeAreaService.save(storeArea, operatorIds);
		return indexPage;
	}

	// 删除节点信息
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		storeAreaService.delete(id);
		return indexPage;
	}

	@RequestMapping(value = "/{id}/organise", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable Long id, Long parentId, String point) {
		StoreArea storeArea = storeAreaService.findOne(id);
		if ("append".equals(point)) {
			StoreArea area = storeAreaService.findOne(parentId);
			storeArea.setParent(area);
		} else {
			storeArea.setParent(null);
		}
		try {
			storeAreaService.save(storeArea,new Long[]{});
			return "OK";
		} catch (Exception e) {
			return "ERROR";
		}
	}
	
	@RequestMapping(value = "/{id}/add-products", method = RequestMethod.GET)
	public String addproduct(@PathVariable Long id,ModelMap model) {
		StoreArea storeArea = storeAreaService.findOne(id);
		model.put("storeArea",storeArea);
		model.put("customers", customerService.findAll());
		return "/settings/store_area_add_products";
	}
	
	@RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
	public String storeProduct(@PathVariable Long id,ModelMap model) {
		StoreArea storeArea = storeAreaService.findOne(id);
		model.put("storeArea",storeArea);
		return "/settings/store_area_view";
	}
	
	@RequestMapping(value = "save-products", method = RequestMethod.POST)
	public String addproduct(Long[] products, Long storeAreaId ,Long customerId) {
		storeAreaService.addproduct( products,  storeAreaId , customerId);
		 
		return "redirect:/settings/store-area/"+storeAreaId+"/view";
	}
	@RequestMapping(value = "del-product", method = RequestMethod.GET)
	public String delproduct(Long productId, Long storeAreaId) {
		storeAreaService.delStoreProduct(productId,storeAreaId);
		return "redirect:/settings/store-area/"+storeAreaId+"/view";
	}
}
