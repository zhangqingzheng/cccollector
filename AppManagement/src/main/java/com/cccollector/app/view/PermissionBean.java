/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.app.model.Module;
import com.cccollector.app.model.Permission;
import com.cccollector.universal.dao.QueryField;

/**
 * 权限Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class PermissionBean extends BaseEditBean<Permission> implements Serializable {
	
	private static final long serialVersionUID = -5111574123072762758L;

	public PermissionBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Permission>() {
			
			@Override
			public Permission loadModel(Integer modelId) {
				Permission permission = null;
				if (modelId == null) {
					permission = new Permission();
					permission.setEnabled(true);
					permission.setModule(getModule());
				} else {
					permission = permissionService.loadById(modelId);
				}
				return permission;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return moduleService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}
	
	/**
	 * 权限
	 */
	public Permission getPermission() {
		return getModel();
	}

	/**
	 * 所属模块
	 */
	public Module getModule() {
		return (Module) relatedModel(1);
	}

	/**
	 * 改变类别
	 */
	public void changeTypeAction() {
		Permission permission = getPermission();
		if (permission.getType() != null) {
			permission.setName(permission.getTypeEnum().name() + permission.getModule().getName());
		}
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		Permission permission = getPermission();
		// 置空
		if (permission.getSpecificIds().equals("")) {
			permission.setSpecificIds(null);
		}
		
		if (permission.getPermissionId() == null) {
			// 处理类别重复
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("module", new QueryField("moduleId", getModule().getModuleId())));
			predicateQueryFieldList.add(new QueryField("type", permission.getType()));
			if (permission.getSpecificIds() != null) {
				predicateQueryFieldList.add(new QueryField("specificIds", permission.getSpecificIds()));
			} else {
				predicateQueryFieldList.add(new QueryField("specificIds", null, QueryField.PredicateEnum.IS_NULL));
			}
			long count = permissionService.count(predicateQueryFieldList);
			if (count > 0) {
				addValidatingMessage("权限类别已存在，请勿重复添加");
				return;
			}

			// 添加权限
			permissionService.save(permission);
			addInfoMessage("添加权限成功！");
		} else {
			// 编辑权限
			permissionService.update(permission);
			addInfoMessage("编辑权限成功！");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("permissionList.xhtml?multiSelect=true");
		}
	}
}
