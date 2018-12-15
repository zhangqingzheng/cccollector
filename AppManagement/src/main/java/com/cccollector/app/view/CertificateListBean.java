/**
 * 
 */
package com.cccollector.app.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.PrimeFaces;
import org.primefaces.component.menuitem.UIMenuItem;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import com.cccollector.app.model.Certificate;
import com.cccollector.app.model.Platform;
import com.cccollector.app.model.RootCertificate;
import com.cccollector.app.model.User;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.view.GenericListBean;

/**
 * 证书列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class CertificateListBean extends BaseListBean<Certificate> implements Serializable {

	private static final long serialVersionUID = -7954886134652538181L;

	public CertificateListBean() {
		modelDisplayName = "证书";
		modelAttributeName = "certificate";
		modelIdAttributeName = "certificateId";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedPage<Certificate>() {

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
				}
				return null;
			}

			@Override
			public List<Certificate> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount) {
				if (getRootCertificate() == null) {
					return null;
				}
				
				// 查询条件
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("rootCertificate", new QueryField("rootCertificateId", getRootCertificate().getRootCertificateId())));
				Object owner = getOwner();
				if (owner != null) {
					predicateQueryFieldList.add(new QueryField("ownerType", owner instanceof Platform ? Certificate.OwnerTypeEnum.平台.ordinal() : Certificate.OwnerTypeEnum.用户.ordinal()));
					predicateQueryFieldList.add(new QueryField("ownerId", owner instanceof Platform ? ((Platform) owner).getPlatformId() : ((User) owner).getUserId()));
				}
				
				// 过滤条件
				for (String key : filters.keySet()) {
					Object value = filters.get(key);
					switch (key) {
					case "ownerType":
						predicateQueryFieldList.add(new QueryField("ownerType", value));
						break;
					case "ownerId":
						predicateQueryFieldList.add(new QueryField("ownerId", value));
						break;
					}
				}

				// 排序
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("ownerType", true));
				orderQueryFieldList.add(new QueryField("ownerId", false));
				orderQueryFieldList.add(new QueryField("enabled", false));
				orderQueryFieldList.add(new QueryField("certificateId", false));
				
				// 总行数
				totalRowCount.append(certificateService.count(predicateQueryFieldList).toString());
				
				return certificateService.loadPage(predicateQueryFieldList, orderQueryFieldList, first, pageSize);
			}
		};
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
		return relatedModel(3);
	}	

	/**
	 * 证书所有者类别枚举数组
	 */
	public Certificate.OwnerTypeEnum[] getOwnerTypeEnums() {
		return Certificate.OwnerTypeEnum.values();
	}

	/**
	 * 选中的所有者类别
	 */
	private Integer selectedOwnerType;
	
	public Integer getSelectedOwnerType() {
		return selectedOwnerType;
	}

	public void setSelectedOwnerType(Integer _selectedOwnerType) {
		selectedOwnerType = _selectedOwnerType;
	}

	/**
	 * 选中的所有者ID
	 */
	private Integer selectedOwnerId;
	
	public Integer getSelectedOwnerId() {
		return selectedOwnerId;
	}

	public void setSelectedOwnerId(Integer _selectedOwnerId) {
		selectedOwnerId = _selectedOwnerId;
	}
	
	/**
	 * 全部所有者
	 */
	public List<?> getAllOwners() {
		if (selectedOwnerType != null && selectedOwnerType == Certificate.OwnerTypeEnum.平台.ordinal()) {
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("enabled", Boolean.FALSE));
			orderQueryFieldList.add(new QueryField("name", Boolean.TRUE));
			return platformService.loadAll(orderQueryFieldList);
		} else if (selectedOwnerType != null && selectedOwnerType == Certificate.OwnerTypeEnum.用户.ordinal()) {
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("username", Boolean.TRUE));
			return userService.loadAll(orderQueryFieldList);
		}
		
		return new ArrayList<>();
	}	

	/**
	 * 获取带PKCS8私钥证书文件
	 */
	public StreamedContent getCertificateWithKeyPKCS8File(Certificate certificate) {
		try {
			FileInputStream fileInputStream = new FileInputStream(rootCertificateService.getDataPath() + certificate.certificateWithKeyPKCS8FilePath());
			StreamedContent certificateWithKeyPKCS8File = new DefaultStreamedContent(fileInputStream, "application/octet-stream", certificate.certificateWithKeyPKCS8FilePath().substring(certificate.certificateWithKeyPKCS8FilePath().lastIndexOf(File.separator) + 1));
			return certificateWithKeyPKCS8File;
		} catch (Exception e) {}
		return null;
	}

	/**
	 * 获取带PKCS12私钥证书文件
	 */
	public StreamedContent getCertificateWithKeyPKCS12File(Certificate certificate) {
		try {
			FileInputStream fileInputStream = new FileInputStream(rootCertificateService.getDataPath() + certificate.certificateWithKeyPKCS12FilePath());
			StreamedContent certificateWithKeyPKCS12File = new DefaultStreamedContent(fileInputStream, "application/octet-stream", certificate.certificateWithKeyPKCS12FilePath().substring(certificate.certificateWithKeyPKCS12FilePath().lastIndexOf(File.separator) + 1));
			return certificateWithKeyPKCS12File;
		} catch (Exception e) {}
		return null;
	}

	/**
	 * 添加证书
	 */
	public void addCertificateAction(ActionEvent actionEvent) {
		// 如果没有选择所有者，则返回
		if ((getModelId2() == null || getModelId2().isEmpty() || getModelId3() == null || getModelId3().isEmpty()) && (selectedOwnerType == null || selectedOwnerId == null)) {
			addErrorMessage("请先选择所有者，再添加证书！");
			return;
		}
		
		if (useDialog && !isMobileScreen() && !(actionEvent.getSource() instanceof UIMenuItem)) {
			// 对话框选项
			Map<String, Object> options = new HashMap<String, Object>();
			options.put("resizable", false);
			options.put("width", 600);
			options.put("height", 400);
			options.put("contentWidth", "100%");
			options.put("contentHeight", "100%");
			options.put("includeViewParams", true);
			
			// 传递的参数
			Map<String, List<String>> params = new HashMap<String, List<String>>();
			List<String> param = new ArrayList<String>();
			param.add("true");
			params.put("useDialog", param);
			param = new ArrayList<String>();
			param.add("");
			params.put(modelIdAttributeName, param);
			if ((getModelId2() == null || getModelId2().isEmpty() || getModelId3() == null || getModelId3().isEmpty()) && (selectedOwnerType != null && selectedOwnerId != null)) {
				param = new ArrayList<String>();
				param.add(selectedOwnerType.toString());
				params.put("selectedOwnerType", param);
				param = new ArrayList<String>();
				param.add(selectedOwnerId.toString());
				params.put("selectedOwnerId", param);
			}
			
			// 显示添加对话框
			PrimeFaces.current().dialog().openDynamic(modelAttributeName + "Edit", options, params);
		} else {
			String navigation = modelAttributeName + "Edit.xhtml?" + modelIdAttributeName + "=null";
			if ((getModelId2() == null || getModelId2().isEmpty() || getModelId3() == null || getModelId3().isEmpty()) && (selectedOwnerType != null && selectedOwnerId != null)) {
				navigation += "&selectedOwnerType=" + selectedOwnerType + "&selectedOwnerId=" + selectedOwnerId;
			}
			handleNavigation(navigation);
		}
	}
	
	/**
	 * 启用证书
	 */
	public void enableAction(Certificate certificate) {
		// 如果已启用，则返回
		if (certificate.getEnabled()) {
			return;
		}
		
		// 启用证书
		certificate.setEnabled(true);
		certificateService.update(certificate, "enabled");																					
		addInfoMessage("启用证书成功！");
		
		// 刷新分页模型
		pageModel = null;
	}
	
	/**
	 * 停用证书
	 */
	public void disableAction(Certificate certificate) {
		// 如果已停用，则返回
		if (!certificate.getEnabled()) {
			return;
		}
		
		// 停用证书
		certificate.setEnabled(false);
		certificateService.update(certificate, "enabled");																					
		addInfoMessage("停用证书成功！");
		
		// 刷新分页模型
		pageModel = null;
	}

	/**
	 * 删除证书
	 */
	public void deleteCertificateAction(Certificate certificate) {
		// 证书如果已启用，则不能被删除
		if (certificate.getEnabled()) {
			addErrorMessage("要删除的证书已启用，请停用后再进行删除！");
			return;			
		}
		
		// 删除证书
		certificateService.deleteCertificate(certificate);
		addInfoMessage("删除证书成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新分页模型
		pageModel = null;
	}
	
	/**
	 * 批量删除证书
	 */
	public void deleteCertificatesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个证书进行删除！");
			return;
		}
		
		for (Certificate certificate : getSelectedModels()) {
			// 证书如果已启用，则不能被删除
			if (certificate.getEnabled()) {
				addErrorMessage("要删除的证书已启用，请停用后再进行删除！");
				return;			
			}
		}
		
		// 删除证书
		for (Certificate certificate : getSelectedModels()) {
			certificateService.deleteCertificate(certificate);				
		}
		addInfoMessage("删除证书成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新分页模型
		pageModel = null;
	}
}
