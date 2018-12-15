/**
 * 
 */
package com.cccollector.news.service;

import java.util.List;

import com.cccollector.news.model.App;
import com.cccollector.universal.service.GenericService;

/**
 * 应用服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface AppService extends GenericService<Integer, App> {

	/**
	 * 加载未使用的应用管理平台应用列表
	 * 
	 * @return 未使用的应用管理平台应用列表
	 */
	public List<com.cccollector.universal.app.model.App> loadNotUsedAppManagementApps();
	
	/**
	 * 添加应用
	 * 
	 * @param appId 待添加的应用ID
	 */
	public void addApp(int appId);

	/**
	 * 同步应用
	 * 
	 * @param app 待同步的应用
	 * @return 是否成功
	 */
	public boolean syncApp(App app);

	/**
	 * 删除应用
	 * 
	 * @param app 待删除的应用
	 */
	public void deleteApp(App app);

	/**
	 * 添加应用到新闻源
	 * 
	 * @param appId 应用ID
	 * @param newsSourceId 新闻源ID
	 */
	public void addAppToNewsSource(int appId, int newsSourceId);

	/**
	 * 从新闻源删除应用
	 * 
	 * @param appId 应用ID
	 * @param newsSourceId 新闻源ID
	 */
	public void deleteAppFromNewsSource(int appId, int newsSourceId);
}
