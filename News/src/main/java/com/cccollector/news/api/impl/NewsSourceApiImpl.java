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

import com.cccollector.news.api.NewsSourceApi;
import com.cccollector.news.dao.AppDao;
import com.cccollector.news.dao.CategoryTagDao;
import com.cccollector.news.dao.ColumnDao;
import com.cccollector.news.dao.DistributionDao;
import com.cccollector.news.dao.GlossaryDao;
import com.cccollector.news.dao.RecommendGroupDao;
import com.cccollector.news.model.App;
import com.cccollector.news.model.CategoryTag;
import com.cccollector.news.model.Column;
import com.cccollector.news.model.ColumnSubstitute;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.Glossary;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.Recommend;
import com.cccollector.news.model.RecommendGroup;
import com.cccollector.universal.app.model.UserDetail;
import com.cccollector.universal.cxf.ApiException;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.model.UniversalJsonViews;
import com.cccollector.universal.service.GenericServiceHibernateImpl;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 新闻源API实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("newsSourceApi")
public class NewsSourceApiImpl extends GenericServiceHibernateImpl<Integer, NewsSource> implements NewsSourceApi {

	@Autowired
	private AppDao appDao;

	@Autowired
	private ColumnDao columnDao;

	@Autowired
	private DistributionDao distributionDao;

	@Autowired
	private GlossaryDao glossaryDao;

	@Autowired
	private CategoryTagDao categoryTagDao;

