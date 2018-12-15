/**
 * 
 */
package com.cccollector.universal.app.service.impl;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.universal.app.dao.AppDao;
import com.cccollector.universal.app.model.App;
import com.cccollector.universal.app.service.AppService;
import com.cccollector.universal.cxf.WebClientUtils;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 应用服务实现类
 *
 * @author 谢朋
 * Copyright © 2017年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("app_appService")
public class AppServiceImpl extends GenericServiceHibernateImpl<Integer, App> implements AppService {
	
	@SuppressWarnings("unused")
	@Autowired
	private AppDao app_appDao;
	
	/**
	 * 平台标识符
	 */
	@Value("${platformBundleId}")
	private String platformBundleId;

	/**
	 * Tomcat数据目录
	 */
	@Value("${paths.tomcatDataPath}")
	private String tomcatDataPath;
	
	/**
	 * 证书颁发机构路径
	 */
	@Value("${paths.caCertificates}")
	private String caCertificates;
	
	/**
	 * 客户端证书路径
	 */
	@Value("${paths.clientCertificates}")
	private String clientCertificates;
	
	/**
	 * 应用管理平台webService地址
	 */
	@Value("${appManagement.webServiceAddress}")
	private String appManagementWebServiceAddress;

	@Override
	public Response syncCertificates(Attachment caCertificate, Attachment clientCertificate) {
		// 证书颁发机构和客户端证书不能都为空
		if (caCertificate == null && clientCertificate == null) {
			return Response.status(Response.Status.EXPECTATION_FAILED).build();
		}
		
		try {
			// 证书颁发机构路径
			File caCertificatesFile = new File(tomcatDataPath + platformBundleId + File.separator + caCertificates);
			// 如果证书颁发机构不为空，则保存证书颁发机构到证书颁发机构路径
			if (caCertificate != null) {
				File caCertificateFile = new File(caCertificatesFile, caCertificate.getContentDisposition().getFilename());
				caCertificate.transferTo(caCertificateFile);
			}
			
			// 客户端证书路径
			File clientCertificatesFile = new File(tomcatDataPath + platformBundleId + File.separator + clientCertificates);
			clientCertificatesFile.mkdirs();
			// 从客户端证书路径删除所有客户端证书文件
			Files.newDirectoryStream(clientCertificatesFile.toPath()).forEach(path -> path.toFile().delete());
			// 如果客户端证书不为空，则保存客户端证书到客户端证书路径
			if (clientCertificate != null) {
				File clientCertificateFile = new File(clientCertificatesFile, clientCertificate.getContentDisposition().getFilename());
				clientCertificate.transferTo(clientCertificateFile);
			}
			
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.EXPECTATION_FAILED).build();
		}
	}

	/**
	 * 获取带证书的JSON WebClient
	 * 
	 * @return 带证书的客户端
	 */
	public WebClient getJsonWebClientWithCertificate() {
		try {
			WebClient webClient = null;
			File clientCertificatesFile = new File(tomcatDataPath + platformBundleId + File.separator + clientCertificates);
			DirectoryStream<Path> directoryStream = Files.newDirectoryStream(clientCertificatesFile.toPath(), path -> path.toString().endsWith(".p12"));
			for (Path clientCertificatePath : directoryStream) {
				webClient = WebClientUtils.getMultipartWebClientWithCertificate(appManagementWebServiceAddress, clientCertificatePath.toFile());
				break;
			}
			directoryStream.close();
			return webClient;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public App loadAppByAppId(int appId) {
		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return null;
		}
		return webClient.path("/apps/" + appId).get(new GenericType<App>() {});
	}
	
	@Override
	public App loadAppWithEditionsAndReleasesByAppId(int appId) {
		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return null;
		}
		return webClient.path("/apps/" + appId + "/withEditionsAndReleases").get(new GenericType<App>() {});
	}
	
	@Override
	public List<App> loadAllApps() {
		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return null;
		}
		return webClient.path("/apps/findByPlatformBundleId").query("platformBundleId", platformBundleId).get(new GenericType<List<App>>() {});
	}
}
