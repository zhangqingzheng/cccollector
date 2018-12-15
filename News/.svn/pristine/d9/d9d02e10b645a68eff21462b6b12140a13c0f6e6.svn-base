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

import com.cccollector.news.model.App;
import com.cccollector.news.model.UserApp;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;

/**
 * 用户应用列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class UserAppListBean extends BaseListBean<UserApp> implements Serializable {

	private static final long serialVersionUID = -3654993983046812051L;

	public UserAppListBean () {
		modelDisplayName = "用户应用";
		modelAttributeName = "userApp";
		modelIdAttributeName = "userAppId";
		useDialog = false;
		genericListBeanHandler = new GenericListBeanHandlerPage<UserApp>() {

			@Override
			public List<UserApp> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
				// 过滤条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				for (String key : filters.keySet()) {
					Object value = filters.get(key);
					switch (key) {
					case "user.userId":
						int userId = 0;
						try {
							userId = Integer.valueOf(value.toString());
						} catch (Exception e) {}
						predicateQueryFieldList.add(new QueryField("user", new QueryField("userId", userId)));
						break;
					case "user.nickName":
						predicateQueryFieldList.add(new QueryField("user", new QueryField("nickName", value, PredicateEnum.LIKE)));
						break;
					case "user.email":
						predicateQueryFieldList.add(new QueryField("user", new QueryField("email", value, PredicateEnum.LIKE)));
						break;
					case "user.cellphone":
						predicateQueryFieldList.add(new QueryField("user", new QueryField("cellphone", value, PredicateEnum.LIKE)));
						break;
					case "userAppId":
						int userAppId = 0;
						try {
							userAppId = Integer.valueOf(value.toString());
						} catch (Exception e) {}
						predicateQueryFieldList.add(new QueryField("userAppId", userAppId));
						break;
					case "app.appId":
						predicateQueryFieldList.add(new QueryField("app", new QueryField("appId", value)));
						break;
					}
				}
				
				predicateQueryFieldList = predicateQueryFieldList.size() == 0 ? null : predicateQueryFieldList;

				// 排序
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("syncTime", false));
				orderQueryFieldList.add(new QueryField("userAppId", false));
				
				// 总行数
				totalRowCount.append(userAppService.count(predicateQueryFieldList).toString());
				
				return userAppService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
			}
		};
	}

	/**
	 * 全部应用
	 */
	private List<App> allApps;

	public List<App> getAllApps() {
		if (allApps == null) {
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("enabled", false));
			orderQueryFieldList.add(new QueryField("name", true));
			allApps = appService.loadAll(orderQueryFieldList);
		}
		return allApps;
	}
	
	/**
	 * 选择用户应用关注
	 */
	public void selectUserAppFollowAction(UserApp userApp) {
		handleNavigation("followList.xhtml?userAppId=" + userApp.getUserAppId());
	}
	
	/**
	 * 选择用户应用粉丝
	 */
	public void selectUserAppFollowerAction(UserApp userApp) {
		handleNavigation("followList.xhtml?followedUserAppId=" + userApp.getUserAppId());
	}
	
	/**
	 * 选择用户应用收藏
	 */
	public void selectUserAppFavoriteAction(UserApp userApp) {
		handleNavigation("favoriteList.xhtml?userAppId=" + userApp.getUserAppId());
	}
	
	/**
	 * 选择用户应用喜欢
	 */
	public void selectUserAppLikeAction(UserApp userApp) {
		handleNavigation("likeList.xhtml?userAppId=" + userApp.getUserAppId());
	}
	
	/**
	 * 选择用户应用不喜欢
	 */
	public void selectUserAppDislikeAction(UserApp userApp) {
		handleNavigation("dislikeList.xhtml?userAppId=" + userApp.getUserAppId());
	}
	
	/**
	 * 选择用户应用评论
	 */
	public void selectUserAppCommentAction(UserApp userApp) {
		handleNavigation("commentList.xhtml?userAppId=" + userApp.getUserAppId());
	}
	
	/**
	 * 选择用户应用日志
	 */
	public void selectUserAppLogAction(UserApp userApp) {
		handleNavigation("logList.xhtml?userAppId=" + userApp.getUserAppId());
	}
	
	/**
	 * 同步用户应用
	 */
	public void syncUserAppAction(UserApp userApp) {
		// 同步用户应用
		boolean success = userAppService.syncUserApp(userApp);
		if (success) {
			addInfoMessage("同步用户应用成功！");
		} else {
			addErrorMessage("同步用户应用失败，请重新尝试！");
		}
		
		// 刷新分页模型
		pageModel = null;
	}
	
	/**
	 * 批量同步用户应用
	 */
	public void syncUserAppsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个用户应用进行同步！");
			return;
		}

		boolean allSuccess = true;
		for (UserApp userApp : getSelectedModels()) {
			// 同步用户应用
			boolean success = userAppService.syncUserApp(userApp);
			if (!success) {
				allSuccess = false;
			}
		}

		if (allSuccess) {
			addInfoMessage("同步用户应用成功！");
		} else {
			addErrorMessage("部分用户应用同步失败，请重试！");
		}
		
		// 刷新分页模型
		pageModel = null;
	}
}
