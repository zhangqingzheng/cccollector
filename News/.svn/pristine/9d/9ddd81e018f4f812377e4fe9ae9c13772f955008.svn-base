/**
 * 
 */
package com.cccollector.news.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.CategoryTagDao;
import com.cccollector.news.dao.ColumnDao;
import com.cccollector.news.dao.DistributionDao;
import com.cccollector.news.dao.SiteVersionDao;
import com.cccollector.news.dao.StaticPageDao;
import com.cccollector.news.model.CategoryTag;
import com.cccollector.news.model.Column;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.SiteVersion;
import com.cccollector.news.model.StaticPage;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.news.service.ColumnService;
import com.cccollector.news.service.SiteVersionService;
import com.cccollector.news.service.TemplateService;
import com.cccollector.news.templatemodel.TemplateColumn;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 站点版本服务实现类
 *
 * @author 谢朋 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("siteVersionService")
public class SiteVersionServiceImpl extends GenericServiceHibernateImpl<Integer, SiteVersion> implements SiteVersionService {

	@Autowired
	private SiteVersionDao siteVersionDao;

	@Autowired
	private DistributionDao distributionDao;

	@Autowired
	private CategoryTagDao categoryTagDao;

	@Autowired
	private ColumnDao columnDao;

	@Autowired
	private StaticPageDao staticPageDao;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private ColumnService columnService;

	/**
	 * Tomcat数据目录
	 */
	@Value("${paths.tomcatDataPath}")
	private String tomcatDataPath;

	/**
	 * 平台标识符
	 */
	@Value("${platformBundleId}")
	private String platformBundleId;

