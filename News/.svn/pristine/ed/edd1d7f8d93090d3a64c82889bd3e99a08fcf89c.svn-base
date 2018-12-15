/**
 * 
 */
package com.cccollector.news.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.WebPageDao;
import com.cccollector.news.model.WebPage;
import com.cccollector.news.service.WebPageService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 网络页面服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("webPageService")
public class WebPageServiceImpl extends GenericServiceHibernateImpl<Integer, WebPage> implements WebPageService {

	@Autowired
	private WebPageDao webPageDao;
	
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
	 * 发布路径
	 */
	@Value("${paths.publish}")
	private String publish;
	
	@Override
	public String getTempPath() {
		return System.getProperty(webAppRootKey) + webTemp + File.separator;
	}

	@Override
	public String getDataPath() {
		return tomcatDataPath + platformBundleId + File.separator;
	}
	
	@Override
	public String getPublishPath() {
		return nginxDataPath + platformBundleId + File.separator + publish + File.separator;
	}
	
	@Override
	public void addWebPage(WebPage webPage, String webPageTempFilePath) {
		// 保存网络页面
		webPage.setUpdateTime(new Date());
		webPageDao.save(webPage);
		
		// 处理文件
		writeUploadFiles(webPage, webPageTempFilePath);
		
		// 删除临时文件
		deleteWebPageDirectory(new File(webPageTempFilePath));
		
	}
	
	@Override
	public void updateWebPage(WebPage webPage, String webPageTempFilePath) {
		// 保存网络页面
		webPage.setUpdateTime(new Date());
		webPageDao.update(webPage);
		
		if (webPageTempFilePath != null) {
			// 删除旧文件
			deleteWebPageDirectory(new File(getDataPath() + webPage.webPageFilePath()));
			
			// 写入新上传文件
			writeUploadFiles(webPage, webPageTempFilePath);
			
			// 删除临时文件
			deleteWebPageDirectory(new File(webPageTempFilePath));
		}
		
	}
	
	@Override
	public void deleteWebPage(WebPage webPage) {
		// 删除网络页面文件
		File webPageFile = new File(getDataPath() + webPage.webPageFilePath());
		deleteWebPageDirectory(webPageFile);
		
		webPageDao.delete(webPage);
	}
	
	@Override
	public void publishWebPage(WebPage webPage) {
		if (webPage.getStatusEnum() != WebPage.StatusEnum.测试中) {
			return;
		}
		writePublishedFiles(webPage);
		
		// 设置发布时间
		webPage.setPublishTime(new Date());
		// 设置发布状态
		webPage.setStatus(WebPage.StatusEnum.已发布.ordinal());
		
		// 更新发布时间与状态
		webPageDao.update(webPage, "publishTime", "status");
	}

	@Override
	public void cancelPublishWebPage(WebPage webPage) {
		if (webPage.getStatusEnum() != WebPage.StatusEnum.已发布) {
			return;
		}
		
		// 删除nginx发布文件
		File webPagePublishFile = new File(getPublishPath() + webPage.webPageFilePath());
		deleteWebPageDirectory(webPagePublishFile);
		
		// 设置发布时间
		webPage.setPublishTime(null);
		// 设置状态
		webPage.setStatus(WebPage.StatusEnum.已撤销.ordinal());
		
		// 更新网络页面
		webPageDao.update(webPage, "publishTime", "status");
	}
	
	/**
	 * 写入已发布文件
	 */
	private boolean writePublishedFiles(WebPage webPage) {
		try {
			// 拷贝所有内容文件
			File webPageDataFile = new File(getDataPath() + webPage.webPageFilePath());
			Files.walkFileTree(webPageDataFile.toPath(), new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					// 构造发布目录文件，创建发布目录文件夹
					String webPagePath = file.toFile().getAbsolutePath().replace(getDataPath(), "");
					File webPagePublishFile = new File(getPublishPath() + webPagePath);
					webPagePublishFile.getParentFile().mkdirs();
					Files.copy(file, webPagePublishFile.toPath(), StandardCopyOption.REPLACE_EXISTING);						
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
					if (e == null) {
						// 创建发布目录文件夹
						File publishDir = new File(dir.toFile().getAbsolutePath().replace(getDataPath(), getPublishPath()));
						publishDir.mkdirs();
						return FileVisitResult.CONTINUE;
					} else {
						throw e;
					}
				}
			});

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * 写入tomcat文件
	 */
	private boolean writeUploadFiles(WebPage webPage, String webPageTempFilePath) {
		try {
			// 拷贝所有内容文件
			File webPageTempFile = new File(webPageTempFilePath);
			Files.walkFileTree(webPageTempFile.toPath(), new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					// 构造目录文件，创建目录文件夹
					String webPageTempPath = file.toFile().getAbsolutePath().replace(webPageTempFilePath, "");
					File webPageFile = new File(getDataPath() + webPage.webPageFilePath() + webPageTempPath);
					webPageFile.getParentFile().mkdirs();
					Files.copy(file, webPageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);						
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
					if (e == null) {
						// 创建文件夹
						File webPageDir = new File(dir.toFile().getAbsolutePath().replace(webPageTempFilePath, getDataPath() + webPage.webPageFilePath()));
						webPageDir.mkdirs();
						return FileVisitResult.CONTINUE;
					} else {
						throw e;
					}
				}
			});

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * 递归删除网络页面文件夹及其文件
	 */
    private void deleteWebPageDirectory(File file) {
		if (file.exists() && file.isDirectory()) {
			try {
				Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
						if (e == null) {
							Files.delete(dir);
							return FileVisitResult.CONTINUE;
						} else {
							throw e;
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
}
