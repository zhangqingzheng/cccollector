/**
 * 
 */
package com.cccollector.app.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cccollector.universal.model.UniversalJsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 发行类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "app_releases")
public class Release implements Serializable {
	
	private static final long serialVersionUID = -87759019519460297L;
	
	private Integer _releaseId;

	/**
	 * 发行ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getReleaseId() {
		return _releaseId;
	}

	public void setReleaseId(Integer releaseId) {
		_releaseId = releaseId;
	}
	
	/**
	 * 发行URL路径
	 */
	@JsonIgnore
	@Transient
	public String getReleaseUrlPath() {
		return "releases/" + _releaseId + "/";
	}

	/**
	 * 发行文件路径
	 */
	@JsonIgnore
	@Transient
	public String getReleaseFilePath() {
		return "releases" + File.separator + _releaseId + File.separator;
	}

	/**
	 * 显示名称
	 */
	@JsonIgnore
	@Transient
	public String getShowName() {
		return getEdition().getShowName() + _version;
	}

	private Date _releaseDate;

	/**
	 * 发行日期
	 */
	@Column(nullable = false)
	public Date getReleaseDate() {
		return _releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		_releaseDate = releaseDate;
	}

	private String _version;

	/**
	 * 版本号
	 */
	@Column(nullable = false, length = 10)
	public String getVersion() {
		return _version;
	}

	public void setVersion(String version) {
		_version = version;
	}

	private String _osVersion;

	/**
	 * 操作系统最小版本号
	 */
	@Column(nullable = false, length = 10)
	public String getOsVersion() {
		return _osVersion;
	}

	public void setOsVersion(String osVersion) {
		_osVersion = osVersion;
	}

	private String _secretKey;	

	/**
	 * 密钥
	 */
	@JsonIgnore
	@Column(nullable = true, length = 32)
	public String getSecretKey() {
		return _secretKey;
	}

	public void setSecretKey(String secretKey) {
		_secretKey = secretKey;
	}	

	private Integer _status;

	/**
	 * 状态
	 */
	@JsonView(UniversalJsonViews.Back.class)
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
		未发布,
		已发布,
		已废弃
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
	 * 状态的枚举
	 */
	@JsonIgnore
	@Transient
	public StatusEnum getStatusEnum() {
		return StatusEnum.values()[_status];
	}	
	
	private Edition _edition;

	/**
	 * 所属的版本
	 */
	@JsonView(UniversalJsonViews.Front.class)
	@ManyToOne
	@JoinColumn(name = "editionId", nullable = false)
	public Edition getEdition() {
		return _edition;
	}

	public void setEdition(Edition edition) {
		_edition = edition;
	}
	
	private List<Resource> _resources = new ArrayList<Resource>();
	
	/**
	 * 包含的资源
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "release")
	@OrderBy("platformId, type, position")
	public List<Resource> getResources() {
		return _resources;
	}

	public void setResources(List<Resource> resources) {
		_resources = resources;
	}		
	
	private Map<Integer, Map<Integer, List<Resource>>> _platformResources = new HashMap<>();
	
	/**
	 * 包含的平台资源
	 */
	@JsonView(UniversalJsonViews.Front.class)
	@Transient
	public Map<Integer, Map<Integer, List<Resource>>> getPlatformResources() {
		return _platformResources;
	}

	public void setPlatformResources(Map<Integer, Map<Integer, List<Resource>>> platformResources) {
		_platformResources = platformResources;
	}	
	
	private Binary _binary;
	
	/**
	 * 发行对应的二进制文件
	 */
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "release")
	public Binary getBinary() {
		return _binary;
	}

	public void setBinary(Binary binary) {
		_binary = binary;
	}
}
