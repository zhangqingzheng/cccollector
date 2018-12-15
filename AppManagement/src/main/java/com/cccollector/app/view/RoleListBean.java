/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.ReorderEvent;

import com.cccollector.app.model.Role;
import com.cccollector.app.model.Platform;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 角色列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class RoleListBean extends BaseListBean<Role> implements Serializable {
	
	private static final long serialVersionUID = 5232902037387051106L;

	public RoleListBean() {
		modelDisplayName = "角色";
		modelAttributeName = "role";
		modelIdAttributeName = "roleId";
		submodelAttributeName = "permission";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Role>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return platformService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Role> loadAllModelList() {
				if (getPlatform() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("platform", new QueryField("platformId", getPlatform().getPlatformId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("position", true));
				return roleService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}
		};
	}
	
	/**
	 * 所属平台
	 */
	public Platform getPlatform() {
		return (Platform) relatedModel(1);
	}
	
	/**
	 * 批量修改角色是否可用
	 */
	public void modifyRolesEnabledAction(boolean enabled) {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个角色进行修改！");
			return;
		}
		
		// 修改角色是否可用
		for (Role role : getSelectedModels()) {
			role.setEnabled(enabled);
			roleService.update(role, "enabled");
		}
		addInfoMessage("修改角色是否可用成功！");
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 移动角色
	 */
	public void moveRoleAction(ReorderEvent event) {
		Role roleTo = allModels.get(event.getToIndex());
		// 如果所选角色不可用，则无法移动
		if (!roleTo.getEnabled()) {
			addErrorMessage("请选择可用的角色进行移动！");
			return;
		}
		
		// 移动角色
		roleService.moveRoles(allModels);
		addInfoMessage("移动角色成功！");
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 删除角色
	 */
	public void deleteRoleAction(Role role) {
		// 角色如果否包含子对象，则不能被删除
		if (roleService.hasChildren(role)) {
			addErrorMessage("要删除的角色中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 删除角色
		roleService.delete(role);
		addInfoMessage("删除角色成功！");
		
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
		
		// 角色如果否包含子对象，则不能被删除
		for (Role role : getSelectedModels()) {
			if (roleService.hasChildren(role)) {
				addErrorMessage("要删除的角色中包含子对象，请清空子对象再进行删除！");
				return;
			}
		}
		
		// 删除角色
		roleService.deleteAll(getSelectedModels());				
		addInfoMessage("删除角色成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
