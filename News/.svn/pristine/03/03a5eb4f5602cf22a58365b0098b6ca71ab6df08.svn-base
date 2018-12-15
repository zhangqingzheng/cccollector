/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.cccollector.news.view.BaseEditBean;
import com.cccollector.news.model.App;
import com.cccollector.news.model.User;
import com.cccollector.news.model.UserApp;

/**
 * 用户应用Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class UserAppBean extends BaseEditBean<UserApp> implements Serializable {

	private static final long serialVersionUID = 7928585172183733578L;

	public UserAppBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModel<UserApp>() {

			@Override
			public UserApp loadModel(Integer modelId) {
				UserApp userApp = null;
				if (modelId == null) {
					userApp = new UserApp();
				} else {
					userApp = userAppService.loadById(modelId);
				}
				return userApp;
			}
		};
	}

	/**
	 * 用户应用
	 */
	public UserApp getUserApp() {
		return getModel();
	}

	/**
	 * 全部用户平台用户应用
	 */
	private List<com.cccollector.universal.user.model.UserApp> allUserUserApps;

	/**
	 * 对应用户平台用户应用ID
	 */
	private Integer userUserAppId;

	public Integer getUserUserAppId() {
		return userUserAppId;
	}

	public void setUserUserAppId(Integer _userUserAppId) {
		userUserAppId = _userUserAppId;
	}

	/**
	 * 处理用户平台用户应用自动完成
	 */
	public List<com.cccollector.universal.user.model.UserApp> handleUserUserAppIdComplete(String query) {
		allUserUserApps = user_userAppService.loadUserAppsBySearchWord(query);
		return allUserUserApps;
	}

	/**
	 * 处理用户平台用户应用选中
	 */
	public void handleUserUserAppIdSelect(SelectEvent event) {
		userUserAppId = Integer.parseInt(event.getObject().toString());
	}

	/**
	 * 保存
	 */
	public void saveAction() {
		UserApp userApp = getUserApp();
		if (userApp.getUserAppId() == null) {

			// 获取用户应用
			UserApp userAppTemp = userAppService.loadById(userUserAppId);
			if (userAppTemp != null) {
				addValidatingMessage("所选用户应用已存在！");
				return;
			}
			com.cccollector.universal.user.model.UserApp userUserApp = null;
			for (int i = 0; i < allUserUserApps.size(); i++) {
				userUserApp = allUserUserApps.get(i);
				if (userUserApp.getUserAppId().intValue() == userUserAppId) {
					break;
				}
			}

			// 获取应用
			App app = appService.loadById(userUserApp.getApp().getAppId());
			if (app == null) {
				addValidatingMessage("所选用户应用对应的应用不存在！");
				return;
			}
			
			// 获取用户
			User user = userService.loadById(userUserApp.getUser().getUserId());
			
			// 添加用户应用
			userAppService.addUserApp(userUserApp, userApp, user, app);
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("userAppList.xhtml?multiSelect=true");
		}
	}
}
