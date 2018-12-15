/**
 * 
 */
package com.cccollector.news.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cccollector.universal.model.UniversalJsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 用户应用类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "user_userApps")
public class UserApp implements Serializable {

	private static final long serialVersionUID = 7270667953726331491L;

	private Integer _userAppId;

	/**
	 * 用户应用ID
	 */
	@Id
	public Integer getUserAppId() {
		return _userAppId;
	}

	public void setUserAppId(Integer userAppId) {
		_userAppId = userAppId;
	}

	private Integer _createTime;

	/**
	 * 创建时间
	 */
	@JsonIgnore
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

	private Date _syncTime;

	/**
	 * 同步时间
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Date getSyncTime() {
		return _syncTime;
	}

	public void setSyncTime(Date syncTime) {
		_syncTime = syncTime;
	}	

	private String _intro;

	/**
	 * 自我介绍
	 */
	@Column(nullable = true, length = 300)
	public String getIntro() {
		return _intro;
	}

	public void setIntro(String intro) {
		_intro = intro;
	}

	private Boolean _isRecentVisible;

	/**
	 * 动态是否可见
	 */
	@Column(nullable = false)
	public Boolean getIsRecentVisible() {
		return _isRecentVisible;
	}

	public void setIsRecentVisible(Boolean isRecentVisible) {
		_isRecentVisible = isRecentVisible;
	}

	/**
	 * 动态是否可见枚举
	 */
	public static enum IsRecentVisibleEnum {
		否,
		是
	}

	/**
	 * 动态是否可见的枚举
	 */
	@JsonIgnore
	@Transient
	public IsRecentVisibleEnum getIsRecentVisibleEnum() {
		int index = _isRecentVisible ? 1 : 0;
		return IsRecentVisibleEnum.values()[index];
	}
	
	private Integer _followCount;

	/**
	 * 关注数
	 */
	@Column(nullable = false)
	public Integer getFollowCount() {
		return _followCount;
	}

	public void setFollowCount(Integer followCount) {
		_followCount = followCount;
	}

	private Integer _followerCount;	

	/**
	 * 粉丝数
	 */
	@Column(nullable = false)
	public Integer getFollowerCount() {
		return _followerCount;
	}

	public void setFollowerCount(Integer followerCount) {
		_followerCount = followerCount;
	}

	private User _user;

	/**
	 * 所属的用户
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId", nullable = false)
	public User getUser() {
		return _user;
	}

	public void setUser(User user) {
		_user = user;
	}

	private App _app;

	/**
	 * 所属的应用
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "appId", nullable = false)
	public App getApp() {
		return _app;
	}

	public void setApp(App app) {
		_app = app;
	}

	/**
	 * 名称
	 */
	@JsonIgnore
	@Transient
	public String getName() {
		return _user.getNickName() + " @ " + _app.getName();
	}
	
	private Map<Integer, Follow> _follows = new HashMap<Integer, Follow>();

	/**
	 * 包含的关注
	 */
	@JsonView(UniversalJsonViews.Private.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userApp")
	@MapKeyColumn(name ="followedUserAppId")
	public Map<Integer, Follow> getFollows() {
		return _follows;
	}

	public void setFollows(Map<Integer, Follow> follows) {
		_follows = follows;
	}

	private Map<Integer, Follow> _followeds = new HashMap<Integer, Follow>();

	/**
	 * 包含的被关注
	 */
	@JsonIgnore	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "followedUserApp")
	@MapKey
	public Map<Integer, Follow> getFolloweds() {
		return _followeds;
	}

	public void setFolloweds(Map<Integer, Follow> followeds) {
		_followeds = followeds;
	}

	private List<Favorite> _favorites = new ArrayList<Favorite>();
	
	/**
	 * 包含的收藏
	 */
	@JsonIgnore	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userApp")
	public List<Favorite> getFavorites() {
		return _favorites;
	}
	
	public void setFavorites(List<Favorite> favorites) {
		_favorites = favorites;
	}
	
	private List<Like> _likes = new ArrayList<Like>();

	/**
	 * 包含的喜欢
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userApp")
	public List<Like> getLikes() {
		return _likes;
	}
	
	public void setLikes(List<Like> likes) {
		_likes = likes;
	}

	private List<Dislike> _dislikes = new ArrayList<Dislike>();
	
	/**
	 * 包含的不喜欢
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userApp")
	public List<Dislike> getDislikes() {
		return _dislikes;
	}
	
	public void setDislikes(List<Dislike> dislikes) {
		_dislikes = dislikes;
	}
	
	private List<Report> _reports = new ArrayList<Report>();
	
	/**
	 * 包含的举报
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userApp")
	public List<Report> getReports() {
		return _reports;
	}
	
	public void setReports(List<Report> reports) {
		_reports = reports;
	}
	
	private List<Comment> _comments = new ArrayList<Comment>();

	/**
	 * 包含的评论
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userApp")
	public List<Comment> getComments() {
		return _comments;
	}
	
	public void setComments(List<Comment> comments) {
		_comments = comments;
	}
	
	private List<Reply> _replies = new ArrayList<Reply>();
	
	/**
	 * 包含的回复
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userApp")
	public List<Reply> getReplies() {
		return _replies;
	}
	
	public void setReplies(List<Reply> replies) {
		_replies = replies;
	}

	private List<Log> _userAppLogs = new ArrayList<Log>();

	/**
	 * 包含的用户应用日志
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userApp")
	public List<Log> getUserAppLogs() {
		return _userAppLogs;
	}
	
	public void setUserAppLogs(List<Log> userAppLogs) {
		_userAppLogs = userAppLogs;
	}

	private List<Log> _activeUserAppLogs = new ArrayList<Log>();

	/**
	 * 包含的主动用户应用日志
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "activeUserApp")
	public List<Log> getActiveUserAppLogs() {
		return _activeUserAppLogs;
	}
	
	public void setActiveUserAppLogs(List<Log> activeUserAppLogs) {
		_activeUserAppLogs = activeUserAppLogs;
	}
}
