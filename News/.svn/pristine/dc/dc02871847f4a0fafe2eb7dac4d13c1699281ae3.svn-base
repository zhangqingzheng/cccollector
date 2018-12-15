/**
 * 
 */
package com.cccollector.news.api.impl;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.api.SiteApi;
import com.cccollector.news.dao.SiteDao;
import com.cccollector.news.model.Site;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.news.service.TemplateService;
import com.cccollector.news.templatemodel.TemplateBaseContent;
import com.cccollector.news.util.TemplateUtil;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 网站API实现类
 *
 * @author 杨彪 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("siteApi")
public class SiteApiImpl extends GenericServiceHibernateImpl<Integer, Site> implements SiteApi {

	@SuppressWarnings("unused")
	@Autowired
	private SiteDao siteDao;

	@Autowired
	private TemplateService templateService;

	@Override
	public Response columnDistributions(int siteVersionId, int columnId, int pageNo) {

		TemplateBaseContent content = templateService.loadStaticData(columnId, TemplateMapping.TypeEnum.栏目.ordinal(), siteVersionId, pageNo);

		String html = TemplateUtil.content2Html(content);

		return Response.ok().header("Access-Control-Allow-Origin", "*").header("Content-Type", "application/javascript").entity(html).build();
	}

	@Override
	public Response categoryTagDistributions(int siteVersionId, int categoryTagId, int pageNo) {

		TemplateBaseContent content = templateService.loadStaticData(categoryTagId, TemplateMapping.TypeEnum.其它.ordinal(), siteVersionId, pageNo);

		String html = TemplateUtil.content2Html(content);

		return Response.ok().header("Access-Control-Allow-Origin", "*").header("Content-Type", "application/javascript").entity(html).build();
	}
}
