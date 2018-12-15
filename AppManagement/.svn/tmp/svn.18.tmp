/**
 * 
 */
package com.cccollector.app.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.app.api.ReleaseApi;
import com.cccollector.app.dao.BinaryDao;
import com.cccollector.app.dao.ReleaseDao;
import com.cccollector.app.model.Binary;
import com.cccollector.app.model.Edition;
import com.cccollector.app.model.Release;
import com.cccollector.app.model.Resource;
import com.cccollector.universal.cxf.ApiException;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.model.UniversalJsonViews;
import com.cccollector.universal.service.GenericServiceHibernateImpl;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 发行API实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("releaseApi")
public class ReleaseApiImpl extends GenericServiceHibernateImpl<Integer, Release> implements ReleaseApi {
	
	@Autowired
	private ReleaseDao releaseDao;
	
	@Autowired
	private BinaryDao binaryDao;

	/**
	 * 验证必填参数
	 */
	private void verifyRequired(String... params) throws ApiException {
		for (String param : params) {
			if (param == null || param.isEmpty() || param.trim().isEmpty()) {
				throw new ApiException(Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build());
			}
		}
	}	
	
	@Override
	@JsonView(UniversalJsonViews.FrontVersion1.class)
	public Response findReleaseByEditionBundleId(String editionBundleId, String version) {
		try {
			// 验证必填参数
			verifyRequired(editionBundleId, version);
			
			// 根据版本标识符和发行版本号查询发行对象
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("edition", new QueryField("bundleId", editionBundleId)));
			predicateQueryFieldList.add(new QueryField("version", version));
			predicateQueryFieldList.add(new QueryField("status", Release.StatusEnum.已发布.ordinal()));
			List<Release> releases = releaseDao.loadAll(predicateQueryFieldList, null);
			// 如果没有查询到
			if (releases == null || releases.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.发行不存在.toJson()).build();
			}
			Release release = releases.get(0);
			// 如果发行对象或其上级对象不可用
			if (!release.getEdition().getApp().getEnabled() || !release.getEdition().getEnabled()) {
				return Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.发行不可用.toJson()).build();
			}
			
			// 获取平台
			release.getEdition().getApp().getPlatforms().iterator();
			
			// 获取平台资源
			for (Resource resource : release.getResources()) {
				// 获取类别资源
				Map<Integer, List<Resource>> typeResources = release.getPlatformResources().get(resource.getPlatformId());
				if (typeResources == null) {
					typeResources = new HashMap<Integer, List<Resource>>();
					release.getPlatformResources().put(resource.getPlatformId(), typeResources);
				}
				// 获取资源列表
				List<Resource> resources = typeResources.get(resource.getType());
				if (resources == null) {
					resources = new ArrayList<Resource>();
					typeResources.put(resource.getType(), resources);
				}
				// 存入资源
				resources.add(resource);
			}

			releaseDao.getEntityManager().detach(release.getEdition());

			return Response.ok().entity(release).build();
		} catch (ApiException e) {
			return e.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Deprecated
	@Override
	@JsonView(UniversalJsonViews.FrontVersion2.class)
	public Response findReleaseByEditionBundleId2(String editionBundleId, String version) {
		try {
			// 验证必填参数
			verifyRequired(editionBundleId, version);
			
			// 根据版本标识符和发行版本号查询发行对象
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("edition", new QueryField("bundleId", editionBundleId)));
			predicateQueryFieldList.add(new QueryField("version", version));
			predicateQueryFieldList.add(new QueryField("status", Release.StatusEnum.已发布.ordinal()));
			List<Release> releases = releaseDao.loadAll(predicateQueryFieldList, null);
			// 如果没有查询到
			if (releases == null || releases.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.发行不存在.toJson()).build();
			}
			Release release = releases.get(0);
			// 如果发行对象或其上级对象不可用
			if (!release.getEdition().getApp().getEnabled() || !release.getEdition().getEnabled()) {
				return Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.发行不可用.toJson()).build();
			}
			
			// 获取平台
			release.getEdition().getApp().getPlatforms().iterator();
			
			// 获取平台资源
			for (Resource resource : release.getResources()) {
				// 获取类别资源
				Map<Integer, List<Resource>> typeResources = release.getPlatformResources().get(resource.getPlatformId());
				if (typeResources == null) {
					typeResources = new HashMap<Integer, List<Resource>>();
					release.getPlatformResources().put(resource.getPlatformId(), typeResources);
				}
				// 获取资源列表
				List<Resource> resources = typeResources.get(resource.getType());
				if (resources == null) {
					resources = new ArrayList<Resource>();
					typeResources.put(resource.getType(), resources);
				}
				// 存入资源
				resources.add(resource);
			}

			// 获取推广方案
			String programsiOS = "[\n" + 
					"      {\n" + 
					"        \"promotions\": [\n" + 
					"          {\n" + 
					"            \"title\": \"紫禁城\",\n" + 
					"            \"visible\": false,\n" + 
					"            \"openAddress\": \"zijincheng://\",\n" + 
					"            \"intro\": null,\n" + 
					"            \"relativeBundleId\": \"com.cccollector.magazine.zijincheng\",\n" + 
					"            \"iconUpdateTime\": null,\n" + 
					"            \"downloadAddress\": \"1033908095\",\n" + 
					"            \"promotionId\": 1\n" + 
					"          },\n" + 
					"          {\n" + 
					"            \"title\": \"收藏家\",\n" + 
					"            \"visible\": false,\n" + 
					"            \"openAddress\": \"shoucangjia://\",\n" + 
					"            \"intro\": null,\n" + 
					"            \"relativeBundleId\": \"com.cccollector.magazine.shoucangjia\",\n" + 
					"            \"iconUpdateTime\": null,\n" + 
					"            \"downloadAddress\": \"1239309398\",\n" + 
					"            \"promotionId\": 2\n" + 
					"          },\n" + 
					"          {\n" + 
					"            \"title\": \"中国收藏\",\n" + 
					"            \"visible\": false,\n" + 
					"            \"openAddress\": \"zhongguoshoucang://\",\n" + 
					"            \"intro\": null,\n" + 
					"            \"relativeBundleId\": \"com.cccollector.magazine.zhongguoshoucang\",\n" + 
					"            \"iconUpdateTime\": null,\n" + 
					"            \"downloadAddress\": \"1238557068\",\n" + 
					"            \"promotionId\": 3\n" + 
					"          },\n" + 
					"          {\n" + 
					"            \"title\": \"收藏投资导刊\",\n" + 
					"            \"visible\": false,\n" + 
					"            \"openAddress\": \"shoucangtouzidaokan://\",\n" + 
					"            \"intro\": null,\n" + 
					"            \"relativeBundleId\": \"com.cccollector.magazine.shoucangtouzidaokan\",\n" + 
					"            \"iconUpdateTime\": null,\n" + 
					"            \"downloadAddress\": \"1238039326\",\n" + 
					"            \"promotionId\": 4\n" + 
					"          }\n" + 
					"        ],\n" + 
					"        \"programId\": 1,\n" + 
					"        \"name\": \"文藏阅读iOS\"\n" + 
					"      }\n" + 
					"    ]";
			String programsAndroid = "[\n" + 
					"      {\n" + 
					"        \"promotions\": [\n" + 
					"          {\n" + 
					"            \"title\": \"紫禁城\",\n" + 
					"            \"visible\": false,\n" + 
					"            \"openAddress\": \"com.cccollector.magazine:zijincheng\",\n" + 
					"            \"intro\": null,\n" + 
					"            \"relativeBundleId\": \"com.cccollector.magazine.zijincheng\",\n" + 
					"            \"iconUpdateTime\": null,\n" + 
					"            \"downloadAddress\": \"http://a.app.qq.com/o/simple.jsp?pkgname=com.cccollector.magazine.zijincheng.android\",\n" + 
					"            \"promotionId\": 5\n" + 
					"          },\n" + 
					"          {\n" + 
					"            \"title\": \"收藏家\",\n" + 
					"            \"visible\": false,\n" + 
					"            \"openAddress\": \"com.cccollector.magazine:shoucangjia\",\n" + 
					"            \"intro\": null,\n" + 
					"            \"relativeBundleId\": \"com.cccollector.magazine.shoucangjia\",\n" + 
					"            \"iconUpdateTime\": null,\n" + 
					"            \"downloadAddress\": \"http://a.app.qq.com/o/simple.jsp?pkgname=com.cccollector.magazine.shoucangjia.android\",\n" + 
					"            \"promotionId\": 7\n" + 
					"          },\n" + 
					"          {\n" + 
					"            \"title\": \"中国收藏\",\n" + 
					"            \"visible\": false,\n" + 
					"            \"openAddress\": \"com.cccollector.magazine:zhongguoshoucang\",\n" + 
					"            \"intro\": null,\n" + 
					"            \"relativeBundleId\": \"com.cccollector.magazine.zhongguoshoucang\",\n" + 
					"            \"iconUpdateTime\": null,\n" + 
					"            \"downloadAddress\": \"http://a.app.qq.com/o/simple.jsp?pkgname=com.cccollector.magazine.zhongguoshoucang.android\",\n" + 
					"            \"promotionId\": 6\n" + 
					"          },\n" + 
					"          {\n" + 
					"            \"title\": \"收藏投资导刊\",\n" + 
					"            \"visible\": false,\n" + 
					"            \"openAddress\": \"com.cccollector.magazine:shoucangtouzidaokan\",\n" + 
					"            \"intro\": null,\n" + 
					"            \"relativeBundleId\": \"com.cccollector.magazine.shoucangtouzidaokan\",\n" + 
					"            \"iconUpdateTime\": null,\n" + 
					"            \"downloadAddress\": \"http://a.app.qq.com/o/simple.jsp?pkgname=com.cccollector.magazine.shoucangtouzidaokan.android\",\n" + 
					"            \"promotionId\": 8\n" + 
					"          }\n" + 
					"        ],\n" + 
					"        \"programId\": 2,\n" + 
					"        \"name\": \"文藏阅读Android\"\n" + 
					"      }\n" + 
					"    ]";
			String programs = release.getEdition().getOsTypeEnum() == Edition.OsTypeEnum.苹果 ? programsiOS :programsAndroid;
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("rawtypes")
			List programList = objectMapper.readValue(programs, ArrayList.class);
			release.getEdition().setPrograms(programList);

			return Response.ok().entity(release).build();
		} catch (ApiException e) {
			return e.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}	

	@Override
	@JsonView(UniversalJsonViews.Front.class)
	public Response findUnpublishedReleaseByEditionBundleId(String editionBundleId, String version) {
		try {
			// 验证必填参数
			verifyRequired(editionBundleId, version);
			
			// 根据版本标识符和发行版本号查询发行对象
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("edition", new QueryField("bundleId", editionBundleId)));
			predicateQueryFieldList.add(new QueryField("version", version));
			List<Release> releases = releaseDao.loadAll(predicateQueryFieldList, null);
			// 如果没有查询到
			if (releases == null || releases.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.发行不存在.toJson()).build();
			}
			Release release = releases.get(0);
			// 如果发行对象或其上级对象不可用
			if (!release.getEdition().getApp().getEnabled() || !release.getEdition().getEnabled() || release.getStatusEnum() == Release.StatusEnum.已废弃) {
				return Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.发行不可用.toJson()).build();
			}
			
			// 获取平台
			release.getEdition().getApp().getPlatforms().iterator();
			
			// 获取平台资源
			for (Resource resource : release.getResources()) {
				// 获取类别资源
				Map<Integer, List<Resource>> typeResources = release.getPlatformResources().get(resource.getPlatformId());
				if (typeResources == null) {
					typeResources = new HashMap<Integer, List<Resource>>();
					release.getPlatformResources().put(resource.getPlatformId(), typeResources);
				}
				// 获取资源列表
				List<Resource> resources = typeResources.get(resource.getType());
				if (resources == null) {
					resources = new ArrayList<Resource>();
					typeResources.put(resource.getType(), resources);
				}
				// 存入资源
				resources.add(resource);
			}
			
			return Response.ok().entity(release).build();
		} catch (ApiException e) {
			return e.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}
	
	@Override
	@JsonView(UniversalJsonViews.FrontVersion1.class)
	public Response getReleaseLastBinary(int releaseId) {
		// 获取发行
		Release release = releaseDao.loadById(releaseId);
		// 如果没有查询到
		if (release == null || !release.getEdition().getApp().getEnabled() || !release.getEdition().getEnabled() || release.getStatusEnum() != Release.StatusEnum.已发布) {
			return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.发行不存在.toJson()).build();
		}
		
		// 查询最新的二进制文件
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("release", new QueryField("edition", new QueryField("editionId", release.getEdition().getEditionId()))));
		predicateQueryFieldList.add(new QueryField("release", new QueryField("status", Release.StatusEnum.已发布.ordinal())));
		predicateQueryFieldList.add(new QueryField("status", Binary.StatusEnum.已发布.ordinal()));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("release", new QueryField("releaseDate", false)));
		List<Binary> binaries = binaryDao.loadAll(predicateQueryFieldList, orderQueryFieldList);
		Binary binaryLast = binaries.isEmpty() ? null : binaries.get(0);
		// 如果最新的二进制文件不存在
		if (binaryLast == null || !release.getReleaseDate().before(binaryLast.getRelease().getReleaseDate())) {
			return Response.status(Response.Status.OK).build();
		}
		
		// 获取平台
		binaryLast.getRelease().getEdition().getApp().getPlatforms().iterator();
		
		// 获取平台资源
		for (Resource resource : binaryLast.getRelease().getResources()) {
			// 获取类别资源
			Map<Integer, List<Resource>> typeResources = binaryLast.getRelease().getPlatformResources().get(resource.getPlatformId());
			if (typeResources == null) {
				typeResources = new HashMap<Integer, List<Resource>>();
				binaryLast.getRelease().getPlatformResources().put(resource.getPlatformId(), typeResources);
			}
			// 获取资源列表
			List<Resource> resources = typeResources.get(resource.getType());
			if (resources == null) {
				resources = new ArrayList<Resource>();
				typeResources.put(resource.getType(), resources);
			}
			// 存入资源
			resources.add(resource);
		}

		releaseDao.getEntityManager().detach(binaryLast.getRelease().getEdition());
		
		return Response.status(Response.Status.OK).entity(binaryLast).build();
	}
	
	/**
	 * 错误消息枚举
	 */	
	public enum ErrorMessage {
		成功(0, "成功"),
		缺少必填项(1, "缺少必填项"),
		发行不存在(2, "发行不存在"),
		发行不可用(3, "发行不可用"),
		格式错误(101, "格式错误"),
		数据异常(102, "数据异常"),
		数据唯一限制(103, "数据唯一限制"),
		操作受限(104, "操作受限"),
		操作超时(105, "操作超时"),
		操作失败(106, "操作失败");
		
		private ErrorMessage(int code, String message) {
			_code = code;
			_message = message;
		}
	
		/**
		 * 代码
		 */
		private int _code;

		public int getCode() {
			return _code;
		}
		
		/**
		 * 消息
		 */
		private String _message;
	
		public String getMessage() {
			return _message;
		}
	
		/**
		 * 转换为JSON
		 */
		public String toJson() {
			return "{\"code\" : \"" + _code + "\", \"message\" : \"" + _message + "\"}";
		}
	}
}
