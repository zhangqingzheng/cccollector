/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.AppDao;
import com.cccollector.news.model.App;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 应用DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("appDao")
public class AppDaoImpl extends GenericDaoHibernateImpl<Integer, App> implements AppDao {
	
	public AppDaoImpl() {
		super(App.class);
	}
}
