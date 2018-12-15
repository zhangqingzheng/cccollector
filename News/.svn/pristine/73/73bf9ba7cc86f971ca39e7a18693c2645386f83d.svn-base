/**
 * 
 */
package com.cccollector.news.service;

import com.cccollector.news.model.TemplateVersion;
import com.cccollector.universal.service.GenericService;

/**
 * 模板版本服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface TemplateVersionService extends GenericService<Integer, TemplateVersion> {
	
	/**
	 * 获取数据目录
	 */
	public String getDataPath();
	
	/**
	 * 获取临时目录
	 */
	public String getTempPath();
	
	/**
	 * 获取发布目录
	 */
	public String getPublishPath();
	
	/**
	 * 添加模板版本
	 * 
	 * @param templateVersion 待添加的模板版本
	 * @param filePath 文件路径
	 */
	public void addTemplateVersion(TemplateVersion templateVersion, String filePath);
	
	/**
	 * 更新模板版本
	 * 
	 * @param templateVersion 待更新的模板版本
	 * @param templateContent 模板内容
	 * @param filePath 文件路径
	 */
	public void updateTemplateVersion(TemplateVersion templateVersion, String templateContent, String filePath);
	
	/**
	 * 删除模板版本
	 * 
	 * @param templateVersion 待删除的模板版本
	 */
	public void deleteTemplateVersion(TemplateVersion templateVersion);
	
	/**
	 * 测试模板版本
	 * 
	 * @param templateVersion 待测试的模板版本
	 */
	public void testTemplateVersion(TemplateVersion templateVersion);
	
	/**
	 * 发布模板版本
	 * 
	 * @param templateVersion 待发布的模板版本
	 */
	public void publishTemplateVersion(TemplateVersion templateVersion);
}
