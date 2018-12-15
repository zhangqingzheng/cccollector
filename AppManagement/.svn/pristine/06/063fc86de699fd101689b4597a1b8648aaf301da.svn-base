/**
 * 
 */
package com.cccollector.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.app.dao.DepartmentDao;
import com.cccollector.app.dao.JobDao;
import com.cccollector.app.dao.RoleDao;
import com.cccollector.app.model.Department;
import com.cccollector.app.model.Job;
import com.cccollector.app.model.Role;
import com.cccollector.app.service.RoleService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 角色服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("roleService")
public class RoleServiceImpl extends GenericServiceHibernateImpl<Integer, Role> implements RoleService {
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private JobDao jobDao;

	@Override
	public void addRole(Role role) {
		// 设置排序位置
		ArrayList<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("platform", new QueryField("platformId", role.getPlatform().getPlatformId())));
		Integer position = roleDao.max("position", predicateQueryFieldList);
		role.setPosition(position == null ? 0 : position.intValue() + 1);
		
		// 保存角色
		roleDao.save(role);
	}

	@Override
	public void moveRoles(List<Role> roles) {
		int position = 0;
		for (Role role : roles) {
			role.setPosition(position);
			roleDao.update(role, "position");
			position++;
		}
	}
	
	@Override
	public void addRoleToDepartment(int roleId, int departmentId) {
		Role role = roleDao.loadById(roleId);
		Department department = departmentDao.loadById(departmentId, "roles");
		department.getRoles().add(role);
	}
	
	@Override
	public void deleteRoleFromDepartment(int roleId, int departmentId) {
		Role role = roleDao.loadById(roleId);
		Department department = departmentDao.loadById(departmentId, "roles");
		department.getRoles().remove(role);
	}

	@Override
	public void addRoleToJob(int roleId, int jobId) {
		Role role = roleDao.loadById(roleId);
		Job job = jobDao.loadById(jobId, "roles");
		job.getRoles().add(role);
	}

	@Override
	public void deleteRoleFromJob(int roleId, int jobId) {
		Role role = roleDao.loadById(roleId);
		Job job = jobDao.loadById(jobId, "roles");
		job.getRoles().remove(role);
	}
}
