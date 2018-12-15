/**
 * 
 */
package com.cccollector.universal.user.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.universal.dao.GenericDaoHibernateImpl;
import com.cccollector.universal.user.dao.UserDao;
import com.cccollector.universal.user.model.User;

/**
 * 用户DAO实现类
 *
 * @author 杨彪
 * Copyright © 2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("user_userDao")
public class UserDaoImpl extends GenericDaoHibernateImpl<Integer, User> implements UserDao {
	
	public UserDaoImpl() {
		super(User.class);
	}
}
