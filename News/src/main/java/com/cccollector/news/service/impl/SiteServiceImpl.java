/**
 * 
 */
package com.cccollector.news.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.NewsSourceDao;
import com.cccollector.news.dao.SiteDao;
import com.cccollector.news.dao.SiteVersionDao;
import com.cccollector.news.dao.TemplateDao;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.Site;
import com.cccollector.news.model.SiteVersion;
import com.cccollector.news.model.Template;
import com.cccollector.news.service.SiteService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 站点服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("siteService")
public class SiteServiceImpl extends GenericServiceHibernateImpl<Integer, Site> implements SiteService {
	
	@Autowired
	private SiteDao siteDao;
	
	@Autowired
	private NewsSourceDao newsSourceDao;	
	
	@Autowired
	private SiteVersionDao siteVersionDao;
	
	@Autowired
	private TemplateDao templateDao;
	
	@Override
	public void addSite(Site site) {
		// 设置排序位置
		Integer position = siteDao.max("position", null);
		site.setPosition(position == null ? 0 : position.intValue() + 1);
		
		// 保存站点
		siteDao.save(site);		
	}
	
	@Override
	public void deleteSite(Site site) {
		// 根据站点ID删除模版版本	
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("site", new QueryField("siteId", site.getSiteId()))));
		List<Template> templateList = templateDao.loadAll(predicateQueryFieldList, null);
		templateDao.deleteAll(templateList);

		// 根据站点ID删除站点版本列表
		predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("site", new QueryField("siteId", site.getSiteId())));
		List<SiteVersion> siteVersionList = siteVersionDao.loadAll(predicateQueryFieldList, null);
		siteVersionDao.deleteAll(siteVersionList);
		
		// 删除站点
		siteDao.deleteById(site.getSiteId());
	}
	
	@Override
	public void addSiteToNewsSource(int siteId, int newsSourceId) {
		Site site = siteDao.loadById(siteId, "newsSources");
		NewsSource newsSource = newsSourceDao.loadById(newsSourceId);
		site.getNewsSources().add(newsSource);
	}

	@Override
	public void deleteSiteFromNewsSource(int siteId, int newsSourceId) {
		Site site = siteDao.loadById(siteId, "newsSources");
		for (NewsSource newsSource : site.getNewsSources()) {
			if (newsSource.getNewsSourceId() == newsSourceId) {
				// 从需要删除的站点中去掉新闻源，并将需要删除的站点中剩余的新闻源保存至newsSourceList
				site.getNewsSources().remove(newsSource);
				List<NewsSource> newsSourceList = new ArrayList<NewsSource>(site.getNewsSources());
				// 清除需要删除的站点包含的所有新闻源，并同步数据库
				site.getNewsSources().clear();
				siteDao.getEntityManager().flush();
				// 将newsSourceList中保存的新闻源添加至需要删除的站点
				site.getNewsSources().addAll(newsSourceList);
				return;
			}
		}
	}
}
