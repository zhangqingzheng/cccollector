/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.ColumnDao;
import com.cccollector.news.model.Column;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 栏目DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("columnDao")
public class ColumnDaoImpl extends GenericDaoHibernateImpl<Integer, Column> implements ColumnDao {

	public ColumnDaoImpl() {
		super(Column.class);
	}
}
