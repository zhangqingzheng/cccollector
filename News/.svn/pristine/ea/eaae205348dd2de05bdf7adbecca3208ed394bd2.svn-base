/**
 * 
 */
package com.cccollector.news.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.news.model.CategoryTag;
import com.cccollector.news.model.Column;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.Template;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.news.model.TemplateVersion;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
/**
 * 模板版本Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class TemplateVersionBean extends BaseEditBean<TemplateVersion> implements Serializable {

	private static final long serialVersionUID = -4048459975694392714L;
	
	public TemplateVersionBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<TemplateVersion>() {
			
			@Override
			public TemplateVersion loadModel(Integer modelId) {
				TemplateVersion templateVersion = null;
				if (modelId == null) {
					templateVersion = new TemplateVersion();
					templateVersion.setTemplate(getTemplate());
					templateVersion.setStatus(TemplateVersion.StatusEnum.制作中.ordinal());
				} else {
					templateVersion = templateVersionService.loadById(modelId);
					
					// 处理文件
					loadFile(1, templateVersionService.getDataPath() + templateVersion.templateVersionFilePath());
				}
				return templateVersion;
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
	 * 模板版本
	 */
	public TemplateVersion getTemplateVersion() {
		return getModel();
	}
	
	/**
	 * 所属模板
	 */
	public Template getTemplate() {
		return (Template) relatedModel(1);
	}
	
	/**
	 * 模板内容
	 */
	public String templateContent;
	
	public String getTemplateContent() {
		try (FileInputStream fileInputStream = new FileInputStream(filePath(1));) {
			int size = fileInputStream.available();
			byte[] buffer = new byte[size];
			fileInputStream.read(buffer);
			templateContent = new String(buffer, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return templateContent;
	}

	public void setTemplateContent(String _templateContent) {
		templateContent = _templateContent;
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		TemplateVersion templateVersion = getTemplateVersion(); 
		// 文件必填
		if (filePath(1) == null) {
			addValidatingMessage("文件为必填项");
			return;
		}
		
		if (templateVersion.getTemplateVersionId() == null) {	
			templateVersionService.addTemplateVersion(templateVersion, filePath(1));
			addInfoMessageToFlash("添加模板版本成功！");
		} else {
			// 编辑模板版本
			templateVersionService.updateTemplateVersion(templateVersion, templateContent, filePath(1));
			addInfoMessageToFlash("编辑模板版本成功！");
		}
		handleNavigation("templateVersionList.xhtml?multiSelect=true");
	}

	/**
	 * 取消
	 */
	public void cancelAction() {
		handleNavigation("templateVersionList.xhtml?multiSelect=true");
	}

	/**
	 * 测试
	 */
	public void testAction() {
		templateVersionService.testTemplateVersion(getTemplateVersion());
		addInfoMessage("测试模板版本成功！");
	}

	/**
	 * 撤销测试
	 */
	public void untestAction() {
		TemplateVersion templateVersion = getTemplateVersion();
		if (templateVersion.getStatus() == TemplateVersion.StatusEnum.测试中.ordinal()) {
			// 设置状态
			templateVersion.setStatus(TemplateVersion.StatusEnum.制作中.ordinal());
			templateVersion.setUpdateTime(new Date());

			// 更新状态
			templateVersionService.update(templateVersion, "updateTime", "status");					
			addInfoMessage("撤销测试模板版本成功！");
		}
	}
	
	/**
	 * 选中的内容ID
	 */
	private String selectedContentId;
	
	public String getSelectedContentId() {
		return selectedContentId;
	}
	
	public void setSelectedContentId(String _selectedContentId) {
		selectedContentId = _selectedContentId;
	}
	
	/**
	 * 栏目
	 */
	private List<Column> allColumns;
	
	public List<Column> getAllColumns() {
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("enabled", true));
		predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("sites", new QueryField("siteVersions", new QueryField("templates", new QueryField("templateId", getTemplate().getTemplateId()))))));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("code", true));
		List<Column> allColumnsTemp = columnService.loadAll(predicateQueryFieldList, orderQueryFieldList);
		
		// 获取关联模板的模板映射
		predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("enabled", true));
		predicateQueryFieldList.add(new QueryField("template", new QueryField("templateId", getTemplate().getTemplateId())));
		List<TemplateMapping> templateMappings = templateMappingService.loadAll(predicateQueryFieldList, null);
		allColumns = new ArrayList<Column>();
		for (Column column : allColumnsTemp) {
			for (TemplateMapping templateMapping : templateMappings) {
				if (column.getTemplateType() == templateMapping.getSourceTemplateType()) {
					allColumns.add(column);
					break;
				}
			}
		}
		return allColumns;
	}
	
	/**
	 * 分发
	 */
	private List<Distribution> allDistributions;
	
	public List<Distribution> getAllDistributions() {
		if (allDistributions == null) {
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));  
			predicateQueryFieldList.add(new QueryField("column", new QueryField("newsSource", new QueryField("sites", new QueryField("siteVersions", new QueryField("templates", new QueryField("templateId", getTemplate().getTemplateId())))))));
			predicateQueryFieldList.add(new QueryField("bodyTemplateType", Distribution.BodyTemplateTypeEnum.图文混排.ordinal()));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("distributionId", false));
			allDistributions = distributionService.loadAll(predicateQueryFieldList, orderQueryFieldList);
		}
		return allDistributions;
	}
	
	/**
	 * 处理分发自动完成
	 */
	public List<Distribution> handleDistributionComplete(String query) {
		allDistributions = distributionService.loadDistributionsBySearchWord(query);
		return allDistributions;
	}

	/**
	 * 分类标签
	 */
	private List<CategoryTag> allCategoryTags;
	
	public List<CategoryTag> getAllCategoryTags() {
		if (allCategoryTags == null) {
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("enabled", true));
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("sites", new QueryField("siteVersions", new QueryField("templates", new QueryField("templateId", getTemplate().getTemplateId()))))));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("code", true));
			allCategoryTags = categoryTagService.loadAll(predicateQueryFieldList, orderQueryFieldList);
		}
		return allCategoryTags;
	}
}
