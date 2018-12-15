/**
 * 
 */
package com.cccollector.news.service;

import java.util.List;

import com.cccollector.news.model.Thumbnail;
import com.cccollector.universal.service.GenericService;

/**
 * 缩略图服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface ThumbnailService extends GenericService<Integer, Thumbnail> {
	
	/**
	 * 获取ImageMagick命令路径
	 */
	public String getImageMagickPath();

	/**
	 * 获取临时目录
	 */
	public String getTempPath();

	/**
	 * 获取数据目录
	 */
	public String getDataPath();

	/**
	 * 获取图像目录
	 */
	public String getImagePath();

	/**
	 * 获取缩略图图片缩略图URL
	 */
	public String getPictureThumbnailThumbnailsUrl(Thumbnail thumbnail);
	
	/**
	 * 添加缩略图
	 * 
	 * @param thumbnail 待添加的缩略图
	 * @param pictureFilePath 图片文件路径
	 */
	public void addThumbnail(Thumbnail thumbnail, String pictureFilePath);
	
	/**
	 * 移动缩略图
	 * 
	 * @param thumbnails 移动后的缩略图
	 */
	public void moveThumbnails(List<Thumbnail> thumbnails);
	
	/**
	 * 更新缩略图
	 * 
	 * @param thumbnail 待更新的缩略图
	 * @param pictureFilePath 图片文件路径
	 */
	public void updateThumbnail(Thumbnail thumbnail, String pictureFilePath);
	
	/**
	 * 删除缩略图
	 * 
	 * @param thumbnail 待删除的缩略图
	 */
	public void deleteThumbnail(Thumbnail thumbnail);
}
