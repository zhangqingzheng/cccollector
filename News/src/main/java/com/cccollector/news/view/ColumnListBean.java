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

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.cccollector.news.model.Column;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.Site;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 栏目列表Bean类
 *
 * @author 谢朋 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ColumnListBean extends BaseListBean<Column> implements Serializable {

	private static final long serialVersionUID = -1134329683413689809L;

	public ColumnListBean() {
		modelDisplayName = "栏目";
		modelAttributeName = "column";
		modelIdAttributeName = "columnId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Column>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return newsSourceService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Column> loadAllModelList() {
				if (getNewsSource() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("code", true));
				return columnService.loadAll(predicateQueryFieldList, orderQueryFieldList);
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
	 * 新闻源关联可用的站点集合
	 */
	private List<Site> allSites;

	public List<Site> getAllSites() {
		if (allSites == null) {
			// 分发在已发布状态下
			List<QueryField> predicateQueryFieldList = null;
			if (getNewsSource() != null) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSources", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
			}
			predicateQueryFieldList.add(new QueryField("enabled", true));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("enabled", true));	
			orderQueryFieldList.add(new QueryField("position", true));	
			allSites = siteService.loadAll(predicateQueryFieldList, orderQueryFieldList);				
		}
		return allSites;
	}
	
	private Integer _siteId;

	/**
	 * 站点Id
	 */	
	public Integer getSiteId() {
		return _siteId;
	}

	public void setSiteId(Integer siteId) {
		_siteId= siteId;
	}

	private String webAddress;

	/**
	 * 网络地址
	 */
	public String getWebAddress() {
		if (_siteId != null) {
			Site site = siteService.loadById(_siteId);
			webAddress = site.getWebAddress();
		} else {			
			webAddress = allSites.get(0).getWebAddress();
		}			
		return webAddress;
	}

	/**
	 * 根栏目TreeNode
	 */
	private TreeNode rootColumnTreeNode;

	public TreeNode getRootColumnTreeNode() {
		if (rootColumnTreeNode == null) {
			Map<Integer, TreeNode> treeNodeMap = new HashMap<Integer, TreeNode>();
			// 根栏目TreeNode
			Column rootColumn = new Column();
			rootColumn.setColumnId(0);
			rootColumnTreeNode = new DefaultTreeNode(rootColumn);

			// 根据栏目生成TreeNode
			for (Column column : getAllModels()) {
				TreeNode parentTreeNode = column.getParentColumn() == null ? rootColumnTreeNode : treeNodeMap.get(column.getParentColumn().getColumnId());
				TreeNode treeNode = new DefaultTreeNode(column, parentTreeNode);
				treeNodeMap.put(column.getColumnId(), treeNode);
			}
		}
		return rootColumnTreeNode;
	}

	/**
	 * 选择站点
	 */
	public void selectSiteAction() {
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 选中的栏目TreeNode
	 */
	private TreeNode selectedColumnTreeNode;

	public TreeNode getSelectedColumnTreeNode() {
		return selectedColumnTreeNode;
	}

	public void setSelectedColumnTreeNode(TreeNode _selectedColumnTreeNode) {
		selectedColumnTreeNode = _selectedColumnTreeNode;
	}

	/**
	 * 加载栏目图标缩略图
	 */
	public String loadIconThumbnail(Column column) {
		if (column.getIconUpdateTime() != null) {
			return columnService.getIconThumbnailColumnsUrl(column);
		}
		return null;
	}

	/**
	 * 添加栏目
	 */
	public void addColumnAction() {
		String parentColumnId = selectedColumnTreeNode != null ? ((Column) selectedColumnTreeNode.getData()).getColumnId().toString() : "";
		handleNavigation("columnEdit.xhtml?columnId=null&newsSourceId=" + getModelId1() + "&parentColumnId=" + parentColumnId);
	}

	/**
	 * 编辑栏目
	 */
	public void editColumnAction(Column column) {
		handleNavigation("columnEdit.xhtml?columnId=" + column.getColumnId() + "&newsSourceId=" + getModelId1());
	}

	/**
	 * 编辑栏目
	 */
	public void editColumnAction() {
		if (selectedColumnTreeNode == null) {
			addErrorMessage("请选择一个栏目进行编辑！");
			return;
		}

		Column column = (Column) selectedColumnTreeNode.getData();
		handleNavigation("columnEdit.xhtml?columnId=" + column.getColumnId() + "&newsSourceId=" + getModelId1());
	}

	/**
	 * 修改栏目是否可用
	 */
	public void modifyColumnEnabledAction(boolean enabled) {
		if (selectedColumnTreeNode == null) {
			addErrorMessage("请选择一个栏目进行修改！");
			return;
		}

		Column column = (Column) selectedColumnTreeNode.getData();
		column.setEnabled(enabled);
		columnService.update(column, "enabled");
		addInfoMessage("修改栏目是否可用成功！");
	}

	/**
	 * 删除栏目
	 */
	public void deleteColumnAction(Column column) {
		// 栏目如果否包含子对象，则不能被删除
		if (columnService.hasChildren(column)) {
			addErrorMessage("要删除的栏目中包含子对象，请清空子对象再进行删除！");
			return;
		}

		// 删除栏目
		columnService.deleteColumn(column);
		addInfoMessage("删除栏目成功！");

		// 清空选中的TreeNode
		selectedColumnTreeNode = null;
		// 刷新全部模型
		allModels = null;
		// 刷新根栏目TreeNode
		rootColumnTreeNode = null;
	}

	/**
	 * 删除栏目
	 */
	public void deleteColumnAction() {
		if (selectedColumnTreeNode == null) {
			addErrorMessage("请选择一个栏目进行删除！");
			return;
		}

		// 栏目如果否包含子对象，则不能被删除
		Column column = (Column) selectedColumnTreeNode.getData();
		if (columnService.hasChildren(column)) {
			addErrorMessage("要删除的栏目中包含子对象，请清空子对象再进行删除！");
			return;
		}

		// 删除栏目
		columnService.deleteColumn(column);
		// 将选中的TreeNode从父TreeNode中删除
		selectedColumnTreeNode.getParent().getChildren().remove(selectedColumnTreeNode);
		addInfoMessage("删除栏目成功！");

		// 清空选中的TreeNode
		selectedColumnTreeNode = null;
	}

	/**
	 * 生成静态化页面
	 */
	// TODO: 单一栏目静态化预留测试用
	public void createStaticPageAction(Column column) {
		// 静态化壳子
		templateService.staticPageCase(column.getColumnId(), TemplateMapping.TypeEnum.栏目.ordinal(), null);
		// 静态化页面Json数据
		if (!(column.getTemplateTypeEnum() == Column.TemplateTypeEnum.子栏目列表 || column.getTemplateTypeEnum() == Column.TemplateTypeEnum.顶部栏目选择_子栏目列表 || column.getTemplateTypeEnum() == Column.TemplateTypeEnum.顶部栏目选择_顶部轮播推荐_子栏目列表)) {
			templateService.staticPageContent(column.getColumnId(), TemplateMapping.TypeEnum.栏目.ordinal(), null);			
		}

		// 生成静态化页面
		addInfoMessage("生成静态化页面成功！");
		// 清空选中的TreeNode
		selectedColumnTreeNode = null;
	}
}
