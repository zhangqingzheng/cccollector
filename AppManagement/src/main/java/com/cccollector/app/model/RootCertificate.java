/**
 * 
 */
package com.cccollector.app.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 根证书类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "ca_rootCertificates")
public class RootCertificate implements Serializable {

	private static final long serialVersionUID = -7677452929350621054L;
	
	private Integer _rootCertificateId;

	/**
	 * 根证书ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getRootCertificateId() {
		return _rootCertificateId;
	}

	public void setRootCertificateId(Integer rootCertificateId) {
		_rootCertificateId = rootCertificateId;
	}

	/**
	 * 根证书URL路径
	 */
	@JsonIgnore
	@Transient
	public String getRootCertificateUrlPath() {
		return "rootCertificates/" + _rootCertificateId + "/";
	}

	/**
	 * 根证书文件路径
	 */
	@JsonIgnore
	@Transient
	public String getRootCertificateFilePath() {
		return "rootCertificates" + File.separator + _rootCertificateId + File.separator;
	}

	private String _name;

	/**
	 * 名称
	 */
	@Column(nullable = false, length = 100)
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	private String _password;

	/**
	 * PKCS密码
	 */
	@Column(nullable = false, length = 10)
	public String getPassword() {
		return _password;
	}

	public void setPassword(String password) {
		_password = password;
	}	

	/**
	 * 根证书URL路径
	 */
	public String rootCertificateUrlPath() {
		return getRootCertificateUrlPath() + _rootCertificateId + "_rootCertificate.pem";
	}

	/**
	 * 根证书文件路径
	 */
	public String rootCertificateFilePath() {
		return getRootCertificateFilePath() + _rootCertificateId + "_rootCertificate.pem";
	}

	/**
	 * 根证书私钥文件路径
	 */
	public String rootKeyFilePath() {
		return getRootCertificateFilePath() + _rootCertificateId + "_rootKey.pem";
	}

	/**
	 * 根证书请求文件路径
	 */
	public String rootRequestFilePath() {
		return getRootCertificateFilePath() + _rootCertificateId + "_rootRequest.csr";
	}

	/**
	 * 带PKCS12私钥根证书文件路径
	 */
	public String rootCertificateWithKeyPKCS12FilePath() {
		return getRootCertificateFilePath() + _rootCertificateId + "_rootCertificateWithKey.p12";
	}

	/**
	 * CA根证书文件名称
	 */
	public String caRootCertificateFileName() {
		return "rootCertificate.pem";
	}

	/**
	 * 客户端根证书文件名称
	 */
	public String clientRootCertificateFileName() {
		return _password + ".p12";
	}

	/**
	 * WEB-INF下CA根证书URL路径
	 */
	public static String webCaRootCertificateUrlPath() {
		return "WEB-INF/caRootCertificate.pem";
	}

	/**
	 * WEB-INF下带PKCS12私钥CA根证书URL路径
	 */
	public static String webCaRootCertificateWithKeyPKCS12UrlPath() {
		return "WEB-INF/caRootCertificate.p12";
	}

	private Date _createTime;

	/**
	 * 创建时间
	 */
	@Column(nullable = false)
	public Date getCreateTime() {
		return _createTime;
	}

	public void setCreateTime(Date createTime) {
		_createTime = createTime;
	}

	private Date _expirationTime;

	/**
	 * 过期时间
	 */
	@Column(nullable = false)
	public Date getExpirationTime() {
		return _expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		_expirationTime = expirationTime;
	}

	private Boolean _enabled;

	/**
	 * 是否可用
	 */
	@Column(nullable = false)
	public Boolean getEnabled() {
		return _enabled;
	}

	public void setEnabled(Boolean enabled) {
		_enabled = enabled;
	}

	/**
	 * 是否可用枚举
	 */
	public static enum EnabledEnum {
		否,
		是
	}

	/**
	 * 是否可用的枚举
	 */
	@Transient
	public EnabledEnum getEnabledEnum() {
		int index = _enabled ? 1 : 0;
		return EnabledEnum.values()[index];
	}

	private List<Certificate> _certificates = new ArrayList<Certificate>();

	/**
	 * 包含的证书
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rootCertificate")
	@OrderBy("certificateId DESC")
	public List<Certificate> getCertificates() {
		return _certificates;
	}

	public void setCertificates(List<Certificate> certificates) {
		_certificates = certificates;
	}
}
