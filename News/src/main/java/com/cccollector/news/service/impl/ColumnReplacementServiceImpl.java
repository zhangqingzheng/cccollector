/**
 * 
 */
package com.cccollector.news.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.ColumnReplacementDao;
import com.cccollector.news.model.ColumnReplacement;
import com.cccollector.news.service.ColumnReplacementService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 栏目替代服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("columnReplacementService")
public class ColumnReplacementServiceImpl extends GenericServiceHibernateImpl<Integer, ColumnReplacement> implements ColumnReplacementService {

	@Autowired
	private ColumnReplacementDao columnReplacementDao;
	
	@Override
	public void addColumnReplacement(ColumnReplacement columnReplacement) {
		// 设置排序位置
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("siteVersionId", columnReplacement.getSiteVersion().getSiteVersionId())));
		Integer position = columnReplacementDao.max("position", predicateQueryFieldList);
		columnReplacement.setPosition(position == null ? 0 : position.intValue() + 1);

		// 保存栏目替代
		columnReplacementDao.save(columnReplacement);
	}

	@Override
	public void moveColumnReplacements(List<ColumnReplacement> columnReplacements) {
		int position = 0;
		for (ColumnReplacement columnReplacement : columnReplacements) {
			columnReplacement.setPosition(position);
			columnReplacementDao.update(columnReplacement, "position");
			position++;
		}
	}
}
