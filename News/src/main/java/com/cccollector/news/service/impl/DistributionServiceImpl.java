/**
 * 
 */
package com.cccollector.news.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.ArticleDao;
import com.cccollector.news.dao.DistributionDao;
import com.cccollector.news.dao.ThumbnailDao;
import com.cccollector.news.model.Column;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.news.model.Thumbnail;
import com.cccollector.news.service.DistributionService;
import com.cccollector.news.service.TemplateService;
import com.cccollector.news.util.TemplateUtil;
import com.cccollector.universal.app.model.UserDetail;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 分发服务实现类
 *
 * @author 谢朋 Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("distributionService")
public class DistributionServiceImpl extends GenericServiceHibernateImpl<Integer, Distribution> implements DistributionService {

	@Autowired
	private DistributionDao distributionDao;

	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private ThumbnailDao thumbnailDao;

	@Autowired
	private TemplateService templateService;

	/**
	 * 平台标识符
	 */
	@Value("${platformBundleId}")
	private String platformBundleId;

	/**
	 * ImageMagick命令路径
	 */
	@Value("${imageMagickPath}")
	private String imageMagickPath;

	/**
	 * Tomcat数据目录
	 */
	@Value("${paths.tomcatDataPath}")
	private String tomcatDataPath;

	/**
	 * nginx数据目录
	 */
	@Value("${paths.nginxDataPath}")
	private String nginxDataPath;

	/**
	 * 应用根路径键
	 */
	@Value("${paths.webAppRootKey}")
	private String webAppRootKey;

	/**
	 * 临时路径
	 */
	@Value("${paths.webTemp}")
	private String webTemp;

	/**
	 * 图像路径
	 */
	@Value("${paths.image}")
	private String image;

	/**
	 * 缩略图URL
	 */
	@Value("${paths.thumbnailUrl}")
	private String thumbnailUrl;

	public String getImageMagickPath() {
		return imageMagickPath;
	}

	public String getTempPath() {
		return System.getProperty(webAppRootKey) + webTemp + File.separator;
	}

	public String getDataPath() {
		return tomcatDataPath + platformBundleId + File.separator;
	}

	public String getImagePath() {
		return nginxDataPath + platformBundleId + File.separator + image + File.separator;
	}

