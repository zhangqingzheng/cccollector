/**
 * 
 */
package com.cccollector.news.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.GlossaryDao;
import com.cccollector.news.model.Glossary;
import com.cccollector.news.service.GlossaryService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 术语服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("glossaryService")
public class GlossaryServiceImpl extends GenericServiceHibernateImpl<Integer, Glossary> implements GlossaryService {

	@SuppressWarnings("unused")
	@Autowired
	private GlossaryDao glossaryDao;
	
	/**
	 * 平台标识符
	 */
	@Value("${platformBundleId}")
	private String platformBundleId;
	
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

	public String getTempPath() {
		return System.getProperty(webAppRootKey) + webTemp + File.separator;
	}

	@Override
	public String getDataPath() {
		return tomcatDataPath + platformBundleId + File.separator;
	}

	public String getImagePath() {
		return nginxDataPath + platformBundleId + File.separator + image + File.separator;
	}

	@Override
	public String getPictureThumbnailGlossariesUrl(Glossary glossary) {
		return thumbnailUrl + "w0_h100/" + glossary.pictureUrlPath();
	}

	@Override
	public void addGlossary(Glossary glossary, String pictureFilePath) {		
		// 保存术语
		glossaryDao.save(glossary);

		// 处理图片文件
		File pictureFile = new File(getDataPath() + glossary.pictureFilePath());	
		if (pictureFilePath != null) {
			File tempFile = new File(pictureFilePath);
			pictureFile.getParentFile().mkdirs();
			try {
				Files.copy(tempFile.toPath(), pictureFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				tempFile.delete();
				
				// 设置图片更新时间
				glossary.setPictureUpdateTime(new Date());
			} catch (Exception e) {
				e.printStackTrace();
			}			
		} else {
			if (pictureFile.exists()) {
				pictureFile.delete();
			}
			
			// 设置图片更新时间
			glossary.setPictureUpdateTime(null);
		}
	}

	@Override
	public void updateGlossary(Glossary glossary, String pictureFilePath) {
		// 获取术语最新数据
		Glossary glossaryNew = glossaryDao.loadById(glossary.getGlossaryId());
		
		// 更新术语
		glossaryNew.setTitle(glossary.getTitle());
		glossaryNew.setDisplayPriority(glossary.getDisplayPriority());
		glossaryNew.setEnabled(glossary.getEnabled());

		// 处理图片文件
		File pictureFile = new File(getDataPath() + glossaryNew.pictureFilePath());	
		if (pictureFilePath != null) {
			if (pictureFilePath.startsWith(getTempPath())) {
				File tempFile = new File(pictureFilePath);
				pictureFile.getParentFile().mkdirs();
				try {
					Files.copy(tempFile.toPath(), pictureFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					tempFile.delete();
					
					// 设置图片更新时间
					glossaryNew.setPictureUpdateTime(new Date());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 清除nginx缓存文件
				File[] sizeFiles = new File(getImagePath()).listFiles();
				if (sizeFiles != null) {
					for (File sizeFile : sizeFiles) {
						File pictureImageFile = new File(sizeFile, glossaryNew.pictureFilePath());
						pictureImageFile.delete();
					}
				}
			}			
		} else {
			if (pictureFile.exists()) {
				pictureFile.delete();
			}
			
			// 设置图片更新时间
			glossary.setPictureUpdateTime(null);
		}
	}

	@Override
	public void deleteGlossary(Glossary glossary) {
		// 删除图片文件
		File pictureFile = new File(getDataPath() + glossary.pictureFilePath());
		if (pictureFile.exists()) {
			pictureFile.delete();
		}
		
		// 清除nginx缓存文件
		File[] sizeFiles = new File(getImagePath()).listFiles();
		if (sizeFiles != null) {
			for (File sizeFile : sizeFiles) {
				File pictureImageFile = new File(sizeFile, glossary.pictureFilePath());
				pictureImageFile.delete();
			}
		}

		// 删除术语
		glossaryDao.delete(glossary);
	}
}
