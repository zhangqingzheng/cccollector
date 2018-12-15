/**
 * 
 */
package com.cccollector.news.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
 * 网络文件夹类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "site_webFolders")
public class WebFolder implements Serializable {

	private static final long serialVersionUID = 5074187651257473661L;
	
	private Integer _webFolderId;

	/**
	 * 网络文件夹ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getWebFolderId() {
		return _webFolderId;
	}

	public void setWebFolderId(Integer webFolderId) {
		_webFolderId = webFolderId;
	}
	
	/**
	 * 网络文件夹URL路径
	 */
	@JsonIgnore
	@Transient
	public String getWebFolderUrlPath() {
		return getSiteVersion().getSiteVersionUrlPath() + "webPages/" + _webName + "/";
	}

	/**
	 * 网络文件夹文件路径
	 */
	@JsonIgnore
	@Transient
	public String getWebFolderFilePath() {
		return getSiteVersion().getSiteVersionFilePathWithSite() + "webPages" + File.separator + _webName + File.separator;
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
	 * 带级别名称
	 */
	@JsonIgnore
	@Transient
	public String getNameWithLevel() {
		String nameWithLevel = _name;
		if (_code != null) {
			int level = _code.split(":").length;
			for (int i = 0; i < level - 1; i++) {
				nameWithLevel = " " + nameWithLevel;
			}
		}
		return nameWithLevel;
	}
	
	private String _webName;

	/**
	 * 网络名称
	 */
	@Column(nullable = false, length = 100)
	public String getWebName() {
		return _webName;
	}

	public void setWebName(String webName) {
		_webName = webName;
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

	private String _code;

	/**
	 * 排序代码
	 */
	@Column(nullable = false, length = 100)
	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		_code = code;
	}
	
	private Date _updateTime;

	/**
	 * 更新时间
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Date getUpdateTime() {
		return _updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		_updateTime = updateTime;
	}
	
	private Date _publishTime;

	/**
	 * 发布时间
	 */
	@JsonIgnore
	@Column(nullable = true)
	public Date getPublishTime() {
		return _publishTime;
	}

	public void setPublishTime(Date publishTime) {
		_publishTime = publishTime;
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
		制作中,
		已发布,
		已撤销
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

	private WebFolder _parentWebFolder;

	/**
	 * 所属的父网络文件夹
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "parentWebFolderId", nullable = true)
	public WebFolder getParentWebFolder() {
		return _parentWebFolder;
	}

	public void setParentWebFolder(WebFolder parentWebFolder) {
		_parentWebFolder = parentWebFolder;
	}

	private Integer _parentWebFolderId;

	/**
	 * 所属的父网络文件夹ID
	 */	
	@Transient
	public Integer getParentWebFolderId() {
		return _parentWebFolderId;
	}

	public void setParentWebFolderId(Integer parentWebFolderId) {
		_parentWebFolderId = parentWebFolderId;
	}

	private List<WebFolder> _childWebFolders = new ArrayList<WebFolder>();

	/**
	 * 包含的子网络文件夹
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentWebFolder")
	@OrderBy("position")
	public List<WebFolder> getChildWebFolders() {
		return _childWebFolders;
	}

	public void setChildWebFolders(List<WebFolder> childWebFolders) {
		_childWebFolders = childWebFolders;
	}

	private SiteVersion _siteVersion;

	/**
	 * 所属的站点版本
	 */
	@ManyToOne
	@JoinColumn(name = "siteVersionId", nullable = false)
	public SiteVersion getSiteVersion() {
		return _siteVersion;
	}

	public void setSiteVersion(SiteVersion siteVersion) {
		_siteVersion = siteVersion;
	}

	private List<WebPage> _webPages = new ArrayList<WebPage>();
	
	/**
	 * 包含的网络页面
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "webFolder")
	public List<WebPage> getWebPages() {
		return _webPages;
	}

	public void setWebPages(List<WebPage> webPages) {
		_webPages = webPages;
	}
}
