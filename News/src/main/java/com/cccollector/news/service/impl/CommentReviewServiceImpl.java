/**
 * 
 */
package com.cccollector.news.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.ArticleDao;
import com.cccollector.news.dao.CommentDao;
import com.cccollector.news.dao.CommentReviewDao;
import com.cccollector.news.dao.LogDao;
import com.cccollector.news.dao.ReplyDao;
import com.cccollector.news.model.Article;
import com.cccollector.news.model.Comment;
import com.cccollector.news.model.CommentReview;
import com.cccollector.news.model.CommentReviewId;
import com.cccollector.news.model.Log;
import com.cccollector.news.model.Reply;
import com.cccollector.news.service.CommentReviewService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 评论审核服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("commentReviewService")
public class CommentReviewServiceImpl extends GenericServiceHibernateImpl<CommentReviewId, CommentReview> implements CommentReviewService {
	
	@SuppressWarnings("unused")
	@Autowired
	private CommentReviewDao commentReviewDao;
	
	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private CommentDao commentDao;

	@Autowired
	private ReplyDao replyDao;

	@Autowired
	private LogDao logDao;

	@Override
	public boolean passCommentReview(CommentReview commentReview) {
		if (commentReview.getTypeEnum() == CommentReview.TypeEnum.评论) {
			// 根据评论ID获取评论
			Comment comment = commentDao.loadById(commentReview.getCommentReviewId().getCommentId());
			// 如果不存在
			if (comment == null) {
				return false;
			}

			// 设置评论状态为已发布
			comment.setStatus(Comment.StatusEnum.已发布.ordinal());
			commentReview.setStatus(CommentReview.StatusEnum.已发布.ordinal());
			commentReview.getComment().setStatus(Comment.StatusEnum.已发布.ordinal());
		} else if (commentReview.getTypeEnum() == CommentReview.TypeEnum.回复) {
			// 根据回复ID获取回复
			Reply reply = replyDao.loadById(commentReview.getCommentReviewId().getReplyId());
			// 如果不存在
			if (reply == null) {
				return false;
			}

			// 设置回复状态为已发布
			reply.setStatus(Reply.StatusEnum.已发布.ordinal());
			commentReview.setStatus(CommentReview.StatusEnum.已发布.ordinal());
			commentReview.getReply().setStatus(Reply.StatusEnum.已发布.ordinal());
		}
		return true;
	}

	@Override
	public boolean notPassCommentReview(CommentReview commentReview) {
		// 根据评论ID获取评论
		Comment comment = commentDao.loadById(commentReview.getCommentReviewId().getCommentId());
		// 如果不存在
		if (comment == null) {
			return false;
		}
		
		// 根据关联内容ID获取关联内容对象
		Object relativeContent = null;
		// 文章
		if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
			relativeContent = articleDao.loadById(comment.getContentId());
		}
		// 如果不存在
		if (relativeContent == null) {
			return false;
		}

		if (commentReview.getTypeEnum() == CommentReview.TypeEnum.评论) {
			// 设置评论状态为审核未通过
			comment.setStatus(Comment.StatusEnum.审核未通过.ordinal());
			commentReview.setStatus(CommentReview.StatusEnum.审核未通过.ordinal());
			commentReview.getComment().setStatus(Comment.StatusEnum.审核未通过.ordinal());

			// 文章
			if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
				Article article = (Article) relativeContent;
				// 文章获得评论数 - 1
				article.setCommentCount(Math.max(article.getCommentCount() - 1, 0));
				// 文章获得回复数 - 评论的回复数
				article.setReplyCount(Math.max(article.getReplyCount() - comment.getReplyCount(), 0));
			}
			
