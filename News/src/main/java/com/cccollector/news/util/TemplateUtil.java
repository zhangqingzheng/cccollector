package com.cccollector.news.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.cccollector.news.model.Column;
import com.cccollector.news.templatemodel.TemplateBaseContent;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class TemplateUtil {

	// TODO:提取到配置文件
	// 时间间隔
	static int delay = 60;

	private static final Object LOCK = new Object();

	private static Map<String, Configuration> templateFolderCache = new HashMap<String, Configuration>();

	/** 要被静态化的栏目 */
	private static Map<Integer, Integer> _tobeStaticColumns = new HashMap<>();

	/**
	 * @return
	 */
	public static List<Integer> getTobeStaticColumns() {

		synchronized (LOCK) {

			List<Integer> result = new ArrayList<>();

			// 获取当前时间
			int currentTime = (int) ((System.currentTimeMillis()) / 1000);

			for (Map.Entry<Integer, Integer> entry : _tobeStaticColumns.entrySet()) {

				Integer taskData = entry.getValue();

				if (taskData + delay > currentTime) {
					result.add(entry.getKey());
				}
			}

			// 从队列中移除
			for (Integer columnId : result) {
				_tobeStaticColumns.remove(columnId);
			}
			return result;
		}
	}

	/**
	 * 增加要被静态化的栏目，如果
	 * 
	 * @param columnIds 栏目ID
	 */
	public static void addTobeStaticColumns(List<Integer> columnIds) {

		synchronized (LOCK) {

			// 获取当前时间
			int currentTime = (int) ((System.currentTimeMillis()) / 1000);

			for (Integer columnId : columnIds) {

				if (!_tobeStaticColumns.containsKey(columnId)) {
					_tobeStaticColumns.put(columnId, currentTime);
				}
			}
		}
	}

	private static Configuration init(String folder) {

		// TODO:正式环境应该关闭DEBUG和增加模板缓存
		Configuration configuration = null;
		try {
			// 创建一个freemarker.template.Configuration实例，它是存储 FreeMarker 应用级设置的核心部分
			// 指定版本号
			configuration = new Configuration(Configuration.VERSION_2_3_28);
			// 设置模板目录
			configuration.setDirectoryForTemplateLoading(new File(folder));
			// 设置默认编码格式
			configuration.setDefaultEncoding("UTF-8");

			// 处理日志与异常
			configuration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
			configuration.setLogTemplateExceptions(false);
			configuration.setWrapUncheckedExceptions(true);
			// 存放入Map中
			templateFolderCache.put(folder, configuration);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return configuration;
	}

	public static String content2Html(TemplateBaseContent content) {

		File templateFile = content.getTemplateFile();

		if (templateFile == null) {
			// TODO:待删除
			System.out.println("对应的模板文件未设置,无法继续处理,本次静态化退出");
			return null;
		}

		if (!templateFile.exists() || !templateFile.isFile()) {
			// TODO:待删除
			System.out.println("对应的模板文件不存在或者不是一个文件,无法继续处理,本次静态化退出,文件路径:" + templateFile.getAbsolutePath());
			return null;
		}

		ByteArrayOutputStream byteArrayOutputStream = null;
		try {

			byteArrayOutputStream = new ByteArrayOutputStream();
			Writer out = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");

			String templateFolder = templateFile.getParent();
			String templateName = templateFile.getName();

			// 从map中获取配置对象
			Configuration configuration = templateFolderCache.get(templateFolder);

			if (configuration == null) {
				configuration = init(templateFolder);
			}

			// 从配置对象设置的目录中获得模板
			Template template = configuration.getTemplate(templateName);

			template.process(content, out);

			return byteArrayOutputStream.toString("UTF-8");

		} catch (TemplateException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean staticPages(List<TemplateBaseContent> contents) {

		boolean result = true;

		for (TemplateBaseContent templateBaseContent : contents) {
			result = result && staticPage(templateBaseContent);
		}

		return result;
	}

	public static boolean staticPage(TemplateBaseContent content) {

		if (content.getStaticFilePath() == null) {
			return false;
		}

		List<File> targetFiles = new ArrayList<>();
		if (content.getTomcatFolder() != null) {
			targetFiles.add(new File(content.getTomcatFolder(), content.getStaticFilePath()));
		}

		if (content.getNginxFolder() != null) {
			targetFiles.add(new File(content.getNginxFolder(), content.getStaticFilePath()));
		}

		String html = content2Html(content);

		if (html == null) {
			return false;
		}

		boolean result = true;

		for (File targetFile : targetFiles) {

			File targetFolder = targetFile.getParentFile();

			if (!targetFolder.exists() && !targetFolder.mkdirs()) {
				System.out.println("创建数据文件夹失败,无法继续处理,本次静态化退出,文件路径:" + targetFolder.getAbsolutePath());
				result = false;
				continue;
			}

			// TODO:待删除
			System.out.println("开始生成静态化文件" + targetFile);

			try {

				FileUtils.writeStringToFile(targetFile, html, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
				result = false;
				continue;
			}
		}

		return result;
	}

}
