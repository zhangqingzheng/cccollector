/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.app.model.Certificate;
import com.cccollector.app.model.Platform;
import com.cccollector.app.model.RootCertificate;
import com.cccollector.app.model.User;

/**
 * 证书Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class CertificateBean extends BaseEditBean<Certificate> implements Serializable {

	private static final long serialVersionUID = 32433262714765726L;

	public CertificateBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Certificate>() {
			
			@Override
			public Certificate loadModel(Integer modelId) {
				Certificate certificate = null;
				if (modelId == null) {
					certificate = new Certificate();
					certificate.setRootCertificate(getRootCertificate());
					if (getOwner() != null) {
						Object owner = getOwner();
						certificate.setOwnerType(owner instanceof Platform ? Certificate.OwnerTypeEnum.平台.ordinal() : Certificate.OwnerTypeEnum.用户.ordinal());
						certificate.setOwnerId(owner instanceof Platform ? ((Platform) owner).getPlatformId() : ((User) owner).getUserId());
						certificate.setOwnerName(owner instanceof Platform ? ((Platform) owner).getBundleId() : ((User) owner).getUsername());
					}
				} else {
					certificate = certificateService.loadById(modelId);
				}
				return certificate;
			}
			
			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return rootCertificateService.loadById(relatedModelId);
				} else if (index == 3) {
					Integer type = -1;
					try {
						type = Integer.valueOf(getModelId2());
					} catch (Exception e) {}
					if (type != -1) {
						return type == 0 ? platformService.loadById(relatedModelId) : userService.loadById(relatedModelId);
					}
				} else if (index == 5) {
					Integer type = -1;
					try {
						type = Integer.valueOf(getModelId4());
					} catch (Exception e) {}
					if (type != -1) {
						return type == 0 ? platformService.loadById(relatedModelId) : userService.loadById(relatedModelId);
					}
				}
				return null;
			}
		};
	}

	/**
	 * 证书
	 */
	public Certificate getCertificate() {
		return getModel();
	}

	/**
	 * 所属根证书
	 */
	public RootCertificate getRootCertificate() {
		return (RootCertificate) relatedModel(1);
	}

	/**
	 * 所有者
	 */
	public Object getOwner() {
		Object owner = relatedModel(3);
		if (owner == null) {
			owner = relatedModel(5);
		}
		return owner;
	}	

	/**
	 * 保存
	 */
	public void saveAction() {		
		Certificate certificate = getCertificate();
		if (certificate.getCertificateId() == null) {
			// 添加岗位
			Integer certificateId = certificateService.addCertificate(certificate);
			if (certificateId != null) {
				addInfoMessageToFlash("添加证书成功！");
			} else {
				addErrorMessageToFlash("添加证书失败，请重试！");
			}
			
			if (getUsingDialog()) {
				PrimeFaces.current().dialog().closeDynamic(certificateId != null);
			} else {
				handleNavigation("certificateList.xhtml?multiSelect=true");
			}
		}
	}
}
