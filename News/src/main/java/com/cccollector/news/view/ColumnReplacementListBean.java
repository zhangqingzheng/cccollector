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

import com.cccollector.news.model.ColumnReplacement;
import com.cccollector.news.model.SiteVersion;
import com.cccollector.universal.dao.QueryField;

/**
 * 栏目替代列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ColumnReplacementListBean extends BaseListBean<ColumnReplacement> implements Serializable {
	
	private static final long serialVersionUID = 1057671471870516588L;

	public ColumnReplacementListBean () {
		modelDisplayName = "栏目替代";
		modelAttributeName = "columnReplacement";
		modelIdAttributeName = "columnReplacementId";
		genericListBeanHandler = new GenericListBeanHandlerRelatedAll<ColumnReplacement>() {

			@Override
			public List<ColumnReplacement> loadAllModelList() {
				if (getSiteVersion() == null) {
					return null;
				}
				// 查询条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("siteVersionId", getSiteVersion().getSiteVersionId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("position", true));
				return columnReplacementService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return siteVersionService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 所属站点版本
	 */
	public SiteVersion getSiteVersion() {
		return (SiteVersion) relatedModel(1);
	}

	/**
	 * 批量修改栏目替代是否可用
	 */
	public void modifyColumnReplacementsEnabledAction(boolean enabled) {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个栏目替代进行修改！");
			return;
		}

		// 修改栏目替代是否可用
		for (ColumnReplacement columnReplacement : getSelectedModels()) {
			columnReplacement.setEnabled(enabled);
			columnReplacementService.update(columnReplacement, "enabled");
		}
		addInfoMessage("修改栏目替代是否可用成功！");

		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 移动栏目替代
	 */
	public void moveColumnReplacementAction(ReorderEvent event) {
		ColumnReplacement columnReplacementTo = allModels.get(event.getToIndex());
		// 如果所选栏目替代不可用，则无法移动
		if (!columnReplacementTo.getEnabled()) {
			addErrorMessage("请选择可用的栏目替代进行移动！");
			return;
		}

		// 移动栏目替代
		columnReplacementService.moveColumnReplacements(allModels);
		addInfoMessage("移动栏目替代成功！");

		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 删除栏目替代
	 */
	public void deleteColumnReplacementAction(ColumnReplacement columnReplacement) {
		// 删除栏目替代
		columnReplacementService.delete(columnReplacement);
		addInfoMessage("删除栏目替代成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 批量删除栏目替代
	 */
	public void deleteColumnReplacementsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个栏目替代进行删除！");
			return;
		}
		
		// 删除栏目替代
		columnReplacementService.deleteAll(getSelectedModels());
		addInfoMessage("删除栏目替代成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
