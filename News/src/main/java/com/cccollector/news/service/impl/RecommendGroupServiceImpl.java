/**
 * 
 */
package com.cccollector.news.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.RecommendGroupDao;
import com.cccollector.news.model.RecommendGroup;
import com.cccollector.news.service.RecommendGroupService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 推荐组服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("recommendGroupService")
public class RecommendGroupServiceImpl extends GenericServiceHibernateImpl<Integer, RecommendGroup> implements RecommendGroupService {
	
	@Autowired
	private RecommendGroupDao recommendGroupDao;

	@Override
	public void addRecommendGroup(RecommendGroup recommendGroup) {
		// 设置排序位置
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", recommendGroup.getNewsSource().getNewsSourceId())));
		predicateQueryFieldList.add(new QueryField("ownerType", recommendGroup.getOwnerType()));
		predicateQueryFieldList.add(new QueryField("ownerId", recommendGroup.getOwnerId()));

		Integer position = recommendGroupDao.max("position", predicateQueryFieldList);
		recommendGroup.setPosition(position == null ? 0 : position.intValue() + 1);

		// 保存推荐组
		recommendGroupDao.save(recommendGroup);
	}

	@Override
	public void moveRecommendGroups(List<RecommendGroup> recommendGroups, RecommendGroup recommendGroupTo) {
		int position = 0;
		for (RecommendGroup recommendGroup : recommendGroups) {
			if (recommendGroup.getOwnerTypeEnum() == recommendGroupTo.getOwnerTypeEnum() && recommendGroup.getOwnerId().intValue() == recommendGroupTo.getOwnerId()) {
				recommendGroup.setPosition(position);
				recommendGroupDao.update(recommendGroup, "position");
				position++;
			}
		}
	}
}
