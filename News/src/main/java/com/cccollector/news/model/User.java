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
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 用户类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
@Entity
@Table(name = "user_users")
public class User implements Serializable {
	
	private static final long serialVersionUID = 3508277587863305554L;
	
	private Integer _userId;

	/**
	 * 用户ID
	 */
	@Id
	public Integer getUserId() {
		return _userId;
	}

	public void setUserId(Integer userId) {
		_userId = userId;
	}
	
	private String _nickName;

	/**
	 * 昵称
	 */
	@Column(nullable = false, length = 100)
	public String getNickName() {
		return _nickName;
	}

	public void setNickName(String nickName) {
		_nickName = nickName;
	}

	private String _cellphone;

	/**
	 * 手机号
	 */
	@Column(nullable = true, length = 100)
	public String getCellphone() {
		return _cellphone;
	}

	public void setCellphone(String cellphone) {
		_cellphone = cellphone;
	}
	
	private String _email;

	/**
	 * 电子邮箱
	 */
	@Column(nullable = true, length = 100)
	public String getEmail() {
		return _email;
	}

	public void setEmail(String email) {
		_email = email;
	}
	
	private Integer _registerTime;

	/**
	 * 注册时间
	 */
	@Column(nullable = false)
	public Integer getRegisterTime() {
		return _registerTime;
	}

	public void setRegisterTime(Integer registerTime) {
		_registerTime = registerTime;
	}
	
	/**
	 * Date类型注册时间
	 */
	@JsonIgnore
	@Transient
	public Date getRegisterTimeDate() {
		if (_registerTime != null) {
			return new Date((long) _registerTime * 1000);
		}
		return null;
	}
	
	@Transient
	public void setRegisterTimeDate(Date registerTimeDate) {
		if (registerTimeDate != null) {
			_registerTime = (int) (registerTimeDate.getTime() / 1000);
		} else {
			_registerTime = (int) (new Date().getTime() / 1000);
		}
	}
	
	private List<UserApp> _userApps = new ArrayList<UserApp>();

	/**
	 * 包含的用户应用
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	@OrderBy("createTime DESC")
	public List<UserApp> getUserApps() {
		return _userApps;
	}
	
	public void setUserApps(List<UserApp> userApps) {
		_userApps = userApps;
	}	
}
