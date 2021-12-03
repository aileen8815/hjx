package com.wwyl.controller.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fyunli
 */

@Controller
public class ReportInboundRegisterController extends BaseReportController {

	@RequestMapping("/report-inboundregister")
	public void report(HttpServletRequest request, HttpServletResponse response) {
		super.report(request, response, "inbound_register.prpt", "pdf", null);
	}
	
}
