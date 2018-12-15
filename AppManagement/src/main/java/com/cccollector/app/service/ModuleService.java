/**
 * 
 */
package com.cccollector.app.service;

import java.util.List;

import com.cccollector.app.model.Module;
import com.cccollector.universal.service.GenericService;

/**
 * 模块服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface ModuleService extends GenericService<Integer, Module> {
	
	/**
	 * 添加模块
	 * 
	 * @param module 待添加的模块
	 */
	public void addModule(Module module);
	
	/**
	 * 移动模块
	 * 
	 * @param modules 移动后的模块
	 */
	public void moveModules(List<Module> modules);
}
