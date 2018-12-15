/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.news.model.Media;
import com.cccollector.news.model.Article;
import com.cccollector.universal.view.GenericEditBean;

/**
 * 多媒体Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class MediaBean extends BaseEditBean<Media> implements Serializable {

	private static final long serialVersionUID = 6705116727637779060L;

	public MediaBean () {
		genericEditBeanHandler = new GenericEditBean.GenericEditBeanHandlerModelRelated<Media>() {

			@Override
			public Media loadModel(Integer modelId) {
				Media media = null;
				if (modelId == null) {
					media = new Media();
					media.setType(Media.TypeEnum.图片.ordinal());
					media.setArticle(getArticle());
				} else {
					media = mediaService.loadById(modelId);

					// 处理图片
					loadPictureFile(1, mediaService.getDataPath() + media.pictureFilePath());
					
					// 处理多媒体
					if (media.getTypeEnum() == Media.TypeEnum.音频) {
						loadFile(1, mediaService.getDataPath() + media.mediaFilePath());
					} else if (media.getTypeEnum() == Media.TypeEnum.视频) {
						loadFile(2, mediaService.getDataPath() + media.mediaFilePath());
					}
				}
				return media;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return articleService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 多媒体
	 */
	public Media getMedia() {
		return getModel();
	}


	/**
	 * 所属文章
	 */
	public Article getArticle() {
		return (Article) relatedModel(1);
	}

	/**
	 * 加载多媒体文件预览
	 */
	public String loadFilePreview() {
		return mediaService.getMediaFilePreviewUrl(getMedia());
	}

	/**
	 * 保存
	 */
	public void saveAction() {
		Media media = getMedia();
		// 图片必填
		if (media.getTypeEnum() == Media.TypeEnum.图片 && pictureFilePath(1) == null) {
			addValidatingMessage("图片为必填项");
			return;
		}

		// 音频必填
		if (media.getTypeEnum() == Media.TypeEnum.音频 && filePath(1) == null) {
			addValidatingMessage("音频为必填项");
			return;
		}

		// 视频必填
		if (media.getTypeEnum() == Media.TypeEnum.视频 && filePath(2) == null) {
			addValidatingMessage("视频为必填项");
			return;
		}

		// 置空
		if (media.getCaption().equals("")) {
			media.setCaption(null);
		}
		
		if (media.getMediaId() == null) {
			// 添加多媒体
			mediaService.addMedia(media, pictureFilePath(1), media.getTypeEnum() == Media.TypeEnum.音频 ? filePath(1) : filePath(2));
			addInfoMessageToFlash("添加多媒体成功！");
		} else {
			// 编辑多媒体
			mediaService.updateMedia(media, pictureFilePath(1), media.getTypeEnum() == Media.TypeEnum.音频 ? filePath(1) : filePath(2));
			addInfoMessageToFlash("编辑多媒体成功！");
		}		
		// 更新文章多媒体数量
		articleService.updateArticleMediaCount(getArticle());
		// 更新文章JSON文件
		articleService.updateArticleJson(getArticle());

		handleNavigation("mediaList.xhtml?multiSelect=true");
	}
}
