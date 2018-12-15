/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.NewsSource;
import com.cccollector.universal.view.GenericListBean;

/**
 * 分发发布Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class DistributionPublishBean extends BaseListBean<Distribution> implements Serializable {

	private static final long serialVersionUID = 7763594937300683028L;

	public DistributionPublishBean () {
		modelDisplayName = "分发";
		modelAttributeName = "distribution";
		modelIdAttributeName = "distributionId";
		genericListBeanHandler = new GenericListBean.GenericListBeanHandlerRelatedAll<Distribution>() {

			@Override
			public List<Distribution> loadAllModelList() {
				return null;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return newsSourceService.loadById(relatedModelId);
				} else if (index == 2) {
					Distribution distribution = distributionService.loadById(relatedModelId);
					
					// 设置发布时间
					distribution.setPublishTimeDate((new Date()));
					
					// 设置定时发布时间
					if (getNewsSource() != null && getNewsSource().getDefaultSchedule() != null) {
						try {
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							String dateFormatString = dateFormat.format(new Date());
							SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
							String timeFormatString = timeFormat.format(getNewsSource().getDefaultSchedule());
							SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							distribution.setScheduledTimeDate(dateTimeFormat.parse(dateFormatString + " " + timeFormatString));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					return distribution;
				}
				return null;
			}
		};
	}
	
	/**
	 * 所属新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(1);
	}

	/**
	 * 分发
	 */
	public Distribution getDistribution() {
		return (Distribution) relatedModel(2);
	}
	
	/**
	 * 定时发布是否开启
	 */
	private Boolean scheduledPublishingEnabled;

	public Boolean getScheduledPublishingEnabled() {
		return scheduledPublishingEnabled;
	}

	public void setScheduledPublishingEnabled(Boolean _scheduledPublishingEnabled) {
		scheduledPublishingEnabled = _scheduledPublishingEnabled;
	}

	/**
	 * 发布
	 */
	public void publishAction() {
		Distribution distribution = getDistribution();
		// 如果校验分发缩略图失败
		if (!distributionService.validateDistributionThumbnails(distribution)) {
			addValidatingMessage("缩略图数量或宽高比例不对，请补充完整！");
			return;
		}
		
        // 发布分发
		if (scheduledPublishingEnabled == null || !scheduledPublishingEnabled) {
			distributionService.publishDistribution(distribution, SecurityContextHolder.getContext().getAuthentication().getPrincipal(), distribution.getPublishTimeDate(), null);
		} else {
			distributionService.publishDistribution(distribution, SecurityContextHolder.getContext().getAuthentication().getPrincipal(), null, distribution.getScheduledTimeDate());
		}
		
		// 更新文章JSON文件
		articleService.updateArticleJson(distribution.getArticle());
		
		PrimeFaces.current().dialog().closeDynamic(true);
	}
}
