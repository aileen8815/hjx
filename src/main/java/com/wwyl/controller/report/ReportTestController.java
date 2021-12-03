package com.wwyl.controller.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fyunli
 */

@Controller
@RequestMapping("/report")
public class ReportTestController extends BaseReportController {

    @RequestMapping("/report-test")
    public String index() {
        return "/report/report_test";
    }

	@RequestMapping("/report-test.pdf")
	public void report(HttpServletRequest request, HttpServletResponse response) {
		super.report(request, response, "storeLocatoin.prpt", "pdf", null);
	}

}
