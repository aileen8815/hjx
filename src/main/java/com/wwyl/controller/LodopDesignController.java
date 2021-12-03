package com.wwyl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fyunli
 */
@Controller
@RequestMapping("/lodop/design")
public class LodopDesignController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(LodopDesignController.class);

	private static final String LODOP_TEMPLATE_FOLDER = "/WEB-INF/freemarker/print/template";
	private static final String LODOP_RUNTIME_SUFFIX = ".ftl";
	private static final String LODOP_DEFINE_SUFFIX = ".define";

	@RequestMapping(method = RequestMethod.GET)
	public String execute(ModelMap modelMap, ServletRequest request) {
		String lodopTemplateFolder = request.getServletContext().getRealPath(LODOP_TEMPLATE_FOLDER);
		File folder = new File(lodopTemplateFolder);
		File[] defineFiles = this.listFiles(folder);
		modelMap.addAttribute("defineFiles", defineFiles);
		return "/print/lodop_design";
	}

	@RequestMapping("/content")
	@ResponseBody
	public Map<String, Object> getDefineContent(ServletRequest request, String lodopFile) {
		try {
			String lodopTemplateFolder = request.getServletContext().getRealPath(LODOP_TEMPLATE_FOLDER);
			File defineFile = new File(lodopTemplateFolder + File.separator + lodopFile);
			BufferedReader fileReader = new BufferedReader(new FileReader(defineFile));
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			while ((line = fileReader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append("\n");
			}
			fileReader.close();
			return this.printMessage(0, stringBuilder.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return this.printMessage(1, "get content failed");
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(ServletRequest request, String lodopFile, String lodopContent) {
		String lodopTemplateFolder = request.getServletContext().getRealPath(LODOP_TEMPLATE_FOLDER);
		File uploadDir = new File(lodopTemplateFolder);
		if (!(uploadDir.isDirectory() && uploadDir.canWrite())) {
			return this.printMessage(1, "上传目录不存在或不可写");
		}

		try {
			lodopContent = StringUtils.replace(lodopContent, "&quot;", "\"");
			System.out.println(lodopContent);
			this.writeLodopDefine(lodopTemplateFolder, lodopFile, lodopContent);
			this.writeLodopRuntime(lodopTemplateFolder, lodopFile, lodopContent);
			return this.printMessage(0, "OK");
		} catch (Exception e) {
			logger.error("execute() - Exception e={}", e); //$NON-NLS-1$
			return this.printMessage(e);
		}
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		return "/print/print_test";
	}

	private File[] listFiles(File folder) {
		if (folder.isDirectory()) {
			return folder.listFiles(new FileFilter() {
				public boolean accept(File file) {
					return file.isDirectory() || file.getName().endsWith(".define");
				}
			});
		} else {
			logger.error("lodop define folder is not exist.");
			return null;
		}
	}

	private void writeLodopDefine(String lodopTemplateFolder, String lodopFile, String lodopContent) throws FileNotFoundException, IOException {
		File lodopDefineFile = new File(lodopTemplateFolder + File.separator + lodopFile + LODOP_DEFINE_SUFFIX);
		if (lodopDefineFile.exists()) {
			this.backupLodopDefine(lodopTemplateFolder, lodopDefineFile);
		}

		FileOutputStream defineOutputStream = new FileOutputStream(lodopDefineFile);
		defineOutputStream.write(lodopContent.getBytes());
		defineOutputStream.close();
	}

	private void backupLodopDefine(String lodopTemplateFolder, File lodopDefineFile) throws FileNotFoundException, IOException {
		FileInputStream defineInputStream = new FileInputStream(lodopDefineFile);
		String backupLodopFile = lodopTemplateFolder + File.separator + lodopDefineFile.getName() + "." + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
		FileOutputStream backupOutputStream = new FileOutputStream(backupLodopFile);
		IOUtils.copy(defineInputStream, backupOutputStream);
		defineInputStream.close();
		backupOutputStream.close();
	}

	private void writeLodopRuntime(String lodopTemplateFolder, String lodopFile, String lodopContent) throws FileNotFoundException, IOException {
		File lodopRuntimeFile = new File(lodopTemplateFolder + File.separator + lodopFile + LODOP_RUNTIME_SUFFIX);
		FileOutputStream runtimeOutputStream = new FileOutputStream(lodopRuntimeFile);
		runtimeOutputStream.write(this.complieRuntimeContent(lodopContent).getBytes());
		runtimeOutputStream.close();
	}

	String complieRuntimeContent(String content) {
		Pattern pattern = Pattern.compile("\"data((\\.)?[a-zA-Z0-9]*)+\"");
		Matcher matcher = pattern.matcher(content);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String replacement = matcher.group(0).replaceAll("\"", "");
			matcher.appendReplacement(sb, replacement);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

}
