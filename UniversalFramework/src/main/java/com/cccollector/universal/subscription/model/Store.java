/**
 * 
 */
package com.cccollector.universal.subscription.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商店类
 *
 * @author 杨彪
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
*/
public class Store implements Serializable {

	private static final long serialVersionUID = 1501633368889465294L;

	private Integer _storeId;

	/**
	 * 商店ID
	 */
	public Integer getStoreId() {
		return _storeId;
	}

	public void setStoreId(Integer storeId) {
		_storeId = storeId;
	}

	private String _name;

	/**
	 * 名称
	 */
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	private String _storeIdentifier;

	/**
	 * 商店标识符
	 */
	public String getStoreIdentifier() {
		return _storeIdentifier;
	}

	public void setStoreIdentifier(String storeIdentifier) {
		_storeIdentifier = storeIdentifier;
	}

	private List<Product> _products = new ArrayList<Product>();

	/**
	 * 包含的商品
	 */
	public List<Product> getProducts() {
		return _products;
	}
	
	public void setProducts(List<Product> products) {
		_products = products;
	}
}