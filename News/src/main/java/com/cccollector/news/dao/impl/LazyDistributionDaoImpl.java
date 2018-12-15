/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.LazyDistributionDao;
import com.cccollector.news.model.LazyDistribution;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 分发DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("lazyDistributionDao")
public class LazyDistributionDaoImpl extends GenericDaoHibernateImpl<Integer, LazyDistribution> implements LazyDistributionDao {

	public LazyDistributionDaoImpl() {
		super(LazyDistribution.class);
	}
}
