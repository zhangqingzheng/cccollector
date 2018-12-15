/**
 * 
 */
package com.cccollector.news.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.news.api.UserAppApi;
import com.cccollector.news.dao.AppDao;
import com.cccollector.news.dao.ArticleDao;
import com.cccollector.news.dao.CommentDao;
import com.cccollector.news.dao.DislikeDao;
import com.cccollector.news.dao.DistributionDao;
import com.cccollector.news.dao.FavoriteDao;
import com.cccollector.news.dao.FollowDao;
import com.cccollector.news.dao.LikeDao;
import com.cccollector.news.dao.LogDao;
import com.cccollector.news.dao.ReplyDao;
import com.cccollector.news.dao.ReportDao;
import com.cccollector.news.dao.UserAppDao;
import com.cccollector.news.dao.UserDao;
import com.cccollector.news.model.App;
import com.cccollector.news.model.Article;
import com.cccollector.news.model.Comment;
import com.cccollector.news.model.Dislike;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.Favorite;
import com.cccollector.news.model.Follow;
import com.cccollector.news.model.Like;
import com.cccollector.news.model.Log;
import com.cccollector.news.model.Reply;
import com.cccollector.news.model.Report;
import com.cccollector.news.model.User;
import com.cccollector.news.model.UserApp;
import com.cccollector.universal.cxf.ApiException;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.model.UniversalJsonViews;
import com.cccollector.universal.service.GenericServiceHibernateImpl;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 用户应用API实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("userAppApi")
public class UserAppApiImpl extends GenericServiceHibernateImpl<Integer, UserApp> implements UserAppApi {

	@Autowired
	private UserAppDao userAppDao;

	@Autowired
	private com.cccollector.universal.user.api.UserAppApi _user_userAppApi;

	@Autowired
	private AppDao appDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private DistributionDao distributionDao;

	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private FollowDao followDao;

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

	@Autowired
	private ReportDao reportDao;

	@Autowired
	private LogDao logDao;

	/**
	 * API接口结果每页数量
	 */
	@Value("${page.apiResultsPerPage}")
	private Integer apiResultsPerPage;

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
	@JsonView(UniversalJsonViews.Public.class)
	public Response findUserAppsBySearchWord(int appId, String searchWord, int page) {
		try {
			// 验证必填参数
			if (appId == 0 || page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			verifyRequired(searchWord);

			// 根据搜索词查询用户应用列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("app", new QueryField("appId", appId)));
			predicateQueryFieldList.add(new QueryField("app", new QueryField("enabled", true)));
			predicateQueryFieldList.add(new QueryField("user", new QueryField("nickName", searchWord, PredicateEnum.LIKE)));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("syncTime", false));
			List<UserApp> userApps = userAppDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);
			// 如果无结果
			if (userApps.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}

			return Response.status(Response.Status.OK).entity(userApps).build();
		} catch (ApiException e) {
			return e.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.Public.class)
	public Response getUserApp(int userAppId) {
		try {
			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId);
			
			return Response.status(Response.Status.OK).entity(userApp).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.Private.class)
	public Response getLoggedUserApp(int userAppId, String accessToken) {
		try {
			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId, "follows");

			return Response.status(Response.Status.OK).entity(userApp).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response syncLoggedUserApp(int userAppId, String accessToken) {
		try {
			// 从用户平台获取用户应用
			com.cccollector.universal.user.model.UserApp userAppResponse = _user_userAppApi.loadUserApp(userAppId, accessToken);
			if (userAppResponse == null) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.用户应用不存在.toJson()).build();
			}
			com.cccollector.universal.user.model.User userResponse = userAppResponse.getUser();

			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId);
			if (userApp == null ) {
				// 获取应用
				App app = appDao.loadById(userAppResponse.getApp().getAppId());
				
				// 获取用户
				User user = userDao.loadById(userResponse.getUserId());
				
				if (user == null) {
					// 使用用户平台用户应用创建用户
					user = new User();
					user.setUserId(userResponse.getUserId());
					user.setNickName(userResponse.getNickName());
					user.setCellphone(userResponse.getCellphone());
					user.setEmail(userResponse.getEmail());
					user.setRegisterTime(userResponse.getRegisterTime());
					userDao.save(user);
				} else {
					// 使用用户平台用户应用更新用户
					user.setNickName(userResponse.getNickName());
					user.setCellphone(userResponse.getCellphone());
					user.setEmail(userResponse.getEmail());
					user.setRegisterTime(userResponse.getRegisterTime());
				}

				// 使用用户平台用户应用创建用户应用
				userApp = new UserApp();
				userApp.setUserAppId(userAppResponse.getUserAppId());
				userApp.setApp(app);
				userApp.setCreateTime(userAppResponse.getCreateTime());
				userApp.setUser(user);
				userApp.setSyncTime(new Date());
				userApp.setIsRecentVisible(true);
				userApp.setFollowCount(0);
				userApp.setFollowerCount(0);
				userAppDao.save(userApp);
			} else {
				// 使用用户平台用户应用更新用户
				User user = userApp.getUser();
				user.setNickName(userResponse.getNickName());
				user.setCellphone(userResponse.getCellphone());
				user.setEmail(userResponse.getEmail());
				user.setRegisterTime(userResponse.getRegisterTime());
	
				// 使用用户平台用户应用更新用户应用
				userApp.setCreateTime(userAppResponse.getCreateTime());
				userApp.setSyncTime(new Date());
			}

