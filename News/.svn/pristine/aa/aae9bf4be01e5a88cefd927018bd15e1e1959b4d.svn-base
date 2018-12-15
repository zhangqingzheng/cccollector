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

import com.cccollector.news.model.Glossary;
import com.cccollector.news.model.NewsSource;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 术语列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class GlossaryListBean extends BaseListBean<Glossary> implements Serializable {

	private static final long serialVersionUID = 414875149910903292L;

	public GlossaryListBean() {
		modelDisplayName = "术语";
		modelAttributeName = "glossary";
		modelIdAttributeName = "glossaryId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedPage<Glossary>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return newsSourceService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Glossary> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
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
					case "displayPriority":
						predicateQueryFieldList.add(new QueryField("displayPriority", value));
						break;
					}
				}

				// 排序
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("glossaryId", false));
				
				// 总行数
				totalRowCount.append(glossaryService.count(predicateQueryFieldList).toString());
				
				return glossaryService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
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
	 * 加载术语图片缩略图
	 */
	public String loadPictureThumbnail(Glossary glossary) {
		if (glossary.getPictureUpdateTime() != null) {
			return glossaryService.getPictureThumbnailGlossariesUrl(glossary);
		}
		return null;
	}

	/**
	 * 术语显示优先级枚举数组
	 */
	public Glossary.DisplayPriorityEnum[] getDisplayPriorityEnums() {
		return Glossary.DisplayPriorityEnum.values();
	}
	
	/**
	 * 修改术语是否可用
	 */
	public void modifyGlossaryEnabledAction(boolean enabled) {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个术语进行修改！");
			return;
		}

		for (Glossary glossary : getSelectedModels()) {
			glossary.setEnabled(enabled);
			glossaryService.update(glossary, "enabled");
		}
		addInfoMessage("修改术语是否可用成功！");
		
		// 刷新分页模型
		pageModel = null;
	}
	
	/**
	 * 删除
	 */
	public void deleteGlossaryAction(Glossary glossary) {
		// 删除术语
		glossaryService.deleteGlossary(glossary);
		addInfoMessage("删除术语成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		pageModel = null;
	}

	/**
	 * 批量删除术语
	 */
	public void deleteGlossariesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个术语进行删除！");
			return;
		}

		for (Glossary glossary : getSelectedModels()) {
			glossaryService.deleteGlossary(glossary);
		}
		addInfoMessage("删除术语成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		pageModel = null;
	}
}
