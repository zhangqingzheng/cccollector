/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.Site;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;

/**
 * 站点列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class SiteListBean extends BaseListBean<Site> implements Serializable {

	private static final long serialVersionUID = 6309691537661826595L;

	public SiteListBean() {
		modelDisplayName = "站点";
		modelAttributeName = "site";
		modelIdAttributeName = "siteId";
		submodelAttributeName = "siteVersion";
		genericListBeanHandler = new GenericListBeanHandlerRelatedAll<Site>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return newsSourceService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Site> loadAllModelList() {
				List<QueryField> predicateQueryFieldList = null;
				if (getNewsSource() != null) {
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("newsSources", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
				}
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("name", true));				
				return siteService.loadAll(predicateQueryFieldList, orderQueryFieldList);
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
	public void disposeNewsSourcesAction(Site site) {
		handleNavigation("newsSourceList.xhtml?siteId=" + site.getSiteId());
	}

	/**
	 * 未被新闻源使用的站点
	 */
	public List<Site> getNotUsedSites() {
		if (getNewsSource() == null) {
			return null;
		}
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("newsSources", new QueryField("newsSourceId", getNewsSource().getNewsSourceId(), PredicateEnum.NOT_IN)));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("enabled", false));
		orderQueryFieldList.add(new QueryField("name", true));
		return siteService.loadAll(predicateQueryFieldList, orderQueryFieldList);
	}
	
	/**
	 * 添加站点到新闻源
	 */
	public void addSiteToNewsSourceAction() {
		// 添加站点到新闻源
		siteService.addSiteToNewsSource(getSelectedModelId(), getNewsSource().getNewsSourceId());
		addInfoMessage("添加站点到新闻源成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 清空选中的模型ID
		setSelectedModelId(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 从新闻源中删除站点
	 */
	public void deleteSiteFromNewsSourceAction(Site site) {
		// 从新闻源中删除站点
		siteService.deleteSiteFromNewsSource(site.getSiteId(), getNewsSource().getNewsSourceId());
		addInfoMessage("从新闻源中删除站点成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量从新闻源中删除站点
	 */
	public void deleteSitesFromNewsSourceAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个站点进行删除！");
			return;
		}

		// 从新闻源中删除站点
		for (Site site : getSelectedModels()) {
			siteService.deleteSiteFromNewsSource(site.getSiteId(), getNewsSource().getNewsSourceId());
		}
		addInfoMessage("从新闻源中删除站点成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 删除站点
	 */
	public void deleteSiteAction(Site site) {
		// 站点如果否包含子对象，则不能被删除
		if (siteService.hasChildrenNotIn(site, "siteVersions")) {
			addErrorMessage("要删除的站点中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 删除站点
		siteService.deleteSite(site);
		addInfoMessage("删除站点成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除站点
	 */
	public void deleteSitesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个站点进行删除！");
			return;
		}
		
		// 站点如果否包含子对象，则不能被删除
		for (Site site : getSelectedModels()) {
			if (siteService.hasChildrenNotIn(site, "siteVersions")) {
				addErrorMessage("要删除的站点中包含子对象，请清空子对象再进行删除！");
				return;
			}
		}
		
		// 删除站点
		for (Site site : getSelectedModels()) {
			siteService.deleteSite(site);
		}
		addInfoMessage("删除站点成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
