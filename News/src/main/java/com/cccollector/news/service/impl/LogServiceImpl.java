/**
 * 
 */
package com.cccollector.news.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.LogDao;
import com.cccollector.news.model.Log;
import com.cccollector.news.service.LogService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 日志服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("logService")
public class LogServiceImpl extends GenericServiceHibernateImpl<Integer, Log> implements LogService {
	
	@SuppressWarnings("unused")
	@Autowired
	private LogDao logDao;
}
