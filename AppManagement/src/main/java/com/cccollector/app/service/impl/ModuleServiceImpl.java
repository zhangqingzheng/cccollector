/**
 * 
 */
package com.cccollector.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.app.dao.ModuleDao;
import com.cccollector.app.model.Module;
import com.cccollector.app.service.ModuleService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 模块服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("moduleService")
public class ModuleServiceImpl extends GenericServiceHibernateImpl<Integer, Module> implements ModuleService {
	
	@Autowired
	private ModuleDao moduleDao;

	@Override
	public void addModule(Module module) {
		// 设置排序位置
		ArrayList<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("platform", new QueryField("platformId", module.getPlatform().getPlatformId())));
		Integer position = moduleDao.max("position", predicateQueryFieldList);
		module.setPosition(position == null ? 0 : position.intValue() + 1);
		
		// 保存模板		
		moduleDao.save(module);
	}

	@Override
	public void moveModules(List<Module> modules) {
		int position = 0;
		for (Module module : modules) {
			module.setPosition(position);
			moduleDao.update(module, "position");
			position++;
		}
	}
}
