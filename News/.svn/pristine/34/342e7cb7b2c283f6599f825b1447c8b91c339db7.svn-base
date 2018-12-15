/**
 * 
 */
package com.cccollector.news.service;

import com.cccollector.news.model.WebPage;
import com.cccollector.universal.service.GenericService;

/**
 * 网络页面服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface WebPageService extends GenericService<Integer, WebPage> {
	
	/**
	 * 获取临时目录
	 */
	public String getTempPath();
	
	/**
	 * 获取数据目录
	 */
	public String getDataPath();
	
	/**
	 * 获取发布目录
	 */
	public String getPublishPath();
	
	/**
	 * 添加网络页面
	 * 
	 * @param webPage 待添加的网络页面
	 * @param webPageTempFilePath 上传临时文件路径
	 */
	public void addWebPage(WebPage webPage, String webPageTempFilePath);
	
	/**
	 * 更新网络页面
	 * 
	 * @param webPage 待更新的网络页面
	 * @param webPageTempFilePath 上传临时文件路径
	 */
	public void updateWebPage(WebPage webPage, String webPageTempFilePath);
	
	/**
	 * 删除网络页面
	 * 
	 * @param webPage 待删除的网络页面
	 */
	public void deleteWebPage(WebPage webPage);
	
	/**
	 * 发布网络页面
	 * 
	 * @param webPage 待发布的网络页面
	 */
	public void publishWebPage(WebPage webPage);
	
	/**
	 * 撤销发布网络页面
	 * 
	 * @param webPage 待撤销的网络页面
	 */
	public void cancelPublishWebPage(WebPage webPage);
}
