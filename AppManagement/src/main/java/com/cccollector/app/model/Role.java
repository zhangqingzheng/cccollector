/**
 * 
 */
package com.cccollector.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 角色类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "plat_roles")
public class Role implements Serializable {
	
	private static final long serialVersionUID = -4591641018756223499L;

	private Integer _roleId;

	/**
	 * 角色ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@Column(nullable = false, length = 100)
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
	@Column(nullable = true, length = 1000)
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
	@Column(nullable = false)
	public Boolean getEnabled() {
		return _enabled;
	}

	public void setEnabled(Boolean enabled) {
		_enabled = enabled;
	}

	/**
	 * 是否可用枚举
	 */
	public static enum EnabledEnum {
		否,
		是
	}

	/**
	 * 是否可用的枚举
	 */
	@Transient
	public EnabledEnum getEnabledEnum() {
		int index = _enabled ? 1 : 0;
		return EnabledEnum.values()[index];
	}	
	
    private Platform _platform;

    /**
	 * 所属的平台
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "platformId", nullable = false)
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
	@Column(nullable = false)
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
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "plat_rolePermissions", joinColumns = @JoinColumn(name = "roleId", nullable = false), inverseJoinColumns = @JoinColumn(name = "permissionId", nullable = false))
	@OrderBy("moduleId, type")		
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
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	@OrderBy("code")
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
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	@OrderBy("departmentId, position")
	public List<Job> getJobs() {
		return _jobs;
	}
	
	public void setJobs(List<Job> jobs) {
		_jobs = jobs;
	}	
}
