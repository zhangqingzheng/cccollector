/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.StaticPageDao;
import com.cccollector.news.model.StaticPage;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 静态化页面DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("staticPageDao")
public class StaticPageDaoImpl extends GenericDaoHibernateImpl<Integer, StaticPage> implements StaticPageDao {
	
	public StaticPageDaoImpl() {
		super(StaticPage.class);
	}
}
