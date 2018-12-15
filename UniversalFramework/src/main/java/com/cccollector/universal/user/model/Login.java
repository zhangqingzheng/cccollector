/**
 * 
 */
package com.cccollector.universal.user.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 登录类
 *
 * @author 杨彪
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Login implements Serializable {

	private static final long serialVersionUID = -4591641018756223499L;

	private Integer _loginId;

	public Integer getLoginId() {
		return _loginId;
	}

	public void setLoginId(Integer loginId) {
		_loginId = loginId;
	}

	private Integer _type;

	/**
	 * 类别
	 */
	public Integer getType() {
		return _type;
	}

	public void setType(Integer type) {
		_type = type;
	}

	private String _account;

	/**
	 * 账号
	 */
	public String getAccount() {
		return _account;
	}

	public void setAccount(String account) {
		_account = account;
	}

	private Integer _userId;

	/**
	 * 用户ID
	 */
	public Integer getUserId() {
		return _userId;
	}

	public void setUserId(Integer userId) {
		_userId = userId;
	}

	private String _accessIp;

	/**
	 * 访问IP
	 */
	public String getAccessIp() {
		return _accessIp;
	}

	public void setAccessIp(String accessIp) {
		_accessIp = accessIp;
	}

	private Integer _appId;

	/**
	 * 登录应用ID
	 */
	public Integer getAppId() {
		return _appId;
	}

	public void setAppId(Integer appId) {
		_appId = appId;
	}

	private String _appName;

	/**
	 * 登录应用名称
	 */
	public String getAppName() {
		return _appName;
	}

	public void setAppName(String appName) {
		_appName = appName;
	}

	private String _loginToken;

	/**
	 * 登录令牌
	 */
	public String getLoginToken() {
		return _loginToken;
	}

	public void setLoginToken(String loginToken) {
		_loginToken = loginToken;
	}

	private Integer _loginTime;

	/**
	 * 登录令牌生成时间
	 */
	public Integer getLoginTime() {
		return _loginTime;
	}

	public void setLoginTime(Integer loginTime) {
		_loginTime = loginTime;
	}

	private String _accessToken;

	/**
	 * 访问令牌
	 */
	public String getAccessToken() {
		return _accessToken;
	}

	public void setAccessToken(String accessToken) {
		_accessToken = accessToken;
	}

	private Integer _accessTime;

	/**
	 * 访问令牌生成时间
	 */
	public Integer getAccessTime() {
		return _accessTime;
	}

	public void setAccessTime(Integer accessTime) {
		_accessTime = accessTime;
	}

	private User _user;

	/**
	 * 对应的用户
	 */
	public User getUser() {
		return _user;
	}

	public void setUser(User user) {
		_user = user;
	}

	private UserApp _userApp;

	/**
	 * 对应的用户应用
	 */
	public UserApp getUserApp() {
		return _userApp;
	}

	public void setUserApp(UserApp userApp) {
		_userApp = userApp;
	}
}
