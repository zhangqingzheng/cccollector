/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.app.model.Department;

/**
 * 部门Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class DepartmentBean extends BaseEditBean<Department> implements Serializable {

	private static final long serialVersionUID = -7013714998413062316L;

	public DepartmentBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Department>() {
			
			@Override
			public Department loadModel(Integer modelId) {
				Department department = null;
				if (modelId == null) {
					department = new Department();
					department.setIsIndependent(false);
					department.setParentDepartment(getParentDepartment());
				} else {
					department = departmentService.loadById(modelId);
					
					// 处理徽章
					loadPictureFile(1, departmentService.getDataPath() + department.badgeFilePath());
				}
				return department;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return departmentService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}
	
	/**
	 * 部门
	 */
	public Department getDepartment() {
		return getModel();
	}
	
	/**
	 * 父部门
	 */
	public Department getParentDepartment() {
		return (Department) relatedModel(1);
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		Department department = getDepartment();
		// 置空
		if (department.getRemarks().equals("")) {
			department.setRemarks(null);
		}
		
		if (department.getDepartmentId() == null) {
			// 添加部门
			departmentService.addDepartment(getDepartment(), pictureFilePath(1));
			addInfoMessageToFlash("添加部门成功！");
		} else {
			// 编辑部门
			departmentService.updateDepartment(getDepartment(), pictureFilePath(1));
			addInfoMessageToFlash("编辑部门成功！");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("departmentList.xhtml?multiSelect=true");
		}
	}
}
