/**
 * 
 */
package com.cccollector.news.service;

import com.cccollector.news.model.CategoryTag;
import com.cccollector.universal.service.GenericService;

/**
 * 分类标签服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface CategoryTagService extends GenericService<Integer, CategoryTag> {
	
	/**
	 * 添加分类标签
	 * 
	 * @param categoryTag 待添加的分类标签
	 */
	public void addCategoryTag(CategoryTag categoryTag);

}
