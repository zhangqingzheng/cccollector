/**
 * 
 */
package com.cccollector.universal.user.model;

import java.io.Serializable;

import com.cccollector.universal.app.model.App;

/**
 * 用户应用类
 *
 * @author 谢朋
 * Copyright © 2017年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class UserApp implements Serializable {
	
	private static final long serialVersionUID = -8813425869381036993L;
	
	private Integer _userAppId;

	/**
	 * 用户应用ID
	 */
	public Integer getUserAppId() {
		return _userAppId;
	}

	public void setUserAppId(Integer userAppId) {
		_userAppId = userAppId;
	}
	
	private Integer _createTime;

	/**
	 * 创建时间
	 */
	public Integer getCreateTime() {
		return _createTime;
	}

	public void setCreateTime(Integer createTime) {
		_createTime = createTime;
	}

	private App _app;

	/**
	 * 应用
	 */
	public App getApp() {
		return _app;
	}

	public void setApp(App app) {
		_app = app;
	}
	
	private User _user;

	/**
	 * 所属的用户
	 */
	public User getUser() {
		return _user;
	}

	public void setUser(User user) {
		_user = user;
	}
}
