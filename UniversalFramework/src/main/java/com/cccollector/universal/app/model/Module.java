/**
 * 
 */
package com.cccollector.universal.app.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 模块类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class Module implements Serializable {

	private static final long serialVersionUID = -8264281287814550583L;
	
	private Integer _moduleId;

	/**
	 * 模块ID
	 */
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
	public String getIdentifier() {
		return _identifier;
	}

	public void setIdentifier(String identifier) {
		_identifier = identifier;
	}

	private Map<Integer, Permission> _permissionMap = new HashMap<Integer, Permission>();
	
	/**
	 * 包含的权限映射
	 */
	public Map<Integer, Permission> getPermissionMap() {
		return _permissionMap;
	}

	public void setPermissionMap(Map<Integer, Permission> permissionMap) {
		_permissionMap = permissionMap;
	}	
}
