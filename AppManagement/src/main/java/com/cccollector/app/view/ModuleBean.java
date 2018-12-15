/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.app.model.Module;
import com.cccollector.app.model.Platform;

/**
 * 模块Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ModuleBean extends BaseEditBean<Module> implements Serializable {

	private static final long serialVersionUID = 3547712716235306388L;

	public ModuleBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Module>() {

			@Override
			public Module loadModel(Integer modelId) {
				Module module = null;
				if (modelId == null) {
					module = new Module();
					module.setEnabled(true);
					module.setPlatform(getPlatform());
				} else {
					module = moduleService.loadById(modelId);
				}
				return module;
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
	 * 模块
	 */
	public Module getModule() {
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
		Module module = getModule();
		// 置空
		if (module.getRemarks().equals("")) {
			module.setRemarks(null);
		}
		
		if (module.getModuleId() == null) {
			// 添加模块
			moduleService.addModule(module);
			addInfoMessageToFlash("添加模块成功！");
		} else {
			// 编辑模块
			moduleService.update(module);
			addInfoMessageToFlash("编辑模块成功！");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("moduleList.xhtml?multiSelect=true");
		}
	}
}
