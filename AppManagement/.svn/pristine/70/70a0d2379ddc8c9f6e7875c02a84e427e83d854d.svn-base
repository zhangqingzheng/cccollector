/**
 * 
 */
package com.cccollector.app.model;

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

import com.cccollector.universal.model.UniversalJsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 版本类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "app_editions")
public class Edition implements Serializable {

	private static final long serialVersionUID = 7611542552335354476L;
	
	private Integer _editionId;

	/**
	 * 版本ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	private String _remarks;

	/**
	 * 备注
	 */
	@JsonIgnore
	@Column(nullable = true, length = 1000)
	public String getRemarks() {
		return _remarks;
	}

	public void setRemarks(String remarks) {
		_remarks = remarks;
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
	@JsonIgnore
	@Transient
	public OsTypeEnum[] getOsTypeEnums() {
		return OsTypeEnum.values();
	}

	/**
	 * 操作系统类别的枚举
	 */
	@JsonIgnore
	@Transient
	public OsTypeEnum getOsTypeEnum() {
		return OsTypeEnum.values()[_osType];
	}

	private Integer _os;

	/**
	 * 操作系统
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Integer getOs() {
		return _os;
	}

	public void setOs(Integer os) {
		_os = os;
	}

	/**
	 * 苹果操作系统枚举
	 */
	public static enum Os0Enum {
		iOS,
		watchOS,
		tvOS,
		macOS
	}

	/**
	 * 苹果操作系统枚举数组
	 */
	@JsonIgnore
	@Transient
	public Os0Enum[] getOs0Enums() {
		return Os0Enum.values();
	}

	/**
	 * 谷歌操作系统枚举
	 */
	public static enum Os1Enum {
		Android,
		AndroidWear,
		AndroidTV
	}

	/**
	 * 谷歌操作系统枚举数组
	 */
	@JsonIgnore
	@Transient
	public Os1Enum[] getOs1Enums() {
		return Os1Enum.values();
	}

	/**
	 * 微软操作系统枚举
	 */
	public static enum Os2Enum {
		WindowsPhone
	}

	/**
	 * 微软操作系统枚举数组
	 */
	@JsonIgnore
	@Transient
	public Os2Enum[] getOs2Enums() {
		return Os2Enum.values();
	}

	private Integer _device;

	/**
	 * 设备
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Integer getDevice() {
		return _device;
	}

	public void setDevice(Integer device) {
		_device = device;
	}

	/**
	 * 苹果设备枚举
	 */
	public static enum Device0Enum {
		iPhone,
		iPad,
		AppleWatch,
		AppleTV,
		Mac
	}

	/**
	 * 苹果设备枚举数组
	 */
	@JsonIgnore
	@Transient
	public Device0Enum[] getDevice0Enums() {
		return Device0Enum.values();
	}

	/**
	 * 谷歌设备枚举
	 */
	public static enum Device1Enum {
		AndroidPhone,
		AndroidPad,
		AndroidWear,
		AndroidTV
	}

	/**
	 * 谷歌设备枚举数组
	 */
	@JsonIgnore
	@Transient
	public Device1Enum[] getDevice1Enums() {
		return Device1Enum.values();
	}

	/**
	 * 微软设备枚举
	 */
	public static enum Device2Enum {
		WindowsPhone,
		WindowsPad
	}

	/**
	 * 微软设备枚举数组
	 */
	@JsonIgnore
	@Transient
	public Device2Enum[] getDevice2Enums() {
		return Device2Enum.values();
	}

	private Boolean _enabled;

	/**
	 * 是否可用
	 */
	@JsonView(UniversalJsonViews.Back.class)
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

	private App _app;

	/**
	 * 所属的应用
	 */
	@JsonView(UniversalJsonViews.Front.class)
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
	@JsonView(UniversalJsonViews.Back.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "edition")
	@OrderBy("releaseDate DESC")
	public List<Release> getReleases() {
		return _releases;
	}

	public void setReleases(List<Release> release) {
		_releases = release;
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	private List _programs = new ArrayList();

	/**
	 * 关联的推广方案
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	@JsonView(UniversalJsonViews.FrontVersion2.class)
	@Transient
	public List getPrograms() {
		return _programs;
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	@Transient
	public void setPrograms(List programs) {
		_programs = programs;
	}
}