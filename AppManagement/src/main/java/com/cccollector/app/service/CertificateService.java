/**
 * 
 */
package com.cccollector.app.service;

import com.cccollector.app.model.Certificate;
import com.cccollector.universal.service.GenericService;

/**
 * 证书服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface CertificateService extends GenericService<Integer, Certificate> {

	/**
	 * 获取数据目录
	 */
	public String getDataPath();
	
	/**
	 * 添加证书
	 * 
	 * @param certificate 待添加的证书
	 * @return 添加后的证书的ID
	 */
	public Integer addCertificate(Certificate certificate);
	
	/**
	 * 删除证书
	 * 
	 * @param certificate 待删除的证书
	 */
	public void deleteCertificate(Certificate certificate);
}
