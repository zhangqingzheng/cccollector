/**
 * 
 */
package com.cccollector.universal.subscription.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用类
 *
 * @author 杨彪
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class App implements Serializable {
	
	private static final long serialVersionUID = -6819956203218735102L;
	
	private Integer _appId;

	/**
	 * 应用ID
	 */
	public Integer getAppId() {
		return _appId;
	}

	public void setAppId(Integer appId) {
		_appId = appId;
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

	private String _bundleId;

	/**
	 * 标识符
	 */
	public String getBundleId() {
		return _bundleId;
	}

	public void setBundleId(String bundleId) {
		_bundleId = bundleId;
	}

	private List<Magazine> _magazines = new ArrayList<Magazine>();

	/**
	 * 关联的杂志
	 */
	public List<Magazine> getMagazines() {
		return _magazines;
	}

	public void setMagazines(List<Magazine> magazines) {
		_magazines = magazines;
	}

	private List<Store> _stores = new ArrayList<Store>();

	/**
	 * 包含的商店
	 */
	public List<Store> getStores() {
		return _stores;
	}

	public void setStores(List<Store> stores) {
		_stores = stores;
	}	
}
