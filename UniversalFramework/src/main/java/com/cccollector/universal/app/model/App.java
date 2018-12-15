/**
 * 
 */
package com.cccollector.universal.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用类
 *
 * @author 谢朋
 * Copyright © 2017年 北京华星成汇文化发展有限公司. All rights reserved.
*/
public class App implements Serializable {
	
	private static final long serialVersionUID = 8547867083829217624L;
	
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

	private Boolean _enabled;

	/**
	 * 是否可用
	 */
	public Boolean getEnabled() {
		return _enabled;
	}

	public void setEnabled(Boolean enabled) {
		_enabled = enabled;
	}
	
	private List<Edition> _editions = new ArrayList<Edition>();

	/**
	 * 包含的版本
	 */
	public List<Edition> getEditions() {
		return _editions;
	}

	public void setEditions(List<Edition> editions) {
		_editions = editions;
	}
}
