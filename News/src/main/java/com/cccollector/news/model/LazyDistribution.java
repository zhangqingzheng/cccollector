/**
 * 
 */
package com.cccollector.news.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 分发类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "news_distributions")
public class LazyDistribution implements Serializable {

	private static final long serialVersionUID = 4167702506933109231L;

	private Integer _distributionId;

	/**
	 * 分发ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getDistributionId() {
		return _distributionId;
	}

	public void setDistributionId(Integer distributionId) {
		_distributionId = distributionId;
	}

	private Integer _listTemplateType;

	/**
	 * 列表模版类别
	 */
	@javax.persistence.Column(nullable = false)
	public Integer getListTemplateType() {
		return _listTemplateType;
	}

	public void setListTemplateType(Integer listTemplateType) {
		_listTemplateType = listTemplateType;
	}

	/**
	 * 列表模版类别枚举
	 */
	public static enum ListTemplateTypeEnum {
		无图,
		单张小图,
		单张大图,
		双张小图,
		三张小图,
		多张小图轮换
	}

	/**
	 * 列表模版类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public ListTemplateTypeEnum[] getListTemplateTypeEnums() {
		return ListTemplateTypeEnum.values();
	}

	/**
	 * 列表模版类别的模版枚举
	 */
	@JsonIgnore
	@Transient
	public ListTemplateTypeEnum getListTemplateTypeEnum() {
		return ListTemplateTypeEnum.values()[_listTemplateType];
	}

	/**
	 * 获取列表缩略图数量
	 */
	@JsonIgnore
	@Transient
	public int getListThumbnailCount() {
		switch (getListTemplateTypeEnum()) {
		case 无图:
			return 0;
		case 单张小图:
			return 1;
		case 单张大图:
			return 1;
		case 双张小图:
			return 2;
		case 三张小图:
			return 3;
		case 多张小图轮换:
			return Integer.MAX_VALUE;
		default:
			return 0;
		}
	}

	private Integer _listThumbnailRatio;

	/**
	 * 列表缩略图比例
	 */
	@javax.persistence.Column(nullable = false)
	public Integer getListThumbnailRatio() {
		return _listThumbnailRatio;
	}

	public void setListThumbnailRatio(Integer listThumbnailRatio) {
		_listThumbnailRatio = listThumbnailRatio;
	}

	/**
	 * 列表缩略图比例枚举
	 */
	public static enum ListThumbnailRatioEnum {
		方图1_1,
		横图3_2,
		竖图2_3,
		横图4_3,
		竖图3_4,
		横图16_9,
		竖图9_16
	}

	/**
	 * 列表缩略图比例枚举数组
	 */
	@JsonIgnore
	@Transient
	public ListThumbnailRatioEnum[] getListThumbnailRatioEnums() {
		return ListThumbnailRatioEnum.values();
	}

	/**
	 * 列表缩略图比例的模版枚举
	 */
	@JsonIgnore
	@Transient
	public ListThumbnailRatioEnum getListThumbnailRatioEnum() {
		return ListThumbnailRatioEnum.values()[_listThumbnailRatio];
	}

