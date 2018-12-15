/**
 * 
 */
package com.cccollector.app.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.ReorderEvent;

import com.cccollector.app.model.Platform;
import com.cccollector.app.model.Release;
import com.cccollector.app.model.Resource;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.view.GenericListBean;

/**
 * 资源列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ResourceListBean extends BaseListBean<Resource> implements Serializable {

	private static final long serialVersionUID = 2714610331381553757L;

	public ResourceListBean() {
		modelDisplayName = "资源";
		modelAttributeName = "resource";
		modelIdAttributeName = "resourceId";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Resource>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return releaseService.loadById(relatedModelId);
				}
				return null;
			}

			@Override
			public List<Resource> loadAllModelList() {
				if (getRelease() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("release", new QueryField("releaseId", getRelease().getReleaseId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("platformId", true));			
				orderQueryFieldList.add(new QueryField("type", true));
				orderQueryFieldList.add(new QueryField("position", true));
				List<Resource> resources = resourceService.loadAll(predicateQueryFieldList, orderQueryFieldList);
				
				// 设置对应平台
				for (Resource resource : resources) {
					Platform platform = platformService.loadById(resource.getPlatformId());
					resource.setPlatform(platform);
				}
				return resources;
			}
		};		
	}

	/**
	 * 所属发行
	 */
	public Release getRelease() {
		return (Release) relatedModel(1);
	}

	/**
	 * 全部发行
	 */
	private List<Release> allReleases;

	public List<Release> getAllReleases() {
		if (allReleases == null) {
			// 查询同一应用下是否还有其他已发布发行
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("edition", new QueryField("app", new QueryField("appId", getRelease().getEdition().getApp().getAppId()))));
			predicateQueryFieldList.add(new QueryField("releaseId", getRelease().getReleaseId(), PredicateEnum.NOT_EQUAL));
			predicateQueryFieldList.add(new QueryField("status", Release.StatusEnum.已发布.ordinal()));
			Long releaseCount = releaseService.count(predicateQueryFieldList);
			
			if (releaseCount > 0) {
				// 如果同一应用下还有其他已发布发行，则返回同一应用下的其他已发布发行
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("edition", new QueryField("osType", true)));
				orderQueryFieldList.add(new QueryField("edition", new QueryField("os", true)));
				orderQueryFieldList.add(new QueryField("releaseDate", false));
				allReleases = releaseService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			} else {
				// 如果同一应用下没有其他已发布发行，则返回其他应用的全部已发布发行
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("edition", new QueryField("app", new QueryField("appId", getRelease().getEdition().getApp().getAppId(), PredicateEnum.NOT_EQUAL))));
				predicateQueryFieldList.add(new QueryField("status", Release.StatusEnum.已发布.ordinal()));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("edition", new QueryField("app", new QueryField("name", true))));
				orderQueryFieldList.add(new QueryField("edition", new QueryField("osType", true)));
				orderQueryFieldList.add(new QueryField("edition", new QueryField("os", true)));
				orderQueryFieldList.add(new QueryField("releaseDate", false));
				allReleases = releaseService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			}
		}
		return allReleases;
	}

	/**
	 * 复制资源
	 */
	public void copyResourcesAction() {
		if (getSelectedModelId() == null) {
			addErrorMessage("请选择一个发行进行复制！");
			return;
		}
		
		// 复制资源
		resourceService.copyResources(getRelease().getReleaseId(), getSelectedModelId());
		addInfoMessage("复制资源成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 清空选中的模型ID
		setSelectedModelId(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 移动资源
	 */
	public void moveResourceAction(ReorderEvent event) {
		// 移动资源
		resourceService.moveResources(allModels);
		addInfoMessage("移动资源成功！");
		
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 删除资源
	 */
	public void deleteResourceAction(Resource resource) {
		// 删除资源
		resourceService.delete(resource);
		addInfoMessage("删除资源成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 批量删除资源
	 */
	public void deleteResourcesAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个资源进行删除！");
			return;
		}
		
		// 删除资源
		resourceService.deleteAll(getSelectedModels());				
		addInfoMessage("删除资源成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}
}
