/**
 * 
 */
package com.cccollector.universal.cxf;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.MultipartProvider;
import org.apache.cxf.transport.http.HTTPConduit;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * WebClient工具类
 * 
 * @author 谢朋
 * Copyright © 2017年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class WebClientUtils {
	
	/**
	 * 获取JSON WebClient
	 * 
	 * @param webServiceAddress 网络服务地址
	 * @return WebClient
	 */
	public static WebClient getJsonWebClient(String webServiceAddress) {
		return WebClientUtils.getJsonWebClientWithCertificate(webServiceAddress, null, null);
	}
	
	/**
	 * 获取带证书的JSON WebClient
	 * 
	 * @param webServiceAddress 网络服务地址
	 * @param clientCertificateFile 客户端证书文件
	 * @return WebClient
	 */
	public static WebClient getJsonWebClientWithCertificate(String webServiceAddress, File clientCertificateFile) {
		// 根据客户端证书文件名获取证书密码
		String clientCertificateFileName = clientCertificateFile.getName();
		String clientCertificatePassword = clientCertificateFileName.substring(0, clientCertificateFileName.indexOf("."));
		
		return WebClientUtils.getJsonWebClientWithCertificate(webServiceAddress, clientCertificateFile, clientCertificatePassword);
	}
	
	/**
	 * 获取带证书的JSON WebClient
	 * 
	 * @param webServiceAddress 网络服务地址
	 * @param clientCertificateFile 客户端证书文件
	 * @param clientCertificatePassword 客户端证书密码
	 * @return WebClient
	 */
	public static WebClient getJsonWebClientWithCertificate(String webServiceAddress, File clientCertificateFile, String clientCertificatePassword) {
		return WebClientUtils.getWebClient(webServiceAddress, MediaType.APPLICATION_JSON_TYPE, clientCertificateFile, clientCertificatePassword);
	}
	
	/**
	 * 获取Multipart WebClient
	 * 
	 * @param webServiceAddress 网络服务地址
	 * @return WebClient
	 */
	public static WebClient getMultipartWebClient(String webServiceAddress) {
		return WebClientUtils.getMultipartWebClientWithCertificate(webServiceAddress, null, null);
	}
	
	/**
	 * 获取带证书的Multipart WebClient
	 * 
	 * @param webServiceAddress 网络服务地址
	 * @param clientCertificateFile 客户端证书文件
	 * @return WebClient
	 */
	public static WebClient getMultipartWebClientWithCertificate(String webServiceAddress, File clientCertificateFile) {
		// 根据客户端证书文件名获取证书密码
		String clientCertificateFileName = clientCertificateFile.getName();
		String clientCertificatePassword = clientCertificateFileName.substring(0, clientCertificateFileName.indexOf("."));
		
		return WebClientUtils.getMultipartWebClientWithCertificate(webServiceAddress, clientCertificateFile, clientCertificatePassword);
	}
	
	/**
	 * 获取带证书的Multipart WebClient
	 * 
	 * @param webServiceAddress 网络服务地址
	 * @param clientCertificateFile 客户端证书文件
	 * @param clientCertificatePassword 客户端证书密码
	 * @return WebClient
	 */
	public static WebClient getMultipartWebClientWithCertificate(String webServiceAddress, File clientCertificateFile, String clientCertificatePassword) {
		return WebClientUtils.getWebClient(webServiceAddress, MediaType.MULTIPART_FORM_DATA_TYPE, clientCertificateFile, clientCertificatePassword);
	}
	
	/**
	 * 获取WebClient
	 * 
	 * @param webServiceAddress 网络服务地址
	 * @param mediaType 媒体类型
	 * @param clientCertificateFile 客户端证书文件
	 * @param clientCertificatePassword 客户端证书密码
	 * @return WebClient
	 */
	public static WebClient getWebClient(String webServiceAddress, MediaType mediaType, File clientCertificateFile, String clientCertificatePassword) {
		try {
			// 构造Provider
			List<Object> providers = new ArrayList<Object>();
			providers.add(new JacksonJsonProvider());
			if (mediaType == MediaType.MULTIPART_FORM_DATA_TYPE) {
				providers.add(new MultipartProvider());
			}
			
			// 创建WebClient
			WebClient webClient = WebClient.create(webServiceAddress, providers).type(mediaType).accept(MediaType.APPLICATION_JSON_TYPE);
			if (clientCertificateFile == null) {
				return webClient;
			}
			
			// 将客户端证书加载到KeyStore
			FileInputStream fileInputStream = new FileInputStream(clientCertificateFile);
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(fileInputStream, clientCertificatePassword.toCharArray());
			fileInputStream.close();
			
			// 将KeyStore存入WebClient
			HTTPConduit conduit = WebClient.getConfig(webClient).getHttpConduit();
			TLSClientParameters clientParameters = new TLSClientParameters();
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("sunx509");
			keyManagerFactory.init(keyStore, clientCertificatePassword.toCharArray());
			clientParameters.setKeyManagers(keyManagerFactory.getKeyManagers());
			conduit.setTlsClientParameters(clientParameters);
			
			return webClient;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
