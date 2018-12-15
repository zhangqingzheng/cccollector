/**
 * 
 */
package com.cccollector.news.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.cccollector.news.dao.CategoryTagDao;
import com.cccollector.news.dao.ColumnDao;
import com.cccollector.news.dao.DistributionDao;
import com.cccollector.news.dao.LazyArticleDao;
import com.cccollector.news.dao.LazyDistributionDao;
import com.cccollector.news.dao.RecommendDao;
import com.cccollector.news.dao.RecommendGroupDao;
import com.cccollector.news.dao.SiteVersionDao;
import com.cccollector.news.dao.StaticPageDao;
import com.cccollector.news.dao.TemplateDao;
import com.cccollector.news.dao.TemplateVersionDao;
import com.cccollector.news.model.Article;
import com.cccollector.news.model.CategoryTag;
import com.cccollector.news.model.Column;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.LazyArticle;
import com.cccollector.news.model.LazyDistribution;
import com.cccollector.news.model.LazyThumbnail;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.Recommend;
import com.cccollector.news.model.RecommendGroup;
import com.cccollector.news.model.Site;
import com.cccollector.news.model.SiteVersion;
import com.cccollector.news.model.StaticPage;
import com.cccollector.news.model.Template;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.news.model.TemplateVersion;
import com.cccollector.news.model.Thumbnail;
import com.cccollector.news.service.ColumnService;
import com.cccollector.news.service.StaticPageService;
import com.cccollector.news.service.TemplateMappingService;
import com.cccollector.news.service.TemplateService;
import com.cccollector.news.templatemodel.TemplateArticle;
import com.cccollector.news.templatemodel.TemplateArticleContent;
import com.cccollector.news.templatemodel.TemplateBaseContent;
import com.cccollector.news.templatemodel.TemplateCategoryTag;
import com.cccollector.news.templatemodel.TemplateColumn;
import com.cccollector.news.templatemodel.TemplateColumnContent;
import com.cccollector.news.templatemodel.TemplateRecommend;
import com.cccollector.news.util.TemplateUtil;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 模板服务实现类
 *
 * @author 谢朋 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("templateService")
public class TemplateServiceImpl extends GenericServiceHibernateImpl<Integer, Template> implements TemplateService {

	// TODO:提取到配置文件
	private int apiResultsPerPage = 25;

	@Autowired
	private CategoryTagDao categoryTagDao;

	@Autowired
	private ColumnDao columnDao;

	@Autowired
	private ColumnService columnService;

	@Autowired
	private DistributionDao distributionDao;

	@Autowired
	private LazyDistributionDao lazyDistributionDao;

	@Autowired
	private LazyArticleDao lazyArticleDao;

	@Autowired
	private TemplateVersionDao templateVersionDao;

	/**
	 * 平台标识符
	 */
	@Value("${platformBundleId}")
	private String platformBundleId;

	@Autowired
	private RecommendDao recommendDao;

	@Autowired
	private RecommendGroupDao recommendGroupDao;

	/**
	 * 相关分发每页数量
	 */
	@Value("${count.relativeDistributions}")
	private Integer relativeDistributionsCount;

	@Autowired
	private SiteVersionDao siteVersionDao;

	@Autowired
	private StaticPageDao staticPageDao;

	@Autowired
	private StaticPageService staticPageService;

	@Autowired
	private TemplateMappingService templateMappingService;

	/**
	 * 回源地址
	 */
	@Value("${paths.staticSourceUrl}")
	private String staticSourceUrl;

	@Autowired
	private TemplateDao templateDao;

	/**
	 * 缩略图URL
	 */
	@Value("${paths.thumbnailUrl}")
	private String thumbnailUrl;

	/**
	 * Tomcat数据目录
	 */
	@Value("${paths.tomcatDataPath}")
	private String tomcatDataPath;

	/**
	 * 新浪应用钥匙
	 */
	@Value("${share.xinlangAppkey}")
	private String xinlangAppkey;

	/**
	 * 编码
	 */
	@Value("${share.code}")
	private String code;

	Map<String, List<TemplateColumn>> bannerInfoCache = new HashMap<String, List<TemplateColumn>>();

	@Override
	public void addTemplate(Template template) {
		// 设置排序位置
		Integer position = templateDao.max("position", null);
		template.setPosition(position == null ? 0 : position.intValue() + 1);

		// 保存新闻源
		templateDao.save(template);
	}

	@Override
	@CacheEvict("templateMappings")
	public void updateTemplate(Template template) {
		Template newTemplate = templateDao.loadById(template.getTemplateId());
		newTemplate.setName(template.getName());
		newTemplate.setValidateExpression(template.getValidateExpression());
		newTemplate.setEnabled(template.getEnabled());
	}

