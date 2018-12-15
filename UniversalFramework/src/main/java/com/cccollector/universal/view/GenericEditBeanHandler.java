/**
 * 
 */
package com.cccollector.universal.view;

/**
 * 泛型编辑Bean处理器接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface GenericEditBeanHandler<T> extends BaseBeanHandler {
	
	/**
	 * 加载模型
	 * 
	 * @param modelId 模型ID
	 * @return 加载后的模型
	 */
	public T loadModel(Integer modelId);
}
