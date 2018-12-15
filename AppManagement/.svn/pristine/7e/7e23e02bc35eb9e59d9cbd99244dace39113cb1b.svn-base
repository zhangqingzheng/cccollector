/**
 * 
 */
package com.cccollector.app.service;

import com.cccollector.app.model.Permission;
import com.cccollector.universal.service.GenericService;

/**
 * 权限服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface PermissionService extends GenericService<Integer, Permission> {
	
	/**
	 * 添加权限到角色
	 * 
	 * @param permissionId 权限ID
	 * @param roleId 角色ID
	 */
	public void addPermissionToRole(int permissionId, int roleId);
	
	/**
	 * 从角色删除权限
	 * 
	 * @param permissionId 权限ID
	 * @param roleId 角色ID
	 */
	public void deletePermissionFromRole(int permissionId, int departmentId);
	
	/**
	 * 添加权限到部门
	 * 
	 * @param permissionId 权限ID
	 * @param departmentId 部门ID
	 */
	public void addPermissionToDepartment(int permissionId, int departmentId);
	
	/**
	 * 从部门删除权限
	 * 
	 * @param permissionId 权限ID
	 * @param departmentId 部门ID
	 */
	public void deletePermissionFromDepartment(int permissionId, int departmentId);
	
	/**
	 * 添加权限到岗位
	 * 
	 * @param permissionId 权限ID
	 * @param jobId 岗位ID
	 */
	public void addPermissionToJob(int permissionId, int jobId);
	
	/**
	 * 从岗位删除权限
	 * 
	 * @param permissionId 权限ID
	 * @param jobId 岗位ID
	 */
	public void deletePermissionFromJob(int permissionId, int jobId);
}
