/**
 * 
 */
package com.cccollector.news.service;

import java.util.Date;
import java.util.List;

import com.cccollector.news.model.Distribution;
import com.cccollector.universal.service.GenericService;

/**
 * 分发服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface DistributionService extends GenericService<Integer, Distribution> {

	/**
	 * 校验分发缩略图
	 * 
	 * @param distribution 待校验的分发
	 * @return 是否校验成功
	 */
	public boolean validateDistributionThumbnails(Distribution distribution);
	
	/**
	 * 发布分发
	 * 
	 * @param distribution 待发布的分发
	 * @param principal 发布人登录信息
	 * @param publishTime 发布时间
	 * @param scheduledTime 定时发布时间
	 */
	public void publishDistribution(Distribution distribution, Object principal, Date publishTime, Date scheduledTime);
	
	/**
	 * 撤销发布分发
	 * 
	 * @param distribution 待撤销发布的分发
	 * @return 是否成功
	 */
	public boolean unpublishDistribution(Distribution distribution);
	
	/**
	 * 删除分发
	 * 
	 * @param distribution 待删除的分发
	 */
	public void deleteDistribution(Distribution distribution);
	
	/**
	 * 根据搜索词加载分发列表
	 * 
	 * @param searchWord 搜索词
	 * @return 分发列表
	 */
    public List<Distribution> loadDistributionsBySearchWord(String searchWord);
}