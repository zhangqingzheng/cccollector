/**
 * 
 */
package com.cccollector.news.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;

import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.Media;
import com.cccollector.news.model.Thumbnail;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.util.ImageUtils;
import com.cccollector.universal.view.GenericEditBean;

/**
 * 缩略图Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ThumbnailBean extends BaseEditBean<Thumbnail> implements Serializable {

	private static final long serialVersionUID = 494266891260339807L;

	public ThumbnailBean () {
		genericEditBeanHandler = new GenericEditBean.GenericEditBeanHandlerModelRelated<Thumbnail>() {

			@Override
			public Thumbnail loadModel(Integer modelId) {
				Thumbnail thumbnail = null;
				if (modelId == null) {
					thumbnail = new Thumbnail();
					Distribution distribution = getDistribution();
					thumbnail.setDistribution(distribution);
					thumbnail.setWidthRatio(distribution.getListThumbnailWidthRatio());
					thumbnail.setHeightRatio(distribution.getListThumbnailHeightRatio());
				} else {
					thumbnail = thumbnailService.loadById(modelId);

					// 处理图片
					loadPictureFile(1, thumbnailService.getDataPath() + thumbnail.pictureFilePath());
				}
				return thumbnail;
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
	 * 缩略图
	 */
	public Thumbnail getThumbnail() {
		return getModel();
	}

	/**
	 * 所属分发
	 */
	public Distribution getDistribution() {
		return (Distribution) relatedModel(1);
	}

	/**
	 * 选中的多媒体ID
	 */
	private Integer selectedMediaId;

	public Integer getSelectedMediaId() {
		return selectedMediaId;
	}

	public void setSelectedMediaId(Integer _selectedMediaId) {
		selectedMediaId = _selectedMediaId;
	}

	/**
	 * 所有图片的多媒体
	 */
	private List<Media> allMedias;
	
	public List<Media> getAllMedias() {
		if (allMedias == null) {
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", getDistribution().getArticle().getArticleId())));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("position", true));		
			return mediaService.loadAll(predicateQueryFieldList, orderQueryFieldList);			
		}
		return null;		
	}

	/**
	 * 裁切多媒体
	 */
	public void cropMediaAction() {
		Thumbnail thumbnail = getThumbnail();
		if (selectedMediaId != null) {
			try {
				// 多媒体文件路径
				Media media = mediaService.loadById(selectedMediaId);
				String mediaFilePath = mediaService.getDataPath() + media.pictureFilePath();
				// 临时文件路径
				String tempPath = thumbnailService.getTempPath();
				new File(tempPath).mkdirs();
				String tempFileName = new Date().getTime() + "_" + new Random().nextInt(10000) + ".jpg";
				String tempFilePath = tempPath + tempFileName;
				
				// 计算裁切尺寸
				int width = media.getWidth();
				int height = (int) Math.floor(width * 1.0 * thumbnail.getHeightRatio() / thumbnail.getWidthRatio());
				if (height > media.getHeight()) {
					height = media.getHeight();
					width = (int) Math.floor(height * 1.0 * thumbnail.getWidthRatio() / thumbnail.getHeightRatio());
				}
				
				// 裁切
				String imageMagickPath = thumbnailService.getImageMagickPath();
				boolean success = ImageUtils.cropImage(mediaFilePath, tempFilePath, imageMagickPath, width, height);
				if (success) {
					pictureFilePathMap.put(1, tempFilePath);
					FileInputStream fileInputStream = new FileInputStream(pictureFilePathMap.get(1));
					pictureImageMap.put(1, new DefaultStreamedContent(fileInputStream, "image/jpeg"));					
					pictureInfoMap.put(1, new SimpleImageInfo(new File(pictureFilePathMap.get(1))));
				}
			} catch (Exception e) {
				pictureFilePathMap.remove(1);
				pictureImageMap.remove(1);
				pictureInfoMap.remove(1);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存
	 */
	public void saveAction() {
		Thumbnail thumbnail = getThumbnail();
		// 图片必填
		if (pictureFilePath(1) == null) {
			addValidatingMessage("图片为必填项");
			return;
		}
		
		if (thumbnail.getThumbnailId() == null) {			
			// 添加缩略图
			thumbnailService.addThumbnail(thumbnail, pictureFilePath(1));
			addInfoMessageToFlash("添加缩略图成功！");
		} else {
			// 编辑缩略图
			thumbnailService.updateThumbnail(thumbnail, pictureFilePath(1));
			addInfoMessageToFlash("编辑缩略图成功！");
		}

		handleNavigation("thumbnailList.xhtml?multiSelect=true&distributionId=" + getModelId1() + "&articleId=" + getModelId2() + "&newsSourceId=" + getModelId3() + "&specifiedDistributionId=" + (getModelId4() == null ? "" : getModelId4()) + "&published=" + (getModelId6() == null ? "" : getModelId6()) + "&columnId=" + ((getModelId5() == null || getModelId5().equals("null")) ? "" : getModelId5()));
	}
}
