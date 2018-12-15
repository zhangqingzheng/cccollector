/**
 * 
 */
package com.cccollector.news.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cccollector.news.model.Site;
import com.cccollector.universal.service.GenericService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 网站API接口
 *
 * @author 杨彪 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Path("/site")
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "网站API")
public interface SiteApi extends GenericService<Integer, Site> {

	/**
	 * 栏目的文章列表
	 *
	 * @param siteVersionId 站点版本ID
	 * @param columnId 栏目ID
	 * @param pageNo 页码
	 * @return 栏目下的文章列表
	 */
	@Path("{siteVersionId}/column/{columnId}/{pageNo}.json")
	@GET
	@ApiOperation(value = "回源栏目列表", response = String.class)
	public Response columnDistributions(@ApiParam(value = "站点版本ID", required = true) @PathParam("siteVersionId") int siteVersionId, @ApiParam(value = "栏目ID", required = true) @PathParam("columnId") int columnId, @ApiParam(value = "页码", required = true) @PathParam("pageNo") int pageNo);

	/**
	 * 分类标签的文章列表
	 *
	 * @param siteVersionId 站点版本ID
	 * @param categoryTagId 分类标签ID
	 * @param pageNo 页码
	 * @return 分类标签下的文章列表
	 */
	@Path("{siteVersionId}/categoryTag/{categoryTagId}/{pageNo}.json")
	@GET
	@ApiOperation(value = "回源分类标签列表", response = String.class)
	public Response categoryTagDistributions(@ApiParam(value = "站点版本ID", required = true) @PathParam("siteVersionId") int siteVersionId, @ApiParam(value = "分类标签ID", required = true) @PathParam("categoryTagId") int categoryTagId, @ApiParam(value = "页码", required = true) @PathParam("pageNo") int pageNo);

}
