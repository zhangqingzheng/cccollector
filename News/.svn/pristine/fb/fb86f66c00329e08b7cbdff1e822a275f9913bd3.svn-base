/**
 * 
 */
package com.cccollector.news.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringEscapeUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 评论类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "user_comments")
public class Comment implements Serializable {

	private static final long serialVersionUID = 3994213257720020569L;

	private Integer _commentId;

	/**
	 * 评论ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getCommentId() {
		return _commentId;
	}

	public void setCommentId(Integer commentId) {
		_commentId = commentId;
	}

	private String _content;

	/**
	 * 内容
	 */
	@Column(nullable = false, length = 3600)
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
		if (contentToHtml == null || contentToHtml.length() <= 20) {
			return contentToHtml;
		}
		return contentToHtml.substring(0, 20);
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
	 * 关联内容类别的枚举
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

	private Integer _floorNumber;

	/**
	 * 楼层数
	 */
	@Column(nullable = false)
	public Integer getFloorNumber() {
		return _floorNumber;
	}

	public void setFloorNumber(Integer floorNumber) {
		_floorNumber = floorNumber;
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

	private Integer _replyCount;

	/**
	 * 包含回复数
	 */
	@Column(nullable = false)
	public Integer getReplyCount() {
		return _replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		_replyCount = replyCount;
	}

	private Integer _likeCount;

	/**
	 * 获得喜欢数
	 */
	@Column(nullable = false)
	public Integer getLikeCount() {
		return _likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		_likeCount = likeCount;
	}

	private Like _like;

	/**
	 * 喜欢对象
	 */
	@Transient
	public Like getLike() {
		return _like;
	}

	public void setLike(Like like) {
		_like = like;
	}

	private List<Reply> _replies = new ArrayList<Reply>();

	/**
	 * 包含的回复
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "comment")
	@OrderBy("createTime")	
	public List<Reply> getReplies() {
		return _replies;
	}
	
	public void setReplies(List<Reply> replies) {
		_replies = replies;
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
	 * 所属的用户应用ID
	 */
	@Transient
	public Integer getUserAppId() {
		return _userAppId;
	}
	
	public void setUserAppId(Integer userAppId) {
		_userAppId = userAppId;
	}

	/**
	 * 内容转为HTML
	 */
	public static String contentToHtml(String content) {
		if (content == null) {
			return null;
		}
		
		StringBuffer html = new StringBuffer();
		String[] hex = content.split("\\\\u");
		String regex1 = "[d][8-9a-f][0-9a-f][0-9a-f]";
		String regex2 = "[ef][0-9a-f][0-9a-f][0-9a-f]";
		for (int i = 0; i < hex.length; i++) {
			// 截取前4位匹配正则表达式，不匹配则不包含表情符号
			String subHex = hex[i].substring(0, Math.min(4, hex[i].length()));
			if (!subHex.matches(regex1) && !subHex.matches(regex2)) {
				html.append(hex[i]);
				continue;
			}
			
			// 如果包含表情符号，则转换
			html.append((char) Integer.parseInt(hex[i].substring(0, 4), 16));

			// 剩余部分直接拼接
			html.append(hex[i].substring(4, hex[i].length()));
		}
		
		return StringEscapeUtils.escapeHtml4(html.toString()).replace(" ", "&nbsp;").replace("\n", "<br/>");
	}
}
