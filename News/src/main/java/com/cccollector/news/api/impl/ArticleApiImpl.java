/**
 * 
 */
package com.cccollector.news.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cccollector.news.api.ArticleApi;
import com.cccollector.news.dao.ArticleDao;
import com.cccollector.news.dao.CommentDao;
import com.cccollector.news.dao.DislikeDao;
import com.cccollector.news.dao.DistributionDao;
import com.cccollector.news.dao.FavoriteDao;
import com.cccollector.news.dao.LikeDao;
import com.cccollector.news.dao.ReplyDao;
import com.cccollector.news.model.Article;
import com.cccollector.news.model.CategoryTag;
import com.cccollector.news.model.Comment;
import com.cccollector.news.model.Dislike;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.Favorite;
import com.cccollector.news.model.Like;
import com.cccollector.news.model.Reply;
import com.cccollector.universal.app.model.UserDetail;
import com.cccollector.universal.cxf.ApiException;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.model.UniversalJsonViews;
import com.cccollector.universal.service.GenericServiceHibernateImpl;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 文章API实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("articleApi")
public class ArticleApiImpl extends GenericServiceHibernateImpl<Integer, Article> implements ArticleApi {

	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private DistributionDao distributionDao;

	@Autowired
	private FavoriteDao favoriteDao;

	@Autowired
	private CommentDao commentDao;

	@Autowired
	private ReplyDao replyDao;

	@Autowired
	private LikeDao likeDao;

	@Autowired
	private DislikeDao dislikeDao;

	/**
	 * API接口结果每页数量
	 */
	@Value("${page.apiResultsPerPage}")
	private Integer apiResultsPerPage;

	/**
	 * 文章相关分发数量
	 */
	@Value("${count.relativeDistributions}")
	private Integer relativeDistributionsCount;

	/**
	 * 评论回复数量
	 */
	@Value("${count.commentReplies}")
	private Integer commentRepliesCount;

	/**
	 * 热门评论数量
	 */
	@Value("${count.topComments}")
	private Integer topCommentsCount;

