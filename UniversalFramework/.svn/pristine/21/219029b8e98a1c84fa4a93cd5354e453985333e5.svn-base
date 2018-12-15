/**
 * 
 */
package com.cccollector.universal.dao;

import java.lang.reflect.Field;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 可路由数据源
 *
 * @author 谢朋
 * Copyright © 2016年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class RoutingDataSource extends AbstractRoutingDataSource implements ApplicationContextAware {
	
	/**
	 * 应用上下文
	 */
	private ApplicationContext _applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		_applicationContext = applicationContext;
	}

	@Override
	protected Object determineCurrentLookupKey() {
		// 获取当前线程数据源查找键
		String dataSourceLookupKey = RoutingDataSourceRouter.getDataSourceLookupKey();
		return dataSourceLookupKey;
	}
	
	/**
	 * 根据数据源ID、JDBC链接获取SQLite数据源查找键
	 * 
	 * @param id 数据源ID
	 * @param jdbcUrl JDBC链接
	 * @return SQLite数据源查找键
	 */
	public String getSqliteDataSourceLookupKey(Integer id, String jdbcUrl) {
		String dataSourceBeanName = "dataSource." + id;
		// 如果数据源Bean未在应用上下文注册，则注册数据源Bean
		if (!_applicationContext.containsBean(dataSourceBeanName)) {
			// SQLite数据源Bean定义
			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ComboPooledDataSource.class);
			beanDefinitionBuilder.setDestroyMethodName("close")
			.addPropertyValue("driverClass", "org.sqlite.JDBC")
			.addPropertyValue("jdbcUrl", jdbcUrl)
			.addPropertyValue("minPoolSize", "5")
			.addPropertyValue("maxPoolSize", "30")
			.addPropertyValue("initialPoolSize", "10");

			// 注册数据源Bean
	        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) _applicationContext;
			DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
			defaultListableBeanFactory.registerBeanDefinition(dataSourceBeanName, beanDefinitionBuilder.getBeanDefinition());
		}
		
		// 将数据源添加至resolvedDataSources
		try {
			Field resolvedDataSourcesField = AbstractRoutingDataSource.class.getDeclaredField("resolvedDataSources");
			resolvedDataSourcesField.setAccessible(true);
			@SuppressWarnings("unchecked")
			Map<Object, DataSource> resolvedDataSources = (Map<Object, DataSource>) resolvedDataSourcesField.get(this);
			if (resolvedDataSources.get(dataSourceBeanName) == null) {
				DataSource dataSource = (DataSource) _applicationContext.getBean(dataSourceBeanName);
				resolvedDataSources.put(dataSourceBeanName, dataSource);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dataSourceBeanName;
	}
}
