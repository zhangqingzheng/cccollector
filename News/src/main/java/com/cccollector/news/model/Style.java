/**
 * 
 */
package com.cccollector.news.model;

import java.io.File;
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
 * 样式类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "news_styles")
public class Style implements Serializable {
	
	private static final long serialVersionUID = 7461141858243717135L;
	
	private Integer _styleId;

	/**
	 * 样式ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getStyleId() {
		return _styleId;
	}

	public void setStyleId(Integer styleId) {
		_styleId = styleId;
	}

	/**
	 * 样式URL路径
	 */
	@JsonIgnore
	@Transient
	public String getStyleUrlPath() {
		return getNewsSource().getNewsSourceUrlPath() + "styles/";
	}

	/**
	 * 样式文件路径
	 */
	@JsonIgnore
	@Transient
	public String getStyleFilePath() {
		return getNewsSource().getNewsSourceFilePath() + "styles" + File.separator;
	}

	/**
	 * CSSURL路径
	 */
	public String cssUrlPath() {
		return getStyleUrlPath() + _styleId + "_style.css";
	}
	
	/**
	 * CSS文件路径
	 */
	public String cssFilePath() {
		return getStyleFilePath() + _styleId + "_style.css";
	}
	
	/**
	 * 简化CSS文件路径
	 */
	public String simplifyCssFilePath() {
		return _styleId + "_style.css";
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

	private Integer _templateType;

	/**
	 * 模版类别
	 */
	@Column(nullable = false)
	public Integer getTemplateType() {
		return _templateType;
	}

	public void setTemplateType(Integer templateType) {
		_templateType = templateType;
	}

	/**
	 * 模版类别枚举
	 */
	public static enum TemplateTypeEnum {
		图文混排,
		图文分离,
		图集,
		杂志,
		图书,
		顶部悬浮音频,
		顶部悬浮视频
	}

	/**
	 * 模版类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public TemplateTypeEnum[] getTemplateTypeEnums() {
		return TemplateTypeEnum.values();
	}

	/**
	 * 模版类别的枚举
	 */
	@JsonIgnore
	@Transient
	public TemplateTypeEnum getTemplateTypeEnum() {
		return TemplateTypeEnum.values()[_templateType];
	}

	private String _styleSheet;

	/**
	 * 样式表
	 */
	@Column(nullable = false)
	public String getStyleSheet() {
		return _styleSheet;
	}

	public void setStyleSheet(String styleSheet) {
		_styleSheet = styleSheet;
	}
	
	private NewsSource _newsSource;
	
	/**
	 * 所属的新闻源
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "newsSourceId", nullable = false)
	public NewsSource getNewsSource() {
		return _newsSource;
	}

	public void setNewsSource(NewsSource newsSource) {
		_newsSource = newsSource;
	}
}
