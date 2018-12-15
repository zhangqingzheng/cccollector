/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.DistributionDao;
import com.cccollector.news.model.Distribution;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 分发DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("distributionDao")
public class DistributionDaoImpl extends GenericDaoHibernateImpl<Integer, Distribution> implements DistributionDao {

	public DistributionDaoImpl() {
		super(Distribution.class);
	}
}
