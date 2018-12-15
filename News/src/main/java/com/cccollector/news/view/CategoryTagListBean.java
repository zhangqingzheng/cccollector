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

import org.primefaces.component.treetable.TreeTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.CategoryTag;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 分类标签列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class CategoryTagListBean extends BaseListBean<CategoryTag> implements Serializable {

	private static final long serialVersionUID = -8669070092668804007L;

	public CategoryTagListBean () {
		modelDisplayName = "分类标签";
		modelAttributeName = "categoryTag";
		modelIdAttributeName = "categoryTagId";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<CategoryTag>() {

			@Override
			public List<CategoryTag> loadAllModelList() {
				if (getNewsSource() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("code", true));
				return categoryTagService.loadAll(predicateQueryFieldList, orderQueryFieldList);
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
	 * 所属新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(1);
	}

	/**
	 * 根分类标签TreeNode
	 */
	private TreeNode rootCategoryTagTreeNode;

	public TreeNode getRootCategoryTagTreeNode() {
		if (rootCategoryTagTreeNode == null) {
			Map<Integer, TreeNode> treeNodeMap = new HashMap<Integer, TreeNode>();
			// 根分类标签TreeNode
			CategoryTag rootCategoryTag = new CategoryTag();
			rootCategoryTag.setCategoryTagId(0);
			rootCategoryTagTreeNode = new DefaultTreeNode(rootCategoryTag);

			// 根据分类标签生成TreeNode
			for (CategoryTag categoryTag : getAllModels()) {
				TreeNode parentTreeNode = categoryTag.getParentCategoryTag() == null ? rootCategoryTagTreeNode : treeNodeMap.get(categoryTag.getParentCategoryTag().getCategoryTagId());
				TreeNode treeNode = new DefaultTreeNode(categoryTag, parentTreeNode);
				treeNodeMap.put(categoryTag.getCategoryTagId(), treeNode);
			}
		}
		return rootCategoryTagTreeNode;
	}

	/**
	 * 选中的TreeNode
	 */
	private TreeNode selectedCategoryTagTreeNode;

	public TreeNode getSelectedCategoryTagTreeNode() {
		return selectedCategoryTagTreeNode;
	}

	public void setSelectedCategoryTagTreeNode(TreeNode _selectedCategoryTagTreeNode) {
		selectedCategoryTagTreeNode = _selectedCategoryTagTreeNode;
	}
	
	/**
	 * 添加分类标签
	 */
	public void addCategoryTagAction() {
		// 分类标签对象
		CategoryTag categoryTag = new CategoryTag();
		categoryTag.setNewsSource(getNewsSource());
		// 分类标签设置为父分类标签
		if (selectedCategoryTagTreeNode != null) {
			categoryTag.setParentCategoryTag((CategoryTag) selectedCategoryTagTreeNode.getData());
		}

		// 添加分类标签
		categoryTagService.addCategoryTag(categoryTag);
		addInfoMessage("添加分类标签成功！");

		// 添加分类标签TreeNode
		if (selectedCategoryTagTreeNode != null) {
			new DefaultTreeNode(categoryTag, selectedCategoryTagTreeNode);
			// 展开父TreeNode
			selectedCategoryTagTreeNode.setExpanded(true);
		} else {
			new DefaultTreeNode(categoryTag, rootCategoryTagTreeNode);
		}
	}

	/**
	 * 编辑分类标签
	 */
	public void onCellEdit(CellEditEvent event) {
		// 获取旧值和修改后的值
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		if (newValue == null || newValue.equals(oldValue)) {
			return;
		}

		// 获取当前编辑的分类标签
		TreeTable treeTable = (TreeTable) event.getSource();
		TreeNode rowNode = treeTable.getRowNode();
		CategoryTag categoryTag = (CategoryTag) rowNode.getData();
		// 获取当前编辑的列，并将对应属性设置为修改后的值
		String categoryTagHeaderText = event.getColumn().getHeaderText();
		if (categoryTagHeaderText.equals("名称")) {
			categoryTag.setName((String) newValue);
		} else if (categoryTagHeaderText.equals("是否可用")) {
			categoryTag.setEnabled((Boolean) newValue);
		} 

		// 编辑分类标签
		categoryTagService.update(categoryTag);
		addInfoMessage("编辑分类标签成功！");
	}

	/**
	 * 修改分类标签是否可用
	 */
	public void modifyCategoryTagEnabledAction(boolean enabled) {
		if (selectedCategoryTagTreeNode == null) {
			addErrorMessage("请选择一个分类标签进行修改！");
			return;
		}

		CategoryTag categoryTag = (CategoryTag) selectedCategoryTagTreeNode.getData();
		categoryTag.setEnabled(enabled);
		categoryTagService.update(categoryTag, "enabled");
		addInfoMessage("修改分类标签是否可用成功！");
	}
	
	/**
	 * 删除分类标签
	 */
	public void deleteCategoryTagAction(CategoryTag categoryTag) {
		// 分类标签如果否包含子对象，则不能被删除
		if (categoryTagService.hasChildren(categoryTag)) {
			addErrorMessage("要删除的分类标签中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 删除分类标签
		categoryTagService.delete(categoryTag);
		addInfoMessage("删除分类标签成功！");

		// 清空选中的TreeNode
		selectedCategoryTagTreeNode = null;
		// 刷新全部模型
		allModels = null;
		// 刷新全部栏分类标签
		rootCategoryTagTreeNode = null;
	}
	
	/**
	 * 删除分类标签
	 */
	public void deleteCategoryTagAction() {
		if (selectedCategoryTagTreeNode == null) {
			addErrorMessage("请选择一个分类标签进行删除！");
			return;
		}

		// 分类标签如果否包含子对象，则不能被删除
		CategoryTag categoryTag = (CategoryTag) selectedCategoryTagTreeNode.getData();
		if (categoryTagService.hasChildren(categoryTag)) {
			addErrorMessage("要删除的分类标签中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 删除分类标签
		categoryTagService.delete(categoryTag);
		// 将选中的TreeNode从父TreeNode中删除
		selectedCategoryTagTreeNode.getParent().getChildren().remove(selectedCategoryTagTreeNode);
		addInfoMessage("删除分类标签成功！");

		// 清空选中的TreeNode
		selectedCategoryTagTreeNode = null;
	}
}
