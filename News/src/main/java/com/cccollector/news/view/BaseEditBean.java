/**
 * 
 */
package com.cccollector.news.view;

import javax.faces.bean.ManagedProperty;

import com.cccollector.news.service.AppService;
import com.cccollector.news.service.ArticleService;
import com.cccollector.news.service.CategoryTagService;
import com.cccollector.news.service.ColumnReplacementService;
import com.cccollector.news.service.ColumnService;
import com.cccollector.news.service.ColumnSubstituteService;
import com.cccollector.news.service.DistributionService;
import com.cccollector.news.service.EditionService;
import com.cccollector.news.service.GlossaryService;
import com.cccollector.news.service.MediaService;
import com.cccollector.news.service.NewsSourceService;
import com.cccollector.news.service.RecommendGroupService;
import com.cccollector.news.service.RecommendService;
import com.cccollector.news.service.ReleaseService;
import com.cccollector.news.service.SiteService;
import com.cccollector.news.service.SiteVersionService;
import com.cccollector.news.service.StyleService;
import com.cccollector.news.service.TemplateMappingService;
import com.cccollector.news.service.TemplateService;
import com.cccollector.news.service.TemplateVersionService;
import com.cccollector.news.service.ThumbnailService;
import com.cccollector.news.service.UserAppService;
import com.cccollector.news.service.UserService;
import com.cccollector.news.service.WebFolderService;
import com.cccollector.news.service.WebPageService;
import com.cccollector.universal.app.service.UserDetailService;
import com.cccollector.universal.view.GenericEditBean;

