/**
 * 
 */
package com.cccollector.news.service.impl;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.ArticleDao;
import com.cccollector.news.dao.ColumnDao;
import com.cccollector.news.dao.DistributionDao;
import com.cccollector.news.model.Article;
import com.cccollector.news.model.CategoryTag;
import com.cccollector.news.model.Column;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.Media;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.SiteVersion;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.news.service.ArticleService;
import com.cccollector.news.service.ColumnService;
import com.cccollector.news.service.SiteVersionService;
import com.cccollector.news.service.TemplateService;
import com.cccollector.news.templatemodel.TemplateColumn;
import com.cccollector.universal.app.model.UserDetail;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.model.UniversalJsonViews;
import com.cccollector.universal.service.GenericServiceHibernateImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 文章服务实现类
 *
 * @author 谢朋 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("articleService")
public class ArticleServiceImpl extends GenericServiceHibernateImpl<Integer, Article> implements ArticleService {

	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private DistributionDao distributionDao;
	
	@Autowired
	private ColumnDao columnDao;

	@Autowired
	private SiteVersionService siteVersionService;
	
	@Autowired
	private ColumnService columnService;
	
	@Autowired
	private TemplateService templateService;

	/**
	 * 预定义栏目
	 */
	LinkedHashMap<String, Column> predefinedColumns = new LinkedHashMap<>();

	/**
	 * 平台标识符
	 */
	@Value("${platformBundleId}")
	private String platformBundleId;

	/**
	 * nginx数据目录
	 */
	@Value("${paths.nginxDataPath}")
	private String nginxDataPath;

	/**
	 * 发布路径
	 */
	@Value("${paths.publish}")
	private String publish;

	/**
	 * 新闻源路径
	 */
	@Value("${paths.newsSources}")
	private String newsSources;

	/**
	 * 文章路径
	 */
	@Value("${paths.articles}")
	private String articles;

	/**
	 * 发布URL
	 */
	@Value("${paths.publishUrl}")
	private String publishUrl;

	@Override
	public String getPublishPath() {
		return nginxDataPath + platformBundleId + File.separator + publish + File.separator;
	}

	@Override
	public String getArticlesPath(NewsSource newsSource) {
		return newsSources + File.separator + newsSource.getNewsSourceId() + File.separator + articles + File.separator;
	}

	@Override
	public String getPublishArticlesPath(NewsSource newsSource) {
		return getPublishPath() + getArticlesPath(newsSource);
	}

	@Override
	public String getJsonPublishArticlesUrl(NewsSource newsSource) {
		return publishUrl + newsSources + "/" + newsSource.getNewsSourceId() + "/" + articles + "/";
	}

	@Override
	public void addArticle(Article article) {
		// 设置存储路径
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(new Date().toString().getBytes());
			String prefix = new BigInteger(1, messageDigest.digest()).toString(16);
			article.setPath(prefix.substring(0, 2) + "/" + prefix.substring(2, 4) + "/");
		} catch (NoSuchAlgorithmException e) {}

