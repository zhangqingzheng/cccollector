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

import com.cccollector.news.model.Follow;
import com.cccollector.news.model.UserApp;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 关注列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class FollowListBean extends BaseListBean<Follow> implements Serializable {

	private static final long serialVersionUID = -528302873432438714L;

	public FollowListBean () {
		modelDisplayName = "关注";
		modelAttributeName = "follow";
		modelIdAttributeName = "followId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedPage<Follow>() {
			
			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return userAppService.loadById(relatedModelId);
				} else if (index == 2) {
					return userAppService.loadById(relatedModelId);					
				}
				return null;
			}

			@Override
			public List<Follow> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
				// 查询条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				if (getUserApp() != null) {
					predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", getUserApp().getUserAppId())));
				}
				if (getFollowedUserApp() != null) {
					predicateQueryFieldList.add(new QueryField("followedUserApp", new QueryField("userAppId", getFollowedUserApp().getUserAppId())));
				}

				// 过滤条件
				for (String key : filters.keySet()) {
					Object value = filters.get(key);
					switch (key) {
					case "followedUserApp.name":
						predicateQueryFieldList.add(new QueryField("followedUserApp", new QueryField("user", new QueryField("nickName", value, PredicateEnum.LIKE))));
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
				totalRowCount.append(followService.count(predicateQueryFieldList).toString());

				return followService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
			}
		};
	}

	/**
	 * 所属用户应用
	 */
	public UserApp getUserApp() {
		return (UserApp) relatedModel(1);
	}
	
	/**
	 * 被关注用户应用
	 */
	public UserApp getFollowedUserApp() {
		return (UserApp) relatedModel(2);
	}
}
