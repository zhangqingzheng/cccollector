/**
 * 
 */
package com.cccollector.universal.app.model;

import java.io.Serializable;

/**
 * 用户类
 *
 * @author 谢朋
 * Copyright © 2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = 7750147654601063104L;
	
	private Integer _userId;

	/**
	 * 用户ID
	 */
	public Integer getUserId() {
		return _userId;
	}

	public void setUserId(Integer userId) {
		_userId = userId;
	}

	private String _username;

	/**
	 * 用户名
	 */
	public String getUsername() {
		return _username;
	}

	public void setUsername(String username) {
		_username = username;
	}
	
	private String _realName;	

	/**
	 * 真实姓名
	 */	
	public String getRealName() {
		return _realName;
	}

	public void setRealName(String realName) {
		_realName = realName;
	}
	
	private Boolean _hasAvatar;

	/**
	 * 是否有头像
	 */
	public Boolean getHasAvatar() {
		return _hasAvatar;
	}

	public void setHasAvatar(Boolean hasAvatar) {
		_hasAvatar = hasAvatar;
	}
	
	private String _email;
	
	/**
	 * 电子邮箱
	 */	
	public String getEmail() {
		return _email;
	}

	public void setEmail(String email) {
		_email = email;
	}

	private String _cellphone;	

	/**
	 * 手机号
	 */
	public String getCellphone() {
		return _cellphone;
	}

	public void setCellphone(String cellphone) {
		_cellphone = cellphone;
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
		正式,
		试用,
		兼职,
		临时,
		离职
	}

	/**
	 * 类别的枚举
	 */
	public TypeEnum getTypeEnum() {
		return TypeEnum.values()[_type];
	}	
}
