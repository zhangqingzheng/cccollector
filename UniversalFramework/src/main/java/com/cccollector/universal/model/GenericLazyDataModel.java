/**
 * 
 */
package com.cccollector.universal.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 * 泛型延迟加载数据模型类
 *
 * @author 谢朋
 * Copyright © 2016年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@SuppressWarnings("serial")
public class GenericLazyDataModel<K extends Serializable, T extends Serializable> extends LazyDataModel<T> {
	
	/**
	 * 泛型延迟加载数据处理
	 */
	private GenericLazyDataHandler<K, T> _genericLazyDataHandler;

	public GenericLazyDataModel(GenericLazyDataHandler<K, T> genericLazyDataHandler) {
		_genericLazyDataHandler = genericLazyDataHandler;
	}

	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		if (_genericLazyDataHandler != null) {
			return _genericLazyDataHandler.load(first, pageSize, sortField, sortOrder, filters);
		}
		return null;
    }
    
	@Override
    public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String,Object> filters) {
		if (_genericLazyDataHandler != null) {
			return _genericLazyDataHandler.load(first, pageSize, multiSortMeta, filters);
		}
		return null;
    }

	@Override
	public T getRowData(String rowKey) {
		if (_genericLazyDataHandler != null) {
			@SuppressWarnings("unchecked")
			K id = (K) Integer.valueOf(rowKey);
			@SuppressWarnings("unchecked")
			List<T> lt = (List<T>) getWrappedData();
			return _genericLazyDataHandler.getById(id, lt);
		}
		return null;
    }

	@Override
	public Object getRowKey(T t) {
		if (_genericLazyDataHandler != null) {
			return _genericLazyDataHandler.getId(t);
		}
		return null;
    }
}
