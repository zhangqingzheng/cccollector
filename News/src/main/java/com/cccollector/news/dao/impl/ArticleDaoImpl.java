/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.ArticleDao;
import com.cccollector.news.model.Article;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 文章DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("articleDao")
public class ArticleDaoImpl extends GenericDaoHibernateImpl<Integer, Article> implements ArticleDao {

	public ArticleDaoImpl() {
		super(Article.class);
	}
}
