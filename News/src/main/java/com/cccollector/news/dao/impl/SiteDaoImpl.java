/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.SiteDao;
import com.cccollector.news.model.Site;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 站点DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("siteDao")
public class SiteDaoImpl extends GenericDaoHibernateImpl<Integer, Site> implements SiteDao {
	
	public SiteDaoImpl() {
		super(Site.class);
	}
}
