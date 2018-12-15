/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.ReleaseDao;
import com.cccollector.news.model.Release;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 发行DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("releaseDao")
public class ReleaseDaoImpl extends GenericDaoHibernateImpl<Integer, Release> implements ReleaseDao {
	
	public ReleaseDaoImpl() {
		super(Release.class);
	}
}
