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
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 部门列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class DepartmentListBean extends BaseListBean<Department> implements Serializable {
	
	private static final long serialVersionUID = 6012584890306603688L;

	public DepartmentListBean() {
		modelDisplayName = "部门";
		modelAttributeName = "department";
		modelClazz = Department.class;
		modelIdAttributeName = "departmentId";
		submodelAttributeName = "job";
		parentModelAttributeName = "parentDepartment";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerAll<Department>() {

			@Override
			public List<Department> loadAllModelList() {
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("code", true));
				return departmentService.loadAll(orderQueryFieldList);
			}
		};
	}
	
	/**
	 * 加载部门徽章缩略图
	 */
	public String loadBadgeThumbnail(Department department) {
		return departmentService.getDepartmentBadgeThumbnailUrl(department);
	}

	/**
	 * 管理岗位
	 */
	public void manageJobsAction(Department department) {
		handleNavigation(submodelAttributeName + "List.xhtml?" + modelIdAttributeName + "=" + department.getDepartmentId());
	}
	
	/**
	 * 管理角色和权限
	 */
	public void manageRolesAndPermissionsAction(Department department) {
		handleNavigation("roleAndPermissionList.xhtml?" + modelIdAttributeName + "=" + department.getDepartmentId());
	}
	
	/**
	 * 删除部门
	 */
	public void deleteDepartmentAction(Department department) {
		// 部门如果否包含子对象，则不能被删除
		if (departmentService.hasChildrenNotIn(department, "roles", "permissions")) {
			addErrorMessage("要删除的部门中包含子对象，请清空子对象再进行删除！");
			return;
		}

		// 删除部门
		departmentService.deleteDepartment(department);
		addInfoMessage("删除部门成功！");
		
		// 清空选中的模型TreeNode
		setSelectedModelTreeNode(null);
		// 刷新全部模型
		allModels = null;
		// 刷新根模型TreeNode
		rootModelTreeNode = null;
	}

	/**
	 * 删除部门
	 */
	public void deleteDepartmentAction() {
		if (getSelectedModelTreeNode() == null) {
			addErrorMessage("请选择一个部门进行删除！");
			return;
		}
		
		// 部门如果否包含子对象，则不能被删除
		Department department = (Department) getSelectedModelTreeNode().getData();
		if (departmentService.hasChildrenNotIn(department, "roles", "permissions")) {
			addErrorMessage("要删除的部门中包含子对象，请清空子对象再进行删除！");
			return;
		}

		// 删除部门
		departmentService.deleteDepartment(department);
		// 将选中的TreeNode从父TreeNode中删除
		getSelectedModelTreeNode().getParent().getChildren().remove(getSelectedModelTreeNode());
		addInfoMessage("删除部门成功！");
		
		// 清空选中的模型TreeNode
		setSelectedModelTreeNode(null);
	}
}
