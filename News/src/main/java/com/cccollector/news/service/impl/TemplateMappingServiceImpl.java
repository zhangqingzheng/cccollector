/**
 * 
 */
package com.cccollector.news.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.TemplateMappingDao;
import com.cccollector.news.model.Template;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.news.model.TemplateVersion;
import com.cccollector.news.service.TemplateMappingService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 模板映射服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("templateMappingService")
public class TemplateMappingServiceImpl extends GenericServiceHibernateImpl<Integer, TemplateMapping> implements TemplateMappingService {

	@Autowired
	private TemplateMappingDao templateMappingDao;

	@Override
	@CacheEvict("templateMappings")
	public boolean addTemplateMapping(TemplateMapping templateMapping) {
		if (templateMapping.getEnabled() == true) {
			if (findTemplateMappings(templateMapping).size() > 0) {
				return false;
			}
		}
		templateMappingDao.save(templateMapping);
		return true;
	}

	@Override
	@CacheEvict("templateMappings")
	public boolean updateTemplateMapping(TemplateMapping templateMapping) {
		if (templateMapping.getEnabled() == true) {
			if (findTemplateMappings(templateMapping).size() > 0) {
				return false;
			}
		}
		templateMappingDao.update(templateMapping);
		return true;
	}

	@Override
	public List<TemplateMapping> findTemplateMappings(TemplateMapping templateMapping) {
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("siteVersionId", templateMapping.getSiteVersion().getSiteVersionId())));
		if (templateMapping.getContentId() == null) {
			predicateQueryFieldList.add(new QueryField("contentId", null, PredicateEnum.IS_NULL));
		} else {
			predicateQueryFieldList.add(new QueryField("contentId", templateMapping.getContentId()));
		}
		predicateQueryFieldList.add(new QueryField("enabled", true));
		predicateQueryFieldList.add(new QueryField("type", templateMapping.getType()));
		predicateQueryFieldList.add(new QueryField("sourceTemplateType", templateMapping.getSourceTemplateType()));
		predicateQueryFieldList.add(new QueryField("targetTemplateType", templateMapping.getTargetTemplateType()));
		return templateMappingDao.loadAll(predicateQueryFieldList, null);
	}

	@Override
	@Cacheable(value="templateMappings")
	public List<TemplateMapping> findTemplateMappingsBySiteVersionId(int siteVersionId) {

		// 查询模板映射
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();

		predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("siteVersionId", siteVersionId)));
		predicateQueryFieldList.add(new QueryField("enabled", true));
		predicateQueryFieldList.add(new QueryField("template", new QueryField("enabled", true)));
		List<TemplateMapping> templateMappings = templateMappingDao.loadAll(predicateQueryFieldList, null);

		for (TemplateMapping templateMapping : templateMappings) {

			Template template = templateMapping.getTemplate();

			List<TemplateVersion> templateVersions = template.getTemplateVersions();

			for (TemplateVersion templateVersion : templateVersions) {
				templateMappingDao.getEntityManager().detach(templateVersion);
			}

			templateMappingDao.getEntityManager().detach(template);
			templateMappingDao.getEntityManager().detach(templateMapping);
		}

		return templateMappings;

	}
}
