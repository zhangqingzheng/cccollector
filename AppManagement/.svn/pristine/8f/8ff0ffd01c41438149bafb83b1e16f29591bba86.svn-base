/**
 * 
 */
package com.cccollector.app.service;

import com.cccollector.app.model.RootCertificate;
import com.cccollector.universal.service.GenericService;

/**
 * 根证书服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface RootCertificateService extends GenericService<Integer, RootCertificate> {

	/**
	 * 获取数据目录
	 */
	public String getDataPath();
	
	/**
	 * 获取可用的根证书
	 */
	public RootCertificate getEnabledRootCertificate();
	
	/**
	 * 添加根证书
	 * 
	 * @param rootCertificate 待添加的根证书
	 */
	public void addRootCertificate(RootCertificate rootCertificate);
	
	/**
	 * 启用根证书
	 * 
	 * @param rootCertificate 待启用的根证书
	 * @return 是否成功
	 */
	public boolean enableRootCertificate(RootCertificate rootCertificate);
	
	/**
	 * 停用根证书
	 * 
	 * @param rootCertificate 待停用的根证书
	 * @return 是否成功
	 */
	public boolean disableRootCertificate(RootCertificate rootCertificate);
	
	/**
	 * 删除根证书
	 * 
	 * @param rootCertificate 待删除的根证书
	 */
	public void deleteRootCertificate(RootCertificate rootCertificate);
}
