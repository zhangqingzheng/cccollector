/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cccollector.news.model.NewsSource;
import com.cccollector.news.service.NewsSourceService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.TemplateBaseBean;

/**
 * 模板Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class TemplateBean extends TemplateBaseBean implements Serializable {

	private static final long serialVersionUID = -6371095344440070460L;

	/**
	 * 新闻源服务
	 */
	@ManagedProperty(value = "#{newsSourceService}")
	private NewsSourceService newsSourceService;

	public void setNewsSourceService(NewsSourceService _newsSourceService) {
		newsSourceService = _newsSourceService;
	}
	
	/**
	 * 全部新闻源
	 */
	private List<NewsSource> allNewsSources;

	public List<NewsSource> getAllNewsSources() {
		if (allNewsSources == null) {
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("position", true));
			orderQueryFieldList.add(new QueryField("name", true));
			allNewsSources = newsSourceService.loadAll(null, orderQueryFieldList);
		}
		return allNewsSources;
	}
}
