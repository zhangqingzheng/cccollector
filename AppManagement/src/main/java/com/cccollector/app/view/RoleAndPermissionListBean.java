/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.app.model.Department;
import com.cccollector.app.model.Job;
import com.cccollector.app.model.Permission;
import com.cccollector.app.model.Platform;
import com.cccollector.app.model.Role;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 角色和权限列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class RoleAndPermissionListBean extends BaseListBean<Role> implements Serializable {

	private static final long serialVersionUID = -6526535275427576171L;

	public RoleAndPermissionListBean() {
		modelDisplayName = "角色";
		modelAttributeName = "role";
		modelIdAttributeName = "roleId";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Role>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return departmentService.loadById(relatedModelId);
				} else if (index == 2) {
					return jobService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Role> loadAllModelList() {
				if (selectedPlatformId == null) {
					return null;
				}

				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				if (getJob() == null) {
					predicateQueryFieldList.add(new QueryField("departments", new QueryField("departmentId", getDepartment().getDepartmentId())));
				} else {
					predicateQueryFieldList.add(new QueryField("jobs", new QueryField("jobId", getJob().getJobId())));
				}
				predicateQueryFieldList.add(new QueryField("platform", new QueryField("platformId", selectedPlatformId)));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("position", true));
				return roleService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}
		};
	}
	
	/**
	 * 关联部门
	 */
	public Department getDepartment() {
		return (Department) relatedModel(1);
	}
	
	/**
	 * 关联岗位
	 */
	public Job getJob() {
		return (Job) relatedModel(2);
	}
	
	/**
	 * 上级显示名称
	 */
	public String getSuperiorDisplayName() {
		return getJob() == null ? "部门" : "岗位";
	}

	/**
	 * 全部平台
	 */
	private List<Platform> allPlatforms;

	public List<Platform> getAllPlatforms() {
		if (allPlatforms == null) {
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("enabled", false));
			orderQueryFieldList.add(new QueryField("name", true));
			allPlatforms = platformService.loadAll(orderQueryFieldList);
		}
		return allPlatforms;
	}

	/**
	 * 选中的平台ID
	 */
	private Integer selectedPlatformId;

	public Integer getSelectedPlatformId() {
		return selectedPlatformId;
	}

	public void setSelectedPlatformId(Integer _selectedPlatformId) {
		selectedPlatformId = _selectedPlatformId;
	}

	/**
	 * 改变平台
	 */
	public void changePlatformAction() {
		// 清空选中的模型
		setSelectedModels(null);
		// 清空选中的模型ID
		setSelectedModelId(null);
		// 刷新全部模型
		allModels = null;
		
		// 清空选中的权限
		selectedPermissions = null;
		// 清空选中的权限ID
		selectedPermissionId = null;
		// 刷新全部权限
		allPermissions = null;
	}

	/**
	 * 未被部门或岗位使用的角色
	 */
	public List<Role> getNotUsedRoles() {
		if (selectedPlatformId == null) {
			return null;
		}

		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		if (getJob() == null) {
			predicateQueryFieldList.add(new QueryField("departments", new QueryField("departmentId", getDepartment().getDepartmentId(), PredicateEnum.NOT_IN)));
		} else {
			predicateQueryFieldList.add(new QueryField("jobs", new QueryField("jobId", getJob().getJobId(), PredicateEnum.NOT_IN)));
		}
		predicateQueryFieldList.add(new QueryField("platform", new QueryField("platformId", selectedPlatformId)));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("enabled", false));
		orderQueryFieldList.add(new QueryField("position", true));
		return roleService.loadAll(predicateQueryFieldList, orderQueryFieldList);
	}
	
	/**
	 * 添加角色
	 */
	public void addRoleAction() {
		if (getJob() == null) {
			// 添加角色到部门
			roleService.addRoleToDepartment(getSelectedModelId(), getDepartment().getDepartmentId());
			addInfoMessage("添加角色到部门成功！");
		} else {
			// 添加角色到岗位
			roleService.addRoleToJob(getSelectedModelId(), getJob().getJobId());
			addInfoMessage("添加角色到岗位成功！");
		}
		
		// 清空选中的模型
		setSelectedModels(null);
		// 清空选中的模型ID
		setSelectedModelId(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 删除角色
	 */
	public void deleteRoleAction(Role role) {
		if (getJob() == null) {
			// 从部门中删除角色
			roleService.deleteRoleFromDepartment(role.getRoleId(), getDepartment().getDepartmentId());
			addInfoMessage("从部门中删除角色成功！");
		} else {
			// 从岗位中删除角色
			roleService.deleteRoleFromJob(role.getRoleId(), getJob().getJobId());
			addInfoMessage("从岗位中删除角色成功！");
		}
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除角色
	 */
	public void deleteRolesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个角色进行删除！");
			return;
		}
		
		if (getJob() == null) {
			// 从部门中删除角色
			for (Role role : getSelectedModels()) {
				roleService.deleteRoleFromDepartment(role.getRoleId(), getDepartment().getDepartmentId());
			}
			addInfoMessage("从部门中删除角色成功！");
		} else {
			// 从岗位中删除角色
			for (Role role : getSelectedModels()) {
				roleService.deleteRoleFromJob(role.getRoleId(), getJob().getJobId());
			}
			addInfoMessage("从岗位中删除角色成功！");
		}
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 全部权限
	 */
	protected List<Permission> allPermissions;

	public List<Permission> getAllPermissions() {
		if (allPermissions == null) {
			if (selectedPlatformId == null) {
				return null;
			}

			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			if (getJob() == null) {
				predicateQueryFieldList.add(new QueryField("departments", new QueryField("departmentId", getDepartment().getDepartmentId())));
			} else {
				predicateQueryFieldList.add(new QueryField("jobs", new QueryField("jobId", getJob().getJobId())));
			}
			predicateQueryFieldList.add(new QueryField("module", new QueryField("platform", new QueryField("platformId", selectedPlatformId))));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("module", new QueryField("position", true)));
			orderQueryFieldList.add(new QueryField("type", true));
			allPermissions = permissionService.loadAll(predicateQueryFieldList, orderQueryFieldList);
		}
		return allPermissions;
	}

	/**
	 * 选中的权限
	 */
	private List<Permission> selectedPermissions;

	public List<Permission> getSelectedPermissions() {
		return selectedPermissions;
	}

	public void setSelectedPermissions(List<Permission> _selectedPermissions) {
		selectedPermissions = _selectedPermissions;
	}

	/**
	 * 未被部门或岗位使用的权限
	 */
	public List<Permission> getNotUsedPermissions() {
		if (selectedPlatformId == null) {
			return null;
		}

		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		if (getJob() == null) {
			predicateQueryFieldList.add(new QueryField("departments", new QueryField("departmentId", getDepartment().getDepartmentId(), PredicateEnum.NOT_IN)));
		} else {
			predicateQueryFieldList.add(new QueryField("jobs", new QueryField("jobId", getJob().getJobId(), PredicateEnum.NOT_IN)));
		}
		predicateQueryFieldList.add(new QueryField("module", new QueryField("platform", new QueryField("platformId", selectedPlatformId))));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("module", new QueryField("position", true)));
		orderQueryFieldList.add(new QueryField("type", true));
		return permissionService.loadAll(predicateQueryFieldList, orderQueryFieldList);
	}

	/**
	 * 选中的权限ID
	 */
	private Integer selectedPermissionId;

	public Integer getSelectedPermissionId() {
		return selectedPermissionId;
	}

	public void setSelectedPermissionId(Integer _selectedPermissionId) {
		selectedPermissionId = _selectedPermissionId;
	}
	
	/**
	 * 添加权限
	 */
	public void addPermissionAction() {
		if (getJob() == null) {
			// 添加权限到部门
			permissionService.addPermissionToDepartment(selectedPermissionId, getDepartment().getDepartmentId());
			addInfoMessage("添加权限到部门成功！");
		} else {
			// 添加权限到岗位
			permissionService.addPermissionToJob(selectedPermissionId, getJob().getJobId());
			addInfoMessage("添加权限到岗位成功！");
		}
		
		// 清空选中的权限
		selectedPermissions = null;
		// 清空选中的权限ID
		selectedPermissionId = null;
		// 刷新全部权限
		allPermissions = null;
	}
	
	/**
	 * 删除权限
	 */
	public void deletePermissionAction(Permission permission) {
		if (getJob() == null) {
			// 从部门中删除权限
			permissionService.deletePermissionFromDepartment(permission.getPermissionId(), getDepartment().getDepartmentId());
			addInfoMessage("从部门中删除权限成功！");
		} else {
			// 从岗位中删除权限
			permissionService.deletePermissionFromJob(permission.getPermissionId(), getJob().getJobId());
			addInfoMessage("从岗位中删除权限成功！");
		}
		
		// 清空选中的权限
		selectedPermissions = null;
		// 刷新全部权限
		allPermissions = null;
	}
	
	/**
	 * 批量删除权限
	 */
	public void deletePermissionsAction() {
		if (selectedPermissions == null || selectedPermissions.size() == 0) {
			addErrorMessage("请选择一个或多个权限进行删除！");
			return;
		}
		
		if (getJob() == null) {
			// 从部门中删除权限
			for (Permission permission : selectedPermissions) {
				permissionService.deletePermissionFromDepartment(permission.getPermissionId(), getDepartment().getDepartmentId());
			}
			addInfoMessage("从部门中删除权限成功！");
		} else {
			// 从岗位中删除权限
			for (Permission permission : selectedPermissions) {
				permissionService.deletePermissionFromJob(permission.getPermissionId(), getJob().getJobId());
			}
			addInfoMessage("从岗位中删除权限成功！");
		}
		
		// 清空选中的权限
		selectedPermissions = null;
		// 刷新全部权限
		allPermissions = null;
	}
}
