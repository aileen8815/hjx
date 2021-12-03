package com.wwyl.controller.report;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

/**
 * @author fyunli
 */
public abstract class BaseReportController {

	public void report(HttpServletRequest request, HttpServletResponse response, String reportFile, String rendererType, Map<String, Object> params) {
		try {
			URL urlToReport = new URL("file:" + request.getServletContext().getRealPath("WEB-INF/report/" + reportFile));

			ResourceManager manager = new ResourceManager();
			manager.registerDefaults();
			Resource res = manager.createDirectly(urlToReport, MasterReport.class);
			MasterReport report = (MasterReport) res.getResource();
			if (MapUtils.isNotEmpty(params)) {
				for (String key : params.keySet()) {
					report.getParameterValues().put(key, params.get(key));
				}
			}

			if ("pdf".equalsIgnoreCase(rendererType)) {
				response.setHeader("Content-disposition", "filename=" + System.currentTimeMillis() + ".pdf");
				response.setContentType("application/pdf");
				PdfReportUtil.createPDF(report, response.getOutputStream());
			} else {
				response.getWriter().write("不支持的报表格式！");
			}
		} catch (Exception e) {
			try {
				e.printStackTrace();
				response.getWriter().write("报表生成失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
