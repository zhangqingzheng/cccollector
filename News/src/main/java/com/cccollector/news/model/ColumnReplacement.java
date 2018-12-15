/**
 * 
 */
package com.cccollector.news.model;

import java.io.Serializable;

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
 * 栏目替代类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "site_columnReplacements")
public class ColumnReplacement implements Serializable {
	
	
	private static final long serialVersionUID = -4939838093404121320L;
	
	private Integer _columnReplacementId;

	/**
	 * 栏目替代ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getColumnReplacementId() {
		return _columnReplacementId;
	}

	public void setColumnReplacementId(Integer columnReplacementId) {
		_columnReplacementId = columnReplacementId;
	}

	private String _name;

	/**
	 * 名称
	 */
	@javax.persistence.Column(nullable = false, length = 100)
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}
	
	private Column _sourceColumn;

	/**
	 * 源栏目
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "sourceColumnId", nullable = false)
	public Column getSourceColumn() {
		return _sourceColumn;
	}

	public void setSourceColumn(Column sourceColumn) {
		_sourceColumn = sourceColumn;
	}

	private Integer _sourceColumnId;

	/**
	 * 源栏目ID
	 */	
	@Transient
	public Integer getSourceColumnId() {
		return _sourceColumnId;
	}

	public void setSourceColumnId(Integer sourceColumnId) {
		_sourceColumnId = sourceColumnId;
	}

	private Column _targetColumn;
	
	/**
	 * 目标栏目
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "targetColumnId", nullable = false)
	public Column getTargetColumn() {
		return _targetColumn;
	}
	
	public void setTargetColumn(Column targetColumn) {
		_targetColumn = targetColumn;
	}
	
	private Integer _targetColumnId;
	
	/**
	 * 目标栏目ID
	 */	
	@Transient
	public Integer getTargetColumnId() {
		return _targetColumnId;
	}
	
	public void setTargetColumnId(Integer targetColumnId) {
		_targetColumnId = targetColumnId;
	}
	
	private Integer _mode;

	/**
	 * 方式
	 */
	@javax.persistence.Column(nullable = false)
	public Integer getMode() {
		return _mode;
	}

	public void setMode(Integer mode) {
		_mode = mode;
	}

	/**
	 * 方式枚举
	 */
	public static enum ModeEnum {
		引用,
		在之前添加,
		在之后添加,
		替换,
		移除
	}

	/**
	 * 方式枚举数组
	 */
	@JsonIgnore
	@Transient
	public ModeEnum[] getModeEnums() {
		return ModeEnum.values();
	}

	/**
	 * 方式的枚举
	 */
	@JsonIgnore
	@Transient
	public ModeEnum getModeEnum() {
		return ModeEnum.values()[_mode];
	}

	private Boolean _enabled;

	/**
	 * 是否可用
	 */
	@JsonIgnore
	@javax.persistence.Column(nullable = false)
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
	@javax.persistence.Column(nullable = false)
	public Integer getPosition() {
		return _position;
	}

	public void setPosition(Integer position) {
		_position = position;
	}

	
	private SiteVersion _siteVersion;

	/**
	 * 所属的站点版本
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "siteVersionId", nullable = false)
	public SiteVersion getSiteVersion() {
		return _siteVersion;
	}

	public void setSiteVersion(SiteVersion siteVersion) {
		_siteVersion = siteVersion;
	}
}
