/**
 * 
 */
package com.cccollector.news.api;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cccollector.news.model.Article;
import com.cccollector.news.model.Comment;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.Reply;
import com.cccollector.universal.service.GenericService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 文章API接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Path("/articles")
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "文章API")
public interface ArticleApi extends GenericService<Integer, Article> {

    /**
     * 根据文章ID获取文章静态数据
     * 
     * @param articleId 文章ID
     * @param newsSourceId 新闻源ID
     * @return 文章静态数据
     */
    @Path("/{articleId}/static")
    @GET
    @ApiOperation(value = "根据文章ID获取文章静态数据", response = Article.class)
    public Response getArticleStaticByArticleId(@ApiParam(value = "文章ID", required = true) @PathParam("articleId") int articleId, @ApiParam(value = "新闻源ID", required = true) @HeaderParam("newsSourceId") int newsSourceId);

    /**
     * 根据文章ID获取文章动态数据
     * 
     * @param articleId 文章ID
     * @param newsSourceId 新闻源ID
     * @param loggedUserAppId 已登录用户应用ID
     * @param loggedAccessToken 已登录访问令牌
     * @return 文章动态数据
     */
    @Path("/{articleId}/dynamic")
    @GET
    @ApiOperation(value = "根据文章ID获取文章动态数据", response = Article.class)
    public Response getArticleDynamicByArticleId(@ApiParam(value = "文章ID", required = true) @PathParam("articleId") int articleId, @ApiParam(value = "新闻源ID", required = true) @HeaderParam("newsSourceId") int newsSourceId, @ApiParam(value = "已登录用户应用ID", required = false) @HeaderParam("loggedUserAppId") int loggedUserAppId, @ApiParam(value = "已登录访问令牌", required = false) @HeaderParam("loggedAccessToken") String loggedAccessToken);

    /**
     * 根据文章ID获取文章相关分发列表
     * 
     * @param articleId 文章ID
     * @param newsSourceId 新闻源ID
     * @return 文章相关分发列表
     */
    @Path("/{articleId}/relativeDistributions")
    @GET
    @ApiOperation(value = "根据文章ID获取文章相关分发列表", response = Distribution.class, responseContainer = "List")
    public Response getArticleRelativeDistributionsByArticleId(@ApiParam(value = "文章ID", required = true) @PathParam("articleId") int articleId, @ApiParam(value = "新闻源ID", required = true) @HeaderParam("newsSourceId") int newsSourceId);
    
    /**
     * 根据文章ID获取文章首发分发
     * 
     * @param articleId 文章ID
     * @return 首发分发
     */
    @Path("/{articleId}/firstPublishedDistribution")
    @GET
    @ApiOperation(value = "根据文章ID获取文章首发分发", response = Distribution.class)
    public Response getArticleFirstPublishedDistributionByArticleId(@ApiParam(value = "文章ID", required = true) @PathParam("articleId") int articleId);

    /**
     * 根据文章ID获取文章评论列表
     * 
     * @param articleId 文章ID
     * @param newsSourceId 新闻源ID
     * @param page 页码
     * @param loggedUserAppId 已登录用户应用ID
     * @param loggedAccessToken 已登录访问令牌
     * @return 文章评论列表
     */
    @Path("/{articleId}/comments")
    @GET
    @ApiOperation(value = "根据文章ID获取文章评论列表", response = Comment.class, responseContainer = "List")
    public Response getArticleCommentsByArticleId(@ApiParam(value = "文章ID", required = true) @PathParam("articleId") int articleId, @ApiParam(value = "新闻源ID", required = true) @HeaderParam("newsSourceId") int newsSourceId, @ApiParam(value = "页码", required = true) @QueryParam("page") int page, @ApiParam(value = "已登录用户应用ID", required = false) @HeaderParam("loggedUserAppId") int loggedUserAppId, @ApiParam(value = "已登录访问令牌", required = false) @HeaderParam("loggedAccessToken") String loggedAccessToken);

