/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.UserDao;
import com.cccollector.news.model.User;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 用户DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("userDao")
public class UserDaoImpl extends GenericDaoHibernateImpl<Integer, User> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}
}
