/**
 * 
 */
package com.cccollector.news.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.FavoriteDao;
import com.cccollector.news.model.Favorite;
import com.cccollector.news.service.FavoriteService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 收藏服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("favoriteService")
public class FavoriteServiceImpl extends GenericServiceHibernateImpl<Integer, Favorite> implements FavoriteService {
	
	@SuppressWarnings("unused")
	@Autowired
	private FavoriteDao favoriteDao;
}
