/**
 * 
 */
package com.cccollector.app.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import com.cccollector.app.model.Platform;
import com.cccollector.universal.service.GenericService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 平台服务接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Path("/platforms")
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "平台服务")
public interface PlatformService extends GenericService<Integer, Platform> {

	/**
	 * 获取数据目录
	 */
	public String getDataPath();

	/**
	 * 获取平台模版目录
	 */
	public String getPlatformTemplatesPath();
    
	/**
	 * 上传平台模版及资源
	 * 
	 * @param bundleId 平台标识符
	 * @param template 模版
	 * @param images 图片文件
	 * @return 是否成功
	 */
    @Path("/uploadTemplateAndResources")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation(value = "上传平台模版及资源")
    public Response uploadTemplateAndResources(@ApiParam(value = "平台标识符", required = true) @Multipart(value = "bundleId") String bundleId, @ApiParam(value = "模版", required = true) @Multipart(value = "template") String template);

	/**
	 * 向平台同步证书
	 * 
	 * @param platform 平台
	 * @return 正确信息
	 */
    public boolean syncCertificatesToPlatform(Platform platform);
	
	/**
	 * 添加平台到应用
	 * 
	 * @param platformId 平台ID
	 * @param appId 应用ID
	 */
	public void addPlatformToApp(int platformId, int appId);
	
	/**
	 * 从应用删除平台
	 * 
	 * @param platformId 平台ID
	 * @param appId 应用ID
	 */
	public void deletePlatformFromApp(int platformId, int appId);
}