	/**
	 * 获取列表缩略图宽度比例
	 */
	@JsonIgnore
	@Transient
	public int getListThumbnailWidthRatio() {
		try {
			String[] thumbnailRatio = getListThumbnailRatioEnum().name().split("_");
			return Integer.valueOf(thumbnailRatio[0].substring(2));
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取列表缩略图高度比例
	 */
	@JsonIgnore
	@Transient
	public int getListThumbnailHeightRatio() {
		try {
			String[] thumbnailRatio = getListThumbnailRatioEnum().name().split("_");
			return Integer.valueOf(thumbnailRatio[1]);
		} catch (Exception e) {
			return 0;
		}
	}

	private Integer _bodyTemplateType;

	/**
	 * 正文模版类别
	 */
	@javax.persistence.Column(nullable = false)
	public Integer getBodyTemplateType() {
		return _bodyTemplateType;
	}

	public void setBodyTemplateType(Integer bodyTemplateType) {
		_bodyTemplateType = bodyTemplateType;
	}

	/**
	 * 正文模版类别枚举
	 */
	public static enum BodyTemplateTypeEnum {
		图文混排,
		图文分离,
		图集,
		杂志,
		图书,
		顶部悬浮音频,
		顶部悬浮视频
	}

	/**
	 * 正文模版类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public BodyTemplateTypeEnum[] getBodyTemplateTypeEnums() {
		return BodyTemplateTypeEnum.values();
	}

	/**
	 * 正文模版类别的模版枚举
	 */
	@JsonIgnore
	@Transient
	public BodyTemplateTypeEnum getBodyTemplateTypeEnum() {
		return BodyTemplateTypeEnum.values()[_bodyTemplateType];
	}

	private Integer _validateTime;

	/**
	 * 验证时间
	 */
	@javax.persistence.Column(nullable = false)
	public Integer getValidateTime() {
		return _validateTime;
	}

	public void  setValidateTime(Integer validateTime) {
		_validateTime = validateTime;
	}

	/**
	 * Date类型验证时间
	 */
	@JsonIgnore
	@Transient
	public Date getValidateTimeDate() {
		if (_validateTime != null) {
			return new Date((long) _validateTime * 1000);
		}
		return null;
	}

	@Transient
	public void setValidateTimeDate(Date validateTimeDate) {
		if (validateTimeDate != null) {
			_validateTime = (int) (validateTimeDate.getTime() / 1000);
		} else {
			_validateTime = (int) (new Date().getTime() / 1000);
		}
	}

	private String _publisher;

	/**
	 * 发布人
	 */
	@JsonIgnore
	@javax.persistence.Column(nullable = true, length = 100)
	public String getPublisher() {
		return _publisher;
	}

	public void setPublisher(String publisher) {
		_publisher = publisher;
	}

	private Integer _publisherId;

	/**
	 * 发布人ID
	 */
	@JsonIgnore
	@javax.persistence.Column(nullable = true)
	public Integer getPublisherId() {
		return _publisherId;
	}

	public void setPublisherId(Integer publisherId) {
		_publisherId = publisherId;
	}

	private Integer _publishTime;

	/**
	 * 发布时间
	 */
	@javax.persistence.Column(nullable = true)
	public Integer getPublishTime() {
		return _publishTime;
	}

	public void  setPublishTime(Integer publishTime) {
		_publishTime = publishTime;
	}

	/**
	 * Date类型发布时间
	 */
	@JsonIgnore
	@Transient
	public Date getPublishTimeDate() {
		if (_publishTime != null) {
			return new Date((long) _publishTime * 1000);
		}
		return null;
	}

	@Transient
	public void setPublishTimeDate(Date publishTimeDate) {
		if (publishTimeDate != null) {
			_publishTime = (int) (publishTimeDate.getTime() / 1000);
		} else {
			_publishTime = (int) (new Date().getTime() / 1000);
		}
	}

	/**
	 * 是否已发布
	 */
	@JsonIgnore
	@Transient
	public boolean getPublished() {
		return _publishTime != null;
	}

	private Integer _scheduledTime;

	/**
	 * 定时发布时间
	 */
	@JsonIgnore
	@javax.persistence.Column(nullable = true)
	public Integer getScheduledTime() {
		return _scheduledTime;
	}

	public void setScheduledTime(Integer scheduledTime) {
		_scheduledTime = scheduledTime;
	}

	/**
	 * Date类型定时发布时间
	 */
	@JsonIgnore
	@Transient
	public Date getScheduledTimeDate() {
		if (_scheduledTime != null) {
			return new Date((long) _scheduledTime * 1000);
		}
		return null;
	}

	@Transient
	public void setScheduledTimeDate(Date scheduledTimeDate) {
		if (scheduledTimeDate != null) {
			_scheduledTime = (int) (scheduledTimeDate.getTime() / 1000);
		} else {
			_scheduledTime = (int) (new Date().getTime() / 1000);
		}
	}

	/**
	 * 是否已定时发布
	 */
	@JsonIgnore
	@Transient
	public boolean getScheduled() {
		return _scheduledTime != null;
	}

	/**
	 * 是否已发布或已定时发布
	 */
	@JsonIgnore
	@Transient
	public boolean getPublishedOrScheduled() {
		return _publishTime != null || _scheduledTime != null;
	}

	private String _staticPageUrl;

	/**
	 * 静态化页面URL
	 */
	@javax.persistence.Column(nullable = true, length = 100)
	public String getStaticPageUrl() {
		return _staticPageUrl;
	}
	
	public void setStaticPageUrl(String staticPageUrl) {
		_staticPageUrl = staticPageUrl;
	}

	/**
	 * 是否首发枚举
	 */
	public static enum EnabledEnum {
		否,
		是
	}
	
	private Boolean _firstPublished;

	/**
	 * 是否首发
	 */
	@javax.persistence.Column(nullable = false)
	public Boolean getFirstPublished() {
		return _firstPublished;
	}

	public void setFirstPublished(Boolean firstPublished) {
		_firstPublished = firstPublished;
	}

	/**
	 * 是否首发枚举
	 */
	public static enum FirstPublishedEnum {
		否,
		是
	}

	/**
	 * 是否首发的枚举
	 */
	@JsonIgnore
	@Transient
	public FirstPublishedEnum getFirstPublishedEnum() {
		int index = _firstPublished ? 1 : 0;
		return FirstPublishedEnum.values()[index];
	}

	private Integer _position;

	/**
	 * 指定排序位置
	 */
	@javax.persistence.Column(nullable = true)
	public Integer getPosition() {
		return _position;
	}

	public void setPosition(Integer position) {
		_position = position;
	}

	/**
	 * 指定排序位置数量
	 */
	public static final int PositionCount = 20;

	private LazyArticle _article;

	/**
	 * 所属的文章
	 */
	@ManyToOne
	@JoinColumn(name = "articleId", nullable = false)
	public LazyArticle getArticle() {
		return _article;
	}

	public void setArticle(LazyArticle article) {
		_article = article;
	}

	private Column _column;

	/**
	 * 所属的栏目
	 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "columnId", nullable = false)
	public Column getColumn() {
		return _column;
	}

	public void setColumn(Column column) {
		_column = column;
	}

	private Integer _columnId;

	/**
	 * 所属的栏目ID
	 */	
	@Transient
	public Integer getColumnId() {
		return _columnId;
	}

	public void setColumnId(Integer columnId) {
		_columnId = columnId;
	}
	
	private List<LazyThumbnail> _thumbnails = new ArrayList<LazyThumbnail>();

	/**
	 * 包含的缩略图
	 */
	@OneToMany(mappedBy = "distribution")
	@OrderBy("position")
	public List<LazyThumbnail> getThumbnails() {
		return _thumbnails;
	}

	public void setThumbnails(List<LazyThumbnail> thumbnails) {
		_thumbnails = thumbnails;
	}
}
