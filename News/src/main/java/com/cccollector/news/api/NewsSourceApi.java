/**
 * 
 */
package com.cccollector.news.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cccollector.news.model.ColumnSubstitute;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.RecommendGroup;
import com.cccollector.universal.service.GenericService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 新闻源API接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Path("/newsSources")
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "新闻源API")
public interface NewsSourceApi extends GenericService<Integer, NewsSource> {
	
	/**
     * 根据应用标识符查询应用关联的新闻源2
     * 
     * @param appBundleId 应用标识符
     * @return 新闻源列表
     */
    @Path("/findByAppBundleId2")
    @GET
    @ApiOperation(value = "根据应用标识符查询应用关联的新闻源2", response = NewsSource.class, responseContainer = "List")
    public Response findNewsSourcesByAppBundleId2(@ApiParam(value = "应用标识符", required = true) @QueryParam("appBundleId") String appBundleId);
    
	/**
	 * 根据应用标识符查询应用关联的新闻源
	 * 
	 * @param appBundleId 应用标识符
	 * @return 新闻源列表
	 */
    @Path("/findByAppBundleId")
    @GET
    @ApiOperation(value = "根据应用标识符查询应用关联的新闻源", response = NewsSource.class, responseContainer = "List")
    public Response findNewsSourcesByAppBundleId(@ApiParam(value = "应用标识符", required = true) @QueryParam("appBundleId") String appBundleId);
    
	/**
	 * 根据新闻源ID、栏目ID获取栏目分发列表
	 * 
	 * @param newsSourceId 新闻源ID
	 * @param columnId 栏目ID
	 * @param page 页码
	 * @return 分发列表
	 */
    @Path("/{newsSourceId}/columns/{columnId}/distributions")
    @GET
    @ApiOperation(value = "根据新闻源ID、栏目ID获取栏目分发列表", response = Distribution.class, responseContainer = "List")
    public Response getDistributionsByNewsSourceIdAndColumnId(@ApiParam(value = "新闻源ID", required = true) @PathParam("newsSourceId") int newsSourceId, @ApiParam(value = "栏目ID", required = true) @PathParam("columnId") int columnId, @ApiParam(value = "页码", required = true) @QueryParam("page") int page);

	/**
	 *  根据新闻源ID、栏目ID获取栏目推荐组列表
	 * 
	 * @param newsSourceId 新闻源ID
	 * @param columnId 栏目ID
	 * @return 推荐组列表
	 */
    @Path("/{newsSourceId}/columns/{columnId}/recommendGroups")
    @GET
    @ApiOperation(value = "根据新闻源ID、栏目ID获取栏目推荐组列表", response = RecommendGroup.class, responseContainer = "List")
    public Response getRecommendGroupsByNewsSourceIdAndColumnId(@ApiParam(value = "新闻源ID", required = true) @PathParam("newsSourceId") int newsSourceId, @ApiParam(value = "栏目ID", required = true) @PathParam("columnId") int columnId);
    
    /**
	 * 根据应用标识符查询应用栏目替身列表
	 * 
	 * @param appBundleId 应用标识符
	 * @return 栏目替身列表
	 */
    @Path("/findColumnSubstitutesByAppBundleId")
    @GET
    @ApiOperation(value = "根据应用标识符查询应用栏目替身列表", response = ColumnSubstitute.class, responseContainer = "List")
    public Response findColumnSubstitutesByAppBundleId(@ApiParam(value = "应用标识符", required = true) @QueryParam("appBundleId") String appBundleId);
}
