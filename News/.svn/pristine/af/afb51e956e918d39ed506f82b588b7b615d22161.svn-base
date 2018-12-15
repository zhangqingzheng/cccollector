/**
 * 
 */
package com.cccollector.news.model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

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
 * 推荐类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "rcmd_recommends")
public class Recommend implements Serializable {
	
	private static final long serialVersionUID = 4859258431291273310L;
	
	private Integer _recommendId;

	/**
	 * 推荐ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getRecommendId() {
		return _recommendId;
	}

	public void setRecommendId(Integer recommendId) {
		_recommendId = recommendId;
	}

	/**
	 * 推荐URL路径
	 */
	@JsonIgnore
	@Transient
	public String getRecommendUrlPath() {
		return getRecommendGroup().getNewsSource().getNewsSourceUrlPath() + "recommends/";
	}

	/**
	 * 推荐文件路径
	 */
	@JsonIgnore
	@Transient
	public String getRecommendFilePath() {
		return getRecommendGroup().getNewsSource().getNewsSourceFilePath() + "recommends" + File.separator;
	}

	private String _title;

	/**
	 * 标题
	 */
	@Column(nullable = false, length = 100)
	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	private String _link;

	/**
	 * 链接
	 */
	@Column(nullable = true, length = 200)
	public String getLink() {
		return _link;
	}

	public void setLink(String link) {
		_link = link;
	}

	/**
	 * 图片URL路径
	 */
	public String pictureUrlPath() {
		return getRecommendUrlPath() + _recommendId + "_picture.jpg";
	}
	
	/**
	 * 图片文件路径
	 */
	public String pictureFilePath() {
		return getRecommendFilePath() + _recommendId + "_picture.jpg";
	}

	private Integer _contentType;

	/**
	 * 关联内容类别
	 */
	@Column(nullable = false)
	public Integer getContentType() {
		return _contentType;
	}

	public void setContentType(Integer contentType) {
		_contentType = contentType;
	}

	/**
	 * 关联内容类别枚举
	 */
	public static enum ContentTypeEnum {
		栏目,
		分发,
		用户
	}

	/**
	 * 关联内容类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public ContentTypeEnum[] getContentTypeEnums() {
		return ContentTypeEnum.values();
	}

	/**
	 * 关联内容类别的枚举
	 */
	@JsonIgnore
	@Transient
	public ContentTypeEnum getContentTypeEnum() {
		return ContentTypeEnum.values()[_contentType];
	}

	private Integer _contentId;
	
	/**
	 * 关联内容ID
	 */
	@Column(nullable = false)
	public Integer getContentId() {
		return _contentId;
	}
	
	public void setContentId(Integer contentId) {
		_contentId = contentId;
	}

	private Object _relativeContent;
	
	/**
	 * 关联内容
	 */
	@JsonIgnore
	@Transient
	public Object getRelativeContent() {
		return _relativeContent;
	}
	
	@Transient
	public void setRelativeContent(Object relativeContent) {
		_relativeContent = relativeContent;
	}
	
	/**
	 * 获取关联内容名称
	 */
	@JsonIgnore
	@Transient
	public String getRelativeContentName() {
		if (_relativeContent == null) {
			return null;
		}
		switch (getContentTypeEnum()) {
		case 栏目:
			com.cccollector.news.model.Column column = (com.cccollector.news.model.Column) _relativeContent;
			return column.getName();
		case 分发:
			Distribution distribution = (Distribution) _relativeContent;
			return distribution.getArticle().getTitle() + " @ " + distribution.getColumn().getName();
		case 用户:
			User user = (User) _relativeContent;
			return user.getNickName();
		default:
			return null;
		}
	}

	private String _contentKeyValues;

	/**
	 * 关联内容键值
	 */
	@Column(nullable = true, length = 1000)
	public String getContentKeyValues() {
		return _contentKeyValues;
	}

	public void setContentKeyValues(String contentKeyValues) {
		_contentKeyValues = contentKeyValues;
	}

	private Integer _updateTime;

	/**
	 * 更新时间
	 */
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

	private Integer _status;

	/**
	 * 状态
	 */
	@JsonIgnore	
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
		撤销发布
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
	 * 状态的模版枚举
	 */
	@JsonIgnore
	@Transient
	public StatusEnum getStatusEnum() {
		return StatusEnum.values()[_status];
	}	
	
	private Boolean _validating;

	/**
	 * 是否正在验证
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Boolean getValidating() {
		return _validating;
	}

	public void setValidating(Boolean validating) {
		_validating = validating;
	}

	/**
	 * 是否正在验证枚举
	 */
	public static enum ValidatingEnum {
		否,
		是
	}

	/**
	 * 是否正在验证的枚举
	 */
	@JsonIgnore
	@Transient
	public ValidatingEnum getValidatingEnum() {
		int index = _validating ? 1 : 0;
		return ValidatingEnum.values()[index];
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

	private RecommendGroup _recommendGroup;

	/**
	 * 所属的推荐组
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "recommendGroupId", nullable = false)
	public RecommendGroup getRecommendGroup() {
		return _recommendGroup;
	}

	public void setRecommendGroup(RecommendGroup recommendGroup) {
		_recommendGroup = recommendGroup;
	}
}
