/**
 * 
 */
package com.cccollector.universal.user.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.universal.cxf.WebClientUtils;
import com.cccollector.universal.service.GenericServiceHibernateImpl;
import com.cccollector.universal.user.dao.UserAppDao;
import com.cccollector.universal.user.model.Transaction;
import com.cccollector.universal.user.service.TransactionService;

/**
 * 交易服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("user_transactionService")
public class TransactionServiceImpl extends GenericServiceHibernateImpl<Integer, Transaction> implements TransactionService {
	
	@SuppressWarnings("unused")
	@Autowired
	private UserAppDao user_transactionDao;
	
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
	public boolean addTransaction(int userAppId, String productName, BigDecimal price, int orderType, int orderId) {
		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return false;
		}
		Form form = new Form();
		form.param("userAppId", userAppId + "");
		form.param("productName", productName);
		form.param("price", price.toString());
		form.param("orderType", orderType + "");
		form.param("orderId", orderId + "");
		Response response = webClient.path("/transactions/addConsume").form(form);
		return response.getStatus() == Response.Status.OK.getStatusCode();
	}

	@Override
	public boolean cancelTransaction(int userAppId, String productName, BigDecimal price, int orderType, int orderId) {
		WebClient webClient = getJsonWebClientWithCertificate();
		if (webClient == null) {
			return false;
		}
		Form form = new Form();
		form.param("userAppId", userAppId + "");
		form.param("productName", productName);
		form.param("price", price.toString());
		form.param("orderType", orderType + "");
		form.param("orderId", orderId + "");
		Response response = webClient.path("/transactions/cancelConsume").form(form);
		return response.getStatus() == Response.Status.OK.getStatusCode();
	}
}
