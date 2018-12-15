/**
 * 
 */
package com.cccollector.news.service;

import java.util.List;

import com.cccollector.news.model.ColumnSubstitute;
import com.cccollector.universal.service.GenericService;

/**
 * 栏目替身服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface ColumnSubstituteService extends GenericService<Integer, ColumnSubstitute> {
	
	/**
	 * 添加栏目替身
	 * 
	 * @param columnSubstitute 待添加的栏目替身
	 */
	public void addColumnSubstitute(ColumnSubstitute columnSubstitute);
	
	/**
	 * 移动栏目替身
	 * 
	 * @param columnSubstitutes 移动后的栏目替身
	 */
	public void moveColumnSubstitutes(List<ColumnSubstitute> columnSubstitutes);
	
}