/**
 * 基础编辑Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public abstract class BaseEditBean<T> extends GenericEditBean<T> {

	/**
	 * 应用服务
	 */
	@ManagedProperty(value = "#{appService}")
	protected AppService appService;

	public void setAppService(AppService _appService) {
		appService = _appService;
	}
	
	/**
	 * 版本服务
	 */
	@ManagedProperty(value = "#{editionService}")
	protected EditionService editionService;

	public void setEditionService(EditionService _editionService) {
		editionService = _editionService;
	}
	
	/**
	 * 发行服务
	 */
	@ManagedProperty(value = "#{releaseService}")
	protected ReleaseService releaseService;

	public void setReleaseService(ReleaseService _releaseService) {
		releaseService = _releaseService;
	}

	/**
	 * 新闻源服务
	 */
	@ManagedProperty(value = "#{newsSourceService}")
	protected NewsSourceService newsSourceService;

	public void setNewsSourceService(NewsSourceService _newsSourceService) {
		newsSourceService = _newsSourceService;
	}

	/**
	 * 栏目服务
	 */
	@ManagedProperty(value = "#{columnService}")
	protected ColumnService columnService;

	public void setColumnService(ColumnService _columnService) {
		columnService = _columnService;
	}

	/**
	 * 文章服务
	 */
	@ManagedProperty(value = "#{articleService}")
	protected ArticleService articleService;

	public void setArticleService(ArticleService _articleService) {
		articleService = _articleService;
	}

	/**
	 * 多媒体服务
	 */
	@ManagedProperty(value = "#{mediaService}")
	protected MediaService mediaService;

	public void setMediaService(MediaService _mediaService) {
		mediaService = _mediaService;
	}

	/**
	 * 分发服务
	 */
	@ManagedProperty(value = "#{distributionService}")
	protected DistributionService distributionService;

	public void setDistributionService(DistributionService _distributionService) {
		distributionService = _distributionService;
	}

	/**
	 * 缩略图服务
	 */
	@ManagedProperty(value = "#{thumbnailService}")
	protected ThumbnailService thumbnailService;

	public void setThumbnailService(ThumbnailService _thumbnailService) {
		thumbnailService = _thumbnailService;
	}

	/**
	 * 样式服务
	 */
	@ManagedProperty(value = "#{styleService}")
	protected StyleService styleService;

	public void setStyleService(StyleService _styleService) {
		styleService = _styleService;
	}

	/**
	 * 术语服务
	 */
	@ManagedProperty(value = "#{glossaryService}")
	protected GlossaryService glossaryService;

	public void setGlossaryService(GlossaryService _glossaryService) {
		glossaryService = _glossaryService;
	}

	/**
	 * 分类标签服务
	 */
	@ManagedProperty(value = "#{categoryTagService}")
	protected CategoryTagService categoryTagService;

	public void setCategoryTagService(CategoryTagService _categoryTagService) {
		categoryTagService = _categoryTagService;
	}

	/**
	 * 推荐组服务
	 */
	@ManagedProperty(value = "#{recommendGroupService}")
	protected RecommendGroupService recommendGroupService;

	public void setRecommendGroupService(RecommendGroupService _recommendGroupService) {
		recommendGroupService = _recommendGroupService;
	}

	/**
	 * 推荐服务
	 */
	@ManagedProperty(value = "#{recommendService}")
	protected RecommendService recommendService;

	public void setRecommendService(RecommendService _recommendService) {
		recommendService = _recommendService;
	}

	/**
	 * 用户服务
	 */
	@ManagedProperty(value = "#{userService}")
	protected UserService userService;

	public void setUserService(UserService _userService) {
		userService = _userService;
	}

	/**
	 * 用户应用服务
	 */
	@ManagedProperty(value = "#{userAppService}")
	protected UserAppService userAppService;

	public void setUserAppService(UserAppService _userAppService) {
		userAppService = _userAppService;
	}

	/**
	 * 应用管理平台应用服务
	 */
	@ManagedProperty(value = "#{app_appService}")
	protected com.cccollector.universal.app.service.AppService app_appService;

	public void setApp_appService(com.cccollector.universal.app.service.AppService _app_appService) {
		app_appService = _app_appService;
	}

	/**
	 * 应用管理平台用户详情服务
	 */
	@ManagedProperty(value = "#{app_userDetailService}")
	protected UserDetailService app_userDetailService;

	public void setApp_userDetailService(UserDetailService _app_userDetailService) {
		app_userDetailService = _app_userDetailService;
	}

	/**
	 * 用户平台用户应用服务
	 */
	@ManagedProperty(value = "#{user_userAppService}")
	protected com.cccollector.universal.user.service.UserAppService user_userAppService;

	public void setUser_userAppService(com.cccollector.universal.user.service.UserAppService _user_userAppService) {
		user_userAppService = _user_userAppService;
	}
	
	/**
	 * 栏目替身服务
	 */
	@ManagedProperty(value = "#{columnSubstituteService}")
	protected ColumnSubstituteService columnSubstituteService;

	public void setColumnSubstituteService(ColumnSubstituteService _columnSubstituteService) {
		columnSubstituteService = _columnSubstituteService;
	}
	
	/**
	 * 栏目替代服务
	 */
	@ManagedProperty(value = "#{columnReplacementService}")
	protected ColumnReplacementService columnReplacementService;

	public void setColumnReplacementService(ColumnReplacementService _columnReplacementService) {
		columnReplacementService = _columnReplacementService;
	}
	
	/**
	 * 站点服务
	 */
	@ManagedProperty(value = "#{siteService}")
	protected SiteService siteService;

	public void setSiteService(SiteService _siteService) {
		siteService = _siteService;
	}
	
	/**
	 * 站点版本服务
	 */
	@ManagedProperty(value = "#{siteVersionService}")
	protected SiteVersionService siteVersionService;

	public void setSiteVersionService(SiteVersionService _siteVersionService) {
		siteVersionService = _siteVersionService;
	}
	
	/**
	 * 模板映射服务
	 */
	@ManagedProperty(value = "#{templateMappingService}")
	protected TemplateMappingService templateMappingService;
	
	public void setTemplateMappingService(TemplateMappingService _templateMappingService) {
		templateMappingService = _templateMappingService;
	}
	
	/**
	 * 模板服务
	 */
	@ManagedProperty(value = "#{templateService}")
	protected TemplateService templateService;

	public void setTemplateService(TemplateService _templateService) {
		templateService = _templateService;
	}
	
	/**
	 * 模板版本服务
	 */
	@ManagedProperty(value = "#{templateVersionService}")
	protected TemplateVersionService templateVersionService;

	public void setTemplateVersionService(TemplateVersionService _templateVersionService) {
		templateVersionService = _templateVersionService;
	}
	
	/**
	 * 网络文件夹服务
	 */
	@ManagedProperty(value = "#{webFolderService}")
	protected WebFolderService webFolderService;
	
	public void setWebFolderService(WebFolderService _webFolderService) {
		webFolderService = _webFolderService;
	}
	
	/**
	 * 网络页面服务
	 */
	@ManagedProperty(value = "#{webPageService}")
	protected WebPageService webPageService;
	
	public void setWebPageService(WebPageService _webPageService) {
		webPageService = _webPageService;
	}
}
