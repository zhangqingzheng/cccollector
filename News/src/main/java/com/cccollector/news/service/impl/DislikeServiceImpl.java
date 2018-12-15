/**
 * 
 */
package com.cccollector.news.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.DislikeDao;
import com.cccollector.news.model.Dislike;
import com.cccollector.news.service.DislikeService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 不喜欢服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("dislikeService")
public class DislikeServiceImpl extends GenericServiceHibernateImpl<Integer, Dislike> implements DislikeService {
	
	@SuppressWarnings("unused")
	@Autowired
	private DislikeDao dislikeDao;
}
