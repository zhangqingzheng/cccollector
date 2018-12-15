/**
 * 
 */
package com.cccollector.news.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cccollector.news.model.Comment;
import com.cccollector.news.model.Dislike;
import com.cccollector.news.model.Favorite;
import com.cccollector.news.model.Like;
import com.cccollector.news.model.Log;
import com.cccollector.news.model.Reply;
import com.cccollector.news.model.UserApp;
import com.cccollector.universal.service.GenericService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 用户应用API接口
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Path("/userApps")
@Produces({ MediaType.APPLICATION_JSON })
@Api(value = "用户应用API")
public interface UserAppApi extends GenericService<Integer, UserApp> {

    /**
     * 根据搜索词查询用户应用列表
     * 
     * @param appId 应用ID
     * @param searchWord 搜索词
	 * @param page 页码
     * @return 用户应用列表
     */
    @Path("/findBySearchWord")
    @GET
    @ApiOperation(value = "根据搜索词查询用户应用列表", response = UserApp.class, responseContainer = "List")
    public Response findUserAppsBySearchWord(@ApiParam(value = "应用ID", required = true) @HeaderParam("appId") int appId, @ApiParam(value = "搜索词", required = true) @QueryParam("searchWord") String searchWord, @ApiParam(value = "页码", required = true) @QueryParam("page") int page);

	/**
	 * 获取用户应用
	 * 
	 * @param userAppId 用户应用ID
	 * @return 用户应用对象
	 */
    @Path("/{userAppId}")
    @GET
    @ApiOperation(value = "获取用户应用", response = UserApp.class)
    public Response getUserApp(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId);

	/**
	 * 获取已登录用户应用
	 * 
	 * @param userAppId 用户应用ID
	 * @param accessToken 访问令牌
	 * @return 已登录用户应用对象
	 */
    @Path("/{userAppId}/logged")
    @GET
    @ApiOperation(value = "获取已登录用户应用", response = UserApp.class)
    public Response getLoggedUserApp(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken);

	/**
	 * 同步已登录用户应用
	 * 
	 * @param userAppId 用户应用ID
	 * @param accessToken 访问令牌
	 * @return 正确信息
	 */
    @Path("/{userAppId}/sync")
    @GET
    @ApiOperation(value = "同步已登录用户应用")
    public Response syncLoggedUserApp(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken);

