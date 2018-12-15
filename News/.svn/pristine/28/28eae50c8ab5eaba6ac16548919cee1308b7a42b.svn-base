/**
 * 
 */
package com.cccollector.news.service;

import java.util.List;

import com.cccollector.news.model.Media;
import com.cccollector.universal.service.GenericService;

/**
 * 多媒体服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface MediaService extends GenericService<Integer, Media> {

	/**
	 * 获取数据目录
	 */
	public String getDataPath();

	/**
	 * 获取多媒体图片缩略图URL
	 */
	public String getMediaPictureThumbnailUrl(Media media);

	/**
	 * 获取多媒体文件预览URL
	 */
	public String getMediaFilePreviewUrl(Media media);
	
	/**
	 * 添加多媒体
	 * 
	 * @param media 待添加的多媒体
	 * @param pictureFilePath 图片文件路径
	 * @param mediaFilePath 多媒体文件路径
	 */
	public void addMedia(Media media, String pictureFilePath, String mediaFilePath);
	
	/**
	 * 移动多媒体
	 * 
	 * @param medias 移动后的多媒体
	 */
	public void moveMedias(List<Media> medias);
	
	/**
	 * 更新多媒体
	 * 
	 * @param media 待更新的多媒体
	 * @param pictureFilePath 图片文件路径
	 * @param mediaFilePath 多媒体文件路径
	 */
	public void updateMedia(Media media, String pictureFilePath, String mediaFilePath);
	
	/**
	 * 删除多媒体
	 * 
	 * @param media 待删除的多媒体
	 */
	public void deleteMedia(Media media);
}
