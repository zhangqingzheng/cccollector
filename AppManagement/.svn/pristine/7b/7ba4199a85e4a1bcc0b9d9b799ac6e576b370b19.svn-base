/**
 * 
 */
package com.cccollector.app.service;

import java.util.List;

import com.cccollector.app.model.Resource;
import com.cccollector.universal.service.GenericService;

/**
 * 资源服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public interface ResourceService extends GenericService<Integer, Resource> {
	
	/**
	 * 添加资源
	 * 
	 * @param resource 待添加的资源
	 */
	public void addResource(Resource resource);
	
	/**
	 * 复制资源
	 * 
	 * @param releaseId 当前的发行ID
	 * @param releaseIdToCopy 待复制的发行ID
	 */
	public void copyResources(int releaseId, int releaseIdToCopy);
	
	/**
	 * 移动资源
	 * 
	 * @param resources 移动后的资源
	 */
	public void moveResources(List<Resource> resources);
}
