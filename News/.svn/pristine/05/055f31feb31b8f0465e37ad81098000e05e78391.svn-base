/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;
import com.cccollector.news.model.App;
import com.cccollector.news.model.Edition;

/**
 * 版本列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class EditionListBean extends BaseListBean<Edition> implements Serializable {

	private static final long serialVersionUID = -8602709064488585674L;

	public EditionListBean() {
		modelDisplayName = "版本";
		modelAttributeName = "edition";
		modelIdAttributeName = "editionId";
		submodelAttributeName = "release";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Edition>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return appService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Edition> loadAllModelList() {
				if (getApp() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("app", new QueryField("appId", getApp().getAppId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("osType", true));
				return editionService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}
		};		
	}

	/**
	 * 所属应用
	 */
	public App getApp() {
		return (App) relatedModel(1);
	}
}
