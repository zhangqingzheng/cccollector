/**
 * 
 */
package com.cccollector.universal.dao;

/**
 * 可路由数据源的路由器
 *
 * @author 谢朋
 * Copyright © 2016年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class RoutingDataSourceRouter {
	
	/**
	 * 数据源查找键
	 */
	private static final ThreadLocal<String> dataSourceLookupKeys = new ThreadLocal<String>();
	
	/**
	 * 获取当前线程数据源查找键
	 * 
	 * @return 数据源查找键
	 */
   public static String getDataSourceLookupKey() {
        return (String) dataSourceLookupKeys.get();
    }
	
	/**
	 * 设置当前线程数据源查找键
	 * 
	 * @param dataSourceLookupKey 数据源查找键
	 */
	public static void setDataSourceLookupKey(String dataSourceLookupKey) {
		dataSourceLookupKeys.set(dataSourceLookupKey);
    }
    
	/**
	 * 移除当前线程数据源查找键
	 */
    public static void removeDataSourceLookupKey() {
    	dataSourceLookupKeys.remove();
    }
}
