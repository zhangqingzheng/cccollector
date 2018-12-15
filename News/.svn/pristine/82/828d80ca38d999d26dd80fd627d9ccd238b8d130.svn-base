/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.news.model.Template;
import com.cccollector.news.model.TemplateVersion;
import com.cccollector.universal.dao.QueryField;

/**
 * 模板版本列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class TemplateVersionListBean extends BaseListBean<TemplateVersion> implements Serializable {

	private static final long serialVersionUID = 4741716472929384654L;

	public TemplateVersionListBean () {
		modelDisplayName = "模板版本";
		modelAttributeName = "templateVersion";
		modelIdAttributeName = "templateVersionId";
		useDialog = false;
		genericListBeanHandler = new GenericListBeanHandlerRelatedAll<TemplateVersion>() {

			@Override
			public List<TemplateVersion> loadAllModelList() {
				if (getTemplate() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("template", new QueryField("templateId", getTemplate().getTemplateId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("version", true));
				
				return templateVersionService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return templateService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 模板
	 */
	public Template getTemplate() {
		return (Template) relatedModel(1);
	}
	
	/**
	 * 发布模板版本
	 */
	public void publishTemplateVersionAction(TemplateVersion templateVersion) {
		if (templateVersion.getStatusEnum() != TemplateVersion.StatusEnum.测试中) {
			return;
		}
		templateVersionService.publishTemplateVersion(templateVersion);
		addInfoMessage("发布模板版本成功！");		
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 取消撤销模板版本
	 */
	public void unCancelTemplateVersionAction(TemplateVersion templateVersion) {
		if (templateVersion.getStatus() != TemplateVersion.StatusEnum.已撤销.ordinal()) {
			return;
		}
		
		// 更改模板版本状态为制作中
		templateVersion.setStatus(TemplateVersion.StatusEnum.制作中.ordinal());
		templateVersion.setUpdateTime(new Date());
		templateVersionService.update(templateVersion, "updateTime", "status");
		addInfoMessage("取消撤销模板版本成功！");		

		// 刷新全部模型
		allModels = null;
	}	
	
	/**
	 * 删除模板版本
	 */
	public void deleteTemplateVersionAction(TemplateVersion templateVersion) {
		// 模板版本如果不是制作中，则不能被删除
		if (templateVersion.getStatusEnum() != TemplateVersion.StatusEnum.制作中) {
			return;
		}
		// 删除模板版本
		templateVersionService.deleteTemplateVersion(templateVersion);
		addInfoMessage("删除模板版本成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 批量删除模板版本
	 */
	public void deleteTemplateVersionsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个模板版本进行删除！");
			return;
		}
		for (TemplateVersion templateVersion : getSelectedModels()) {
			// 模板版本如果不是制作中，则不能被删除
			if (templateVersion.getStatusEnum() != TemplateVersion.StatusEnum.制作中) {
				addErrorMessage("只可删除状态为制作中的模板版本！");
				return;
			}
		}
		
		// 删除模板版本文件和目录
		for (TemplateVersion templateVersion : getSelectedModels()) {
			// 删除模板版本
			templateVersionService.deleteTemplateVersion(templateVersion);
		}		
		addInfoMessage("删除模板版本成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 测试模板对话框
	 */
	public void testTemplateAction(TemplateVersion templateVersion) {
		
		// 对话框选项
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", false);
		options.put("width", 1000);
		options.put("height", 700);
		options.put("contentWidth", "100%");
		options.put("contentHeight", "100%");
		options.put("includeViewParams", true);

		// 传递的参数
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		List<String> param = new ArrayList<String>();
		param.add(templateVersion.getTemplateVersionId().toString());
		params.put("templateVersionId", param);
		
		// 显示添加对话框
		PrimeFaces.current().dialog().openDynamic("validateTemplate", options, params);
	}
}
