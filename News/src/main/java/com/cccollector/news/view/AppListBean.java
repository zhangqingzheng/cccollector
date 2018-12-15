/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.news.model.App;
import com.cccollector.news.model.NewsSource;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;

/**
 * 应用列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class AppListBean extends BaseListBean<App> implements Serializable {

	private static final long serialVersionUID = 6309691537661826595L;

	public AppListBean() {
		modelDisplayName = "应用";
		modelAttributeName = "app";
		modelIdAttributeName = "appId";
		submodelAttributeName = "edition";
		genericListBeanHandler = new GenericListBeanHandlerRelatedAll<App>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return newsSourceService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<App> loadAllModelList() {
				List<QueryField> predicateQueryFieldList = null;
				if (getNewsSource() != null) {
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("newsSources", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
				}
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("name", true));				
				return appService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}
		};
	}

	/**
	 * 关联新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(1);
	}
	
	/**
	 * 处理新闻源
	 */
	public void disposeNewsSourcesAction(App app) {
		handleNavigation("newsSourceList.xhtml?appId=" + app.getAppId());
	}
	
	/**
	 * 管理栏目替身
	 */
	public void manageColumnSubstitutesAction(App app) {
		handleNavigation("columnSubstituteList.xhtml?appId=" + app.getAppId());
	}

	/**
	 * 同步应用
	 */
	public void syncAppAction(App app) {
		// 同步应用
		boolean success = appService.syncApp(app);
		if (success) {
			addInfoMessage("同步应用成功！");
		} else {
			addErrorMessage("同步应用失败，请重新尝试！");
		}
		
		// 刷新全部模型
		allModels = null;
	}	

	/**
	 * 未被新闻源使用的应用
	 */
	public List<App> getNotUsedApps() {
		if (getNewsSource() == null) {
			return null;
		}
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("newsSources", new QueryField("newsSourceId", getNewsSource().getNewsSourceId(), PredicateEnum.NOT_IN)));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("enabled", false));
		orderQueryFieldList.add(new QueryField("name", true));
		return appService.loadAll(predicateQueryFieldList, orderQueryFieldList);
	}
	
	/**
	 * 添加应用到新闻源
	 */
	public void addAppToNewsSourceAction() {
		// 添加应用到新闻源
		appService.addAppToNewsSource(getSelectedModelId(), getNewsSource().getNewsSourceId());
		addInfoMessage("添加应用到新闻源成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 清空选中的模型ID
		setSelectedModelId(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 从新闻源中删除应用
	 */
	public void deleteAppFromNewsSourceAction(App app) {
		// 从新闻源中删除应用
		appService.deleteAppFromNewsSource(app.getAppId(), getNewsSource().getNewsSourceId());
		addInfoMessage("从新闻源中删除应用成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量从新闻源中删除应用
	 */
	public void deleteAppsFromNewsSourceAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个应用进行删除！");
			return;
		}

		// 从新闻源中删除应用
		for (App app : getSelectedModels()) {
			appService.deleteAppFromNewsSource(app.getAppId(), getNewsSource().getNewsSourceId());
		}
		addInfoMessage("从新闻源中删除应用成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 删除应用
	 */
	public void deleteAppAction(App app) {
		// 应用如果否包含子对象，则不能被删除
		if (appService.hasChildrenNotIn(app, "editions")) {
			addErrorMessage("要删除的应用中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 删除应用
		appService.deleteApp(app);
		addInfoMessage("删除应用成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除应用
	 */
	public void deleteAppsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个应用进行删除！");
			return;
		}
		
		// 应用如果否包含子对象，则不能被删除
		for (App app : getSelectedModels()) {
			if (appService.hasChildrenNotIn(app, "editions")) {
				addErrorMessage("要删除的应用中包含子对象，请清空子对象再进行删除！");
				return;
			}
		}
		
		// 删除应用
		for (App app : getSelectedModels()) {
			appService.deleteApp(app);
		}
		addInfoMessage("删除应用成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
