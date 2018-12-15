/**
 * 
 */
package com.cccollector.news.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.ColumnSubstituteDao;
import com.cccollector.news.model.ColumnSubstitute;
import com.cccollector.news.service.ColumnSubstituteService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 栏目替身服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("columnSubstituteService")
public class ColumnSubstituteServiceImpl extends GenericServiceHibernateImpl<Integer, ColumnSubstitute> implements ColumnSubstituteService {

	@Autowired
	private ColumnSubstituteDao columnSubstituteDao;
	
	@Override
	public void addColumnSubstitute(ColumnSubstitute columnSubstitute) {
		// 设置排序位置
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("app", new QueryField("appId", columnSubstitute.getApp().getAppId())));
		Integer position = columnSubstituteDao.max("position", predicateQueryFieldList);
		columnSubstitute.setPosition(position == null ? 0 : position.intValue() + 1);

		// 保存栏目替身
		columnSubstituteDao.save(columnSubstitute);
	}

	@Override
	public void moveColumnSubstitutes(List<ColumnSubstitute> columnSubstitutes) {
		int position = 0;
		for (ColumnSubstitute columnSubstitute : columnSubstitutes) {
			columnSubstitute.setPosition(position);
			columnSubstituteDao.update(columnSubstitute, "position");
			position++;
		}
	}
}
