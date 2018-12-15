/**
 * 
 */
package com.cccollector.app.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.app.dao.RootCertificateDao;
import com.cccollector.app.model.RootCertificate;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 根证书DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("rootCertificateDao")
public class RootCertificateDaoImpl extends GenericDaoHibernateImpl<Integer, RootCertificate> implements RootCertificateDao {
	
	public RootCertificateDaoImpl() {
		super(RootCertificate.class);
	}
}
