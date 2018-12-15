/**
 * 
 */
package com.cccollector.news.service;

import java.util.List;

import com.cccollector.news.model.Template;
import com.cccollector.news.templatemodel.TemplateBaseContent;
import com.cccollector.universal.service.GenericService;

/**
 * 模板服务接口
 *
 * @author 谢朋 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface TemplateService extends GenericService<Integer, Template> {

	/**
	 * 添加模板
	 * 
	 * @param template 待添加的模板
	 */
	public void addTemplate(Template template);
	
	/**
	 * 修改模板
	 * 
	 * @param template 待更新的模板
	 */
	public void updateTemplate(Template template);

	/**
	 * 移动模板
	 * 
	 * @param templates 移动后的模板
	 */
	public void moveTemplates(List<Template> templates);

	/**
	 * 加载需要静态化的数据
	 * @param contentId 内容ID
	 * @param templateMappingType 模板映射类别
	 * @param siteVersionId 站点版本ID
	 * @param pageNo 页码,当类型为分发的时候,页码为null
	 */
	public TemplateBaseContent loadStaticData(int contentId, int templateMappingType, int siteVersionId, Integer pageNo);

	/**
	 * 加载需要静态化的数据
	 * @param contentId 内容ID
	 * @param templateMappingType 模板映射类别
	 * @param siteVersionId 站点版本ID
	 * @param templateVersionId 指定的模板版本ID,值可以为null
	 * @param pageNo 页码,当类型为分发的时候,页码为null
	 */
	public TemplateBaseContent loadStaticData(int contentId, int templateMappingType, int siteVersionId, Integer templateVersionId, Integer pageNo);

	/**
	 * 根据内容Id,模板映射类别获取静态化页面外壳
	 * @param contentId 内容ID
	 * @param templateMappingType 模板映射类别
	 */
	public void staticPageCase(int contentId, int templateMappingType, Integer siteVersionId);
	
	/**
	 * 根据内容Id，模板映射类别,页码获取静态化页面Json数据
	 * @param contentId 内容ID
	 * @param templateMappingType 模板映射类别
	 */
	public void staticPageContent(int contentId, int templateMappingType, Integer siteVersionId);
}
