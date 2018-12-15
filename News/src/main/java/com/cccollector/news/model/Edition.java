/**
 * 
 */
package com.cccollector.news.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 版本类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "app_editions")
public class Edition implements Serializable {

	private static final long serialVersionUID = -417679216014601273L;
	
	private Integer _editionId;

	/**
	 * 版本ID
	 */
	@Id
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
	@Column(nullable = false, length = 100)
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	/**
	 * 显示名称
	 */
	@JsonIgnore
	@Transient
	public String getShowName() {
		return getApp().getName() + _name;
	}

	private String _bundleId;

	/**
	 * 标识符
	 */
	@Column(nullable = false, length = 100)
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
	@Column(nullable = false)
	public Integer getOsType() {
		return _osType;
	}

	public void setOsType(Integer osType) {
		_osType = osType;
	}

	/**
	 * 操作系统类别枚举
	 */
	public static enum OsTypeEnum {
		苹果,
		谷歌,
		微软
	}

	/**
	 * 操作系统类别枚举数组
	 */
	@Transient
	public OsTypeEnum[] getOsTypeEnums() {
		return OsTypeEnum.values();
	}

	/**
	 * 操作系统类别的枚举
	 */
	@Transient
	public OsTypeEnum getOsTypeEnum() {
		return OsTypeEnum.values()[_osType];
	}

	private Boolean _enabled;

	/**
	 * 是否可用
	 */
	@Column(nullable = false)
	public Boolean getEnabled() {
		return _enabled;
	}

	public void setEnabled(Boolean enabled) {
		_enabled = enabled;
	}

	/**
	 * 是否可用枚举
	 */
	public static enum EnabledEnum {
		否,
		是
	}

	/**
	 * 是否可用的枚举
	 */
	@Transient
	public EnabledEnum getEnabledEnum() {
		int index = _enabled ? 1 : 0;
		return EnabledEnum.values()[index];
	}

	private App _app;

	/**
	 * 所属的应用
	 */
	@ManyToOne
	@JoinColumn(name = "appId", nullable = false)
	public App getApp() {
		return _app;
	}

	public void setApp(App app) {
		_app = app;
	}

	private List<Release> _releases = new ArrayList<Release>();

	/**
	 * 包含的发行
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "edition")
	@OrderBy("releaseDate DESC")
	public List<Release> getReleases() {
		return _releases;
	}

	public void setReleases(List<Release> release) {
		_releases = release;
	}
}
