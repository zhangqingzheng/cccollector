/**
 * 
 */
package com.cccollector.universal.user.service.impl;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.universal.cxf.WebClientUtils;
import com.cccollector.universal.service.GenericServiceHibernateImpl;
import com.cccollector.universal.user.dao.UserDao;
import com.cccollector.universal.user.model.User;
import com.cccollector.universal.user.service.UserService;

/**
 * 用户服务实现类
 *
 * @author 杨彪 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("user_userService")
public class UserServiceImpl extends GenericServiceHibernateImpl<Integer, User> implements UserService {

	@SuppressWarnings("unused")
	@Autowired
	private UserDao userDao;

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
	private String webServiceAddress;

	/**
	 * 获取带证书的JSON WebClient
	 * 
	 * @return 带证书的客户端
	 */
	private WebClient getJsonWebClientWithCertificate() {
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
	public Integer addUserAndUserApp(int appId, int editionId, int releaseId, String account, String password) {

		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return null;
		}

		webClient.header("appId", String.valueOf(appId));
		webClient.header("editionId", String.valueOf(editionId));
		webClient.header("releaseId", String.valueOf(releaseId));

		Form form = new Form();
		form.param("account", account);
		form.param("password", password);

		Response response = webClient.path("/users/addUserAndUserApp").form(form);

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			return null;
		}

		Integer userAppId = response.readEntity(new GenericType<Integer>() {});

		return userAppId;
	
	}

	@Override
	public Boolean isAccountExist(String account) {

		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return null;
		}

		Form form = new Form();
		form.param("account", account);

		Response response = webClient.path("/users/isAccountExist").form(form);

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			return null;
		}

		return response.readEntity(new GenericType<Boolean>() {});
	}
}
