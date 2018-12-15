/**
 * 
 */
package com.cccollector.news.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 发行类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "app_releases")
public class Release implements Serializable {
	
	private static final long serialVersionUID = 441313093316202912L;
	
	private Integer _releaseId;

	/**
	 * 发行ID
	 */
	@Id
	public Integer getReleaseId() {
		return _releaseId;
	}

	public void setReleaseId(Integer releaseId) {
		_releaseId = releaseId;
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

	private Integer _status;

	/**
	 * 状态
	 */
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
	 * 状态的枚举
	 */
	@Transient
	public StatusEnum getStatusEnum() {
		return StatusEnum.values()[_status];
	}	
	
	private Edition _edition;

	/**
	 * 所属的版本
	 */
	@ManyToOne
	@JoinColumn(name = "editionId", nullable = false)
	public Edition getEdition() {
		return _edition;
	}

	public void setEdition(Edition edition) {
		_edition = edition;
	}	
}
