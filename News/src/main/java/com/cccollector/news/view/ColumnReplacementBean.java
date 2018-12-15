/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.news.model.SiteVersion;
import com.cccollector.news.model.Column;
import com.cccollector.news.model.ColumnReplacement;
import com.cccollector.news.model.NewsSource;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;

/**
 * 栏目替代Bean类
 *
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ColumnReplacementBean extends BaseEditBean<ColumnReplacement> implements Serializable {

	private static final long serialVersionUID = 2895241980586418655L;

	public ColumnReplacementBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<ColumnReplacement>() {

			@Override
			public ColumnReplacement loadModel(Integer modelId) {
				ColumnReplacement columnReplacement = null;
				if (modelId == null) {
					columnReplacement = new ColumnReplacement();
					columnReplacement.setMode(ColumnReplacement.ModeEnum.在之后添加.ordinal());
					columnReplacement.setEnabled(false);
					columnReplacement.setSourceColumn(new Column());
					columnReplacement.setTargetColumn(new Column());
					columnReplacement.setSiteVersion(getSiteVersion());
				} else {
					columnReplacement = columnReplacementService.loadById(modelId);
				}
				return columnReplacement;
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
	 * 栏目替代
	 */
	public ColumnReplacement getColumnReplacement() {
		return getModel();
	}

	/**
	 * 所属站点版本
	 */
	public SiteVersion getSiteVersion() {
		return (SiteVersion) relatedModel(1);
	}
	
	/**
	 * 选中的源新闻源ID
	 */
	private String selectedSourceNewsSourceId;
	
	public String getSelectedSourceNewsSourceId() {
		return selectedSourceNewsSourceId;
	}
	
	public void setSelectedSourceNewsSourceId(String _selectedSourceNewsSourceId) {
		selectedSourceNewsSourceId = _selectedSourceNewsSourceId;
	}
	
	/**
	 * 选中的目标新闻源ID
	 */
	private String selectedTargetNewsSourceId;
	
	public String getSelectedTargetNewsSourceId() {
		return selectedTargetNewsSourceId;
	}
	
	public void setSelectedTargetNewsSourceId(String _selectedTargetNewsSourceId) {
		selectedTargetNewsSourceId = _selectedTargetNewsSourceId;
	}

	/**
	 * 源新闻源
	 */
	private List<NewsSource> allSourceNewsSources;
	
	public List<NewsSource> getAllSourceNewsSources() {
		if (allSourceNewsSources == null) {
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("enabled", true));
			predicateQueryFieldList.add(new QueryField("sites", new QueryField("siteVersions", new QueryField("siteVersionId", getSiteVersion().getSiteVersionId(), PredicateEnum.IN))));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("position", true));
			allSourceNewsSources = newsSourceService.loadAll(predicateQueryFieldList, orderQueryFieldList);
		}
		return allSourceNewsSources;
	}
	
	/**
	 * 目标新闻源
	 */
	private List<NewsSource> allTargetNewsSources;
	
	public List<NewsSource> getAllTargetNewsSources() {
		if (allTargetNewsSources == null) {
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("enabled", true));
			predicateQueryFieldList.add(new QueryField("sites", new QueryField("siteVersions", new QueryField("siteVersionId", getSiteVersion().getSiteVersionId(), PredicateEnum.IN))));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("position", true));
			allTargetNewsSources = newsSourceService.loadAll(predicateQueryFieldList, orderQueryFieldList);
		}
		return allTargetNewsSources;
	}
	
	/**
	 * 全部源栏目
	 */
	private List<Column> allSourceColumns;

	public List<Column> getAllSourceColumns() {
		if (selectedSourceNewsSourceId != null ) {
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", Integer.parseInt(selectedSourceNewsSourceId))));
			predicateQueryFieldList.add(new QueryField("enabled", true));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("code", true));
			allSourceColumns = columnService.loadAll(predicateQueryFieldList, orderQueryFieldList);
		}
		return allSourceColumns;
	}
	
	/**
	 * 全部目标栏目
	 */
	private List<Column> allTargetColumns;
	
	public List<Column> getAllTargetColumns() {
		if (selectedTargetNewsSourceId != null ) {
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", Integer.parseInt(selectedTargetNewsSourceId))));
			predicateQueryFieldList.add(new QueryField("enabled", true));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("code", true));
			allTargetColumns = columnService.loadAll(predicateQueryFieldList, orderQueryFieldList);
		}
		return allTargetColumns;
	}

	/**
	 * 保存
	 */
	public void saveAction() {
		ColumnReplacement columnReplacement = getColumnReplacement();
		if (columnReplacement.getColumnReplacementId() == null) {
			// 添加栏目替代
			columnReplacementService.addColumnReplacement(columnReplacement);
			addInfoMessageToFlash("添加栏目替代成功！");
		} else {
			// 编辑栏目替代
			columnReplacementService.update(columnReplacement);
			addInfoMessageToFlash("编辑栏目替代成功！");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("columnReplacementList.xhtml?multiSelect=true");
		}
	}
}
