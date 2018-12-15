/**
 * 
 */
package com.cccollector.news.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.ColumnDao;
import com.cccollector.news.dao.DistributionDao;
import com.cccollector.news.dao.RecommendDao;
import com.cccollector.news.dao.UserDao;
import com.cccollector.news.model.Recommend;
import com.cccollector.news.service.RecommendService;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 推荐服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("recommendService")
public class RecommendServiceImpl extends GenericServiceHibernateImpl<Integer, Recommend> implements RecommendService {

	@Autowired
	private RecommendDao recommendDao;
	
	@Autowired
	private ColumnDao columnDao;
	
	@Autowired
	private DistributionDao distributionDao;
	
	@Autowired
	private UserDao userDao;
	
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
	
	@Override
	public String getImageMagickPath() {
		return imageMagickPath;
	}
	
	@Override
	public String getTempPath() {
		return System.getProperty(webAppRootKey) + webTemp + File.separator;
	}
	
	@Override
	public String getDataPath() {
		return tomcatDataPath + platformBundleId + File.separator;
	}

	public String getImagePath() {
		return nginxDataPath + platformBundleId + File.separator + image + File.separator;
	}

	@Override
	public String getPictureThumbnailRecommendsUrl(Recommend recommend) {
		return thumbnailUrl + "w0_h100/" + recommend.pictureUrlPath();
	}

	@Override
	public List<?> loadContentsBySearchWord(Recommend recommend, String searchWord) {
		List<?> allContents = null;
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		Integer id = 0;
		try {
			id = Integer.valueOf(searchWord);
		} catch (Exception e) {}
		switch (recommend.getContentTypeEnum()) {
		case 栏目:
			predicateQueryFieldList.add(new QueryField("name", searchWord, QueryField.PredicateEnum.LIKE));
			if (id > 0) {
				predicateQueryFieldList.add(new QueryField("columnId", id, QueryField.PredicateEnum.EQUAL, QueryField.BooleanOperatorEnum.OR));
			}
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", recommend.getRecommendGroup().getNewsSource().getNewsSourceId())));
			orderQueryFieldList.add(new QueryField("code", true));
			allContents = columnDao.loadAll(predicateQueryFieldList, orderQueryFieldList);
			break;
		case 分发:
			predicateQueryFieldList.add(new QueryField("article", new QueryField("title", searchWord, QueryField.PredicateEnum.LIKE)));
			if (id > 0) {
				predicateQueryFieldList.add(new QueryField("distributionId", id, QueryField.PredicateEnum.EQUAL, QueryField.BooleanOperatorEnum.OR));
			}
			predicateQueryFieldList.add(new QueryField("article", new QueryField("newsSource", new QueryField("newsSourceId", recommend.getRecommendGroup().getNewsSource().getNewsSourceId()))));
			predicateQueryFieldList.add(new QueryField("publishTime", null, QueryField.PredicateEnum.IS_NOT_NULL));
			orderQueryFieldList.add(new QueryField("publishTime", false));
			orderQueryFieldList.add(new QueryField("distributionId", false));
			allContents = distributionDao.loadPage(predicateQueryFieldList, orderQueryFieldList, 0, 20);
			break;
		case 用户:
			predicateQueryFieldList.add(new QueryField("nickName", searchWord, QueryField.PredicateEnum.LIKE));
			predicateQueryFieldList.add(new QueryField("cellphone", searchWord, QueryField.PredicateEnum.LIKE, QueryField.BooleanOperatorEnum.OR));
			predicateQueryFieldList.add(new QueryField("email", searchWord, QueryField.PredicateEnum.LIKE, QueryField.BooleanOperatorEnum.OR));
			orderQueryFieldList.add(new QueryField("registerTime", Boolean.FALSE));
			if (id > 0) {
				predicateQueryFieldList.add(new QueryField("userId", id, QueryField.PredicateEnum.EQUAL, QueryField.BooleanOperatorEnum.OR));
			}
			allContents = userDao.loadPage(predicateQueryFieldList, orderQueryFieldList, 0, 20);
			break;
		default:
			break;
		}
		return allContents;
	}

	@Override
	public void addRecommend(Recommend recommend, String pictureFilePath) {
		// 设置排序位置
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("recommendGroup", new QueryField("recommendGroupId", recommend.getRecommendGroup().getRecommendGroupId())));
		Integer position = recommendDao.max("position", predicateQueryFieldList);
		recommend.setPosition(position == null ? 0 : position.intValue() + 1);
		recommend.setUpdateTimeDate(new Date());
		
		// 保存推荐
		recommendDao.save(recommend);

		// 处理图片文件
		if (recommend.getRecommendGroup().getPictureRatio() != null) {
			File pictureFile = new File(getDataPath() + recommend.pictureFilePath());	
			File tempFile = new File(pictureFilePath);
			pictureFile.getParentFile().mkdirs();
			try {
				Files.copy(tempFile.toPath(), pictureFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void moveRecommends(List<Recommend> recommends) {
		int position = 0;
		for (Recommend recommend : recommends) {
			recommend.setPosition(position);
			recommendDao.update(recommend, "position");
			position++;
		}
	}

	@Override
	public void updateRecommend(Recommend recommend, String pictureFilePath) {
		// 获取推荐最新数据
		Recommend recommendNew = recommendDao.loadById(recommend.getRecommendId());
		
		// 更新推荐
		recommendNew.setTitle(recommend.getTitle());
		recommendNew.setLink(recommend.getLink());

		// 处理图片文件
		if (recommendNew.getRecommendGroup().getPictureRatio() != null) {
			File pictureFile = new File(getDataPath() + recommend.pictureFilePath());	
			if (pictureFilePath.startsWith(getTempPath())) {
				File tempFile = new File(pictureFilePath);
				pictureFile.getParentFile().mkdirs();
				try {
					Files.copy(tempFile.toPath(), pictureFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					tempFile.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			// 清除nginx缓存文件
			File[] sizeFiles = new File(getImagePath()).listFiles();
			if (sizeFiles != null) {
				for (File sizeFile : sizeFiles) {
					File pictureImageFile = new File(sizeFile, recommend.pictureFilePath());
					pictureImageFile.delete();
				}
			}
		}
	}

	@Override
	public void deleteRecommend(Recommend recommend) {
		// 删除图片文件
		File pictureFile = new File(getDataPath() + recommend.pictureFilePath());
		if (pictureFile.exists()) {
			pictureFile.delete();
		}
		
		// 清除nginx缓存文件
		File[] sizeFiles = new File(getImagePath()).listFiles();
		if (sizeFiles != null) {
			for (File sizeFile : sizeFiles) {
				File pictureImageFile = new File(sizeFile, recommend.pictureFilePath());
				pictureImageFile.delete();
			}
		}
		
		// 删除推荐
		recommendDao.delete(recommend);
	}
}
