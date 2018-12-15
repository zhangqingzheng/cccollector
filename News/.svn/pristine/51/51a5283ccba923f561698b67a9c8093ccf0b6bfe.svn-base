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
import org.primefaces.event.SelectEvent;

import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.Thumbnail;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 缩略图列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ThumbnailListBean extends BaseListBean<Thumbnail> implements Serializable {

	private static final long serialVersionUID = 4989065479876577650L;

	public ThumbnailListBean() {
		modelDisplayName = "缩略图";
		modelAttributeName = "thumbnail";
		modelIdAttributeName = "thumbnailId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Thumbnail>() {

			@Override
			public List<Thumbnail> loadAllModelList() {
				if (getDistribution() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("distribution", new QueryField("distributionId", getDistribution().getDistributionId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("position", true));
				return thumbnailService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return distributionService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 所属分发
	 */
	public Distribution getDistribution() {
		return (Distribution) relatedModel(1);
	}

	/**
	 * 加载缩略图图片缩略图
	 */
	public String loadPictureThumbnail(Thumbnail thumbnail) {
		return thumbnailService.getPictureThumbnailThumbnailsUrl(thumbnail);
	}

	/**
	 * 选择缩略图
	 */
	public void selectThumbnailAction(SelectEvent event) {
		Thumbnail thumbnail = (Thumbnail) event.getObject();
		if (getMultiSelect()) {
			handleNavigation("thumbnailEdit.xhtml?thumbnailId=" + thumbnail.getThumbnailId() + "&distributionId=" +  getModelId1() + "&articleId=" + getModelId2() + "&newsSourceId=" + getModelId3() + "&specifiedDistributionId=" + (getModelId4() == null ? "" : getModelId4()) + "&published=" + (getModelId6() == null ? "" : getModelId6()) + "&columnId=" + ((getModelId5() == null || getModelId5().equals("null")) ? "null" : getModelId5()));
		}
	}

	/**
	 * 添加缩略图
	 */
	public void addThumbnailAction() {
		handleNavigation("thumbnailEdit.xhtml?thumbnailId=null&distributionId=" + getModelId1() + "&articleId=" + getModelId2() + "&newsSourceId=" + getModelId3() + "&specifiedDistributionId=" + (getModelId4() == null ? "" : getModelId4()) + "&published=" + (getModelId6() == null ? "" : getModelId6()) + "&columnId=" + ((getModelId5() == null || getModelId5().equals("null")) ? "null" : getModelId5()));
	}

	/**
	 * 编辑缩略图
	 */
	public void editThumbnailAction(Thumbnail thumbnail) {
		handleNavigation("thumbnailEdit.xhtml?thumbnailId=" + thumbnail.getThumbnailId() + "&distributionId=" + getModelId1() + "&articleId=" + getModelId2() + "&newsSourceId=" + getModelId3() + "&specifiedDistributionId=" + (getModelId4() == null ? "" : getModelId4()) + "&published=" + (getModelId6() == null ? "" : getModelId6()) + "&columnId=" + ((getModelId5() == null || getModelId5().equals("null")) ? "null" : getModelId5()));
	}

	/**
	 * 编辑缩略图
	 */
	public void editThumbnailAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个缩略图进行编辑！");
			return;
		}

		if (getSelectedModels().size() == 1) {
			handleNavigation("thumbnailEdit.xhtml?thumbnailId=" + getSelectedModels().get(0).getThumbnailId() + "&distributionId=" + getModelId1() + "&articleId=" + getModelId2() + "&newsSourceId=" + getModelId3() + "&specifiedDistributionId=" + (getModelId4() == null ? "" : getModelId4()) + "&published=" + (getModelId6() == null ? "" : getModelId6()) + "&columnId=" + ((getModelId5() == null || getModelId5().equals("null")) ? "null" : getModelId5()));
		} else {
			addErrorMessage("只能选择一个缩略图进行编辑！");
		}
	}

	/**
	 * 移动缩略图
	 */
	public void moveThumbnailAction(ReorderEvent event) {
		// 移动缩略图
		thumbnailService.moveThumbnails(allModels);
		addInfoMessage("移动缩略图成功！");

		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 删除缩略图
	 */
	public void deleteThumbnailAction(Thumbnail thumbnail) {
		// 删除缩略图
		thumbnailService.deleteThumbnail(thumbnail);
		addInfoMessage("删除缩略图成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 批量删除缩略图
	 */
	public void deleteThumbnailsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个缩略图进行删除！");
			return;
		}
		
		// 删除缩略图图片文件和目录
		for (Thumbnail thumbnail : getSelectedModels()) {
			thumbnailService.deleteThumbnail(thumbnail);
		}
		addInfoMessage("删除缩略图成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
