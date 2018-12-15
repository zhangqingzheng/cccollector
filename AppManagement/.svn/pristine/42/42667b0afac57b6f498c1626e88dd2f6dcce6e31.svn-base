/**
 * 
 */
package com.cccollector.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 模块类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "plat_modules")
public class Module implements Serializable {
	
	private static final long serialVersionUID = -4591641018756223499L;

	private Integer _moduleId;

	/**
	 * 模块ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getModuleId() {
		return _moduleId;
	}

	public void setModuleId(Integer moduleId) {
		_moduleId = moduleId;
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

	private String _identifier;

	/**
	 * 标识符
	 */
	@Column(nullable = false, length = 100)
	public String getIdentifier() {
		return _identifier;
	}

	public void setIdentifier(String identifier) {
		_identifier = identifier;
	}

	private String _remarks;

	/**
	 * 备注
	 */
	@JsonIgnore
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
	
	private Integer _position;

	/**
	 * 排序位置
	 */
	@JsonIgnore
	@Column(nullable = false)
	public Integer getPosition() {
		return _position;
	}

	public void setPosition(Integer position) {
		_position = position;
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

	private List<Permission> _permissions = new ArrayList<Permission>();
	
	/**
	 * 包含的权限
	 */
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "module")
	@OrderBy("type")
	public List<Permission> getPermissions() {
		return _permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		_permissions = permissions;
	}	

	private Map<Integer, Permission> _permissionMap = new HashMap<Integer, Permission>();
	
	/**
	 * 包含的权限映射
	 */
	@Transient
	public Map<Integer, Permission> getPermissionMap() {
		return _permissionMap;
	}

	public void setPermissionMap(Map<Integer, Permission> permissionMap) {
		_permissionMap = permissionMap;
	}	
}
