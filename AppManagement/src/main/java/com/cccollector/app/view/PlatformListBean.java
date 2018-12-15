/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.app.model.App;
import com.cccollector.app.model.Certificate;
import com.cccollector.app.model.Platform;
import com.cccollector.app.model.RootCertificate;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;

/**
 * 平台列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class PlatformListBean extends BaseListBean<Platform> implements Serializable {

	private static final long serialVersionUID = -213809676355266996L;

	public PlatformListBean () {
		modelDisplayName = "平台";
		modelAttributeName = "platform";
		modelIdAttributeName = "platformId";
		submodelAttributeName = "module";
		genericListBeanHandler = new GenericListBeanHandlerRelatedAll<Platform>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return appService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Platform> loadAllModelList() {
				List<QueryField> predicateQueryFieldList = null;
				if (getApp() != null) {
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("apps", new QueryField("appId", getApp().getAppId())));
				}
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("name", true));
				return platformService.loadAll(predicateQueryFieldList, orderQueryFieldList);
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
	 * 处理应用
	 */
	public void disposeAppsAction(Platform platform) {
		handleNavigation("appList.xhtml?platformId=" + platform.getPlatformId());
	}

	/**
	 * 管理角色
	 */
	public void manageRolesAction(Platform platform) {
		handleNavigation("roleList.xhtml?platformId=" + platform.getPlatformId());
	}
	
	/**
	 * 管理证书
	 */
	public void manageCertificatesAction(Platform platform) {
		// 获取可用的根证书
		RootCertificate rootCertificate = rootCertificateService.getEnabledRootCertificate();
		if (rootCertificate != null) {
			handleNavigation("certificateList.xhtml?rootCertificateId=" + rootCertificate.getRootCertificateId() + "&ownerType=" + Certificate.OwnerTypeEnum.平台.ordinal() + "&ownerId=" + platform.getPlatformId());
		}
	}		
	
	/**
	 * 同步证书
	 */
	public void syncCertificatesAction(Platform platform) {
		// 向平台同步证书
		boolean success = platformService.syncCertificatesToPlatform(platform);
		if (success) {
			addInfoMessage("同步证书成功！");
		} else {
			addErrorMessage("同步证书失败，请重新尝试！");
		}
	}		

	/**
	 * 未被应用使用的平台
	 */
	public List<Platform> getNotUsedPlatforms() {
		if (getApp() == null) {
			return null;
		}
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("apps", new QueryField("appId", getApp().getAppId(), PredicateEnum.NOT_IN)));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("enabled", false));
		orderQueryFieldList.add(new QueryField("name", true));
		return platformService.loadAll(predicateQueryFieldList, orderQueryFieldList);
	}
	
	/**
	 * 添加平台到应用
	 */
	public void addPlatformToAppAction() {
		// 添加平台到应用
		platformService.addPlatformToApp(getSelectedModelId(), getApp().getAppId());
		addInfoMessage("添加平台到应用成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 清空选中的模型ID
		setSelectedModelId(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 从应用中删除平台
	 */
	public void deletePlatformFromAppAction(Platform platform) {
		// 从应用中删除平台
		platformService.deletePlatformFromApp(platform.getPlatformId(), getApp().getAppId());
		addInfoMessage("从应用中删除平台成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量从应用中删除平台
	 */
	public void deletePlatformsFromAppAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个平台进行删除！");
			return;
		}
		
		// 从应用中删除平台
		for (Platform platform : getSelectedModels()) {
			platformService.deletePlatformFromApp(platform.getPlatformId(), getApp().getAppId());
		}
		addInfoMessage("从应用中删除平台成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量修改平台是否可用
	 */
	public void modifyPlatformsEnabledAction(boolean enabled) {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个平台进行修改！");
			return;
		}
		
		// 修改平台是否可用
		for (Platform platform : getSelectedModels()) {
			platform.setEnabled(enabled);
			platformService.update(platform, "enabled");
		}
		addInfoMessage("修改平台是否可用成功！");
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 删除平台
	 */
	public void deletePlatformAction(Platform platform) {
		// 平台如果否包含子对象，则不能被删除
		if (platformService.hasChildren(platform)) {
			addErrorMessage("要删除的平台中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 删除平台
		platformService.delete(platform);
		addInfoMessage("删除平台成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除平台
	 */
	public void deletePlatformsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个平台进行删除！");
			return;
		}
		
		// 平台如果否包含子对象，则不能被删除
		for (Platform platform : getSelectedModels()) {
			if (platformService.hasChildren(platform)) {
				addErrorMessage("要删除的平台中包含子对象，请清空子对象再进行删除！");
				return;
			}
		}
		
		// 删除平台
		platformService.deleteAll(getSelectedModels());				
		addInfoMessage("删除平台成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
