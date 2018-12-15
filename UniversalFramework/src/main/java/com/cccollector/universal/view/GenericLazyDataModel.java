/**
 * 
 */
package com.cccollector.universal.view;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 * 泛型延迟加载数据模型类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@SuppressWarnings("serial")
public class GenericLazyDataModel<T> extends LazyDataModel<T> {
	
	/**
	 * 模型ID属性名称
	 */
	private String modelIdAttributeName;
	
	/**
	 * 泛型列表Bean处理器
	 */
	private GenericListBeanHandler<T> genericListBeanHandler;

	public GenericLazyDataModel(String _modelIdAttributeName, GenericListBeanHandler<T> _genericListBeanHandler) {
		modelIdAttributeName = _modelIdAttributeName;
		genericListBeanHandler = _genericListBeanHandler;
	}

	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		StringBuffer totalRowCount = new StringBuffer();
		List<T> lt = genericListBeanHandler.loadPageModelList(first, pageSize, sortField, sortOrder, filters, totalRowCount);
		setRowCount(totalRowCount.length() > 0 ? Integer.parseInt(totalRowCount.toString()) : 0);
		return lt;
    }
    
	@Override
    public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String,Object> filters) {
		StringBuffer totalRowCount = new StringBuffer();
		List<T> lt = genericListBeanHandler.loadMultiSortPageModelList(first, pageSize, multiSortMeta, filters, totalRowCount);
		setRowCount(totalRowCount.length() > 0 ? Integer.parseInt(totalRowCount.toString()) : 0);
		return lt;
    }

	@Override
	public T getRowData(String rowKey) {
		Integer id = Integer.valueOf(rowKey);
		List<T> lt = (List<T>) getWrappedData();
		for (T t : lt) {
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(modelIdAttributeName, t.getClass());
				Object value = propertyDescriptor.getReadMethod().invoke(t);
				if (id.equals(value)) {
					return t;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
    }

	@Override
	public Object getRowKey(T t) {
		try {
			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(modelIdAttributeName, t.getClass());
			return propertyDescriptor.getReadMethod().invoke(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
}
