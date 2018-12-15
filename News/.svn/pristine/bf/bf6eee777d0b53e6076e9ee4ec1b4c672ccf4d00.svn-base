/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.news.model.App;

/**
 * 应用Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class AppBean extends BaseEditBean<App> implements Serializable {

	private static final long serialVersionUID = 2369713112678103678L;

	public AppBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModel<App>() {
			
			@Override
			public App loadModel(Integer modelId) {
				App app = null;
				if (modelId == null) {
					app = new App();
				} else {
					app = appService.loadById(modelId);
				}
				return app;
			}
		};
	}

	/**
	 * 应用
	 */
	public App getApp() {
		return getModel();
	}

	/**
	 * 获取未使用的应用管理平台应用
	 */
	public List<com.cccollector.universal.app.model.App> getNotUsedAppManagementApps() {
		return appService.loadNotUsedAppManagementApps();
	}

	/**
	 * 对应应用管理平台应用ID
	 */
	private Integer appManagementAppId;

	public Integer getAppManagementAppId() {
		return appManagementAppId;
	}

	public void setAppManagementAppId(Integer _appManagementAppId) {
		appManagementAppId = _appManagementAppId;
	}

	/**
	 * 保存
	 */
	public void saveAction() {
		App app = getApp();
		if (app.getAppId() == null) {
			// 添加应用
			appService.addApp(appManagementAppId);
			addInfoMessageToFlash("添加应用成功！");
			
			if (getUsingDialog()) {
				PrimeFaces.current().dialog().closeDynamic(null);
			} else {
				handleNavigation("appList.xhtml?multiSelect=true");
			}
		}
	}
}
