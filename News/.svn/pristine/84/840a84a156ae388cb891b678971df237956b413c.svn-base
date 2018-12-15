/**
 * 
 */
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 站点版本类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "site_siteVersions")
public class SiteVersion implements Serializable {

	private static final long serialVersionUID = -8218295461884807460L;

	private Integer _siteVersionId;

	/**
	 * 站点版本ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getSiteVersionId() {
		return _siteVersionId;
	}

	public void setSiteVersionId(Integer siteVersionId) {
		_siteVersionId = siteVersionId;
	}

	/**
	 * 站点版本文件路径
	 */
	@JsonIgnore
	@Transient
	public String getSiteVersionFilePathWithSite() {
		return getSite().getSiteFilePath() + _version + File.separator;
	}

	/**
	 * 站点版本URL路径
	 */
	@JsonIgnore
	@Transient
	public String getSiteVersionUrlPath() {
		return _version + "/";
	}

	/**
	 * 站点版本文件路径
	 */
	@JsonIgnore
	@Transient
	public String getSiteVersionFilePath() {
		return _version + File.separator;
	}

	private Integer _version;

	/**
	 * 版本号
	 */
	@Column(nullable = false)
	public Integer getVersion() {
		return _version;
	}

	public void setVersion(Integer version) {
		_version = version;
	}

	/**
	 * 名称
	 */
	@JsonIgnore
	@Transient
	public String getName() {
		if (_version == null) {
			return _site.getName();
		} else {
			return _site.getName() + "_" + _version + "版";
		}
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

	private Integer _status;

	/**
	 * 状态
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Integer getStatus() {
		return _status;
	}

	public void setStatus(Integer status) {
		_status = status;
	}

	/**
	 * 状态枚举
	 */
	public static enum StatusEnum {
		制作中, 测试中, 已发布, 待撤销, 已撤销
	}

	/**
	 * 状态枚举数组
	 */
	@JsonIgnore
	@Transient
	public StatusEnum[] getStatusEnums() {
		return StatusEnum.values();
	}

	/**
	 * 状态的模版枚举
	 */
	@JsonIgnore
	@Transient
	public StatusEnum getStatusEnum() {
		return StatusEnum.values()[_status];
	}

	private Site _site;

	/**
	 * 所属的站点
	 */
	@ManyToOne
	@JoinColumn(name = "siteId", nullable = false)
	public Site getSite() {
		return _site;
	}

	public void setSite(Site site) {
		_site = site;
	}

	private List<ColumnReplacement> _columnReplacements = new ArrayList<ColumnReplacement>();

	/**
	 * 包含的栏目替代
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "siteVersion")
	@OrderBy("position")
	public List<ColumnReplacement> getColumnReplacements() {
		return _columnReplacements;
	}

	public void setColumnReplacements(List<ColumnReplacement> columnReplacements) {
		_columnReplacements = columnReplacements;
	}

	private List<Template> _templates = new ArrayList<Template>();

	/**
	 * 包含的模板
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "siteVersion")
	@OrderBy("position")
	public List<Template> getTemplates() {
		return _templates;
	}

	public void setTemplates(List<Template> templates) {
		_templates = templates;
	}

	private List<StaticPage> _staticPages = new ArrayList<StaticPage>();

	/**
	 * 包含的静态化页面
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "siteVersion")
	@OrderBy("staticTime")
	public List<StaticPage> getStaticPages() {
		return _staticPages;
	}

	public void setStaticPages(List<StaticPage> staticPages) {
		_staticPages = staticPages;
	}
}
