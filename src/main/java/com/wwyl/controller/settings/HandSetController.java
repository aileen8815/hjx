package com.wwyl.controller.settings;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.Enums;
import com.wwyl.controller.BaseController;
import com.wwyl.dao.RepoUtils;
import com.wwyl.entity.settings.HandSet;
import com.wwyl.service.settings.HandSetService;

/**
 * @author zhaozz
 */
@Controller
@RequestMapping("/settings/hand-set")
public class HandSetController extends BaseController {

	@Resource
	HandSetService handSetService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model) {
		prepareInput(model);
		return formPage;
	}

	private void prepareInput(ModelMap model) {
		List<HandSet> handSet = handSetService.findAll();
		model.addAttribute("handSet", handSet);
		model.addAttribute("handSetStatus", Enums.HandSetStatus.values());
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("handSet") @Valid HandSet handSet, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			prepareInput(model);
			return formPage;
		}
		handSetService.save(handSet);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "name", defaultValue = "") String name) {
		Page<HandSet> handSet = handSetService.findByCodeOrNameLike(page, rows, name);
		return toEasyUiDatagridResult(handSet);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public HandSet gethandSet(@PathVariable Long id) {
		HandSet handSet = handSetService.findOne(id);
		return handSet;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		HandSet handSet = handSetService.findOne(id);
		prepareInput(model);
		model.addAttribute("handSet", handSet);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("handSet") @Valid HandSet handSet, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			prepareInput(model);
			return formPage;
		}
		handSetService.save(handSet);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		handSetService.delete(id);
		return indexRedirect;
	}
	
	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity exists(String code) {
		if (handSetService.exists(code)) {
			return new ResponseEntity(HttpStatus.OK);
		} else {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}
}
