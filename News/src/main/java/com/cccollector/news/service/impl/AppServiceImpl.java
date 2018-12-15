/**
 * 
 */
package com.cccollector.news.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.AppDao;
import com.cccollector.news.dao.EditionDao;
import com.cccollector.news.dao.ReleaseDao;
import com.cccollector.news.dao.NewsSourceDao;
import com.cccollector.news.model.App;
import com.cccollector.news.model.Edition;
import com.cccollector.news.model.Release;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.service.AppService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 应用服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("appService")
public class AppServiceImpl extends GenericServiceHibernateImpl<Integer, App> implements AppService {
	
	@Autowired
	private AppDao appDao;
	
	@Autowired
	private EditionDao editionDao;
	
	@Autowired
	private ReleaseDao releaseDao;
	
	@Autowired
	private NewsSourceDao newsSourceDao;
	
	@Autowired
	private com.cccollector.universal.app.service.AppService app_appService;
	
	@Override
	public List<com.cccollector.universal.app.model.App> loadNotUsedAppManagementApps() {
		// 加载全部应用管理平台应用
		List<com.cccollector.universal.app.model.App> allAppManagementApps = app_appService.loadAllApps();
		
		// 加载全部应用
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("name", true));
		List<App> allApps = appDao.loadAll(null, orderQueryFieldList);
		
		// 如果没有应用，则返回全部应用管理平台应用
		if (allApps.size() == 0) {
			return allAppManagementApps;
		}
		
		// 排除已使用应用管理平台应用
		List<com.cccollector.universal.app.model.App> notUsedAppManagementApps = new ArrayList<com.cccollector.universal.app.model.App>();
		int i = 0;
		for (com.cccollector.universal.app.model.App appManagementApp : allAppManagementApps) {
			if (i < allApps.size() && appManagementApp.getAppId() == allApps.get(i).getAppId()) {
				i++;
			} else {
				notUsedAppManagementApps.add(appManagementApp);
			}
		}
		return notUsedAppManagementApps;
	}

	@Override
	public void addApp(int appId) {
		// 根据应用ID从应用管理平台获取应用
		com.cccollector.universal.app.model.App appManagementApp = app_appService.loadAppByAppId(appId);
		
		// 添加应用
		App app = new App();
		app.setAppId(appManagementApp.getAppId());
		app.setName(appManagementApp.getName());
		app.setBundleId(appManagementApp.getBundleId());
		app.setEnabled(appManagementApp.getEnabled());
		app.setSyncTime(new Date());
		appDao.save(app);
	}

	@Override
	public boolean syncApp(App app) {
		// 根据应用ID从应用管理平台获取应用
		com.cccollector.universal.app.model.App appManagementApp = app_appService.loadAppWithEditionsAndReleasesByAppId(app.getAppId());
		if (appManagementApp == null) {
			return false;
		}

		// 根据应用ID删除发行列表		
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("edition", new QueryField("app", new QueryField("appId", app.getAppId()))));
		List<Release> releaseList = releaseDao.loadAll(predicateQueryFieldList, null);
		releaseDao.deleteAll(releaseList);

		// 根据应用ID删除版本列表
		predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("app", new QueryField("appId", app.getAppId())));
		List<Edition> editionList = editionDao.loadAll(predicateQueryFieldList, null);
		editionDao.deleteAll(editionList);

		// 更新应用
		App appNew = appDao.loadById(app.getAppId());
		appNew.setName(appManagementApp.getName());
		appNew.setBundleId(appManagementApp.getBundleId());
		appNew.setEnabled(appManagementApp.getEnabled());
		appNew.setSyncTime(new Date());
		
		// 添加版本
		for (com.cccollector.universal.app.model.Edition appManagementEdition : appManagementApp.getEditions()) {	
			Edition edition = new Edition();
			edition.setEditionId(appManagementEdition.getEditionId());
			edition.setBundleId(appManagementEdition.getBundleId());
			edition.setName(appManagementEdition.getName());
			edition.setOsType(appManagementEdition.getOsType());
			edition.setEnabled(appManagementEdition.getEnabled());
			edition.setApp(appNew);
			editionDao.save(edition);
			
			// 添加发行
			for (com.cccollector.universal.app.model.Release appManagementRelease : appManagementEdition.getReleases()) {
				Release release = new Release();
				release.setReleaseId(appManagementRelease.getReleaseId());
				release.setVersion(appManagementRelease.getVersion());
				release.setOsVersion(appManagementRelease.getOsVersion());
				release.setReleaseDate(appManagementRelease.getReleaseDate());
				release.setStatus(appManagementRelease.getStatus());
				release.setEdition(edition);
				releaseDao.save(release);
			}		
		}
		return true;
	}

	@Override
	public void deleteApp(App app) {
		// 根据应用ID删除发行列表		
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("edition", new QueryField("app", new QueryField("appId", app.getAppId()))));
		List<Release> releaseList = releaseDao.loadAll(predicateQueryFieldList, null);
		releaseDao.deleteAll(releaseList);

		// 根据应用ID删除版本列表
		predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("app", new QueryField("appId", app.getAppId())));
		List<Edition> editionList = editionDao.loadAll(predicateQueryFieldList, null);
		editionDao.deleteAll(editionList);
		
		// 删除应用
		appDao.deleteById(app.getAppId());
	}

	@Override
	public void addAppToNewsSource(int appId, int newsSourceId) {
		App app = appDao.loadById(appId, "newsSources");
		NewsSource newsSource = newsSourceDao.loadById(newsSourceId);
		app.getNewsSources().add(newsSource);
	}

	@Override
	public void deleteAppFromNewsSource(int appId, int newsSourceId) {
		App app = appDao.loadById(appId, "newsSources");
		for (NewsSource newsSource : app.getNewsSources()) {
			if (newsSource.getNewsSourceId() == newsSourceId) {
				// 从需要删除的应用中去掉新闻源，并将需要删除的应用中剩余的新闻源保存至newsSourceList
				app.getNewsSources().remove(newsSource);
				List<NewsSource> newsSourceList = new ArrayList<NewsSource>(app.getNewsSources());
				// 清除需要删除的应用包含的所有新闻源，并同步数据库
				app.getNewsSources().clear();
				appDao.getEntityManager().flush();
				// 将newsSourceList中保存的新闻源添加至需要删除的应用
				app.getNewsSources().addAll(newsSourceList);
				return;
			}
		}
	}
}
