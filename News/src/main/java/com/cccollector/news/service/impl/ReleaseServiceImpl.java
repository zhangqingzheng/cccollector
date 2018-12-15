/**
 * 
 */
package com.cccollector.news.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.ReleaseDao;
import com.cccollector.news.model.Release;
import com.cccollector.news.service.ReleaseService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 发行服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("releaseService")
public class ReleaseServiceImpl extends GenericServiceHibernateImpl<Integer, Release> implements ReleaseService {
	
	@Autowired
	@SuppressWarnings("unused")
	private ReleaseDao releaseDao;
}
