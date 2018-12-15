/**
 * 
 */
package com.cccollector.news.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DualListModel;

import com.cccollector.news.model.CategoryTag;
import com.cccollector.news.model.Column;
import com.cccollector.news.model.NewsSource;
import com.cccollector.universal.dao.QueryField;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 栏目Bean类
 * 
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ColumnBean extends BaseEditBean<Column> implements Serializable {

	private static final long serialVersionUID = 4937992908761245740L;


	public ColumnBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Column>() {

			@Override
			public Column loadModel(Integer modelId) {
				Column column = null;
				if (modelId == null) {
					column = new Column();
					column.setIconRatio(Column.IconRatioEnum.方图1_1.ordinal());
					column.setTemplateType(Column.TemplateTypeEnum.顶部栏目选择_文章列表.ordinal());
					column.setThumbnailRatio(Column.ThumbnailRatioEnum.方图1_1.ordinal());
					column.setBusinessType(Column.BusinessTypeEnum.无.ordinal());
					column.setEnabled(false);
					column.setParentColumn(getParentColumn());
					column.setNewsSource(getNewsSource());
					magazineIdentifier = "";
					storeIdentifier = "";
				} else {
					column = columnService.loadById(modelId, "categoryTags");

					// 处理图标
					loadPictureFile(1, columnService.getDataPath() + column.iconFilePath());
					
					// 处理业务标识符
					if (column.getBusinessTypeEnum() == Column.BusinessTypeEnum.杂志) {
						try {
							ObjectMapper objectMapper = new ObjectMapper();
							HashMap<String, String> businessIdentifierMap = objectMapper.readValue(column.getBusinessIdentifiers(), HashMap.class);
							magazineIdentifier = businessIdentifierMap.get("magazineIdentifier");
							storeIdentifier = businessIdentifierMap.get("storeIdentifier") != null ? businessIdentifierMap.get("storeIdentifier") : "";
						} catch (IOException e) {}
					}
				}
				
				// 处理分类标签
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("code", true));
				List<CategoryTag> allCategoryTags = categoryTagService.loadAll(predicateQueryFieldList, orderQueryFieldList);

				// 将分类标签放入分类标签双列表模型
				Map<Integer, CategoryTag> selectedCategoryTagMap = new HashMap<Integer, CategoryTag>();
				for (CategoryTag categoryTag : column.getCategoryTags()) {
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
				return column;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return newsSourceService.loadById(relatedModelId);
				} else if (index == 2) {
					if (relatedModelId > 0) {
						return columnService.loadById(relatedModelId);						
					}
				}
				return null;
			}
		};
	}

	/**
	 * 栏目
	 */
	public Column getColumn() {
		return getModel();
	}

	/**
	 * 所属新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(1);
	}

	/**
	 * 父栏目
	 */
	public Column getParentColumn() {
		return (Column) relatedModel(2);
	}

	/**
	 * 杂志标识符
	 */
	private String magazineIdentifier;

	public String getMagazineIdentifier() {
		return magazineIdentifier;
	}

	public void setMagazineIdentifier(String _magazineIdentifier) {
		magazineIdentifier = _magazineIdentifier;
	}

	/**
	 * 商店标识符
	 */
	private String storeIdentifier;
	
	public String getStoreIdentifier() {
		return storeIdentifier;
	}

	public void setStoreIdentifier(String _storeIdentifier) {
		storeIdentifier = _storeIdentifier;
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
		Column column = getColumn();
		// 置空
		if (column.getIntro().equals("")) {
			column.setIntro(null);
		}
		
		// 设置业务标识符
		if (column.getBusinessType() == Column.BusinessTypeEnum.杂志.ordinal()) {
			try {
				Map<String, String> businessIdentifierMap = new HashMap<String, String>();
				businessIdentifierMap.put("magazineIdentifier", magazineIdentifier);
				if (!storeIdentifier.isEmpty()) {
					businessIdentifierMap.put("storeIdentifier", storeIdentifier);
				}
				ObjectMapper objectMapper = new ObjectMapper();
				String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(businessIdentifierMap);
				column.setBusinessIdentifiers(json);
			} catch (JsonProcessingException e1) {}			
		} else if (column.getBusinessType() == Column.BusinessTypeEnum.书城.ordinal()) {
			column.setBusinessIdentifiers(null);
		} else {
			column.setBusinessIdentifiers(null);
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
		column.setCategoryTags(selectedCategoryTags);
		
		if (column.getColumnId() == null) {
			// 添加栏目
			columnService.addColumn(column, getNewsSource(), pictureFilePath(1));
			addInfoMessageToFlash("添加栏目成功！");
		} else {
			// 编辑栏目
			columnService.updateColumn(column, pictureFilePath(1));
			addInfoMessageToFlash("编辑栏目成功！");
		}

		handleNavigation("columnList.xhtml?multiSelect=true");
	}
}