	/**
	 * 热门评论喜欢最小数量
	 */
	@Value("${count.topCommentLikesMin}")
	private Integer topCommentLikesMinCount;

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
	@JsonView(UniversalJsonViews.StaticPublicPrivate.class)
	public Response getArticleStaticByArticleId(int articleId, int newsSourceId) {
		try {
			// 判断是否正在审核
			boolean inReview = false;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetail) {
				UserDetail userDetail = (UserDetail) principal;
				inReview = userDetail.getUserId() != null && userDetail.getCertificateEnabled() != null && userDetail.getCertificateEnabled() == true;
			}

			// 验证必填参数
			if (articleId == 0 || newsSourceId == 0 || !inReview) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			
			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId, "medias", "categoryTags");
			// 如果不存在
			if (article == null || article.getNewsSource().getNewsSourceId() != newsSourceId || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			return Response.status(Response.Status.OK).entity(article).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.DynamicPublicPrivate.class)
	public Response getArticleDynamicByArticleId(int articleId, int newsSourceId, int loggedUserAppId, String loggedAccessToken) {
		try {
			// 判断是否正在审核
			boolean inReview = false;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetail) {
				UserDetail userDetail = (UserDetail) principal;
				inReview = userDetail.getUserId() != null && userDetail.getCertificateEnabled() != null && userDetail.getCertificateEnabled() == true;
			}

			// 验证必填参数
			if (articleId == 0 || newsSourceId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			
			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId);
			// 如果不存在
			if (article == null || (!article.getPublished() && !inReview) || article.getNewsSource().getNewsSourceId() != newsSourceId || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			// 文章获得阅读数 + 1
			if (article.getPublished()) {
				article.setViewCount(article.getViewCount() + 1);
			}

			// 用户应用已登录
			if (loggedUserAppId > 0 && loggedAccessToken != null && !loggedAccessToken.isEmpty()) {
				// 设置已登录用户应用对此文章的收藏
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", loggedUserAppId)));
				predicateQueryFieldList.add(new QueryField("contentType", Favorite.ContentTypeEnum.文章.ordinal()));
				predicateQueryFieldList.add(new QueryField("contentId", articleId));
				List<Favorite> favorites = favoriteDao.loadAll(predicateQueryFieldList, null);
				article.setFavorite(favorites.size() > 0 ? favorites.get(0) : null);
				
				// 设置已登录用户应用对此文章的喜欢
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", loggedUserAppId)));
				predicateQueryFieldList.add(new QueryField("contentType", Like.ContentTypeEnum.文章.ordinal()));
				predicateQueryFieldList.add(new QueryField("contentId", articleId));
				List<Like> likes = likeDao.loadAll(predicateQueryFieldList, null);
				article.setLike(likes.size() > 0 ? likes.get(0) : null);
				
				// 设置已登录用户应用对此文章的不喜欢
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", loggedUserAppId)));
				predicateQueryFieldList.add(new QueryField("contentType", Dislike.ContentTypeEnum.文章.ordinal()));
				predicateQueryFieldList.add(new QueryField("contentId", articleId));
				List<Dislike> dislikes = dislikeDao.loadAll(predicateQueryFieldList, null);
				article.setDislike(dislikes.size() > 0 ? dislikes.get(0) : null);
			}

			return Response.status(Response.Status.OK).entity(article).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response getArticleRelativeDistributionsByArticleId(int articleId, int newsSourceId) {
		try {
			// 判断是否正在审核
			boolean inReview = false;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetail) {
				UserDetail userDetail = (UserDetail) principal;
				inReview = userDetail.getUserId() != null && userDetail.getCertificateEnabled() != null && userDetail.getCertificateEnabled() == true;
			}

			// 验证必填参数
			if (articleId == 0 || newsSourceId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			
			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId, "categoryTags");
			// 如果不存在
			if (article == null || (!article.getPublished() && !inReview) || article.getNewsSource().getNewsSourceId() != newsSourceId || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}
			
			// 包含的分类标签ID
			for (CategoryTag categoryTag : article.getCategoryTags()) {
				article.getCategoryTagIds().add(categoryTag.getCategoryTagId());
			}
			
			List<Distribution> relativeDistributions = new ArrayList<Distribution>();
			if (article.getCategoryTagIds().size() > 0) {
				// 根据分类标签查询分发列表
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSourceId)));
				predicateQueryFieldList.add(new QueryField("articleId",  articleId, PredicateEnum.NOT_EQUAL));
				predicateQueryFieldList.add(new QueryField("categoryTags", new QueryField("categoryTagId", article.getCategoryTagIds(), PredicateEnum.IN)));
				predicateQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL)));
				predicateQueryFieldList.add(new QueryField("distributions", new QueryField("firstPublished", Boolean.TRUE)));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", Boolean.FALSE)));
				orderQueryFieldList.add(new QueryField("distributions", new QueryField("distributionId", Boolean.FALSE)));
				relativeDistributions = articleDao.loadPage(Distribution.class, new QueryField("distributions", null), predicateQueryFieldList, orderQueryFieldList, 0, relativeDistributionsCount);						
				for (Distribution distribution : relativeDistributions) {
					// 所属栏目ID
					distribution.setColumnId(distribution.getColumn().getColumnId());

					// 包含的缩略图列表
					distribution.getThumbnails().iterator();
				}
			}

			return Response.status(Response.Status.OK).entity(relativeDistributions).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response getArticleFirstPublishedDistributionByArticleId(int articleId) {
		try {
			// 验证必填参数
			if (articleId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据文章ID查询分发列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("enabled", true)));
			predicateQueryFieldList.add(new QueryField("articleId", articleId));
			predicateQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL)));
			predicateQueryFieldList.add(new QueryField("distributions", new QueryField("firstPublished", Boolean.TRUE)));
			List<Distribution> distributions = articleDao.loadPage(Distribution.class, new QueryField("distributions", null), predicateQueryFieldList, null, 0, 1);		
			Distribution distribution = distributions.isEmpty() ? null : distributions.get(0);
			// 如果不存在
			if (distribution == null) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.分发不存在.toJson()).build();
			}
			
			// 所属栏目ID
			distribution.setColumnId(distribution.getColumn().getColumnId());

			// 包含的缩略图列表
			distribution.getThumbnails().iterator();
			
			return Response.status(Response.Status.OK).entity(distribution).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.Private.class)
	public Response getArticleCommentsByArticleId(int articleId, int newsSourceId, int page, int loggedUserAppId, String loggedAccessToken) {
		try {
			// 验证必填参数
			if (articleId == 0 || newsSourceId == 0 || page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			
			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId);
			// 如果不存在
			if (article == null || !article.getPublished() || article.getNewsSource().getNewsSourceId() != newsSourceId || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			// 根据文章ID查询评论列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("contentType", Comment.ContentTypeEnum.文章.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", articleId));
			predicateQueryFieldList.add(new QueryField("status", Comment.StatusEnum.已发布.ordinal()));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("createTime", Boolean.FALSE));
			List<Comment> comments = commentDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);
			// 如果无结果
			if (comments.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			for (Comment comment : comments) {
				commentDao.getEntityManager().detach(comment);
				
				// 设置所属的用户应用ID
				comment.setUserAppId(comment.getUserApp().getUserAppId());
				
				// 设置评论最先的回复
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("comment", new QueryField("commentId", comment.getCommentId())));
				predicateQueryFieldList.add(new QueryField("status", Comment.StatusEnum.已发布.ordinal()));
				orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("createTime", Boolean.TRUE));
				List<Reply> replies = replyDao.loadPage(predicateQueryFieldList, orderQueryFieldList, 0, commentRepliesCount);
				for (Reply reply : replies) {
					// 设置所属的用户应用ID
					reply.setUserAppId(reply.getUserApp().getUserAppId());
				}
				comment.setReplies(replies);
				
				// 用户应用已登录
				if (loggedUserAppId > 0 && loggedAccessToken != null && !loggedAccessToken.isEmpty()) {
					// 设置已登录用户应用对此评论的喜欢
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", loggedUserAppId)));
					predicateQueryFieldList.add(new QueryField("contentType", Like.ContentTypeEnum.评论.ordinal()));
					predicateQueryFieldList.add(new QueryField("contentId", comment.getCommentId()));
					List<Like> likes = likeDao.loadAll(predicateQueryFieldList, null);
					comment.setLike(likes.size() > 0 ? likes.get(0) : null);
				}
			}
			
			return Response.status(Response.Status.OK).entity(comments).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.Private.class)
	public Response getArticleCommentsTopByArticleId(int articleId, int newsSourceId, int loggedUserAppId, String loggedAccessToken) {
		try {
			// 验证必填参数
			if (articleId == 0 || newsSourceId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			
			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId);
			// 如果不存在
			if (article == null || !article.getPublished() || article.getNewsSource().getNewsSourceId() != newsSourceId || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			// 根据文章ID查询热门评论列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("contentType", Comment.ContentTypeEnum.文章.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", articleId));
			predicateQueryFieldList.add(new QueryField("status", Comment.StatusEnum.已发布.ordinal()));
			predicateQueryFieldList.add(new QueryField("likeCount", topCommentLikesMinCount, PredicateEnum.GREATER_THAN_OR_EQUAL));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("likeCount", Boolean.FALSE));
			orderQueryFieldList.add(new QueryField("createTime", Boolean.FALSE));
			List<Comment> comments = commentDao.loadPage(predicateQueryFieldList, orderQueryFieldList, 0, topCommentsCount);
			for (Comment comment : comments) {
				commentDao.getEntityManager().detach(comment);
				
				// 设置所属的用户应用ID
				comment.setUserAppId(comment.getUserApp().getUserAppId());
				
				// 设置评论最先的回复
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("comment", new QueryField("commentId", comment.getCommentId())));
				predicateQueryFieldList.add(new QueryField("status", Comment.StatusEnum.已发布.ordinal()));
				orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("createTime", Boolean.TRUE));
				List<Reply> replies = replyDao.loadPage(predicateQueryFieldList, orderQueryFieldList, 0, commentRepliesCount);
				for (Reply reply : replies) {
					// 设置所属的用户应用ID
					reply.setUserAppId(reply.getUserApp().getUserAppId());
				}
				comment.setReplies(replies);
				
				// 用户应用已登录
				if (loggedUserAppId > 0 && loggedAccessToken != null && !loggedAccessToken.isEmpty()) {
					// 设置已登录用户应用对此评论的喜欢
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", loggedUserAppId)));
					predicateQueryFieldList.add(new QueryField("contentType", Like.ContentTypeEnum.评论.ordinal()));
					predicateQueryFieldList.add(new QueryField("contentId", comment.getCommentId()));
					List<Like> likes = likeDao.loadAll(predicateQueryFieldList, null);
					comment.setLike(likes.size() > 0 ? likes.get(0) : null);
				}
			}

			return Response.status(Response.Status.OK).entity(comments).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.Private.class)
	public Response getArticleCommentRepliesByArticleIdAndCommentId(int articleId, int newsSourceId, int commentId, int page) {
		try {
			// 验证必填参数
			if (articleId == 0 || newsSourceId == 0 || commentId == 0 || page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			
			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId);
			// 如果不存在
			if (article == null || !article.getPublished() || article.getNewsSource().getNewsSourceId() != newsSourceId || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			// 根据评论ID查询回复列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("comment", new QueryField("commentId", commentId)));
			predicateQueryFieldList.add(new QueryField("comment", new QueryField("status", Comment.StatusEnum.已发布.ordinal())));
			predicateQueryFieldList.add(new QueryField("status", Reply.StatusEnum.已发布.ordinal()));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("createTime", Boolean.TRUE));
			List<Reply> replies = replyDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);
			// 如果无结果
			if (replies.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			for (Reply reply : replies) {
				// 设置所属的用户应用ID
				reply.setUserAppId(reply.getUserApp().getUserAppId());
			}
			
			return Response.status(Response.Status.OK).entity(replies).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response getDistributionByDistributionId(int distributionId, int newsSourceId) {
		try {
			// 验证必填参数
			if (distributionId == 0 || newsSourceId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据分发ID获取分发
			Distribution distribution = distributionDao.loadById(distributionId);
			// 如果不存在
			if (distribution == null || !distribution.getPublished() || distribution.getArticle().getNewsSource().getNewsSourceId() != newsSourceId || !distribution.getArticle().getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.分发不存在.toJson()).build();
			}

			// 所属栏目ID
			distribution.setColumnId(distribution.getColumn().getColumnId());
			
			// 包含的缩略图
			distribution.getThumbnails().iterator();
			
			return Response.status(Response.Status.OK).entity(distribution).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response findDistributionsByCategoryTagId(int categoryTagId, int newsSourceId, int page) {
		try {
			// 验证必填参数
			if (categoryTagId == 0 || newsSourceId == 0 || page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			
			// 根据分类标签查询分发列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSourceId)));
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("enabled", true)));
			predicateQueryFieldList.add(new QueryField("categoryTags", new QueryField("categoryTagId", categoryTagId, PredicateEnum.IN)));
			predicateQueryFieldList.add(new QueryField("categoryTags", new QueryField("enabled", true)));
			predicateQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL)));
			predicateQueryFieldList.add(new QueryField("distributions", new QueryField("firstPublished", Boolean.TRUE)));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", Boolean.FALSE)));
			orderQueryFieldList.add(new QueryField("distributions", new QueryField("distributionId", Boolean.FALSE)));
			List<Distribution> distributions = articleDao.loadPage(Distribution.class, new QueryField("distributions", null), predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);		
			// 如果无结果
			if (distributions.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			for (Distribution distribution : distributions) {
				// 所属栏目ID
				distribution.setColumnId(distribution.getColumn().getColumnId());

				// 包含的缩略图列表
				distribution.getThumbnails().iterator();
			}

			return Response.status(Response.Status.OK).entity(distributions).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response findDistributionsBySearchWord(String searchWord, int newsSourceId, int page) {
		try {
			// 验证必填参数
			if (newsSourceId == 0 || page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			verifyRequired(searchWord);

			// 根据搜索词查询分发列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSourceId)));
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("enabled", true)));
			predicateQueryFieldList.add(new QueryField("title", searchWord, PredicateEnum.LIKE));
			predicateQueryFieldList.add(new QueryField("distributions", new QueryField("column", new QueryField("enabled", true))));
			predicateQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL)));
			predicateQueryFieldList.add(new QueryField("distributions", new QueryField("firstPublished", Boolean.TRUE)));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", Boolean.FALSE)));
			orderQueryFieldList.add(new QueryField("distributions", new QueryField("distributionId", Boolean.FALSE)));
			List<Distribution> distributions = articleDao.loadPage(Distribution.class, new QueryField("distributions", null), predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);		
			// 如果无结果
			if (distributions.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			for (Distribution distribution : distributions) {
				// 所属栏目ID
				distribution.setColumnId(distribution.getColumn().getColumnId());

				// 包含的缩略图列表
				distribution.getThumbnails().iterator();
			}

			return Response.status(Response.Status.OK).entity(distributions).build();
		} catch (ApiException e) {
			return e.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response findDistributionsBySearchWordInApp(String searchWord, int appId, int page) {
		try {
			// 验证必填参数
			if (appId == 0 || page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			verifyRequired(searchWord);

			// 根据搜索词查询分发列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("apps", new QueryField("appId", appId))));
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("enabled", true)));
			predicateQueryFieldList.add(new QueryField("title", searchWord, PredicateEnum.LIKE));
			predicateQueryFieldList.add(new QueryField("distributions", new QueryField("column", new QueryField("enabled", true))));
			predicateQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL)));
			predicateQueryFieldList.add(new QueryField("distributions", new QueryField("firstPublished", Boolean.TRUE)));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", Boolean.FALSE)));
			orderQueryFieldList.add(new QueryField("distributions", new QueryField("distributionId", Boolean.FALSE)));
			List<Distribution> distributions = articleDao.loadPage(Distribution.class, new QueryField("distributions", null), predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);		
			// 如果无结果
			if (distributions.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			for (Distribution distribution : distributions) {
				// 所属栏目ID
				distribution.setColumnId(distribution.getColumn().getColumnId());

				// 包含的缩略图列表
				distribution.getThumbnails().iterator();
			}

			return Response.status(Response.Status.OK).entity(distributions).build();
		} catch (ApiException e) {
			return e.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	/**
	 * 错误消息枚举
	 */	
	public enum ErrorMessage {
		成功(0, "成功"),
		缺少必填项(1, "缺少必填项"),
		格式错误(101, "格式错误"),
		数据异常(102, "数据异常"),
		数据唯一限制(103, "数据唯一限制"),
		操作受限(104, "操作受限"),
		操作超时(105, "操作超时"),
		操作失败(106, "操作失败"),
		列表结束(107, "列表结束"),
		文章不存在(201, "文章不存在"),
		分发不存在(202, "分发不存在");

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
