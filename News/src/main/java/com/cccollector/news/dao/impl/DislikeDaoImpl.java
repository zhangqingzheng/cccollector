/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.DislikeDao;
import com.cccollector.news.model.Dislike;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 不喜欢DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("dislikeDao")
public class DislikeDaoImpl extends GenericDaoHibernateImpl<Integer, Dislike> implements DislikeDao {

	public DislikeDaoImpl() {
		super(Dislike.class);
	}
}
