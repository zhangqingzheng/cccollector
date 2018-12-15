/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.PrimeFaces;
import org.springframework.core.NestedRuntimeException;

import com.cccollector.app.model.App;

/**
 * 应用Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class AppBean extends BaseEditBean<App> implements Serializable {

	private static final long serialVersionUID = 7166995802786045308L;

	public AppBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModel<App>() {

			@Override
			public App loadModel(Integer modelId) {
				App app = null;
				if (modelId == null) {
					app = new App();
					app.setEnabled(true);
				} else {
					app = appService.loadById(modelId);
				}
				return app;
			}
		};
	}

	/**
	 * 应用
	 */
	public App getApp() {
		return getModel();
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		App app = getApp();
		// 置空
		if (app.getRemarks().equals("")) {
			app.setRemarks(null);
		}
		
		try {
			if (app.getAppId() == null) {
				// 添加应用
				appService.save(app);
				addInfoMessageToFlash("添加应用成功！");
			} else {
				// 编辑应用
				appService.update(app);
				addInfoMessageToFlash("编辑应用成功！");
			}
		} catch (Exception e) {
			if (e instanceof ConstraintViolationException || (e instanceof NestedRuntimeException && ((NestedRuntimeException) e).contains(ConstraintViolationException.class))) {
				addValidatingMessage("标识符必须唯一，不能重复！");
				return;
			} else {
				throw e;
			}
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("appList.xhtml?multiSelect=true");
		}
	}
}
