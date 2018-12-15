/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.ReportDao;
import com.cccollector.news.model.Report;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 举报DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("reportDao")
public class ReportDaoImpl extends GenericDaoHibernateImpl<Integer, Report> implements ReportDao {

	public ReportDaoImpl() {
		super(Report.class);
	}
}
