/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.ColumnSubstituteDao;
import com.cccollector.news.model.ColumnSubstitute;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 栏目替身DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("columnSubstituteDao")
public class ColumnSubstituteDaoImpl extends GenericDaoHibernateImpl<Integer, ColumnSubstitute> implements ColumnSubstituteDao {

	public ColumnSubstituteDaoImpl() {
		super(ColumnSubstitute.class);
	}
}
