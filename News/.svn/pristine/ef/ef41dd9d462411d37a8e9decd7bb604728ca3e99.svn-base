/**
 * 
 */
package com.cccollector.news.service;

import com.cccollector.news.model.SiteVersion;
import com.cccollector.universal.service.GenericService;

/**
 * 站点版本服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface SiteVersionService extends GenericService<Integer, SiteVersion> {
	
	/**
	 * 整站静态化
	 * 
	 * @param 站点版本
	 */
	public void staticPageAction(int siteVersionId);
	
	/**
	 * 整站栏目静态化
	 * 
	 * @param 站点版本
	 */
	public void columnStaticPageAction(int siteVersionId);
	
	/**
	 * 整站文章静态化
	 * 
	 * @param 站点版本
	 */
	public void articleStaticPageAction(SiteVersion siteVersion);
	
	/**
	 * 整站分类标签静态化
	 * 
	 * @param 站点版本
	 */
	public void categoryTagStaticPageAction(SiteVersion siteVersion);
	
	/**
	 * 添加站点版本
	 * 
	 * @param siteVersion 待添加的站点版本
	 */
	public void addSiteVersion(SiteVersion siteVersion);
	
	/**
	 * 测试站点版本
	 * 
	 * @param siteVersion 待测试的站点版本
	 */
	public void testSiteVersion(SiteVersion siteVersion);
	
	/**
	 * 发布站点版本
	 * 
	 * @param siteVersion 待发布的站点版本
	 */
	public void publishSiteVersion(SiteVersion siteVersion);
	
	/**
	 * 取消待撤销站点版本
	 * 
	 * @param siteVersion 待取消待撤销的站点版本
	 */
	public void cancelRevokingSiteVersion(SiteVersion siteVersion);
	
	/**
	 * 撤销待撤销站点版本
	 * 
	 * @param siteVersion 待撤销待撤销的站点版本
	 */
	public void revokeSiteVersion(SiteVersion siteVersion);
}