	@Override
	@CacheEvict("templateColumns")
	public void staticPageAction(int siteVersionId) {
		SiteVersion siteVersion = siteVersionDao.loadById(siteVersionId);
		siteVersionDao.getEntityManager().detach(siteVersion);

		// 获取站点下的所有分发
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("column", new QueryField("newsSource", new QueryField("sites", new QueryField("siteId", siteVersion.getSite().getSiteId())))));
		predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("publishTime", false));
		// TODO:20条测试
		// List<Distribution> distributions = distributionDao.loadPage(predicateQueryFieldList, null, 0, 20);
		List<Distribution> distributions = distributionDao.loadAll(predicateQueryFieldList, orderQueryFieldList);

		for (Distribution distribution : distributions) {
			// 静态化外壳
			templateService.staticPageCase(distribution.getDistributionId(), TemplateMapping.TypeEnum.文章.ordinal(), siteVersionId);
		}

		// 获取站点下的所有栏目
		List<TemplateColumn> columns = columnService.loadAllColumns(siteVersion.getSite().getSiteId(), siteVersion.getSiteVersionId());
		List<Integer> columnList = new ArrayList<>();
		// 静态化
		for (TemplateColumn firstColumn : columns) {
			for (TemplateColumn secondColumn : firstColumn.getChildColumns()) {
				columnList.add(secondColumn.getColumnId());
				for (TemplateColumn thirdColumn : secondColumn.getChildColumns()) {
					columnList.add(thirdColumn.getColumnId());
				}
			}
		}

		for (Integer columnId : columnList) {
			Column column = columnDao.loadById(columnId);
			// 静态化外壳
			templateService.staticPageCase(columnId, TemplateMapping.TypeEnum.栏目.ordinal(), siteVersionId);
			if (!(column.getTemplateTypeEnum() == Column.TemplateTypeEnum.子栏目列表 || column.getTemplateTypeEnum() == Column.TemplateTypeEnum.顶部栏目选择_子栏目列表
					|| column.getTemplateTypeEnum() == Column.TemplateTypeEnum.顶部栏目选择_顶部轮播推荐_子栏目列表)) {
				templateService.staticPageContent(column.getColumnId(), TemplateMapping.TypeEnum.栏目.ordinal(), siteVersionId);
			}
		}

		// 获取所有的分类标签
		predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("articles", new QueryField("distributions",
				new QueryField("column", new QueryField("newsSource", new QueryField("sites", new QueryField("siteId", siteVersion.getSite().getSiteId())))))));
		List<CategoryTag> categoryTags = categoryTagDao.loadAll(predicateQueryFieldList, null);
		Set<CategoryTag> categoryTagSet = new HashSet<>();
		categoryTagSet.addAll(categoryTags);
		// 静态化
		for (CategoryTag categoryTag : categoryTagSet) {
			// 静态化外壳
			templateService.staticPageCase(categoryTag.getCategoryTagId(), TemplateMapping.TypeEnum.其它.ordinal(), siteVersionId);
			if (categoryTag.getArticles() != null && categoryTag.getArticles().size() > 0) {
				// 静态化列表页Json数据
				templateService.staticPageContent(categoryTag.getCategoryTagId(), TemplateMapping.TypeEnum.其它.ordinal(), siteVersionId);
			}
		}
	}

	@Override
	@CacheEvict("templateColumns")
	public void columnStaticPageAction(int siteVersionId) {
		// 获取站点下的所有栏目
		SiteVersion siteVersion = siteVersionDao.loadById(siteVersionId);
		siteVersionDao.getEntityManager().detach(siteVersion);
		List<TemplateColumn> columns = columnService.loadAllColumns(siteVersion.getSite().getSiteId(), siteVersion.getSiteVersionId());
		List<Integer> columnList = new ArrayList<>();
		// 静态化
		for (TemplateColumn firstColumn : columns) {
			for (TemplateColumn secondColumn : firstColumn.getChildColumns()) {
				columnList.add(secondColumn.getColumnId());
				for (TemplateColumn thirdColumn : secondColumn.getChildColumns()) {
					columnList.add(thirdColumn.getColumnId());
				}
			}
		}

		for (Integer columnId : columnList) {
			Column column = columnDao.loadById(columnId);
			// 静态化外壳
			templateService.staticPageCase(columnId, TemplateMapping.TypeEnum.栏目.ordinal(), siteVersionId);
			if (!(column.getTemplateTypeEnum() == Column.TemplateTypeEnum.子栏目列表 || column.getTemplateTypeEnum() == Column.TemplateTypeEnum.顶部栏目选择_子栏目列表
					|| column.getTemplateTypeEnum() == Column.TemplateTypeEnum.顶部栏目选择_顶部轮播推荐_子栏目列表)) {
				templateService.staticPageContent(column.getColumnId(), TemplateMapping.TypeEnum.栏目.ordinal(), siteVersionId);
			}
		}
	}

	@Override
	@CacheEvict("templateColumns")
	public void articleStaticPageAction(SiteVersion siteVersion) {

		long start = System.currentTimeMillis();

		// 获取站点下的所有分发
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("column", new QueryField("newsSource", new QueryField("sites", new QueryField("siteId", siteVersion.getSite().getSiteId())))));
		predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("publishTime", false));
		// TODO:20条测试
		// List<Distribution> distributions = distributionDao.loadPage(predicateQueryFieldList, null, 0, 20);
		List<Distribution> distributions = distributionDao.loadAll(predicateQueryFieldList, orderQueryFieldList);

		for (Distribution distribution : distributions) {
			// 静态化外壳
			templateService.staticPageCase(distribution.getDistributionId(), TemplateMapping.TypeEnum.文章.ordinal(), siteVersion.getSiteVersionId());
		}

		System.out.println("静态化页面耗时:" + (System.currentTimeMillis() - start) / 1000 + "秒");
	}

	@Override
	@CacheEvict("templateColumns")
	public void categoryTagStaticPageAction(SiteVersion siteVersion) {
		// 获取所有的分类标签
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("articles", new QueryField("distributions",
				new QueryField("column", new QueryField("newsSource", new QueryField("sites", new QueryField("siteId", siteVersion.getSite().getSiteId())))))));
		List<CategoryTag> categoryTags = categoryTagDao.loadAll(predicateQueryFieldList, null);
		Set<CategoryTag> categoryTagSet = new HashSet<>();
		categoryTagSet.addAll(categoryTags);
		// 静态化
		for (CategoryTag categoryTag : categoryTagSet) {
			// 静态化外壳
			templateService.staticPageCase(categoryTag.getCategoryTagId(), TemplateMapping.TypeEnum.其它.ordinal(), siteVersion.getSiteVersionId());
			if (categoryTag.getArticles() != null && categoryTag.getArticles().size() > 0) {
				// 静态化列表页Json数据
				templateService.staticPageContent(categoryTag.getCategoryTagId(), TemplateMapping.TypeEnum.其它.ordinal(), siteVersion.getSiteVersionId());
			}
		}
	}

	@Override
	public void addSiteVersion(SiteVersion siteVersion) {
		// 设置版本号
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("site", new QueryField("siteId", siteVersion.getSite().getSiteId())));
		Integer version = siteVersionDao.max("version", predicateQueryFieldList);
		siteVersion.setVersion(version == null ? 1 : version.intValue() + 1);

		// 添加站点版本
		siteVersionDao.save(siteVersion);
	}

	@Override
	public void testSiteVersion(SiteVersion siteVersion) {
		// 查询全部测试中的模板
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("site", new QueryField("siteId", siteVersion.getSite().getSiteId())));
		predicateQueryFieldList.add(new QueryField("status", SiteVersion.StatusEnum.测试中.ordinal()));
		List<SiteVersion> siteVersionList = siteVersionDao.loadAll(predicateQueryFieldList, null);
		if (siteVersionList != null && siteVersionList.size() > 0) {
			for (SiteVersion siteVersionTemp : siteVersionList) {
				siteVersionTemp.setStatus(SiteVersion.StatusEnum.制作中.ordinal());
				// 更新状态
				siteVersionDao.update(siteVersionTemp, "status");
			}
		}
		if (siteVersion.getStatus() == SiteVersion.StatusEnum.制作中.ordinal()) {
			// 设置状态
			siteVersion.setStatus(SiteVersion.StatusEnum.测试中.ordinal());
			// 更新状态
			siteVersionDao.update(siteVersion, "status");

		}
	}

	@Override
	public void publishSiteVersion(SiteVersion siteVersion) {
		// 查询同一模板下已发布的站点版本
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("site", new QueryField("siteId", siteVersion.getSite().getSiteId())));
		predicateQueryFieldList.add(new QueryField("status", SiteVersion.StatusEnum.已发布.ordinal()));
		List<SiteVersion> siteVersionList = siteVersionDao.loadAll(predicateQueryFieldList, null);
		if (siteVersionList != null && siteVersionList.size() > 0) {
			for (SiteVersion temp : siteVersionList) {
				temp.setStatus(SiteVersion.StatusEnum.待撤销.ordinal());
				siteVersionDao.update(temp, "status");
			}
		}

		// 设置发布状态
		siteVersion.setStatus(SiteVersion.StatusEnum.已发布.ordinal());
		// 更新发布状态
		siteVersionDao.update(siteVersion, "status");

		// 发布站点版本的时候,将数据从Tomcat下复制到Nginx下
		File nginxFolder = getStaticPagesFolderNginx(siteVersion);
		File tomcatFolder = getStaticPagesFolderTomcat(siteVersion);
		try {
			Path source = Paths.get(tomcatFolder.toString());
			Path target = Paths.get(nginxFolder.toString());
			// 源文件夹非目录
			if(source == null||!Files.isDirectory(source))
	            throw new IllegalArgumentException("source must be directory");
			Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

			            @Override
			            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			                // 在目标文件夹中创建dir对应的子文件夹
			            	Path subDir =null;
			            	if (dir.compareTo(source) == 0) {
			            		subDir = target;
			            	} else {
			            		subDir = target.resolve(dir.subpath(source.getNameCount(), dir.getNameCount()));
			            	}

			                Files.createDirectories(subDir);			                	
			                return FileVisitResult.CONTINUE;
			            }

			            @Override
			            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			            	Files.copy(file, target.resolve(file.subpath(source.getNameCount(), file.getNameCount())),StandardCopyOption.REPLACE_EXISTING);
			                return FileVisitResult.CONTINUE;
			            }
			        });
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 更新分发表中的数据
		predicateQueryFieldList = new ArrayList<>();
		predicateQueryFieldList.add(new QueryField("contentType", StaticPage.ContentTypeEnum.分发.ordinal()));
		predicateQueryFieldList.add(new QueryField("siteVersion", new QueryField("siteVersionId", siteVersion.getSiteVersionId())));

		List<StaticPage> staticPages = staticPageDao.loadAll(predicateQueryFieldList, null);
		Map<Integer, String> cache = new HashMap<>();

		for (StaticPage staticPage : staticPages) {
			cache.put(staticPage.getContentId(), staticPage.getFilePath());
		}

		for (Map.Entry<Integer, String> entry : cache.entrySet()) {

			Distribution distribution = new Distribution();
			distribution.setDistributionId(entry.getKey());
			distribution.setStaticPageUrl(entry.getValue().replace("\\", "/"));

			distributionDao.update(distribution, "staticPageUrl");
		}
	}

	@Override
	public void cancelRevokingSiteVersion(SiteVersion siteVersion) {
		// 查询同一模板下已发布的站点版本
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("site", new QueryField("siteId", siteVersion.getSite().getSiteId())));
		predicateQueryFieldList.add(new QueryField("status", SiteVersion.StatusEnum.已发布.ordinal()));
		List<SiteVersion> siteVersionList = siteVersionDao.loadAll(predicateQueryFieldList, null);
		if (siteVersionList != null && siteVersionList.size() > 0) {
			for (SiteVersion temp : siteVersionList) {
				temp.setStatus(SiteVersion.StatusEnum.待撤销.ordinal());
				siteVersionDao.update(temp, "status");
			}
		}

		// 设置站点版本状态
		siteVersion.setStatus(SiteVersion.StatusEnum.已发布.ordinal());
		// 更新站点版本状态
		siteVersionDao.update(siteVersion, "status");
	}

	@Override
	public void revokeSiteVersion(SiteVersion siteVersion) {
		// 设置站点版本状态
		siteVersion.setStatus(SiteVersion.StatusEnum.已撤销.ordinal());
		// TODO:站点版本从待撤销到已撤销需要删除nginx下已发布的文件数据

		// 更新站点版本状态
		siteVersionDao.update(siteVersion, "status");
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
	 * 获取Tomcat的数据存储路径
	 */
	private String getDataPath4Tomcat() {
		return tomcatDataPath + platformBundleId + File.separator;
	}
}
