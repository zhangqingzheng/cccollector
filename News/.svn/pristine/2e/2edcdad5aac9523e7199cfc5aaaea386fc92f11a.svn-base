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

import com.cccollector.news.model.Comment;
import com.cccollector.news.model.Reply;
import com.cccollector.news.model.UserApp;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 回复列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ReplyListBean extends BaseListBean<Reply> implements Serializable {

	private static final long serialVersionUID = 4792277881670897624L;

	public ReplyListBean () {
		modelDisplayName = "回复";
		modelAttributeName = "reply";
		modelIdAttributeName = "replyId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedPage<Reply>() {
			
			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return commentService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Reply> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
				// 查询条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("comment", new QueryField("commentId", getComment().getCommentId())));
				
				// 过滤条件
				for (String key : filters.keySet()) {
					Object value = filters.get(key);
					switch (key) {
					case "content":
						predicateQueryFieldList.add(new QueryField("content", value, PredicateEnum.LIKE));
						break;
					case "status":
						predicateQueryFieldList.add(new QueryField("status", value));
						break;
					case "userApp.name":
						predicateQueryFieldList.add(new QueryField("userApp", new QueryField("user", new QueryField("nickName", value, PredicateEnum.LIKE))));
						break;
					}
				}
				
				predicateQueryFieldList = predicateQueryFieldList.size() == 0 ? null : predicateQueryFieldList;

				// 排序
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("createTime", false));
				
				// 总行数
				totalRowCount.append(replyService.count(predicateQueryFieldList).toString());

				List<Reply> replies = replyService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
				// 设置回复用户应用
				for (Reply reply : replies) {
					UserApp replyUserApp = userAppService.loadById(reply.getReplyUserAppId());
					reply.setReplyUserApp(replyUserApp);
				}
				
				return replies;
			}
		};
	}

	/**
	 * 所属评论
	 */
	public Comment getComment() {
		return (Comment) relatedModel(1);
	}

	/**
	 * 评论路径
	 */
	public String getCommentPath() {
		String commentPath = "commentList.xhtml";
		if (getModelId2() != null && !getModelId2().isEmpty()) {
			commentPath += "?userAppId=" + getModelId2();
		} else if (getModelId3() != null && !getModelId3().isEmpty() && getModelId4() != null && !getModelId4().isEmpty()) {
			commentPath += "?contentType=" + getModelId3() + "&contentId=" + getModelId4();
			if (getModelId3().equals("0") && getModelId5() != null && !getModelId5().isEmpty()) {
				commentPath += "&newsSourceId=" + getModelId5();
			}
		}
		return commentPath;
	}

	/**
	 * 状态枚举数组
	 */
	public Reply.StatusEnum[] getStatusEnums() {
		return Reply.StatusEnum.values();
	}
}