	/**
	 * 修改已登录用户应用资料
	 * 
	 * @param userAppId 用户应用ID
	 * @param accessToken 访问令牌	
	 * @param intro 自我介绍
	 * @param isRecentVisible 动态是否可见
	 * @return 用户应用对象
	 */
    @Path("/{userAppId}/modifyProfile")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "修改已登录用户应用资料", response = UserApp.class)
    public Response modifyLoggedUserAppProfile(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "自我介绍", required = false) @FormParam(value = "intro") String intro, @ApiParam(value = "动态是否可见", required = false) @FormParam("isRecentVisible") Boolean isRecentVisible); 

    /**
     * 获取用户应用的关注用户应用列表
     * 
	 * @param userAppId 用户应用ID
	 * @param page 页码
     * @return 关注用户应用列表
     */
    @Path("/{userAppId}/followUserApps")
    @GET
    @ApiOperation(value = "获取用户应用的关注用户应用列表", response = UserApp.class, responseContainer = "List")
    public Response getUserAppFollowUserApps(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "页码", required = true) @QueryParam("page") int page);

    /**
     * 获取用户应用的粉丝用户应用列表
     * 
	 * @param userAppId 用户应用ID
     * @param page 页码
     * @return 粉丝用户应用列表
     */
    @Path("/{userAppId}/followedUserApps")
    @GET
    @ApiOperation(value = "获取用户应用的粉丝用户应用列表", response = UserApp.class, responseContainer = "List")
    public Response getUserAppFollowedUserApps(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "页码", required = true) @QueryParam("page") int page);

	/**
	 * 添加已登录用户应用关注
	 * 
	 * @param userAppId 用户应用ID
	 * @param accessToken 访问令牌
	 * @param followedUserAppId 被关注用户应用ID
	 * @return 正确信息
	 */
    @Path("/{userAppId}/addFollow")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "添加已登录用户应用关注")
    public Response addLoggedUserAppFollow(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "被关注用户应用ID", required = true) @FormParam("followedUserAppId") int followedUserAppId);

    /**
     * 取消已登录用户应用关注
     * 
     * @param userAppId 用户应用ID
	 * @param accessToken 访问令牌
     * @param followedUserAppId 被关注用户应用ID
     * @return 正确信息
     */
    @Path("/{userAppId}/removeFollow")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "取消已登录用户应用关注")
    public Response removeLoggedUserAppFollow(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "被关注用户应用ID", required = true) @FormParam("followedUserAppId") int followedUserAppId);

	/**
     * 获取已登录用户应用的文章收藏列表
	 * 
	 * @param userAppId 用户应用ID
	 * @param accessToken 访问令牌
     * @param page 页码
	 * @return 文章收藏列表
	 */
    @Path("/{userAppId}/articleFavorites")
    @GET
    @ApiOperation(value = "获取已登录用户应用的文章收藏列表", response = Favorite.class, responseContainer = "List")
    public Response getLoggedUserAppArticleFavorites(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "页码", required = true) @QueryParam("page") int page);

    /**
     * 添加已登录用户应用文章收藏
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param articleId 文章ID
     * @return 文章收藏
     */
    @Path("/{userAppId}/addArticleFavorite")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "添加已登录用户应用文章收藏", response = Favorite.class)
    public Response addLoggedUserAppArticleFavorite(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "文章ID", required = true) @FormParam("articleId") int articleId);

    /**
     * 取消已登录用户应用文章收藏
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param articleId 文章ID
     * @return 正确信息
     */
    @Path("/{userAppId}/removeArticleFavorite")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "取消已登录用户应用文章收藏")
    public Response removeLoggedUserAppArticleFavorite(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "文章ID", required = true) @FormParam("articleId") int articleId);

    /**
     * 添加已登录用户应用文章喜欢
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param articleId 文章ID
     * @return 文章喜欢
     */
    @Path("/{userAppId}/addArticleLike")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "添加已登录用户应用文章喜欢", response = Like.class)
    public Response addLoggedUserAppArticleLike(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "文章ID", required = true) @FormParam("articleId") int articleId);

    /**
     * 取消已登录用户应用文章喜欢
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param articleId 文章ID
     * @return 正确信息
     */
    @Path("/{userAppId}/removeArticleLike")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "取消已登录用户应用文章喜欢")
    public Response removeLoggedUserAppArticleLike(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "文章ID", required = true) @FormParam("articleId") int articleId);     

    /**
     * 添加已登录用户应用文章不喜欢
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param articleId 文章ID
     * @return 文章不喜欢
     */
    @Path("/{userAppId}/addArticleDislike")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "添加已登录用户应用文章不喜欢", response = Dislike.class)
    public Response addLoggedUserAppArticleDislike(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "文章ID", required = true) @FormParam("articleId") int articleId);

    /**
     * 取消已登录用户应用文章不喜欢
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param articleId 文章ID
     * @return 正确信息
     */
    @Path("/{userAppId}/removeArticleDislike")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "取消已登录用户应用文章不喜欢")
    public Response removeLoggedUserAppArticleDislike(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "文章ID", required = true) @FormParam("articleId") int articleId);

    /**
     * 添加已登录用户应用文章评论
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param articleId 文章ID
     * @param content 内容
     * @return 文章评论
     */
    @Path("/{userAppId}/addArticleComment")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "添加已登录用户应用文章评论", response = Comment.class)
    public Response addLoggedUserAppArticleComment(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "文章ID", required = true) @FormParam("articleId") int articleId, @ApiParam(value = "内容", required = true) @FormParam("content") String content);

    /**
     * 删除已登录用户应用文章评论
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param commentId 评论ID
     * @return 正确信息
     */
    @Path("/{userAppId}/removeArticleComment")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "删除已登录用户应用文章评论")
    public Response removeLoggedUserAppArticleComment(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "评论ID", required = true) @FormParam("commentId") int commentId);

    /**
     * 添加已登录用户应用评论回复
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param commentId 评论ID
     * @param content 内容
     * @return 评论回复
     */
    @Path("/{userAppId}/addCommentReply")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "添加已登录用户应用评论回复", response = Reply.class)
    public Response addLoggedUserAppCommentReply(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "评论ID", required = true) @FormParam("commentId") int commentId, @ApiParam(value = "内容", required = true) @FormParam("content") String content);

    /**
     * 添加已登录用户应用回复回复
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param commentId 评论ID
     * @param content 内容
     * @param replyUserAppId 回复用户应用ID
     * @return 回复回复
     */
    @Path("/{userAppId}/addReplyReply")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "添加已登录用户应用回复回复", response = Reply.class)
    public Response addLoggedUserAppReplyReply(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "评论ID", required = true) @FormParam("commentId") int commentId, @ApiParam(value = "内容", required = true) @FormParam("content") String content, @ApiParam(value = "回复用户应用ID", required = true) @FormParam("replyUserAppId") int replyUserAppId);
    
    /**
     * 删除已登录用户应用回复
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param replyId 回复ID
     * @return 正确信息
     */
    @Path("/{userAppId}/removeReply")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "删除已登录用户应用回复")
    public Response removeLoggedUserAppReply(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "回复ID", required = true) @FormParam("replyId") int replyId);

    /**
     * 添加已登录用户应用评论喜欢
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param commentId 评论ID
     * @return 评论喜欢
     */
    @Path("/{userAppId}/addCommentLike")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "添加已登录用户应用评论喜欢", response = Like.class)
    public Response addLoggedUserAppCommentLike(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "评论ID", required = true) @FormParam("commentId") int commentId);

    /**
     * 取消已登录用户应用评论喜欢
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param commentId 评论ID
     * @return 正确信息
     */
    @Path("/{userAppId}/removeCommentLike")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "取消已登录用户应用评论喜欢")
    public Response removeLoggedUserAppCommentLike(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "评论ID", required = true) @FormParam("commentId") int commentId);
    
    /**
     * 获取用户应用的评论列表
     * 
     * @param userAppId 用户应用ID
     * @param page 页码
     * @param loggedUserAppId 已登录用户应用ID
     * @param loggedAccessToken 已登录访问令牌
     * @return 评论列表
     */
    @Path("/{userAppId}/comments")
    @GET
    @ApiOperation(value = "获取用户应用的评论列表", response = Comment.class, responseContainer = "List")
    public Response getUserAppComments(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "页码", required = true) @QueryParam("page") int page, @ApiParam(value = "已登录用户应用ID", required = false) @HeaderParam("loggedUserAppId") int loggedUserAppId, @ApiParam(value = "已登录访问令牌", required = false) @HeaderParam("loggedAccessToken") String loggedAccessToken);
    
    /**
     * 获取用户应用的回复列表
     * 
     * @param userAppId 用户应用ID
     * @param page 页码
     * @param loggedUserAppId 已登录用户应用ID
     * @param loggedAccessToken 已登录访问令牌
     * @return 回复列表
     */
    @Path("/{userAppId}/replies")
    @GET
    @ApiOperation(value = "获取用户应用的回复列表", response = Reply.class, responseContainer = "List")
    public Response getUserAppReplies(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "页码", required = true) @QueryParam("page") int page, @ApiParam(value = "已登录用户应用ID", required = false) @HeaderParam("loggedUserAppId") int loggedUserAppId, @ApiParam(value = "已登录访问令牌", required = false) @HeaderParam("loggedAccessToken") String loggedAccessToken);
   
    /**
     * 获取用户应用的喜欢列表
     * 
     * @param userAppId 用户应用ID
     * @param page 页码
     * @param loggedUserAppId 已登录用户应用ID
     * @param loggedAccessToken 已登录访问令牌
     * @return 喜欢列表
     */
    @Path("/{userAppId}/likes")
    @GET
    @ApiOperation(value = "获取用户应用的喜欢列表", response = Like.class, responseContainer = "List")
    public Response getUserAppLikes(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "页码", required = true) @QueryParam("page") int page, @ApiParam(value = "已登录用户应用ID", required = false) @HeaderParam("loggedUserAppId") int loggedUserAppId, @ApiParam(value = "已登录访问令牌", required = false) @HeaderParam("loggedAccessToken") String loggedAccessToken);

    /**
     * 添加已登录用户应用评论举报
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param commentId 评论ID
     * @param type 类别
     * @return 正确信息
     */
    @Path("/{userAppId}/addCommentReport")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "添加已登录用户应用评论举报")
    public Response addLoggedUserAppCommentReport(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "评论ID", required = true) @FormParam("commentId") int commentId, @ApiParam(value = "类别", required = true) @FormParam("type") int type);  

    /**
     * 添加已登录用户应用回复举报
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param replyId 回复ID
     * @param type 类别
     * @return 正确信息
     */
    @Path("/{userAppId}/addReplyReport")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "添加已登录用户应用回复举报")
    public Response addLoggedUserAppReplyReport(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "回复ID", required = true) @FormParam("replyId") int replyId, @ApiParam(value = "类别", required = true) @FormParam("type") int type);  

	/**
     * 获取已登录用户应用的系统日志列表
	 * 
	 * @param userAppId 用户应用ID
	 * @param accessToken 访问令牌
     * @param page 页码
 	 * @return 系统日志列表
	 */
    @Path("/{userAppId}/systemLogs")
    @GET
    @ApiOperation(value = "获取已登录用户应用的系统日志列表", response = Log.class, responseContainer = "List")
    public Response getLoggedUserAppSystemLogs(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "页码", required = true) @QueryParam("page") int page);    

    /**
     * 获取已登录用户应用的回复日志列表
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param page 页码
     * @return 回复列表
     */
    @Path("/{userAppId}/replyLogs")
    @GET
    @ApiOperation(value = "获取已登录用户应用的回复日志列表", response = Reply.class, responseContainer = "List")
    public Response getLoggedUserAppReplyLogs(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "页码", required = true) @QueryParam("page") int page);    

    /**
     * 获取已登录用户应用的喜欢日志列表
     * 
     * @param userAppId 用户应用ID
     * @param accessToken 访问令牌
     * @param page 页码
     * @return 喜欢列表
     */
    @Path("/{userAppId}/likeLogs")
    @GET
    @ApiOperation(value = "获取已登录用户应用的喜欢日志列表", response = Like.class, responseContainer = "List")
    public Response getLoggedUserAppLikeLogs(@ApiParam(value = "用户应用ID", required = true) @PathParam("userAppId") int userAppId, @ApiParam(value = "访问令牌", required = true) @HeaderParam("accessToken") String accessToken, @ApiParam(value = "页码", required = true) @QueryParam("page") int page);    
}
