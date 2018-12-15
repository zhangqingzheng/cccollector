/**
 * 
 */
package com.cccollector.news.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.WebFolderDao;
import com.cccollector.news.model.WebFolder;
import com.cccollector.news.service.WebFolderService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 网络文件夹服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("webFolderService")
public class WebFolderServiceImpl extends GenericServiceHibernateImpl<Integer, WebFolder> implements WebFolderService {

	@Autowired
	private WebFolderDao webFolderDao;
	
	@Override
	public void addWebFolder(WebFolder webFolder) {
		// 设置排序位置
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(webFolder.getParentWebFolder() == null ? new QueryField("parentWebFolder", null, PredicateEnum.IS_NULL) : new QueryField("parentWebFolder", new QueryField("webFolderId", webFolder.getParentWebFolder().getWebFolderId())));
		Integer position = webFolderDao.max("position", predicateQueryFieldList);
		webFolder.setPosition(position == null ? 0 : position.intValue() + 1);
		
		// 设置排序代码
		String code = String.format("%02d", webFolder.getPosition());
		if (webFolder.getParentWebFolder() != null) {
			code = webFolder.getParentWebFolder().getCode() + ":" + code;
		}
		webFolder.setCode(code);
		webFolder.setUpdateTime(new Date());
		// 保存网络文件夹
		webFolderDao.save(webFolder);
	}
}
