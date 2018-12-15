/**
 * 
 */
package com.cccollector.universal.component.service.impl;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.universal.component.dao.RedeemCodeDao;
import com.cccollector.universal.component.model.RedeemCode;
import com.cccollector.universal.component.service.RedeemCodeService;
import com.cccollector.universal.cxf.WebClientUtils;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 兑换码服务实现类
 *
 * @author 杨彪
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("component_redeemCodeService")
public class RedeemCodeServiceImpl extends GenericServiceHibernateImpl<Integer, RedeemCode> implements RedeemCodeService {
	
	@SuppressWarnings("unused")
	@Autowired
	private RedeemCodeDao com_redeemCodeDao;
	
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
	@Value("${appComponent.webServiceAddress}")
	private String appComponentWebServiceAddress;

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
				webClient = WebClientUtils.getMultipartWebClientWithCertificate(appComponentWebServiceAddress, clientCertificatePath.toFile());
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
	public boolean isRedeemCodeCanUse(String code, int userAppId) {

		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return false;
		}

		Response response = webClient.path("/redeemCode/isRedeemCodeCanUse").query("code", code).query("userAppId", String.valueOf(userAppId)).get();

		return Response.Status.OK.getStatusCode() == response.getStatus();
	}
}