    /**
     * 根据文章ID获取文章热门评论列表
	 * 
	 * @param articleId 文章ID
     * @param newsSourceId 新闻源ID
     * @param loggedUserAppId 已登录用户应用ID
     * @param loggedAccessToken 已登录访问令牌
	 * @return 文章热门评论列表
	 */
    @Path("/{articleId}/comments/top")
    @GET
    @ApiOperation(value = "根据文章ID获取文章热门评论列表", response = Comment.class, responseContainer = "List")
    public Response getArticleCommentsTopByArticleId(@ApiParam(value = "文章ID", required = true) @PathParam("articleId") int articleId, @ApiParam(value = "新闻源ID", required = true) @HeaderParam("newsSourceId") int newsSourceId, @ApiParam(value = "已登录用户应用ID", required = false) @HeaderParam("loggedUserAppId") int loggedUserAppId, @ApiParam(value = "已登录访问令牌", required = false) @HeaderParam("loggedAccessToken") String loggedAccessToken);
   
    /**
     * 根据文章ID、评论ID获取评论回复列表
	 * 
	 * @param articleId 文章ID
     * @param newsSourceId 新闻源ID
	 * @param commentId 评论ID
     * @param page 页码
	 * @return 评论回复列表
	 */
    @Path("/{articleId}/comments/{commentId}/replies")
    @GET
    @ApiOperation(value = "根据文章ID、评论ID获取评论回复列表", response = Reply.class, responseContainer = "List")
    public Response getArticleCommentRepliesByArticleIdAndCommentId(@ApiParam(value = "文章ID", required = true) @PathParam("articleId") int articleId, @ApiParam(value = "新闻源ID", required = true) @HeaderParam("newsSourceId") int newsSourceId, @ApiParam(value = "评论ID", required = true) @PathParam("commentId") int commentId, @ApiParam(value = "页码", required = true) @QueryParam("page") int page);

    /**
	 * 根据分发ID获取分发
	 * 
	 * @param distributionId 分发ID
	 * @param newsSourceId 新闻源ID
	 * @return 分发
	 */
    @Path("/distributions/{distributionId}")
    @GET
    @ApiOperation(value = "根据分发ID获取分发", response = Distribution.class)
    public Response getDistributionByDistributionId(@ApiParam(value = "分发ID", required = true) @PathParam("distributionId") int distributionId, @ApiParam(value = "新闻源ID", required = true) @HeaderParam("newsSourceId") int newsSourceId);

    /**
     * 根据分类标签ID查询分发列表
     * 
     * @param categoryTagId 分类标签ID
     * @param newsSourceId 新闻源ID
	 * @param page 页码
     * @return 分发列表
     */
    @Path("/distributions/findByCategoryTagId")
    @GET
    @ApiOperation(value = "根据分类标签ID查询分发列表", response = Distribution.class, responseContainer = "List")
    public Response findDistributionsByCategoryTagId(@ApiParam(value = "分类标签ID", required = true) @QueryParam("categoryTagId") int categoryTagId, @ApiParam(value = "新闻源ID", required = true) @HeaderParam("newsSourceId") int newsSourceId, @ApiParam(value = "页码", required = true) @QueryParam("page") int page);

    /**
	 * 根据搜索词查询分发列表
     * 
	 * @param searchWord 搜索词
     * @param newsSourceId 新闻源ID
     * @param page 页码
     * @return 分发列表
     */
    @Path("/distributions/findBySearchWord")
    @GET
    @ApiOperation(value = "根据搜索词查询分发列表", response = Distribution.class, responseContainer = "List")
    public Response findDistributionsBySearchWord(@ApiParam(value = "搜索词", required = true) @QueryParam("searchWord") String searchWord, @ApiParam(value = "新闻源ID", required = true) @HeaderParam("newsSourceId") int newsSourceId, @ApiParam(value = "页码", required = true) @QueryParam("page") int page);
    
    /**
     * 根据搜索词查询分发列表
     * 
     * @param searchWord 搜索词
     * @param appId 应用ID
     * @param page 页码
     * @return 分发列表
     */
    @Path("/distributions/findBySearchWordInApp")
    @GET
    @ApiOperation(value = "根据搜索词查询分发列表", response = Distribution.class, responseContainer = "List")
    public Response findDistributionsBySearchWordInApp(@ApiParam(value = "搜索词", required = true) @QueryParam("searchWord") String searchWord, @ApiParam(value = "应用ID", required = true) @HeaderParam("appId") int appId, @ApiParam(value = "页码", required = true) @QueryParam("page") int page);
}
