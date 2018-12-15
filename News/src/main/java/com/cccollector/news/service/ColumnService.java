/**
 * 
 */
package com.cccollector.news.service;

import java.util.List;

import com.cccollector.news.model.Column;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.templatemodel.TemplateColumn;
import com.cccollector.universal.service.GenericService;

/**
 * 栏目服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface ColumnService extends GenericService<Integer, Column> {

	/**
	 * 获取数据目录
	 */
	public String getDataPath();

	/**
	 * 获取栏目图标缩略图URL
	 */
	public String getIconThumbnailColumnsUrl(Column column);
	
	/**
	 * 添加栏目
	 * 
	 * @param column 待添加的栏目
	 * @param newsSource 所属的新闻源
	 * @param iconFilePath 栏目图标文件
	 */
	public void addColumn(Column column, NewsSource newsSource, String iconFilePath);
	
	/**
	 * 更新栏目
	 * 
	 * @param column 待更新的栏目
	 * @param iconFilePath 栏目图标文件
	 */
	public void updateColumn(Column column, String iconFilePath);
	
	/**
	 * 删除栏目
	 * 
	 * @param column 待删除的栏目
	 */
	public void deleteColumn(Column column);
	
	/**
	 * 加载所有栏目数据
	 * @param siteId 站点ID
	 * @param siteVersionId 站点版本Id
	 */
	public List<TemplateColumn> loadAllColumns(int siteId, int siteVersionId);
}
