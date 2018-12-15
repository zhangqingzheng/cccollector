/**
 * 
 */
package com.cccollector.app.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.app.dao.UserDetailDao;
import com.cccollector.app.model.UserDetail;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 用户详情DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("userDetailDao")
public class UserDetailDaoImpl extends GenericDaoHibernateImpl<Integer, UserDetail> implements UserDetailDao {
	
	public UserDetailDaoImpl() {
		super(UserDetail.class);
	}
}