	@Override
	public boolean validateDistributionThumbnails(Distribution distribution) {
		// 获取分发及缩略图
		distribution = distributionDao.loadById(distribution.getDistributionId(), "thumbnails");

		// 如果缩略图数量小于分发的列表缩略图数量，则返回失败
		int listThumbnailCount = distribution.getListThumbnailCount();
		if ((listThumbnailCount == Integer.MAX_VALUE && distribution.getThumbnails().size() == 0)
				|| (listThumbnailCount != Integer.MAX_VALUE && distribution.getThumbnails().size() != listThumbnailCount)) {
			return false;
		}

		// 如果缩略图宽高比例不对，则返回
		int widthRatio = distribution.getListThumbnailWidthRatio();
		int heightRatio = distribution.getListThumbnailHeightRatio();
		for (Thumbnail thumbnail : distribution.getThumbnails()) {
			if (thumbnail.getWidthRatio() != widthRatio || thumbnail.getHeightRatio() != heightRatio) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void publishDistribution(Distribution distribution, Object principal, Date publishTime, Date scheduledTime) {
		// 设置发布人信息
		if (principal instanceof UserDetail) {
			UserDetail userDetail = (UserDetail) principal;
			distribution.setPublisher(userDetail.getRealName());
			distribution.setPublisherId(userDetail.getUserId());
		} else {
			distribution.setPublisher((String) principal);
			distribution.setPublisherId(0);
		}

		// 设置发布时间
		if (publishTime != null) {
			distribution.setPublishTimeDate(publishTime);
			distribution.setScheduledTime(null);

			// 查询同一篇文章的已发布分发中是否有首发
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", distribution.getArticle().getArticleId())));
			predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
			predicateQueryFieldList.add(new QueryField("firstPublished", true));
			Long count = distributionDao.count(predicateQueryFieldList);
			// 设置是否首发
			distribution.setFirstPublished(count == 0);
		} else {
			distribution.setPublishTime(null);
			distribution.setScheduledTimeDate(scheduledTime);
			distribution.setFirstPublished(false);
		}

		// 更新分发
		distributionDao.update(distribution, "publisher", "publisherId", "publishTime", "scheduledTime", "firstPublished");

		// 更新文章是否已发布
		if (publishTime != null) {
			distribution.getArticle().setPublished(true);
			articleDao.update(distribution.getArticle(), "published");
		}
		
		if (distribution.getPublished()) {
			Column column = distribution.getColumn();
			// 执行静态化方法
			templateService.staticPageCase(distribution.getDistributionId(), TemplateMapping.TypeEnum.文章.ordinal(), null);
			// 加入到定时任务
			List<Integer> columnIds = new ArrayList<>();
			columnIds.add(column.getColumnId());
			TemplateUtil.addTobeStaticColumns(columnIds);
		}
	}

	@Override
	public boolean unpublishDistribution(Distribution distribution) {
		// 如果分发是首发
		boolean firstPublished = distribution.getFirstPublished();
		if (firstPublished) {
			// 查询同一篇文章除当前分发外的已发布分发数量
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", distribution.getArticle().getArticleId())));
			predicateQueryFieldList.add(new QueryField("publishTime", null, PredicateEnum.IS_NOT_NULL));
			predicateQueryFieldList.add(new QueryField("distributionId", distribution.getDistributionId(), PredicateEnum.NOT_EQUAL));
			Long count = distributionDao.count(predicateQueryFieldList);
			// 如果同一篇文章有不止一个已发布分发，只能先撤销发布非首发的分发
			if (count > 0) {
				return false;
			}
		}

		// 设置发布人信息
		distribution.setPublisher(null);
		distribution.setPublisherId(null);
		distribution.setPublishTime(null);
		distribution.setScheduledTime(null);

		// 设置是否首发
		distribution.setFirstPublished(false);

		// 设置指定排序位置
		distribution.setPosition(null);

		// 更新分发
		distributionDao.update(distribution, "publisher", "publisherId", "publishTime", "scheduledTime", "firstPublished", "position");

		// 更新文章是否已发布
		if (firstPublished) {
			distribution.getArticle().setPublished(false);
			articleDao.update(distribution.getArticle(), "published");
		}

		Column column = distribution.getColumn();
		// 加入到定时任务
		List<Integer> columnIds = new ArrayList<>();
		columnIds.add(column.getColumnId());
		TemplateUtil.addTobeStaticColumns(columnIds);

		return true;
	}

	@Override
	public void deleteDistribution(Distribution distribution) {
		// 删除缩略图
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("distribution", new QueryField("distributionId", distribution.getDistributionId())));
		List<Thumbnail> thumbnails = thumbnailDao.loadAll(predicateQueryFieldList, null);
		for (Thumbnail thumbnail : thumbnails) {
			// 删除图片文件
			File pictureFile = new File(getDataPath() + thumbnail.pictureFilePath());
			if (pictureFile.exists()) {
				pictureFile.delete();
			}

			// 清除nginx缓存文件
			File[] sizeFiles = new File(getImagePath()).listFiles();
			if (sizeFiles != null) {
				for (File sizeFile : sizeFiles) {
					File pictureImageFile = new File(sizeFile, thumbnail.pictureFilePath());
					pictureImageFile.delete();
				}
			}
		}

		// 删除缩略图
		thumbnailDao.deleteAll(thumbnails);

		// 删除分发
		distributionDao.deleteById(distribution.getDistributionId());
	}

	@Override
	public List<Distribution> loadDistributionsBySearchWord(String searchWord) {
		// 根据搜索词查询分发列表
		int id = 0;
		try {
			id = Integer.valueOf(searchWord);
		} catch (Exception e) {}
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		if (id > 0) {
			predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", id, PredicateEnum.EQUAL, QueryField.BooleanOperatorEnum.OR)));
			predicateQueryFieldList.add(new QueryField("distributionId", id, PredicateEnum.EQUAL, QueryField.BooleanOperatorEnum.OR));
		}
		predicateQueryFieldList.add(new QueryField("article", new QueryField("title", searchWord, PredicateEnum.LIKE, QueryField.BooleanOperatorEnum.OR)));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("distributionId", false));
		List<Distribution> distributions = distributionDao.loadPage(predicateQueryFieldList, orderQueryFieldList, 0, 20);
		if (distributions.size() == 0) {
			return null;
		}
		
		return distributions;
	}
}
