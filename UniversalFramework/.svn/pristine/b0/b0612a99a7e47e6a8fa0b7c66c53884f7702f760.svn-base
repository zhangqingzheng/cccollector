/**
 * 
 */
package com.cccollector.universal.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 * 泛型延迟加载数据处理接口
 *
 * @author 谢朋
 * Copyright © 2016年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface GenericLazyDataHandler<K extends Serializable, T extends Serializable> {
	
	/**
	 * 分页加载实体对象列表
	 * 
	 * @param first 起始行数
	 * @param pageSize 每页行数
	 * @param sortField 排序字段
	 * @param sortOrder 排序顺序
	 * @param filters 过滤字段
	 * @return 实体对象列表
	 */
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters);
	
	/**
	 * 分页加载实体对象列表
	 * 
	 * @param first 起始行数
	 * @param pageSize 每页行数
	 * @param multiSortMeta 多字段排序
	 * @param filters 过滤字段
	 * @return 实体对象列表
	 */
	public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String,Object> filters);
	
	/**
	 * 根据ID从实体对象列表获取实体对象
	 * 
	 * @param id 实体对象的ID
	 * @param lt 实体对象列表
	 * @return 对应的实体对象
	 */
	public T getById(K id, List<T> lt);
	
	/**
	 * 根据实体对象获取ID
	 * 
	 * @param t 实体对象
	 * @return 实体对象的ID
	 */
	public K getId(T t);
}
