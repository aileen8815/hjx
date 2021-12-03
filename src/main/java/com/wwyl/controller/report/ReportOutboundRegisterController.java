package com.wwyl.controller.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fyunli
 */

@Controller
public class ReportOutboundRegisterController extends BaseReportController {

	@RequestMapping("/report-outboundregister")
	public void report(HttpServletRequest request, HttpServletResponse response) {
		super.report(request, response, "outbound_register.prpt", "pdf", null);
	}
	
}
