/**
 * 
 */
package com.cccollector.news.service;

import com.cccollector.news.model.Style;
import com.cccollector.universal.service.GenericService;

/**
 * 样式服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface StyleService extends GenericService<Integer, Style> {

	/**
	 * 获取发布目录
	 */
	public String getPublishPath();

	/**
	 * 获取样式CSS发布URL
	 */
	public String getCssPublishStylesUrl(Style style);	
	
	/**
	 * 更新样式CSS文件
	 * 
	 * @param style 待更新的样式
	 */
	public void updateStyleCss(Style style);
	
	/**
	 * 删除样式CSS文件
	 * 
	 * @param style 待删除的样式
	 */
	public void deleteStyleCss(Style style);
}
