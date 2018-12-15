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

import com.cccollector.app.model.Module;
import com.cccollector.app.model.Platform;
import com.cccollector.universal.dao.QueryField;

/**
 * 模块列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ModuleListBean extends BaseListBean<Module> implements Serializable {
	
	private static final long serialVersionUID = -4685815945906543533L;

	public ModuleListBean () {
		modelDisplayName = "模块";
		modelAttributeName = "module";
		modelIdAttributeName = "moduleId";
		submodelAttributeName = "permission";
		genericListBeanHandler = new GenericListBeanHandlerRelatedAll<Module>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return platformService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Module> loadAllModelList() {
				if (getPlatform() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("platform", new QueryField("platformId", getPlatform().getPlatformId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("position", true));
				return moduleService.loadAll(predicateQueryFieldList, orderQueryFieldList);
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
	 * 批量修改模块是否可用
	 */
	public void modifyModulesEnabledAction(boolean enabled) {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个模块进行修改！");
			return;
		}
		
		// 修改模块是否可用
		for (Module module : getSelectedModels()) {
			module.setEnabled(enabled);
			moduleService.update(module, "enabled");
		}
		addInfoMessage("修改模块是否可用成功！");
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 移动模块
	 */
	public void moveModuleAction(ReorderEvent event) {
		Module moduleTo = allModels.get(event.getToIndex());
		// 如果所选模块不可用，则无法移动
		if (!moduleTo.getEnabled()) {
			addErrorMessage("请选择可用的模块进行移动！");
			return;
		}
		
		// 移动模块
		moduleService.moveModules(allModels);
		addInfoMessage("移动模块成功！");
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 删除模块
	 */
	public void deleteModuleAction(Module module) {
		// 模块如果否包含子对象，则不能被删除
		if (moduleService.hasChildren(module)) {
			addErrorMessage("要删除的模块中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 删除模块
		moduleService.delete(module);
		addInfoMessage("删除模块成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除模块
	 */
	public void deleteModulesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个模块进行删除！");
			return;
		}
		
		// 模块如果否包含子对象，则不能被删除
		for (Module module : getSelectedModels()) {
			if (moduleService.hasChildren(module)) {
				addErrorMessage("要删除的模块中包含子对象，请清空子对象再进行删除！");
				return;
			}
		}
		
		// 删除模块
		moduleService.deleteAll(getSelectedModels());				
		addInfoMessage("删除模块成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
