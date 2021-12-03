package com.wwyl.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fyunli
 */
@Controller
public class ErrorController extends BaseController {

	@RequestMapping(value = "/403")
	public String http403() {
		return "/403";
	}

	@RequestMapping(value = "/404")
	public String http404() {
		return "/404";
	}

	@RequestMapping(value = "/error")
	@ResponseBody
	public Map<String, Object> printJsonError(String error) {
		return this.printMessage(1, error);
	}

}
