/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.ReorderEvent;

import com.cccollector.news.model.App;
import com.cccollector.news.model.ColumnSubstitute;
import com.cccollector.universal.dao.QueryField;

/**
 * 栏目替身列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ColumnSubstituteListBean extends BaseListBean<ColumnSubstitute> implements Serializable {
	
	private static final long serialVersionUID = 5007497571164364460L;

	public ColumnSubstituteListBean () {
		modelDisplayName = "栏目替身";
		modelAttributeName = "columnSubstitute";
		modelIdAttributeName = "columnSubstituteId";
		genericListBeanHandler = new GenericListBeanHandlerRelatedAll<ColumnSubstitute>() {

			@Override
			public List<ColumnSubstitute> loadAllModelList() {
				if (getApp() == null) {
					return null;
				}
				// 查询条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("app", new QueryField("appId", getApp().getAppId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("position", true));
				return columnSubstituteService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return appService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 所属应用
	 */
	public App getApp() {
		return (App) relatedModel(1);
	}

	/**
	 * 批量修改栏目替身是否可用
	 */
	public void modifyColumnSubstitutesEnabledAction(boolean enabled) {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个栏目替身进行修改！");
			return;
		}

		// 修改栏目替身是否可用
		for (ColumnSubstitute columnSubstitute : getSelectedModels()) {
			columnSubstitute.setEnabled(enabled);
			columnSubstituteService.update(columnSubstitute, "enabled");
		}
		addInfoMessage("修改栏目替身是否可用成功！");

		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 移动栏目替身
	 */
	public void moveColumnSubstituteAction(ReorderEvent event) {
		ColumnSubstitute columnSubstituteTo = allModels.get(event.getToIndex());
		// 如果所选栏目替身不可用，则无法移动
		if (!columnSubstituteTo.getEnabled()) {
			addErrorMessage("请选择可用的栏目替身进行移动！");
			return;
		}

		// 移动栏目替身
		columnSubstituteService.moveColumnSubstitutes(allModels);
		addInfoMessage("移动栏目替身成功！");

		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 删除栏目替身
	 */
	public void deleteColumnSubstituteAction(ColumnSubstitute columnSubstitute) {
		// 删除栏目替身
		columnSubstituteService.delete(columnSubstitute);
		addInfoMessage("删除栏目替身成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 批量删除栏目替身
	 */
	public void deleteColumnSubstitutesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个栏目替身进行删除！");
			return;
		}
		
		// 删除栏目替身
		columnSubstituteService.deleteAll(getSelectedModels());
		addInfoMessage("删除栏目替身成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
