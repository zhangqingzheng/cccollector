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

import com.cccollector.news.model.Column;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.RecommendGroup;
import com.cccollector.news.model.RecommendGroup.TemplateTypeEnum;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.universal.dao.QueryField;

/**
 * 推荐组Bean类
 *
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class RecommendGroupBean extends BaseEditBean<RecommendGroup> implements Serializable {

	private static final long serialVersionUID = 2618116849349202505L;

	public RecommendGroupBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<RecommendGroup>() {

			@Override
			public RecommendGroup loadModel(Integer modelId) {
				RecommendGroup recommendGroup = null;
				if (modelId == null) {
					recommendGroup = new RecommendGroup();
					recommendGroup.setTemplateType(TemplateTypeEnum.顶部轮播.ordinal());
					recommendGroup.setEnabled(false);
					recommendGroup.setNewsSource(getNewsSource());
				} else {
					recommendGroup = recommendGroupService.loadById(modelId);

					// 设置所有者
					switch (recommendGroup.getOwnerTypeEnum()) {
					case 栏目:
						Column column = columnService.loadById(recommendGroup.getOwnerId());
						recommendGroup.setOwner(column);
						break;
					}
				}
				return recommendGroup;
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
	 * 推荐组
	 */
	public RecommendGroup getRecommendGroup() {
		return getModel();
	}

	/**
	 * 所属新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(1);
	}

	/**
	 * 全部栏目
	 */
	private List<Column> allColumns;

	public List<Column> getAllColumns() {
		if (allColumns == null) {
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("code", true));
			allColumns = columnService.loadAll(predicateQueryFieldList, orderQueryFieldList);
		}
		return allColumns;
	}

	/**
	 * 保存
	 */
	public void saveAction() {
		RecommendGroup recommendGroup = getRecommendGroup();
		if (recommendGroup.getRecommendGroupId() == null) {
			// 添加推荐组
			recommendGroupService.addRecommendGroup(recommendGroup);
			addInfoMessageToFlash("添加推荐组成功！");
		} else {
			// 编辑推荐组
			recommendGroupService.update(recommendGroup);

			// 刷新推荐组对应的栏目静态页面
			if (RecommendGroup.OwnerTypeEnum.栏目.ordinal() == recommendGroup.getOwnerType()) {
				templateService.staticPageCase(recommendGroup.getOwnerId(), TemplateMapping.TypeEnum.栏目.ordinal(), null);
			}

			addInfoMessageToFlash("编辑推荐组成功！");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("recommendGroupList.xhtml?multiSelect=true");
		}
	}
}
