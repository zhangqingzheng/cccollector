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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 权限类
 * 
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "plat_permissions")
public class Permission implements Serializable {

	private static final long serialVersionUID = 1444865703711458412L;
	
	private Integer _permissionId;

	/**
	 * 权限ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getPermissionId() {
		return _permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		_permissionId = permissionId;
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

	private Integer _type;

	/**
	 * 类别
	 */
	@Column(nullable = false)
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
		浏览,
		管理,
		添加,
		修改,
		删除,
		审核,
		发布,
		禁用,
		操作一,
		操作二,
		操作三
	}

	/**
	 * 类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public TypeEnum[] getTypeEnums() {
		return TypeEnum.values();
	}

	/**
	 * 类别的枚举
	 */
	@JsonIgnore
	@Transient
	public TypeEnum getTypeEnum() {
		return TypeEnum.values()[_type];
	}

	private String _specificIds;

	/**
	 * 特定ID
	 */
	@Column(nullable = true, length = 1000)
	public String getSpecificIds() {
		return _specificIds;
	}

	public void setSpecificIds(String specificIds) {
		_specificIds = specificIds;
	}

	private Boolean _enabled;

	/**
	 * 是否可用
	 */
	@JsonIgnore
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
	@JsonIgnore
	@Transient
	public EnabledEnum getEnabledEnum() {
		int index = _enabled ? 1 : 0;
		return EnabledEnum.values()[index];
	}	
	
	private Module _module;
	
	/**
	 * 所属的模块
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "moduleId", nullable = false)
	public Module getModule() {
		return _module;
	}

	public void setModule(Module module) {
		_module = module;
	}
	
	private List<Role> _roles = new ArrayList<Role>();
	
	/**
	 * 关联的角色
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
	@OrderBy("platformId, position")
	public List<Role> getRoles() {
		return _roles;
	}

	public void setRoles(List<Role> roles) {
		_roles = roles;
	}
	
	private List<Department> _departments = new ArrayList<Department>();

	/**
	 * 关联的部门
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
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
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
	@OrderBy("departmentId, position")
	public List<Job> getJobs() {
		return _jobs;
	}
	
	public void setJobs(List<Job> jobs) {
		_jobs = jobs;
	}		
}
