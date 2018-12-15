/**
 * 
 */
package com.cccollector.app.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.app.dao.ModuleDao;
import com.cccollector.app.model.Module;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 模块DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("moduleDao")
public class ModuleDaoImpl extends GenericDaoHibernateImpl<Integer, Module> implements ModuleDao {
	
	public ModuleDaoImpl() {
		super(Module.class);
	}
}
