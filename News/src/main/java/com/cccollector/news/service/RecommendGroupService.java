/**
 * 
 */
package com.cccollector.news.service;

import java.util.List;

import com.cccollector.news.model.RecommendGroup;
import com.cccollector.universal.service.GenericService;

/**
 * 推荐组服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface RecommendGroupService extends GenericService<Integer, RecommendGroup> {
	
	/**
	 * 添加推荐组
	 * 
	 * @param recommendGroup 待添加的推荐组
	 */
	public void addRecommendGroup(RecommendGroup recommendGroup);
	
	/**
	 * 移动推荐组
	 * 
	 * @param recommendGroups 移动后的推荐组
	 * @param recommendGroupTo 移动的推荐组
	 */
	public void moveRecommendGroups(List<RecommendGroup> recommendGroups, RecommendGroup recommendGroupTo);
}
