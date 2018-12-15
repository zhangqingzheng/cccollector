/**
 * 
 */
package com.cccollector.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 二进制文件类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "app_binaries")
public class Binary implements Serializable {
	
	private static final long serialVersionUID = -4241590221086146412L;
	
	private Integer _binaryId;

	/**
	 * 二进制文件ID
	 */
	@Id
	public Integer getBinaryId() {
		return _binaryId;
	}

	public void setBinaryId(Integer binaryId) {
		_binaryId = binaryId;
	}

	private Integer _type;

	/**
	 * 类别
	 */
	@Column(nullable = false)
	public Integer getType() {
		return _type;
	}

	public void setType(Integer type) {
		_type = type;
	}

	/**
	 * 类别枚举
	 */
	public static enum TypeEnum {
		APK,
		APP
	}

	/**
	 * 类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public TypeEnum[] getTypeEnums() {
		return TypeEnum.values();
	}

	/**
	 * 类别的枚举
	 */
	@JsonIgnore
	@Transient
	public TypeEnum getTypeEnum() {
		return TypeEnum.values()[_type];
	}	
	
	/**
	 * 二进制文件URL路径
	 */
	public String binaryUrlPath() {
		return getRelease().getReleaseUrlPath() + "binary." + getTypeEnum().name().toLowerCase();
	}
	
	/**
	 * 二进制文件文件路径
	 */
	public String binaryFilePath() {
		return getRelease().getReleaseFilePath() + "binary." + getTypeEnum().name().toLowerCase();
	}
	
	private Integer _fileSize;

	/**
	 * 文件大小
	 */
	@Column(nullable = false)
	public Integer getFileSize() {
		return _fileSize;
	}

	public void setFileSize(Integer fileSize) {
		_fileSize = fileSize;
	}	
	
	private String fileSizeFormatString(int fileSize) {
		double fileSizeKB = fileSize / 1024.0;
		double fileSizeMB = fileSizeKB / 1024.0;
		String formatString = String.format("%,d", fileSize);
		if (fileSizeMB >= 1) {
			formatString += " (" + String.format("%.2f", fileSizeMB) + "MB)";
		} else if (fileSizeKB >= 1) {
			formatString += " (" + String.format("%.2f", fileSizeKB) + "KB)";
		}
		return formatString;
	}

	/**
	 * 文件大小格式化字符串
	 */
	@JsonIgnore
	@Transient
	public String getFileSizeFormatString() {
		return fileSizeFormatString(_fileSize);
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
	
	private Release _release = new Release();
	
	/**
	 * 所属发行
	 */
	@OneToOne
	@JoinColumn(name = "releaseId", nullable = false)
	public Release getRelease() {
		return _release;
	}

	public void setRelease(Release release) {
		_release = release;
	}
}
