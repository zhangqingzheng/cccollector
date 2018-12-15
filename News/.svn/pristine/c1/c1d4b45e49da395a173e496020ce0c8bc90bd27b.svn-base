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
import com.cccollector.news.model.Like;
import com.cccollector.news.model.UserApp;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 喜欢列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class LikeListBean extends BaseListBean<Like> implements Serializable {

	private static final long serialVersionUID = 5297561164749468350L;

	public LikeListBean () {
		modelDisplayName = "喜欢";
		modelAttributeName = "like";
		modelIdAttributeName = "likeId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedPage<Like>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return userAppService.loadById(relatedModelId);
				} else if (index == 3) {
					if (Integer.valueOf(getModelId2()) == Like.ContentTypeEnum.文章.ordinal()) {
						return articleService.loadById(relatedModelId);
					} else if (Integer.valueOf(getModelId2()) == Like.ContentTypeEnum.评论.ordinal()) {
						return commentService.loadById(relatedModelId);
					}
				}
				return null;
			}
			
			@Override		
			public List<Like> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
				// 查询条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				if (getUserApp() != null) {
					predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", getUserApp().getUserAppId())));
				}
				if (getModelId2() != null && !getModelId2().isEmpty() && getModelId3() != null && !getModelId3().isEmpty()) {
					predicateQueryFieldList.add(new QueryField("contentType", Integer.valueOf(getModelId2())));
					predicateQueryFieldList.add(new QueryField("contentId", Integer.valueOf(getModelId3())));
				}

				// 过滤条件
				for (String key : filters.keySet()) {
					Object value = filters.get(key);
					switch (key) {
					case "modelId2":
						predicateQueryFieldList.add(new QueryField("contentType", value));
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
				totalRowCount.append(likeService.count(predicateQueryFieldList).toString());

				List<Like> likes = likeService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
				// 设置关联内容
				for (Like like : likes) {
					switch (like.getContentTypeEnum()) {
					case 文章:
						Article article = articleService.loadById(like.getContentId());
						like.setRelativeContent(article);
						break;
					case 评论:
						Comment comment = commentService.loadById(like.getContentId());
						like.setRelativeContent(comment);
						break;
					}
				}
				return likes;
			}
		};
	}

	/**
	 * 用户应用
	 */
	public UserApp getUserApp() {
		return (UserApp) relatedModel(1);
	}

	/**
	 * 关联文章
	 */
	public Article getArticle() {
		if (getModelId2() != null && !getModelId2().isEmpty() && getModelId3() != null && !getModelId3().isEmpty()) {
			if (Integer.valueOf(getModelId2()) == Like.ContentTypeEnum.文章.ordinal()) {
				return (Article) relatedModel(3);
			}			
		}
		return null;
	}

	/**
	 * 关联评论
	 */
	public Comment getComment() {
		if (getModelId2() != null && !getModelId2().isEmpty() && getModelId3() != null && !getModelId3().isEmpty()) {
			if (Integer.valueOf(getModelId2()) == Like.ContentTypeEnum.评论.ordinal()) {
				return (Comment) relatedModel(3);
			}			
		}
	 	return null;
	}

	/**
	 * 关联内容类别枚举数组
	 */
	public Like.ContentTypeEnum[] getContentTypeEnums() {
		return Like.ContentTypeEnum.values();
	}
}
