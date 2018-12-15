/**
 * 
 */
package com.cccollector.news.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.CategoryTagDao;
import com.cccollector.news.model.CategoryTag;
import com.cccollector.news.service.CategoryTagService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 分类标签服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("categoryTagService")
public class CategoryTagServiceImpl extends GenericServiceHibernateImpl<Integer, CategoryTag> implements CategoryTagService {

	@Autowired
	private CategoryTagDao categoryTagDao;

	@Override
	public void addCategoryTag(CategoryTag categoryTag) {
		categoryTag.setName("[分类标签]");
		categoryTag.setEnabled(true);

		// 设置排序位置
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", categoryTag.getNewsSource().getNewsSourceId())));
		predicateQueryFieldList.add(categoryTag.getParentCategoryTag() == null ? new QueryField("parentCategoryTag", null, PredicateEnum.IS_NULL) : new QueryField("parentCategoryTag", new QueryField("categoryTagId", categoryTag.getParentCategoryTag().getCategoryTagId())));
		Integer position = categoryTagDao.max("position", predicateQueryFieldList);
		categoryTag.setPosition(position == null ? 0 : position.intValue() + 1);
		
		// 设置排序代码
		String code = String.format("%02d", categoryTag.getPosition());
		if (categoryTag.getParentCategoryTag() != null) {
			code = categoryTag.getParentCategoryTag().getCode() + ":" + code;
		}
		categoryTag.setCode(code);

		// 保存分类标签
		categoryTagDao.save(categoryTag);
	}
	
}
