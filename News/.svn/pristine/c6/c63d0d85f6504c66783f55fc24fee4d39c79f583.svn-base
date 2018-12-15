/**
 * 
 */
package com.cccollector.news.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DualListModel;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cccollector.news.model.Article;
import com.cccollector.news.model.CategoryTag;
import com.cccollector.news.model.Media;
import com.cccollector.news.model.NewsSource;
import com.cccollector.news.model.Style;
import com.cccollector.universal.app.model.UserDetail;
import com.cccollector.universal.dao.QueryField;

/**
 * 文章Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class ArticleBean extends BaseEditBean<Article> implements Serializable {

	private static final long serialVersionUID = -5433909094181489046L;

	public ArticleBean() {
		genericEditBeanHandler = new GenericEditBeanHandlerModelRelated<Article>() {

			@Override
			public Object loadRelatedModel(int index, int relatedModelId) {
				if (index == 1) {
					return newsSourceService.loadById(relatedModelId);
				}
				return null;
			}
			
			@Override
			public Article loadModel(Integer modelId) {
				Article article = null;
				if (modelId == null) {
					article = new Article();
					article.setNewsSource(getNewsSource());
					article.setPictureCount(0);
					article.setAudioCount(0);
					article.setVideoCount(0);
					article.setPublished(false);
					article.setViewCount(0);
					article.setFavoriteCount(0);
					article.setCommentCount(0);
					article.setReplyCount(0);
					article.setLikeCount(0);
					article.setDislikeCount(0);
				} else {
					article = articleService.loadById(modelId, "categoryTags");
				}
				
				// 处理分类标签
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("code", true));
				List<CategoryTag> allCategoryTags = categoryTagService.loadAll(predicateQueryFieldList, orderQueryFieldList);

				// 将分类标签放入分类标签双列表模型
				Map<Integer, CategoryTag> selectedCategoryTagMap = new HashMap<Integer, CategoryTag>();
				for (CategoryTag categoryTag : article.getCategoryTags()) {
					selectedCategoryTagMap.put(categoryTag.getCategoryTagId(), categoryTag);
				}
				categoryTagDualListModel = new DualListModel<CategoryTag>(new ArrayList<CategoryTag>(), new ArrayList<CategoryTag>());
				for (CategoryTag categoryTag : allCategoryTags) {
					if (selectedCategoryTagMap.get(categoryTag.getCategoryTagId()) != null) {
						categoryTagDualListModel.getTarget().add(categoryTag);
					} else {
						categoryTagDualListModel.getSource().add(categoryTag);
					}
				}
				return article;
			}
		};
	}

	/**
	 * 文章
	 */
	public Article getArticle() {
		return getModel();
	}

	/**
	 * 所属新闻源
	 */
	public NewsSource getNewsSource() {
		return (NewsSource) relatedModel(1);
	}

	/**
	 * 选中的样式模版类别
	 */
	private Integer selectedStyleTemplateType;

	public Integer getSelectedStyleTemplateType() {
		return selectedStyleTemplateType;
	}

	public void setSelectedStyleTemplateType(Integer _selectedStyleTemplateType) {
		selectedStyleTemplateType = _selectedStyleTemplateType;
	}

	/**
	 * 所有样式
	 */
	private List<Style> allStyles;
	
	public List<Style> getAllStyles() {
		if (allStyles == null) {
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("newsSource", new QueryField("newsSourceId", getNewsSource().getNewsSourceId())));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("templateType", true));			
			allStyles = styleService.loadAll(predicateQueryFieldList, orderQueryFieldList);
			if (allStyles.size() > 0) {
				selectedStyleTemplateType = allStyles.get(0).getTemplateType();
			}
		}
		return allStyles;
	}

	/**
	 * 获取样式CSS文件URL
	 */
	public String getSelectedStyleCssFilePath() {
		if (selectedStyleTemplateType != null) {
			List<Style> styles = getAllStyles();
			for (Style style : styles) {
				if (style.getTemplateType().equals(selectedStyleTemplateType)) {
					return styleService.getCssPublishStylesUrl(style);
				}
			}
		}
		return null;
	}

	/**
	 * 选中的多媒体ID
	 */
	private Integer selectedMediaId;

	public Integer getSelectedMediaId() {
		return selectedMediaId;
	}

	public void setSelectedMediaId(Integer _selectedMediaId) {
		selectedMediaId = _selectedMediaId;
	}

	/**
	 * 所有多媒体
	 */
	private List<Media> allMedias;
	
	public List<Media> getAllMedias() {
		if (allMedias == null && getArticle().getArticleId() != null) {
			List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
			predicateQueryFieldList.add(new QueryField("article", new QueryField("articleId", getArticle().getArticleId())));
			List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
			orderQueryFieldList.add(new QueryField("position", true));
			allMedias = mediaService.loadAll(predicateQueryFieldList, orderQueryFieldList);
		}
		return allMedias;
	}

	/**
	 * 获取多媒体图片文件URL
	 */
	public String getSelectedMediaPictureFilePath() {
		if (selectedMediaId != null) {
			List<Media> medias = getAllMedias();
			for (Media media : medias) {
				if (media.getMediaId().intValue() == selectedMediaId) {
					return mediaService.getMediaPictureThumbnailUrl(media);
				}
			}
		}
		return null;
	}

	/**
	 * 分类标签双列表模型
	 */
	private DualListModel<CategoryTag> categoryTagDualListModel;

	public DualListModel<CategoryTag> getCategoryTagDualListModel() {
		return categoryTagDualListModel;
	}

	public void setCategoryTagDualListModel(DualListModel<CategoryTag> _categoryTagDualListModel) {
		categoryTagDualListModel = _categoryTagDualListModel;
	}
	
	/**
	 * 保存
	 */
	public void saveAction() {
		Article article = getArticle();
		// 置空
		if (article.getSummary().equals("")) {
			article.setSummary(null);
		}
		if (article.getAuthor().equals("")) {
			article.setAuthor(null);
		}
		if (article.getSource().equals("")) {
			article.setSource(null);
		}
		if (article.getKeywords().equals("")) {
			article.setKeywords(null);
		}
		if (article.getContent().equals("")) {
			article.setContent(null);
		}
		
		// 设置更新时间
		article.setUpdateTimeDate((new Date()));
		
		boolean isAdd = article.getArticleId() == null;
		if (isAdd) {
			// 添加文章
			articleService.addArticle(article);
			addInfoMessageToFlash("添加文章成功！");
		} else {
			// 编辑文章
			articleService.updateArticle(article);
			
			// 更新文章多媒体数量
			articleService.updateArticleMediaCount(article);
			addInfoMessageToFlash("编辑文章成功！");
		}
		
		// 处理分类标签
		List<CategoryTag> selectedCategoryTags = new ArrayList<CategoryTag>();
		for (Object selectedCategoryTag : categoryTagDualListModel.getTarget()) {
			if (selectedCategoryTag instanceof CategoryTag) {
				selectedCategoryTags.add((CategoryTag) selectedCategoryTag);
			} else if (selectedCategoryTag instanceof String) {
				CategoryTag categoryTag = new CategoryTag();
				categoryTag.setCategoryTagId(Integer.parseInt((String) selectedCategoryTag)); 
				selectedCategoryTags.add(categoryTag);
			}
		}
		article.setCategoryTags(selectedCategoryTags);
		articleService.update(article);
		
		// 更新文章JSON文件
		articleService.updateArticleJson(article);
		
		if (!isAdd && getModelId2() != null) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("columnDistributionList.xhtml?multiSelect=true&newsSourceId=" + getModelId1() + "&published=" + (getModelId3() == null ? "" : getModelId3()) + "&columnId=" + ((getModelId2() == null || getModelId2().equals("null")) ? "" : getModelId2()));
			} catch (IOException e) {
				e.printStackTrace();
			}	
