/**
 * 
 */
package com.cccollector.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.app.dao.ResourceDao;
import com.cccollector.app.model.Release;
import com.cccollector.app.model.Resource;
import com.cccollector.app.service.ResourceService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 资源服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("resourceService")
public class ResourceServiceImpl extends GenericServiceHibernateImpl<Integer, Resource> implements ResourceService {
	
	@Autowired
	private ResourceDao resourceDao;

	@Override
	public void addResource(Resource resource) {
		// 设置排序位置
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("release", new QueryField("releaseId", resource.getRelease().getReleaseId())));
		predicateQueryFieldList.add(new QueryField("platformId", resource.getPlatformId()));
		predicateQueryFieldList.add(new QueryField("type", resource.getType()));
		Integer position = resourceDao.max("position", predicateQueryFieldList);
		resource.setPosition(position == null ? 0 : position.intValue() + 1);		
		
		// 添加资源
		resourceDao.save(resource);
	}

	@Override
	public void copyResources(int releaseId, int releaseIdToCopy) {
		// 查询将要复制的资源
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("release", new QueryField("releaseId", releaseIdToCopy)));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("platformId", true));			
		orderQueryFieldList.add(new QueryField("type", true));
		orderQueryFieldList.add(new QueryField("position", true));
		List<Resource> resourcesToCopy = resourceDao.loadAll(predicateQueryFieldList, orderQueryFieldList);
		
		// 复制资源
		Release release = new Release();
		release.setReleaseId(releaseId);
		for (Resource resourceToCopy : resourcesToCopy) {
			// 复制数据
			Resource resource = new Resource();
			resource.setPlatformId(resourceToCopy.getPlatformId());
			resource.setType(resourceToCopy.getType());
			resource.setAddress(resourceToCopy.getAddress());
			resource.setRelease(release);
			
			// 设置排序位置
			predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("release", new QueryField("releaseId", resource.getRelease().getReleaseId())));
			predicateQueryFieldList.add(new QueryField("platformId", resource.getPlatformId()));
			predicateQueryFieldList.add(new QueryField("type", resource.getType()));
			Integer position = resourceDao.max("position", predicateQueryFieldList);
			resource.setPosition(position == null ? 0 : position.intValue() + 1);		
			
			// 添加资源
			resourceDao.save(resource);
		}
	}

	@Override
	public void moveResources(List<Resource> resources) {
		Map<Integer, Map<Integer, Integer>> positionMap = new HashMap<>();
		for (Resource resource : resources) {
			Map<Integer, Integer> platformPositionMap = positionMap.get(resource.getPlatformId());
			if (platformPositionMap == null) {
				platformPositionMap = new HashMap<Integer, Integer>();
				platformPositionMap.put(Resource.TypeEnum.接口.ordinal(), 0);
				platformPositionMap.put(Resource.TypeEnum.路径.ordinal(), 0);
				positionMap.put(resource.getPlatformId(), platformPositionMap);
			}
			int position = platformPositionMap.get(resource.getType());
			resource.setPosition(position);
			resourceDao.update(resource, "position");
			platformPositionMap.put(resource.getType(), position + 1);
		}
	}
}
