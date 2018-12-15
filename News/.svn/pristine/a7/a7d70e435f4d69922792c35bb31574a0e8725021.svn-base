/**
 * 
 */
package com.cccollector.news.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.ReorderEvent;

import com.cccollector.news.model.Column;
import com.cccollector.news.model.Distribution;
import com.cccollector.news.model.Recommend;
import com.cccollector.news.model.RecommendGroup;
import com.cccollector.news.model.User;
import com.cccollector.universal.dao.QueryField;

/**
 * 推荐列表Bean类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@ManagedBean
@ViewScoped
public class RecommendListBean extends BaseListBean<Recommend> implements Serializable {

	private static final long serialVersionUID = -3311868921459638918L;

	public RecommendListBean () {
		modelDisplayName = "推荐";
		modelAttributeName = "recommend";
		modelIdAttributeName = "recommendId";
		useDialog = false;
		genericListBeanHandler = new GenericListBeanHandlerRelatedAll<Recommend>() {

			@Override
			public List<Recommend> loadAllModelList() {
				if (getRecommendGroup() == null) {
					return null;
				}
				List<QueryField> predicateQueryFieldList = new ArrayList<QueryField>();
				predicateQueryFieldList.add(new QueryField("recommendGroup", new QueryField("recommendGroupId", getRecommendGroup().getRecommendGroupId())));
				List<QueryField> orderQueryFieldList = new ArrayList<QueryField>();
				orderQueryFieldList.add(new QueryField("position", true));
				List<Recommend> allRecommends = recommendService.loadAll(predicateQueryFieldList, orderQueryFieldList);
				
				// 应用模式
				if (_mode != null) {
					List<Recommend> recommendsToRemove = new ArrayList<Recommend>();
					for (Recommend recommend : allRecommends) {
						if (getModeEnum() == ModeEnum.发布 && recommend.getStatusEnum() != Recommend.StatusEnum.已发布) {
							recommendsToRemove.add(recommend);
						} else if (getModeEnum() == ModeEnum.预览) {
							if (recommend.getStatusEnum() == Recommend.StatusEnum.未发布 && recommend.getValidating() == false) {
								recommendsToRemove.add(recommend);
							} else if (recommend.getStatusEnum() == Recommend.StatusEnum.已发布 && recommend.getValidating() == true) {
								recommendsToRemove.add(recommend);
							} else if (recommend.getStatusEnum() == Recommend.StatusEnum.撤销发布) {
								recommendsToRemove.add(recommend);
							}
						}
					}
					allRecommends.removeAll(recommendsToRemove);
					if (allRecommends.size() > getRecommendGroup().getRecommendCount()) {
						allRecommends = allRecommends.subList(0, getRecommendGroup().getRecommendCount());
					}
				}
				
				// 设置关联内容
				for (Recommend recommend : allRecommends) {
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
				}
				return allRecommends;
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
	 * 推荐组
	 */
	public RecommendGroup getRecommendGroup() {
		return (RecommendGroup) relatedModel(1);
	}
	
	private Integer _mode;

	/**
	 * 模式
	 */
	public Integer getMode() {
		return _mode;
	}

	public void setMode(Integer mode) {
		_mode = mode;
	}

	/**
	 * 模式枚举
	 */
	public static enum ModeEnum {
		发布,
		预览
	}

	/**
	 * 模式枚举数组
	 */
	public ModeEnum[] getModeEnums() {
		return ModeEnum.values();
	}	

	/**
	 * 模式的枚举
	 */
	public ModeEnum getModeEnum() {
		return _mode != null ? ModeEnum.values()[_mode] : null;
	}

	/**
	 * 选择模式
	 */
	public void selectModeAction() {
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 加载推荐图片缩略图
	 */
	public String loadPictureThumbnail(Recommend recommend) {
		if (getRecommendGroup().getPictureRatio() != null) {
			return recommendService.getPictureThumbnailRecommendsUrl(recommend);
		}
		return null;
	}

	/**
	 * 移动推荐
	 */
	public void moveRecommendAction(ReorderEvent event) {
		// 移动推荐
		recommendService.moveRecommends(allModels);
		addInfoMessage("移动推荐成功！");

		// 刷新全部模型
		allModels = null;
	}
	
	/**
	 * 删除推荐
	 */
	public void deleteRecommendAction(Recommend recommend) {
		// 推荐如果已发布，则不能被删除
		if (recommend.getStatusEnum() == Recommend.StatusEnum.已发布) {
			addErrorMessage("要删除的推荐已发布，请撤销发布后再进行删除！");
			return;
		}

		// 删除推荐
		recommendService.deleteRecommend(recommend);
		addInfoMessage("删除推荐成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 批量删除推荐
	 */
	public void deleteRecommendsAction() {
		if (getSelectedModels() == null || getSelectedModels().size() == 0) {
			addErrorMessage("请选择一个或多个推荐进行删除！");
			return;
		}
		// 推荐如果已发布，则不能被删除
		for (Recommend recommend : getSelectedModels()) {
			if (recommend.getStatusEnum() == Recommend.StatusEnum.已发布) {
				addErrorMessage("要删除的推荐中包含已发布推荐，请撤销发布后再进行删除！");
				return;
			}
		}
		
		// 删除推荐图片文件和目录
		for (Recommend recommend : getSelectedModels()) {
			// 删除推荐
			recommendService.deleteRecommend(recommend);
		}		
		addInfoMessage("删除推荐成功！");
		
		// 清空选中的模型
		setSelectedModels(null);
		// 刷新全部模型
		allModels = null;
	}

	/**
	 * 修改推荐是否正在验证
	 */
	public void modifyRecommendValidatingAction(Recommend recommend) {
		if (recommend.getStatusEnum() != Recommend.StatusEnum.未发布 && recommend.getStatusEnum() != Recommend.StatusEnum.已发布) {
			return;
		}
		
		// 更新推荐是否正在验证
		recommendService.update(recommend, "validating");
		addInfoMessage("修改推荐是否正在验证成功！");
	}
	
	/**
	 * 前进推荐状态
	 */
	public void forwardRecommendStatusAction(Recommend recommend) {
		if (recommend.getStatusEnum() != Recommend.StatusEnum.未发布 && recommend.getStatusEnum() != Recommend.StatusEnum.已发布) {
			return;
		}
		
		// 设置状态
		recommend.setStatus(recommend.getStatus() + 1);
		
		// 设置是否正在验证
		recommend.setValidating(false);
		
		// 更新推荐
		recommendService.update(recommend, "status", "validating");
		addInfoMessage(recommend.getStatusEnum() == Recommend.StatusEnum.已发布 ? "发布推荐成功！" : "撤销发布推荐成功！");
	}
}