		// 设置存储后缀
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(new Date().toString().getBytes());
			String suffix = new BigInteger(1, messageDigest.digest()).toString(16);
			article.setSuffix(suffix.substring(suffix.length() - 6, suffix.length()));
		} catch (NoSuchAlgorithmException e) {}

		// 设置编辑信息
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetail) {
			UserDetail userDetail = (UserDetail) principal;
			article.setEditor(userDetail.getRealName());
			article.setEditorId(userDetail.getUserId());
		} else {
			article.setEditor((String) principal);
			article.setEditorId(0);
		}
		article.setEditTimeDate((new Date()));

		// 添加文章
		articleDao.save(article);
	}

	@Override
	public void updateArticle(Article article) {
		// 获取文章最新数据
		Article articleNew = articleDao.loadById(article.getArticleId(), "categoryTags");

		// 更新文章
		articleNew.setTitle(article.getTitle());
		articleNew.setSummary(article.getSummary());
		articleNew.setAuthor(article.getAuthor());
		articleNew.setSource(article.getSource());
		articleNew.setKeywords(article.getKeywords());
		articleNew.setContent(article.getContent());
		articleNew.setUpdateTime(article.getUpdateTime());
		articleNew.setCategoryTags(article.getCategoryTags());

		// 获取文章下所有已发布分发所属的栏目数据
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("distributions", new QueryField("article",  new QueryField("articleId", articleNew.getArticleId()))));
		predicateQueryFieldList.add(new QueryField("distributions", new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL)));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("columnId", true));
		List<Column> columns = columnDao.loadAll(predicateQueryFieldList, orderQueryFieldList);
		
		// 栏目列表静态化
		for (Column column : columns) {
			templateService.staticPageContent(column.getColumnId(), TemplateMapping.TypeEnum.栏目.ordinal(), null);				
		}
		// 文章正文静态化
		predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("article",  new QueryField("articleId", articleNew.getArticleId())));
		predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
		orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("publishTime", false));
		List<Distribution> distributions = distributionDao.loadAll(predicateQueryFieldList, orderQueryFieldList);
		for (Distribution distribution : distributions) {
			templateService.staticPageContent(distribution.getDistributionId(), TemplateMapping.TypeEnum.文章.ordinal(), null);
		}
		
		// 标签列表静态化
		List<CategoryTag> categoryTags = articleNew.getCategoryTags();
		for (CategoryTag categoryTag : categoryTags) {
			templateService.staticPageContent(categoryTag.getCategoryTagId(), TemplateMapping.TypeEnum.其它.ordinal(), null);
		}
	}

	@Override
	public void updateArticleMediaCount(Article article) {
		// 获取文章最新数据
		Article articleNew = articleDao.loadById(article.getArticleId(), "medias");

		// 重新计算多媒体数量，并更新
		articleNew.setPictureCount(0);
		articleNew.setAudioCount(0);
		articleNew.setVideoCount(0);
		for (Media media : articleNew.getMedias()) {
			if (media.getTypeEnum() == Media.TypeEnum.图片) {
				articleNew.setPictureCount(articleNew.getPictureCount() + 1);
			} else if (media.getTypeEnum() == Media.TypeEnum.音频) {
				articleNew.setAudioCount(articleNew.getAudioCount() + 1);
			} else if (media.getTypeEnum() == Media.TypeEnum.视频) {
				articleNew.setVideoCount(articleNew.getVideoCount() + 1);
			}
		}
		articleNew.setUpdateTimeDate(new Date());
	}

	@Override
	public void updateArticleJson(Article article) {
		// 获取文章最新数据
		Article articleNew = articleDao.loadById(article.getArticleId(), "medias", "categoryTags");
		// 如果未发布，则返回
		if (!articleNew.getPublished()) {
			return;
		}

		// 包含的分类标签ID
		for (CategoryTag categoryTag : articleNew.getCategoryTags()) {
			articleNew.getCategoryTagIds().add(categoryTag.getCategoryTagId());
		}

		// 将文章数据写入JSON文件
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			byte[] articleNewBytes = objectMapper.writerWithView(UniversalJsonViews.StaticPublicPrivate.class).withDefaultPrettyPrinter().writeValueAsBytes(articleNew);
			File jsonPublishFile = new File(getPublishArticlesPath(articleNew.getNewsSource()) + articleNew.jsonFilePath());
			jsonPublishFile.getParentFile().mkdirs();
			Files.write(jsonPublishFile.toPath(), articleNewBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteArticleJson(Article article) {
		// 获取文章最新数据
		Article articleNew = articleDao.loadById(article.getArticleId());
		// 如果未发布，则删除文章JSON文件
		if (!articleNew.getPublished()) {
			File jsonPublishFile = new File(getPublishArticlesPath(article.getNewsSource()) + article.jsonFilePath());
			jsonPublishFile.delete();
		}
	}
   
	/**
	 * 替换图片路径宽度
	 */
    public String changePictureWidth(String htmlStr) {
        // 正则匹配img标签
        String regEx_img = "<img(.*?)>";
        Pattern pattern = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlStr);
        StringBuffer sb = new StringBuffer();
        // 匹配到img标签
        while (matcher.find()) {
        	StringBuffer sbreplace = new StringBuffer("");
        	String group = matcher.group();
        	Matcher mSrc = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(group);
        	// 匹配到src标签
        	if (mSrc.find()) {
        		String src = mSrc.group().replace("w0", "w768");
        		mSrc.appendReplacement(sbreplace, src);
        		mSrc.appendTail(sbreplace);
        		matcher.appendReplacement(sb, sbreplace.toString());
        	}
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
