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
import org.springframework.security.core.context.SecurityContextHolder;

import com.cccollector.news.model.Article;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.NewsSource;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 分发列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class DistributionListBean extends BaseListBean<Distribution> implements Serializable {

	private static final long serialVersionUID = 536309715051623965L;

	public DistributionListBean() {
		modelDisplayName = "分发";
		modelAttributeName = "distribution";
		modelIdAttributeName = "distributionId";
		submodelAttributeName = "thumbnail";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Distribution>() {

			@Override
			public List<Distribution> loadAllModelList() {
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", getArticle().getArticleId())));
				if (getModelId3() != null && !getModelId3().equals("null")) {
					try {
						predicateQueryFieldList.add(new QueryField("distributionId", Integer.valueOf(getModelId3())));
					} catch (Exception e) {}
				}
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("distributionId", false));
				return distributionService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return articleService.loadById(relatedModelId);
				} else if (index == 2) {
					return newsSourceService.loadById(relatedModelId);					
				}
				return null;
			}
		};
	}

	/**
	 * 所属文章
	 */
	public Article getArticle() {
		return (Article) relatedModel(1);
	}

	/**
	 * 所属新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(2);
	}
	
	/**
	 * 获取文章路径
	 */
	public String getArticlePath() {
		if (getModelId3() == null || getModelId3().equals("null")) {
			return "articleList.xhtml?newsSourceId=" + getModelId2();
		} else {
			return "columnDistributionList.xhtml?newsSourceId=" + getModelId2() + "&published=" + getModelId5() + "&columnId="  + (getModelId4() != null && !(getModelId4().equals("null"))? getModelId4() : "");
		}
	}

	/**
	 * 选择分发
	 */
	public void selectDistributionAction(SelectEvent event) {
		Distribution distribution = (Distribution) event.getObject();
		if (!getMultiSelect()) {
			handleNavigation("thumbnailList.xhtml?distributionId=" + distribution.getDistributionId() + "&articleId=" + getModelId1() + "&newsSourceId=" + getModelId2() + "&specifiedDistributionId=" + (getModelId3() == null ? "null" : getModelId3()) + "&published=" + (getModelId5() == null ? "null" : getModelId5()) + "&columnId=" + ((getModelId4() == null || getModelId4().equals("null")) ? "null" : getModelId4()));
		}
	}
	
	/**
	 * 删除分发
	 */
	public void deleteDistributionAction(Distribution distribution) {
		// 分发如果已发布，则不能被删除
		if (distribution.getPublished()) {
			addErrorMessage("要删除的分发已被发布，请撤销发布后再进行删除！");
			return;
		}

		// 删除分发
		distributionService.deleteDistribution(distribution);
		addInfoMessage("删除分发成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除分发
	 */
	public void deleteDistributionsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个分发进行删除！");
			return;
		}

		// 分发如果已发布，则不能被删除
		for (Distribution distribution : getSelectedModels()) {
			if (distribution.getPublished()) {
				addErrorMessage("要删除的分发已被发布，请撤销发布后再进行删除！");
				return;
			}
		}
		
		for (Distribution distribution : getSelectedModels()) {			
			// 删除分发
			distributionService.deleteDistribution(distribution);
		}
		addInfoMessage("删除分发成功！");

		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
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
		
		// 刷新全部模型
		allModels = null;
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
		
		// 刷新全部模型
		allModels = null;
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
		predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", getArticle().getArticleId())));
		predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
		List<Distribution> distributionList = distributionService.loadAll(predicateQueryFieldList, null);
		for (Distribution distributionToUpdate : distributionList) {
			// 设置是否首发
			distributionToUpdate.setFirstPublished(distributionToUpdate.getDistributionId().intValue() == distribution.getDistributionId());
			
			// 更新分发是否首发
			distributionService.update(distributionToUpdate, "firstPublished");
		}
		addInfoMessage("修改分发是否首发成功！");
		
		// 刷新全部模型
		allModels = null;
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
			predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
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
		
		// 刷新全部模型
		allModels = null;
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
		
		// 刷新全部模型
		allModels = null;
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
		
		// 刷新全部模型
		allModels = null;
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
		addInfoMessage("撤销发布分发成功！");
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量撤销发布分发
	 */
	public void unpublishDistributionsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个分发进行批量撤销发布！");
			return;
		}

		// 选中的分发中如果有未发布的分发，则不能批量撤销发布
		List<Distribution> firstPublishedDistributions = new ArrayList<Distribution>();
		for (Distribution distribution : getSelectedModels()) {
			if (!distribution.getPublishedOrScheduled()) {
				addErrorMessage("批量撤销发布不能包含未发布的分发！");
				return;
			}
			if (distribution.getFirstPublished() == true) {
				firstPublishedDistributions.add(distribution);
			}
		}
		
		boolean allSuccess = true;
		// 撤销发布非首发分发
		for (Distribution distribution : getSelectedModels()) {
			if (distribution.getFirstPublished() == false) {
				if (distributionService.unpublishDistribution(distribution) == true) {
					// 删除文章JSON文件
					articleService.deleteArticleJson(distribution.getArticle());
				} else {
					allSuccess = false;
				}
			}
		}
		// 撤销发布首发分发
		for (Distribution distribution : firstPublishedDistributions) {
			if (distributionService.unpublishDistribution(distribution) == true) {
				// 删除文章JSON文件
				articleService.deleteArticleJson(distribution.getArticle());
			} else {
				allSuccess = false;
			}
		}
		if (allSuccess) {
			addInfoMessage("批量撤销发布分发成功！");
		} else {
			addErrorMessage("部分分发批量撤销发布失败，请重试！");
		}
				
		// 刷新全部模型
		allModels = null;
	}
}
