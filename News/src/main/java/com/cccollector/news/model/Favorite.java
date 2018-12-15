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
 * 收藏类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "user_favorites")
public class Favorite implements Serializable {

	private static final long serialVersionUID = 3147105425374452552L;

	private Integer _favoriteId;

	/**
	 * 收藏ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getFavoriteId() {
		return _favoriteId;
	}
	
	public void setFavoriteId(Integer favoriteId) {
		_favoriteId = favoriteId;
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
		文章
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
	 * 关联内容类别的模版枚举
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
		case 文章:
			Article article = (Article) _relativeContent;
			return article.getTitle();
		default:
			return null;
		}
	}

	private Distribution _distribution;

	/**
	 * 分发对象
	 */
	@Transient
	public Distribution getDistribution() {
		return _distribution;
	}
	
	public void setDistribution(Distribution distribution) {
		_distribution = distribution;
	}

	private Integer _createTime;
	
	/**
	 * 创建时间
	 */
	@Column(nullable = false)
	public Integer getCreateTime() {
		return _createTime;
	}
	
	public void setCreateTime(Integer createTime) {
		_createTime = createTime;
	}
	
	/**
	 * Date类型创建时间
	 */
	@JsonIgnore
	@Transient
	public Date getCreateTimeDate() {
		if (_createTime != null) {
			return new Date((long) _createTime * 1000);
		}
		return null;
	}

	@Transient
	public void setCreateTimeDate(Date createTimeDate) {
		if (createTimeDate != null) {
			_createTime = (int) (createTimeDate.getTime() / 1000);
		} else {
			_createTime = (int) (new Date().getTime() / 1000);
		}
	}

	private UserApp _userApp;

	/**
	 * 所属的用户应用
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userAppId", nullable = false)
	public UserApp getUserApp() {
		return _userApp;
	}

	public void setUserApp(UserApp userApp) {
		_userApp = userApp;
	}
}
