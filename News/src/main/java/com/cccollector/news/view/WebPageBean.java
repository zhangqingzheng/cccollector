/**
 * 
 */
package com.cccollector.news.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;

import com.cccollector.news.model.WebFolder;
import com.cccollector.news.model.WebPage;
/**
 * 网络页面Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class WebPageBean extends BaseEditBean<WebPage> implements Serializable {

	private static final long serialVersionUID = -2928122077608690020L;

	public WebPageBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<WebPage>() {
			
			@Override
			public WebPage loadModel(Integer modelId) {
				WebPage webPage = null;
				if (modelId == null) {
					webPage = new WebPage();
					webPage.setWebFolder(getWebFolder());
					webPage.setStatus(WebPage.StatusEnum.制作中.ordinal());
				} else {
					webPage = webPageService.loadById(modelId);
					
					// 处理文件
					loadFile(1, webPageService.getDataPath() + webPage.webPageFilePath());
				}
				return webPage;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return webFolderService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 网络页面
	 */
	public WebPage getWebPage() {
		return getModel();
	}
	
	/**
	 * 所属网络文件夹
	 */
	public WebFolder getWebFolder() {
		return (WebFolder) relatedModel(1);
	}
	
	/**
	 * 网络页面文件临时路径
	 */
	private String webPageTempFilePath;

	public String getWebPageTempFilePath() {
		return webPageTempFilePath;
	}
	
	/**
	 * 处理网络页面zip包上传与解压缩
	 */
	public void handleWebPageFileUpload(FileUploadEvent fileUploadEvent) {
		try {
			// 保存zip文件
			new File(webPageService.getTempPath()).mkdirs();
			String tempName = new Date().getTime() + "_" + new Random().nextInt(10000);
			String zipFilePath = webPageService.getTempPath() + tempName + ".zip";
			fileUploadEvent.getFile().write(zipFilePath);
			
			// 打开zip文件
			ZipFile zipFile = null;
			try {
				zipFile = new ZipFile(zipFilePath);
				zipFile.entries().nextElement();
			} catch (Exception e) {
				zipFile = null;
			}
			if (zipFile == null) {
				try {
					zipFile = new ZipFile(zipFilePath, Charset.forName("GBK"));
					zipFile.entries().nextElement();
				} catch (Exception e) {
					zipFile = null;
				}
			}
			if (zipFile == null) {
				return;
			}
				
			// 查找根路径
			String rootPath = null;
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.isDirectory() && entry.getName().indexOf("/") == entry.getName().lastIndexOf("/")) {
					rootPath = entry.getName();
					break;
				}
			}
			if (rootPath == null) {
				return;
			}
			
			// 解压缩
			entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				// 创建文件夹或写入文件
				String entryName = entry.getName().replace(rootPath, "");
				File entryOutFile = new File(webPageService.getTempPath() + tempName + "/" + entryName);
				if (entry.isDirectory()) {
					entryOutFile.mkdirs();
				} else {
					entryOutFile.getParentFile().mkdirs();
					InputStream inputStream = zipFile.getInputStream(entry);
					FileOutputStream fileOutputStream = new FileOutputStream(entryOutFile);
					byte[] buffer = new byte[1024];
					int lenth;
					while ((lenth = inputStream.read(buffer)) > 0) {
						fileOutputStream.write(buffer, 0, lenth);
					}
					fileOutputStream.close();
					inputStream.close();
				}
			}
			webPageTempFilePath = webPageService.getTempPath() + tempName;
			zipFile.close();
			new File(zipFilePath).delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		WebPage webPage = getWebPage(); 
		if (webPage.getWebPageId() == null) {
			webPageService.addWebPage(webPage, webPageTempFilePath);
			addInfoMessageToFlash("添加网络页面成功！");
		} else {
			webPageService.updateWebPage(webPage, webPageTempFilePath);
			addInfoMessageToFlash("编辑网络页面成功！");
		}
		handleNavigation("webPageList.xhtml?multiSelect=true");
	}
	
	/**
	 * 取消
	 */
	public void cancelAction() {
		handleNavigation("webPageList.xhtml?multiSelect=true");
	}

	/**
	 * 测试
	 */
	public void testAction() {
		WebPage webPage = getWebPage();
		if (webPage.getStatusEnum() != WebPage.StatusEnum.制作中) {
			return;
		}
		// 设置状态
		webPage.setStatus(WebPage.StatusEnum.测试中.ordinal());
		// 设置更新时间
		webPage.setUpdateTime(new Date());
		
		// 更新状态
		webPageService.update(webPage, "updateTime", "status");					
		addInfoMessage("测试网络页面成功！");
	}

	/**
	 * 撤销测试
	 */
	public void untestAction() {
		WebPage webPage = getWebPage();
		if (webPage.getStatusEnum() != WebPage.StatusEnum.测试中) {
			return;
		}
		// 设置状态
		webPage.setStatus(WebPage.StatusEnum.制作中.ordinal());
		// 设置更新时间
		webPage.setUpdateTime(new Date());
		
		// 更新状态
		webPageService.update(webPage, "updateTime", "status");						
		addInfoMessage("撤销测试网络页面成功！");
	}
}
