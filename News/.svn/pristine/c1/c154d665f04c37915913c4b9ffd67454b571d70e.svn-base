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

import com.cccollector.news.model.App;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.Site;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;

/**
 * 新闻源列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class NewsSourceListBean extends BaseListBean<NewsSource> implements Serializable {

	private static final long serialVersionUID = 4724801461970557382L;

	public NewsSourceListBean () {
		modelDisplayName = "新闻源";
		modelAttributeName = "newsSource";
		modelIdAttributeName = "newsSourceId";
		submodelAttributeName = "column";
		genericListBeanHandler = new GenericListBeanHandlerRelatedAll<NewsSource>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return appService.loadById(relatedModelId);
				}
				if (index == 2) {
					return siteService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<NewsSource> loadAllModelList() {
				if (getApp() == null && getSite() == null) {
					List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
					orderQueryFieldList.add(new QueryField("enabled", false));
					orderQueryFieldList.add(new QueryField("position", true));					
					return newsSourceService.loadAll(orderQueryFieldList);
				} else if (getApp() != null) {
					return appService.loadById(getApp().getAppId(), "newsSources").getNewsSources();
				} else {
					return siteService.loadById(getSite().getSiteId(), "newsSources").getNewsSources();
				}
			}
		};
	}

	/**
	 * 关联应用
	 */
	public App getApp() {
		return (App) relatedModel(1);
	}
	
	/**
	 * 关联站点
	 */
	public Site getSite() {
		return (Site) relatedModel(2);
	}
	
	/**
	 * 处理应用
	 */
	public void disposeAppsAction(NewsSource newsSource) {
		handleNavigation("appList.xhtml?newsSourceId=" + newsSource.getNewsSourceId());
	}
	
	/**
	 * 处理站点
	 */
	public void disposeSitesAction(NewsSource newsSource) {
		handleNavigation("siteList.xhtml?newsSourceId=" + newsSource.getNewsSourceId());
	}
	
	/**
	 * 管理样式
	 */
	public void manageStylesAction(NewsSource newsSource) {
		handleNavigation("styleList.xhtml?newsSourceId=" + newsSource.getNewsSourceId());
	}
	
	/**
	 * 管理分类标签
	 */
	public void manageCategoryTagsAction(NewsSource newsSource) {
		handleNavigation("categoryTagList.xhtml?newsSourceId=" + newsSource.getNewsSourceId());
	}

	/**
	 * 未被应用使用的新闻源
	 */
	public List<NewsSource> getNotUsedAppNewsSources() {
		if (getApp() == null) {
			return null;
		}
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("apps", new QueryField("appId", getApp().getAppId(), PredicateEnum.NOT_IN)));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("enabled", false));
		orderQueryFieldList.add(new QueryField("position", true));
		return newsSourceService.loadAll(predicateQueryFieldList, orderQueryFieldList);
	}
	
	/**
	 * 未被站点使用的新闻源
	 */
	public List<NewsSource> getNotUsedSiteNewsSources() {
		if (getSite() == null) {
			return null;
		}
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("sites", new QueryField("siteId", getSite().getSiteId(), PredicateEnum.NOT_IN)));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("enabled", false));
		orderQueryFieldList.add(new QueryField("position", true));
		return newsSourceService.loadAll(predicateQueryFieldList, orderQueryFieldList);
	}
	
	/**
	 * 添加新闻源到应用
	 */
	public void addNewsSourceToAppAction() {
		// 添加新闻源到应用
		newsSourceService.addNewsSourceToApp(getSelectedModelId(), getApp().getAppId());
		addInfoMessage("添加新闻源到应用成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 清空选中的模型ID
		setSelectedModelId(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 从应用中删除新闻源
	 */
	public void deleteNewsSourceFromAppAction(NewsSource newsSource) {
		// 从应用中删除新闻源
		newsSourceService.deleteNewsSourceFromApp(newsSource.getNewsSourceId(), getApp().getAppId());
		addInfoMessage("从应用中删除新闻源成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量从应用中删除新闻源
	 */
	public void deleteNewsSourcesFromAppAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个新闻源进行删除！");
			return;
		}

		// 从应用中删除新闻源
		for (NewsSource newsSource : getSelectedModels()) {
			newsSourceService.deleteNewsSourceFromApp(newsSource.getNewsSourceId(), getApp().getAppId());
		}
		addInfoMessage("从应用中删除新闻源成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 添加新闻源到站点
	 */
	public void addNewsSourceToSiteAction() {
		// 添加新闻源到站点
		newsSourceService.addNewsSourceToSite(getSelectedModelId(), getSite().getSiteId());
		addInfoMessage("添加新闻源到站点成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 清空选中的模型ID
		setSelectedModelId(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 从站点中删除新闻源
	 */
	public void deleteNewsSourceFromSiteAction(NewsSource newsSource) {
		// 从站点中删除新闻源
		newsSourceService.deleteNewsSourceFromSite(newsSource.getNewsSourceId(), getSite().getSiteId());
		addInfoMessage("从站点中删除新闻源成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量从站点中删除新闻源
	 */
	public void deleteNewsSourcesFromSiteAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个新闻源进行删除！");
			return;
		}

		// 从应用中删除新闻源
		for (NewsSource newsSource : getSelectedModels()) {
			newsSourceService.deleteNewsSourceFromSite(newsSource.getNewsSourceId(), getSite().getSiteId());
		}
		addInfoMessage("从应用中删除新闻源成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量修改新闻源是否可用
	 */
	public void modifyNewsSourcesEnabledAction(boolean enabled) {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个新闻源进行修改！");
			return;
		}

		// 修改新闻源是否可用
		for (NewsSource newsSource : getSelectedModels()) {
			newsSource.setEnabled(enabled);
			newsSourceService.update(newsSource, "enabled");
		}
		addInfoMessage("修改新闻源是否可用成功！");
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 移动新闻源
	 */
	public void moveNewsSourceAction(ReorderEvent event) {
		if (getApp() == null && getSite() == null) {
			NewsSource newsSourceTo = allModels.get(event.getToIndex());
			// 如果所选新闻源不可用，则无法移动
			if (!newsSourceTo.getEnabled()) {
				addErrorMessage("请选择可用的新闻源进行移动！");
				return;
			}

			// 移动新闻源
			newsSourceService.moveNewsSources(allModels);
		} else if (getApp() != null){
			// 在应用中移动新闻源
			newsSourceService.moveNewsSourcesInApp(allModels, getApp().getAppId());
		} else {
			// 在站点中移动新闻源
			newsSourceService.moveNewsSourcesInSite(allModels, getSite().getSiteId());
		}
		addInfoMessage("移动新闻源成功！");
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 删除新闻源
	 */
	public void deleteNewsSourceAction(NewsSource newsSource) {
		// 新闻源如果否包含子对象，则不能被删除
		if (newsSourceService.hasChildren(newsSource)) {
			addErrorMessage("要删除的新闻源中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 删除新闻源
		newsSourceService.delete(newsSource);
		addInfoMessage("删除新闻源成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除新闻源
	 */
	public void deleteNewsSourcesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个新闻源进行删除！");
			return;
		}

		// 新闻源如果否包含子对象，则不能被删除
		for (NewsSource newsSource : getSelectedModels()) {
			if (newsSourceService.hasChildren(newsSource)) {
				addErrorMessage("要删除的新闻源中包含子对象，请清空子对象再进行删除！");
				return;
			}
		}

		// 删除新闻源
		newsSourceService.deleteAll(getSelectedModels());
		addInfoMessage("删除新闻源成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
