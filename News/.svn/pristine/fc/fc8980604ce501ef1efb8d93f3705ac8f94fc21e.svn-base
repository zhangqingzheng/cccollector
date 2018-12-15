/**
 * 
 */
package com.cccollector.news.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.StaticPageDao;
import com.cccollector.news.dao.TemplateVersionDao;
import com.cccollector.news.model.StaticPage;
import com.cccollector.news.model.TemplateVersion;
import com.cccollector.news.service.SiteVersionService;
import com.cccollector.news.service.StaticPageService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 静态化页面服务实现类
 *
 * @author 谢朋 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("staticPageService")
public class StaticPageServiceImpl extends GenericServiceHibernateImpl<Integer, StaticPage> implements StaticPageService {

	@Autowired
	private StaticPageDao staticPageDao;
	@Autowired
	private TemplateVersionDao templateVersionDao;

	@Autowired
	private SiteVersionService siteVersionService;

	@Override
	public void addStaticPage(int contentType, int contentId, String filePath, int siteVersionId, int templateVersionId) {

		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("siteVersionId", siteVersionId)));
		predicateQueryFieldList.add(new QueryField("contentType", contentType));
		predicateQueryFieldList.add(new QueryField("contentId", contentId));

		TemplateVersion templateVersion = templateVersionDao.loadById(templateVersionId);

		List<StaticPage> staticPageWithSiteVersions = staticPageDao.loadAll(predicateQueryFieldList, null);
		if (staticPageWithSiteVersions != null && staticPageWithSiteVersions.size() > 0) {

			StaticPage staticPage = staticPageWithSiteVersions.get(0);
			staticPage.setStaticTimeDate(new Date());
			staticPage.setTemplateVersion(templateVersion);
			staticPage.setSiteVersion(siteVersionService.loadById(siteVersionId));

		} else {
			StaticPage staticPage = new StaticPage();
			staticPage.setContentType(contentType);
			staticPage.setContentId(contentId);
			staticPage.setFilePath(filePath);
			staticPage.setStaticTimeDate(new Date());
			staticPage.setTemplateVersion(templateVersion);
			staticPage.setSiteVersion(siteVersionService.loadById(siteVersionId));
			staticPageDao.save(staticPage);
		}

	}
}
