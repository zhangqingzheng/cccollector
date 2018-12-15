/**
 * 
 */
package com.cccollector.universal.user.service.impl;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.ws.rs.core.GenericType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.universal.cxf.WebClientUtils;
import com.cccollector.universal.service.GenericServiceHibernateImpl;
import com.cccollector.universal.user.dao.UserAppDao;
import com.cccollector.universal.user.model.UserApp;
import com.cccollector.universal.user.service.UserAppService;

/**
 * 用户应用服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("user_userAppService")
public class UserAppServiceImpl extends GenericServiceHibernateImpl<Integer, UserApp> implements UserAppService {
	
	@SuppressWarnings("unused")
	@Autowired
	private UserAppDao user_userAppDao;
	
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
	 * 客户端证书路径
	 */
	@Value("${paths.clientCertificates}")
	private String clientCertificates;
	
	/**
	 * 用户平台webService地址
	 */
	@Value("${user.webServiceAddress}")
	private String userWebServiceAddress;

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
				webClient = WebClientUtils.getMultipartWebClientWithCertificate(userWebServiceAddress, clientCertificatePath.toFile());
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
	public UserApp loadUserAppByUserAppId(int userAppId) {
		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return null;
		}
		return webClient.path("/userApps/" + userAppId).get(new GenericType<UserApp>() {});
	}

	@Override
	public List<UserApp> loadUserAppsBySearchWord(String searchWord) {
		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return null;
		}
		return webClient.path("/userApps/findBySearchWord").query("searchWord", searchWord).get(new GenericType<List<UserApp>>() {});
	}
}
