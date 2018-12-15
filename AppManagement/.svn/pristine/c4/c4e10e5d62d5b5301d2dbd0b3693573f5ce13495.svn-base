/**
 * 
 */
package com.cccollector.app.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.app.dao.AppDao;
import com.cccollector.app.dao.CertificateDao;
import com.cccollector.app.dao.PlatformDao;
import com.cccollector.app.dao.RootCertificateDao;
import com.cccollector.app.model.App;
import com.cccollector.app.model.Certificate;
import com.cccollector.app.model.Platform;
import com.cccollector.app.model.RootCertificate;
import com.cccollector.app.service.PlatformService;
import com.cccollector.universal.cxf.WebClientUtils;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 平台服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("platformService")
public class PlatformServiceImpl extends GenericServiceHibernateImpl<Integer, Platform> implements PlatformService {
	
	@Autowired
	private PlatformDao platformDao;
	
	@Autowired
	private AppDao appDao;
	
	@Autowired
	private RootCertificateDao rootCertificateDao;
	
	@Autowired
	private CertificateDao certificateDao;
	
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
	 * 应用根路径键
	 */
	@Value("${paths.webAppRootKey}")
	private String webAppRootKey;

	@Override
	public String getDataPath() {
		return tomcatDataPath + platformBundleId + File.separator;
	}
	
	@Override
	public String getPlatformTemplatesPath() {
		return System.getProperty(webAppRootKey) + "WEB-INF" + File.separator + "platformTemplates" + File.separator;
	}
	
	public String getPlatformPath(Platform platform) {
		return System.getProperty(webAppRootKey) + platform.getPlatformFilePath();
	}

	@Override
	public Response uploadTemplateAndResources(String bundleId, String template) {
//		File platformTemplatesFile = new File(getPlatformTemplatesPath() + bundleId);
//		platformTemplatesFile.mkdirs();
		
//		File platformResourcesFile = new File(getPlatformPath() + bundleId);
//		platformResourcesFile.mkdirs();
		
		return Response.ok().build();
	}

	/**
	 * 获取带证书的Multipart WebClient
	 * 
	 * @param webServiceAddress 网络服务地址
	 * @return 带证书的客户端
	 */
	public WebClient getMultipartWebClientWithCertificate(String webServiceAddress) {
		try {
			WebClient webClient = null;
			File clientCertificatesFile = new File(tomcatDataPath + platformBundleId + File.separator + clientCertificates);
			DirectoryStream<Path> directoryStream = Files.newDirectoryStream(clientCertificatesFile.toPath(), path -> path.toString().endsWith(".p12"));
			for (Path clientCertificatePath : directoryStream) {
				webClient = WebClientUtils.getMultipartWebClientWithCertificate(webServiceAddress, clientCertificatePath.toFile());
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
	public boolean syncCertificatesToPlatform(Platform platform) {
		try {
			// 查询可用的根证书
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("enabled", true));
			List<RootCertificate> rootCertificateList = rootCertificateDao.loadPage(predicateQueryFieldList, null, 0, 1);
			RootCertificate rootCertificate = rootCertificateList.size() > 0 ? rootCertificateList.get(0) : null;
			if (rootCertificate == null) {
				return false;
			}

			// 查询可用的证书
			predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("rootCertificate", new QueryField("rootCertificateId", rootCertificate.getRootCertificateId())));
			predicateQueryFieldList.add(new QueryField("ownerType", Certificate.OwnerTypeEnum.平台.ordinal()));
			predicateQueryFieldList.add(new QueryField("ownerId", platform.getPlatformId()));
			predicateQueryFieldList.add(new QueryField("enabled", true));
			List<Certificate> certificateList = certificateDao.loadPage(predicateQueryFieldList, null, 0, 1);
			Certificate certificate = certificateList != null && certificateList.size() > 0 ? certificateList.get(0) : null;
			if (certificate == null) {
				return false;
			}

			// 获取WebClient
			String webServiceAddress = platform.getTomcatServer() + "/services/webService";
			WebClient webClient = getMultipartWebClientWithCertificate(webServiceAddress);
			if (webClient == null) {
				return false;
			}

			// 构造客户端证书附件
			List<Attachment> attachments = new ArrayList<Attachment>();
			File clientCertificateFile = new File(getDataPath() + certificate.certificateWithKeyPKCS12FilePath());
			ContentDisposition clientCertificateContentDisposition = new ContentDisposition("attachment;filename=" + certificate.clientCertificateFileName());
			FileInputStream clientCertificateFileInputStream = new FileInputStream(clientCertificateFile);
			attachments.add(new Attachment("clientCertificate", clientCertificateFileInputStream, clientCertificateContentDisposition));

			Response response = webClient.path("/app_apps/syncCertificates").post(new MultipartBody(attachments));
			return response.getStatus() == Response.Status.OK.getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void addPlatformToApp(int platformId, int appId) {
		Platform platform = platformDao.loadById(platformId);
		App app = appDao.loadById(appId, "platforms");
		app.getPlatforms().add(platform);
	}

	@Override
	public void deletePlatformFromApp(int platformId, int appId) {
		App app = appDao.loadById(appId, "platforms");
		for (Platform platform : app.getPlatforms()) {
			if (platform.getPlatformId() == platformId) {
				app.getPlatforms().remove(platform);
				return;
			}
		}
	}
}
