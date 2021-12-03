package com.wwyl.controller.settings;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wwyl.controller.BaseController;
import com.wwyl.entity.settings.Area;
import com.wwyl.service.settings.AreaService;

/**
 * @author jianl
 */
@Controller
@RequestMapping("/settings/area")
public class AreaController extends BaseController {

	@Resource
	private AreaService areaService;

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return indexPage;
	}

	@RequestMapping(value = "tree", method = RequestMethod.GET)
	@ResponseBody
	public List<Area> tree(Long id) {
		return areaService.findRootareas(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public Area get(@PathVariable Long id) {
		return areaService.findOne(id);
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(ModelMap model, Long parentId) {
		Area area = new Area();
		if (parentId != null && parentId > 0) {
			Area parent = areaService.findOne(parentId);
			area.setParent(parent);
		}
		model.addAttribute("_method", "post");
		model.addAttribute("area", area);
		return formPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@ModelAttribute("area") @Valid Area area, BindingResult result) {
		if (result.hasErrors()) {
			return formPage;
		}
		areaService.save(area);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		Area area = areaService.findOne(id);
		model.addAttribute("_method", "put");
		model.addAttribute("area", area);
		return formPage;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(@ModelAttribute("area") @Valid Area area, String perms, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("_method", "put");
			this.printBindingResult(result);
			return formPage;
		}

		areaService.save(area);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, ModelMap model) {
		areaService.delete(id);
		return indexRedirect;
	}

	@RequestMapping(value = "/{id}/organise", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable Long id, Long parentId, String point) {
		Area area = areaService.findOne(id);
		if ("append".equals(point)) {
			Area parent = areaService.findOne(parentId);
			area.setParent(parent);
		} else {
			area.setParent(null);
		}
		try {
			areaService.save(area);
			return printMessage(0);
		} catch (Exception e) {
			return printMessage(1, e.getMessage());
		}
	}

	@RequestMapping(value = "/exists", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity exists(String oldCode,String code) {
		if (StringUtils.equals(oldCode, code)) {
	            return new ResponseEntity(HttpStatus.OK);
	    }

		if (areaService.exists(code)) {
			 return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);

			
		} 
			return new ResponseEntity(HttpStatus.OK);
	 
	}
}
