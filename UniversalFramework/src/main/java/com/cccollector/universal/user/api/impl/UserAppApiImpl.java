/**
 * 
 */
package com.cccollector.universal.user.api.impl;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.universal.cxf.WebClientUtils;
import com.cccollector.universal.service.GenericServiceHibernateImpl;
import com.cccollector.universal.user.api.UserAppApi;
import com.cccollector.universal.user.dao.UserAppDao;
import com.cccollector.universal.user.model.UserApp;

/**
 * 用户应用API实现类
 *
 * @author 谢朋
 * Copyright © 2017年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("user_userAppApi")
public class UserAppApiImpl extends GenericServiceHibernateImpl<Integer, UserApp> implements UserAppApi {

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
	 * 用户平台api地址
	 */
	@Value("${user.apiAddress}")
	private String userApiAddress;

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
				webClient = WebClientUtils.getMultipartWebClientWithCertificate(userApiAddress, clientCertificatePath.toFile());
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
	public boolean verifyUserAppAccessToken(int userAppId, String accessToken) {
		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return false;
		}
		Response response = webClient.path("/userApps/" + userAppId + "/verifyAccessToken").header("accessToken", accessToken).get();
		return response.getStatus() == Response.Status.OK.getStatusCode();
	}

	@Override
	public UserApp loadUserApp(int userAppId, String accessToken) {
		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return null;
		}
		return webClient.path("/userApps/" + userAppId + "/loggedWithApp").header("accessToken", accessToken).get(new GenericType<UserApp>() {});
	}

	@Override
	@Deprecated
	public UserApp loadUserAppByUserId(int appId, int userId, String accessToken) {
		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return null;
		}
		return webClient.path("/userApps/0/loggedByUserIdWithApp").header("appId", appId).header("userId", userId).header("accessToken", accessToken).get(new GenericType<UserApp>() {});
	}
}
