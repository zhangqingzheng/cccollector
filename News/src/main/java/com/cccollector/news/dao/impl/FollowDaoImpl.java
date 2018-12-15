/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.FollowDao;
import com.cccollector.news.model.Follow;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 关注DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("followDao")
public class FollowDaoImpl extends GenericDaoHibernateImpl<Integer, Follow> implements FollowDao {

	public FollowDaoImpl() {
		super(Follow.class);
	}
}
