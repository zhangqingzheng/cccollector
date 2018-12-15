/**
 * 
 */
package com.cccollector.universal.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 版本类
 *
 * @author 谢朋
 * Copyright © 2017年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class Edition implements Serializable {
	
	private static final long serialVersionUID = 5651001583285565153L;
	
	private Integer _editionId;

	/**
	 * 版本ID
	 */
	public Integer getEditionId() {
		return _editionId;
	}

	public void setEditionId(Integer editionId) {
		_editionId = editionId;
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

	private Integer _osType;

	/**
	 * 操作系统类别
	 */
	public Integer getOsType() {
		return _osType;
	}

	public void setOsType(Integer osType) {
		_osType = osType;
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

	private List<Release> _releases = new ArrayList<Release>();

	/**
	 * 包含的发行
	 */
	public List<Release> getReleases() {
		return _releases;
	}

	public void setReleases(List<Release> release) {
		_releases = release;
	}
}
