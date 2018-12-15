package com.cccollector.news.templatemodel;

import java.io.Serializable;

/**
 * 多媒体类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class TempalteMedia implements Serializable {

	private static final long serialVersionUID = 3465124096617196604L;
	
	private Integer _mediaId;

	/**
	 * 多媒体ID
	 */
	public Integer getMediaId() {
		return _mediaId;
	}

	public void setMediaId(Integer mediaId) {
		_mediaId = mediaId;
	}

	private Integer _type;

	/**
	 * 类别
	 */
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
	public TypeEnum[] getTypeEnums() {
		return TypeEnum.values();
	}

	/**
	 * 类别的枚举
	 */
	public TypeEnum getTypeEnum() {
		return TypeEnum.values()[_type];
	}

	private String _caption;

	/**
	 * 说明
	 */
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
	public Integer getDuration() {
		return _duration;
	}

	public void setDuration(Integer duration) {
		_duration = duration;
	}

	/**
	 * 音频频时长格式化字符串
	 */
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
	public Integer getPosition() {
		return _position;
	}

	public void setPosition(Integer position) {
		_position = position;
	}

	private TemplateArticle _templateArticle;

	/**
	 * 所属的文章
	 */
	public TemplateArticle getTemplateArticle() {
		return _templateArticle;
	}
	
	public void setTemplateArticle(TemplateArticle templateArticle) {
		_templateArticle = templateArticle;
	}
}
