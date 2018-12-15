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

import com.cccollector.news.model.Column;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.RecommendGroup;
import com.cccollector.universal.dao.QueryField;

/**
 * 推荐组列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class RecommendGroupListBean extends BaseListBean<RecommendGroup> implements Serializable {
	
	private static final long serialVersionUID = 6395397767015367187L;

	public RecommendGroupListBean () {
		modelDisplayName = "推荐组";
		modelAttributeName = "recommendGroup";
		modelIdAttributeName = "recommendGroupId";
		submodelAttributeName = "recommend";
		genericListBeanHandler = new GenericListBeanHandlerRelatedAll<RecommendGroup>() {

			@Override
			public List<RecommendGroup> loadAllModelList() {
				if (getNewsSource() == null) {
					return null;
				}
				// 查询条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("ownerType",true));
				orderQueryFieldList.add(new QueryField("ownerId", true));
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("position", false));

				List<RecommendGroup> allRecommendGroups = recommendGroupService.loadAll(predicateQueryFieldList, orderQueryFieldList);
				
				// 设置所有者
				for (RecommendGroup recommendGroup : allRecommendGroups) {
					switch (recommendGroup.getOwnerTypeEnum()) {
					case 栏目:
						Column column = columnService.loadById(recommendGroup.getOwnerId());
						recommendGroup.setOwner(column);
						break;
					}
				}
				return allRecommendGroups;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return newsSourceService.loadById(relatedModelId);
				}
				return null;
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
	 * 批量修改推荐组是否可用
	 */
	public void modifyRecommendGroupsEnabledAction(boolean enabled) {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个推荐组进行修改！");
			return;
		}

		for (RecommendGroup recommendGroup : getSelectedModels()) {
			recommendGroup.setEnabled(enabled);
			recommendGroupService.update(recommendGroup, "enabled");
		}
		addInfoMessage("修改推荐组是否可用成功！");

		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 移动推荐组
	 */
	public void moveRecommendGroupAction(ReorderEvent event) {
		RecommendGroup recommendGroupTo = allModels.get(event.getToIndex());
		if (!recommendGroupTo.getEnabled()) {
			addErrorMessage("请选择可用的推荐组进行移动！");
			return;
		}

		// 移动推荐组
		recommendGroupService.moveRecommendGroups(allModels, recommendGroupTo);
		addInfoMessage("移动推荐组成功！");

		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 删除推荐组
	 */
	public void deleteRecommendGroupAction(RecommendGroup recommendGroup) {
		// 推荐组如果否包含子对象，则不能被删除
		if (recommendGroupService.hasChildren(recommendGroup)) {
			addErrorMessage("要删除的推荐组中包含子对象，请清空子对象再进行删除！");
			return;
		}

		// 删除推荐组
		recommendGroupService.delete(recommendGroup);
		addInfoMessage("删除推荐组成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 批量删除推荐组
	 */
	public void deleteRecommendGroupsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个推荐组进行删除！");
			return;
		}

		// 推荐组如果否包含子对象，则不能被删除
		for (RecommendGroup recommendGroup : getSelectedModels()) {
			if (recommendGroupService.hasChildren(recommendGroup)) {
				addErrorMessage("要删除的推荐组中包含子对象，请清空子对象再进行删除！");
				return;
			}
		}
		
		// 删除推荐组
		recommendGroupService.deleteAll(getSelectedModels());
		addInfoMessage("删除推荐组成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
