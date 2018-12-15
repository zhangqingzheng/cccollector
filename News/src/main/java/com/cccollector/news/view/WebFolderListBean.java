/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.news.model.SiteVersion;
import com.cccollector.news.model.WebFolder;
import com.cccollector.news.model.WebPage;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 网络文件夹列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class WebFolderListBean extends BaseListBean<WebFolder> implements Serializable {

	private static final long serialVersionUID = -3803887148242189459L;

	public WebFolderListBean () {
		modelDisplayName = "网络文件夹";
		modelAttributeName = "webFolder";
		modelClazz = WebFolder.class;
		modelIdAttributeName = "webFolderId";
		submodelAttributeName = "webPage";
		parentModelAttributeName = "parentWebFolder";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<WebFolder>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return siteVersionService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<WebFolder> loadAllModelList() {
				if (getSiteVersion() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("siteVersionId", getSiteVersion().getSiteVersionId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("code", true));
				return webFolderService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}
		};
	}

	/**
	 * 所属站点版本
	 */
	public SiteVersion getSiteVersion() {
		return (SiteVersion) relatedModel(1);
	}
	
	/**
	 * 管理网络页面
	 */
	public void manageWebPagesAction(WebFolder webFolder) {
		handleNavigation(submodelAttributeName + "List.xhtml?" + modelIdAttributeName + "=" + webFolder.getWebFolderId());
	}
	
	/**
	 * 发布网络文件夹
	 */
	public void publishWebFolderAction(WebFolder webFolder) {
		if (webFolder.getStatusEnum() != WebFolder.StatusEnum.制作中) {
			return;
		}
		// 设置更新时间
		webFolder.setUpdateTime(new Date());
		// 设置发布时间
		webFolder.setPublishTime(null);
		// 设置发布状态
		webFolder.setStatus(WebFolder.StatusEnum.已发布.ordinal());
		
		// 更新时间与状态
		webFolderService.update(webFolder, "updateTime", "publishTime", "status");
		addInfoMessage("发布网络文件夹成功！");
		
		// 刷新全部模型
		allModels = null;
		// 刷新根模型TreeNode
		rootModelTreeNode = null;
	}
	
	/**
	 * 一键发布所有制作中网络文件夹
	 */
	public void publishAllWebFolersAction() {
		for (WebFolder webFolder : allModels) {
			if (webFolder.getStatusEnum() == WebFolder.StatusEnum.制作中) {
				// 设置更新时间
				webFolder.setUpdateTime(new Date());
				// 设置发布时间
				webFolder.setPublishTime(null);
				// 设置发布状态
				webFolder.setStatus(WebFolder.StatusEnum.已发布.ordinal());
				// 更新时间与状态
				webFolderService.update(webFolder, "updateTime", "publishTime", "status");
			}
		}
		addInfoMessage("发布网络文件夹成功！");
		
		// 刷新全部模型
		allModels = null;
		// 刷新根模型TreeNode
		rootModelTreeNode = null;
	}

	/**
	 * 撤销发布网络文件夹
	 */
	public void cancelPublishWebFolderAction(WebFolder webFolder) {
		if (webFolder.getStatus() != WebFolder.StatusEnum.已发布.ordinal()) {
			return;
		}
		// 设置更新时间
		webFolder.setUpdateTime(new Date());
		// 设置发布时间
		webFolder.setPublishTime(null);
		// 设置发布状态
		webFolder.setStatus(WebFolder.StatusEnum.已撤销.ordinal());
		
		// 更新
		webFolderService.update(webFolder, "updateTime", "publishTime", "status");
		addInfoMessage("撤销发布网络文件夹成功！");
		
		// 刷新全部模型
		allModels = null;
		// 刷新根模型TreeNode
		rootModelTreeNode = null;
	}
	
	/**
	 * 取消撤销网络文件夹
	 */
	public void unCancelWebFolderAction(WebFolder webFolder) {
		if (webFolder.getStatusEnum() != WebFolder.StatusEnum.已撤销) {
			return;
		}
		webFolder.setUpdateTime(new Date());
		webFolder.setStatus(WebPage.StatusEnum.制作中.ordinal());
		webFolderService.update(webFolder, "updateTime", "status");
	}

	/**
	 * 删除网络文件夹
	 */
	public void deleteWebFolderAction(WebFolder webFolder) {
		// 网络文件夹如果否包含子对象，则不能被删除
		if (webFolderService.hasChildren(webFolder)) {
			addErrorMessage("要删除的网络文件夹中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 只能删除状态为制作中的网络文件夹
		if (webFolder.getStatusEnum() != WebFolder.StatusEnum.制作中) {
			addErrorMessage("只能删除状态为制作中的网络文件夹！");
			return;
		}

		// 删除网络文件夹
		webFolderService.delete(webFolder);
		addInfoMessage("删除网络文件夹成功！");
		
		// 清空选中的模型TreeNode
		setSelectedModelTreeNode(null);
		// 刷新全部模型
		allModels = null;
		// 刷新根模型TreeNode
		rootModelTreeNode = null;
	}

	/**
	 * 删除网络文件夹
	 */
	public void deleteWebFolderAction() {
		if (getSelectedModelTreeNode() == null) {
			addErrorMessage("请选择一个网络文件夹进行删除！");
			return;
		}
		
		// 网络文件夹如果否包含子对象，则不能被删除
		WebFolder webFolder = (WebFolder) getSelectedModelTreeNode().getData();
		if (webFolderService.hasChildrenNotIn(webFolder)) {
			addErrorMessage("要删除的网络文件夹中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 只能删除状态为制作中的网络文件夹
		if (webFolder.getStatusEnum() != WebFolder.StatusEnum.制作中) {
			addErrorMessage("只能删除状态为制作中的网络文件夹！");
			return;
		}

		// 删除网络文件夹
		webFolderService.delete(webFolder);
		// 将选中的TreeNode从父TreeNode中删除
		getSelectedModelTreeNode().getParent().getChildren().remove(getSelectedModelTreeNode());
		addInfoMessage("删除网络文件夹成功！");
		
		// 清空选中的模型TreeNode
		setSelectedModelTreeNode(null);
	}
}
