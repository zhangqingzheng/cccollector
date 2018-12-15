/**
 * 
 */
package com.cccollector.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.app.dao.DepartmentDao;
import com.cccollector.app.dao.JobDao;
import com.cccollector.app.dao.PermissionDao;
import com.cccollector.app.dao.RoleDao;
import com.cccollector.app.model.Department;
import com.cccollector.app.model.Job;
import com.cccollector.app.model.Permission;
import com.cccollector.app.model.Role;
import com.cccollector.app.service.PermissionService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 权限服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("permissionService")
public class PermissionServiceImpl extends GenericServiceHibernateImpl<Integer, Permission> implements PermissionService {
	
	@Autowired
	private PermissionDao permissionDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private JobDao jobDao;

	@Override
	public void addPermissionToRole(int permissionId, int roleId) {
		Permission permission = permissionDao.loadById(permissionId);
		Role role = roleDao.loadById(roleId, "permissions");
		role.getPermissions().add(permission);
	}

	@Override
	public void deletePermissionFromRole(int permissionId, int roleId) {
		Permission permission = permissionDao.loadById(permissionId);
		Role role = roleDao.loadById(roleId, "permissions");
		role.getPermissions().remove(permission);
	}
	
	@Override
	public void addPermissionToDepartment(int permissionId, int departmentId) {
		Permission permission = permissionDao.loadById(permissionId);
		Department department = departmentDao.loadById(departmentId, "permissions");
		department.getPermissions().add(permission);
	}
	
	@Override
	public void deletePermissionFromDepartment(int permissionId, int departmentId) {
		Permission permission = permissionDao.loadById(permissionId);
		Department department = departmentDao.loadById(departmentId, "permissions");
		department.getPermissions().remove(permission);
	}
	
	@Override
	public void addPermissionToJob(int permissionId, int jobId) {
		Permission permission = permissionDao.loadById(permissionId);
		Job job = jobDao.loadById(jobId, "permissions");
		job.getPermissions().add(permission);
	}

	@Override
	public void deletePermissionFromJob(int permissionId, int jobId) {
		Permission permission = permissionDao.loadById(permissionId);
		Job job = jobDao.loadById(jobId, "permissions");
		job.getPermissions().remove(permission);
	}
}
