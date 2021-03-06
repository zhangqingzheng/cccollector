/**
 * 
 */
package com.cccollector.app.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.app.dao.PlatformDao;
import com.cccollector.app.model.Platform;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 平台DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("platformDao")
public class PlatformDaoImpl extends GenericDaoHibernateImpl<Integer, Platform> implements PlatformDao {
	
	public PlatformDaoImpl() {
		super(Platform.class);
	}
}
