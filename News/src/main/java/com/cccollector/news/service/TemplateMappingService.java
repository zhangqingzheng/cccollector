/**
 * 
 */
package com.cccollector.news.service;

import java.util.List;

import com.cccollector.news.model.TemplateMapping;
import com.cccollector.universal.service.GenericService;

/**
 * 模板映射服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface TemplateMappingService extends GenericService<Integer, TemplateMapping> {

	/**
	 * 添加模板映射
	 * 
	 * @param templateMapping 待添加的模板映射
	 */
	public boolean addTemplateMapping(TemplateMapping templateMapping);

	/**
	 * 更新模板映射
	 * 
	 * @param templateMapping 待更新的模板映射
	 */
	public boolean updateTemplateMapping(TemplateMapping templateMapping);

	/**
	 * 查询相同类别、源模版类别、目标模版类别、内容ID并且可用的模板映射
	 * 
	 * @param templateMapping 模板映射
	 */
	public List<TemplateMapping> findTemplateMappings(TemplateMapping templateMapping);

	/**
	 * 根据站点版本ID查找可用的模板映射
	 * 
	 * @param siteVersionId 站点版本ID
	 */
	public List<TemplateMapping> findTemplateMappingsBySiteVersionId(int siteVersionId);

}
