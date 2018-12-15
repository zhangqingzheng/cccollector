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

/**
 * 举报类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "user_reports")
public class Report implements Serializable {

	private static final long serialVersionUID = 2925188510204071898L;

	private Integer _reportId;

	/**
	 * 举报ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getReportId() {
		return _reportId;
	}

	public void setReportId(Integer reportId) {
		_reportId = reportId;
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
		恶意攻击谩骂,
		营销广告,
		违法信息,
		淫秽色情,
		其他
	}

	/**
	 * 类别枚举数组
	 */
	@Transient
	public TypeEnum[] getTypeEnums() {
		return TypeEnum.values();
	}

	/**
	 * 类别的枚举
	 */
	@Transient
	public TypeEnum getTypeEnum() {
		return TypeEnum.values()[_type];
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
		评论,
		回复
	}

	/**
	 * 关联内容类别枚举数组
	 */
	@Transient
	public ContentTypeEnum[] getContentTypeEnums() {
		return ContentTypeEnum.values();
	}

	/**
	 * 关联内容类别的枚举
	 */
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
	@Transient
	public String getRelativeContentName() {
		if (_relativeContent == null) {
			return null;
		}
		switch (getContentTypeEnum()) {
		case 评论:
			Comment comment = (Comment) _relativeContent;
			return comment.getContentToHtml();
		case 回复:
			Reply reply = (Reply) _relativeContent;
			return reply.getContentToHtml();
		default:
			return null;
		}
	}

	private Integer _status;

	/**
	 * 状态
	 */
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
		待审核,
		审核通过,
		审核未通过
	}

	/**
	 * 状态枚举数组
	 */
	@Transient
	public StatusEnum[] getStatusEnums() {
		return StatusEnum.values();
	}

	/**
	 * 状态的枚举
	 */
	@Transient
	public StatusEnum getStatusEnum() {
		return StatusEnum.values()[_status];
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
	@ManyToOne
	@JoinColumn(name = "userAppId", nullable = false)
	public UserApp getUserApp() {
		return _userApp;
	}

	public void setUserApp(UserApp userApp) {
		_userApp = userApp;
	}
}
