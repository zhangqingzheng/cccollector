/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.CommentReviewDao;
import com.cccollector.news.model.CommentReview;
import com.cccollector.news.model.CommentReviewId;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 评论审核DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("commentReviewDao")
public class CommentReviewDaoImpl extends GenericDaoHibernateImpl<CommentReviewId, CommentReview> implements CommentReviewDao {

	public CommentReviewDaoImpl() {
		super(CommentReview.class);
	}
}
