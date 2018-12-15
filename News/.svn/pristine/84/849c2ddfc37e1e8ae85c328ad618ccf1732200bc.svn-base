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

import com.cccollector.news.model.Article;
import com.cccollector.news.model.Media;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 多媒体列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class MediaListBean extends BaseListBean<Media> implements Serializable {

	private static final long serialVersionUID = -2191040921832234097L;

	public MediaListBean() {
		modelDisplayName = "多媒体";
		modelAttributeName = "media";
		modelIdAttributeName = "mediaId";
		useDialog = false;
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Media>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return articleService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Media> loadAllModelList() {
				if (getArticle() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", getArticle().getArticleId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("position", true));
				return mediaService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}
		};
	}

	/**
	 * 所属文章
	 */
	public Article getArticle() {
		return (Article) relatedModel(1);
	}
	
	/**
	 * 获取文章路径
	 */
	public String getArticlePath() {
		if (getModelId3() == null || getModelId3().isEmpty()) {
			return "articleList.xhtml?newsSourceId=" + getModelId2();
		} else {
			return "columnDistributionList.xhtml?newsSourceId=" + getModelId2() + "&published=" + getModelId3() + "&columnId="  + (getModelId4() != null && !(getModelId4().equals("null"))? getModelId4() : "");
		}
	}

	/**
	 * 加载多媒体图片缩略图
	 */
	public String loadPictureThumbnail(Media media) {
		return mediaService.getMediaPictureThumbnailUrl(media);
	}

	/**
	 * 加载多媒体文件预览
	 */
	public String loadFilePreview(Media media) {
		return mediaService.getMediaFilePreviewUrl(media);
	}

	/**
	 * 移动多媒体
	 */
	public void moveMediaAction(ReorderEvent event) {
		// 移动多媒体
		mediaService.moveMedias(allModels);
		// 更新文章多媒体数量
		articleService.updateArticleMediaCount(getArticle());		
		// 更新文章JSON文件
		articleService.updateArticleJson(getArticle());
		addInfoMessage("移动多媒体成功！");

		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 删除多媒体
	 */
	public void deleteMediaAction(Media media) {
		// 删除多媒体
		mediaService.deleteMedia(media);
		// 更新文章多媒体数量
		articleService.updateArticleMediaCount(getArticle());
		// 更新文章JSON文件
		articleService.updateArticleJson(getArticle());
		addInfoMessage("删除多媒体成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 批量删除多媒体
	 */
	public void deleteMediasAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个多媒体进行删除！");
			return;
		}
		
		// 删除多媒体
		for (Media media : getSelectedModels()) {
			mediaService.deleteMedia(media);
		}			
		// 更新文章多媒体数量
		articleService.updateArticleMediaCount(getArticle());
		// 更新文章JSON文件
		articleService.updateArticleJson(getArticle());
		addInfoMessage("删除多媒体成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
