/**
 * 
 */
package com.cccollector.app.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.app.dao.BinaryDao;
import com.cccollector.app.model.Binary;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 二进制文件DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("binaryDao")
public class BinaryDaoImpl extends GenericDaoHibernateImpl<Integer, Binary> implements BinaryDao {
	
	public BinaryDaoImpl() {
		super(Binary.class);
	}
}
