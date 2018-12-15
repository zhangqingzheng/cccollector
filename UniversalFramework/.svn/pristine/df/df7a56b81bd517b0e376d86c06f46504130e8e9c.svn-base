/**
 * 
 */
package com.cccollector.universal.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class Role implements Serializable {
	
	private static final long serialVersionUID = -4591641018756223499L;

	private Integer _roleId;

	/**
	 * 角色ID
	 */
	public Integer getRoleId() {
		return _roleId;
	}

	public void setRoleId(Integer roleId) {
		_roleId = roleId;
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

	private String _remarks;

	/**
	 * 备注
	 */
	public String getRemarks() {
		return _remarks;
	}

	public void setRemarks(String remarks) {
		_remarks = remarks;
	}	

	private Boolean _enabled;

	/**
	 * 是否可用
	 */
	public Boolean getEnabled() {
		return _enabled;
	}

	public void setEnabled(Boolean enabled) {
		_enabled = enabled;
	}

    private Platform _platform;

    /**
	 * 所属的平台
	 */
	public Platform getPlatform() {
		return _platform;
	}

	public void setPlatform(Platform platform) {
		_platform = platform;
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
	
	private List<Department> _departments = new ArrayList<Department>();

	/**
	 * 关联的部门
	 */
	public List<Department> getDepartments() {
		return _departments;
	}

	public void setDepartments(List<Department> departments) {
		_departments = departments;
	}	
	
	private List<Job> _jobs = new ArrayList<Job>();
	
	/**
	 * 关联的岗位
	 */
	public List<Job> getJobs() {
		return _jobs;
	}
	
	public void setJobs(List<Job> jobs) {
		_jobs = jobs;
	}	
}
