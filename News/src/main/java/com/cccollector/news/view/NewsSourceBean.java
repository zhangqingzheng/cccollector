/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.news.model.NewsSource;

/**
 * 新闻源Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class NewsSourceBean extends BaseEditBean<NewsSource> implements Serializable {

	private static final long serialVersionUID = -2842648828994439235L;

	public NewsSourceBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModel<NewsSource>() {

			@Override
			public NewsSource loadModel(Integer modelId) {
				NewsSource newsSource = null;
				if (modelId == null) {
					newsSource = new NewsSource();
					newsSource.setReviewEnabled(false);
					newsSource.setModerateEnabled(false);
					newsSource.setEnabled(true);
				} else {
					newsSource = newsSourceService.loadById(modelId);
				}
				return newsSource;
			}
		};
	}

	/**
	 * 新闻源
	 */
	public NewsSource getNewsSource() {
		return getModel();
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		NewsSource newsSource = getNewsSource();
		// 置空
		if (newsSource.getRemarks().equals("")) {
			newsSource.setRemarks(null);
		}
		
		if (newsSource.getNewsSourceId() == null) {
			// 添加新闻源
			newsSourceService.addNewsSource(newsSource);
			addInfoMessageToFlash("添加新闻源成功！");
		} else {
			// 编辑新闻源
			newsSourceService.update(newsSource);
			addInfoMessageToFlash("编辑新闻源成功！");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("newsSourceList.xhtml?multiSelect=true");
		}
	}
}
