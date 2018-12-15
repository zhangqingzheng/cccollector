/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.SortOrder;

import com.cccollector.news.model.Article;
import com.cccollector.news.model.Comment;
import com.cccollector.news.model.CommentReview;
import com.cccollector.news.model.Reply;
import com.cccollector.news.model.UserApp;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 评论审核列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class CommentReviewListBean extends BaseListBean<CommentReview> implements Serializable {

	private static final long serialVersionUID = -7152350526618110258L;

	public CommentReviewListBean () {
		modelDisplayName = "评论	审核";
		modelAttributeName = "commentReview";
		modelIdAttributeName = "commentReviewId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerPage<CommentReview>() {

			@Override
			public List<CommentReview> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
				// 查询条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				if (showNotProcessed) {
					predicateQueryFieldList.add(new QueryField("status", CommentReview.StatusEnum.已发布.ordinal(), PredicateEnum.LESS_THAN_OR_EQUAL));
				}

				// 过滤条件
				if (filters != null) {
					for (String key : filters.keySet()) {
						Object value = filters.get(key);
						switch (key) {
						case "type":
							predicateQueryFieldList.add(new QueryField("commentReviewId", new QueryField("replyId", 0, Integer.parseInt((String) value) == 0 ? QueryField.PredicateEnum.EQUAL : QueryField.PredicateEnum.GREATER_THAN)));
							break;
						case "content":
							predicateQueryFieldList.add(new QueryField("content", value, PredicateEnum.LIKE));
							break;
						case "status":
							predicateQueryFieldList.add(new QueryField("status", value));
							break;
						}
					}
				}
				
				predicateQueryFieldList = predicateQueryFieldList.size() == 0 ? null : predicateQueryFieldList;

				// 排序
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("createTime", Boolean.FALSE));
				
				// 总行数
				totalRowCount.append(commentReviewService.count(predicateQueryFieldList).toString());

				List<CommentReview> commentReviews = commentReviewService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
				for (CommentReview commentReview : commentReviews) {
					// 评论
					Comment comment = commentService.loadById(commentReview.getCommentReviewId().getCommentId());
					commentReview.setComment(comment);
					// 评论关联内容
					switch (comment.getContentTypeEnum()) {
					case 文章:
						Article article = articleService.loadById(comment.getContentId());
						comment.setRelativeContent(article);
						break;
					}
					// 评论
					if (commentReview.getCommentReviewId().getReplyId() > 0) {
						Reply reply = replyService.loadById(commentReview.getCommentReviewId().getReplyId());
						commentReview.setReply(reply);
					}
					// 用户应用
					UserApp userApp = userAppService.loadById(commentReview.getUserAppId());
					commentReview.setUserApp(userApp);
				}

				return commentReviews;
			}
		};
	}

	/**
	 * 是否显示未处理
	 */
	private boolean showNotProcessed;

	public boolean getShowNotProcessed() {
		return showNotProcessed;
	}

	public void setShowNotProcessed(boolean _showNotProcessed) {
		showNotProcessed = _showNotProcessed;
	}

	/**
	 * 类别枚举数组
	 */
	public CommentReview.TypeEnum[] getTypeEnums() {
		return CommentReview.TypeEnum.values();
	}

	/**
	 * 状态枚举数组
	 */
	public CommentReview.StatusEnum[] getStatusEnums() {
		return CommentReview.StatusEnum.values();
	}
	
	/**
	 * 审核通过评论审核
	 */
	public void passCommentReviewAction(CommentReview commentReview) {
		// 审核通过评论审核
		boolean success = commentReviewService.passCommentReview(commentReview);
		if (success) {
			addInfoMessage("通过评论审核成功！");
		} else {
			addErrorMessage("通过评论审核失败！");
		}
	}
	
	/**
	 * 审核未通过评论审核
	 */
	public void notPassCommentReviewAction(CommentReview commentReview) {
		// 审核未通过评论审核
		boolean success = commentReviewService.notPassCommentReview(commentReview);
		if (success) {
			addInfoMessage("未通过评论审核成功！");
		} else {
			addErrorMessage("未通过评论审核失败！");
		}
	}
	
	/**
	 * 删除评论审核
	 */
	public void deleteCommentReviewAction(CommentReview commentReview) {
		// 删除评论审核
		boolean success = commentReviewService.deleteCommentReview(commentReview);
		if (success) {
			addInfoMessage("删除评论审核成功！");
		} else {
			addErrorMessage("删除评论审核失败！");
		}
	}
	
	/**
	 * 恢复评论审核
	 */
	public void recoverCommentReviewAction(CommentReview commentReview) {
		// 恢复评论审核
		boolean success = commentReviewService.recoverCommentReview(commentReview);
		if (success) {
			addInfoMessage("恢复评论审核成功！");
		} else {
			addErrorMessage("恢复评论审核失败！");
		}
	}
}
