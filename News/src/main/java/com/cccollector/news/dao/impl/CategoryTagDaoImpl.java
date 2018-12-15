/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.CategoryTagDao;
import com.cccollector.news.model.CategoryTag;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 分类标签DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("categoryTagDao")
public class CategoryTagDaoImpl extends GenericDaoHibernateImpl<Integer, CategoryTag> implements CategoryTagDao {

	public CategoryTagDaoImpl() {
		super(CategoryTag.class);
	}
}
