/**
 * 
 */
package com.cccollector.news.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.CommentDao;
import com.cccollector.news.model.Comment;
import com.cccollector.news.service.CommentService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 评论服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("commentService")
public class CommentServiceImpl extends GenericServiceHibernateImpl<Integer, Comment> implements CommentService {
	
	@SuppressWarnings("unused")
	@Autowired
	private CommentDao commentDao;
}
