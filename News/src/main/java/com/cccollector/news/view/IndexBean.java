/**
 * 
 */
package com.cccollector.news.view;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;

/**
 * 首页Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class IndexBean {

	/**
	 * API接口Swagger2Feature
	 */
	@ManagedProperty(value = "#{apiSwagger2Feature}")
	private Swagger2Feature apiSwagger2Feature;

	public void setApiSwagger2Feature(Swagger2Feature _apiSwagger2Feature) {
		apiSwagger2Feature = _apiSwagger2Feature;
	}

	/**
	 * 获取接口文档地址
	 */
	public String getApiDocs() {
		return apiSwagger2Feature.getSchemes()[0] + "://" + apiSwagger2Feature.getHost() + apiSwagger2Feature.getBasePath() + "/api-docs?url=" + apiSwagger2Feature.getBasePath() + "/swagger.json";
	}
	
	/**
	 * 上传模版及资源
	 */
	public void uploadTemplateAndResourcesAction() {
		// 获取平台标识符
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = facesContext.getApplication().getResourceBundle(facesContext, "configBundle");
		String bundleId = resourceBundle.getString("platformBundleId");
		
		// 获取模版文件
		File templateFile = new File(System.getProperty("collectionDisplay.webAppRoot") + resourceBundle.getString("appManagement.templateFile"));
		String template = null;
		try {
			template = new String(Files.readAllBytes(templateFile.toPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Attachment> attachments = new LinkedList<Attachment>();
		ContentDisposition bundleIdContentDisposition = new ContentDisposition("form-data; name='bundleId';");
		attachments.add(new Attachment("bundleId", new ByteArrayInputStream(bundleId.getBytes()), bundleIdContentDisposition));
		ContentDisposition templateContentDisposition = new ContentDisposition("form-data; name='template';");
		attachments.add(new Attachment("template", new ByteArrayInputStream(template.getBytes()), templateContentDisposition));
		MultipartBody multipartBody = new MultipartBody(attachments);
//		appManagementServiceClient.type(MediaType.MULTIPART_FORM_DATA_TYPE).replacePath("/platformService/uploadTemplateAndResources").post(multipartBody);
	}
}
