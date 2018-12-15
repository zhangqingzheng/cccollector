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
 * 关注类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "user_follows")
public class Follow implements Serializable {

	private static final long serialVersionUID = 1313825575968052139L;

	private Integer _followId;

	/**
	 * 关注ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getFollowId() {
		return _followId;
	}

	public void setFollowId(Integer followId) {
		_followId = followId;
	}

	private Boolean _mutual;

	/**
	 * 是否互相关注
	 */
	@Column(nullable = false)
	public Boolean getMutual() {
		return _mutual;
	}

	public void setMutual(Boolean mutual) {
		_mutual = mutual;
	}

	/**
	 * 是否互相关注枚举
	 */
	public static enum MutualEnum {
		否,
		是
	}

	/**
	 * 是否互相关注的枚举
	 */
	@JsonIgnore
	@Transient
	public MutualEnum getMutualEnum() {
		int index = _mutual ? 1 : 0;
		return MutualEnum.values()[index];
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

	private UserApp _followedUserApp;

	/**
	 * 所属的被关注用户应用
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "followedUserAppId", nullable = false)
	public UserApp getFollowedUserApp() {
		return _followedUserApp;
	}

	public void setFollowedUserApp(UserApp followedUserApp) {
		_followedUserApp = followedUserApp;
	}
}
