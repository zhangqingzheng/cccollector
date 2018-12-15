/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.news.model.SiteVersion;
import com.cccollector.news.model.WebFolder;

/**
 * 网络文件夹Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class WebFolderBean extends BaseEditBean<WebFolder> implements Serializable {

	private static final long serialVersionUID = -6878487854625217268L;

	public WebFolderBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<WebFolder>() {
			
			@Override
			public WebFolder loadModel(Integer modelId) {
				WebFolder webFolder = null;
				if (modelId == null) {
					webFolder = new WebFolder();
					webFolder.setParentWebFolder(getParentWebFolder());
					webFolder.setStatus(WebFolder.StatusEnum.制作中.ordinal());
					webFolder.setSiteVersion(getSiteVersion());
				} else {
					webFolder = webFolderService.loadById(modelId);
				}
				return webFolder;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return siteVersionService.loadById(relatedModelId);
				}
				if (index == 2) {
					return webFolderService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}
	
	/**
	 * 网络文件夹
	 */
	public WebFolder getWebFolder() {
		return getModel();
	}
	
	/**
	 * 所属站点版本
	 */
	public SiteVersion getSiteVersion() {
		return (SiteVersion) relatedModel(1);
	}
	
	/**
	 * 父网络文件夹
	 */
	public WebFolder getParentWebFolder() {
		return (WebFolder) relatedModel(2);
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		WebFolder webFolder = getWebFolder();
		
		if (webFolder.getWebFolderId() == null) {
			// 添加网络文件夹
			webFolderService.addWebFolder(webFolder);
			addInfoMessageToFlash("添加网络文件夹成功！");
		} else {
			// 编辑网络文件夹
			webFolderService.update(webFolder);
			addInfoMessageToFlash("编辑网络文件夹成功！");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("webFolderList.xhtml?multiSelect=true");
		}
	}
}
