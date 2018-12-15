/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DualListModel;

import com.cccollector.news.model.CategoryTag;
import com.cccollector.news.model.Glossary;
import com.cccollector.news.model.NewsSource;
import com.cccollector.universal.dao.QueryField;

/**
 * 术语Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class GlossaryBean extends BaseEditBean<Glossary> implements Serializable {

	private static final long serialVersionUID = 5149715112028393748L;

	public GlossaryBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Glossary>() {

			@Override
			public Glossary loadModel(Integer modelId) {
				Glossary glossary = null;
				if (modelId == null) {
					glossary = new Glossary();
					glossary.setPictureRatio(Glossary.PictureRatioEnum.方图1_1.ordinal());
					glossary.setDisplayPriority(Glossary.DisplayPriorityEnum.中.ordinal());
					glossary.setEnabled(false);
					glossary.setNewsSource(getNewsSource());
				} else {
					glossary = glossaryService.loadById(modelId, "categoryTags");
					
					// 处理图片
					loadPictureFile(1, glossaryService.getDataPath() + glossary.pictureFilePath());
				}
				
				// 处理分类标签
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("code", true));
				List<CategoryTag> allCategoryTags = categoryTagService.loadAll(predicateQueryFieldList, orderQueryFieldList);

				// 将分类标签放入分类标签双列表模型
				Map<Integer, CategoryTag> selectedCategoryTagMap = new HashMap<Integer, CategoryTag>();
				for (CategoryTag categoryTag : glossary.getCategoryTags()) {
					selectedCategoryTagMap.put(categoryTag.getCategoryTagId(), categoryTag);
				}
				categoryTagDualListModel = new DualListModel<CategoryTag>(new ArrayList<CategoryTag>(), new ArrayList<CategoryTag>());
				for (CategoryTag categoryTag : allCategoryTags) {
					if (selectedCategoryTagMap.get(categoryTag.getCategoryTagId()) != null) {
						categoryTagDualListModel.getTarget().add(categoryTag);
					} else {
						categoryTagDualListModel.getSource().add(categoryTag);
					}
				}
				return glossary;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
			if (index == 1) {
				return newsSourceService.loadById(relatedModelId);
			}
				return null;
			}
		};
	}

	/**
	 * 术语
	 */
	public Glossary getGlossary() {
		return getModel();
	}

	/**
	 * 所属新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(1);
	}

	/**
	 * 分类标签双列表模型
	 */
	private DualListModel<CategoryTag> categoryTagDualListModel;

	public DualListModel<CategoryTag> getCategoryTagDualListModel() {
		return categoryTagDualListModel;
	}

	public void setCategoryTagDualListModel(DualListModel<CategoryTag> _categoryTagDualListModel) {
		categoryTagDualListModel = _categoryTagDualListModel;
	}

	/**
	 * 保存
	 */
	public void saveAction() {
		Glossary glossary = getGlossary();
		if (glossary.getGlossaryId() == null) {
			// 添加术语
			glossaryService.addGlossary(glossary, pictureFilePath(1));
			addInfoMessageToFlash("添加术语成功！");
		} else {
			// 编辑术语
			glossaryService.updateGlossary(glossary, pictureFilePath(1));
			addInfoMessageToFlash("编辑术语成功！");
		}
		
		// 处理分类标签
		List<CategoryTag> selectedCategoryTags = new ArrayList<CategoryTag>();
		for (Object selectedCategoryTag : categoryTagDualListModel.getTarget()) {
			if (selectedCategoryTag instanceof CategoryTag) {
				selectedCategoryTags.add((CategoryTag) selectedCategoryTag);
			} else if (selectedCategoryTag instanceof String) {
				CategoryTag categoryTag = new CategoryTag();
				categoryTag.setCategoryTagId(Integer.parseInt((String) selectedCategoryTag)); 
				selectedCategoryTags.add(categoryTag);
			}
		}
		glossary.setCategoryTags(selectedCategoryTags);
		glossaryService.update(glossary);

		handleNavigation("glossaryList.xhtml?multiSelect=true");
	}
}
