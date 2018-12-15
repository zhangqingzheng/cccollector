/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.springframework.cache.annotation.CacheEvict;

import com.cccollector.news.model.SiteVersion;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 模板映射列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class TemplateMappingListBean extends BaseListBean<TemplateMapping> implements Serializable {

	private static final long serialVersionUID = 1426668947229079594L;

	public TemplateMappingListBean() {
		modelDisplayName = "模板映射";
		modelAttributeName = "templateMapping";
		modelIdAttributeName = "templateMappingId";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<TemplateMapping>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return siteVersionService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<TemplateMapping> loadAllModelList() {
				if (getSiteVersion() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("siteVersionId", getSiteVersion().getSiteVersionId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("templateMappingId", true));
				orderQueryFieldList.add(new QueryField("enabled", true));
				return templateMappingService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}
		};
	}

	/**
	 * 所属站点版本
	 */
	public SiteVersion getSiteVersion() {
		return (SiteVersion) relatedModel(1);
	}
	
	/**
	 * 批量修改模板映射是否可用
	 */
	public void modifyTemplateMappingsEnabledAction(boolean enabled) {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个模板映射进行修改！");
			return;
		}

		// 修改模板映射是否可用
		for (TemplateMapping templateMapping : getSelectedModels()) {
			if (enabled == true) {
				if (templateMappingService.findTemplateMappings(templateMapping).size() > 0) {
					addErrorMessage("已存在重复可用模板映射, 请重新修改!");
					return;
				}
			}
			templateMapping.setEnabled(enabled);
			templateMappingService.update(templateMapping, "enabled");
		}
		addInfoMessage("修改模板映射是否可用成功！");
		
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 删除模板映射
	 */
	@CacheEvict("templateMappings")
	public void deleteTemplateMappingAction(TemplateMapping templateMapping) {
		// 删除模板映射
		templateMappingService.delete(templateMapping);
		addInfoMessage("删除模板映射成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除模板映射
	 */
	@CacheEvict("templateMappings")
	public void deleteTemplateMappingsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个模板映射进行删除！");
			return;
		}

		// 删除模板映射
		templateMappingService.deleteAll(getSelectedModels());
		addInfoMessage("删除模板映射成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
