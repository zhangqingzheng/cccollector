/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.app.model.Platform;
import com.cccollector.app.model.Role;

/**
 * 角色Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class RoleBean extends BaseEditBean<Role> implements Serializable {

	private static final long serialVersionUID = -2090450974113521203L;

	public RoleBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Role>() {
			
			@Override
			public Role loadModel(Integer modelId) {
				Role role = null;
				if (modelId == null) {
					role = new Role();
					role.setEnabled(true);
					role.setPlatform(getPlatform());
				} else {
					role = roleService.loadById(modelId);
				}
				return role;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return platformService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 角色
	 */
	public Role getRole() {
		return getModel();
	}

	/**
	 * 所属平台
	 */
	public Platform getPlatform() {
		return (Platform) relatedModel(1);
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		Role role = getRole();
		// 置空
		if (role.getRemarks().equals("")) {
			role.setRemarks(null);
		}
		
		if (role.getRoleId() == null) {
			// 添加角色
			roleService.addRole(role);
			addInfoMessageToFlash("添加角色成功 ！ ");
		} else {
			// 编辑角色
			roleService.update(role);
			addInfoMessageToFlash("编辑角色成功 ！ ");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("roleList.xhtml?multiSelect=true");
		}
	}
}
