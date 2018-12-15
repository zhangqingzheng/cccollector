/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.news.model.SiteVersion;
import com.cccollector.news.model.Template;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericEditBean;

/**
 * 模板映射Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class TemplateMappingBean extends BaseEditBean<TemplateMapping> implements Serializable {

	private static final long serialVersionUID = 6705116727637779060L;

	public TemplateMappingBean () {
		genericEditBeanHandler = new GenericEditBean.GenericEditBeanHandlerModelRelated<TemplateMapping>() {

			@Override
			public TemplateMapping loadModel(Integer modelId) {
				TemplateMapping templateMapping = null;
				if (modelId == null) {
					templateMapping = new TemplateMapping();
					templateMapping.setType(TemplateMapping.TypeEnum.栏目.ordinal());
					templateMapping.setSourceTemplateType(TemplateMapping.ColumnSourceTemplateTypeEnum.子栏目列表.ordinal());
					templateMapping.setTargetTemplateType(TemplateMapping.ColumnTargetTemplateTypeEnum.子栏目列表.ordinal());
					templateMapping.setTemplate(new Template());
					templateMapping.setSiteVersion(getSiteVersion());
				} else {
					templateMapping = templateMappingService.loadById(modelId, "template");
				}
				return templateMapping;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return siteVersionService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 模板映射
	 */
	public TemplateMapping getTemplateMapping() {
		return getModel();
	}


	/**
	 * 所属站点版本
	 */
	public SiteVersion getSiteVersion() {
		return (SiteVersion) relatedModel(1);
	}
	
	/**
	 * 改变类别
	 */
	public void changeTypeAction() {
		TemplateMapping templateMapping = getTemplateMapping();
		templateMapping.setSourceTemplateType(0);
		templateMapping.setTargetTemplateType(0);
	}
	
	/**
	 * 全部模板
	 */
	public List<Template> getAllTemplates() {
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("enabled", true));
		predicateQueryFieldList.add(new QueryField("type", getTemplateMapping().getType()));
		predicateQueryFieldList.add(new QueryField("subtype", getTemplateMapping().getTargetTemplateType()));
		predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("siteVersionId", getTemplateMapping().getSiteVersion().getSiteVersionId())));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("position", true));
		return templateService.loadAll(predicateQueryFieldList, orderQueryFieldList);
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		TemplateMapping templateMapping = getTemplateMapping();
		if (templateMapping.getTemplateMappingId() == null) {
			// 添加模板映射
			boolean flag = templateMappingService.addTemplateMapping(templateMapping);
			if (!flag) {
				addErrorMessageToFlash("请重新添加!");
				return;
			}
		} else {
			// 编辑模板映射
			boolean flag = templateMappingService.updateTemplateMapping(templateMapping);
			if (!flag) {
				addErrorMessageToFlash("请重新编辑!");
				return;
			}
		}	
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("templateMappingList.xhtml?multiSelect=true");
		}
	}
}
