/**
 * 
 */
package com.cccollector.news.service.impl;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.ThumbnailDao;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.Thumbnail;
import com.cccollector.news.service.ArticleService;
import com.cccollector.news.service.ThumbnailService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 缩略图服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("thumbnailService")
public class ThumbnailServiceImpl extends GenericServiceHibernateImpl<Integer, Thumbnail> implements ThumbnailService {
	
	@Autowired
	private ThumbnailDao thumbnailDao;
	
	@Autowired
	private ArticleService articleService;
	
	/**
	 * 平台标识符
	 */
	@Value("${platformBundleId}")
	private String platformBundleId;
	
	/**
	 * ImageMagick命令路径
	 */
	@Value("${imageMagickPath}")
	private String imageMagickPath;
	
	/**
	 * Tomcat数据目录
	 */
	@Value("${paths.tomcatDataPath}")
	private String tomcatDataPath;
	
	/**
	 * nginx数据目录
	 */
	@Value("${paths.nginxDataPath}")
	private String nginxDataPath;
	
	/**
	 * 应用根路径键
	 */
	@Value("${paths.webAppRootKey}")
	private String webAppRootKey;
	
	/**
	 * 临时路径
	 */
	@Value("${paths.webTemp}")
	private String webTemp;

	/**
	 * 图像路径
	 */
	@Value("${paths.image}")
	private String image;
	
	/**
	 * 缩略图URL
	 */
	@Value("${paths.thumbnailUrl}")
	private String thumbnailUrl;
	
	@Override
	public String getImageMagickPath() {
		return imageMagickPath;
	}
	
	@Override
	public String getTempPath() {
		return System.getProperty(webAppRootKey) + webTemp + File.separator;
	}

	@Override
	public String getDataPath() {
		return tomcatDataPath + platformBundleId + File.separator;
	}

	@Override
	public String getImagePath() {
		return nginxDataPath + platformBundleId + File.separator + image + File.separator;
	}

	@Override
	public String getPictureThumbnailThumbnailsUrl(Thumbnail thumbnail) {
		return thumbnailUrl + "w0_h100/" + thumbnail.pictureUrlPath();
	}

	@Override
	public void addThumbnail(Thumbnail thumbnail, String pictureFilePath) {
		// 设置存储路径
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(new Date().toString().getBytes());
	        String prefix = new BigInteger(1, messageDigest.digest()).toString(16);
	        thumbnail.setPath(prefix.substring(0, 2) + "/" + prefix.substring(2, 4) + "/" );
		} catch (NoSuchAlgorithmException e) {}

		// 设置排序位置
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("distribution", new QueryField("distributionId", thumbnail.getDistribution().getDistributionId())));
		Integer position = thumbnailDao.max("position", predicateQueryFieldList);
		thumbnail.setPosition(position == null ? 0 : position.intValue() + 1);
		
		// 保存缩略图
		thumbnailDao.save(thumbnail);

		// 处理图片文件
		File tempFile = new File(pictureFilePath);
		File pictureFile = new File(getDataPath() + thumbnail.pictureFilePath());	
		pictureFile.getParentFile().mkdirs();
		try {
			Files.copy(tempFile.toPath(), pictureFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			tempFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void moveThumbnails(List<Thumbnail> thumbnails) {
		int position = 0;
		for (Thumbnail thumbnail : thumbnails) {
			thumbnail.setPosition(position);
			thumbnailDao.update(thumbnail, "position");
			position++;
		}
	}

	@Override
	public void updateThumbnail(Thumbnail thumbnail, String pictureFilePath) {
		// 获取多媒体最新数据
		Thumbnail thumbnailNew = thumbnailDao.loadById(thumbnail.getThumbnailId());
		Distribution distribution = thumbnailNew.getDistribution();
		// 如果分发已发布
		if (distribution.getPublished()) {
			// 更新文章更新时间
			distribution.getArticle().setUpdateTimeDate(new Date());
			articleService.update(distribution.getArticle(), "updateTime");
			
			// 更新文章JSON文件
			articleService.updateArticleJson(distribution.getArticle());
		}
		
		// 处理图片文件
		if (pictureFilePath.startsWith(getTempPath())) {
			File tempFile = new File(pictureFilePath);
			File pictureFile = new File(getDataPath() + thumbnailNew.pictureFilePath());	
			pictureFile.getParentFile().mkdirs();
			try {
				Files.copy(tempFile.toPath(), pictureFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 清除nginx缓存文件
			File[] sizeFiles = new File(getImagePath()).listFiles();
			if (sizeFiles != null) {
				for (File sizeFile : sizeFiles) {
					File pictureImageFile = new File(sizeFile, thumbnailNew.pictureFilePath());
					pictureImageFile.delete();
				}
			}
		}
	}

	@Override
	public void deleteThumbnail(Thumbnail thumbnail) {
		// 删除图片文件
		File pictureFile = new File(getDataPath() + thumbnail.pictureFilePath());
		if (pictureFile.exists()) {
			pictureFile.delete();
		}
		
		// 清除nginx缓存文件
		File[] sizeFiles = new File(getImagePath()).listFiles();
		if (sizeFiles != null) {
			for (File sizeFile : sizeFiles) {
				File pictureImageFile = new File(sizeFile, thumbnail.pictureFilePath());
				pictureImageFile.delete();
			}
		}

		// 删除缩略图
		thumbnailDao.delete(thumbnail);
	}
}
