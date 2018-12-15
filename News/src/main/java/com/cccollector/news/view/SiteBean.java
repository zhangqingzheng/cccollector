/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.news.model.Site;

/**
 * 站点Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class SiteBean extends BaseEditBean<Site> implements Serializable {

	private static final long serialVersionUID = -1614971229353642363L;

	public SiteBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModel<Site>() {

			@Override
			public Site loadModel(Integer modelId) {
				Site site = null;
				if (modelId == null) {
					site = new Site();
					site.setEnabled(true);
				} else {
					site = siteService.loadById(modelId);
				}
				return site;
			}
		};
	}

	/**
	 * 站点
	 */
	public Site getSite() {
		return getModel();
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		Site site = getSite();
		// 置空
		if (site.getRemarks().equals("")) {
			site.setRemarks(null);
		}
		
		if (site.getSiteId() == null) {
			// 添加站点
			siteService.addSite(site);
			addInfoMessageToFlash("添加站点成功！");
		} else {
			// 编辑站点
			siteService.update(site);
			addInfoMessageToFlash("编辑站点成功！");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("siteList.xhtml?multiSelect=true");
		}
	}
}