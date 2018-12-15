/**
 * 
 */
package com.cccollector.news.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 应用类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "app_apps")
public class App implements Serializable {

	private static final long serialVersionUID = -6984814890177516894L;

	private Integer _appId;

	/**
	 * 应用ID
	 */
	@Id
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
	@Column(nullable = false, length = 100)
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
	@Column(nullable = false, length = 100)
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
	@JsonIgnore
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
	@JsonIgnore
	@Transient
	public EnabledEnum getEnabledEnum() {
		int index = _enabled ? 1 : 0;
		return EnabledEnum.values()[index];
	}

	private Date _syncTime;

	/**
	 * 同步时间
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Date getSyncTime() {
		return _syncTime;
	}

	public void setSyncTime(Date syncTime) {
		_syncTime = syncTime;
	}

	private List<Edition> _editions = new ArrayList<Edition>();

	/**
	 * 包含的版本
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "app")
	@OrderBy("osType")
	public List<Edition> getEditions() {
		return _editions;
	}

	public void setEditions(List<Edition> editions) {
		_editions = editions;
	}

	private List<NewsSource> _newsSources = new ArrayList<NewsSource>();

	/**
	 * 关联的新闻源
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "app_appNewsSources", joinColumns = @JoinColumn(name = "appId", nullable = false), inverseJoinColumns = @JoinColumn(name = "newsSourceId", nullable = false))
	@OrderColumn(name = "position")
	public List<NewsSource> getNewsSources() {
		return _newsSources;
	}

	public void setNewsSources(List<NewsSource> newsSources) {
		_newsSources = newsSources;
	}

	private List<UserApp> _userApps = new ArrayList<UserApp>();

	/**
	 * 包含的用户应用
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "app")
	@OrderBy("createTime DESC")
	public List<UserApp> getUserApps() {
		return _userApps;
	}

	public void setUserApps(List<UserApp> userApps) {
		_userApps = userApps;
	}
	
	private List<ColumnSubstitute> _columnSubstitutes = new ArrayList<ColumnSubstitute>();
	
	/**
	 * 包含的栏目替身
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "app")
	@OrderBy("position")
	public List<ColumnSubstitute> getColumnSubstitutes() {
		return _columnSubstitutes;
	}
	
	public void setColumnSubstitutes(List<ColumnSubstitute> columnSubstitutes) {
		_columnSubstitutes = columnSubstitutes;
	}
}
