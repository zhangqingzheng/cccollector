/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.GlossaryDao;
import com.cccollector.news.model.Glossary;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 术语DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("glossaryDao")
public class GlossaryDaoImpl extends GenericDaoHibernateImpl<Integer, Glossary> implements GlossaryDao {

	public GlossaryDaoImpl() {
		super(Glossary.class);
	}
}
