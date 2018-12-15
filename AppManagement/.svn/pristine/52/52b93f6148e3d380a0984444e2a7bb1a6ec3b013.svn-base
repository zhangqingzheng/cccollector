/**
 * 
 */
package com.cccollector.app.service;

import java.util.List;

import com.cccollector.app.model.Role;
import com.cccollector.universal.service.GenericService;

/**
 * 角色服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface RoleService extends GenericService<Integer, Role> {
	
	/**
	 * 添加角色
	 * 
	 * @param role 待添加的角色
	 */
	public void addRole(Role role);
	
	/**
	 * 移动角色
	 * 
	 * @param roles 移动后的角色
	 */
	public void moveRoles(List<Role> roles);
	
	/**
	 * 添加角色到部门
	 * 
	 * @param roleId 角色ID
	 * @param departmentId 部门ID
	 */
	public void addRoleToDepartment(int roleId, int departmentId);
	
	/**
	 * 从部门删除角色
	 * 
	 * @param roleId 角色ID
	 * @param departmentId 部门ID
	 */
	public void deleteRoleFromDepartment(int roleId, int departmentId);
	
	/**
	 * 添加角色到岗位
	 * 
	 * @param roleId 角色ID
	 * @param jobId 岗位ID
	 */
	public void addRoleToJob(int roleId, int jobId);
	
	/**
	 * 从岗位删除角色
	 * 
	 * @param roleId 角色ID
	 * @param jobId 岗位ID
	 */
	public void deleteRoleFromJob(int roleId, int jobId);
}
