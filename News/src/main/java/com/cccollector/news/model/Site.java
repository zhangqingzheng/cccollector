package com.cccollector.news.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * 站点类
 * 
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "site_sites")
public class Site implements Serializable {

	private static final long serialVersionUID = 8343374526078339664L;
	
	private Integer _siteId;
	
	/**
	 * 站点ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getSiteId() {
		return _siteId;
	}
	
	public void setSiteId(Integer siteId) {
		_siteId = siteId;
	}
	
	/**
	 * 站点文件路径
	 */
	@JsonIgnore
	@Transient
	public String getSiteFilePath() {
		return "sites" + File.separator + _siteId + File.separator;
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

	private String _webAddress;
	
	/**
	 * 网站地址
	 */
	@Column(nullable = false, length = 100)
	public String getWebAddress() {
		return _webAddress;
	}
	
	public void setWebAddress(String webAddress) {
		_webAddress = webAddress;
	}
	
	private String _rootPath;
	
	/**
	 * 根路径
	 */
	@Column(nullable = false, length = 100)
	public String getRootPath() {
		return _rootPath;
	}
	
	public void setRootPath(String rootPath) {
		_rootPath = rootPath;
	}
	
	private String _remarks;
	
	/**
	 * 备注
	 */
	@Column(nullable = true, length = 1000)
	public String getRemarks() {
		return _remarks;
	}
	
	public void setRemarks(String remarks) {
		_remarks = remarks;
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
	
	private Integer _position;
	
	/**
	 * 排序位置
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Integer getPosition() {
		return _position;
	}
	
	public void setPosition(Integer position) {
		_position = position;
	}
	
	private List<SiteVersion> _siteVersions = new ArrayList<SiteVersion>();

	/**
	 * 包含的站点版本
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
	@OrderBy("version")
	public List<SiteVersion> getSiteVersions() {
		return _siteVersions;
	}

	public void setSiteVersions(List<SiteVersion> siteVersions) {
		_siteVersions = siteVersions;
	}

	private List<NewsSource> _newsSources = new ArrayList<NewsSource>();

	/**
	 * 关联的新闻源
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "site_siteNewsSources", joinColumns = @JoinColumn(name = "siteId", nullable = false), inverseJoinColumns = @JoinColumn(name = "newsSourceId", nullable = false))
	@OrderColumn(name = "position")
	public List<NewsSource> getNewsSources() {
		return _newsSources;
	}

	public void setNewsSources(List<NewsSource> newsSources) {
		_newsSources = newsSources;
	}
}
