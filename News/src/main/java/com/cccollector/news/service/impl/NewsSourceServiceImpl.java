/**
 * 
 */
package com.cccollector.news.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.AppDao;
import com.cccollector.news.dao.NewsSourceDao;
import com.cccollector.news.dao.SiteDao;
import com.cccollector.news.model.App;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.Site;
import com.cccollector.news.service.NewsSourceService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 新闻源服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("newsSourceService")
public class NewsSourceServiceImpl extends GenericServiceHibernateImpl<Integer, NewsSource> implements NewsSourceService {

	@Autowired
	private NewsSourceDao newsSourceDao;
	
	@Autowired
	private AppDao appDao;
	
	@Autowired
	private SiteDao siteDao;

	@Override
	public void addNewsSource(NewsSource newsSource) {
		// 设置排序位置
		Integer position = newsSourceDao.max("position", null);
		newsSource.setPosition(position == null ? 0 : position.intValue() + 1);
		
		// 保存新闻源
		newsSourceDao.save(newsSource);		
	}

	@Override
	public void moveNewsSources(List<NewsSource> newsSources) {
		int position = 0;
		for (NewsSource newsSource : newsSources) {
			newsSource.setPosition(position);
			newsSourceDao.update(newsSource, "position");
			position++;
		}
	}

	@Override
	public void addNewsSourceToApp(int newsSourceId, int appId) {
		NewsSource newsSource = newsSourceDao.loadById(newsSourceId);
		App app = appDao.loadById(appId, "newsSources");
		app.getNewsSources().add(newsSource);
	}

	@Override
	public void moveNewsSourcesInApp(List<NewsSource> newsSources, int appId) {
		App app = appDao.loadById(appId, "newsSources");
		// 清除应用包含的所有新闻源，并同步数据库
		app.getNewsSources().clear();
		appDao.getEntityManager().flush();
		// 将新闻源添加至应用
		app.getNewsSources().addAll(newsSources);
	}

	@Override
	public void deleteNewsSourceFromApp(int newsSourceId, int appId) {
		App app = appDao.loadById(appId, "newsSources");
		for (NewsSource newsSource : app.getNewsSources()) {
			if (newsSource.getNewsSourceId() == newsSourceId) {
				// 从应用中去掉需要删除的新闻源，并将剩余的新闻源保存至newsSourceList
				app.getNewsSources().remove(newsSource);
				List<NewsSource> newsSourceList = new ArrayList<NewsSource>(app.getNewsSources());
				// 清除应用包含的所有新闻源，并同步数据库
				app.getNewsSources().clear();
				appDao.getEntityManager().flush();
				// 将newsSourceList中保存的新闻源添加至应用
				app.getNewsSources().addAll(newsSourceList);
				return;
			}
		}
	}
	
	@Override
	public void addNewsSourceToSite(int newsSourceId, int siteId) {
		NewsSource newsSource = newsSourceDao.loadById(newsSourceId);
		Site site = siteDao.loadById(siteId, "newsSources");
		site.getNewsSources().add(newsSource);
	}

	@Override
	public void moveNewsSourcesInSite(List<NewsSource> newsSources, int siteId) {
		Site site = siteDao.loadById(siteId, "newsSources");
		// 清除站点包含的所有新闻源，并同步数据库
		site.getNewsSources().clear();
		siteDao.getEntityManager().flush();
		// 将新闻源添加至站点
		site.getNewsSources().addAll(newsSources);
	}

	@Override
	public void deleteNewsSourceFromSite(int newsSourceId, int siteId) {
		Site site = siteDao.loadById(siteId, "newsSources");
		for (NewsSource newsSource : site.getNewsSources()) {
			if (newsSource.getNewsSourceId() == newsSourceId) {
				// 从站点中去掉需要删除的新闻源，并将剩余的新闻源保存至newsSourceList
				site.getNewsSources().remove(newsSource);
				List<NewsSource> newsSourceList = new ArrayList<NewsSource>(site.getNewsSources());
				// 清除站点包含的所有新闻源，并同步数据库
				site.getNewsSources().clear();
				siteDao.getEntityManager().flush();
				// 将newsSourceList中保存的新闻源添加至站点
				site.getNewsSources().addAll(newsSourceList);
				return;
			}
		}
	}
}
