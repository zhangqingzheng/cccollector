/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.app.model.Edition;
import com.cccollector.app.model.Release;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 发行列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ReleaseListBean extends BaseListBean<Release> implements Serializable {

	private static final long serialVersionUID = 716207303877310844L;

	public ReleaseListBean() {
		modelDisplayName = "发行";
		modelAttributeName = "release";
		modelIdAttributeName = "releaseId";
		submodelAttributeName = "resource";
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
	 * 所属版本
	 */
	public Edition getEdition() {
		return (Edition) relatedModel(1);
	}
	
	/**
	 * 管理二进制文件
	 */
	public void manageBinariesAction(Release release) {
		handleNavigation("binaryList.xhtml?" + modelIdAttributeName + "=" + release.getReleaseId());
	}
	
	/**
	 * 删除发行
	 */
	public void deleteReleaseAction(Release release) {
		// 发行如果否包含子对象，则不能被删除
		if (releaseService.hasChildren(release)) {
			addErrorMessage("要删除的发行中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 删除发行
		releaseService.delete(release);
		addInfoMessage("删除发行成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除发行
	 */
	public void deleteReleasesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个发行进行删除！");
			return;
		}
		
		// 发行如果否包含子对象，则不能被删除
		for (Release release : getSelectedModels()) {
			if (releaseService.hasChildren(release)) {
				addErrorMessage("要删除的发行中包含子对象，请清空子对象再进行删除！");
				return;
			}
		}
		
		// 删除发行
		releaseService.deleteAll(getSelectedModels());				
		addInfoMessage("删除发行成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
