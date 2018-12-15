/**
 * 
 */
package com.cccollector.news.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 分类标签类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "tag_categoryTags")
public class CategoryTag implements Serializable {

	private static final long serialVersionUID = -3626112812704279504L;

	private Integer _categoryTagId;

	/**
	 * 分类标签ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getCategoryTagId() {
		return _categoryTagId;
	}

	public void setCategoryTagId(Integer categoryTagId) {
		_categoryTagId = categoryTagId;
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

	/**
	 * 带父分类标签名称
	 */
	@JsonIgnore
	@Transient
	public String getNameWithParents() {
		String nameWithParents = _name;
		CategoryTag parentCategoryTag = _parentCategoryTag;
		while (parentCategoryTag != null) {
			nameWithParents = parentCategoryTag.getName() + ":" + nameWithParents;
			parentCategoryTag = parentCategoryTag.getParentCategoryTag();
		}
		return nameWithParents;
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
	 * 是否可用枚举数组
	 */
	@JsonIgnore
	@Transient
	public EnabledEnum[] getEnabledEnums() {
		return EnabledEnum.values();
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

	private String _code;

	/**
	 * 排序代码
	 */
	@javax.persistence.Column(nullable = false, length = 100)
	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		_code = code;
	}

	private CategoryTag _parentCategoryTag;

	/**
	 * 所属的父分类标签
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "parentCategoryTagId", nullable = true)
	public CategoryTag getParentCategoryTag() {
		return _parentCategoryTag;
	}

	public void setParentCategoryTag(CategoryTag parentCategoryTag) {
		_parentCategoryTag = parentCategoryTag;
	}

	private Integer _parentCategoryTagId;

	/**
	 * 所属的父分类标签ID
	 */	
	@Transient
	public Integer getParentCategoryTagId() {
		return _parentCategoryTagId;
	}

	public void setParentCategoryTagId(Integer parentCategoryTagId) {
		_parentCategoryTagId = parentCategoryTagId;
	}

	private List<CategoryTag> _childCategoryTags = new ArrayList<CategoryTag>();

	/**
	 * 包含的子分类标签
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCategoryTag")
	@OrderBy("position")
	public List<CategoryTag> getChildCategoryTags() {
		return _childCategoryTags;
	}
	
	public void setChildCategoryTags(List<CategoryTag> childCategoryTags) {
		_childCategoryTags = childCategoryTags;
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
	
	private List<Column> _columns = new ArrayList<Column>();

	/**
	 * 关联的栏目
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "categoryTags")
	@OrderBy("code")
	public List<Column> getColumns() {
		return _columns;
	}
	
	public void setColumns(List<Column> columns) {
		_columns = columns;
	}
	
	private List<Article> _articles = new ArrayList<Article>();

	/**
	 * 关联的文章
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "categoryTags")
	@OrderBy("articleId")
	public List<Article> getArticles() {
		return _articles;
	}
	
	public void setArticles(List<Article> articles) {
		_articles = articles;
	}

	private List<Glossary> _glossaries = new ArrayList<Glossary>();

	/**
	 * 关联的术语
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "categoryTags")
	@OrderBy("glossaryId")
	public List<Glossary> getGlossaries() {
		return _glossaries;
	}
	
	public void setGlossaries(List<Glossary> glossaries) {
		_glossaries = glossaries;
	}
}