	@Autowired
	private RecommendGroupDao recommendGroupDao;
	
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
	public Response findNewsSourcesByAppBundleId2(String appBundleId) {
		try {
			// 验证必填参数
			verifyRequired(appBundleId);

			// 根据应用标识符查询应用
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("bundleId", appBundleId));
			List<App> apps = appDao.loadAll(predicateQueryFieldList, null);
			App app = apps.isEmpty() ? null : apps.get(0);
			// 如果没有查询到
			if (app == null) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.应用不存在.toJson()).build();
			}
			// 如果应用不可用
			if (!app.getEnabled()) {
				return Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.应用不可用.toJson()).build();
			}

			// 关联的新闻源
			List<NewsSource> newsSources = new ArrayList<>();
			for (NewsSource newsSource : app.getNewsSources()) {
				if (newsSource.getEnabled()) {
					// 包含的样式
					newsSource.getStyles().entrySet();

					appDao.getEntityManager().detach(newsSource);
					newsSources.add(newsSource);
					
					// 包含的栏目
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSource.getNewsSourceId())));
					predicateQueryFieldList.add(new QueryField("enabled", true));
					List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
					orderQueryFieldList.add(new QueryField("code", Boolean.TRUE));
					List<Column> columns = columnDao.loadAll(predicateQueryFieldList, orderQueryFieldList);				
					for (Column column : columns) {
						// 所属父栏目ID
						if (column.getParentColumn() != null) {
							column.setParentColumnId(column.getParentColumn().getColumnId());
						}
						// 包含的分类标签ID
						for (CategoryTag categoryTag : column.getCategoryTags()) {
							column.getCategoryTagIds().add(categoryTag.getCategoryTagId());
						}
					}
					newsSource.setColumns(columns);				

					// 包含的术语
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSource.getNewsSourceId())));
					predicateQueryFieldList.add(new QueryField("enabled", true));
					orderQueryFieldList = new ArrayList<QueryField>();
					orderQueryFieldList.add(new QueryField("glossaryId", Boolean.FALSE));
					List<Glossary> glossaries = glossaryDao.loadAll(predicateQueryFieldList, orderQueryFieldList);
					for (Glossary glossary : glossaries) {
						// 包含的分类标签ID
						for (CategoryTag categoryTag : glossary.getCategoryTags()) {
							glossary.getCategoryTagIds().add(categoryTag.getCategoryTagId());
						}
					}
					newsSource.setGlossaries(glossaries);
					
					// 包含的分类标签
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSource.getNewsSourceId())));
					predicateQueryFieldList.add(new QueryField("enabled", true));
					orderQueryFieldList = new ArrayList<QueryField>();
					orderQueryFieldList.add(new QueryField("code", Boolean.TRUE));
					List<CategoryTag> categoryTags = categoryTagDao.loadAll(predicateQueryFieldList, orderQueryFieldList);
					for (CategoryTag categoryTag : categoryTags) {
						// 所属父分类标签ID
						if (categoryTag.getParentCategoryTag() != null) {
							categoryTag.setParentCategoryTagId(categoryTag.getParentCategoryTag().getCategoryTagId());
						}
					}
					newsSource.setCategoryTags(categoryTags);
				}
			}
			
			return Response.status(Response.Status.OK).entity(newsSources).build();
		} catch (ApiException e) {
			return e.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}
	
	@Override
	public Response findNewsSourcesByAppBundleId(String appBundleId) {
		try {
			// 验证必填参数
			verifyRequired(appBundleId);
			
			// 根据应用标识符查询应用
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("bundleId", appBundleId));
			List<App> apps = appDao.loadAll(predicateQueryFieldList, null);
			App app = apps.isEmpty() ? null : apps.get(0);
			// 如果没有查询到
			if (app == null) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.应用不存在.toJson()).build();
			}
			// 如果应用不可用
			if (!app.getEnabled()) {
				return Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.应用不可用.toJson()).build();
			}
			
			// 关联的新闻源
			List<NewsSource> newsSources = new ArrayList<>();
			for (NewsSource newsSource : app.getNewsSources()) {
				if (newsSource.getEnabled()) {
					// 包含的样式
					newsSource.getStyles().entrySet();
					
					appDao.getEntityManager().detach(newsSource);
					newsSources.add(newsSource);
					
					// 包含的栏目
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSource.getNewsSourceId())));
					predicateQueryFieldList.add(new QueryField("enabled", true));
					List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
					orderQueryFieldList.add(new QueryField("code", Boolean.TRUE));
					List<Column> columns = columnDao.loadAll(predicateQueryFieldList, orderQueryFieldList);				
					for (Column column : columns) {
						// 所属父栏目ID
						if (column.getParentColumn() != null) {
							column.setParentColumnId(column.getParentColumn().getColumnId());
						}
						// 包含的分类标签ID
						for (CategoryTag categoryTag : column.getCategoryTags()) {
							column.getCategoryTagIds().add(categoryTag.getCategoryTagId());
						}
					}
					newsSource.setColumns(columns);				
					
					// 包含的术语
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSource.getNewsSourceId())));
					predicateQueryFieldList.add(new QueryField("enabled", true));
					orderQueryFieldList = new ArrayList<QueryField>();
					orderQueryFieldList.add(new QueryField("glossaryId", Boolean.FALSE));
					List<Glossary> glossaries = glossaryDao.loadAll(predicateQueryFieldList, orderQueryFieldList);
					for (Glossary glossary : glossaries) {
						// 包含的分类标签ID
						for (CategoryTag categoryTag : glossary.getCategoryTags()) {
							glossary.getCategoryTagIds().add(categoryTag.getCategoryTagId());
						}
					}
					newsSource.setGlossaries(glossaries);
					
					// 包含的分类标签
					predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSource.getNewsSourceId())));
					predicateQueryFieldList.add(new QueryField("enabled", true));
					orderQueryFieldList = new ArrayList<QueryField>();
					orderQueryFieldList.add(new QueryField("code", Boolean.TRUE));
					List<CategoryTag> categoryTags = categoryTagDao.loadAll(predicateQueryFieldList, orderQueryFieldList);
					for (CategoryTag categoryTag : categoryTags) {
						// 所属父分类标签ID
						if (categoryTag.getParentCategoryTag() != null) {
							categoryTag.setParentCategoryTagId(categoryTag.getParentCategoryTag().getCategoryTagId());
						}
					}
					newsSource.setCategoryTags(categoryTags);
					break;
				}
			}
			return Response.status(Response.Status.OK).entity(newsSources).build();
			
		} catch (ApiException e) {
			return e.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	@JsonView(UniversalJsonViews.StaticDynamicPublic.class)
	public Response getDistributionsByNewsSourceIdAndColumnId(int newsSourceId, int columnId, int page) {
		try {
			// 判断是否正在审核
			boolean inReview = false;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetail) {
				UserDetail userDetail = (UserDetail) principal;
				inReview = userDetail.getUserId() != null && userDetail.getCertificateEnabled() != null && userDetail.getCertificateEnabled() == true;
			}

			// 验证必填参数
			if (newsSourceId == 0 || columnId == 0 || page < 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			
			// 根据栏目ID获取栏目
			Column column = columnDao.loadById(columnId, "childColumns");
			// 如果不存在
			if (column == null || !column.getEnabled() || column.getNewsSource().getNewsSourceId() != newsSourceId || !column.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.栏目不存在.toJson()).build();
			}

			// 根据栏目ID获取已发布分发列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			if (column.getChildColumns().size() == 0) {
				predicateQueryFieldList.add(new QueryField("column", new QueryField("columnId", columnId)));				
			} else {
				predicateQueryFieldList.add(new QueryField("column", new QueryField("parentColumn", new QueryField("columnId", columnId))));				
			}
			if (!inReview) {
				predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
			} else {
				predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NULL));
				predicateQueryFieldList.add(new QueryField("scheduledTime", null, PredicateEnum.IS_NULL));
			}
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			if (!inReview) {
				if (column.getChildColumns().size() == 0) {
					orderQueryFieldList.add(new QueryField("position", Boolean.FALSE));				
				}
				orderQueryFieldList.add(new QueryField("publishTime", Boolean.FALSE));
			} else {
				orderQueryFieldList.add(new QueryField("validateTime", Boolean.FALSE));
			}
			orderQueryFieldList.add(new QueryField("distributionId", Boolean.FALSE));
			List<Distribution> distributions = distributionDao.loadPage(predicateQueryFieldList, orderQueryFieldList, page * apiResultsPerPage, apiResultsPerPage);
			// 如果无结果
			if (distributions.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.列表结束.toJson()).build();
			}
			for (Distribution distribution : distributions) {
				// 所属栏目ID
				distribution.setColumnId(distribution.getColumn().getColumnId());

				// 包含的缩略图
				distribution.getThumbnails().iterator();
			}

			// 调整指定排序位置
			if (!inReview && column.getChildColumns().size() == 0 && page == 0 && distributions.size() > 0) {
				while (distributions.get(0).getPosition() != null && distributions.get(0).getPosition() > 0) {
				    Distribution distribution = distributions.get(0);
				    distributions.remove(distribution);
				    distributions.add(Math.min(distribution.getPosition(), distributions.size()), distribution);
				}
			}
			
			return Response.status(Response.Status.OK).entity(distributions).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}

	@Override
	public Response getRecommendGroupsByNewsSourceIdAndColumnId(int newsSourceId, int columnId) {
		try {
			// 判断是否正在审核
			boolean inReview = false;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetail) {
				UserDetail userDetail = (UserDetail) principal;
				inReview = userDetail.getUserId() != null && userDetail.getCertificateEnabled() != null && userDetail.getCertificateEnabled() == true;
			}

			// 验证必填参数
			if (newsSourceId == 0 || columnId == 0) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.缺少必填项.toJson()).build();
			}
			
			// 根据栏目ID获取栏目
			Column column = columnDao.loadById(columnId);
			// 如果不存在
			if (column == null || !column.getEnabled() || column.getNewsSource().getNewsSourceId() != newsSourceId || !column.getNewsSource().getEnabled()) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.栏目不存在.toJson()).build();
			}

			// 根据栏目ID获取推荐组列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSourceId)));		
			predicateQueryFieldList.add(new QueryField("ownerType", RecommendGroup.OwnerTypeEnum.栏目.ordinal()));
			predicateQueryFieldList.add(new QueryField("ownerId", columnId));
			predicateQueryFieldList.add(new QueryField("enabled", true));	
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("position", Boolean.TRUE));
			List<RecommendGroup> recommendGroups = recommendGroupDao.loadAll(predicateQueryFieldList, orderQueryFieldList);
			for (RecommendGroup recommendGroup : recommendGroups) {
				// 包含的推荐
				recommendGroup.getRecommends().iterator();
				recommendGroupDao.getEntityManager().detach(recommendGroup);
				
				// 处理推荐
				List<Recommend> recommendsToRemove = new ArrayList<Recommend>();
				for (Recommend recommend : recommendGroup.getRecommends()) {
					if (!inReview && recommend.getStatusEnum() != Recommend.StatusEnum.已发布) {
						// 如果未在审核，未发布的，移除
						recommendsToRemove.add(recommend);
					} else if (inReview) {
						// 如果在审核，未发布且未验证、已发布且在验证、已撤销的，移除
						if (recommend.getStatusEnum() == Recommend.StatusEnum.未发布 && recommend.getValidating() == false) {
							recommendsToRemove.add(recommend);
						} else if (recommend.getStatusEnum() == Recommend.StatusEnum.已发布 && recommend.getValidating() == true) {
							recommendsToRemove.add(recommend);
						} else if (recommend.getStatusEnum() == Recommend.StatusEnum.撤销发布) {
							recommendsToRemove.add(recommend);
						}
					}
				}
				recommendGroup.getRecommends().removeAll(recommendsToRemove);
				// 截断超过数量的
				if (recommendGroup.getRecommends().size() > recommendGroup.getRecommendCount()) {
					recommendGroup.setRecommends(recommendGroup.getRecommends().subList(0, recommendGroup.getRecommendCount())); 
				}
			}
			
			return Response.ok().entity(recommendGroups).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ErrorMessage.操作失败.toJson()).build();
		}
	}	

	@Override
	public Response findColumnSubstitutesByAppBundleId(String appBundleId) {
		try {
			// 验证必填参数
			verifyRequired(appBundleId);

			// 根据应用标识符查询应用
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("bundleId", appBundleId));
			List<App> apps = appDao.loadAll(predicateQueryFieldList, null);
			App app = apps.isEmpty() ? null : apps.get(0);
			// 如果没有查询到
			if (app == null) {
				return Response.status(Response.Status.NOT_FOUND).entity(ErrorMessage.应用不存在.toJson()).build();
			}
			// 如果应用不可用
			if (!app.getEnabled()) {
				return Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.应用不可用.toJson()).build();
			}
			List<ColumnSubstitute> columnSubstitutes = new ArrayList<>();
			for (ColumnSubstitute columnSubstitute : app.getColumnSubstitutes()) {
				if (columnSubstitute.getEnabled()) {
					columnSubstitute.setSourceColumnId(columnSubstitute.getSourceColumn().getColumnId());
					columnSubstitute.setTargetColumnId(columnSubstitute.getTargetColumn().getColumnId());
					columnSubstitutes.add(columnSubstitute);
				}
			}
			
			return Response.status(Response.Status.OK).entity(columnSubstitutes).build();
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
		格式错误(101, "格式错误"),
		数据异常(102, "数据异常"),
		数据唯一限制(103, "数据唯一限制"),
		操作受限(104, "操作受限"),
		操作超时(105, "操作超时"),
		操作失败(106, "操作失败"),
		列表结束(107, "列表结束"),
		栏目不存在(201, "栏目不存在");

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
