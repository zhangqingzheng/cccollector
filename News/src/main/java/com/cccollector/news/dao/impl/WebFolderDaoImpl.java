/**
 * 
 */
package com.cccollector.news.dao.impl;

import org.springframework.stereotype.Repository;

import com.cccollector.news.dao.WebFolderDao;
import com.cccollector.news.model.WebFolder;
import com.cccollector.universal.dao.GenericDaoHibernateImpl;

/**
 * 网络文件夹DAO实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Repository("webFolderDao")
public class WebFolderDaoImpl extends GenericDaoHibernateImpl<Integer, WebFolder> implements WebFolderDao {
	
	public WebFolderDaoImpl() {
		super(WebFolder.class);
	}
}
