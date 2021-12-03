package com.wwyl.controller.store;


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
import com.wwyl.controller.BaseController;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.store.StoreAreaAssignee;
import com.wwyl.service.security.SecurityService;
import com.wwyl.service.store.StoreAreaAssigneeService;

/**库存分配
 * @author jianl
 */
@Controller
@RequestMapping("/store/store-area-assignee")
public class StoreAreaAssigneeController extends BaseController {

	@Resource
	private StoreAreaAssigneeService storeAreaAssigneeService;
	
	@Resource
	private SecurityService securityService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public List<Operator> list() {
		List<Operator>  operators=securityService.findOperatorsByRole(Constants.ROLE_KEEPER);
		return operators;
	}

	@RequestMapping(value = "getoprator", method = RequestMethod.GET)
	@ResponseBody
	public List<StoreAreaAssignee> getoprator(Long id) {
		List<StoreAreaAssignee> storeAreaAssignees=	storeAreaAssigneeService.findByOperatorId(id);
		return storeAreaAssignees;
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		
		List<Operator>  operators=securityService.findOperatorsByRole(Constants.ROLE_KEEPER);
		model.addAttribute("_method", "post");
		model.addAttribute("operators", operators);
		return formPage;
	}
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		
		List<Operator>  operators=securityService.findOperatorsByRole(Constants.ROLE_KEEPER);
		Operator  operator=securityService.findOperatorById(id);
		model.addAttribute("_method", "post");
		model.addAttribute("operators", operators);
		model.addAttribute("operator", operator);
		
		return formPage;
	}
	
	@RequestMapping(value="/{id}",method = RequestMethod.POST)
	public String update(@PathVariable Long id,@ModelAttribute("storeAreaAssignee") @Valid StoreAreaAssignee storeAreaAssignee, BindingResult result,Long[] storeAreas,ModelMap model) {
		
		storeAreaAssigneeService.save(storeAreaAssignee,storeAreas);
		return indexRedirect;
	}
	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("storeAreaAssignee") @Valid StoreAreaAssignee storeAreaAssignee, BindingResult result,Long[] storeAreas,ModelMap model) {
		
		storeAreaAssigneeService.save(storeAreaAssignee,storeAreas);
		return indexRedirect;
	}



	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		storeAreaAssigneeService.delete(id);
		return indexRedirect;
	}

}
