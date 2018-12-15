/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.MediaDao;
import com.cccollector.news.model.Media;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 多媒体DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("mediaDao")
public class MediaDaoImpl extends GenericDaoHibernateImpl<Integer, Media> implements MediaDao {
	
	public MediaDaoImpl() {
		super(Media.class);
	}
}
