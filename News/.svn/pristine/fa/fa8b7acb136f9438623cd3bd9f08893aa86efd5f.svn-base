/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.news.model.Site;
import com.cccollector.news.model.SiteVersion;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 站点版本列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class SiteVersionListBean extends BaseListBean<SiteVersion> implements Serializable {

	private static final long serialVersionUID = -8602709064488585674L;

	public SiteVersionListBean() {
		modelDisplayName = "站点版本";
		modelAttributeName = "siteVersion";
		modelIdAttributeName = "siteVersionId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<SiteVersion>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return siteService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<SiteVersion> loadAllModelList() {
				if (getSite() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("site", new QueryField("siteId", getSite().getSiteId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("version", true));
				return siteVersionService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}
		};		
	}

	/**
	 * 所属站点
	 */
	public Site getSite() {
		return (Site) relatedModel(1);
	}
	
	/**
	 * 管理栏目替代
	 */
	public void manageColumnReplacementsAction(SiteVersion siteVersion) {
		handleNavigation("columnReplacementList.xhtml?siteVersionId=" + siteVersion.getSiteVersionId());
	}
	
	/**
	 * 发布站点版本
	 */
	public void publishSiteVersionAction(SiteVersion siteVersion) {
		if (siteVersion.getStatusEnum() != SiteVersion.StatusEnum.测试中) {
			return;
		}
		siteVersionService.publishSiteVersion(siteVersion);
		addInfoMessage("发布站点版本成功！");		
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 取消待撤销站点版本(将待撤销状态恢复为已发布状态)
	 */
	public void cancelRevokingSiteVersionAction(SiteVersion siteVersion) {
		if (siteVersion.getStatus() != SiteVersion.StatusEnum.待撤销.ordinal()) {
			return;
		}
		siteVersionService.cancelRevokingSiteVersion(siteVersion);
		addInfoMessage("取消撤销站点版本成功！");		

		// 刷新全部模型
		allModels = null;
	}	
	
	/**
	 * 撤销待撤销站点版本(将待撤销状态变为已撤销状态并删除站点版本文件)
	 */
	public void revokeSiteVersionAction(SiteVersion siteVersion) {
		if (siteVersion.getStatus() != SiteVersion.StatusEnum.待撤销.ordinal()) {
			return;
		}
		siteVersionService.revokeSiteVersion(siteVersion);
		addInfoMessage("取消撤销站点版本成功！");		

		// 刷新全部模型
		allModels = null;
	}	
	
	/**
	 * 取消已撤销站点版本
	 */
	public void unRevokeSiteVersionAction(SiteVersion siteVersion) {
		if (siteVersion.getStatus() != SiteVersion.StatusEnum.已撤销.ordinal()) {
			return;
		}
		
		// 更改站点版本状态为制作中
		siteVersion.setStatus(SiteVersion.StatusEnum.制作中.ordinal());
		siteVersionService.update(siteVersion, "status");
		addInfoMessage("取消撤销站点版本成功！");		

		// 刷新全部模型
		allModels = null;
	}	
	
	/**
	 * 删除站点版本
	 */
	public void deleteSiteVersionAction(SiteVersion siteVersion) {
		// 站点版本如果不是制作中，则不能被删除
		if (siteVersion.getStatusEnum() != SiteVersion.StatusEnum.制作中) {
			return;
		}
		// 删除站点版本
		siteVersionService.delete(siteVersion);
//		TODO:删除tomcat下的站点版本数据
		addInfoMessage("删除站点版本成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 批量删除站点版本
	 */
	public void deleteSiteVersionsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个站点版本进行删除！");
			return;
		}
		for (SiteVersion siteVersion : getSelectedModels()) {
			// 站点版本如果不是制作中，则不能被删除
			if (siteVersion.getStatusEnum() != SiteVersion.StatusEnum.制作中) {
				addErrorMessage("只可删除状态为制作中的站点版本！");
				return;
			}
		}
		
		// 删除站点版本文件和目录
		for (SiteVersion siteVersion : getSelectedModels()) {
			// 删除站点版本
			siteVersionService.delete(siteVersion);
//			TODO:删除tomcat下的站点版本数据
		}		
		addInfoMessage("删除站点版本成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 管理模板
	 */
	public void manageTemplatesAction(SiteVersion siteVersion) {
		handleNavigation("templateList.xhtml?siteVersionId=" + siteVersion.getSiteVersionId());
	}
	
	/**
	 * 管理模板映射
	 */
	public void manageTemplateMappingsAction(SiteVersion siteVersion) {
		handleNavigation("templateMappingList.xhtml?siteVersionId=" + siteVersion.getSiteVersionId());
	}
	
	/**
	 * 管理网络文件夹
	 */
	public void manageWebFoldersAction(SiteVersion siteVersion) {
		handleNavigation("webFolderList.xhtml?siteVersionId=" + siteVersion.getSiteVersionId());
	}
	
	/**
	 * 整站静态化
	 */
	public void staticPageAction(SiteVersion siteVersion) {
		if (siteVersion.getStatusEnum() != SiteVersion.StatusEnum.测试中 && siteVersion.getStatusEnum() != SiteVersion.StatusEnum.已发布) {
			return;
		}
		siteVersionService.staticPageAction(siteVersion.getSiteVersionId());
	}
	
	/**
	 * 整站栏目静态化
	 */
	public void columnStaticPageAction() {
		if (getSelectedModels().get(0).getStatusEnum() != SiteVersion.StatusEnum.测试中 && getSelectedModels().get(0).getStatusEnum() != SiteVersion.StatusEnum.已发布) {
			addErrorMessageToFlash("只能静态化状态为测试中或已发布的站点版本!");
			return;
		}
		siteVersionService.columnStaticPageAction(getSelectedModels().get(0).getSiteVersionId());
	}
	
	/**
	 * 整站文章静态化
	 */
	public void articleStaticPageAction() {
		if (getSelectedModels().get(0).getStatusEnum() != SiteVersion.StatusEnum.测试中 && getSelectedModels().get(0).getStatusEnum() != SiteVersion.StatusEnum.已发布) {
			addErrorMessageToFlash("只能静态化状态为测试中或已发布的站点版本!");
			return;
		}
		siteVersionService.articleStaticPageAction(getSelectedModels().get(0));
	}
	
	/**
	 * 整站分类标签静态化
	 */
	public void categoryTagStaticPageAction() {
		if (getSelectedModels().get(0).getStatusEnum() != SiteVersion.StatusEnum.测试中 && getSelectedModels().get(0).getStatusEnum() != SiteVersion.StatusEnum.已发布) {
			addErrorMessageToFlash("只能静态化状态为测试中或已发布的站点版本!");
			return;
		}
		siteVersionService.categoryTagStaticPageAction(getSelectedModels().get(0));
	}
}
