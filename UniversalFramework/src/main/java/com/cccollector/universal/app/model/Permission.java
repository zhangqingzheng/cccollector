/**
 * 
 */
package com.cccollector.universal.app.model;

import java.io.Serializable;

/**
 * 权限类
 * 
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class Permission implements Serializable {
	
	private static final long serialVersionUID = 2947824746903168851L;
	
	private Integer _permissionId;

	/**
	 * 权限ID
	 */
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
	 * 类别的枚举
	 */
	public TypeEnum getTypeEnum() {
		return TypeEnum.values()[_type];
	}

	private String _specificIds;

	/**
	 * 特定ID
	 */
	public String getSpecificIds() {
		return _specificIds;
	}

	public void setSpecificIds(String specificIds) {
		_specificIds = specificIds;
	}

	private Module _module;
	
	/**
	 * 所属的模块
	 */
	public Module getModule() {
		return _module;
	}

	public void setModule(Module module) {
		_module = module;
	}
}
