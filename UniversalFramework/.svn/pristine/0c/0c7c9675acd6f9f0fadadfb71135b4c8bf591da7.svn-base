/**
 * 
 */
package com.cccollector.universal.view;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 * 泛型列表Bean处理器接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface GenericListBeanHandler<T> extends BaseBeanHandler {
	
	/**
	 * 加载全部模型列表
	 * 
	 * @return 模型列表
	 */
	public List<T> loadAllModelList();
	
	/**
	 * 加载分页模型列表
	 * 
	 * @param first 起始行数
	 * @param pageSize 每页行数
	 * @param sortField 排序字段
	 * @param sortOrder 排序顺序
	 * @param filters 过滤字段
	 * @param totalRowCount 返回的总行数
	 * @return 模型列表
	 */
	public List<T> loadPageModelList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, StringBuffer totalRowCount);
	
	/**
	 * 加载多字段排序分页模型列表
	 * 
	 * @param first 起始行数
	 * @param pageSize 每页行数
	 * @param multiSortMeta 多字段排序
	 * @param filters 过滤字段
	 * @param totalRowCount 返回的总行数
	 * @return 模型列表
	 */
	public List<T> loadMultiSortPageModelList(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String,Object> filters, StringBuffer totalRowCount);
}
