/**
 * 
 */
package com.cccollector.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 资源类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "app_resources")
public class Resource implements Serializable {
	
	private static final long serialVersionUID = -4591641018756223499L;
	
	private Integer _resourceId;

	/**
	 * 资源ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getResourceId() {
		return _resourceId;
	}

	public void setResourceId(Integer resourceId) {
		_resourceId = resourceId;
	}

	private Integer _platformId;	
	
	/**
	 * 对应平台ID
	 */
	@Column(nullable = false)
	public Integer getPlatformId() {
		return _platformId;
	}

	public void setPlatformId(Integer platformId) {
		_platformId = platformId;
	}	

	private Platform _platform;
	
	/**
	 * 对应平台
	 */
	@JsonIgnore
	@Transient
	public Platform getPlatform() {
		return _platform;
	}
	
	public void setPlatform(Platform platform) {
		_platform = platform;
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
		接口,
		路径
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
	
	private String _address;

	/**
	 * 地址
	 */
	@Column(nullable = true, length = 200)
	public String getAddress() {
		return _address;
	}

	public void setAddress(String address) {
		_address = address;
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
	
	private Release _release;
	
	/**
	 * 所属的发行
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "releaseId", nullable = false)
	public Release getRelease() {
		return _release;
	}

	public void setRelease(Release release) {
		_release = release;
	}
}