//			handleNavigation("columnDistributionList.xhtml?multiSelect=true&newsSourceId=" + getModelId1() + "&published=" + (getModelId3() == null ? "" : getModelId3()) + "&columnId=" + (getModelId2() == null || getModelId2().equals("null") ? "" : getModelId2()));
		} else {
			handleNavigation("articleList.xhtml?multiSelect=true");
		}
	}

	/**
	 * 管理多媒体
	 */
	public void manageMediasAction() {
		handleNavigation("mediaList.xhtml?articleId=" + getArticle().getArticleId() + "&newsSourceId=" + getModelId1() + "&published=" + (getModelId3() == null ? "" : getModelId3()) + "&columnId=" + (getModelId2() == null ? "" : getModelId2()));
	}
	
	/**
	 * 审稿通过
	 */
	public void reviewAction() {
		Article article = getArticle();
		if (article.getArticleId() != null && getNewsSource().getReviewEnabled() && article.getReviewTime() == null) {
			// 设置审稿人信息
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetail) {
				UserDetail userDetail = (UserDetail) principal;
				article.setReviewer(userDetail.getRealName());
				article.setReviewerId(userDetail.getUserId());
			} else {
				article.setReviewer((String) principal);
				article.setReviewerId(0);
			}
			article.setReviewTimeDate((new Date()));
			
			// 更新文章
			articleService.update(article);
			
			handleNavigation("articleList.xhtml?multiSelect=true&newsSourceId=" + getModelId1());
		}
	}
	
	/**
	 * 撤销审稿
	 */
	public void revokeAction() {
		Article article = getArticle();
		if (article.getArticleId() != null && getNewsSource().getReviewEnabled() && article.getReviewTime() != null) {
			// 如果包含分发，则返回
			if (articleService.loadById(article.getArticleId(), "distributions").getDistributions().size() > 0) {
				addValidatingMessage( "文章已包含分发，无法撤销审稿，请删除所有分发后重新尝试！");
				return;
			}
			
			// 设置审稿人信息
			article.setReviewer(null);
			article.setReviewerId(null);
			article.setReviewTime(null);
			
			// 更新文章
			Article articleUpdated = articleService.update(article);
			if (articleUpdated != null) {
				addInfoMessage("撤销审稿成功！");
			} else {
				addValidatingMessage("撤销审稿失败，请重新尝试！");
			}
		}
	}
}
