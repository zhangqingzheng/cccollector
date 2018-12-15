/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cccollector.news.model.Site;
import com.cccollector.news.model.SiteVersion;
/**
 * 站点版本Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class SiteVersionBean extends BaseEditBean<SiteVersion> implements Serializable {

	private static final long serialVersionUID = 4750639273683361199L;

	public SiteVersionBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<SiteVersion>() {
			
			@Override
			public SiteVersion loadModel(Integer modelId) {
				SiteVersion siteVersion = null;
				if (modelId == null) {
					siteVersion = new SiteVersion();
					siteVersion.setSite(getSite());
					siteVersion.setStatus(SiteVersion.StatusEnum.制作中.ordinal());
				} else {
					siteVersion = siteVersionService.loadById(modelId);
				}
				return siteVersion;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return siteService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 站点版本
	 */
	public SiteVersion getSiteVersion() {
		return getModel();
	}
	
	/**
	 * 所属站点
	 */
	public Site getSite() {
		return (Site) relatedModel(1);
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		SiteVersion siteVersion = getSiteVersion(); 
		if (siteVersion.getSiteVersionId() == null) {	
			siteVersionService.addSiteVersion(siteVersion);
			addInfoMessageToFlash("添加站点版本成功！");
		} else {
			// 编辑站点版本
			siteVersionService.update(siteVersion);
			addInfoMessageToFlash("编辑站点版本成功！");
		}
		
		handleNavigation("siteVersionList.xhtml?multiSelect=true");
	}

	/**
	 * 取消
	 */
	public void cancelAction() {
		handleNavigation("siteVersionList.xhtml?multiSelect=true");
	}

	/**
	 * 测试
	 */
	public void testAction() {
		siteVersionService.testSiteVersion(getSiteVersion());
		addInfoMessage("测试站点版本成功！");
	}

	/**
	 * 撤销测试
	 */
	public void untestAction() {
		SiteVersion siteVersion = getSiteVersion();
		if (siteVersion.getStatus() == SiteVersion.StatusEnum.测试中.ordinal()) {
			// 设置状态
			siteVersion.setStatus(SiteVersion.StatusEnum.制作中.ordinal());

			// 更新状态
			siteVersionService.update(siteVersion, "status");					
			addInfoMessage("撤销测试站点版本成功！");
		}
	}
}
