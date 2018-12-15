/**
 * 
 */
package com.cccollector.news.service;

import java.util.List;

import com.cccollector.news.model.NewsSource;
import com.cccollector.universal.service.GenericService;

/**
 * 新闻源服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface NewsSourceService extends GenericService<Integer, NewsSource> {
	
	/**
	 * 添加新闻源
	 * 
	 * @param newsSource 待添加的新闻源
	 */
	public void addNewsSource(NewsSource newsSource);
	
	/**
	 * 移动新闻源
	 * 
	 * @param newsSources 移动后的新闻源
	 */
	public void moveNewsSources(List<NewsSource> newsSources);

	/**
	 * 添加新闻源到应用
	 * 
	 * @param newsSourceId 新闻源ID
	 * @param appId 应用ID
	 */
	public void addNewsSourceToApp(int newsSourceId, int appId);
	
	/**
	 * 在应用中移动新闻源
	 * 
	 * @param newsSources 移动后的新闻源
	 * @param appId 应用ID
	 */
	public void moveNewsSourcesInApp(List<NewsSource> newsSources, int appId);
	
	/**
	 * 从应用删除新闻源
	 * 
	 * @param newsSourceId 新闻源ID
	 * @param appId 应用ID
	 */
	public void deleteNewsSourceFromApp(int newsSourceId, int appId);
	
	/**
	 * 添加新闻源到站点
	 * 
	 * @param newsSourceId 新闻源ID
	 * @param siteId 站点ID
	 */
	public void addNewsSourceToSite(int newsSourceId, int siteId);
	
	/**
	 * 在站点中移动新闻源
	 * 
	 * @param newsSources 移动后的新闻源
	 * @param siteId 站点ID
	 */
	public void moveNewsSourcesInSite(List<NewsSource> newsSources, int siteId);
	
	/**
	 * 从站点删除新闻源
	 * 
	 * @param newsSourceId 新闻源ID
	 * @param siteId 站点ID
	 */
	public void deleteNewsSourceFromSite(int newsSourceId, int siteId);
}
