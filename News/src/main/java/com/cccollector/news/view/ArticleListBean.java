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
import com.cccollector.news.model.NewsSource;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 文章列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ArticleListBean extends BaseListBean<Article> implements Serializable {

	private static final long serialVersionUID = -119079606360863423L;


	public ArticleListBean() {
		modelDisplayName = "文章";
		modelAttributeName = "article";
		modelIdAttributeName = "articleId";
		submodelAttributeName = "media";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedPage<Article>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return newsSourceService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Article> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
				if (getNewsSource() == null) {
					return null;
				}
				// 查询条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
				
				// 过滤条件
				for (String key : filters.keySet()) {
					Object value = filters.get(key);
					switch (key) {
					case "title":
						predicateQueryFieldList.add(new QueryField("title", value, PredicateEnum.LIKE));
						break;
					case "author":
						predicateQueryFieldList.add(new QueryField("author", value, PredicateEnum.LIKE));
						break;
					case "source":
						predicateQueryFieldList.add(new QueryField("source", value, PredicateEnum.LIKE));
						break;
					case "keywords":
						predicateQueryFieldList.add(new QueryField("keywords", value, PredicateEnum.LIKE));
						break;
					case "editor":
						predicateQueryFieldList.add(new QueryField("editor", value, PredicateEnum.LIKE));
						break;
					case "reviewer":
						predicateQueryFieldList.add(new QueryField("reviewer", value, PredicateEnum.LIKE));
						break;
					}
				}

				// 排序
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("articleId", false));
				
				// 总行数
				totalRowCount.append(articleService.count(predicateQueryFieldList).toString());
				
				return articleService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
			}
		};
	}

	/**
	 * 所属新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(1);
	}
	
	/**
	 * 选择文章收藏
	 */
	public void selectArticleFavoriteAction(Article article) {
		handleNavigation("favoriteList.xhtml?contentType=0&contentId=" + article.getArticleId() + "&newsSourceId=" + getModelId1());
	}
	
	/**
	 * 选择文章喜欢
	 */
	public void selectArticleLikeAction(Article article) {
		handleNavigation("likeList.xhtml?contentType=0&contentId=" + article.getArticleId() + "&newsSourceId=" + getModelId1());
	}
	
	/**
	 * 选择文章不喜欢
	 */
	public void selectArticleDislikeAction(Article article) {
		handleNavigation("dislikeList.xhtml?contentType=0&contentId=" + article.getArticleId() + "&newsSourceId=" + getModelId1());
	}
	
	/**
	 * 选择文章评论
	 */
	public void selectArticleCommentAction(Article article) {
		handleNavigation("commentList.xhtml?contentType=0&contentId=" + article.getArticleId() + "&newsSourceId=" + getModelId1());
	}

	/**
	 * 管理分发
	 */
	public void manageDistributionsAction(Article article) {
		// 新闻源未开启审稿，或者新闻源开启审稿并且文章已通过审稿，则进入包含的分发列表
		if (getNewsSource().getReviewEnabled() && article.getReviewTime() == null) {
			addErrorMessage("请先审稿，审稿通过后才可管理分发！");
		} else {
			handleNavigation("distributionList.xhtml?articleId=" + article.getArticleId() + "&newsSourceId=" + getModelId1());
		}
	}
	
	/**
	 * 删除文章
	 */
	public void deleteArticleAction(Article article) {
		// 文章如果否包含子对象，则不能被删除
		if (articleService.hasChildren(article)) {
			addErrorMessage("要删除的文章中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 删除文章
		articleService.delete(article);
		addInfoMessage("删除文章成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新分页模型
		pageModel = null;
	}
	
	/**
	 * 批量删除文章
	 */
	public void deleteArticlesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个文章进行删除！");
			return;
		}

		// 文章如果否包含子对象，则不能被删除
		for (Article article : getSelectedModels()) {
			if (articleService.hasChildren(article)) {
				addErrorMessage("要删除的文章中包含子对象，请清空子对象再进行删除！");
				return;
			}
		}
		// 删除文章
		articleService.deleteAll(getSelectedModels());
		addInfoMessage("删除文章成功！");

		// 清空选中的模型
		setSelectedModels(null);		
		// 刷新分页模型
		pageModel = null;
	}
}
