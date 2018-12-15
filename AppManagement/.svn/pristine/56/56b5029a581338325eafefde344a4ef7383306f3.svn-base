/**
 * 
 */
package com.cccollector.app.model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 证书类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Entity
@Table(name = "ca_certificates")
public class Certificate implements Serializable {

	private static final long serialVersionUID = -9063397979074171371L;
	
	private Integer _certificateId;

	/**
	 * 证书ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getCertificateId() {
		return _certificateId;
	}
	
	public void setCertificateId(Integer certificateId) {
		_certificateId = certificateId;
	}

	/**
	 * 证书URL路径
	 */
	@JsonIgnore
	@Transient
	public String getCertificateUrlPath() {
		return getRootCertificate().getRootCertificateUrlPath() + "certificates/" + _certificateId + "/";
	}

	/**
	 * 证书文件路径
	 */
	@JsonIgnore
	@Transient
	public String getCertificateFilePath() {
		return getRootCertificate().getRootCertificateFilePath() + "certificates" + File.separator + _certificateId + File.separator;
	}

	private Integer _ownerType;

	/**
	 * 所有者类别
	 */
	@Column(nullable = false)
	public Integer getOwnerType() {
		return _ownerType;
	}

	public void setOwnerType(Integer ownerType) {
		_ownerType = ownerType;
	}

	/**
	 * 所有者类别枚举
	 */
	public static enum OwnerTypeEnum {
		平台,
		用户
	}

	/**
	 * 所有者类别枚举数组
	 */
	@JsonIgnore
	@Transient
	public OwnerTypeEnum[] getOwnerTypeEnums() {
		return OwnerTypeEnum.values();
	}

	/**
	 * 所有者类别的枚举
	 */
	@JsonIgnore
	@Transient
	public OwnerTypeEnum getOwnerTypeEnum() {
		return OwnerTypeEnum.values()[_ownerType];
	}

	private Integer _ownerId;

	/**
	 * 所有者ID
	 */
	@Column(nullable = false)
	public Integer getOwnerId() {
		return _ownerId;
	}
	
	public void setOwnerId(Integer ownerId) {
		_ownerId = ownerId;
	}

	private String _ownerName;

	/**
	 * 所有者名称
	 */
	@Column(nullable = false, length = 100)
	public String getOwnerName() {
		return _ownerName;
	}
	
	public void setOwnerName(String ownerName) {
		_ownerName = ownerName;
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
	 * 证书URL路径
	 */
	public String certificateUrlPath() {
		return getCertificateUrlPath() + _certificateId + "_certificate.pem";
	}

	/**
	 * 证书文件路径
	 */
	public String certificateFilePath() {
		return getCertificateFilePath() + _certificateId + "_certificate.pem";
	}

	/**
	 * 证书私钥文件路径
	 */
	public String keyFilePath() {
		return getCertificateFilePath() + _certificateId + "_key.pem";
	}

	/**
	 * 证书请求文件路径
	 */
	public String requestFilePath() {
		return getCertificateFilePath() + _certificateId + "_request.csr";
	}

	/**
	 * 证书私钥PKCS8文件路径
	 */
	public String keyPKCS8FilePath() {
		return getCertificateFilePath() + _certificateId + "_key_pkcs8.pem";
	}

	/**
	 * 带PKCS8私钥证书文件路径
	 */
	public String certificateWithKeyPKCS8FilePath() {
		return getCertificateFilePath() + _certificateId + "_certificateWithKey.pem";
	}

	/**
	 * 带PKCS12私钥证书文件路径
	 */
	public String certificateWithKeyPKCS12FilePath() {
		return getCertificateFilePath() + _certificateId + "_certificateWithKey.p12";
	}

	/**
	 * 客户端证书文件名称
	 */
	public String clientCertificateFileName() {
		return _password + ".p12";
	}

	private Date _issueTime;

	/**
	 * 签发时间
	 */
	@Column(nullable = false)
	public Date getIssueTime() {
		return _issueTime;
	}
	
	public void setIssueTime(Date issueTime) {
		_issueTime = issueTime;
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
	
	private RootCertificate _rootCertificate;
	
	/**
	 * 所属的根证书
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "rootCertificateId", nullable = false)
	public RootCertificate getRootCertificate() {
		return _rootCertificate;
	}
	
	public void setRootCertificate(RootCertificate rootCertificate) {
		_rootCertificate = rootCertificate;
	}
}
