/**
 * 
 */
package com.cccollector.news.model;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cccollector.universal.model.UniversalJsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 文章类
 *
 * @author 谢朋 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "news_articles")
public class LazyArticle implements Serializable {

	private static final long serialVersionUID = -4591641018756223499L;

	private Integer _articleId;

	/**
	 * 文章ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getArticleId() {
		return _articleId;
	}

	public void setArticleId(Integer articleId) {
		_articleId = articleId;
	}

	private String _title;

	/**
	 * 标题
	 */
	@JsonView(UniversalJsonViews.StaticPublic.class)
	@Column(nullable = false, length = 200)
	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	private String _source;

	/**
	 * 来源
	 */
	@JsonView(UniversalJsonViews.StaticPublic.class)
	@Column(nullable = true, length = 200)
	public String getSource() {
		return _source;
	}

	public void setSource(String source) {
		_source = source;
	}

	private Boolean _published;

	/**
	 * 是否已发布
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Boolean getPublished() {
		return _published;
	}

	public void setPublished(Boolean published) {
		_published = published;
	}

	/**
	 * 是否已发布枚举
	 */
	public static enum PublishedEnum {
		否, 是
	}

	/**
	 * 是否已发布的枚举
	 */
	@JsonIgnore
	@Transient
	public PublishedEnum getPublishedEnum() {
		int index = _published ? 1 : 0;
		return PublishedEnum.values()[index];
	}

	private NewsSource _newsSource;

	/**
	 * 所属的新闻源
	 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "newsSourceId", nullable = false)
	public NewsSource getNewsSource() {
		return _newsSource;
	}

	public void setNewsSource(NewsSource newsSource) {
		_newsSource = newsSource;
	}

	private List<LazyDistribution> _distributions = new ArrayList<LazyDistribution>();

	/**
	 * 包含的分发
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
	public List<LazyDistribution> getDistributions() {
		return _distributions;
	}

	public void setDistributions(List<LazyDistribution> distributions) {
		_distributions = distributions;
	}

	private List<CategoryTag> _categoryTags = new ArrayList<CategoryTag>();

	/**
	 * 关联的分类标签
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tag_articleCategoryTags", joinColumns = @JoinColumn(name = "articleId", nullable = false), inverseJoinColumns = @JoinColumn(name = "categoryTagId", nullable = false))
	@OrderColumn(name = "position")
	public List<CategoryTag> getCategoryTags() {
		return _categoryTags;
	}

	public void setCategoryTags(List<CategoryTag> categoryTags) {
		_categoryTags = categoryTags;
	}

	private List<Integer> _categoryTagIds = new ArrayList<Integer>();

	/**
	 * 关联的分类标签ID
	 */
	@JsonView(UniversalJsonViews.StaticPrivate.class)
	@Transient
	public List<Integer> getCategoryTagIds() {
		return _categoryTagIds;
	}

	public void setCategoryTagIds(List<Integer> categoryTagIds) {
		_categoryTagIds = categoryTagIds;
	}
}
