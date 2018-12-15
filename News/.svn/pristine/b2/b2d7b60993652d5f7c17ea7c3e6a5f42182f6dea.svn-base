/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.cccollector.news.model.Column;
import com.cccollector.news.model.Article;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.Distribution.BodyTemplateTypeEnum;
import com.cccollector.news.model.Distribution.ListTemplateTypeEnum;
import com.cccollector.news.util.TemplateUtil;
import com.cccollector.news.model.Media;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.news.model.Thumbnail;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.dao.QueryField.PredicateEnum;
import com.cccollector.universal.util.ImageUtils;

/**
 * 分发Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class DistributionBean extends BaseEditBean<Distribution> implements Serializable {

	private static final long serialVersionUID = 2121103737305965733L;

	public DistributionBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Distribution>() {

			@Override
			public Distribution loadModel(Integer modelId) {
				Distribution distribution = null;
				if (modelId == null) {
					distribution = new Distribution();
					distribution.setListTemplateType(ListTemplateTypeEnum.无图.ordinal());
					distribution.setListThumbnailRatio(Column.ThumbnailRatioEnum.方图1_1.ordinal());
					distribution.setBodyTemplateType(BodyTemplateTypeEnum.图文混排.ordinal());
					distribution.setFirstPublished(false);
					distribution.setArticle(getArticle());
					distribution.setColumn(new Column());
				} else {
					distribution = distributionService.loadById(modelId);	
				}
				return distribution;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return articleService.loadById(relatedModelId);
				} else if (index == 2) {
					return newsSourceService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 分发
	 */
	public Distribution getDistribution() {
		return getModel();
	}
	
	/**
	 * 所属文章
	 */
	public Article getArticle() {
		return (Article) relatedModel(1);
	}

	/**
	 * 所属新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(2);
	}

	/**
	 * 未使用的栏目
	 */
	public List<Column> getNotUsedColumns() {
		List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
		predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
		predicateQueryFieldList.add(new QueryField("distributions", new QueryField("article", new QueryField("articleId", getArticle().getArticleId(), PredicateEnum.NOT_IN))));
		List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
		orderQueryFieldList.add(new QueryField("code", true));
		return columnService.loadAll(predicateQueryFieldList, orderQueryFieldList);
	}

	/**
	 * 更新列表缩略图比例
	 */
	public void updateListThumbnailRatioAction() {
		Distribution distribution = getDistribution();
		Column column = columnService.loadById(distribution.getColumn().getColumnId());
		distribution.setListThumbnailRatio(column.getThumbnailRatio());
	}

	/**
	 * 裁切缩略图
	 */
	private boolean cropThumbnails;

	public boolean getCropThumbnails() {
		return cropThumbnails;
	}

	public void setCropThumbnails(boolean _cropThumbnails) {
		cropThumbnails = _cropThumbnails;
	}

	/**
	 * 保存
	 */
	public void saveAction() {
		Distribution distribution = getDistribution();
		Article article = getArticle();
		boolean success = true;
		if (distribution.getDistributionId() == null) {
			// 如果所选栏目包含子栏目，则返回
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("parentColumn", new QueryField("columnId", distribution.getColumn().getColumnId())));
			Long count = columnService.count(predicateQueryFieldList);
			if (count > 0) {
				addValidatingMessage("不能在包含子栏目的栏目中添加分发！");
				return;
			}
			
			// 设置验证时间
			distribution.setValidateTimeDate((new Date()));
			
			// 添加分发
			Integer distributionId = distributionService.save(distribution);
			success = distributionId != null;
			addInfoMessageToFlash("添加分发成功！");
		} else {
			// 编辑分发
			Distribution distributionUpdated = distributionService.update(distribution);
			success = distributionUpdated != null;
			
			if(success) {
				if(distributionUpdated.getPublished()) {
					Column column = distributionUpdated.getColumn();
					// 执行静态化方法
					templateService.staticPageCase(distribution.getDistributionId(), TemplateMapping.TypeEnum.文章.ordinal(), null);
					// 加入到定时任务
					List<Integer> columnIds = new ArrayList<>();
					columnIds.add(column.getColumnId());
					TemplateUtil.addTobeStaticColumns(columnIds);
				}
			}

			addInfoMessageToFlash("编辑分发成功！");
		}
		
		// 裁切缩略图
		if (success && cropThumbnails) {
			article = articleService.loadById(article.getArticleId(), "medias");
			int thumbnailCount = distribution.getListThumbnailCount();
			if (thumbnailCount == Integer.MAX_VALUE) {
				thumbnailCount = article.getMedias().size();
			}
			for (int i = 0; i < thumbnailCount; i++) {
				try {
					Media media = article.getMedias().get(i);
					Thumbnail thumbnail = new Thumbnail();
					thumbnail.setDistribution(distribution);
					thumbnail.setWidthRatio(distribution.getListThumbnailWidthRatio());
					thumbnail.setHeightRatio(distribution.getListThumbnailHeightRatio());

					// 设置存储路径
					MessageDigest messageDigest = MessageDigest.getInstance("MD5");
					messageDigest.update(new Date().toString().getBytes());
			        String prefix = new BigInteger(1, messageDigest.digest()).toString(16);
			        thumbnail.setPath(prefix.substring(0, 2) + "/" + prefix.substring(2, 4) + "/" );

					// 设置排序位置
					List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("distribution", new QueryField("distributionId", distribution.getDistributionId())));
					Integer position = thumbnailService.max("position", predicateQueryFieldList);
					thumbnail.setPosition(position == null ? 0 : position.intValue() + 1);
					
					// 添加缩略图
					thumbnailService.save(thumbnail);
					
					// 计算裁切尺寸
					int width = media.getWidth();
					int height = (int) Math.floor(width * 1.0 * thumbnail.getHeightRatio() / thumbnail.getWidthRatio());
					if (height > media.getHeight()) {
						height = media.getHeight();
						width = (int) Math.floor(height * 1.0 * thumbnail.getWidthRatio() / thumbnail.getHeightRatio());
					}
					
					// 裁切
					String mediaFilePath = mediaService.getDataPath() + media.pictureFilePath();
					String thumbnailFilePath = thumbnailService.getDataPath() + thumbnail.pictureFilePath();	
					ImageUtils.cropImage(mediaFilePath, thumbnailFilePath, thumbnailService.getImageMagickPath(), width, height);
				} catch (Exception e) {}
			}
		}

		if (getUsingDialog()) {
			PrimeFaces.current().dialog().closeDynamic(null);
		} else {
			handleNavigation("distributionList.xhtml?multiSelect=true");
		}
	}
}
