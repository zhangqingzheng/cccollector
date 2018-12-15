/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.UserAppDao;
import com.cccollector.news.model.UserApp;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 用户应用DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("userAppDao")
public class UserAppDaoImpl extends GenericDaoHibernateImpl<Integer, UserApp> implements UserAppDao {

	public UserAppDaoImpl() {
		super(UserApp.class);
	}
}
