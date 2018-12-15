/**
 * 
 */
package com.cccollector.news.service;

import com.cccollector.news.model.CommentReview;
import com.cccollector.news.model.CommentReviewId;
import com.cccollector.universal.service.GenericService;

/**
 * 评论审核服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface CommentReviewService extends GenericService<CommentReviewId, CommentReview> {
	
	/**
	 * 审核通过评论审核
	 * 
	 * @param commentReview 待审核的评论审核
	 * @return 是否成功
	 */
	public boolean passCommentReview(CommentReview commentReview);
	
	/**
	 * 审核未通过评论审核
	 * 
	 * @param commentReview 待审核的评论审核
	 * @return 是否成功
	 */
	public boolean notPassCommentReview(CommentReview commentReview);
	
	/**
	 * 删除评论审核
	 * 
	 * @param commentReview 待删除的评论审核
	 * @return 是否成功
	 */
	public boolean deleteCommentReview(CommentReview commentReview);
	
	/**
	 * 恢复评论审核
	 * 
	 * @param commentReview 待恢复的评论审核
	 * @return 是否成功
	 */
	public boolean recoverCommentReview(CommentReview commentReview);
}
