/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.app.model.Platform;
import com.cccollector.app.model.Release;
import com.cccollector.app.model.Resource;
import com.cccollector.universal.dao.QueryField;

/**
 * 资源Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ResourceBean extends BaseEditBean<Resource> implements Serializable {

	private static final long serialVersionUID = 2991830342991151876L;

	public ResourceBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Resource>() {

			@Override
			public Resource loadModel(Integer modelId) {
				Resource resource = null;
				if (modelId == null) {
					resource = new Resource();
					resource.setType(Resource.TypeEnum.接口.ordinal());
					resource.setRelease(getRelease());
				} else {
					resource = resourceService.loadById(modelId);
					
					// 设置对应平台
					Platform platform = platformService.loadById(resource.getPlatformId());
					resource.setPlatform(platform);
				}
				return resource;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return releaseService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 资源
	 */
	public Resource getResource() {
		return getModel();
	}

	/**
	 * 所属发行
	 */
	public Release getRelease() {
		return (Release) relatedModel(1);
	}

	/**
	 * 获取当前应用关联的全部平台
	 */
	public List<Platform> getAllPlatforms() {
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("apps", new QueryField("appId", getRelease().getEdition().getApp().getAppId())));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("enabled", false));
		orderQueryFieldList.add(new QueryField("name", true));
		return platformService.loadAll(predicateQueryFieldList, orderQueryFieldList);
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		Resource resource = getResource();
		if (resource.getResourceId() == null) {
			// 添加资源
			resourceService.addResource(resource);
			addInfoMessageToFlash("添加资源成功！");
		} else {
			// 编辑资源
			resourceService.update(resource);
			addInfoMessageToFlash("编辑资源成功！");
		}
		
		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("resourceList.xhtml?multiSelect=true");
		}
	}
}