	/**
	 * 根据类型找到对应的模板版本
	 * 
	 * @param templateMappingType 模板映射类型
	 * @param contentId 内容ID
	 * @param siteVersionId 站点版本ID
	 * @return
	 */
	private TemplateVersion getTemplateVersion(int templateMappingType, int contentId, int siteVersionId, Integer templateVersionId) {

		if (templateVersionId != null) {
			return templateVersionDao.loadById(templateVersionId);
		}

		List<TemplateMapping> templateMappings = templateMappingService.findTemplateMappingsBySiteVersionId(siteVersionId);

		int sourceTemplateType = 0;// 源模版类别
		int targetTemplateType = 0;// 目标模版类别

		// 类别为栏目
		if (templateMappingType == TemplateMapping.TypeEnum.栏目.ordinal()) {
			// 根据栏目ID获取栏目
			Column column = columnDao.loadById(contentId);
			if (column == null || !column.getEnabled()) {
				return null;
			}

			sourceTemplateType = column.getTemplateType();
			if (column.getTemplateTypeEnum() == Column.TemplateTypeEnum.子栏目列表 || column.getTemplateTypeEnum() == Column.TemplateTypeEnum.顶部栏目选择_子栏目列表
					|| column.getTemplateTypeEnum() == Column.TemplateTypeEnum.顶部栏目选择_顶部轮播推荐_子栏目列表) {
				targetTemplateType = TemplateMapping.ColumnTargetTemplateTypeEnum.子栏目列表.ordinal();
			} else if (column.getTemplateTypeEnum() == Column.TemplateTypeEnum.文章列表 || column.getTemplateTypeEnum() == Column.TemplateTypeEnum.顶部栏目选择_文章列表
					|| column.getTemplateTypeEnum() == Column.TemplateTypeEnum.顶部栏目选择_顶部轮播推荐_文章列表) {
				targetTemplateType = TemplateMapping.ColumnTargetTemplateTypeEnum.文章列表.ordinal();
			} else if (column.getTemplateTypeEnum() == Column.TemplateTypeEnum.顶部栏目选择_顶部轮播推荐_子栏目_文章列表) {
				targetTemplateType = TemplateMapping.ColumnTargetTemplateTypeEnum.子栏目_文章列表.ordinal();
			} else {
				targetTemplateType = TemplateMapping.ColumnTargetTemplateTypeEnum.文章列表.ordinal();
			}
		}

		// 类别为文章
		if (templateMappingType == TemplateMapping.TypeEnum.文章.ordinal()) {
			// 根据分发ID获取分发
			Distribution distribution = distributionDao.loadById(contentId);
			// 如果不存在
			if (distribution == null) {
				return null;
			}
			sourceTemplateType = distribution.getBodyTemplateType();
			targetTemplateType = TemplateMapping.ArticleTargetTemplateTypeEnum.图文混排.ordinal();
		}

		// 类别为其他
		if (templateMappingType == TemplateMapping.TypeEnum.其它.ordinal()) {
			// 根据分类标签ID获取分类标签
			CategoryTag categoryTag = categoryTagDao.loadById(contentId);
			// 如果不存在
			if (categoryTag == null) {
				return null;
			}

			sourceTemplateType = TemplateMapping.OtherSourceTemplateTypeEnum.分类标签.ordinal();
			targetTemplateType = TemplateMapping.OtherTargetTemplateTypeEnum.分类标签.ordinal();
		}

		List<TemplateMapping> suitTemplateMappings = new ArrayList<>();
		for (TemplateMapping templateMapping : templateMappings) {

			if ((templateMapping.getType() != templateMappingType) || (templateMapping.getSourceTemplateType() != sourceTemplateType)
					|| (templateMapping.getTargetTemplateType() != targetTemplateType) || (templateMapping.getContentId() != null && templateMapping.getContentId() != contentId)) {
				continue;
			}

			suitTemplateMappings.add(templateMapping);
		}

		// 没有模板映射直接返回空对象
		if (suitTemplateMappings == null || suitTemplateMappings.size() == 0) {
			// 输出错误日志
			System.out.println("找不到模板映射.站点版本ID[" + siteVersionId + "]," + TemplateMapping.TypeEnum.栏目.name() + "ID[" + contentId + "],源类型［" + sourceTemplateType + "］,目标类型［"
					+ targetTemplateType + "］");
			return null;
		}

		// 根据模板映射获取模板
		TemplateMapping templateMapping = null;

		for (TemplateMapping suitTemplateMapping : suitTemplateMappings) {

			if (templateMapping == null) {
				templateMapping = suitTemplateMapping;
				continue;
			}

			// ID命中的优先
			if (suitTemplateMapping.getContentId() != null && suitTemplateMapping.getContentId() != 0) {
				templateMapping = suitTemplateMapping;
				break;
			}

			// 取最新的一条
			if (templateMapping.getTemplateMappingId() < suitTemplateMapping.getTemplateMappingId()) {
				templateMapping = suitTemplateMapping;
				continue;
			}
		}

		// 根据模板映射获取模板
		Template templateToReturn = templateMapping.getTemplate();

		// 根据模板获取模板版本
		TemplateVersion templateVersionToReturn = new TemplateVersion();
		List<TemplateVersion> templateVersions = templateToReturn.getTemplateVersions();
		for (TemplateVersion templateVersion : templateVersions) {
			if (templateVersion.getStatusEnum() == TemplateVersion.StatusEnum.已发布) {
				templateVersionToReturn = templateVersion;
				break;
			}
		}

		return templateVersionToReturn;

	}

	@Override
	public void moveTemplates(List<Template> templates) {
		int position = 0;
		for (Template template : templates) {
			template.setPosition(position);
			templateDao.update(template, "position");
			position++;
		}
	}

	/**
	 * 处理栏目、分发、标签静态化壳子<br>
	 * 方法一: 生成[静态化页面]文件 方法二: 添加[静态化页面]记录 方法三: 如果是分发,更新分发表中[静态化页面Url]字段
	 */
	@Override
	public void staticPageCase(int contentId, int templateMappingType, Integer siteVersionId) {

		// 加载[数据]
		List<TemplateBaseContent> templateBaseContents = new ArrayList<>();

		if (siteVersionId != null) {
			templateBaseContents.add(loadStaticData(contentId, templateMappingType, siteVersionId, null));
		} else {
			templateBaseContents.addAll(loadStaticDatas(contentId, templateMappingType, null));
		}

		for (TemplateBaseContent templateBaseContent : templateBaseContents) {
			// 生成[静态化页面]文件
			boolean isSuccess = TemplateUtil.staticPage(templateBaseContent);

			if (!isSuccess) {// 不成功的话,不记录静态页面
				continue;
			}

			int staticPageContentType = 0;

			if (templateMappingType == TemplateMapping.TypeEnum.栏目.ordinal()) {
				staticPageContentType = StaticPage.ContentTypeEnum.栏目.ordinal();
			} else if (templateMappingType == TemplateMapping.TypeEnum.文章.ordinal()) {
				staticPageContentType = StaticPage.ContentTypeEnum.分发.ordinal();
			} else if (templateMappingType == TemplateMapping.TypeEnum.其它.ordinal()) {
				staticPageContentType = StaticPage.ContentTypeEnum.分类标签.ordinal();
			}

			// 添加[静态化页面]记录
			staticPageService.addStaticPage(staticPageContentType, contentId, templateBaseContent.getStaticFilePath(), templateBaseContent.getSiteVersionId(),
					templateBaseContent.getTemplateVersionId());

			// 更新分发表中[静态化页面Url]字段
			if (templateMappingType == TemplateMapping.TypeEnum.文章.ordinal()) {
				Distribution distribution = distributionDao.loadById(contentId);
				distribution.setStaticPageUrl(templateBaseContent.getStaticFilePath().replace("\\", "/"));
			}
		}
	}

	/**
	 * 处理栏目、标签静态化Json数据<br>
	 * 生成前3页Json数据
	 */
	@Override
	public void staticPageContent(int contentId, int templateMappingType, Integer siteVersionId) {
		// 生成栏目列表或者标签列表[前3页]Json数据
		if (siteVersionId != null) {
			TemplateUtil.staticPage(loadStaticData(contentId, templateMappingType, siteVersionId, 0));
			TemplateUtil.staticPage(loadStaticData(contentId, templateMappingType, siteVersionId, 1));
			TemplateUtil.staticPage(loadStaticData(contentId, templateMappingType, siteVersionId, 2));
		} else {
			TemplateUtil.staticPages(loadStaticDatas(contentId, templateMappingType, 0));
			TemplateUtil.staticPages(loadStaticDatas(contentId, templateMappingType, 1));
			TemplateUtil.staticPages(loadStaticDatas(contentId, templateMappingType, 2));

		}
	}

