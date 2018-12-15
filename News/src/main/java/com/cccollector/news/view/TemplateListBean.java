/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.ReorderEvent;
import org.springframework.cache.annotation.CacheEvict;

import com.cccollector.news.model.SiteVersion;
import com.cccollector.news.model.Template;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 模板列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class TemplateListBean extends BaseListBean<Template> implements Serializable {

	private static final long serialVersionUID = -1873491076698067458L;

	public TemplateListBean() {
		modelDisplayName = "模板";
		modelAttributeName = "template";
		modelIdAttributeName = "templateId";
		submodelAttributeName = "templateVersion";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Template>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return siteVersionService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Template> loadAllModelList() {
				if (getSiteVersion() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("siteVersionId", getSiteVersion().getSiteVersionId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("position", true));
				return templateService.loadAll(predicateQueryFieldList, orderQueryFieldList);
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
	 * 批量修改模板是否可用
	 */
	public void modifyTemplatesEnabledAction(boolean enabled) {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个模板进行修改！");
			return;
		}

		// 修改模板是否可用
		for (Template template : getSelectedModels()) {
			template.setEnabled(enabled);
			templateService.update(template, "enabled");
		}
		addInfoMessage("修改模板是否可用成功！");
		
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 移动模板
	 */
	public void moveTemplateAction(ReorderEvent event) {
		// 移动模板
		Template templateTo = allModels.get(event.getToIndex());
		// 如果所选模板不可用，则无法移动
		if (!templateTo.getEnabled()) {
			addErrorMessage("请选择可用的模板进行移动！");
			return;
		}
		// 移动模板
		templateService.moveTemplates(allModels);
		addInfoMessage("移动模板成功！");

		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 删除模板
	 */
	@CacheEvict("templateMappings")
	public void deleteTemplateAction(Template template) {
		// 模板如果否包含子对象，则不能被删除
		if (templateService.hasChildren(template)) {
			addErrorMessage("要删除的模板中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 删除模板
		templateService.delete(template);
		addInfoMessage("删除模板成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除模板
	 */
	@CacheEvict("templateMappings")
	public void deleteTemplatesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个模板进行删除！");
			return;
		}

		// 模板如果否包含子对象，则不能被删除
		for (Template template : getSelectedModels()) {
			if (templateService.hasChildren(template)) {
				addErrorMessage("要删除的模板中包含子对象，请清空子对象再进行删除！");
				return;
			}
		}

		// 删除模板
		templateService.deleteAll(getSelectedModels());
		addInfoMessage("删除模板成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
