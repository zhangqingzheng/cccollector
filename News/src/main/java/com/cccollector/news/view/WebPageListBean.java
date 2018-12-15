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

import com.cccollector.news.model.WebFolder;
import com.cccollector.news.model.WebPage;
import com.cccollector.universal.dao.QueryField;

/**
 * 网络页面列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class WebPageListBean extends BaseListBean<WebPage> implements Serializable {

	private static final long serialVersionUID = 43720451181168706L;

	public WebPageListBean () {
		modelDisplayName = "网络页面";
		modelAttributeName = "webPage";
		modelIdAttributeName = "webPageId";
		useDialog = false;
		genericListBeanHandler = new GenericListBeanHandlerRelatedAll<WebPage>() {

			@Override
			public List<WebPage> loadAllModelList() {
				if (getWebFolder() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("webFolder", new QueryField("webFolderId", getWebFolder().getWebFolderId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("webPageId", true));
				orderQueryFieldList.add(new QueryField("updateTime", false));
				
				return webPageService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return webFolderService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 网络文件夹
	 */
	public WebFolder getWebFolder() {
		return (WebFolder) relatedModel(1);
	}
	
	/**
	 * 发布网络页面
	 */
	public void publishWebPageAction(WebPage webPage) {
		if (webPage.getStatusEnum() != WebPage.StatusEnum.测试中) {
			return;
		}
		webPageService.publishWebPage(webPage);
		addInfoMessage("发布网络页面成功！");		
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量发布网络页面
	 */
	public void publishWebPagesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个网络页面进行发布！");
			return;
		}
		for (WebPage webPage : getSelectedModels()) {
			// 选中的网络页面中包含不是测试中状态的网络页面，则不能批量发布
			if (webPage.getStatusEnum() != WebPage.StatusEnum.测试中) {
				addErrorMessage("只能发布状态是测试中的网络页面！");			
				return;
			}
		}
		for (WebPage webPage : getSelectedModels()) {
			webPageService.publishWebPage(webPage);
		}

		addInfoMessage("批量发布网络页面成功！");
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 撤销发布网络页面
	 */
	public void cancelPublishWebPageAction(WebPage webPage) {
		if (webPage.getStatusEnum() != WebPage.StatusEnum.已发布) {
			return;
		}
		// 撤销发布网络页面
		webPageService.cancelPublishWebPage(webPage);
		addInfoMessage("撤销发布网络页面成功！");		

		// 刷新全部模型
		allModels = null;
	}	
	
	/**
	 * 批量撤销发布网络页面
	 */
	public void cancelPublishWebPagesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个网络页面进行撤销发布！");
			return;
		}
		// 选中的网络页面中如果包含已发布之外的网络页面，则不能批量撤销发布
		for (WebPage webPage : getSelectedModels()) {
			if (webPage.getStatusEnum() != WebPage.StatusEnum.已发布) {
				addErrorMessage("只能撤销发布已发布状态的网络页面！");			
				return;
			}
		}
		// 撤销发布网络页面
		for (WebPage webPage :  getSelectedModels()) {
			// 撤销发布网络页面
			webPageService.cancelPublishWebPage(webPage);
		}
		addInfoMessage("批量撤销发布网络页面成功！");
	
		// 刷新全部模型
		allModels = null;
	} 
	
	/**
	 * 取消撤销网络页面
	 */
	public void unCancelWebPageAction(WebPage webPage) {
		if (webPage.getStatusEnum() != WebPage.StatusEnum.已撤销) {
			return;
		}
		webPage.setUpdateTime(new Date());
		webPage.setStatus(WebPage.StatusEnum.制作中.ordinal());
		webPageService.update(webPage, "updateTime", "status");
	}
	
	/**
	 * 删除网络页面
	 */
	public void deleteWebPageAction(WebPage webPage) {
		// 只能删除制作中的网络页面
		if (webPage.getStatusEnum() != WebPage.StatusEnum.制作中) {
			addErrorMessage("只能删除状态为制作中的网络页面！");
			return;
		}

		// 删除网络页面
		webPageService.deleteWebPage(webPage);
		addInfoMessage("删除网络页面成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 批量删除网络页面
	 */
	public void deleteWebPagesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个网络页面进行删除！");
			return;
		}
		for (WebPage webPage : getSelectedModels()) {
			// 只能删除制作中的网络页面
			if (webPage.getStatusEnum() != WebPage.StatusEnum.制作中) {
				addErrorMessage("只能删除状态为制作中的网络页面！");
				return;
			}
		}
		
		// 删除网络页面文件和目录
		for (WebPage webPage : getSelectedModels()) {
			// 删除网络页面
			webPageService.deleteWebPage(webPage);
		}		
		addInfoMessage("删除网络页面成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