			return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.Private.class)
	public Response modifyLoggedUserAppProfile(int userAppId, String accessToken, String intro, Boolean isRecentVisible) {
		try {
			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId, "follows");
			
			// 更新用户应用基本信息
			if (isRecentVisible != null) {
				userApp.setIsRecentVisible(isRecentVisible);
			}
			
			if (intro != null) {
				if (intro.length() < 0 || intro.length() > 300) {
					return Response.status(Response.Status.NOT_ACCEPTABLE).entity(ErrorMessage.格式错误.toJson()).build();					
				}
				userApp.setIntro(intro);
			}

			return Response.status(Response.Status.OK).entity(userApp).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.Public.class)
	public Response getUserAppFollowUserApps(int userAppId, int page) {
		try {
			// 验证必填参数
			if (page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 获取用户应用的关注列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("createTime", false));
			List<Follow> follows = followDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);
			// 如果无结果
			if (follows.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			// 获取关注用户应用列表
			List<UserApp> userApps = new ArrayList<UserApp>();
			for (Follow follow : follows) {
				userApps.add(follow.getFollowedUserApp());
			}
			
			return Response.status(Response.Status.OK).entity(userApps).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.Public.class)
	public Response getUserAppFollowedUserApps(int userAppId, int page) {
		try {
			// 验证必填参数
			if (page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 获取用户应用的粉丝列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("followedUserApp", new QueryField("userAppId", userAppId)));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("createTime", false));
			List<Follow> followeds = followDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);
			// 如果无结果
			if (followeds.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			// 获取粉丝用户应用列表
			List<UserApp> userApps = new ArrayList<UserApp>();
			for (Follow followed : followeds) {
				userApps.add(followed.getUserApp());
			}
			
			return Response.status(Response.Status.OK).entity(userApps).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response addLoggedUserAppFollow(int userAppId, String accessToken, int followedUserAppId) {
		try {
			// 验证必填参数
			if (followedUserAppId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId);
			
			// 根据被关注用户应用ID获取被关注用户应用
			UserApp followedUserApp = userAppDao.loadById(followedUserAppId);
			// 如果不存在
			if (followedUserApp == null || !followedUserApp.getApp().getAppId().equals(userApp.getApp().getAppId())) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.用户应用不存在.toJson()).build();
			}

			// 查询关注
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("followedUserApp", new QueryField("userAppId", followedUserAppId)));
			long followedsCount = followDao.count(predicateQueryFieldList);
			// 如果已存在
			if (followedsCount > 0) {
				return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
			}

			// 获取被关注
			predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", followedUserAppId)));
			predicateQueryFieldList.add(new QueryField("followedUserApp", new QueryField("userAppId", userAppId)));
			List<Follow> followedsList = followDao.loadPage(predicateQueryFieldList, null, 0, 1);
			Follow followed = followedsList.size() > 0 ? followedsList.get(0) : null;

			// 添加关注
			Follow follow = new Follow();
			follow.setFollowedUserApp(followedUserApp);
			follow.setMutual(followed != null);
			follow.setCreateTimeDate(new Date());
			follow.setUserApp(userApp);
			followDao.save(follow);

			// 设置被关注为相互关注
			if (followed != null) {
				followed.setMutual(true);
			}
			
			// 当前用户应用关注数 + 1
			userApp.setFollowCount(userApp.getFollowCount() + 1);
			
			// 被关注用户应用粉丝数 + 1
			followedUserApp.setFollowerCount(followedUserApp.getFollowerCount() + 1);

			return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response removeLoggedUserAppFollow(int userAppId, String accessToken, int followedUserAppId) {
		try {
			// 验证必填参数
			if (followedUserAppId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId);
			
			// 根据被关注用户应用ID获取被关注用户应用
			UserApp followedUserApp = userAppDao.loadById(followedUserAppId);
			// 如果不存在
			if (followedUserApp == null || !followedUserApp.getApp().getAppId().equals(userApp.getApp().getAppId())) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.用户应用不存在.toJson()).build();
			}
			
			// 查询关注
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("followedUserApp", new QueryField("userAppId", followedUserAppId)));
			List<Follow> follows = followDao.loadPage(predicateQueryFieldList, null, 0, 1);
			Follow follow = follows.size() > 0 ? follows.get(0) : null;
			// 如果不存在
			if (follow == null) {
				return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
			}

			// 获取被关注
			predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", followedUserAppId)));
			predicateQueryFieldList.add(new QueryField("followedUserApp", new QueryField("userAppId", userAppId)));
			List<Follow> followedsList = followDao.loadPage(predicateQueryFieldList, null, 0, 1);
			Follow followed = followedsList.size() > 0 ? followedsList.get(0) : null;

			// 删除关注
			followDao.delete(follow);

			// 设置被关注为未相互关注
			if (followed != null) {
				followed.setMutual(false);
			}

			// 用户应用关注数 - 1
			userApp.setFollowCount(Math.max(userApp.getFollowCount() - 1, 0));
			
			// 被关注用户应用粉丝数 - 1
			followedUserApp.setFollowerCount(Math.max(followedUserApp.getFollowerCount() - 1, 0));

			return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();		
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response getLoggedUserAppArticleFavorites(int userAppId, String accessToken, int page) {
		try {
			// 验证必填参数
			if (page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 查询文章收藏列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Favorite.ContentTypeEnum.文章.ordinal()));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("createTime", false));
			List<Favorite> favorites = favoriteDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);
			// 如果无结果
			if (favorites.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			// 查询收藏对应的首发分发列表
			List<Integer> articleIds = new ArrayList<Integer>();
			for (Favorite favorite : favorites) {
				articleIds.add(favorite.getContentId());
			}
			if (articleIds.size() > 0) {				
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", articleIds, PredicateEnum.IN)));
				predicateQueryFieldList.add(new QueryField("column", new QueryField("enabled", true)));
				predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
				predicateQueryFieldList.add(new QueryField("firstPublished", true));
				List<Distribution> distributions = distributionDao.loadAll(predicateQueryFieldList, null);
				// 将首发分发存入收藏
				List<Favorite> favoritesToRemove = new ArrayList<Favorite>();
				for (Favorite favorite : favorites) {
					for (Distribution distribution : distributions) {
						if (favorite.getContentId().equals(distribution.getArticle().getArticleId())) {
							// 所属栏目ID
							distribution.setColumnId(distribution.getColumn().getColumnId());
							
							// 包含缩略图列表
							distribution.getThumbnails().iterator();
							
							favorite.setDistribution(distribution);
							break;
						}
					}
					if (favorite.getDistribution() == null) {
						favoritesToRemove.add(favorite);
					}
				}
				favorites.removeAll(favoritesToRemove);
			}

			return Response.status(Response.Status.OK).entity(favorites).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}	

	@Override
	public Response addLoggedUserAppArticleFavorite(int userAppId, String accessToken, int articleId) {
		try {			
			// 验证必填参数
			if (articleId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId);
			// 如果不存在
			if (article == null || !article.getPublished() || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			// 查询文章收藏
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Favorite.ContentTypeEnum.文章.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", articleId));
			long favoritesCount = favoriteDao.count(predicateQueryFieldList);
			// 如果已存在
			if (favoritesCount > 0) {
				return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
			}

			// 添加收藏
			Favorite favorite = new Favorite();
			favorite.setContentType(Favorite.ContentTypeEnum.文章.ordinal());
			favorite.setContentId(articleId);
			favorite.setCreateTimeDate(new Date());
			UserApp userApp = new UserApp();
			userApp.setUserAppId(userAppId);
			favorite.setUserApp(userApp);
			favoriteDao.save(favorite);

			// 文章获得收藏数 + 1
			article.setFavoriteCount(article.getFavoriteCount() + 1);
			
			return Response.status(Response.Status.OK).entity(favorite).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response removeLoggedUserAppArticleFavorite(int userAppId, String accessToken, int articleId) {
		try {			
			// 验证必填参数
			if (articleId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId);
			// 如果不存在
			if (article == null || !article.getPublished() || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			// 查询文章收藏
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Favorite.ContentTypeEnum.文章.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", articleId));
			List<Favorite> favorites = favoriteDao.loadPage(predicateQueryFieldList, null, 0, 1);
			Favorite favorite = favorites.size() > 0 ? favorites.get(0) : null;
			// 如果不存在
			if (favorite == null) {
				return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
			}

			// 删除文章收藏
			favoriteDao.delete(favorite);

			// 文章获得收藏数 - 1
			article.setFavoriteCount(Math.max(article.getFavoriteCount() - 1, 0));

			return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response addLoggedUserAppArticleLike(int userAppId, String accessToken, int articleId) {
		try {
			// 验证必填参数
			if (articleId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId);
			// 如果不存在
			if (article == null || !article.getPublished() || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			// 查询文章喜欢
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Like.ContentTypeEnum.文章.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", articleId));
			long likesCount = likeDao.count(predicateQueryFieldList);
			// 如果已存在
			if (likesCount > 0) {
				return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
			}

			// 添加喜欢
			Like like = new Like();
			like.setContentType(Like.ContentTypeEnum.文章.ordinal());
			like.setContentId(articleId);
			like.setCreateTimeDate(new Date());
			UserApp userApp = new UserApp();
			userApp.setUserAppId(userAppId);
			like.setUserApp(userApp);
			likeDao.save(like);

			// 文章获得喜欢数 + 1
			article.setLikeCount(article.getLikeCount() + 1);
			
			return Response.status(Response.Status.OK).entity(like).build();									
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response removeLoggedUserAppArticleLike(int userAppId, String accessToken, int articleId) {
		try {
			// 验证必填参数
			if (articleId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId);
			// 如果不存在
			if (article == null || !article.getPublished() || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			// 查询文章喜欢
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Like.ContentTypeEnum.文章.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", articleId));
			List<Like> likes = likeDao.loadPage(predicateQueryFieldList, null, 0, 1);
			Like like = likes.size() > 0 ? likes.get(0) : null;
			// 如果不存在
			if (like == null) {
				return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
			}

			// 删除文章喜欢
			likeDao.delete(like);

			// 文章获得喜欢数 - 1
			article.setLikeCount(Math.max(article.getLikeCount() - 1, 0));
			
			return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response addLoggedUserAppArticleDislike(int userAppId, String accessToken, int articleId) {
		try {
			// 验证必填参数
			if (articleId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId);
			// 如果不存在
			if (article == null || !article.getPublished() || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			// 查询文章不喜欢
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Dislike.ContentTypeEnum.文章.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", articleId));
			long dislikesCount = dislikeDao.count(predicateQueryFieldList);
			// 如果已存在
			if (dislikesCount > 0) {
				return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
			}

			// 添加不喜欢
			Dislike dislike = new Dislike();
			dislike.setContentType(Dislike.ContentTypeEnum.文章.ordinal());
			dislike.setContentId(articleId);
			dislike.setCreateTimeDate(new Date());
			UserApp userApp = new UserApp();
			userApp.setUserAppId(userAppId);
			dislike.setUserApp(userApp);
			dislikeDao.save(dislike);

			// 文章获得不喜欢数 + 1
			article.setDislikeCount(article.getDislikeCount() + 1);
			
			return Response.status(Response.Status.OK).entity(dislike).build();									
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response removeLoggedUserAppArticleDislike(int userAppId, String accessToken, int articleId) {
		try {
			// 验证必填参数
			if (articleId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId);
			// 如果不存在
			if (article == null || !article.getPublished() || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			// 查询文章不喜欢
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Dislike.ContentTypeEnum.文章.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", articleId));
			List<Dislike> dislikes = dislikeDao.loadPage(predicateQueryFieldList, null, 0, 1);
			Dislike dislike = dislikes.size() > 0 ? dislikes.get(0) : null;
			// 如果不存在
			if (dislike == null) {
				return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
			}

			// 删除文章不喜欢
			dislikeDao.delete(dislike);

			// 文章获得不喜欢数 - 1
			article.setDislikeCount(Math.max(article.getDislikeCount() - 1, 0));
			
			return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}
	
	@Override
	public Response addLoggedUserAppArticleComment(int userAppId, String accessToken, int articleId, String content) {
		try {
			// 验证必填参数
			if (articleId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			verifyRequired(content);

			// 验证格式
			if (content.length() > 3600) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.格式错误.toJson()).build();
			}

			// 根据文章ID获取文章
			Article article = articleDao.loadById(articleId);
			// 如果不存在
			if (article == null || !article.getPublished() || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			// 查询文章的评论的最大楼层数
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("contentType", Comment.ContentTypeEnum.文章.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", article.getArticleId()));
			Integer maxFloorNumber = commentDao.max("floorNumber", predicateQueryFieldList);

			// 添加评论
			Comment comment = new Comment();
			comment.setContent(content.trim());
			comment.setContentType(Comment.ContentTypeEnum.文章.ordinal());
			comment.setContentId(articleId);
			comment.setFloorNumber(maxFloorNumber == null ? 1 : maxFloorNumber.intValue() + 1);
			comment.setStatus(article.getNewsSource().getModerateEnabled() ? Comment.StatusEnum.待审核.ordinal() : Comment.StatusEnum.已发布.ordinal());				
			comment.setCreateTimeDate(new Date());
			comment.setReplyCount(0);
			comment.setLikeCount(0);
			UserApp userApp = new UserApp();
			userApp.setUserAppId(userAppId);
			comment.setUserApp(userApp);
			commentDao.save(comment);
			
			// 文章获得评论数 + 1
			article.setCommentCount(article.getCommentCount() + 1);
			
			// 设置所属的用户应用ID
			comment.setUserAppId(comment.getUserApp().getUserAppId());

			return Response.status(Response.Status.OK).entity(comment).build();
		} catch (ApiException e) {
			return e.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response removeLoggedUserAppArticleComment(int userAppId, String accessToken, int commentId) {
		try {
			// 验证必填参数
			if (commentId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据评论ID获取评论
			Comment comment = commentDao.loadById(commentId);
			// 如果不存在
			if (comment == null || comment.getContentTypeEnum() != Comment.ContentTypeEnum.文章 || comment.getUserApp().getUserAppId() != userAppId) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.评论不存在.toJson()).build();
			}

			// 根据关联内容ID获取文章
			Article article = articleDao.loadById(comment.getContentId());
			// 如果不存在
			if (article == null || !article.getPublished() || !article.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
			}

			// 设置评论状态为用户已删除
			comment.setStatus(Comment.StatusEnum.用户已删除.ordinal());

			// 文章获得评论数 - 1
			article.setCommentCount(Math.max(article.getCommentCount() - 1, 0));

			// 文章获得回复数 - 评论的回复数
			article.setReplyCount(Math.max(article.getReplyCount() - comment.getReplyCount(), 0));

			return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.Public.class)
	public Response addLoggedUserAppCommentReply(int userAppId, String accessToken, int commentId, String content) {
		try {
			// 验证必填参数
			if (commentId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			verifyRequired(content);

			// 验证格式
			if (content.length() > 1200) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.格式错误.toJson()).build();
			}

			// 根据评论ID获取评论
			Comment comment = commentDao.loadById(commentId);
			// 如果不存在
			if (comment == null || comment.getStatusEnum() != Comment.StatusEnum.已发布) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.评论不存在.toJson()).build();
			}

			Object commentContent = null;
			Boolean moderateEnabled = null;
			if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
				// 根据关联内容ID获取文章
				Article article = articleDao.loadById(comment.getContentId());
				// 如果不存在
				if (article == null || !article.getPublished() || !article.getNewsSource().getEnabled()) {
					return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
				}
				commentContent = article;
				moderateEnabled = article.getNewsSource().getModerateEnabled();
			}

			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId);
			// 如果不存在
			if (!comment.getUserApp().getApp().getAppId().equals(userApp.getApp().getAppId())) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.用户应用不存在.toJson()).build();
			}

			// 添加回复
			Reply reply = new Reply();
			reply.setContent(content.trim());
			reply.setReplyUserAppId(comment.getUserApp().getUserAppId());
			reply.setStatus(moderateEnabled ? Comment.StatusEnum.待审核.ordinal() : Comment.StatusEnum.已发布.ordinal());				
			reply.setCreateTimeDate(new Date());
			reply.setComment(comment);
			reply.setUserApp(userApp);
			replyDao.save(reply);

			// 评论获得回复数 + 1
			comment.setReplyCount(comment.getReplyCount() + 1);
			
			if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
				// 文章获得回复数 + 1
				Article article = (Article) commentContent;
				article.setReplyCount(article.getReplyCount() + 1);
			}
			
			// 创建日志
			Log log = new Log();
			log.setTitle("用户 " + userApp.getUser().getNickName() + " 回复了你的评论");
			log.setContentType(Log.ContentTypeEnum.回复.ordinal());
			log.setContentId(reply.getReplyId());
			log.setCreateTimeDate(new Date());
			log.setActiveUserApp(userApp);
			log.setUserApp(comment.getUserApp());
			logDao.save(log);
			
			comment.setReplies(null);

			return Response.status(Response.Status.OK).entity(reply).build();					
		} catch (ApiException e) {
			return e.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.Public.class)
	public Response addLoggedUserAppReplyReply(int userAppId, String accessToken, int commentId, String content, int replyUserAppId) {
		try {
			// 验证必填参数
			if (commentId == 0 || replyUserAppId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			verifyRequired(content);

			// 验证格式
			if (content.length() > 1200) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.格式错误.toJson()).build();
			}

			// 根据评论ID获取评论
			Comment comment = commentDao.loadById(commentId);
			// 如果不存在
			if (comment == null || comment.getStatusEnum() != Comment.StatusEnum.已发布) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.评论不存在.toJson()).build();
			}

			Object commentContent = null;
			Boolean moderateEnabled = null;
			if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
				// 根据关联内容ID获取文章
				Article article = articleDao.loadById(comment.getContentId());
				// 如果不存在
				if (article == null || !article.getPublished() || !article.getNewsSource().getEnabled()) {
					return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
				}
				commentContent = article;
				moderateEnabled = article.getNewsSource().getModerateEnabled();
			}

			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId);

			// 根据回复用户应用ID获取回复用户应用
			UserApp replyUserApp = userAppDao.loadById(replyUserAppId);
			// 如果不存在
			if (replyUserApp == null || !replyUserApp.getApp().getAppId().equals(userApp.getApp().getAppId())) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.用户应用不存在.toJson()).build();
			}

			// 添加回复
			Reply reply = new Reply();
			reply.setContent(content.trim());
			reply.setReplyUserAppId(replyUserAppId);
			reply.setStatus(moderateEnabled ? Comment.StatusEnum.待审核.ordinal() : Comment.StatusEnum.已发布.ordinal());				
			reply.setCreateTimeDate(new Date());
			reply.setComment(comment);
			reply.setUserApp(userApp);
			replyDao.save(reply);

			// 评论获得回复数 + 1
			comment.setReplyCount(comment.getReplyCount() + 1);
			
			if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
				// 文章获得回复数 + 1
				Article article = (Article) commentContent;
				article.setReplyCount(article.getReplyCount() + 1);
			}
			
			// 创建回复评论用户应用日志
			Log log = new Log();
			log.setTitle("用户 " + userApp.getUser().getNickName() + " 回复了你的评论");
			log.setContentType(Log.ContentTypeEnum.回复.ordinal());
			log.setContentId(reply.getReplyId());
			log.setCreateTimeDate(new Date());
			log.setActiveUserApp(userApp);
			log.setUserApp(comment.getUserApp());
			logDao.save(log);
			
			// 创建回复回复用户应用日志
			if (!comment.getUserApp().getUserAppId().equals(replyUserApp.getUserAppId())) {
				log = new Log();
				log.setTitle("用户 " + userApp.getUser().getNickName() + " 回复了你的回复");
				log.setContentType(Log.ContentTypeEnum.回复.ordinal());
				log.setContentId(reply.getReplyId());
				log.setCreateTimeDate(new Date());
				log.setActiveUserApp(userApp);
				log.setUserApp(replyUserApp);
				logDao.save(log);
			}
			
			comment.setReplies(null);

			return Response.status(Response.Status.OK).entity(reply).build();					
		} catch (ApiException e) {
			return e.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}
	
	@Override
	public Response removeLoggedUserAppReply(int userAppId, String accessToken, int replyId) {
		try {
			// 验证必填参数
			if (replyId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据回复ID获取回复
			Reply reply = replyDao.loadById(replyId);
			// 如果不存在
			if (reply == null || reply.getUserApp().getUserAppId() != userAppId || reply.getComment().getStatusEnum() != Comment.StatusEnum.已发布) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.评论不存在.toJson()).build();
			}

			Object commentContent = null;
			if (reply.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
				// 根据关联内容ID获取文章
				Article article = articleDao.loadById(reply.getComment().getContentId());
				// 如果不存在
				if (article == null || !article.getPublished() || !article.getNewsSource().getEnabled()) {
					return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.文章不存在.toJson()).build();
				}
				commentContent = article;
			}

			// 设置回复状态为用户已删除
			reply.setStatus(Reply.StatusEnum.用户已删除.ordinal());

			// 评论获得回复数 - 1
			reply.getComment().setReplyCount(Math.max(reply.getComment().getReplyCount() - 1, 0));
			
			if (reply.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
				// 文章获得回复数 - 1
				Article article = (Article) commentContent;
				article.setReplyCount(Math.max(article.getReplyCount() - 1, 0));
			}

			return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response addLoggedUserAppCommentLike(int userAppId, String accessToken, int commentId) {
		try {
			// 验证必填参数
			if (commentId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据评论ID获取评论
			Comment comment = commentDao.loadById(commentId);
			// 如果不存在
			if (comment == null || comment.getStatusEnum() != Comment.StatusEnum.已发布) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.评论不存在.toJson()).build();
			}

			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId);
			// 如果不存在
			if (!comment.getUserApp().getApp().getAppId().equals(userApp.getApp().getAppId())) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.用户应用不存在.toJson()).build();
			}

			// 查询评论喜欢
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Like.ContentTypeEnum.评论.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", commentId));
			long likesCount = likeDao.count(predicateQueryFieldList);
			// 如果已存在
			if (likesCount > 0) {
				return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
			}

			// 添加喜欢
			Like like = new Like();
			like.setContentType(Like.ContentTypeEnum.评论.ordinal());
			like.setContentId(commentId);
			like.setCreateTimeDate(new Date());
			like.setUserApp(userApp);
			likeDao.save(like);

			// 评论获得喜欢数 + 1
			comment.setLikeCount(comment.getLikeCount() + 1);
			
			// 创建日志
			Log log = new Log();
			log.setTitle("用户 " + userApp.getUser().getNickName() + " 赞了你的评论");
			log.setContentType(Log.ContentTypeEnum.喜欢.ordinal());
			log.setContentId(like.getLikeId());
			log.setCreateTimeDate(new Date());
			log.setActiveUserApp(userApp);
			log.setUserApp(comment.getUserApp());
			logDao.save(log);

			return Response.status(Response.Status.OK).entity(like).build();									
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response removeLoggedUserAppCommentLike(int userAppId, String accessToken, int commentId) {
		try {
			// 验证必填参数
			if (commentId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据评论ID获取评论
			Comment comment = commentDao.loadById(commentId);
			// 如果不存在
			if (comment == null || comment.getStatusEnum() != Comment.StatusEnum.已发布) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.评论不存在.toJson()).build();
			}

			// 查询评论喜欢
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Like.ContentTypeEnum.评论.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", commentId));
			List<Like> likes = likeDao.loadPage(predicateQueryFieldList, null, 0, 1);
			Like like = likes.size() > 0 ? likes.get(0) : null;
			// 如果不存在
			if (like == null) {
				return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
			}

			// 删除评论喜欢
			likeDao.delete(like);

			// 评论获得喜欢数 - 1
			comment.setLikeCount(Math.max(comment.getLikeCount() - 1, 0));
			
			return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response getUserAppComments(int userAppId, int page, int loggedUserAppId, String loggedAccessToken) {
		try {
			// 验证必填参数
			if (page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			boolean logged = loggedUserAppId > 0 && loggedAccessToken != null && !loggedAccessToken.isEmpty();
			boolean userAppLogged = logged && userAppId == loggedUserAppId;
			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId);
			// 如果用户应用不是动态可见或已登录
			if (!(userApp.getIsRecentVisible() || userAppLogged)) {
				return Response.status(Response.Status.OK).entity(new ArrayList<Comment>()).build();
			}

			// 查询评论列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			if (userAppLogged) {
				// 如果用户应用已登录
				List<Integer> statusList = new ArrayList<>();
				statusList.add(Comment.StatusEnum.待审核.ordinal());
				statusList.add(Comment.StatusEnum.已发布.ordinal());
				statusList.add(Comment.StatusEnum.审核未通过.ordinal());
				predicateQueryFieldList.add(new QueryField("status", statusList, PredicateEnum.IN));			
			} else {
				predicateQueryFieldList.add(new QueryField("status", Comment.StatusEnum.已发布.ordinal()));
			}
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("createTime", false));
			List<Comment> comments = commentDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);
			// 如果无结果
			if (comments.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			// 查询评论对应的首发分发列表
			List<Integer> articleIds = new ArrayList<Integer>();
			for (Comment comment : comments) {
				// 设置所属的用户应用ID
				comment.setUserAppId(comment.getUserApp().getUserAppId());
				
				commentDao.getEntityManager().detach(comment);
				comment.setReplies(null);
				
				if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
					articleIds.add(comment.getContentId());
				}
			}
			if (articleIds.size() > 0) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", articleIds, PredicateEnum.IN)));
				predicateQueryFieldList.add(new QueryField("column", new QueryField("enabled", true)));
				predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
				predicateQueryFieldList.add(new QueryField("firstPublished", true));
				List<Distribution> distributions = distributionDao.loadAll(predicateQueryFieldList, null);
				// 将首发分发存入评论
				List<Comment> commentsToRemove = new ArrayList<Comment>();
				for (Comment comment : comments) {
					for (Distribution distribution : distributions) {
						if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章 && comment.getContentId().equals(distribution.getArticle().getArticleId())) {
							// 所属栏目ID
							distribution.setColumnId(distribution.getColumn().getColumnId());

							// 包含缩略图列表
							distribution.getThumbnails().iterator();
							
							comment.setDistribution(distribution);
							break;
						}
					}
					if (comment.getContentTypeEnum() == Comment.ContentTypeEnum.文章 && comment.getDistribution() == null) {
						commentsToRemove.add(comment);
					}
				}
				comments.removeAll(commentsToRemove);
			}
			
			// 如果已登录
			if (logged) {
				// 查询评论对应的已登录评论喜欢列表
				List<Integer> commentIds = new ArrayList<Integer>();
				for (Comment comment : comments) {
					commentIds.add(comment.getCommentId());
				}
				if (commentIds.size() > 0) {
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", loggedUserAppId)));
					predicateQueryFieldList.add(new QueryField("contentType", Like.ContentTypeEnum.评论.ordinal()));
					predicateQueryFieldList.add(new QueryField("contentId", commentIds, PredicateEnum.IN));
					List<Like> loggedLikes = likeDao.loadAll(predicateQueryFieldList, null);
					// 将喜欢存入评论
					for (Comment comment : comments) {
						for (Like loggedLike : loggedLikes) {
							if (comment.getCommentId().equals(loggedLike.getContentId())) {
								comment.setLike(loggedLike);
								break;
							}
						}
					}
				}
			}			

			return Response.status(Response.Status.OK).entity(comments).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response getUserAppReplies(int userAppId, int page, int loggedUserAppId, String loggedAccessToken) {
		try {
			// 验证必填参数
			if (page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			boolean logged = loggedUserAppId > 0 && loggedAccessToken != null && !loggedAccessToken.isEmpty();
			boolean userAppLogged = logged && userAppId == loggedUserAppId;
			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId);
			// 如果用户应用不是动态可见或已登录
			if (!(userApp.getIsRecentVisible() || userAppLogged)) {
				return Response.status(Response.Status.OK).entity(new ArrayList<Reply>()).build();
			}
			
			// 查询回复列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			if (userAppLogged) {
				// 如果用户应用已登录
				List<Integer> statusList = new ArrayList<>();
				statusList.add(Reply.StatusEnum.待审核.ordinal());
				statusList.add(Reply.StatusEnum.已发布.ordinal());
				statusList.add(Reply.StatusEnum.审核未通过.ordinal());
				predicateQueryFieldList.add(new QueryField("status", statusList, PredicateEnum.IN));					
			} else {
				predicateQueryFieldList.add(new QueryField("status", Reply.StatusEnum.已发布.ordinal()));									
				predicateQueryFieldList.add(new QueryField("comment", new QueryField("status", Comment.StatusEnum.已发布.ordinal())));
			}
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("createTime", false));
			List<Reply> replies = replyDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);
			// 如果无结果
			if (replies.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			List<Integer> articleIds = new ArrayList<Integer>();
			for (Reply reply : replies) {
				// 设置所属的用户应用ID
				reply.setUserAppId(reply.getUserApp().getUserAppId());
				reply.getComment().setUserAppId(reply.getComment().getUserApp().getUserAppId());
				
				commentDao.getEntityManager().detach(reply.getComment());
				reply.getComment().setReplies(null);
				
				if (reply.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
					articleIds.add(reply.getComment().getContentId());
				}
			}
			// 查询回复所属评论对应的首发分发列表
			if (articleIds.size() > 0) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", articleIds, PredicateEnum.IN)));
				predicateQueryFieldList.add(new QueryField("column", new QueryField("enabled", true)));
				predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
				predicateQueryFieldList.add(new QueryField("firstPublished", true));
				List<Distribution> distributions = distributionDao.loadAll(predicateQueryFieldList, null);
				// 将首发分发存入回复所属评论
				List<Reply> repliesToRemove = new ArrayList<Reply>();
				for (Reply reply : replies) {
					for (Distribution distribution : distributions) {
						if (reply.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章 && reply.getComment().getContentId().equals(distribution.getArticle().getArticleId())) {
							// 所属栏目ID
							distribution.setColumnId(distribution.getColumn().getColumnId());

							// 包含缩略图列表
							distribution.getThumbnails().iterator();
							
							reply.getComment().setDistribution(distribution);
							break;
						}
					}
					if (reply.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章 && reply.getComment().getDistribution() == null) {
						repliesToRemove.add(reply);
					}
				}
				replies.removeAll(repliesToRemove);
			}
			
			// 如果已登录
			if (logged) {
				// 查询评论对应的已登录评论喜欢列表
				List<Integer> commentIds = new ArrayList<Integer>();
				for (Reply reply : replies) {
					commentIds.add(reply.getComment().getCommentId());
				}
				if (commentIds.size() > 0) {
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", loggedUserAppId)));
					predicateQueryFieldList.add(new QueryField("contentType", Like.ContentTypeEnum.评论.ordinal()));
					predicateQueryFieldList.add(new QueryField("contentId", commentIds, PredicateEnum.IN));
					List<Like> loggedLikes = likeDao.loadAll(predicateQueryFieldList, null);
					// 将喜欢存入评论
					for (Reply reply : replies) {
						for (Like loggedLike : loggedLikes) {
							if (reply.getComment().getCommentId().equals(loggedLike.getContentId())) {
								reply.getComment().setLike(loggedLike);
								break;
							}
						}
					}
				}
			}			

			return Response.status(Response.Status.OK).entity(replies).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response getUserAppLikes(int userAppId, int page, int loggedUserAppId, String loggedAccessToken) {
		try {
			// 验证必填参数
			if (page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			boolean logged = loggedUserAppId > 0 && loggedAccessToken != null && !loggedAccessToken.isEmpty();
			boolean userAppLogged = logged && userAppId == loggedUserAppId;
			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId);
			// 如果用户应用不是动态可见或已登录
			if (!(userApp.getIsRecentVisible() || userAppLogged)) {
				return Response.status(Response.Status.OK).entity(new ArrayList<Like>()).build();
			}

			// 查询喜欢列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("createTime", false));
			List<Like> likes = likeDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);			
			// 如果无结果
			if (likes.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			// 查询喜欢关联的内容列表
			List<Integer> articleIds = new ArrayList<Integer>();
			List<Integer> commentIds = new ArrayList<Integer>();
			for (Like like : likes) {
				if (like.getContentTypeEnum() == Like.ContentTypeEnum.文章) {
					articleIds.add(like.getContentId());
				}
				if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论) {
					commentIds.add(like.getContentId());
				}
				likeDao.getEntityManager().detach(like);
			}
			// 处理文章喜欢
			if (articleIds.size() > 0) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", articleIds, PredicateEnum.IN)));
				predicateQueryFieldList.add(new QueryField("column", new QueryField("enabled", true)));
				predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
				predicateQueryFieldList.add(new QueryField("firstPublished", true));
				List<Distribution> distributions = distributionDao.loadAll(predicateQueryFieldList, null);
				// 将首发分发存入喜欢
				List<Like> likesToRemove = new ArrayList<Like>();
				for (Like like : likes) {
					for (Distribution distribution : distributions) {
						if (like.getContentTypeEnum() == Like.ContentTypeEnum.文章 && like.getContentId().equals(distribution.getArticle().getArticleId())) {
							// 所属栏目ID
							distribution.setColumnId(distribution.getColumn().getColumnId());

							// 包含缩略图列表
							distribution.getThumbnails().iterator();
							
							like.setDistribution(distribution);
							break;
						}
					}
					if (like.getContentTypeEnum() == Like.ContentTypeEnum.文章 && like.getDistribution() == null) {
						likesToRemove.add(like);
					}
				}
				likes.removeAll(likesToRemove);
			}
			// 处理评论喜欢
			if (commentIds.size() > 0) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("commentId", commentIds, PredicateEnum.IN));
				predicateQueryFieldList.add(new QueryField("status", Comment.StatusEnum.已发布.ordinal()));
				List<Comment> comments = commentDao.loadAll(predicateQueryFieldList, null);
				// 将评论存入喜欢
				List<Like> likesToRemove = new ArrayList<Like>();
				for (Like like : likes) {
					for (Comment comment : comments) {
						// 设置所属的用户应用ID
						comment.setUserAppId(comment.getUserApp().getUserAppId());
						
						if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论 && like.getContentId().equals(comment.getCommentId())) {
							like.setComment(comment);
							commentDao.getEntityManager().detach(comment);
							comment.setReplies(null);
							break;
						}
					}
					if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论 && like.getComment() == null) {
						likesToRemove.add(like);
					}
				}
				likes.removeAll(likesToRemove);
				
				// 查询评论喜欢对应的首发分发列表
				articleIds = new ArrayList<Integer>();
				for (Like like : likes) {
					if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论 && like.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
						articleIds.add(like.getComment().getContentId());
					}
				}
				if (articleIds.size() > 0) {
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", articleIds, PredicateEnum.IN)));
					predicateQueryFieldList.add(new QueryField("column", new QueryField("enabled", true)));
					predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
					predicateQueryFieldList.add(new QueryField("firstPublished", true));
					List<Distribution> distributions = distributionDao.loadAll(predicateQueryFieldList, null);
					// 将首发分发存入评论喜欢
					likesToRemove = new ArrayList<Like>();
					for (Like like : likes) {
						for (Distribution distribution : distributions) {
							if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论 && like.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章 && like.getComment().getContentId().equals(distribution.getArticle().getArticleId())) {
								// 所属栏目ID
								distribution.setColumnId(distribution.getColumn().getColumnId());

								// 包含缩略图列表
								distribution.getThumbnails().iterator();
								
								like.getComment().setDistribution(distribution);
								break;
							}
						}
						if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论 && like.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章 && like.getComment().getDistribution() == null) {
							likesToRemove.add(like);
						}
					}
					likes.removeAll(likesToRemove);
				}
			}
			
			// 如果已登录
			if (logged) {
				// 查询评论对应的已登录评论喜欢列表
				commentIds = new ArrayList<Integer>();
				for (Like like : likes) {
					if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论) {
						commentIds.add(like.getContentId());
					}
				}
				// 处理已登录评论喜欢
				if (commentIds.size() > 0) {
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", loggedUserAppId)));
					predicateQueryFieldList.add(new QueryField("contentType", Like.ContentTypeEnum.评论.ordinal()));
					predicateQueryFieldList.add(new QueryField("contentId", commentIds, PredicateEnum.IN));
					List<Like> loggedLikes = likeDao.loadAll(predicateQueryFieldList, null);
					// 将喜欢存入评论
					for (Like like : likes) {
						for (Like loggedLike : loggedLikes) {
							if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论 && like.getContentId().equals(loggedLike.getContentId())) {
								like.getComment().setLike(loggedLike);
								break;
							}
						}
					}
				}
			}			
			
			return Response.status(Response.Status.OK).entity(likes).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response addLoggedUserAppCommentReport(int userAppId, String accessToken, int commentId, int type) {
		try {
			// 验证必填参数
			if (commentId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据评论ID获取评论
			Comment comment = commentDao.loadById(commentId);
			// 如果不存在
			if (comment == null || comment.getStatusEnum() != Comment.StatusEnum.已发布) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.评论不存在.toJson()).build();
			}

			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId);
			// 如果不存在
			if (!comment.getUserApp().getApp().getAppId().equals(userApp.getApp().getAppId())) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.用户应用不存在.toJson()).build();
			}

			// 查询评论举报
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Report.ContentTypeEnum.评论.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", commentId));
			List<Report> reports = reportDao.loadAll(predicateQueryFieldList, null);
			Report report = reports.size() > 0 ? reports.get(0) : null;
			// 如果不存在，则添加举报
			if (report == null) {
				report = new Report();
				report.setType(type);
				report.setContentType(Report.ContentTypeEnum.评论.ordinal());
				report.setContentId(commentId);
				report.setStatus(Report.StatusEnum.待审核.ordinal());
				report.setCreateTimeDate(new Date());
				report.setUserApp(userApp);
				reportDao.save(report);
			}
			
			return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response addLoggedUserAppReplyReport(int userAppId, String accessToken, int replyId, int type) {
		try {
			// 验证必填参数
			if (replyId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 根据回复ID获取回复
			Reply reply = replyDao.loadById(replyId);
			// 如果不存在
			if (reply == null || reply.getStatusEnum() != Reply.StatusEnum.已发布 || reply.getComment().getStatusEnum() != Comment.StatusEnum.已发布) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.评论不存在.toJson()).build();
			}

			// 根据用户应用ID获取用户应用
			UserApp userApp = userAppDao.loadById(userAppId);
			// 如果不存在
			if (!reply.getUserApp().getApp().getAppId().equals(userApp.getApp().getAppId())) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.用户应用不存在.toJson()).build();
			}

			// 查询回复举报
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userApp.getUserAppId())));
			predicateQueryFieldList.add(new QueryField("contentType", Report.ContentTypeEnum.回复.ordinal()));
			predicateQueryFieldList.add(new QueryField("contentId", replyId));
			List<Report> reports = reportDao.loadAll(predicateQueryFieldList, null);
			Report report = reports.size() > 0 ? reports.get(0) : null;
			// 如果不存在，则添加举报
			if (report == null) {
				report = new Report();
				report.setType(type);
				report.setContentType(Report.ContentTypeEnum.回复.ordinal());
				report.setContentId(replyId);
				report.setCreateTimeDate(new Date());
				report.setStatus(Report.StatusEnum.待审核.ordinal());
				report.setUserApp(userApp);
				reportDao.save(report);
			}
			
			return Response.status(Response.Status.OK).entity(ErrorMessage.成功.toJson()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response getLoggedUserAppSystemLogs(int userAppId, String accessToken, int page) {
		try {
			// 验证必填参数
			if (page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 查询系统日志
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Log.ContentTypeEnum.系统.ordinal()));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("createTime", false));
			List<Log> logs = logDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);
			// 如果无结果
			if (logs.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			
			return Response.status(Response.Status.OK).entity(logs).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response getLoggedUserAppReplyLogs(int userAppId, String accessToken, int page) {
		try {
			// 验证必填参数
			if (page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 查询回复日志
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Log.ContentTypeEnum.回复.ordinal()));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("createTime", false));
			List<Log> logs = logDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);
			// 如果无结果
			if (logs.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			// 查询日志关联的回复列表
			List<Integer> replyIds = new ArrayList<Integer>();
			for (Log log : logs) {
				replyIds.add(log.getContentId());
			}
			List<Reply> repliesToReturn = new ArrayList<Reply>();
			if (replyIds.size() > 0) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("replyId", replyIds, PredicateEnum.IN));
				predicateQueryFieldList.add(new QueryField("status", Reply.StatusEnum.已发布.ordinal()));				
				predicateQueryFieldList.add(new QueryField("comment", new QueryField("status", Comment.StatusEnum.已发布.ordinal())));
				predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId, PredicateEnum.NOT_EQUAL)));
				List<Reply> replies = replyDao.loadAll(predicateQueryFieldList, null);				
				for (Log log : logs) {
					for (Reply reply : replies) {
						if (log.getContentId().equals(reply.getReplyId())) {
							// 设置所属的用户应用ID
							reply.setUserAppId(reply.getUserApp().getUserAppId());
							reply.getComment().setUserAppId(reply.getComment().getUserApp().getUserAppId());
							
							commentDao.getEntityManager().detach(reply.getComment());
							reply.getComment().setReplies(null);

							repliesToReturn.add(reply);
							break;
						}
					}
				}
			}
			// 查询回复所属评论对应的首发分发列表
			List<Integer> articleIds = new ArrayList<Integer>();
			for (Reply reply : repliesToReturn) {
				if (reply.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
					articleIds.add(reply.getComment().getContentId());
				}
			}
			if (articleIds.size() > 0) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", articleIds, PredicateEnum.IN)));
				predicateQueryFieldList.add(new QueryField("column", new QueryField("enabled", true)));
				predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
				predicateQueryFieldList.add(new QueryField("firstPublished", true));
				List<Distribution> distributions = distributionDao.loadAll(predicateQueryFieldList, null);
				// 将首发分发存入回复所属评论
				List<Reply> repliesToRemove = new ArrayList<Reply>();
				for (Reply reply : repliesToReturn) {
					for (Distribution distribution : distributions) {
						if (reply.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章 && reply.getComment().getContentId().equals(distribution.getArticle().getArticleId())) {
							// 所属栏目ID
							distribution.setColumnId(distribution.getColumn().getColumnId());

							// 包含缩略图列表
							distribution.getThumbnails().iterator();
							
							reply.getComment().setDistribution(distribution);
							break;
						}
					}
					if (reply.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章 && reply.getComment().getDistribution() == null) {
						repliesToRemove.add(reply);
					}
				}
				repliesToReturn.removeAll(repliesToRemove);
			}
			
			// 查询评论对应的已登录评论喜欢列表
			List<Integer> commentIds = new ArrayList<Integer>();
			for (Reply reply : repliesToReturn) {
				commentIds.add(reply.getComment().getCommentId());
			}
			if (commentIds.size() > 0) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
				predicateQueryFieldList.add(new QueryField("contentType", Like.ContentTypeEnum.评论.ordinal()));
				predicateQueryFieldList.add(new QueryField("contentId", commentIds, PredicateEnum.IN));
				List<Like> commentLikes = likeDao.loadAll(predicateQueryFieldList, null);
				// 将喜欢存入评论
				for (Reply reply : repliesToReturn) {
					for (Like commentLike : commentLikes) {
						if (reply.getComment().getCommentId().equals(commentLike.getContentId())) {
							reply.getComment().setLike(commentLike);
							break;
						}
					}
				}
			}
			
			return Response.status(Response.Status.OK).entity(repliesToReturn).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response getLoggedUserAppLikeLogs(int userAppId, String accessToken, int page) {
		try {
			// 验证必填参数
			if (page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}

			// 查询喜欢日志
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
			predicateQueryFieldList.add(new QueryField("contentType", Log.ContentTypeEnum.喜欢.ordinal()));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("createTime", false));
			List<Log> logs = logDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);
			// 如果无结果
			if (logs.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			// 查询日志关联的喜欢列表
			List<Integer> likeIds = new ArrayList<Integer>();
			for (Log log : logs) {
				likeIds.add(log.getContentId());
			}
			List<Like> likesToReturn = new ArrayList<Like>();
			if (likeIds.size() > 0) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("likeId", likeIds, PredicateEnum.IN));
				predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId, PredicateEnum.NOT_EQUAL)));
				List<Like> likes = likeDao.loadAll(predicateQueryFieldList, null);				
				for (Log log : logs) {
					for (Like like : likes) {
						if (log.getContentId().equals(like.getLikeId())) {
							// 设置所属的用户应用ID
							like.setUserAppId(like.getUserApp().getUserAppId());

							likesToReturn.add(like);
							break;
						}
					}
				}
			}
			
			// 查询喜欢关联的内容列表
			List<Integer> commentIds = new ArrayList<Integer>();
			for (Like like : likesToReturn) {
				if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论) {
					commentIds.add(like.getContentId());
				}
				likeDao.getEntityManager().detach(like);
			}
			// 处理评论喜欢
			if (commentIds.size() > 0) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("commentId", commentIds, PredicateEnum.IN));
				predicateQueryFieldList.add(new QueryField("status", Comment.StatusEnum.已发布.ordinal()));
				List<Comment> comments = commentDao.loadAll(predicateQueryFieldList, null);
				// 将评论存入喜欢
				List<Like> likesToRemove = new ArrayList<Like>();
				for (Like like : likesToReturn) {
					for (Comment comment : comments) {
						// 设置所属的用户应用ID
						comment.setUserAppId(comment.getUserApp().getUserAppId());
						
						if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论 && like.getContentId().equals(comment.getCommentId())) {
							like.setComment(comment);
							commentDao.getEntityManager().detach(comment);
							comment.setReplies(null);
							break;
						}
					}
					if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论 && like.getComment() == null) {
						likesToRemove.add(like);
					}
				}
				likesToReturn.removeAll(likesToRemove);
			}
			// 查询评论喜欢对应的首发分发列表
			List<Integer> articleIds = new ArrayList<Integer>();
			for (Like like : likesToReturn) {
				if (like.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章) {
					articleIds.add(like.getComment().getContentId());
				}
			}
			if (articleIds.size() > 0) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", articleIds, PredicateEnum.IN)));
				predicateQueryFieldList.add(new QueryField("column", new QueryField("enabled", true)));
				predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
				predicateQueryFieldList.add(new QueryField("firstPublished", true));
				List<Distribution> distributions = distributionDao.loadAll(predicateQueryFieldList, null);
				// 将首发分发存入评论喜欢
				List<Like> likesToRemove = new ArrayList<Like>();
				for (Like like : likesToReturn) {
					for (Distribution distribution : distributions) {
						if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论 && like.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章 && like.getComment().getContentId().equals(distribution.getArticle().getArticleId())) {
							// 所属栏目ID
							distribution.setColumnId(distribution.getColumn().getColumnId());

							// 包含缩略图列表
							distribution.getThumbnails().iterator();
							
							like.getComment().setDistribution(distribution);
							break;
						}
					}
					if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论 && like.getComment().getContentTypeEnum() == Comment.ContentTypeEnum.文章 && like.getComment().getDistribution() == null) {
						likesToRemove.add(like);
					}
				}
				likesToReturn.removeAll(likesToRemove);
			}
			
			// 查询评论对应的已登录评论喜欢列表
			commentIds = new ArrayList<Integer>();
			for (Like like : likesToReturn) {
				if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论) {
					commentIds.add(like.getContentId());
				}
			}
			// 处理已登录评论喜欢
			if (commentIds.size() > 0) {
				predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("userApp", new QueryField("userAppId", userAppId)));
				predicateQueryFieldList.add(new QueryField("contentType", Like.ContentTypeEnum.评论.ordinal()));
				predicateQueryFieldList.add(new QueryField("contentId", commentIds, PredicateEnum.IN));
				List<Like> commentLikes = likeDao.loadAll(predicateQueryFieldList, null);
				// 将喜欢存入评论
				for (Like like : likesToReturn) {
					for (Like commentLike : commentLikes) {
						if (like.getContentTypeEnum() == Like.ContentTypeEnum.评论 && like.getContentId().equals(commentLike.getContentId())) {
							like.getComment().setLike(commentLike);
							break;
						}
					}
				}
			}
			
			return Response.status(Response.Status.OK).entity(likesToReturn).build();
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
		应用不存在(2, "应用不存在"),
		应用不可用(3, "应用不可用"),
		用户应用不存在(4, "用户应用不存在"),
		格式错误(101, "格式错误"),
		数据异常(102, "数据异常"),
		数据唯一限制(103, "数据唯一限制"),
		操作受限(104, "操作受限"),
		操作超时(105, "操作超时"),
		操作失败(106, "操作失败"),
		列表结束(107, "列表结束"),
		文章不存在(201, "文章不存在"),
		评论不存在(201, "评论不存在");

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
