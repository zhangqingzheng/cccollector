/**
 * 
 */
package com.cccollector.news.model;

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
 * 静态化页面类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "site_staticPages")
public class StaticPage implements Serializable {
	
	private static final long serialVersionUID = -8506329355446609315L;

	private Integer _staticPageId;

	/**
	 * 静态化页面ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getStaticPageId() {
		return _staticPageId;
	}

	public void setStaticPageId(Integer staticPageId) {
		_staticPageId = staticPageId;
	}
	
	private Integer _contentType;

	/**
	 * 内容类别
	 */
	@Column(nullable = false)
	public Integer getContentType() {
		return _contentType;
	}

	public void setContentType(Integer contentType) {
		_contentType = contentType;
	}

	/**
	 * 内容类别枚举
	 */
	public static enum ContentTypeEnum {
		栏目,
		分发,
		分类标签
	}

	/**
	 * 内容类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public ContentTypeEnum[] getContentTypeEnums() {
		return ContentTypeEnum.values();
	}

	/**
	 * 内容类别的枚举
	 */
	@JsonIgnore
	@Transient
	public ContentTypeEnum getContentTypeEnum() {
		return ContentTypeEnum.values()[_contentType];
	}

	private Integer _contentId;
	
	/**
	 * 内容ID
	 */
	@Column(nullable = false)
	public Integer getContentId() {
		return _contentId;
	}
	
	public void setContentId(Integer contentId) {
		_contentId = contentId;
	}

	private Object _content;
	
	/**
	 * 内容
	 */
	@JsonIgnore
	@Transient
	public Object getContent() {
		return _content;
	}
	
	@Transient
	public void setContent(Object content) {
		_content = content;
	}
	
	/**
	 * 获取内容名称
	 */
	@JsonIgnore
	@Transient
	public String getContentName() {
		if (_content == null) {
			return null;
		}
		switch (getContentTypeEnum()) {
		case 栏目:
			com.cccollector.news.model.Column column = (com.cccollector.news.model.Column) _content;
			return column.getName();
		case 分发:
			Distribution distribution = (Distribution) _content;
			return distribution.getArticle().getTitle() + " @ " + distribution.getColumn().getName();
		case 分类标签:
			CategoryTag categoryTag = (CategoryTag) _content;
			return categoryTag.getName();
		default:
			return null;
		}
	}
	
	private String _filePath;
	
	/**
	 * 文件路径
	 */
	@Column(nullable = false, length = 100)
	public String getFilePath() {
		return _filePath;
	}
	
	public void setFilePath(String filePath) {
		_filePath = filePath;
	}
	
	private Integer _staticTime;

	/**
	 * 静态化时间
	 */
	@Column(nullable = false)
	public Integer getStaticTime() {
		return _staticTime;
	}
	
	public void setStaticTime(Integer staticTime) {
		_staticTime = staticTime;
	}
	
	/**
	 * Date类型静态化时间
	 */
	@JsonIgnore
	@Transient
	public Date getStaticTimeDate() {
		if (_staticTime != null) {
			return new Date((long) _staticTime * 1000);
		}
		return null;
	}
	
	@Transient
	public void setStaticTimeDate(Date staticTimeDate) {
		if (staticTimeDate != null) {
			_staticTime = (int) (staticTimeDate.getTime() / 1000);
		} else {
			_staticTime = (int) (new Date().getTime() / 1000);
		}
	}
	
	private TemplateVersion _templateVersion;

	/**
	 * 关联的模板版本
	 */
	@ManyToOne
	@JoinColumn(name = "templateVersionId", nullable = false)
	public TemplateVersion getTemplateVersion() {
		return _templateVersion;
	}

	public void setTemplateVersion(TemplateVersion templateVersion) {
		_templateVersion = templateVersion;
	}

	private SiteVersion _siteVersion;

	/**
	 * 所属的站点版本
	 */
	@ManyToOne
	@JoinColumn(name = "siteVersionId", nullable = false)
	public SiteVersion getSiteVersion() {
		return _siteVersion;
	}

	public void setSiteVersion(SiteVersion siteVersion) {
		_siteVersion = siteVersion;
	}
}