			// 创建日志
			Log log = new Log();
			log.setTitle("你的评论 " + comment.getName() + " 审核未通过");
			log.setContentType(Log.ContentTypeEnum.系统.ordinal());
			log.setContentId(0);
			log.setCreateTimeDate(new Date());
			log.setUserApp(comment.getUserApp());
			logDao.save(log);
		} else if (commentReview.getTypeEnum() == CommentReview.TypeEnum.回复) {
			// 根据回复ID获取回复
			Reply reply = replyDao.loadById(commentReview.getCommentReviewId().getReplyId());
			// 如果不存在
			if (reply == null || (reply.getComment().getStatusEnum() != Comment.StatusEnum.待审核 && reply.getComment().getStatusEnum() != Comment.StatusEnum.已发布)) {
				return false;
			}

			// 设置回复状态为审核未通过
			reply.setStatus(Reply.StatusEnum.审核未通过.ordinal());
			commentReview.setStatus(CommentReview.StatusEnum.审核未通过.ordinal());
			commentReview.getReply().setStatus(Reply.StatusEnum.审核未通过.ordinal());

			// 评论获得回复数 - 1
			reply.getComment().setReplyCount(Math.max(reply.getComment().getReplyCount() - 1, 0));
			
			// 文章
			if (reply.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
				// 文章获得回复数 - 1
				Article article = (Article) relativeContent;
				article.setReplyCount(Math.max(article.getReplyCount() - 1, 0));
			}
			
			// 创建日志
			Log log = new Log();
			log.setTitle("你的回复 " + reply.getName() + " 审核未通过");
			log.setContentType(Log.ContentTypeEnum.系统.ordinal());
			log.setContentId(0);
			log.setCreateTimeDate(new Date());
			log.setUserApp(reply.getUserApp());
			logDao.save(log);
		}
		return true;
	}

	@Override
	public boolean deleteCommentReview(CommentReview commentReview) {
		// 根据评论ID获取评论
		Comment comment = commentDao.loadById(commentReview.getCommentReviewId().getCommentId());
		// 如果不存在
		if (comment == null) {
			return false;
		}
		
		// 根据关联内容ID获取关联内容对象
		Object relativeContent = null;
		// 文章
		if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
			relativeContent = articleDao.loadById(comment.getContentId());
		}
		// 如果不存在
		if (relativeContent == null) {
			return false;
		}

		if (commentReview.getTypeEnum() == CommentReview.TypeEnum.评论) {
			// 设置评论状态为系统已删除
			comment.setStatus(Comment.StatusEnum.系统已删除.ordinal());
			commentReview.setStatus(CommentReview.StatusEnum.系统已删除.ordinal());
			commentReview.getComment().setStatus(Comment.StatusEnum.系统已删除.ordinal());

			// 文章
			if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
				Article article = (Article) relativeContent;
				// 文章获得评论数 - 1
				article.setCommentCount(Math.max(article.getCommentCount() - 1, 0));
				// 文章获得回复数 - 评论的回复数
				article.setReplyCount(Math.max(article.getReplyCount() - comment.getReplyCount(), 0));
			}
			
			// 创建日志
			Log log = new Log();
			log.setTitle("你的评论 " + comment.getName() + " 已被系统删除");
			log.setContentType(Log.ContentTypeEnum.系统.ordinal());
			log.setContentId(0);
			log.setCreateTimeDate(new Date());
			log.setUserApp(comment.getUserApp());
			logDao.save(log);
		} else if (commentReview.getTypeEnum() == CommentReview.TypeEnum.回复) {
			// 根据回复ID获取回复
			Reply reply = replyDao.loadById(commentReview.getCommentReviewId().getReplyId());
			// 如果不存在
			if (reply == null || (reply.getComment().getStatusEnum() != Comment.StatusEnum.待审核 && reply.getComment().getStatusEnum() != Comment.StatusEnum.已发布)) {
				return false;
			}

			// 设置回复状态为系统已删除
			reply.setStatus(Reply.StatusEnum.系统已删除.ordinal());
			commentReview.setStatus(CommentReview.StatusEnum.系统已删除.ordinal());
			commentReview.getReply().setStatus(Reply.StatusEnum.系统已删除.ordinal());

			// 评论获得回复数 - 1
			reply.getComment().setReplyCount(Math.max(reply.getComment().getReplyCount() - 1, 0));
			
			// 文章
			if (reply.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
				// 文章获得回复数 - 1
				Article article = (Article) relativeContent;
				article.setReplyCount(Math.max(article.getReplyCount() - 1, 0));
			}
			
			// 创建日志
			Log log = new Log();
			log.setTitle("你的回复 " + reply.getName() + " 已被系统删除");
			log.setContentType(Log.ContentTypeEnum.系统.ordinal());
			log.setContentId(0);
			log.setCreateTimeDate(new Date());
			log.setUserApp(reply.getUserApp());
			logDao.save(log);
		}
		return true;
	}

	@Override
	public boolean recoverCommentReview(CommentReview commentReview) {
		// 根据评论ID获取评论
		Comment comment = commentDao.loadById(commentReview.getCommentReviewId().getCommentId());
		// 如果不存在
		if (comment == null) {
			return false;
		}
		
		// 根据关联内容ID获取关联内容对象
		Object relativeContent = null;
		// 文章
		if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
			relativeContent = articleDao.loadById(comment.getContentId());
		}
		// 如果不存在
		if (relativeContent == null) {
			return false;
		}

		if (commentReview.getTypeEnum() == CommentReview.TypeEnum.评论) {
			// 设置评论状态为待审核
			comment.setStatus(Comment.StatusEnum.待审核.ordinal());
			commentReview.setStatus(CommentReview.StatusEnum.待审核.ordinal());
			commentReview.getComment().setStatus(Comment.StatusEnum.待审核.ordinal());

			// 文章
			if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
				Article article = (Article) relativeContent;
				// 文章获得评论数 + 1
				article.setCommentCount(article.getCommentCount() + 1);
				// 文章获得回复数 + 评论的回复数
				article.setReplyCount(article.getReplyCount() + comment.getReplyCount());
			}
		} else if (commentReview.getTypeEnum() == CommentReview.TypeEnum.回复) {
			// 根据回复ID获取回复
			Reply reply = replyDao.loadById(commentReview.getCommentReviewId().getReplyId());
			// 如果不存在
			if (reply == null || (reply.getComment().getStatusEnum() != Comment.StatusEnum.待审核 && reply.getComment().getStatusEnum() != Comment.StatusEnum.已发布)) {
				return false;
			}

			// 设置回复状态为待审核
			reply.setStatus(Reply.StatusEnum.待审核.ordinal());
			commentReview.setStatus(CommentReview.StatusEnum.待审核.ordinal());
			commentReview.getReply().setStatus(Reply.StatusEnum.待审核.ordinal());

			// 评论获得回复数 + 1
			reply.getComment().setReplyCount(reply.getComment().getReplyCount() + 1);
			
			// 文章
			if (reply.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
				// 文章获得回复数 + 1
				Article article = (Article) relativeContent;
				article.setReplyCount(article.getReplyCount() + 1);
			}
		}
		return true;
	}
}
