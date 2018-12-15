/**
 * 
 */
package com.cccollector.news.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;

import com.cccollector.news.model.Column;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.Media;
import com.cccollector.news.model.Recommend;
import com.cccollector.news.model.RecommendGroup;
import com.cccollector.news.model.TemplateMapping;
import com.cccollector.news.model.User;
import com.cccollector.news.model.UserApp;
import com.cccollector.universal.dao.QueryField;
import com.cccollector.universal.util.ImageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 推荐Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class RecommendBean extends BaseEditBean<Recommend> implements Serializable {

	private static final long serialVersionUID = 6158219460323709766L;

	public RecommendBean () {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Recommend>() {

			@Override
			public Recommend loadModel(Integer modelId) {
				Recommend recommend = null;
				if (modelId == null) {
					recommend = new Recommend();
					recommend.setContentType(Recommend.ContentTypeEnum.分发.ordinal());
					recommend.setStatus(Recommend.StatusEnum.未发布.ordinal());
					recommend.setValidating(false);
					recommend.setRecommendGroup(getRecommendGroup());
				} else {
					recommend = recommendService.loadById(modelId);

					// 设置关联内容
					switch (recommend.getContentTypeEnum()) {
					case 栏目:
						Column column = columnService.loadById(recommend.getContentId());
						recommend.setRelativeContent(column);
						break;
					case 分发:
						Distribution distribution = distributionService.loadById(recommend.getContentId());
						recommend.setRelativeContent(distribution);
						break;
					case 用户:
						User user = userService.loadById(recommend.getContentId());
						recommend.setRelativeContent(user);
						break;
					}

					// 处理图片
					loadPictureFile(1, recommendService.getDataPath() + recommend.pictureFilePath());
				}
				return recommend;
			}

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return recommendGroupService.loadById(relatedModelId);
				}
				return null;
			}
		};
	}

	/**
	 * 推荐
	 */
	public Recommend getRecommend() {
		return getModel();
	}

	/**
	 * 所属推荐组
	 */
	public RecommendGroup getRecommendGroup() {
		return (RecommendGroup) relatedModel(1);
	}

	/**
	 * 处理关联内容类别选中
	 */
	public void handleContentTypeSelect() {
		getRecommend().setContentId(null);
		getRecommend().setContentKeyValues(null);
		pictureFilePathMap.put(1, null);
		pictureImageMap.put(1, null);
	}

	/**
	 * 全部内容
	 */
	private List<?> allContents;

	/**
	 * 处理关联内容自动完成
	 */
	public List<?> handleContentComplete(String query) {
		allContents = recommendService.loadContentsBySearchWord(getRecommend(), query);
		return allContents;
	}

	/**
	 * 处理关联内容选中
	 */
	public void handleContentIdSelect(SelectEvent event) {
		Recommend recommend = getRecommend();
		RecommendGroup recommendGroup = getRecommendGroup();
		int contentId = Integer.parseInt(event.getObject().toString());
		for (Object content : allContents) {
			switch (recommend.getContentTypeEnum()) {
			case 栏目:
				Column column = (Column) content;
				if (column.getColumnId() == contentId) {
					recommend.setTitle(column.getName());
					return;
				}
				break;
			case 分发:
				Distribution distribution = (Distribution) content;
				if (distribution.getDistributionId() == contentId) {
					recommend.setTitle(distribution.getArticle().getTitle());
					
					// 设置关联内容键值
					try {
						Map<String, Object> contentKeyMap = new HashMap<String, Object>();
						contentKeyMap.put("bodyTemplateType", distribution.getBodyTemplateType());
						ObjectMapper objectMapper = new ObjectMapper();
						String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contentKeyMap);
						recommend.setContentKeyValues(json);
					} catch (JsonProcessingException e1) {}
					
					if (recommendGroup.getPictureRatio() == null || (distribution.getArticle().getPictureCount() == 0 && distribution.getArticle().getAudioCount() == 0 && distribution.getArticle().getVideoCount() == 0)) {
						return;
					}
					// 获取多媒体
					List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
					predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", distribution.getArticle().getArticleId())));
					List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
					orderQueryFieldList.add(new QueryField("position", true));
					List<Media> medias = mediaService.loadAll(predicateQueryFieldList, orderQueryFieldList);
					Media media = medias.get(0);
					String mediaFilePath = mediaService.getDataPath()+ media.pictureFilePath();
					
					// 计算裁切尺寸
					int width = media.getWidth();
					int height = (int) Math.floor(width * 1.0 * recommendGroup.getPictureHeightRatio() / recommendGroup.getPictureWidthRatio());
					if (height > media.getHeight()) {
						height = media.getHeight();
						width = (int) Math.floor(height * 1.0 * recommendGroup.getPictureWidthRatio() / recommendGroup.getPictureHeightRatio());
					}
					
					// 裁切
					try {
						new File(recommendService.getTempPath()).mkdirs();
						String tempFileName = new Date().getTime() + "_" + new Random().nextInt(10000) + ".jpg";
						pictureFilePathMap.put(1, recommendService.getTempPath() + tempFileName);
						ImageUtils.cropImage(mediaFilePath, pictureFilePathMap.get(1), recommendService.getImageMagickPath(), width, height);
						FileInputStream fileInputStream = new FileInputStream(pictureFilePathMap.get(1));
						pictureImageMap.put(1, new DefaultStreamedContent(fileInputStream, "image/jpeg"));					
						pictureInfoMap.put(1, new SimpleImageInfo(new File(pictureFilePathMap.get(1))));
					} catch (Exception e) {
						pictureFilePathMap.remove(1);
						pictureImageMap.remove(1);
						pictureInfoMap.remove(1);
					}
					return;
				}
				break;
			case 用户:
				User user = (User) content;
				if (user.getUserId() == contentId) {
					recommend.setTitle(user.getNickName());
					
					// 设置关联内容键值
					try {
						User userWithUserApps = userService.loadById(user.getUserId(), "userApps");
						Map<Integer, Map<String, Object>> contentKeyMap = new HashMap<Integer, Map<String, Object>>();
						for (UserApp userApp : userWithUserApps.getUserApps()) {
							Map<String, Object> userAppMap = new HashMap<String, Object>();
							userAppMap.put("userAppId", userApp.getUserAppId());
							userAppMap.put("appId", userApp.getApp().getAppId());
							contentKeyMap.put(userApp.getApp().getAppId(), userAppMap);
						}
						ObjectMapper objectMapper = new ObjectMapper();
						String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contentKeyMap);
						recommend.setContentKeyValues(json);
					} catch (JsonProcessingException e1) {}
					return;
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 保存
	 */
	public void saveAction() {
		Recommend recommend = getRecommend();
		// 是否包含图片
		if (getRecommendGroup().getPictureRatio() != null && pictureFilePath(1) == null) {
			addValidatingMessage("请先上传图片，再保存推荐！");
			return;
		}

		// 置空
		if (recommend.getLink().equals("")) {
			recommend.setLink(null);
		}

		// 设置更新时间
		recommend.setUpdateTimeDate(new Date());

		if (recommend.getRecommendId() == null) {
			// 添加推荐
			recommendService.addRecommend(recommend, pictureFilePath(1));

			// 刷新推荐对应的栏目静态页面
			RecommendGroup recommendGroup = recommend.getRecommendGroup();
			if (RecommendGroup.OwnerTypeEnum.栏目.ordinal() == recommendGroup.getOwnerType()) {
				templateService.staticPageCase(recommendGroup.getOwnerId(), TemplateMapping.TypeEnum.栏目.ordinal(), null);
			}

			addInfoMessageToFlash("添加推荐成功！");
		} else {
			// 编辑推荐
			recommendService.updateRecommend(recommend, pictureFilePath(1));

			// 刷新推荐对应的栏目静态页面
			RecommendGroup recommendGroup = recommend.getRecommendGroup();
			if (RecommendGroup.OwnerTypeEnum.栏目.ordinal() == recommendGroup.getOwnerType()) {
				templateService.staticPageCase(recommendGroup.getOwnerId(), TemplateMapping.TypeEnum.栏目.ordinal(), null);
			}

			addInfoMessageToFlash("编辑推荐成功！");
		}

		handleNavigation("recommendList.xhtml?multiSelect=true");
	}
}
