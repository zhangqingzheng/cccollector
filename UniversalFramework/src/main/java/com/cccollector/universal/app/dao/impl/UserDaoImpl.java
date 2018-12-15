/**
 * 
 */
package com.cccollector.universal.app.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.universal.app.dao.UserDao;
import com.cccollector.universal.app.model.User;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 用户DAO实现类
 *
 * @author 谢朋
 * Copyright © 2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("app_userDao")
public class UserDaoImpl extends GenericDaoHibernateImpl<Integer, User> implements UserDao {
	
	public UserDaoImpl() {
		super(User.class);
	}
}