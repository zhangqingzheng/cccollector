/**
 * 
 */
package com.cccollector.news.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 评论审核类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "user_comment_reviews")
public class CommentReview implements Serializable {

	private static final long serialVersionUID = 9150952677630175116L;
	
	private CommentReviewId _commentReviewId;

	/**
	 * 评论审核ID
	 */
	@EmbeddedId
	public CommentReviewId getCommentReviewId() {
		return _commentReviewId;
	}

	public void setCommentReviewId(CommentReviewId commentReviewId) {
		_commentReviewId = commentReviewId;
	}

	/**
	 * 类别枚举
	 */
	public static enum TypeEnum {
		评论,
		回复
	}

	/**
	 * 评论审核类别
	 */
	@Transient
	public TypeEnum getTypeEnum() {
		return _commentReviewId.getReplyId() == 0 ? TypeEnum.评论 : TypeEnum.回复;
	}
	
	private Comment _comment;

	/**
	 * 评论对象
	 */
	@Transient
	public Comment getComment() {
		return _comment;
	}
	
	public void setComment(Comment comment) {
		_comment = comment;
	}
	
	private Reply _reply;

	/**
	 * 回复对象
	 */
	@Transient
	public Reply getReply() {
		return _reply;
	}
	
	public void setReply(Reply reply) {
		_reply = reply;
	}

	private String _content;

	/**
	 * 内容
	 */
	@Column(nullable = false)
	public String getContent() {
		return _content;
	}

	public void setContent(String content) {
		_content = content;
	}

	/**
	 * HTML内容
	 */
	@Transient
	public String getContentToHtml() {
		return Comment.contentToHtml(_content);
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

	private Integer _userAppId;
	
	/**
	 * 所属的用户应用ID
	 */
	@Column(nullable = false)
	public Integer getUserAppId() {
		return _userAppId;
	}
	
	public void setUserAppId(Integer userAppId) {
		_userAppId = userAppId;
	}
	
	private UserApp _userApp;
	
	/**
	 * 用户应用对象
	 */
	@Transient
	public UserApp getUserApp() {
		return _userApp;
	}
	
	public void setUserApp(UserApp userApp) {
		_userApp = userApp;
	}
}
