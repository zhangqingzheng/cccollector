/**
 * 
 */
package com.cccollector.news.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 新闻源类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "news_newsSources")
public class NewsSource implements Serializable {
	
	private static final long serialVersionUID = -6275159355165828481L;
	
	private Integer _newsSourceId;

	/**
	 * 新闻源ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getNewsSourceId() {
		return _newsSourceId;
	}

	public void setNewsSourceId(Integer newsSourceId) {
		_newsSourceId = newsSourceId;
	}

	/**
	 * 新闻源URL路径
	 */
	@JsonIgnore
	@Transient
	public String getNewsSourceUrlPath() {
		return "newsSources/" + _newsSourceId + "/";
	}

	/**
	 * 新闻源文件路径
	 */
	@JsonIgnore
	@Transient
	public String getNewsSourceFilePath() {
		return "newsSources" + File.separator + _newsSourceId + File.separator;
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

	private String _remarks;

	/**
	 * 备注
	 */
	@JsonIgnore
	@javax.persistence.Column(nullable = true, length = 1000)
	public String getRemarks() {
		return _remarks;
	}

	public void setRemarks(String remarks) {
		_remarks = remarks;
	}

	/**
	 * 是否可用枚举
	 */
	public static enum EnabledEnum {
		否,
		是
	}

	private Boolean _reviewEnabled;

	/**
	 * 是否开启审稿
	 */
	@JsonIgnore
	@javax.persistence.Column(nullable = false)
	public Boolean getReviewEnabled() {
		return _reviewEnabled;
	}

	public void setReviewEnabled(Boolean reviewEnabled) {
		_reviewEnabled = reviewEnabled;
	}

	/**
	 * 是否开启审稿的是否可用枚举
	 */
	@JsonIgnore
	@Transient
	public EnabledEnum getReviewEnabledEnum() {
		int index = _reviewEnabled ? 1 : 0;
		return EnabledEnum.values()[index];
	}

	private Boolean _moderateEnabled;

	/**
	 * 是否开启审核评论
	 */
	@JsonIgnore
	@javax.persistence.Column(nullable = false)
	public Boolean getModerateEnabled() {
		return _moderateEnabled;
	}

	public void setModerateEnabled(Boolean moderateEnabled) {
		_moderateEnabled = moderateEnabled;
	}

	/**
	 * 是否开启审核评论的是否可用枚举
	 */
	@JsonIgnore
	@Transient
	public EnabledEnum getModerateEnabledEnum() {
		int index = _moderateEnabled ? 1 : 0;
		return EnabledEnum.values()[index];
	}

	private Date _defaultSchedule;

	/**
	 * 默认定时发布时间
	 */
	@JsonIgnore
	@javax.persistence.Column(nullable = true)
	public Date getDefaultSchedule() {
		return _defaultSchedule;
	}

	public void setDefaultSchedule(Date defaultSchedule) {
		_defaultSchedule = defaultSchedule;
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
	 * 是否可用的是否可用枚举
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

	private List<App> _apps = new ArrayList<App>();

	/**
	 * 关联的应用
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "newsSources")
	@OrderBy("name")
	public List<App> getApps() {
		return _apps;
	}

	public void setApps(List<App> apps) {
		_apps = apps;
	}

	private List<Column> _columns = new ArrayList<Column>();
	
	/**
	 * 包含的栏目
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "newsSource")
	@OrderBy("code")
	public List<Column> getColumns() {
		return _columns;
	}

	public void setColumns(List<Column> columns) {
		_columns = columns;
	}

	private List<Article> _articles = new ArrayList<Article>();

	/**
	 * 包含的文章
	 */
	@JsonIgnore		
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "newsSource")
	@OrderBy("articleId DESC")
	public List<Article> getArticles() {
		return _articles;
	}
	
	public void setArticles(List<Article> articles) {
		_articles = articles;
	}

	private Map<Integer, Style> _styles = new HashMap<Integer, Style>();
	
	/**
	 * 包含的样式
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "newsSource")
	@MapKeyColumn(name ="templateType")
	public Map<Integer, Style> getStyles() {
		return _styles;
	}
	
	public void setStyles(Map<Integer, Style> styles) {
		_styles = styles;
	}

	private List<Glossary> _glossaries = new ArrayList<Glossary>();

	/**
	 * 包含的术语
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "newsSource")
	@OrderBy("glossaryId DESC")
	public List<Glossary> getGlossaries() {
		return _glossaries;
	}
	
	public void setGlossaries(List<Glossary> glossaries) {
		_glossaries = glossaries;
	}

	private List<CategoryTag> _categoryTags = new ArrayList<CategoryTag>();
	
	/**
	 * 包含的分类标签
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "newsSource")
	@OrderBy("categoryTagId")
	public List<CategoryTag> getCategoryTags() {
		return _categoryTags;
	}
	
	public void setCategoryTags(List<CategoryTag> categoryTags) {
		_categoryTags = categoryTags;
	}

	private List<RecommendGroup> _recommendGroups = new ArrayList<RecommendGroup>();
	
	/**
	 * 包含的推荐组
	 */
	@JsonIgnore	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "newsSource")
	@OrderBy("ownerType, ownerId, position")
	public List<RecommendGroup> getRecommendGroups() {
		return _recommendGroups;
	}
	
	public void setRecommendGroups(List<RecommendGroup> recommendGroups) {
		_recommendGroups = recommendGroups;
	}
	
	private List<Site> _sites = new ArrayList<Site>();

	/**
	 * 关联的站点
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "newsSources")
	@OrderBy("name")
	public List<Site> getSites() {
		return _sites;
	}

	public void setSites(List<Site> sites) {
		_sites = sites;
	}
}
