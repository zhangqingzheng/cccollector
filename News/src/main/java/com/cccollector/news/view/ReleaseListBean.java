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
import com.cccollector.news.model.Edition;
import com.cccollector.news.model.Release;

/**
 * 发行列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ReleaseListBean extends BaseListBean<Release> implements Serializable {

	private static final long serialVersionUID = 5601410568885546354L;

	public ReleaseListBean() {
		modelDisplayName = "发行";
		modelAttributeName = "release";
		modelIdAttributeName = "releaseId";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Release>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return editionService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Release> loadAllModelList() {
				if (getEdition() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("edition", new QueryField("editionId", getEdition().getEditionId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("releaseDate", false));
				return releaseService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}
		};
	}

	/**
	 * 关联版本
	 */
	public Edition getEdition() {
		return (Edition) relatedModel(1);
	}
}
