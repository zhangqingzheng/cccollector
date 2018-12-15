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
 * 多媒体类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "news_medias")
public class Media implements Serializable {
	
	private static final long serialVersionUID = -9116096799458150351L;
	
	private Integer _mediaId;

	/**
	 * 多媒体ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getMediaId() {
		return _mediaId;
	}

	public void setMediaId(Integer mediaId) {
		_mediaId = mediaId;
	}

	/**
	 * 多媒体URL路径
	 */
	@JsonIgnore
	@Transient
	public String getMediaUrlPath() {
		return getArticle().getNewsSource().getNewsSourceUrlPath() + "medias/";
	}

	/**
	 * 多媒体文件路径
	 */
	@JsonIgnore
	@Transient
	public String getMediaFilePath() {
		return getArticle().getNewsSource().getNewsSourceFilePath() + "medias" + File.separator;
	}

	private Integer _type;

	/**
	 * 类别
	 */
	@Column(nullable = false)
	public Integer getType() {
		return _type;
	}

	public void setType(Integer type) {
		_type = type;
	}

	/**
	 * 类别枚举
	 */
	public static enum TypeEnum {
		图片,
		音频,
		视频
	}

	/**
	 * 类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public TypeEnum[] getTypeEnums() {
		return TypeEnum.values();
	}

	/**
	 * 类别的枚举
	 */
	@JsonIgnore
	@Transient
	public TypeEnum getTypeEnum() {
		return TypeEnum.values()[_type];
	}

	/**
	 * 图片URL路径
	 */
	public String pictureUrlPath() {
		return getMediaUrlPath() + _path + _mediaId + "_picture_" + _suffix + ".jpg";
	}
	
	/**
	 * 图片文件路径
	 */
	public String pictureFilePath() {
		return getMediaFilePath() + _path + _mediaId + "_picture_" + _suffix + ".jpg";
	}

	/**
	 * 多媒体URL路径
	 */
	public String mediaUrlPath() {
		return getMediaUrlPath() + _path + _mediaId + (getTypeEnum() == TypeEnum.音频 ? ("_audio_" + _suffix + ".m4a") : ( "_video_" + _suffix + ".mp4"));
	}
	
	/**
	 * 多媒体文件路径
	 */
	public String mediaFilePath() {
		return getMediaFilePath() + _path + _mediaId + (getTypeEnum() == TypeEnum.音频 ? ("_audio_" + _suffix + ".m4a") : ( "_video_" + _suffix + ".mp4"));
	}

	private String _caption;

	/**
	 * 说明
	 */
	@Column(nullable = true, length = 1000)
	public String getCaption() {
		return _caption;
	}

	public void setCaption(String caption) {
		_caption = caption;
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

	private String _suffix;

	/**
	 * 存储后缀
	 */
	@Column(nullable = false, length = 6)
	public String getSuffix() {
		return _suffix;
	}

	public void setSuffix(String suffix) {
		_suffix = suffix;
	}
	
	private Integer _width;

	/**
	 * 图片宽度
	 */
	@Column(nullable = false)
	public Integer getWidth() {
		return _width;
	}
	
	public void setWidth(Integer width) {
		_width = width;
	}

	private Integer _height;

	/**
	 * 图片高度
	 */
	@Column(nullable = false)	
	public Integer getHeight() {
		return _height;
	}
	
	public void setHeight(Integer height) {
		_height = height;
	}
	
	private Integer _duration;

	/**
	 * 音视频时长
	 */
	@Column(nullable = false)
	public Integer getDuration() {
		return _duration;
	}

	public void setDuration(Integer duration) {
		_duration = duration;
	}

	/**
	 * 音频频时长格式化字符串
	 */
	@JsonIgnore
	@Transient
	public String getDurationFormatString() {
		long durationLong = _duration * 1000;
		long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
//        long day = durationLong / nd;
        long hour = durationLong % nd / nh;
        long min = durationLong % nd % nh / nm;
        long sec = durationLong % nd % nh % nm / ns;

        if (hour != 0) {
            if (min < 10) {
                if (sec < 10) {
                    return hour + ":0" + min + ":0" + sec;
                } else {
                    return hour + ":0" + min + ":" + sec;
                }
            } else {
                if (sec < 10) {
                    return hour + ":"+ min + ":0" + sec;
                } else {
                    return hour + ":"+ min + ":" + sec;
                }
            }
        } else if (min != 0) {
            if (min < 10) {
                if (sec < 10) {
                    return "0" + min + ":0" + sec;
                } else {
                    return "0" + min + ":" + sec;
                }
            } else {
                if (sec < 10) {
                    return min + ":0" + sec;
                } else {
                    return min + ":" + sec;
                }
            }
        } else {
            if (sec < 10) {
                return "00:0" + sec + "";
            } else {
                return "00:" + sec + "";
            }
        }
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

	private Article _article;

	/**
	 * 所属的文章
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "articleId", nullable = false)
	public Article getArticle() {
		return _article;
	}

	public void setArticle(Article article) {
		_article = article;
	}
}
