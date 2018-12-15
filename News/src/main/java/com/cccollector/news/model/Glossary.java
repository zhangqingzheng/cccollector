/**
 * 
 */
package com.cccollector.news.model;

import java.io.File;
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
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 术语类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "news_glossaries")
public class Glossary implements Serializable {

	private static final long serialVersionUID = 43374839161943677L;

	private Integer _glossaryId;

	/**
	 * 术语ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getGlossaryId() {
		return _glossaryId;
	}

	public void setGlossaryId(Integer glossaryId) {
		_glossaryId = glossaryId;
	}

	/**
	 * 术语URL路径
	 */
	@JsonIgnore
	@Transient
	public String getGlossaryUrlPath() {
		return getNewsSource().getNewsSourceUrlPath() + "glossaries/";
	}

	/**
	 * 术语文件路径
	 */
	@JsonIgnore
	@Transient
	public String getGlossaryFilePath() {
		return getNewsSource().getNewsSourceFilePath() + "glossaries" + File.separator;
	}

	private String _title;

	/**
	 * 标题
	 */
	@Column(nullable = false, length = 200)
	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	private Integer _pictureRatio;

	/**
	 * 图片比例
	 */
	@Column(nullable = false)
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
		横图16_9
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
		return PictureRatioEnum.values()[_pictureRatio];
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

	/**
	 * 图片URL路径
	 */
	public String pictureUrlPath() {
		return getGlossaryUrlPath() + _glossaryId + "_picture.jpg";
	}
	
	/**
	 * 图片文件路径
	 */
	public String pictureFilePath() {
		return getGlossaryFilePath() + _glossaryId + "_picture.jpg";
	}

	private Date _pictureUpdateTime;

	/**
	 * 图片更新时间
	 */
	@javax.persistence.Column(nullable = true)
	public Date getPictureUpdateTime() {
		return _pictureUpdateTime;
	}

	public void setPictureUpdateTime(Date pictureUpdateTime) {
		_pictureUpdateTime = pictureUpdateTime;
	}

	private Integer _displayPriority;

	/**
	 * 显示优先级
	 */
	@Column(nullable = false)
	public Integer getDisplayPriority() {
		return _displayPriority;
	}

	public void setDisplayPriority(Integer displayPriority) {
		_displayPriority = displayPriority;
	}

	/**
	 * 显示优先级枚举
	 */
	public static enum DisplayPriorityEnum {
		低,
		中,
		高,
		超高
	}

	/**
	 *显示优先级枚举数组
	 */
	@JsonIgnore
	@Transient
	public DisplayPriorityEnum[] getDisplayPriorityEnums() {
		return DisplayPriorityEnum.values();
	}

	/**
	 * 显示优先级的枚举
	 */
	@JsonIgnore
	@Transient
	public DisplayPriorityEnum getDisplayPriorityEnum() {
		return DisplayPriorityEnum.values()[_displayPriority];
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
	
	private List<CategoryTag> _categoryTags = new ArrayList<CategoryTag>();

	/**
	 * 关联的分类标签
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tag_glossaryCategoryTags", joinColumns = @JoinColumn(name = "glossaryId", nullable = false), inverseJoinColumns = @JoinColumn(name = "categoryTagId", nullable = false))
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
	@Transient
	public List<Integer> getCategoryTagIds() {
		return _categoryTagIds;
	}
	
	public void setCategoryTagIds(List<Integer> categoryTagIds) {
		_categoryTagIds = categoryTagIds;
	}	
}
