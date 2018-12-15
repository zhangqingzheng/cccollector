/**
 * 
 */
package com.cccollector.news.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.LikeDao;
import com.cccollector.news.model.Like;
import com.cccollector.news.service.LikeService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 喜欢服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("likeService")
public class LikeServiceImpl extends GenericServiceHibernateImpl<Integer, Like> implements LikeService {
	
	@SuppressWarnings("unused")
	@Autowired
	private LikeDao likeDao;
}
