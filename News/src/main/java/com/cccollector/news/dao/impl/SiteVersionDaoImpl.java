/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.SiteVersionDao;
import com.cccollector.news.model.SiteVersion;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 站点版本DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("siteVersionDao")
public class SiteVersionDaoImpl extends GenericDaoHibernateImpl<Integer, SiteVersion> implements SiteVersionDao {
	
	public SiteVersionDaoImpl() {
		super(SiteVersion.class);
	}
}
