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

import com.cccollector.universal.model.UniversalJsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 回复类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "user_replies")
public class Reply implements Serializable {

	private static final long serialVersionUID = -1919205203035484008L;

	private Integer _replyId;

	/**
	 * 回复ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getReplyId() {
		return _replyId;
	}
	
	public void setReplyId(Integer replyId) {
		_replyId = replyId;
	}

	private String _content;

	/**
	 * 内容
	 */
	@Column(nullable = false, length = 1200)
	public String getContent() {
		return _content;
	}

	public void setContent(String content) {
		_content = content;
	}

	/**
	 * HTML内容
	 */
	@JsonIgnore
	@Transient
	public String getContentToHtml() {
		return Comment.contentToHtml(_content);
	}

	/**
	 * 显示名称
	 */
	@JsonIgnore
	@Transient
	public String getName() {
		String contentToHtml = getContentToHtml();
		if (contentToHtml == null || contentToHtml.length() <= 40) {
			return contentToHtml;
		}
		return contentToHtml.substring(0, 20);
	}

	private Integer _replyUserAppId;

	/**
	 * 回复用户应用ID
	 */
	@Column(nullable = false)
	public Integer getReplyUserAppId() {
		return _replyUserAppId;
	}
	
	public void setReplyUserAppId(Integer replyUserAppId) {
		_replyUserAppId = replyUserAppId;
	}
	
	private UserApp _replyUserApp;
	
	/**
	 * 回复用户应用
	 */
	@JsonIgnore
	@Transient
	public UserApp getReplyUserApp() {
		return _replyUserApp;
	}
	
	public void setReplyUserApp(UserApp replyUserApp) {
		_replyUserApp = replyUserApp;
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
		已发布,
		审核未通过,
		用户已删除,
		系统已删除
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
	 * 状态的枚举
	 */
	@JsonIgnore
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
	
	private Comment _comment;

	/**
	 * 所属的评论
	 */
	@JsonView(UniversalJsonViews.Public.class)
	@ManyToOne
	@JoinColumn(name = "commentId", nullable = false)
	public Comment getComment() {
		return _comment;
	}
	
	public void setComment(Comment comment) {
		_comment = comment;
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

	private Integer _userAppId;
	
	/**
	 * 所属用户应用ID
	 */
	@Transient
	public Integer getUserAppId() {
		return _userAppId;
	}
	
	public void setUserAppId(Integer userAppId) {
		_userAppId = userAppId;
	}
}
