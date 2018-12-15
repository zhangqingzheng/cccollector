/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.RecommendDao;
import com.cccollector.news.model.Recommend;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 推荐DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("recommendDao")
public class RecommendDaoImpl extends GenericDaoHibernateImpl<Integer, Recommend> implements RecommendDao {
	
	public RecommendDaoImpl() {
		super(Recommend.class);
	}
}
