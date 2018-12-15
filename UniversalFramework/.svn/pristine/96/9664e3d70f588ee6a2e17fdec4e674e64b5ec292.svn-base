/**
 * 
 */
package com.cccollector.universal.subscription.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 杂志类
 *
 * @author 杨彪
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class Magazine implements Serializable {
	
	private static final long serialVersionUID = -608383170113775320L;
	
	private Integer _magazineId;

	/**
	 * 杂志ID
	 */
	public Integer getMagazineId() {
		return _magazineId;
	}

	public void setMagazineId(Integer magazineId) {
		_magazineId = magazineId;
	}

	/**
	 * 杂志URL路径
	 */
	public String getMagazineUrlPath() {
		return "magazines/" + _magazineId + "/";
	}

	/**
	 * 杂志文件路径
	 */
	public String getMagazineFilePath() {
		return "magazines" + File.separator + _magazineId + File.separator;
	}

	private String _name;

	/**
	 * 名称
	 */
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	private String _magazineIdentifier;
	
	/**
	 * 杂志标识符
	 */
	public String getMagazineIdentifier() {
		return _magazineIdentifier;
	}

	public void setMagazineIdentifier(String magazineIdentifier) {
		_magazineIdentifier = magazineIdentifier;
	}
	
	private Integer _language;

	/**
	 * 语言
	 */
	public Integer getLanguage() {
		return _language;
	}

	public void setLanguage(Integer language) {
		_language = language;
	}

	/**
	 * 语言枚举
	 */
	public static enum LanguageEnum {
		简体中文,
		繁体中文,
		英文,
		法文,
		德文,
		日文,
		韩文,
		西班牙文
	}

	/**
	 * 语言枚举数组
	 */
	public LanguageEnum[] getLanguageEnums() {
		return LanguageEnum.values();
	}

	/**
	 * 语言的枚举
	 */
	public LanguageEnum getLanguageEnum() {
		return LanguageEnum.values()[_language];
	}
	
	private Integer _formats;

	/**
	 * 版式
	 */
	public Integer getFormats() {
		return _formats;
	}

	public void setFormats(Integer formats) {
		_formats = formats;
	}

	/**
	 * 版式枚举
	 */
	public static enum FormatsEnum {
		纸质,
		照排,
		网页,
		前端
	}

	/**
	 * 版式枚举数组
	 */
	public FormatsEnum[] getFormatsEnums() {
		return FormatsEnum.values();
	}

	/**
	 * 版式的版式枚举名称
	 */
	public String getFormatsEnumsString() {
		String formatsEnumsString = "";
		for (int i = 0; i < FormatsEnum.values().length; i++) {
			if ((_formats & (int) Math.pow(2, i)) > 0) {
				formatsEnumsString += FormatsEnum.values()[i].name() + " ";
			}
		}
		return formatsEnumsString;
	}
	
	private String _profile;
	
	/**
	 * 简介
	 */
	public String getProfile() {
		return _profile;
	}

	public void setProfile(String profile) {
		_profile = profile;
	}

	private List<Issue> _issues = new ArrayList<Issue>();
	
	/**
	 * 包含的期刊
	 */
	public List<Issue> getIssues() {
		return _issues;
	}

	public void setIssues(List<Issue> issues) {
		_issues = issues;
	}
	
	private List<Subscription> _subscriptions = new ArrayList<Subscription>();
	
	/**
	 * 包含的订阅
	 */
	public List<Subscription> getSubscriptions() {
		return _subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions) {
		_subscriptions = subscriptions;
	}
}
