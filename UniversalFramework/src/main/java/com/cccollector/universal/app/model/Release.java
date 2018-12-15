/**
 * 
 */
package com.cccollector.universal.app.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 发行类
 *
 * @author 谢朋
 * Copyright © 2017年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class Release implements Serializable {
	
	private static final long serialVersionUID = -508928509114961226L;
	
	private Integer _releaseId;

	/**
	 * 发行ID
	 */
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
	public Integer getStatus() {
		return _status;
	}

	public void setStatus(Integer status) {
		_status = status;
	}
}
