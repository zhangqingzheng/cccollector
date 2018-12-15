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

import com.cccollector.news.model.Like;
import com.cccollector.news.model.Reply;
import com.cccollector.news.model.Log;
import com.cccollector.news.model.UserApp;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 日志列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class LogListBean extends BaseListBean<Log> implements Serializable {

	private static final long serialVersionUID = 5193503726506064840L;

	public LogListBean () {
		modelDisplayName = "日志";
		modelAttributeName = "log";
		modelIdAttributeName = "logId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedPage<Log>() {
			
			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return userAppService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Log> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
				// 查询条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				if (getUserApp() != null) {
					predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", getUserApp().getUserAppId())));
				}

				// 过滤条件
				for (String key : filters.keySet()) {
					Object value = filters.get(key);
					switch (key) {
					case "title":
						predicateQueryFieldList.add(new QueryField("title", value, PredicateEnum.LIKE));
						break;
					case "modelId2":
						predicateQueryFieldList.add(new QueryField("contentType", value));
						break;
					case "activeUserApp.name":
						predicateQueryFieldList.add(new QueryField("activeUserApp", new QueryField("user", new QueryField("nickName", value, PredicateEnum.LIKE))));
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
				totalRowCount.append(logService.count(predicateQueryFieldList).toString());

				List<Log> logs = logService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
				// 设置关联内容
				for (Log log : logs) {
					switch (log.getContentTypeEnum()) {
					case 回复:
						Reply reply = replyService.loadById(log.getContentId());
						log.setRelativeContent(reply);
						break;
					case 喜欢:
						Like like = likeService.loadById(log.getContentId());
						log.setRelativeContent(like);
						break;
					default:
						break;
					}
				}
				return logs;
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
	 * 关联内容类别枚举数组
	 */
	public Log.ContentTypeEnum[] getContentTypeEnums() {
		return Log.ContentTypeEnum.values();
	}
}
