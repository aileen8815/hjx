package com.wwyl.controller.settings;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import com.wwyl.entity.settings.TallyArea;
import com.wwyl.entity.settings.TallyAreaType;
import com.wwyl.service.settings.TallyAreaService;
import com.wwyl.service.settings.TallyAreaTypeService;

/**
 * @author liujian
 */
@Controller
@RequestMapping("settings/tally-area")
public class TallyAreaController extends BaseController {

	@Resource
	TallyAreaService tallyAreaService;

	@Resource
	TallyAreaTypeService tallyAreaTypeService;

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
		List<TallyAreaType> tallyAreaTypes = tallyAreaTypeService.findAll();
		model.addAttribute("tallyAreaTypes", tallyAreaTypes);
		model.addAttribute("tallyAreaStatuses", Enums.TallyAreaStatus.values());
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute("tallyAreaType") @Valid TallyArea tallyArea, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			prepareInput(model);
			return formPage;
		}
		tallyAreaService.save(tallyArea);
		return indexRedirect;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "rows", defaultValue = RepoUtils.DEFAULT_PAGE_SIZE) int rows,
			@RequestParam(value = "name", defaultValue = "") String name) {
		Page<TallyArea> tallyAreas = tallyAreaService.findByCodeOrNameLike(page, rows, name);
		return toEasyUiDatagridResult(tallyAreas);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public TallyArea gettallyArea(@PathVariable Long id) {
		TallyArea tallyArea = tallyAreaService.findOne(id);
		return tallyArea;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		TallyArea tallyArea = tallyAreaService.findOne(id);
		prepareInput(model);
		model.addAttribute("tallyArea", tallyArea);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute("tallyArea") @Valid TallyArea tallyArea, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			prepareInput(model);
			return formPage;
		}
		tallyAreaService.save(tallyArea);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		tallyAreaService.delete(id);
		return indexRedirect;
	}
	
	@RequestMapping(value = "/valid-code", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity exists(String oldCode,String code) {
		if (StringUtils.equals(oldCode, code)) {
	            return new ResponseEntity(HttpStatus.OK);
	    }

		if (tallyAreaService.exists(code)) {
			 return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);

			
		} 
			return new ResponseEntity(HttpStatus.OK);
	 
	}

}
