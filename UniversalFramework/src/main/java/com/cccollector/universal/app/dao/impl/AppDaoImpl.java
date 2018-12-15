/**
 * 
 */
package com.cccollector.universal.app.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.universal.app.dao.AppDao;
import com.cccollector.universal.app.model.App;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 应用DAO实现类
 *
 * @author 谢朋
 * Copyright © 2017年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("app_appDao")
public class AppDaoImpl extends GenericDaoHibernateImpl<Integer, App> implements AppDao {
	
	public AppDaoImpl() {
		super(App.class);
	}
}
