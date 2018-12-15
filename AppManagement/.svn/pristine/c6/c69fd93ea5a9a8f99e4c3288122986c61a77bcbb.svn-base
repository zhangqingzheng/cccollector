/**
 * 
 */
package com.cccollector.app.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.cccollector.app.model.RootCertificate;
import com.cccollector.universal.dao.QueryField;

/**
 * 根证书列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class RootCertificateListBean extends BaseListBean<RootCertificate> implements Serializable {

	private static final long serialVersionUID = 1420657042836180954L;

	public RootCertificateListBean () {
		modelDisplayName = "根证书";
		modelAttributeName = "rootCertificate";
		modelIdAttributeName = "rootCertificateId";
		submodelAttributeName = "certificate";
		genericListBeanHandler = new GenericListBeanHandlerAll<RootCertificate>() {

			@Override
			public List<RootCertificate> loadAllModelList() {
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("rootCertificateId", false));
				return rootCertificateService.loadAll(orderQueryFieldList);
			}
		};
	}

	/**
	 * 获取根证书文件
	 */
	public StreamedContent getRootCertificateFile(RootCertificate rootCertificate) {
		try {
			FileInputStream fileInputStream = new FileInputStream(rootCertificateService.getDataPath() + rootCertificate.rootCertificateFilePath());
			StreamedContent rootCertificateFile = new DefaultStreamedContent(fileInputStream, "application/octet-stream", rootCertificate.rootCertificateFilePath().substring(rootCertificate.rootCertificateFilePath().lastIndexOf(File.separator) + 1));
			return rootCertificateFile;
		} catch (Exception e) {}
		return null;
	}
	
	/**
	 * 启用根证书
	 */
	public void enableAction(RootCertificate rootCertificate) {
		// 如果已启用，则返回
		if (rootCertificate.getEnabled()) {
			return;
		}
		
		// 启用根证书
		boolean success = rootCertificateService.enableRootCertificate(rootCertificate);
		if (success) {
			addInfoMessage("启用根证书成功！");
		} else {
			addErrorMessage("启用根证书失败，请重新尝试！");
		}
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 停用根证书
	 */
	public void disableAction(RootCertificate rootCertificate) {
		// 如果已停用，则返回
		if (!rootCertificate.getEnabled()) {
			return;
		}
		
		// 停用根证书
		boolean success = rootCertificateService.disableRootCertificate(rootCertificate);
		if (success) {
			addInfoMessage("停用根证书成功！");
		} else {
			addErrorMessage("停用根证书失败，请重新尝试！");
		}
		
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 删除根证书
	 */
	public void deleteRootCertificateAction(RootCertificate rootCertificate) {
		// 根证书如果否包含子对象，则不能被删除
		if (rootCertificateService.hasChildren(rootCertificate)) {
			addErrorMessage("要删除的根证书中包含子对象，请清空子对象再进行删除！");
			return;
		}
		
		// 根证书如果已启用，则不能被删除
		if (rootCertificate.getEnabled()) {
			addErrorMessage("要删除的根证书已启用，请停用后再进行删除！");
			return;			
		}
		
		// 删除根证书
		rootCertificateService.deleteRootCertificate(rootCertificate);
		addInfoMessage("删除根证书成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除根证书
	 */
	public void deleteRootCertificatesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个根证书进行删除！");
			return;
		}
		
		for (RootCertificate rootCertificate : getSelectedModels()) {
			// 根证书如果否包含子对象，则不能被删除
			if (rootCertificateService.hasChildren(rootCertificate)) {
				addErrorMessage("要删除的根证书中包含子对象，请清空子对象再进行删除！");
				return;
			}
			
			// 根证书如果已启用，则不能被删除
			if (rootCertificate.getEnabled()) {
				addErrorMessage("要删除的根证书已启用，请停用后再进行删除！");
				return;			
			}
		}
		
		// 删除根证书
		for (RootCertificate rootCertificate : getSelectedModels()) {
			rootCertificateService.deleteRootCertificate(rootCertificate);				
		}
		addInfoMessage("删除根证书成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
