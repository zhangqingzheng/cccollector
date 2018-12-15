/**
 * 
 */
package com.cccollector.universal.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

/**
 * 部门类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class Department implements Serializable {

	private static final long serialVersionUID = 3369490936932996669L;
	
	private Integer _departmentId;

	/**
	 * 部门ID
	 */
	public Integer getDepartmentId() {
		return _departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		_departmentId = departmentId;
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

	private Boolean _hasBadge;

	/**
	 * 是否有徽章
	 */
	public Boolean getHasBadge() {
		return _hasBadge;
	}

	public void setHasBadge(Boolean hasBadge) {
		_hasBadge = hasBadge;
	}

	private Boolean _isIndependent;
	
	/**
	 * 是否独立机构
	 */
	@Column(nullable = false)
	public Boolean getIsIndependent() {
		return _isIndependent;
	}
	
	public void setIsIndependent(Boolean isIndependent) {
		_isIndependent = isIndependent;
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
	
	private Department _parentDepartment;

	/**
	 * 所属的父部门
	 */
	public Department getParentDepartment() {
		return _parentDepartment;
	}

	public void setParentDepartment(Department parentDepartment) {
		_parentDepartment = parentDepartment;
	}

	private Integer _parentDepartmentId;

	/**
	 * 所属的父部门ID
	 */	
	public Integer getParentDepartmentId() {
		return _parentDepartmentId;
	}

	public void setParentDepartmentId(Integer parentDepartmentId) {
		_parentDepartmentId = parentDepartmentId;
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

	private String _code;

	/**
	 * 排序代码
	 */
	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		_code = code;
	}

	private List<Department> _childDepartments = new ArrayList<Department>();

	/**
	 * 包含的子部门
	 */
	public List<Department> getChildDepartments() {
		return _childDepartments;
	}
	
	public void setChildDepartments(List<Department> childDepartments) {
		_childDepartments = childDepartments;
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
	
	private List<Job> _jobs = new ArrayList<Job>();

	/**
	 * 包含的岗位
	 */
	public List<Job> getJobs() {
		return _jobs;
	}

	public void setJobs(List<Job> jobs) {
		_jobs = jobs;
	}
}
