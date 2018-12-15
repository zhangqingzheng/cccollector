package com.cccollector.news.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cccollector.news.model.TemplateMapping;
import com.cccollector.news.service.TemplateService;
import com.cccollector.news.util.TemplateUtil;

@Component
public class TimedTask {

	@Autowired
	private TemplateService templateService;

	/**
	 * Nginx发布目录
	 */
	@Value("${pom.config.nginxDataPath}")
	private String nginxDataPath;

	int pageSize = 25;

	int staticPages = 3;

	// 处理1分钟以后栏目静态化
	@Scheduled(cron = "0/5 * * * * ?") // 从第0分钟开始，每隔1一分钟执行一次
	// @Scheduled(cron = "* * 0/1 * * ?") // 从第0分钟开始，每隔1一分钟执行一次
	public void dealColumnStaticPage() {

		List<Integer> columnIds = TemplateUtil.getTobeStaticColumns();

		if (columnIds != null && columnIds.size() > 0) {
			System.out.println("定时静态化任务启动,任务数量：" + columnIds.size());
		} else {
			System.out.println("定时静态化任务启动,无任务结束");
		}

		for (Integer columnId : columnIds) {
			templateService.staticPageContent(columnId, TemplateMapping.TypeEnum.栏目.ordinal(), null);
		}
	}
}
