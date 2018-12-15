/**
 * 
 */
package com.cccollector.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.app.dao.EditionDao;
import com.cccollector.app.model.Edition;
import com.cccollector.app.service.EditionService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 版本服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("editionService")
public class EditionServiceImpl extends GenericServiceHibernateImpl<Integer, Edition> implements EditionService {
	
	@SuppressWarnings("unused")
	@Autowired
	private EditionDao editionDao;
}
