/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.TemplateDao;
import com.cccollector.news.model.Template;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 模板DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("templateDao")
public class TemplateDaoImpl extends GenericDaoHibernateImpl<Integer, Template> implements TemplateDao {
	
	public TemplateDaoImpl() {
		super(Template.class);
	}
}