	/**
	 * 计算文章的存储地址
	 */
	private String calculatePath4Article(int distributionId, SiteVersion siteVersion) {

		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("siteVersionId", siteVersion.getSiteVersionId())));
		predicateQueryFieldList.add(new QueryField("contentType", StaticPage.ContentTypeEnum.分发.ordinal()));
		predicateQueryFieldList.add(new QueryField("contentId", distributionId));
		List<StaticPage> staticPages = staticPageDao.loadAll(predicateQueryFieldList, null);

		if (staticPages != null && staticPages.size() > 0) {
			return staticPages.get(0).getFilePath();
		}

		SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat dd = new SimpleDateFormat("dd");

		// 避免数据问题导致页面出现异常
		LazyDistribution distribution = lazyDistributionDao.loadById(distributionId);
		if (distribution == null) {
			return "";
		}

		Date date = distribution.getPublishTimeDate();

		byte[] digest = (System.currentTimeMillis() + "" + new Random().nextInt(999999)).getBytes();
		String md5 = DigestUtils.md5DigestAsHex(digest);
		String fileName = md5.substring(8, 24);

		String filePath = yyyyMM.format(date) + File.separator + dd.format(date) + File.separator + fileName + ".html";

		TemplateVersion templateVersion = getTemplateVersion(TemplateMapping.TypeEnum.文章.ordinal(), distributionId, siteVersion.getSiteVersionId(), null);

		if (templateVersion != null) {
			// 记录至数据库,避免下次再使用的时候,没有记录的问题
			StaticPage staticPage = new StaticPage();
			staticPage.setContentType(StaticPage.ContentTypeEnum.分发.ordinal());
			staticPage.setContentId(distributionId);
			staticPage.setFilePath(filePath);
			staticPage.setStaticTimeDate(new Date());
			staticPage.setTemplateVersion(templateVersion);
			staticPage.setSiteVersion(siteVersion);
			staticPageDao.save(staticPage);
		}

		return filePath;
	}

	/**
	 * 计算分类标签的存储地址
	 */
	private String calculatePath4CategoryTag(int categoryTagId) {
		return "categoryTag" + File.separator + categoryTagId + File.separator + "index.html";
	}

	/**
	 * 计算分类标签页面文章列表的存储地址
	 */
	private String calculatePath4CategoryTagArticles(int categoryTagId, int pageNo) {
		return "categoryTag" + File.separator + categoryTagId + File.separator + pageNo + ".json";
	}

	/**
	 * 计算栏目的存储地址
	 */
	private static String calculatePath4Column(int columnId) {
		return "column" + File.separator + columnId + File.separator + "index.html";
	}

	/**
	 * 计算栏目页面文章列表的存储地址
	 */
	private String calculatePath4ColumnArticles(int columnId, int pageNo) {
		return "column" + File.separator + columnId + File.separator + pageNo + ".json";
	}

	/**
	 * 计算文章的存储地址
	 */
	private String calculateUrl4Article(int distributionId, SiteVersion siteVersion) {
		return "/" + calculatePath4Article(distributionId, siteVersion).replace("\\", "/");
	}

	/**
	 * 计算分类标签的存储地址
	 */
	private String calculateUrl4CategoryTag(int categoryTagId) {
		return "/" + calculatePath4CategoryTag(categoryTagId).replace("\\", "/");
	}

	/**
	 * 计算栏目的存储地址
	 */
	public static String calculateUrl4Column(int columnId) {
		return "/" + calculatePath4Column(columnId).replace("\\", "/");
	}

	/**
	 * 处理点击导航栏一、二级栏目显示
	 */
	private TemplateColumn dealClickNavigationColumns(Column firstColumn, Column secondColumn, List<Column> thirdColumns) {
		try {
			if (firstColumn == null || secondColumn == null) {
				return null;
			}
			TemplateColumn levelColumn = new TemplateColumn();
			TemplateColumn secondLevelColumn = new TemplateColumn();
			TemplateColumn thirdLevelColumn = new TemplateColumn();
			List<TemplateColumn> secondLevelColumns = new ArrayList<TemplateColumn>();
			List<TemplateColumn> thirdLevelColumns = new ArrayList<TemplateColumn>();
			// 获取当前栏目及其父栏目
			levelColumn.setColumnId(firstColumn.getColumnId());
			levelColumn.setName(firstColumn.getName());
			levelColumn.setLink(calculateUrl4Column(firstColumn.getColumnId()));
			secondLevelColumn.setColumnId(secondColumn.getColumnId());
			secondLevelColumn.setName(secondColumn.getName());
			secondLevelColumn.setLink(calculateUrl4Column(secondColumn.getColumnId()));
			for (Column column : thirdColumns) {
				thirdLevelColumn = new TemplateColumn();
				thirdLevelColumn.setColumnId(column.getColumnId());
				thirdLevelColumn.setName(column.getName());
				thirdLevelColumn.setIntro(column.getIntro());
				thirdLevelColumn.setIconUrl(thumbnailUrl + "w0_h0" + column.iconUrlPath());
				thirdLevelColumn.setLink(calculateUrl4Column(column.getColumnId()));
				thirdLevelColumns.add(thirdLevelColumn);
			}
			secondLevelColumn.setChildColumns(thirdLevelColumns);
			secondLevelColumns.add(secondLevelColumn);
			levelColumn.setChildColumns(secondLevelColumns);
			return levelColumn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 处理默认导航栏一、二级栏目显示
	 */
	private TemplateColumn dealDefaultNavigationColumns(TemplateColumn firstColumn, TemplateColumn secondColumn, SiteVersion siteVersion, List<TemplateColumn> templateColumns) {
		try {
			if (firstColumn == null || secondColumn == null) {
				return null;
			}
			TemplateColumn levelColumn = new TemplateColumn();
			TemplateColumn secondLevelColumn = new TemplateColumn();
			List<TemplateColumn> secondLevelColumns = new ArrayList<TemplateColumn>();
			// 获取默认一、二级栏目
			levelColumn.setColumnId(firstColumn.getColumnId());
			levelColumn.setName(firstColumn.getName());
			levelColumn.setLink(calculateUrl4Column(firstColumn.getColumnId()));
			secondLevelColumn.setColumnId(secondColumn.getColumnId());
			secondLevelColumn.setName(secondColumn.getName());
			secondLevelColumn.setLink(calculateUrl4Column(secondColumn.getColumnId()));
			secondLevelColumns.add(secondLevelColumn);
			levelColumn.setChildColumns(secondLevelColumns);

			if (levelColumn.getName().equals(templateColumns.get(0).getName())) {

				// 生成html文件
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("<script>location.href=\"/column/" + secondLevelColumn.getColumnId() + "/index.html\";</script>");

				// TODO:需要考虑正式环境以及测试环境
				// 输出成index.html文件
				File defaultIndexFileTomcat = new File(getStaticPagesFolderTomcat(siteVersion), "index.html");
				File defaultIndexFileNginx = new File(getStaticPagesFolderNginx(siteVersion), "index.html");

				List<File> files = new ArrayList<>();
				if (getStaticPagesFolderTomcat(siteVersion).exists()) {
					files.add(defaultIndexFileTomcat);
				}
				if (getStaticPagesFolderNginx(siteVersion).exists()) {
					files.add(defaultIndexFileNginx);
				}

				for (File file : files) {
					try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
						bw.write(stringBuffer.toString());
						bw.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			return levelColumn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取Tomcat的数据存储路径
	 */
	private String getDataPath4Tomcat() {
		return tomcatDataPath + platformBundleId + File.separator;
	}

	/**
	 * 根据当前栏目处理导航栏一、二级栏目显示
	 */
	private TemplateColumn getNavigationColumns(Column column, List<TemplateColumn> templateColumns, SiteVersion siteVersion) {
		try {
			// 设置导航栏一、二级栏目显示
			TemplateColumn navigationColumn = null;
			if (column == null) {
				// 设置默认栏目
				// 获取首页栏目及推荐栏目
				TemplateColumn templateColumnIndex = templateColumns.get(0);
				TemplateColumn templateColumnSecond = templateColumnIndex.getChildColumns().get(0);
				navigationColumn = dealDefaultNavigationColumns(templateColumnIndex, templateColumnSecond, siteVersion, templateColumns);
			} else {
				if (column.getParentColumn() != null) {
					if (column.getParentColumn().getParentColumn() != null) {
						// 处理三级栏目
						// 获取一、三级栏目 设置为一、二级栏目
						navigationColumn = dealClickNavigationColumns(column.getParentColumn().getParentColumn(), column, column.getChildColumns());
					} else {
						// 处理当前二级栏目
						// 获取一、二级栏目
						navigationColumn = dealClickNavigationColumns(column.getParentColumn(), column, column.getChildColumns());
					}
				} else {
					// 处理当前一级栏目
					for (TemplateColumn templateColumnIndex : templateColumns) {
						if (column.getName().equals(templateColumnIndex.getName())) {
							TemplateColumn templateColumnSecond = null;
							// 如果栏目是精选阅读
							if (column.getName().equals(templateColumns.get(2).getName())) {
								// 将第一个三级栏目设置为二级栏目
								templateColumnSecond = templateColumnIndex.getChildColumns().get(0).getChildColumns().get(0);
							} else {
								// 将第一个二级栏目设置为二级栏目
								templateColumnSecond = templateColumnIndex.getChildColumns().get(0);
							}
							navigationColumn = dealDefaultNavigationColumns(templateColumnIndex, templateColumnSecond, siteVersion, templateColumns);
						}
					}
				}
			}
			return navigationColumn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据新闻源Id与栏目Id获取推荐组列表
	 */
	private List<RecommendGroup> getRecommendGroups(int newsSourceId, int columnId) {
		try {
			// 验证必填参数
			if (newsSourceId == 0 || columnId == 0) {
				return null;
			}

			// 根据栏目ID获取栏目
			Column column = columnDao.loadById(columnId);
			// 如果不存在
			if (column == null || !column.getEnabled() || column.getNewsSource().getNewsSourceId() != newsSourceId || !column.getNewsSource().getEnabled()) {
				return null;
			}

			// 根据栏目ID获取推荐组列表
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSourceId)));
			predicateQueryFieldList.add(new QueryField("ownerType", RecommendGroup.OwnerTypeEnum.栏目.ordinal()));
			predicateQueryFieldList.add(new QueryField("ownerId", columnId));
			predicateQueryFieldList.add(new QueryField("enabled", true));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("position", true));
			List<RecommendGroup> recommendGroups = recommendGroupDao.loadAll(predicateQueryFieldList, orderQueryFieldList);
			for (RecommendGroup recommendGroup : recommendGroups) {
				// 包含的推荐
				recommendGroup.getRecommends().iterator();
				recommendGroupDao.getEntityManager().detach(recommendGroup);

				// 处理推荐 (不包含审核状态)
				List<Recommend> recommendsToRemove = new ArrayList<Recommend>();
				for (Recommend recommend : recommendGroup.getRecommends()) {
					if (recommend.getStatusEnum() != Recommend.StatusEnum.已发布) {
						// 如果不是已发布的，移除
						recommendsToRemove.add(recommend);
					}
				}
				recommendGroup.getRecommends().removeAll(recommendsToRemove);
				// 截断超过数量的
				if (recommendGroup.getRecommends().size() > recommendGroup.getRecommendCount()) {
					recommendGroup.setRecommends(recommendGroup.getRecommends().subList(0, recommendGroup.getRecommendCount()));
				}
			}

			return recommendGroups;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 处理推荐数据
	 */
	private TemplateColumn getRecommends(int newsSourceId, int columnId, SiteVersion siteVersion) {
		try {
			TemplateColumn templateColumnWithInRecommends = new TemplateColumn();
			List<RecommendGroup> recommendGroups = getRecommendGroups(newsSourceId, columnId);
			List<TemplateRecommend> templateRecommendList = new ArrayList<TemplateRecommend>();
			TemplateRecommend templateRecommend = null;
			if (recommendGroups != null && recommendGroups.size() > 0) {
				RecommendGroup recommendGroup = recommendGroups.get(0);
				for (Recommend recommend : recommendGroup.getRecommends()) {
					templateRecommend = new TemplateRecommend();
					templateRecommend.setRecommendId(recommend.getRecommendId());
					templateRecommend.setTitle(recommend.getTitle());
					templateRecommend.setPictureUrl(thumbnailUrl + "w632_h355/" + recommend.pictureUrlPath());
					templateRecommend.setUrl(getRecommendUrl(recommend.getRecommendId(), siteVersion));
					templateRecommendList.add(templateRecommend);
				}
				templateColumnWithInRecommends.setTemplateRecommends(templateRecommendList);
			}
			return templateColumnWithInRecommends;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 计算Nginx下的静态页面存储路径
	 */
	private File getStaticPagesFolderNginx(SiteVersion siteVersion) {
		return new File(siteVersion.getSite().getRootPath(), siteVersion.getSiteVersionFilePath());
	}

	private File getStaticPagesFolderTomcat(SiteVersion siteVersion) {
		File siteVersionFolder = new File(getDataPath4Tomcat(), siteVersion.getSiteVersionFilePathWithSite());
		return new File(siteVersionFolder, "staticPages");
	}

	/**
	 * 根据新闻源Id, 分发Id获取模板文章对象
	 */
	private TemplateArticle getTemplateArticleBydistributionId(int distributionId, SiteVersion siteVersion) {
		try {
			// 验证必填参数
			if (distributionId == 0) {
				return null;
			}

			// 根据分发ID获取分发
			Distribution distribution = distributionDao.loadById(distributionId);

			// 如果不存在
			if (distribution == null) {
				return null;
			}

			NewsSource newsSource = distribution.getColumn().getNewsSource();

			// 处理模板文章数据
			Article article = distribution.getArticle();
			// 如果不存在
			if (article == null || article.getNewsSource().getNewsSourceId() != newsSource.getNewsSourceId() || !article.getNewsSource().getEnabled()) {
				return null;
			}

			TemplateArticle templateArticle = new TemplateArticle();
			templateArticle.setDistributionId(distributionId);
			templateArticle.setTitle(article.getTitle());
			templateArticle.setSource(article.getSource());
			templateArticle.setUpdateTimeDate(new Date((long) article.getUpdateTime() * 1000));
			templateArticle.setContent(article.getContent());
			templateArticle.setAppkey(xinlangAppkey);
			templateArticle.setCode(code);
			List<Thumbnail> thumbnails = distribution.getThumbnails();
			if (thumbnails != null && thumbnails.size() > 0) {
				Thumbnail thumbnail = thumbnails.get(0);
				templateArticle.setPictureUrl(thumbnailUrl + "w146_h0/" + thumbnail.pictureUrlPath());
			}

			// 添加模板分类标签
			for (CategoryTag categoryTag : article.getCategoryTags()) {
				TemplateCategoryTag templateCategoryTag = new TemplateCategoryTag();
				templateCategoryTag.setCategoryTagId(categoryTag.getCategoryTagId());
				templateCategoryTag.setName(categoryTag.getName());
				templateCategoryTag.setLink(calculateUrl4CategoryTag(categoryTag.getCategoryTagId()));
				templateArticle.getTemplateCategoryTags().add(templateCategoryTag);
				article.getCategoryTagIds().add(categoryTag.getCategoryTagId());
			}

			// 添加模板分发
			// 根据分类标签Id查询相关分发
			List<LazyDistribution> relativeDistributions = new ArrayList<LazyDistribution>();
			if (article.getCategoryTagIds().size() > 0) {
				// 根据分类标签查询分发列表
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSource.getNewsSourceId())));
				predicateQueryFieldList.add(new QueryField("articleId", article.getArticleId(), PredicateEnum.NOT_EQUAL));
				predicateQueryFieldList.add(new QueryField("categoryTags", new QueryField("categoryTagId", article.getCategoryTagIds(), PredicateEnum.IN)));
				predicateQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL)));
				predicateQueryFieldList.add(new QueryField("distributions", new QueryField("firstPublished", Boolean.TRUE)));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", Boolean.FALSE)));
				orderQueryFieldList.add(new QueryField("distributions", new QueryField("distributionId", Boolean.FALSE)));
				relativeDistributions = lazyArticleDao.loadPage(LazyDistribution.class, new QueryField("distributions", null), predicateQueryFieldList, orderQueryFieldList, 0,
						relativeDistributionsCount);
			}

			templateArticle.setRelativeArticles(mappingDistributions2TemplateArticle(relativeDistributions, siteVersion));

			return templateArticle;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 处理三级栏目显示
	 */
	private TemplateColumn getthirdColumns(Column columnShow) {
		try {
			TemplateColumn thirdLevelColumn = null;
			TemplateColumn thirdLevelColumnToReturn = new TemplateColumn();
			List<TemplateColumn> thirdLevelColumnList = new ArrayList<TemplateColumn>();
			if (columnShow != null) {
				Column newColumn = columnDao.loadById(columnShow.getColumnId(), "childColumns");
				if (newColumn.getParentColumn() != null) {
					if (newColumn.getParentColumn().getParentColumn() != null) {
						// 三级栏目
						newColumn.getParentColumn().getParentColumn().getChildColumns().iterator();
						if (newColumn.getParentColumn().getParentColumn().getChildColumns() != null && newColumn.getParentColumn().getParentColumn().getChildColumns().size() > 0) {
							// 区别文藏合作二级栏目
							newColumn.getParentColumn().getParentColumn().getChildColumns().iterator();
							for (Column column : newColumn.getParentColumn().getParentColumn().getChildColumns()) {
								column.getChildColumns().iterator();
								if (column.getChildColumns() == null || column.getChildColumns().size() == 0) {
									thirdLevelColumnList = new ArrayList<TemplateColumn>();
									for (Column tempColumn : newColumn.getParentColumn().getChildColumns()) {
										thirdLevelColumn = new TemplateColumn();
										thirdLevelColumn.setColumnId(tempColumn.getColumnId());
										thirdLevelColumn.setName(tempColumn.getName());
										thirdLevelColumn.setLink(calculateUrl4Column(tempColumn.getColumnId()));
										thirdLevelColumnList.add(thirdLevelColumn);
									}
									thirdLevelColumnToReturn.setChildColumns(thirdLevelColumnList);
									break;
								}
							}
						}
					} else {
						// 二级栏目
						if (newColumn.getChildColumns() != null && newColumn.getChildColumns().size() > 0) {
							// 区别文藏合作二级栏目
							newColumn.getParentColumn().getChildColumns().iterator();
							for (Column column : newColumn.getParentColumn().getChildColumns()) {
								column.getChildColumns().iterator();
								if (column.getChildColumns() == null || column.getChildColumns().size() == 0) {
									thirdLevelColumnList = new ArrayList<TemplateColumn>();
									for (Column tempColumn : newColumn.getChildColumns()) {
										thirdLevelColumn = new TemplateColumn();
										thirdLevelColumn.setColumnId(tempColumn.getColumnId());
										thirdLevelColumn.setName(tempColumn.getName());
										thirdLevelColumn.setLink(calculateUrl4Column(tempColumn.getColumnId()));
										thirdLevelColumnList.add(thirdLevelColumn);
									}
									thirdLevelColumnToReturn.setChildColumns(thirdLevelColumnList);
									break;
								}
							}
						}
					}
				}
			}
			return thirdLevelColumnToReturn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据推荐Id，站点版本Id获取跳转url路径
	 */
	private String getRecommendUrl(int recommendId, SiteVersion siteVersion) {
		Recommend recommend = recommendDao.loadById(recommendId);
		// 处理返回url
		String url = "";
		if (recommend.getLink() == null) {
			if (recommend.getContentTypeEnum() == Recommend.ContentTypeEnum.栏目) {
				// 获取栏目链接
				url = calculateUrl4Column(recommend.getContentId());
			} else if (recommend.getContentTypeEnum() == Recommend.ContentTypeEnum.分发) {
				// 获取分发链接
				url = calculateUrl4Article(recommend.getContentId(), siteVersion);
			} else if (recommend.getContentTypeEnum() == Recommend.ContentTypeEnum.用户) {
				// 获取用户链接
				url = getUserLink(recommend.getContentId());
			}
		} else {
			// 优先设置推荐外部链接
			url = recommend.getLink();
		}
		return url;
	}

	/**
	 * 获取用户链接
	 */
	private String getUserLink(int userId) {
		String url = "";
		return url;
	}

	private void loadBannerInfo(int siteId, SiteVersion siteVersion, int templateMappingType, int contentId, TemplateBaseContent baseContent) {

		List<TemplateColumn> allColumns = columnService.loadAllColumns(siteId, siteVersion.getSiteVersionId());

		// 栏目映射没有处理好,导致栏目的数量不是三个
		if (allColumns.size() != 3) {
			return;
		}

		// 处理当前栏目高亮显示
		if (templateMappingType == TemplateMapping.TypeEnum.栏目.ordinal()) {
			boolean hasChoosed = false;
			for (TemplateColumn templateColumn : allColumns) {
				for (TemplateColumn secondTemplateColumn : templateColumn.getChildColumns()) {
					if (contentId == secondTemplateColumn.getColumnId()) {
						baseContent.setCurrentSecondColumnId(secondTemplateColumn.getColumnId());
						hasChoosed = true;
						break;
					}
					for (TemplateColumn thirdTemplateColumn : secondTemplateColumn.getChildColumns()) {
						if (contentId == thirdTemplateColumn.getColumnId()) {
							baseContent.setCurrentSecondColumnId(secondTemplateColumn.getColumnId());
							hasChoosed = true;
							break;
						}
					}
				}
				if (hasChoosed) {
					break;
				}
			}
		}

		// 设置栏目数据
		baseContent.setHomePageColumn(allColumns.get(0));
		baseContent.setSelectionColumn(allColumns.get(1));
		baseContent.setCooperationColumn(allColumns.get(2));

		// 获取当前栏目数据
		TemplateColumn currentColumn = loadCurrentColumn(templateMappingType, contentId, allColumns, siteVersion);

		// 设置当前栏目数据
		baseContent.setCurrentColumn(currentColumn);
	}

	private void loadCategoryTagArticles(int categoryTagId, SiteVersion siteVersion, int pageNo, TemplateColumnContent content) {

		CategoryTag categoryTag = categoryTagDao.loadById(categoryTagId);
		NewsSource newsSource = categoryTag.getNewsSource();

		// 根据分类标签查询分发列表
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", newsSource.getNewsSourceId())));
		predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("enabled", true)));
		predicateQueryFieldList.add(new QueryField("categoryTags", new QueryField("categoryTagId", categoryTagId, PredicateEnum.IN)));
		predicateQueryFieldList.add(new QueryField("categoryTags", new QueryField("enabled", true)));
		predicateQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL)));
		predicateQueryFieldList.add(new QueryField("distributions", new QueryField("firstPublished", Boolean.TRUE)));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", Boolean.FALSE)));
		orderQueryFieldList.add(new QueryField("distributions", new QueryField("distributionId", Boolean.FALSE)));

		Long totalCount = lazyArticleDao.count(predicateQueryFieldList);
		if ((pageNo + 1) * apiResultsPerPage < totalCount) {
			content.setHasMoreTemplateArticles(true);
		} else {
			content.setHasMoreTemplateArticles(false);
		}

		List<LazyDistribution> distributions = lazyArticleDao.loadPage(LazyDistribution.class, new QueryField("distributions", null), predicateQueryFieldList, orderQueryFieldList,
				pageNo * apiResultsPerPage, apiResultsPerPage);
		// 如果无结果
		if (distributions.isEmpty()) {
			content.setTemplateArticles(new ArrayList<TemplateArticle>());
		}

		for (LazyDistribution distribution : distributions) {
			// 所属栏目ID
			distribution.setColumnId(distribution.getColumn().getColumnId());

			// 包含的缩略图列表
			distribution.getThumbnails().iterator();
		}

		content.setTemplateArticles(mappingDistributions2TemplateArticle(distributions, siteVersion));
	}

	private void loadColumnDistributions(Column column, SiteVersion siteVersion, int pageNo, TemplateColumnContent content) {

		// TODO:方法耗时时间过长

		// 根据栏目ID获取已发布分发列表
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		if (column.getChildColumns().size() == 0) {
			predicateQueryFieldList.add(new QueryField("column", new QueryField("columnId", column.getColumnId())));
		} else {
			predicateQueryFieldList.add(new QueryField("column", new QueryField("parentColumn", new QueryField("columnId", column.getColumnId()))));
		}
		predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		if (column.getChildColumns().size() == 0) {
			orderQueryFieldList.add(new QueryField("position", Boolean.FALSE));
		}
		orderQueryFieldList.add(new QueryField("publishTime", Boolean.FALSE));
		orderQueryFieldList.add(new QueryField("distributionId", Boolean.FALSE));

		Long totalCount = distributionDao.count(predicateQueryFieldList);
		if ((pageNo + 1) * apiResultsPerPage < totalCount) {
			content.setHasMoreTemplateArticles(true);
		} else {
			content.setHasMoreTemplateArticles(false);
		}

		List<LazyDistribution> distributions = lazyDistributionDao.loadPage(predicateQueryFieldList, orderQueryFieldList, pageNo * apiResultsPerPage, apiResultsPerPage);

		// 如果无结果
		if (distributions.isEmpty()) {
			content.setTemplateArticles(new ArrayList<TemplateArticle>());
		}

		for (LazyDistribution distribution : distributions) {
			// 所属栏目ID
			distribution.setColumnId(distribution.getColumn().getColumnId());

			// 包含的缩略图
			distribution.getThumbnails().iterator();
		}

		// 调整指定排序位置
		if (column.getChildColumns().size() == 0 && pageNo == 0 && distributions.size() > 0) {
			while (distributions.get(0).getPosition() != null && distributions.get(0).getPosition() > 0) {
				LazyDistribution distribution = distributions.get(0);
				distributions.remove(distribution);
				distributions.add(Math.min(distribution.getPosition(), distributions.size()), distribution);
			}
		}

		content.setTemplateArticles(mappingDistributions2TemplateArticle(distributions, siteVersion));
	}

	private TemplateColumn loadCurrentColumn(int staticPageContentType, int contentId, List<TemplateColumn> columns, SiteVersion siteVersion) {
		TemplateColumn currentColumnToReturn = new TemplateColumn();
		Column currentColumn = new Column();
		TemplateColumn navigationColumn = new TemplateColumn();
		if (staticPageContentType == StaticPage.ContentTypeEnum.栏目.ordinal()) {

			currentColumn = columnDao.loadById(contentId);

			// 获取导航栏一、二级栏目数据
			navigationColumn = getNavigationColumns(currentColumn, columns, siteVersion);
			// 设置导航栏一、二级栏目数据
			currentColumnToReturn.setColumnId(currentColumn.getColumnId());

			TemplateColumn parentColumn = new TemplateColumn();
			if (navigationColumn != null) {
				parentColumn.setName(navigationColumn.getName());
				if (navigationColumn.getChildColumns() != null && navigationColumn.getChildColumns().size() > 0) {
					currentColumnToReturn.setName(navigationColumn.getChildColumns().get(0).getName());
					if (navigationColumn.getChildColumns().get(0).getChildColumns() != null && navigationColumn.getChildColumns().get(0).getChildColumns().size() > 0) {
						currentColumnToReturn.getChildColumns().addAll(navigationColumn.getChildColumns().get(0).getChildColumns());
					}
				}
			}
			currentColumnToReturn.setParentColumn(parentColumn);
		} else if (staticPageContentType == StaticPage.ContentTypeEnum.分发.ordinal()) {

			Distribution distribution = distributionDao.loadById(contentId);
			currentColumn = distribution.getColumn();

			// 获取导航栏一、二级栏目数据
			navigationColumn = getNavigationColumns(currentColumn, columns, siteVersion);
			// 设置导航栏一、二级栏目数据
			currentColumnToReturn.setColumnId(currentColumn.getColumnId());

			TemplateColumn parentColumn = new TemplateColumn();
			if (navigationColumn != null) {
				parentColumn.setName(navigationColumn.getName());
				if (navigationColumn.getChildColumns() != null && navigationColumn.getChildColumns().size() > 0) {
					currentColumnToReturn.setName(navigationColumn.getChildColumns().get(0).getName());
				}
			}
			currentColumnToReturn.setParentColumn(parentColumn);
		} else if (staticPageContentType == StaticPage.ContentTypeEnum.分类标签.ordinal()) {
			CategoryTag categoryTag = categoryTagDao.loadById(contentId);
			// 设置导航栏一、二级栏目数据
			// TODO: 分类标签Id 如何在页面上回显
			currentColumnToReturn.setColumnId(categoryTag.getCategoryTagId());
			currentColumnToReturn.setName(categoryTag.getName());
			TemplateColumn parentColumn = new TemplateColumn();
			parentColumn.setName("搜索");
			currentColumnToReturn.setParentColumn(parentColumn);
		}
		return currentColumnToReturn;
	}

	private TemplateBaseContent loadStaticArticleData(int contentId, SiteVersion siteVersion, Integer templateVersionId) {

		TemplateArticleContent content = new TemplateArticleContent();

		Distribution distribution = distributionDao.loadById(contentId);
		Column column = distribution.getColumn();
		NewsSource newsSource = column.getNewsSource();

		Site site = siteVersion.getSite();

		loadBannerInfo(site.getSiteId(), siteVersion, StaticPage.ContentTypeEnum.分发.ordinal(), distribution.getDistributionId(), content);

		content.setTitle(distribution.getArticle().getTitle() + "_" + siteVersion.getSite().getName());

		TemplateArticle templateArticle = getTemplateArticleBydistributionId(distribution.getDistributionId(), siteVersion);

		content.setTemplateArticle(templateArticle);

		TemplateVersion templateVersion = getTemplateVersion(TemplateMapping.TypeEnum.文章.ordinal(), distribution.getDistributionId(), siteVersion.getSiteVersionId(),
				templateVersionId);

		if (templateVersion != null) {
			content.setTemplateVersionId(templateVersion.getTemplateVersionId());// 设置模板版本ID
			content.setTemplateFile(new File(getDataPath4Tomcat(), templateVersion.templateVersionFilePath()));
		}

		// 静态页面文件路径
		content.setStaticFilePath(calculatePath4Article(contentId, siteVersion));
		content.setNewsSourceId(newsSource.getNewsSourceId());

		// 设置正文分享url
		String articleUrl = (siteVersion.getSite().getWebAddress() + content.getStaticFilePath()).replace("\\", "/");
		content.getTemplateArticle().setArticleUrl(articleUrl);
		return content;
	}

	private TemplateBaseContent loadStaticCategoryTagData(int contentId, SiteVersion siteVersion, Integer templateVersionId, Integer pageNo) {

		TemplateColumnContent content = new TemplateColumnContent();
		CategoryTag categoryTag = categoryTagDao.loadById(contentId);

		Site site = siteVersion.getSite();

		// 设置模板
		TemplateVersion templateVersion = getTemplateVersion(TemplateMapping.TypeEnum.其它.ordinal(), categoryTag.getCategoryTagId(), siteVersion.getSiteVersionId(),
				templateVersionId);

		if (templateVersion == null) {
			return content;
		}

		content.setTemplateVersionId(templateVersion.getTemplateVersionId());// 设置模板版本ID

		content.setTemplateFile(new File(getDataPath4Tomcat(), templateVersion.templateVersionFilePath()));

		if (pageNo == null) {

			loadBannerInfo(site.getSiteId(), siteVersion, StaticPage.ContentTypeEnum.分类标签.ordinal(), categoryTag.getCategoryTagId(), content);

			content.setTitle(categoryTag.getName() + "_" + siteVersion.getSite().getName());

			// 静态页面文件路径
			content.setStaticFilePath(calculatePath4CategoryTag(contentId));

			// 回源地址
			content.setStaticSourceUrl(staticSourceUrl);
		} else {
			loadCategoryTagArticles(contentId, siteVersion, pageNo, content);

			// 静态页面文件路径
			content.setStaticFilePath(calculatePath4CategoryTagArticles(contentId, pageNo));
		}

		return content;
	}

	/**
	 * 加载栏目静态化的数据<br>
	 * 主要有两类,栏目的框架数据以及栏目的列表数据
	 */
	private TemplateBaseContent loadStaticColumnData(int contentId, SiteVersion siteVersion, Integer templateVersionId, Integer pageNo) {

		TemplateColumnContent content = new TemplateColumnContent();

		Column column = columnDao.loadById(contentId);
		Site site = siteVersion.getSite();

		// 设置模板
		TemplateVersion templateVersion = getTemplateVersion(TemplateMapping.TypeEnum.栏目.ordinal(), column.getColumnId(), siteVersion.getSiteVersionId(), templateVersionId);

		if (templateVersion == null) {
			return content;
		}

		content.setTemplateVersionId(templateVersion.getTemplateVersionId());// 设置模板版本ID

		content.setTemplateFile(new File(getDataPath4Tomcat(), templateVersion.templateVersionFilePath()));

		if (pageNo == null) {// 栏目的框架页面

			loadBannerInfo(site.getSiteId(), siteVersion, StaticPage.ContentTypeEnum.栏目.ordinal(), column.getColumnId(), content);

			content.setTitle(column.getName() + "_" + site.getName());

			// 获取推荐组数据
			TemplateColumn templateColumnWithInRecommend = getRecommends(column.getNewsSource().getNewsSourceId(), column.getColumnId(), siteVersion);

			if (templateColumnWithInRecommend != null) {
				// 设置推荐组数据
				content.setTemplateRecommends(templateColumnWithInRecommend.getTemplateRecommends());
			}

			// 获取三级栏目
			TemplateColumn thirdLevelColumnToReturn = getthirdColumns(column);
			// 设置三级栏目
			content.setThirdLevelColumns(thirdLevelColumnToReturn.getChildColumns());

			content.setStaticFilePath(calculatePath4Column(contentId));

			// 回源地址
			content.setStaticSourceUrl(staticSourceUrl);
		} else {
			// 获取文章列表
			loadColumnDistributions(column, siteVersion, pageNo, content);
			content.setStaticFilePath(calculatePath4ColumnArticles(contentId, pageNo));
		}

		return content;
	}

	@Override
	public TemplateBaseContent loadStaticData(int contentId, int templateMappingType, int siteVersionId, Integer pageNo) {
		return loadStaticData(contentId, templateMappingType, siteVersionId, null, pageNo);
	}

	@Override
	public TemplateBaseContent loadStaticData(int contentId, int templateMappingType, int siteVersionId, Integer templateVersionId, Integer pageNo) {

		// TODO:测试数据
		long start = System.currentTimeMillis();

		TemplateBaseContent content = null;

		SiteVersion siteVersion = siteVersionDao.loadById(siteVersionId);

		if (TemplateMapping.TypeEnum.栏目.ordinal() == templateMappingType) {
			content = loadStaticColumnData(contentId, siteVersion, templateVersionId, pageNo);

		} else if (TemplateMapping.TypeEnum.文章.ordinal() == templateMappingType) {
			content = loadStaticArticleData(contentId, siteVersion, templateVersionId);

		} else if (TemplateMapping.TypeEnum.其它.ordinal() == templateMappingType) {
			content = loadStaticCategoryTagData(contentId, siteVersion, templateVersionId, pageNo);
		}

		// 静态页面文件路径
		if (siteVersion.getStatusEnum() == SiteVersion.StatusEnum.已发布) {// 只有在已发布的情况下,才向Nginx下发送数据
			content.setNginxFolder(getStaticPagesFolderNginx(siteVersion));
		}

		content.setTomcatFolder(getStaticPagesFolderTomcat(siteVersion));

		// 设置当前站点版本Id
		content.setSiteVersionId(siteVersionId);

		System.out.println("加载静态化数据耗时:" + (System.currentTimeMillis() - start));

		return content;
	}

	/**
	 * 加载可以发布的静态化数据
	 * 
	 * @param contentId 内容ID
	 * @param templateMappingType 内容类型
	 * @param pageNo 页码,可以为空
	 */
	private List<TemplateBaseContent> loadStaticDatas(int contentId, int templateMappingType, Integer pageNo) {

		List<Site> sites = null;// 要处理的站点

		if (TemplateMapping.TypeEnum.文章.ordinal() == templateMappingType) {

			Distribution distribution = distributionDao.loadById(contentId);
			Column column = distribution.getColumn();
			NewsSource newsSource = column.getNewsSource();
			sites = newsSource.getSites();

		} else if (TemplateMapping.TypeEnum.栏目.ordinal() == templateMappingType) {

			Column column = columnDao.loadById(contentId);
			NewsSource newsSource = column.getNewsSource();
			sites = newsSource.getSites();

		} else if (TemplateMapping.TypeEnum.其它.ordinal() == templateMappingType) {

			CategoryTag tag = categoryTagDao.loadById(contentId);
			NewsSource newsSource = tag.getNewsSource();
			sites = newsSource.getSites();
		}

		List<TemplateBaseContent> result = new ArrayList<>();

		for (Site site : sites) {

			if (!site.getEnabled()) {// 只处理可用的站点
				continue;
			}

			List<SiteVersion> siteVersions = site.getSiteVersions();

			for (SiteVersion siteVersion : siteVersions) {

				if (siteVersion.getStatusEnum() != SiteVersion.StatusEnum.已发布 && siteVersion.getStatusEnum() != SiteVersion.StatusEnum.测试中) {
					continue;
				}

				result.add(loadStaticData(contentId, templateMappingType, siteVersion.getSiteVersionId(), pageNo));
			}
		}

		return result;
	}

	/**
	 * 将分发映射为TemplateArticle
	 * 
	 * @param distributions 分发列表
	 * @return
	 */
	private List<TemplateArticle> mappingDistributions2TemplateArticle(List<LazyDistribution> distributions, SiteVersion siteVersion) {

		List<TemplateArticle> templateArticles = new ArrayList<>();
		for (LazyDistribution distribution : distributions) {

			LazyArticle article = distribution.getArticle();

			TemplateArticle templateArticle = new TemplateArticle();

			templateArticle.setTitle(article.getTitle());

			// 缩略图

			if (distribution.getPosition() != null && distribution.getPosition() == 0) {
				templateArticle.setIsTop(true);
			}

			List<LazyThumbnail> thumbnails = distribution.getThumbnails();
			if (thumbnails != null && thumbnails.size() > 0) {
				LazyThumbnail thumbnail = thumbnails.get(0);
				templateArticle.setPictureUrl(thumbnailUrl + "w146_h0/" + thumbnail.pictureUrlPath());
			}

			templateArticle.setListThumbnailRatio(distribution.getListThumbnailRatio());

			templateArticle.setArticleUrl(calculateUrl4Article(distribution.getDistributionId(), siteVersion));

			templateArticle.setSource(article.getSource());
			templateArticle.setUpdateTimeDate(distribution.getPublishTimeDate());

			templateArticles.add(templateArticle);
		}

		return templateArticles;
	}
}