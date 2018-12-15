/**
 * 
 */
package com.cccollector.news.service;

import com.cccollector.news.model.Site;
import com.cccollector.universal.service.GenericService;

/**
 * 站点服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface SiteService extends GenericService<Integer, Site> {
	
	/**
	 * 添加站点
	 * 
	 * @param site 待添加站点
	 */
	public void addSite(Site site);
	
	/**
	 * 删除站点
	 * 
	 * @param site 待删除的站点
	 */
	public void deleteSite(Site site);
	
	/**
	 * 添加站点到新闻源
	 * 
	 * @param siteId 站点ID
	 * @param newsSourceId 新闻源ID
	 */
	public void addSiteToNewsSource(int siteId, int newsSourceId);

	/**
	 * 从新闻源删除站点
	 * 
	 * @param siteId 站点ID
	 * @param newsSourceId 新闻源ID
	 */
	public void deleteSiteFromNewsSource(int siteId, int newsSourceId);
	
}
