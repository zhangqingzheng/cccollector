/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.CommentDao;
import com.cccollector.news.model.Comment;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 评论DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("commentDao")
public class CommentDaoImpl extends GenericDaoHibernateImpl<Integer, Comment> implements CommentDao {

	public CommentDaoImpl() {
		super(Comment.class);
	}
}
