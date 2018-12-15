/**
 * 
 */
package com.cccollector.news.service;

import com.cccollector.news.model.StaticPage;
import com.cccollector.universal.service.GenericService;

/**
 * 静态化页面服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface StaticPageService extends GenericService<Integer, StaticPage> {
	
	/**
	 * 添加静态化记录信息
	 * @param contentType 内容类别
	 * @param contentId 内容ID
	 * @param filePath 文件路径
	 * @param siteVersionId 站点版本ID
	 * @param templateVersionId 模板版本ID
	 * 
	 */
	public void addStaticPage(int contentType, int contentId, String filePath, int siteVersionId, int templateVersionId);
}
