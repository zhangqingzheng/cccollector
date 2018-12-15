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
import com.cccollector.news.model.Favorite;
import com.cccollector.news.model.UserApp;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 收藏列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class FavoriteListBean extends BaseListBean<Favorite> implements Serializable {

	private static final long serialVersionUID = -723237987665075319L;

	public FavoriteListBean () {
		modelDisplayName = "收藏";
		modelAttributeName = "favorite";
		modelIdAttributeName = "favoriteId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedPage<Favorite>() {
			
			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return userAppService.loadById(relatedModelId);
				} else if (index == 3) {
					if (Integer.valueOf(getModelId2()) == Favorite.ContentTypeEnum.文章.ordinal()) {
						return articleService.loadById(relatedModelId);
					}
				}
				return null;
			}

			@Override
			public List<Favorite> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
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
				orderQueryFieldList.add(new QueryField("createTime", Boolean.FALSE));
				
				// 总行数
				totalRowCount.append(favoriteService.count(predicateQueryFieldList).toString());

				List<Favorite> favorites = favoriteService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
				// 设置关联内容
				for (Favorite favorite : favorites) {
					switch (favorite.getContentTypeEnum()) {
					case 文章:
						Article article = articleService.loadById(favorite.getContentId());
						favorite.setRelativeContent(article);
						break;
					}
				}
				return favorites;
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
		return (Article) relatedModel(3);
	}

	/**
	 * 关联内容类别枚举数组
	 */
	public Favorite.ContentTypeEnum[] getContentTypeEnums() {
		return Favorite.ContentTypeEnum.values();
	}
}
