/**
 * 
 */
package com.cccollector.universal.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 岗位类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class Job implements Serializable {

	private static final long serialVersionUID = -7016631869151564312L;
	
	private Integer _jobId;

	/**
	 * 岗位ID
	 */
	public Integer getJobId() {
		return _jobId;
	}

	public void setJobId(Integer jobId) {
		_jobId = jobId;
	}

	private String _name;

	/**
	 * 名称
	 */
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}
	
	private Integer _position;

	/**
	 * 排序位置
	 */
	public Integer getPosition() {
		return _position;
	}

	public void setPosition(Integer position) {
		_position = position;
	}

	private User _user;
	
	/**
	 * 所属的用户
	 */
	public User getUser() {
		return _user;
	}
	
	public void setUser(User user) {
		_user = user;
	}
	
	private Department _department;
	
	/**
	 * 所属的部门
	 */
	public Department getDepartment() {
		return _department;
	}

	public void setDepartment(Department department) {
		_department = department;
	}
	
	private List<Role> _roles = new ArrayList<Role>();

	/**
	 * 关联的角色
	 */
	public List<Role> getRoles() {
		return _roles;
	}

	public void setRoles(List<Role> roles) {
		_roles = roles;
	}
	
	private List<Permission> _permissions = new ArrayList<Permission>();

	/**
	 * 关联的权限
	 */
	public List<Permission> getPermissions() {
		return _permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		_permissions = permissions;
	}	
}
