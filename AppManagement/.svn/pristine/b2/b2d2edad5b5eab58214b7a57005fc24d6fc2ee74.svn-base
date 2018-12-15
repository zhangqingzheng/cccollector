/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.app.model.RootCertificate;

/**
 * 根证书Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class RootCertificateBean extends BaseEditBean<RootCertificate> implements Serializable {

	private static final long serialVersionUID = -1566321669832762744L;

	public RootCertificateBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModel<RootCertificate>() {

			@Override
			public RootCertificate loadModel(Integer modelId) {
				RootCertificate rootCertificate = null;
				if (modelId == null) {
					rootCertificate = new RootCertificate();
				} else {
					rootCertificate = rootCertificateService.loadById(modelId);
				}
				return rootCertificate;
			}
		};
	}

	/**
	 * 根证书
	 */
	public RootCertificate getRootCertificate() {
		return getModel();
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		RootCertificate rootCertificate = getRootCertificate();
		if (rootCertificate.getRootCertificateId() == null) {
			// 添加根证书
			rootCertificateService.addRootCertificate(rootCertificate);
			addInfoMessageToFlash("添加根证书成功！");
		} else {
			// 编辑根证书
			rootCertificateService.update(rootCertificate);
			addInfoMessageToFlash("编辑根证书成功！");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("rootCertificateList.xhtml?multiSelect=true");
		}
	}
}
