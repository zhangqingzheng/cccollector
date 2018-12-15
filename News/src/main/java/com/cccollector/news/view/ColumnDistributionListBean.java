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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortOrder;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cccollector.news.model.Column;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.Site;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 栏目分发列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ColumnDistributionListBean extends BaseListBean<Distribution> implements Serializable {

	private static final long serialVersionUID = -6538832794576954861L;

	public ColumnDistributionListBean () {
		modelDisplayName = "分发";
		modelAttributeName = "distribution";
		modelIdAttributeName = "distributionId";
		submodelAttributeName = "distribution";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedPage<Distribution>() {
			
			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return newsSourceService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Distribution> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
				// 查询条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				if (selectedColumnId != null) {
					predicateQueryFieldList.add(new QueryField("column", new QueryField("columnId", selectedColumnId)));
				} else {
					predicateQueryFieldList.add(new QueryField("column", new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId()))));
				}
				predicateQueryFieldList.add(new QueryField("publishTime", null, getPublishedEnum() == PublishedEnum.已发布 ? QueryField.PredicateEnum.IS_NOT_NULL : QueryField.PredicateEnum.IS_NULL));
				predicateQueryFieldList.add(new QueryField("scheduledTime", null, getPublishedEnum() == PublishedEnum.定时发布 ? QueryField.PredicateEnum.IS_NOT_NULL : QueryField.PredicateEnum.IS_NULL));
				
				// 过滤条件
				for (String key : filters.keySet()) {
					Object value = filters.get(key);
					switch (key) {
					case "article.title":
						predicateQueryFieldList.add(new QueryField("article", new QueryField("title", value, QueryField.PredicateEnum.LIKE)));
						break;
					case "article.author":
						predicateQueryFieldList.add(new QueryField("article", new QueryField("author", value, QueryField.PredicateEnum.LIKE)));
						break;
					case "article.source":
						predicateQueryFieldList.add(new QueryField("article", new QueryField("source", value, QueryField.PredicateEnum.LIKE)));
						break;
					case "article.keywords":
						predicateQueryFieldList.add(new QueryField("article", new QueryField("keywords", value, QueryField.PredicateEnum.LIKE)));
						break;
					case "article.editor":
						predicateQueryFieldList.add(new QueryField("article", new QueryField("editor", value, QueryField.PredicateEnum.LIKE)));
						break;
					case "article.reviewer":
						predicateQueryFieldList.add(new QueryField("article", new QueryField("reviewer", value, QueryField.PredicateEnum.LIKE)));
						break;
					case "publisher":
						predicateQueryFieldList.add(new QueryField("publisher", value, QueryField.PredicateEnum.LIKE));
						break;
					}
				}

				// 排序
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				if (getPublishedEnum() == PublishedEnum.未发布) {
					orderQueryFieldList.add(new QueryField("validateTime", Boolean.FALSE));
					orderQueryFieldList.add(new QueryField("article", new QueryField("articleId", Boolean.FALSE)));
				} else if (getPublishedEnum() == PublishedEnum.已发布) {
					if (selectedColumnId != null) {
						orderQueryFieldList.add(new QueryField("position", Boolean.FALSE));
					}
					orderQueryFieldList.add(new QueryField("publishTime", Boolean.FALSE));
				} else if (getPublishedEnum() == PublishedEnum.定时发布) {
					orderQueryFieldList.add(new QueryField("scheduledTime", Boolean.FALSE));
				}
				orderQueryFieldList.add(new QueryField("distributionId", Boolean.FALSE));
				
				// 总行数
				totalRowCount.append(distributionService.count(predicateQueryFieldList).toString());
				
				List<Distribution> columnDistributions = distributionService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
				
				// 调整指定排序位置
				if (getPublishedEnum() == PublishedEnum.已发布 && selectedColumnId != null && columnDistributions.size() > 0) {
					while (columnDistributions.get(0).getPosition() != null && columnDistributions.get(0).getPosition() > 0) {
					    Distribution distribution = columnDistributions.get(0);
					    columnDistributions.remove(distribution);
					    columnDistributions.add(Math.min(distribution.getPosition(), columnDistributions.size()), distribution);
					}
				}
				return columnDistributions;
			}
		};
	}

	/**
	 * 所属新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(1);
	}

	/**
	 * 发布状态
	 */
	private Integer published;

	public Integer getPublished() {
		return published;
	}

	public void setPublished(Integer _published) {
		published = _published;
	}

	/**
	 * 发布状态枚举
	 */
	public static enum PublishedEnum {
		未发布,
		已发布,
		定时发布
	}

	/**
	 * 发布状态的枚举
	 */
	public PublishedEnum getPublishedEnum() {
		return PublishedEnum.values()[published];
	}

	/**
	 * 新闻源关联可用的站点集合
	 */
	private List<Site> allSites;

	public List<Site> getAllSites() {
		if (published != PublishedEnum.已发布.ordinal()) {
			return allSites;
		}
		if (allSites == null) {
			// 分发在已发布状态下
			List<QueryField> predicateQueryFieldList = null;
			if (getNewsSource() != null) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSources", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
			}
			predicateQueryFieldList.add(new QueryField("enabled", true));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("enabled", true));	
			orderQueryFieldList.add(new QueryField("position", true));	
			allSites = siteService.loadAll(predicateQueryFieldList, orderQueryFieldList);				
		}
		return allSites;
	}
	
	private Integer _siteId;

	/**
	 * 站点Id
	 */	
	public Integer getSiteId() {
		return _siteId;
	}

	public void setSiteId(Integer siteId) {
		_siteId= siteId;
	}

	private String webAddress;

	/**
	 * 网络地址
	 */
	public String getWebAddress() {
		if (_siteId != null) {
			Site site = siteService.loadById(_siteId);
			webAddress = site.getWebAddress();
		} else {			
			webAddress = allSites.get(0).getWebAddress();
		}			
		return webAddress;
	}

	/**
	 * 选择站点
	 */
	public void selectSiteAction() {
		// 刷新全部模型
		pageModel = null;
	}

	/**
	 * 全部栏目
	 */
	private List<Column> allColumns;
	
	public List<Column> getAllColumns() {
		if (allColumns == null) {
			List<QueryField> predicateQueryFieldList = null;
			if (getNewsSource() != null) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
			}
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("code", true));	
			allColumns = columnService.loadAll(predicateQueryFieldList, orderQueryFieldList);
		}
		return allColumns;
	}

	/**
	 * 选中的栏目ID
	 */
	private Integer selectedColumnId;
	
	public Integer getSelectedColumnId() {
		return selectedColumnId;
	}

	public void setSelectedColumnId(Integer _selectedColumnId) {
		selectedColumnId = _selectedColumnId;
	}
	
	/**
	 * 选择分发
	 */
	public void selectDistributionAction(SelectEvent event) {
		Distribution distribution = (Distribution) event.getObject();
		if (!getMultiSelect()) {
			handleNavigation("distributionList.xhtml?articleId=" + distribution.getArticle().getArticleId() + "&newsSourceId=" + getModelId1() + "&specifiedDistributionId=" + distribution.getDistributionId() + "&published=" + published + "&columnId=" + (selectedColumnId != null ? selectedColumnId.toString() : "null"));
		} else {
			handleNavigation("articleEdit.xhtml?articleId=" + distribution.getArticle().getArticleId() + "&newsSourceId=" + getModelId1() + "&published=" + published + "&columnId=" + (selectedColumnId != null ? selectedColumnId.toString() : "null"));
		}
	}
	
	/**
	 * 添加文章
	 */
	public void addArticleAction() {
		handleNavigation("articleEdit.xhtml?articleId=null");
	}
	
	/**
	 * 编辑文章
	 */
	public void editArticleAction(Distribution distribution) {
		handleNavigation("articleEdit.xhtml?articleId=" + distribution.getArticle().getArticleId() + "&newsSourceId=" + getModelId1() + "&published=" + published + "&columnId=" + (selectedColumnId != null ? selectedColumnId.toString() : "null"));
	}
	
	/**
	 * 编辑文章
	 */
	public void editArticleAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个文章进行编辑！");
			return;
		}

		if (getSelectedModels() != null && getSelectedModels().size() == 1) {
			handleNavigation("articleEdit.xhtml?articleId=" + getSelectedModels().get(0).getArticle().getArticleId() + "&newsSourceId=" + getModelId1() + "&published=" + published + "&columnId=" + (selectedColumnId != null ? selectedColumnId.toString() : "null"));
		} else {
			addErrorMessage("只能选择一个文章进行编辑！");
		}
	}
	
	/**
	 * 编辑分发
	 */
	public void editDistributionAction(Distribution distribution) {
		// 对话框选项
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", false);
		options.put("width", 600);
		options.put("height", 400);
		options.put("contentWidth", "100%");
		options.put("contentHeight", "100%");
		options.put("includeViewParams", true);
		
		// 传递的参数
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		List<String> param = new ArrayList<String>();
		param.add("true");
		params.put("useDialog", param);
		param = new ArrayList<String>();
		param.add(distribution.getDistributionId().toString());
		params.put("distributionId", param);
		
		// 显示编辑分发对话框
		PrimeFaces.current().dialog().openDynamic("distributionEdit", options, params);
	}
	
	/**
	 * 编辑分发
	 */
	public void editDistributionAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个分发进行编辑！");
			return;
		}

		if (getSelectedModels() != null && getSelectedModels().size() == 1) {
			// 对话框选项
			Map<String, Object> options = new HashMap<String, Object>();
			options.put("resizable", false);
			options.put("width", 600);
			options.put("height", 400);
			options.put("contentWidth", "100%");
			options.put("contentHeight", "100%");
			options.put("includeViewParams", true);
			
			// 传递的参数
			Map<String, List<String>> params = new HashMap<String, List<String>>();
			List<String> param = new ArrayList<String>();
			param.add("true");
			params.put("useDialog", param);
			param = new ArrayList<String>();
			param.add( getSelectedModels().get(0).getDistributionId().toString());
			params.put("distributionId", param);
			
			// 显示编辑分发对话框
			PrimeFaces.current().dialog().openDynamic("distributionEdit", options, params);
		} else {
			addErrorMessage("只能选择一个分发进行编辑！");
		}
	}
	
	/**
	 * 更新分发验证时间
	 */
	public void updateDistributionValidateTimeAction(Distribution distribution) {
        // 设置验证时间
		distribution.setValidateTimeDate(new Date());
		
		// 更新分发验证时间
		distributionService.update(distribution, "validateTime");
		addInfoMessage("更新验证时间成功！");
		
		// 刷新分页模型
		pageModel = null;
	}
	
	/**
	 * 批量更新分发验证时间
	 */
	public void updateDistributionsValidateTimeAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个分发进行更新验证时间！");
			return;
		}
		for (Distribution distribution : getSelectedModels()) {
			// 设置验证时间
			distribution.setValidateTimeDate(new Date());
			
			// 更新分发验证时间
			distributionService.update(distribution, "validateTime");
		}
		addInfoMessage("更新验证时间成功！");
		
		// 刷新分页模型
		pageModel = null;
	}

	/**
	 * 修改分发是否首发	
	 */
	public void modifyDistributionFirstPublishedAction(Distribution distribution) {
		// 只能修改已发布的分发
		if (!distribution.getPublished()) {
			distribution.setFirstPublished(false);
			addErrorMessage("只能修改已发布的分发为首发！");
			return;
		}
		
		// 不能单撤销首发
		if (distribution.getFirstPublished() == false) {
			distribution.setFirstPublished(true);
			addErrorMessage("每篇文章必须要有一个首发分发！");
			return;
		}
		
		// 查询全部已发布分发
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", distribution.getArticle().getArticleId())));
		predicateQueryFieldList.add(new QueryField("publishTime", null, QueryField.PredicateEnum.IS_NOT_NULL));
		List<Distribution> distributionList = distributionService.loadAll(predicateQueryFieldList, null);
		for (Distribution distributionToUpdate : distributionList) {
			// 设置是否首发
			distributionToUpdate.setFirstPublished(distributionToUpdate.getDistributionId().intValue() == distribution.getDistributionId());
			
			// 更新分发是否首发
			distributionService.update(distributionToUpdate, "firstPublished");
		}
		addInfoMessage("修改分发是否首发成功！");
		
		// 刷新分页模型
		pageModel = null;
	}

	/**
	 * 修改分发指定排序位置
	 */
	public void modifyDistributionPositionAction(Distribution distribution) {
		// 只能修改已发布的分发
		if (!distribution.getPublished()) {
			distribution.setPosition(null);
			addErrorMessage("只能修改已发布的分发的指定排序位置！");
			return;
		}
		
		// 指定排序位置不能超出栏目下已发布分发数量
		if (distribution.getPosition() != null) {
			// 查询同一栏目下已发布分发数量
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("column", new QueryField("columnId", distribution.getColumn().getColumnId())));
			predicateQueryFieldList.add(new QueryField("publishTime", null, QueryField.PredicateEnum.IS_NOT_NULL));
			long distributionCount = distributionService.count(predicateQueryFieldList);
			if (distribution.getPosition() >= distributionCount) {
				distribution.setPosition(null);
				addErrorMessage("指定排序位置大于栏目下已发布分发数量，请重试！");
				return;
			}
		}
		
		if (distribution.getPosition() != null) {
			// 查询同一栏目下相同指定排序位置的分发，将其指定排序位置置空
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("column", new QueryField("columnId", distribution.getColumn().getColumnId())));
			predicateQueryFieldList.add(new QueryField("position", distribution.getPosition()));
			List<Distribution> distributionList = distributionService.loadAll(predicateQueryFieldList, null);
			if (distributionList != null) {
				for (Distribution distributionSamePosition : distributionList) {
					// 设置指定排序位置
					distributionSamePosition.setPosition(null);

					// 更新分发指定排序位置
					distributionService.update(distributionSamePosition, "position");
				} 
			}
		}
		
		// 更新分发指定排序位置
		distributionService.update(distribution, "position");
		addInfoMessage("修改分发指定排序位置成功！");
		
		// 刷新分页模型
		pageModel = null;
	}
	
	/**
	 * 发布分发
	 */
	public void publishDistributionAction(Distribution distribution) {
		if (distribution.getPublishedOrScheduled()) {
			return;
		}
		
		// 对话框选项
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", false);
		options.put("width", 600);
		options.put("height", 400);
		options.put("contentWidth", "100%");
		options.put("contentHeight", "100%");
		options.put("includeViewParams", true);
		
		// 传递的参数
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		List<String> param = new ArrayList<String>();
		param.add(distribution.getDistributionId().toString());
		params.put("distributionId", param);
		
		// 显示发布分发对话框
		PrimeFaces.current().dialog().openDynamic("distributionPublish", options, params);
	}
	
	/**
	 * 处理发布分发返回
	 */
	public void handlePublishDistributionReturn(SelectEvent event) {
		Boolean success = (Boolean) event.getObject();
		if (success == true) {
			addInfoMessage("发布分发成功！");
		} else {
			addErrorMessage("发布分发失败，请重新尝试！");
		}
		
		// 刷新分页模型
		pageModel = null;
	}
	
	/**
	 * 批量发布分发
	 */
	public void publishDistributionsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个分发进行批量发布！");
			return;
		}

		for (Distribution distribution : getSelectedModels()) {
			// 选中的分发中如果有已发布或已定时发布的分发，则不能批量发布
			if (distribution.getPublishedOrScheduled()) {
				addErrorMessage("批量发布不能包含已发布的分发！");
				return;
			}
			
			// 如果校验分发缩略图失败，则不能批量发布
			if (!distributionService.validateDistributionThumbnails(distribution)) {
				addErrorMessage("批量发布不能包含缩略图数量或宽高比例不对的分发！");
				return;
			}
		}
		
		for (Distribution distribution : getSelectedModels()) {
			// 发布分发
			distributionService.publishDistribution(distribution, SecurityContextHolder.getContext().getAuthentication().getPrincipal(), new Date(), null);
			
			// 更新文章JSON文件
			articleService.updateArticleJson(distribution.getArticle());
		}
		addInfoMessage("批量发布分发成功！");
		
		// 刷新分页模型
		pageModel = null;
	}
	
	/**
	 * 撤销发布分发
	 */
	public void unpublishDistributionAction(Distribution distribution) {
		if (!distribution.getPublishedOrScheduled()) {
			return;
		}
		
        // 撤销发布分发
		if (distributionService.unpublishDistribution(distribution) == false) {
			addErrorMessage("请修改同一篇文章的其他分发为首发，再撤销发布此分发！");
			return;
		}
		
		// 删除文章JSON文件
		articleService.deleteArticleJson(distribution.getArticle());		
		addInfoMessage( "撤销发布分发成功！");
		
		// 刷新分页模型
		pageModel = null;
	}
}
