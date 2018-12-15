/**
 * 
 */
package com.cccollector.news.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cccollector.universal.model.UniversalJsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 文章类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "news_articles")
public class Article implements Serializable {
	
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

	private String _summary;

	/**
	 * 摘要
	 */
	@JsonView(UniversalJsonViews.StaticPrivate.class)
	@Column(nullable = true, length = 2000)
	public String getSummary() {
		return _summary;
	}

	public void setSummary(String summary) {
		_summary = summary;
	}

	private String _author;

	/**
	 * 作者
	 */
	@JsonView(UniversalJsonViews.StaticPrivate.class)
	@Column(nullable = true, length = 200)
	public String getAuthor() {
		return _author;
	}

	public void setAuthor(String author) {
		_author = author;
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

	private String _keywords;

	/**
	 * 关键字
	 */
	@JsonView(UniversalJsonViews.StaticPrivate.class)
	@Column(nullable = true, length = 200)
	public String getKeywords() {
		return _keywords;
	}

	public void setKeywords(String keywords) {
		_keywords = keywords;
	}

	private String _content;

	/**
	 * 内容
	 */
	@JsonView(UniversalJsonViews.StaticPrivate.class)
	@Column(nullable = true)
	public String getContent() {
		return _content;
	}

	public void setContent(String content) {
		_content = content;
	}

	private String _path;

	/**
	 * 存储路径
	 */
	@JsonView(UniversalJsonViews.StaticPublic.class)
	@Column(nullable = false, length = 6)
	public String getPath() {
		return _path;
	}

	public void setPath(String path) {
		_path = path;
	}

	private String _suffix;
	
	/**
	 * 存储后缀
	 */
	@JsonView(UniversalJsonViews.StaticPublic.class)
	@Column(nullable = false, length = 6)
	public String getSuffix() {
		return _suffix;
	}
	
	public void setSuffix(String suffix) {
		_suffix = suffix;
	}

	/**
	 * JSON文件路径
	 */
	public String jsonFilePath() {
		return _path + _articleId + "_article_" +_suffix + ".json";
	}

	private String _editor;

	/**
	 * 编辑
	 */
	@JsonIgnore
	@Column(nullable = false, length = 100)
	public String getEditor() {
		return _editor;
	}

	public void setEditor(String editor) {
		_editor = editor;
	}

	private Integer _editorId;

	/**
	 * 编辑ID
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Integer getEditorId() {
		return _editorId;
	}

	public void setEditorId(Integer editorId) {
		_editorId = editorId;
	}

	private Integer _editTime;

	/**
	 * 编辑时间
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Integer getEditTime() {
		return _editTime;
	}

	public void setEditTime(Integer editTime) {
		_editTime = editTime;
	}
	
	/**
	 * Date类型编辑时间
	 */
	@JsonIgnore
	@Transient
	public Date getEditTimeDate() {
		if (_editTime != null) {
			return new Date((long) _editTime * 1000);
		}
		return null;
	}
	
	@Transient
	public void setEditTimeDate(Date editTimeDate) {
		if (editTimeDate != null) {
			_editTime = (int) (editTimeDate.getTime() / 1000);
		} else {
			_editTime = (int) (new Date().getTime() / 1000);
		}
	}
	
	private String _reviewer;

	/**
	 * 审稿人
	 */
	@JsonIgnore
	@Column(nullable = true, length = 100)
	public String getReviewer() {
		return _reviewer;
	}

	public void setReviewer(String reviewer) {
		_reviewer = reviewer;
	}

	private Integer _reviewerId;

	/**
	 * 审稿人ID
	 */
	@JsonIgnore
	@Column(nullable = true)
	public Integer getReviewerId() {
		return _reviewerId;
	}

	public void setReviewerId(Integer reviewerId) {
		_reviewerId = reviewerId;
	}

	private Integer _reviewTime;

	/**
	 * 审稿时间
	 */
	@JsonIgnore
	@Column(nullable = true)
	public Integer getReviewTime() {
		return _reviewTime;
	}

	public void setReviewTime(Integer reviewTime) {
		_reviewTime = reviewTime;
	}

	/**
	 * Date类型审稿时间
	 */
	@JsonIgnore
	@Transient
	public Date getReviewTimeDate() {
		if (_reviewTime != null) {
			return new Date((long) _reviewTime * 1000);
		}
		return null;
	}
	
	@Transient
	public void setReviewTimeDate(Date reviewTimeDate) {
		if (reviewTimeDate != null) {
			_reviewTime = (int) (reviewTimeDate.getTime() / 1000);
		} else {
			_reviewTime = (int) (new Date().getTime() / 1000);
		}
	}
	
	private Integer _updateTime;

	/**
	 * 更新时间
	 */
	@JsonView(UniversalJsonViews.StaticPublic.class)
	@Column(nullable = false)
	public Integer getUpdateTime() {
		return _updateTime;	
	}

	public void setUpdateTime(Integer updateTime) {
		_updateTime = updateTime;
	}

	/**
	 * Date类型更新时间
	 */
	@JsonIgnore
	@Transient
	public Date getUpdateTimeDate() {
		if (_updateTime != null) {
			return new Date((long) _updateTime * 1000);
		}
		return null;
	}
	
	@Transient
	public void setUpdateTimeDate(Date updateTimeDate) {
		if (updateTimeDate != null) {
			_updateTime = (int) (updateTimeDate.getTime() / 1000);
		} else {
			_updateTime = (int) (new Date().getTime() / 1000);
		}
	}

	private Integer _pictureCount;

	/**
	 * 包含图片数
	 */
	@JsonView(UniversalJsonViews.StaticPublic.class)
	@Column(nullable = false)
	public Integer getPictureCount() {
		return _pictureCount;
	}
	
	public void setPictureCount(Integer pictureCount) {
		_pictureCount = pictureCount;
	}

	private Integer _audioCount;
	
	/**
	 * 包含音频数
	 */
	@JsonView(UniversalJsonViews.StaticPublic.class)
	@Column(nullable = false)
	public Integer getAudioCount() {
		return _audioCount;
	}
	
	public void setAudioCount(Integer audioCount) {
		_audioCount = audioCount;
	}

	private Integer _videoCount;	

	/**
	 * 包含视频数
	 */
	@JsonView(UniversalJsonViews.StaticPublic.class)
	@Column(nullable = false)
	public Integer getVideoCount() {
		return _videoCount;
	}
	
	public void setVideoCount(Integer videoCount) {
		_videoCount = videoCount;
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
		否,
		是
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

	private Integer _viewCount;

	/**
	 * 获得阅读数
	 */
	@JsonView(UniversalJsonViews.DynamicPrivate.class)
	@Column(nullable = false)
	public Integer getViewCount() {
		return _viewCount;
	}
	
	public void setViewCount(Integer viewCount) {
		_viewCount = viewCount;
	}
	
	private Integer _favoriteCount;
	
	/**
	 * 获得收藏数
	 */
	@JsonView(UniversalJsonViews.DynamicPrivate.class)
	@Column(nullable = false)
	public Integer getFavoriteCount() {
		return _favoriteCount;
	}
	
	public void setFavoriteCount(Integer favoriteCount) {
		_favoriteCount = favoriteCount;
	}

	private Integer _commentCount;
	
	/**
	 * 获得评论数
	 */
	@JsonView(UniversalJsonViews.DynamicPublic.class)
	@Column(nullable = false)
	public Integer getCommentCount() {
		return _commentCount;
	}
	
	public void setCommentCount(Integer commentCount) {
		_commentCount = commentCount;
	}

	private Integer _replyCount;

	/**
	 * 获得回复数
	 */
	@JsonView(UniversalJsonViews.DynamicPublic.class)
	@Column(nullable = false)
	public Integer getReplyCount() {
		return _replyCount;
	}
	
	public void setReplyCount(Integer replyCount) {
		_replyCount = replyCount;
	}

	private Integer _likeCount;

	/**
	 * 获得喜欢数
	 */
	@JsonView(UniversalJsonViews.DynamicPrivate.class)
	@Column(nullable = false)
	public Integer getLikeCount() {
		return _likeCount;
	}
	
	public void setLikeCount(Integer likeCount) {
		_likeCount = likeCount;
	}

	private Integer _dislikeCount;	

	/**
	 * 获得不喜欢数
	 */
	@JsonView(UniversalJsonViews.DynamicPrivate.class)
	@Column(nullable = false)
	public Integer getDislikeCount() {
		return _dislikeCount;
	}
	
	public void setDislikeCount(Integer dislikeCount) {
		_dislikeCount = dislikeCount;
	}

	private Favorite _favorite;

	/**
	 * 收藏对象
	 */
	@JsonView(UniversalJsonViews.DynamicPrivate.class)
	@Transient
	public Favorite getFavorite() {
		return _favorite;
	}
	
	public void setFavorite(Favorite favorite) {
		_favorite = favorite;
	}

	private Like _like;

	/**
	 * 喜欢对象
	 */
	@JsonView(UniversalJsonViews.DynamicPrivate.class)
	@Transient
	public Like getLike() {
		return _like;
	}

	public void setLike(Like like) {
		_like = like;
	}

	private Dislike _dislike;

	/**
	 * 不喜欢对象
	 */
	@JsonView(UniversalJsonViews.DynamicPrivate.class)
	@Transient	
	public Dislike getDislike() {
		return _dislike;
	}
	
	public void setDislike(Dislike dislike) {
		_dislike = dislike;
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
	
	private List<Distribution> _distributions = new ArrayList<Distribution>();
	
	/**
	 * 包含的分发
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
	public List<Distribution> getDistributions() {
		return _distributions;
	}

	public void setDistributions(List<Distribution> distributions) {
		_distributions = distributions;
	}

	private List<Media> _medias = new ArrayList<Media>();
	
	/**
	 * 包含的多媒体
	 */
	@JsonView(UniversalJsonViews.StaticPrivate.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
	@OrderBy("position")
	public List<Media> getMedias() {
		return _medias;
	}

	public void setMedias(List<Media> medias) {
		_medias = medias;
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
