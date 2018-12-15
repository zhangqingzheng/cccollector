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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 推荐组类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "rcmd_recommendGroups")
public class RecommendGroup implements Serializable {

	private static final long serialVersionUID = 1444865703711458412L;
	
	private Integer _recommendGroupId;

	/**
	 * 推荐组ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getRecommendGroupId() {
		return _recommendGroupId;
	}

	public void setRecommendGroupId(Integer recommendGroupId) {
		_recommendGroupId = recommendGroupId;
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
		顶部轮播
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

	private Integer _pictureRatio;

	/**
	 * 图片比例
	 */
	@Column(nullable = true)
	public Integer getPictureRatio() {
		return _pictureRatio;
	}
	
	public void setPictureRatio(Integer pictureRatio) {
		_pictureRatio = pictureRatio;
	}

	/**
	 * 图片比例枚举
	 */
	public static enum PictureRatioEnum {
		方图1_1,
		横图3_2,
		竖图2_3,
		横图4_3,
		竖图3_4,
		横图16_9,
		竖图9_16
	}

	/**
	 * 图片比例枚举数组
	 */
	@JsonIgnore
	@Transient
	public PictureRatioEnum[] getPictureRatioEnums() {
		return PictureRatioEnum.values();
	}

	/**
	 * 图片比例的枚举
	 */
	@JsonIgnore
	@Transient
	public PictureRatioEnum getPictureRatioEnum() {
		if (_pictureRatio != null) {
			return PictureRatioEnum.values()[_pictureRatio];
		}
		return null;
	}

	/**
	 * 获取图片宽度比例
	 */
	@JsonIgnore
	@Transient
	public int getPictureWidthRatio() {
		try {
			String[] pictureRatio = getPictureRatioEnum().name().split("_");
			return Integer.valueOf(pictureRatio[0].substring(2));
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取图片高度比例
	 */
	@JsonIgnore
	@Transient
	public int getPictureHeightRatio() {
		try {
			String[] pictureRatio = getPictureRatioEnum().name().split("_");
			return Integer.valueOf(pictureRatio[1]);
		} catch (Exception e) {
			return 0;
		}
	}

	private Integer _recommendCount;

	/**
	 * 包含推荐数量
	 */
	@Column(nullable = false)
	public Integer getRecommendCount() {
		return _recommendCount;
	}

	public void setRecommendCount(Integer recommendCount) {
		_recommendCount = recommendCount;
	}

	private Boolean _enabled;

	/**
	 * 是否可用
	 */
	@JsonIgnore
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
	
	private Integer _ownerType;

	/**
	 * 所有者类别
	 */
	@Column(nullable = false)
	public Integer getOwnerType() {
		return _ownerType;
	}

	public void setOwnerType(Integer ownerType) {
		_ownerType = ownerType;
	}

	/**
	 * 所有者类别枚举
	 */
	public static enum OwnerTypeEnum {
		栏目
	}

	/**
	 * 所有者类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public OwnerTypeEnum[] getOwnerTypeEnums() {
		return OwnerTypeEnum.values();
	}

	/**
	 * 所有者类别的枚举
	 */
	@JsonIgnore
	@Transient
	public OwnerTypeEnum getOwnerTypeEnum() {
		return OwnerTypeEnum.values()[_ownerType];
	}

	private Integer _ownerId;

	/**
	 * 所有者ID
	 */
	@Column(nullable = false)
	public Integer getOwnerId() {
		return _ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		_ownerId = ownerId;
	}

	private Object _owner;

	/**
	 * 所有者
	 */
	@JsonIgnore
	@Transient
	public Object getOwner() {
		return _ownerId;
	}

	@Transient
	public void setOwner(Object owner) {
		_owner = owner;
	}
	
	/**
	 * 获取所有者名称
	 */
	@JsonIgnore
	@Transient
	public String getOwnerName() {
		switch (getOwnerTypeEnum()) {
		case 栏目:
			com.cccollector.news.model.Column column = (com.cccollector.news.model.Column) _owner;
			return column.getName();
		default:
			return null;
		}
	}

	private List<Recommend> _recommends = new ArrayList<Recommend>();

	/**
	 * 包含的推荐
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "recommendGroup")
	@OrderBy("position")
	public List<Recommend> getRecommends() {
		return _recommends;
	}

	public void setRecommends(List<Recommend> recommend) {
		_recommends = recommend;
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
}
