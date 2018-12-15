/**
 * 
 */
package com.cccollector.news.model;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *缩略图类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "news_thumbnails")
public class LazyThumbnail implements Serializable {
	
	private static final long serialVersionUID = -9116096799458150351L;
	
	private Integer _thumbnailId;

	/**
	 *缩略图ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getThumbnailId() {
		return _thumbnailId;
	}
	
	public void setThumbnailId(Integer thumbnailId) {
		_thumbnailId = thumbnailId;
	}

	/**
	 * 缩略图URL路径
	 */
	@JsonIgnore
	@Transient
	public String getThumbnailUrlPath() {
		return getDistribution().getArticle().getNewsSource().getNewsSourceUrlPath() + "thumbnails/";
	}

	/**
	 * 缩略图文件路径
	 */
	@JsonIgnore
	@Transient
	public String getThumbnailFilePath() {
		return getDistribution().getArticle().getNewsSource().getNewsSourceFilePath() + "thumbnails" + File.separator;
	}

	private Integer _widthRatio;

	/**
	 * 宽度比例
	 */
	@Column(nullable = false)
	public Integer getWidthRatio() {
		return _widthRatio;
	}
	
	public void setWidthRatio(Integer widthRatio) {
		_widthRatio = widthRatio;
	}

	private Integer _heightRatio;

	/**
	 * 高度比例
	 */
	@Column(nullable = false)
	public Integer getHeightRatio() {
		return _heightRatio;
	}
	
	public void setHeightRatio(Integer heightRatio) {
		_heightRatio = heightRatio;
	}

	private String _path;

	/**
	 * 存储路径
	 */
	@Column(nullable = false, length = 6)
	public String getPath() {
		return _path;
	}

	public void setPath(String path) {
		_path = path;
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

	/**
	 * 图片URL路径
	 */
	public String pictureUrlPath() {
		return getThumbnailUrlPath() + _path + _thumbnailId + "_thumbnail" + ".jpg";
	}
	
	/**
	 * 图片文件路径
	 */
	public String pictureFilePath() {
		return getThumbnailFilePath() +_path + _thumbnailId + "_thumbnail" + ".jpg";
	}
	
	private Distribution _distribution;

	/**
	 * 所属的分发
	 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "distributionId", nullable = false)
	public Distribution getDistribution() {
		return _distribution;
	}
	
	public void setDistribution(Distribution distribution) {
		_distribution = distribution;
	}
}
